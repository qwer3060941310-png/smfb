/*
 * Round-trip test for com.desertstormfront.io.WorldSerializer (T03/T04).
 *
 * WorldSerializer is the save-game codec: a World is written to a DataWriter and read back from a
 * DataReader. Its load path parses data the game does not control (save files), and it indexes
 * straight into enum arrays and unit/player lists, so both fidelity and malformed-input behaviour
 * matter.
 *
 * This test builds a minimal head-less World (same harness as ModGameplayProbeTest), serialises it
 * to memory, deserialises it and asserts the surviving invariants, then checks that a truncated
 * stream fails loudly rather than yielding a broken World.
 *
 * Head-less constraints (see ModGameplayProbeTest):
 *  - GameConfig.configure(...) must run before World.create() (UserConfig static init needs it).
 *  - debugEnabled MUST stay false (OsfLog -> Gdx.app is null here).
 *  - world.buildTerrain(...) must run: TerrainGrid direction fields and fog size depend on it.
 *
 * Runs head-less, so it is part of run\dev.ps1 test. Exit code 0 and "WORLDSERIALIZER PASSED" mean
 * success.
 */
package com.desertstormfront.test;

import com.desertstormfront.app.support.ProgressCallback;
import com.desertstormfront.config.GameConfig;
import com.desertstormfront.game.ScenarioType;
import com.desertstormfront.game.World;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.game.player.Controller;
import com.desertstormfront.game.player.FogOfWar;
import com.desertstormfront.game.player.Player;
import com.desertstormfront.game.player.PlayerStatistics;
import com.desertstormfront.io.WorldSerializer;
import com.desertstormfront.map.MapDefinition;
import com.noblemaster.lib.data.DateTime;
import com.noblemaster.lib.data.Version;
import com.noblemaster.lib.io.stream.impl.StreamDataReader;
import com.noblemaster.lib.io.stream.impl.StreamDataWriter;
import com.noblemaster.lib.util.FastRandom;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public final class WorldSerializerRoundTripTest {

    private static final int MAP_SIZE = 48;
    private static final int PLAYERS = 2;
    private static final long SEED = 20260926L;

    private WorldSerializerRoundTripTest() {
    }

    public static void main(String[] args) {
        try {
            run();
        } catch (Throwable t) {
            System.out.println("WORLDSERIALIZER FAILED unexpected=" + t);
            t.printStackTrace(System.out);
            System.exit(1);
        }
    }

    private static void run() {
        // debugEnabled = false keeps OsfLog (-> Gdx.app) off this path.
        GameConfig.configure("tsf", new Version("1.0.0"), new DateTime(0L),
                false, false, 800, 600, false);

        MapDefinition ruleset = MapDefinition.getRuleset("tsf");
        if (ruleset == null) {
            System.out.println("WORLDSERIALIZER FAILED ruleset 'tsf' is missing");
            System.exit(1);
        }

        World world = World.create(ruleset, "roundtrip", ScenarioType.Supremacy, MAP_SIZE, MAP_SIZE, PLAYERS);
        world.setRandom(new FastRandom(SEED));
        world.buildTerrain(new ProgressCallback() {
            @Override
            public boolean onProgress(float progress) {
                return true;
            }
        });
        world.setSpeedFactor(1.0f);

        // World.create() builds players via Player.create() but leaves the controller unset (the game
        // fills it in from the lobby), and the serializer writes the controller ordinal. Fill in the
        // few fields the writer dereferences so the save path has a fully wired world.
        for (int i = 0; i < world.getPlayers().size(); ++i) {
            Player p = (Player) world.getPlayers().get(i);
            if (p.getController() == null) {
                p.setController(i == 0 ? Controller.Human : Controller.AiNormal);
            }
            if (p.getAvailableUnitTypes() == null) {
                p.setAvailableUnitTypes(ruleset.getUnitTypes());
            }
            if (p.getFogOfWar() == null) {
                p.setFogOfWar(new FogOfWar(MAP_SIZE / 4, MAP_SIZE / 4));
            }
            if (p.getStatistics() == null) {
                p.setStatistics(new PlayerStatistics());
            }
        }

        Player p0 = (Player) world.getPlayers().get(0);
        Player p1 = (Player) world.getPlayers().get(1);
        UnitType tank = ruleset.getUnitTypeSlots().getBattleTank();
        Unit u0 = world.spawnUnit(tank, 10.0f, 10.0f, p0);
        Unit u1 = world.spawnUnit(tank, 12.0f, 10.0f, p1);
        u0.setHealth(77);
        u1.setHealth(33);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        StreamDataWriter writer = new StreamDataWriter(buffer);
        WorldSerializer.write(writer, world);
        writer.flush();
        byte[] bytes = buffer.toByteArray();

        // --- round-trip invariants ----------------------------------------------
        World back = WorldSerializer.read(new StreamDataReader(new ByteArrayInputStream(bytes)));
        Assert.check("world decoded (non-null)", back != null);
        Assert.check("map definition kept",
                back.getMapDefinition() != null && back.getMapDefinition().getId().equals(world.getMapDefinition().getId()));
        Assert.equal("name kept", back.getName(), world.getName());
        Assert.equal("description kept", back.getDescription(), world.getDescription());
        Assert.check("game time kept", back.getGameTime() == world.getGameTime());
        Assert.check("turn kept", back.getTurn() == world.getTurn());
        Assert.check("player count kept", back.getPlayers().size() == world.getPlayers().size());
        Assert.check("unit count kept", back.getUnits().size() == world.getUnits().size());

        boolean unitsMatch = back.getUnits().size() == world.getUnits().size();
        if (unitsMatch) {
            for (int i = 0; i < world.getUnits().size(); ++i) {
                Unit original = (Unit) world.getUnits().get(i);
                Unit restored = (Unit) back.getUnits().get(i);
                if (restored.getUnitType() != original.getUnitType()
                        || restored.getHealth() != original.getHealth()
                        || restored.getPosition().getX() != original.getPosition().getX()
                        || restored.getPosition().getY() != original.getPosition().getY()) {
                    unitsMatch = false;
                    break;
                }
            }
        }
        Assert.check("units kept (type/health/position)", unitsMatch);

        // --- truncated payload fails loudly -------------------------------------
        byte[] truncated = new byte[Math.max(1, bytes.length / 3)];
        System.arraycopy(bytes, 0, truncated, 0, truncated.length);
        boolean threw = false;
        try {
            WorldSerializer.read(new StreamDataReader(new ByteArrayInputStream(truncated)));
        } catch (Throwable t) {
            threw = true;
        }
        Assert.check("truncated stream fails loudly", threw);

        // --- a corrupt index must fail with a readable message (T04 bounds check) --
        // Header + version + map id + name + description + seed + time + turn + player count
        // + an out-of-range controller ordinal: the reader must reject the last one by name.
        ByteArrayOutputStream badBuffer = new ByteArrayOutputStream();
        StreamDataWriter bad = new StreamDataWriter(badBuffer);
        bad.writeBoolean(true);
        bad.writeInt(9);
        bad.writeString("tsf");
        bad.writeString("name");
        bad.writeString("desc");
        bad.writeLong(0L);
        bad.writeFloat(0.0f);
        bad.writeInt(0);
        bad.writeInt(1);
        bad.writeInt(99);
        bad.flush();
        String corruptMessage = null;
        try {
            WorldSerializer.read(new StreamDataReader(new ByteArrayInputStream(badBuffer.toByteArray())));
        } catch (Throwable t) {
            corruptMessage = String.valueOf(t.getMessage());
        }
        Assert.check("corrupt index rejected", corruptMessage != null && corruptMessage.contains("Corrupt save"));
        Assert.check("corrupt index message names the field", corruptMessage != null && corruptMessage.contains("controller"));

        Assert.report("WORLDSERIALIZER", "round-trip/units/truncation/corruptIndex ok bytes=" + bytes.length);
    }
}
