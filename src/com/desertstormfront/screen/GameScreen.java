/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.desertstormfront.ai.AIPlanner;
import com.desertstormfront.ai.intent.Intent;
import com.desertstormfront.ai.intent.IntentGroup;
import com.desertstormfront.ai.intent.IntentGroupList;
import com.desertstormfront.ai.plan.PlanContext;
import com.desertstormfront.ai.plan.PlanNode;
import com.desertstormfront.app.GameApplication;
import com.desertstormfront.app.support.BackgroundTask;
import com.desertstormfront.app.support.IsoMathHelper;
import com.desertstormfront.audio.AudioClip;
import com.desertstormfront.audio.GameAudio;
import com.desertstormfront.audio.MusicPlaylist;
import com.desertstormfront.command.DirectUnitCommander;
import com.desertstormfront.command.NetworkedUnitCommander;
import com.desertstormfront.command.UnitCommander;
import com.desertstormfront.config.GameConfig;
import com.desertstormfront.config.TouchDeviceFlags;
import com.desertstormfront.config.UserConfig;
import com.desertstormfront.game.GameEventListener;
import com.desertstormfront.game.World;
import com.desertstormfront.game.WorldSimulator;
import com.desertstormfront.game.mode.CaptureTheFlagMode;
import com.desertstormfront.game.mode.EscortMode;
import com.desertstormfront.game.mode.GameMode;
import com.desertstormfront.game.model.AmmoType;
import com.desertstormfront.game.model.Bird;
import com.desertstormfront.game.model.BirdList;
import com.desertstormfront.game.model.BirdType;
import com.desertstormfront.game.model.Domain;
import com.desertstormfront.game.model.DrawQueue;
import com.desertstormfront.game.model.EffectTimer;
import com.desertstormfront.game.model.Layer;
import com.desertstormfront.game.model.PositionedSortable;
import com.desertstormfront.game.model.Projectile;
import com.desertstormfront.game.model.ProjectileList;
import com.desertstormfront.game.model.Site;
import com.desertstormfront.game.model.Sortable;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.game.model.UnitOrderMode;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.game.model.Volley;
import com.desertstormfront.game.player.Controller;
import com.desertstormfront.game.player.FogOfWar;
import com.desertstormfront.game.player.Player;
import com.desertstormfront.game.player.PlayerList;
import com.desertstormfront.game.player.PlayerStatistics;
import com.desertstormfront.game.player.UnitGroup;
import com.desertstormfront.graphics.table.BuildingSpriteTable;
import com.desertstormfront.graphics.table.SpriteAnimationSet;
import com.desertstormfront.graphics.table.SpriteRegion;
import com.desertstormfront.graphics.table.UnitSpriteAnimation;
import com.desertstormfront.graphics.table.UnitSpriteTable;
import com.desertstormfront.io.SpriteSheetLoader;
import com.desertstormfront.screen.BaseScreen;
import com.desertstormfront.screen.BriefingAcceptListener;
import com.desertstormfront.screen.BuildPanelListener;
import com.desertstormfront.screen.CampaignScreen;
import com.desertstormfront.screen.GameEventSoundListener;
import com.desertstormfront.screen.GameInputProcessor;
import com.desertstormfront.screen.GameOverDismissListener;
import com.desertstormfront.screen.GameScene;
import com.desertstormfront.screen.InGameMenuButtonListener;
import com.desertstormfront.screen.LanNoticeListener;
import com.desertstormfront.screen.LoadGameScreen;
import com.desertstormfront.screen.MainMenuScreen;
import com.desertstormfront.screen.MultiSelectPanelListener;
import com.desertstormfront.screen.MultiplayerLobbyScreen;
import com.desertstormfront.screen.PauseMenuListener;
import com.desertstormfront.screen.PlayerSlotListener;
import com.desertstormfront.screen.ProductionPanelListener;
import com.desertstormfront.screen.QuitConfirmListener;
import com.desertstormfront.screen.RestartConfirmListener;
import com.desertstormfront.screen.SkirmishSetupScreen;
import com.desertstormfront.screen.SquadBarListener;
import com.desertstormfront.screen.UnitPanelListener;
import com.desertstormfront.screen.ZoomButtonListener;
import com.desertstormfront.session.SessionMode;
import com.desertstormfront.session.impl.CampaignGameMode;
import com.desertstormfront.session.impl.MultiplayerGameMode;
import com.desertstormfront.session.impl.SkirmishGameMode;
import com.desertstormfront.session.impl.TutorialGameMode;
import com.desertstormfront.ui.ActionListener;
import com.desertstormfront.ui.BatchRenderer;
import com.desertstormfront.ui.Container;
import com.desertstormfront.ui.GlBuffer;
import com.desertstormfront.ui.GlTexture;
import com.desertstormfront.ui.Label;
import com.desertstormfront.ui.ShaderProgram;
import com.desertstormfront.ui.VectorTextRenderer;
import com.desertstormfront.ui.hud.BuildingPanel;
import com.desertstormfront.ui.hud.CarrierUnitPanel;
import com.desertstormfront.ui.hud.GroupBar;
import com.desertstormfront.ui.hud.GuiAssets;
import com.desertstormfront.ui.hud.InGameMenu;
import com.desertstormfront.ui.hud.LoadingProgressDialog;
import com.desertstormfront.ui.hud.MessageDialog;
import com.desertstormfront.ui.hud.MinimapPanel;
import com.desertstormfront.ui.hud.MinimapRenderer;
import com.desertstormfront.ui.hud.MissionBriefingDialog;
import com.desertstormfront.ui.hud.NationSelectDialog;
import com.desertstormfront.ui.hud.ResultBanner;
import com.desertstormfront.ui.hud.ScoreDialog;
import com.desertstormfront.ui.hud.SingleUnitPanel;
import com.desertstormfront.ui.hud.SquadPanel;
import com.desertstormfront.ui.hud.TimeStatusBar;
import com.desertstormfront.ui.hud.TutorialHintBox;
import com.desertstormfront.world.Neighbor;
import com.desertstormfront.world.RoadTile;
import com.desertstormfront.world.TerrainGrid;
import com.desertstormfront.world.UnitPosition;
import com.desertstormfront.world.Vec2;
import com.desertstormfront.world.Vec3;
import com.noblemaster.lib.data.DateTime;
import com.noblemaster.lib.i18n.Messages;
import com.noblemaster.lib.io.GameFile;
import com.noblemaster.lib.log.OsfLog;
import com.noblemaster.lib.math.MathHelper;
import com.noblemaster.lib.util.FastRandom;
import com.noblemaster.lib.util.HashUtils;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Collection;

public final class GameScreen
extends BaseScreen {
    private final int[] playerColors = new int[]{-2097152, -16776992, -2096928, -2039808, -16719872, -16719648, -2039584, -16777216};
    private final int b = 25;
    private final float c = 0.2f;
    private final float d = 0.9f;
    private long sessionStartTime;
    private SessionMode gameMode;
    private InputProcessor inputMultiplexer;
    private World world;
    private BackgroundTask backgroundTask;
    private boolean loadingComplete;
    private GameEventListener gameEventListener;
    private float spectatingSince;
    private Vec2 escortTargetArea;
    private BuildingSpriteTable buildingSpriteTable;
    private UnitSpriteTable unitSpriteTable;
    private UnitCommander unitCommander;
    private AIPlanner[] aiPlanners;
    private int aiUpdateCount;
    private float aiElapsedTime;
    private GameScene scene;
    private UnitList selectedUnits;
    private UnitList selectionScratch;
    private final long w = 1000000000L;
    private long lastTapTime;
    private boolean doubleTap;
    private boolean pointerHeld;
    private UnitList hoveredUnits;
    private float pointerTileX;
    private float pointerTileY;
    private float clickMarkerUntilTime;
    private Unit clickedUnit;
    private int clickMarkerScreenX;
    private int clickMarkerScreenY;
    private int visibleMinTileX;
    private int visibleMinTileY;
    private int visibleMaxTileX;
    private int visibleMaxTileY;
    private final float L = 0.5f;
    private final float M = 0.5f;
    private final float N = 1.0f;
    private float cameraZoom;
    private float targetCameraZoom;
    private int frameCount;
    /** Sound bank (music playlist + every cue) - see {@link com.desertstormfront.audio.GameAudio}. */
    private GameAudio audio;
    private long X;
    private float ae;
    private Vec3 cameraPosition;
    private float at;
    private float au;
    private final int av = 128;
    private final int aw = 64;
    private final int zPixelScale = MathHelper.round(64.0f / MathHelper.sqrt(2.0f));
    private int minimapWidth;
    private int minimapHeight;
    private GlTexture tilesTexture;
    private GlTexture unitsTexture;
    private final short aC = (short)16;
    private ShaderProgram spriteShaderProgram;
    private int positionAttribute;
    private int texCoordAttribute;
    private int translateUniform;
    private int textureUniform;
    private int mvpMatrixUniform;
    private boolean useFloatVertices;
    private GlBuffer terrainVertexBuffer;
    private GlBuffer terrainIndexBuffer;
    private short[] spriteVertexData;
    private int spriteVertexOffset;
    private int spriteVertexCapacity;
    private int spriteBufferIndex;
    private GlBuffer[] spriteVertexBuffers;
    private GlBuffer[] spriteIndexBuffers;
    private GlBuffer lineVertexBuffer;
    private GlBuffer lineIndexBuffer;
    private GlBuffer fogVertexBuffer;
    private GlBuffer fogIndexBuffer;
    private GlBuffer markerVertexBuffer;
    private GlBuffer markerIndexBuffer;
    private VectorTextRenderer vectorTextRenderer;
    private BatchRenderer hudRenderer;
    private MessageDialog lanNoticeDialog;
    private ActionListener lanNoticeListener;
    private LoadingProgressDialog loadingProgressDialog;
    private LoadingProgressDialog networkProgressDialog;
    private InGameMenu inGameMenu;
    private ActionListener pauseMenuListener;
    private NationSelectDialog nationSelectDialog;
    private ActionListener playerSlotListener;
    private MissionBriefingDialog missionBriefingDialog;
    private ActionListener briefingAcceptListener;
    private ResultBanner resultBanner;
    private ScoreDialog scoreDialog;
    private ActionListener gameOverDismissListener;
    private MessageDialog restartDialog;
    private ActionListener restartConfirmListener;
    private MessageDialog quitDialog;
    private ActionListener quitConfirmListener;
    private TimeStatusBar timeStatusBar;
    private ActionListener inGameMenuButtonListener;
    private MinimapRenderer minimapRenderer;
    private MinimapPanel minimapPanel;
    private Label zoomLabel;
    private ActionListener zoomButtonListener;
    private Label fpsLabel;
    private SingleUnitPanel singleUnitPanel;
    private ActionListener unitPanelListener;
    private CarrierUnitPanel carrierUnitPanel;
    private ActionListener productionPanelListener;
    private BuildingPanel buildingPanel;
    private ActionListener buildPanelListener;
    private SquadPanel squadPanel;
    private ActionListener squadBarListener;
    private GroupBar groupBar;
    private ActionListener multiSelectPanelListener;
    private TutorialHintBox tutorialHintBox;
    private final int bJ = Integer.MIN_VALUE;
    private long gameTimeNanos;
    private long gameTimeMillis;
    private long bM;
    private boolean pointerDown;
    private int pointerIndex;
    private long[] pointerDownTimes;
    private int[] pointerX;
    private int[] pointerY;
    private int pressX;
    private int pressY;
    private boolean dragging;
    private int boxSelectStartX;
    private int boxSelectStartY;
    private int[] domainCounts;
    private float dragStartCameraX;
    private float dragStartCameraY;
    private float dragDx;
    private float dragDy;
    private boolean keyboardScrolling;
    private boolean cd;
    private boolean ce;
    private boolean cf;
    private int actionKeyCode;
    private DrawQueue underLayerQueue;
    private DrawQueue baseLayerQueue;
    private DrawQueue upperLayerQueue;
    private FastRandom random;
    private final long cl = 1000000000L;
    private long[] waterSparkleTimes;
    private int[] waterSparkleX;
    private int[] waterSparkleY;
    private static /* synthetic */ int[] cp;
    private static /* synthetic */ int[] cq;
    private static /* synthetic */ int[] cr;
    private static /* synthetic */ int[] cs;

    public GameScreen(SessionMode sessionMode) {
        this.gameMode = sessionMode;
    }

    @Override
    public BaseScreen update() {
        GameMode gameMode;
        int i29;
        int n;
        int n2;
        int n3;
        int n4;
        int n5;
        boolean i9;
        boolean i10;
        boolean i11;
        boolean i8;
        boolean i7;
        Player player = this.unitCommander.getPlayer();
        int i2 = 0;
        boolean i3 = false;
        while (i2 < this.selectedUnits.size()) {
            if (((Unit)this.selectedUnits.get(i2)).isDestroyed()) {
                this.selectedUnits.remove(i2);
                i3 = true;
                continue;
            }
            ++i2;
        }
        if (i3 && this.selectedUnits.size() == 0) {
            this.groupBar.collapse();
            if (!this.scene.isPanelOpen()) {
                this.setScene(GameScene.Default);
            }
        }
        float f4 = Gdx.graphics.getRawDeltaTime();
        long l5 = this.gameTimeNanos + (long)(f4 * 1.0E9f);
        Input input = Gdx.input;
        if (TouchDeviceFlags.isTouchDevice()) {
            if (UserConfig.isMouseControl()) {
                if (this.isTouchDown()) {
                    i7 = true;
                    i8 = false;
                    i11 = false;
                } else if (this.isTouchUp()) {
                    i7 = true;
                    i8 = true;
                    i11 = true;
                } else {
                    i7 = false;
                    i8 = this.pointerDown ? this.cd : false;
                    i11 = this.pointerDown ? this.cf : false;
                }
                i10 = this.selectedUnits.size() > 0 && (input.isKeyPressed(59) || input.isKeyPressed(60));
                i9 = false;
            } else {
                i7 = this.isTouchDown();
                i8 = true;
                i10 = false;
                i11 = false;
                i9 = this.isTouchUp();
                if (i9) {
                    this.dragging = false;
                } else if (this.ce) {
                    this.clearSelection();
                    i7 = false;
                }
            }
        } else {
            i7 = this.isTouchDown();
            i8 = true;
            i9 = false;
            i10 = false;
            i11 = false;
        }
        int i13 = MathHelper.round((float)this.getTouchX() * GameApplication.scale);
        int i14 = MathHelper.round((float)this.getTouchY() * GameApplication.scale);
        if (!(i7 || this.pointerDown || TouchDeviceFlags.isTouchDevice())) {
            i13 = Integer.MIN_VALUE;
            i14 = Integer.MIN_VALUE;
        }
        int i15 = (int)((float)i13 / this.cameraZoom);
        int i16 = (int)((float)i14 / this.cameraZoom);
        if (!this.tutorialHintBox.isWaitingForConfirmation()) {
            int n6;
            boolean bl;
            boolean bl2 = bl = this.isSelectionCommandable() && i8 || i10 && i8;
            if (i7) {
                if (!this.scene.isPanelOpen()) {
                    int n7;
                    if (!this.pointerDown) {
                        this.pressX = i15;
                        this.pressY = i16;
                        this.dragStartCameraX = this.cameraPosition.getX();
                        this.dragStartCameraY = this.cameraPosition.getY();
                        this.pointerHeld = true;
                        this.doubleTap = l5 - this.lastTapTime < 400000000L;
                        this.lastTapTime = l5;
                        if (TouchDeviceFlags.isTouchDevice() && UserConfig.isMouseControl() && !i10) {
                            if (input.isKeyPressed(59) || input.isKeyPressed(60)) {
                                this.groupBar.collapse();
                                this.selectedUnits.clear();
                                this.setScene(GameScene.Default);
                                this.dragging = true;
                            } else if (input.isKeyPressed(129) || input.isKeyPressed(130)) {
                                this.groupBar.collapse();
                                this.dragging = true;
                            }
                        }
                    } else if (!(this.dragging || TouchDeviceFlags.isTouchDevice() && !UserConfig.isTouchScroll())) {
                        n7 = i15 - this.pressX;
                        int n8 = i16 - this.pressY;
                        this.setCameraPosition(this.dragStartCameraX + (float)n7, this.dragStartCameraY + (float)n8);
                    }
                    n7 = 0;
                    if (!this.dragging && this.minimapPanel.isExpanded()) {
                        float f = (float)i13 - this.minimapPanel.getX() - 22.0f - 110.0f;
                        float f2 = i14 - 10;
                        float f3 = IsoMathHelper.toTileX(f, f2, 220, 110);
                        float f5 = IsoMathHelper.toTileY(f, f2, 220, 110);
                        if (f3 >= 0.0f && f3 < 1.0f && f5 >= 0.0f && f5 < 1.0f) {
                            n5 = (int)(f3 * (float)this.world.getTerrainGrid().getWidth());
                            n6 = (int)(f5 * (float)this.world.getTerrainGrid().getHeight());
                            n4 = this.getWidth();
                            n3 = this.getHeight();
                            this.setCameraPosition((float)(n4 / 2) - IsoMathHelper.toScreenX(n5, n6, 128), (float)(n3 / 2) - IsoMathHelper.toScreenY(n5, n6, 64));
                            n7 = 1;
                        }
                    }
                    if (n7 == 0 && this.groupBar.contains(this.pressX, this.pressY)) {
                        n7 = 1;
                        this.dragging = false;
                    }
                    if (n7 == 0) {
                        int n9;
                        int n10;
                        World world = this.unitCommander.getWorld();
                        TerrainGrid terrainGrid = world.getTerrainGrid();
                        int n11 = terrainGrid.getWidth();
                        int n12 = terrainGrid.getHeight();
                        float f = -this.cameraPosition.getX() + (float)i15;
                        float f6 = -this.cameraPosition.getY() + (float)i16;
                        this.pointerTileX = IsoMathHelper.toTileX(f, f6, 128, 64);
                        this.pointerTileY = IsoMathHelper.toTileY(f, f6, 128, 64);
                        Unit unit = null;
                        float f7 = 0.7f;
                        n2 = (int)this.pointerTileX;
                        n = (int)this.pointerTileY;
                        i29 = 2;
                        int n13 = n - i29;
                        while (n13 <= n + i29) {
                            n10 = n2 - i29;
                            while (n10 <= n2 + i29) {
                                if (n10 >= 0 && n13 >= 0 && n10 < n11 && n13 < n12) {
                                    Object object;
                                    float f8;
                                    Unit unit2 = terrainGrid.getUnitAtTile(n10, n13);
                                    if (unit2 != null && (f8 = ((Vec2)(object = unit2.getPosition())).distanceTo(this.pointerTileX, this.pointerTileY)) < f7) {
                                        unit = unit2;
                                        f7 = f8;
                                    }
                                    object = terrainGrid.getUnitsAtTile(n10, n13);
                                    n9 = ((ArrayList)object).size();
                                    int n14 = 0;
                                    while (n14 < n9) {
                                        Unit unit3 = (Unit)((ArrayList)object).get(n14);
                                        UnitPosition unitPosition = unit3.getPosition();
                                        float f9 = unit3.getUnitType().getDomain() == Domain.Air ? unitPosition.distanceTo(this.pointerTileX + 0.9f, this.pointerTileY + 0.9f) : unitPosition.distanceTo(this.pointerTileX, this.pointerTileY);
                                        if (f9 < f7 && (bl && unit3.getOwner().isEnemyOf(player) || unit3.getOwner() == player && unit3.getOrderMode() != UnitOrderMode.d)) {
                                            unit = unit3;
                                            f7 = f9;
                                        }
                                        ++n14;
                                    }
                                }
                                ++n10;
                            }
                            ++n13;
                        }
                        if (unit != null) {
                            if (bl && this.selectedUnits.contains(unit)) {
                                unit = null;
                            } else if (!this.unitCommander.getPlayer().getFogOfWar().isUnitVisible(unit)) {
                                unit = null;
                            } else if (this.selectedUnits.size() > 0 && bl && ((Unit)this.selectedUnits.get(0)).canGuard(unit) && !unit.canEmbark((Unit)this.selectedUnits.get(0))) {
                                unit = null;
                            } else if (this.selectedUnits.size() > 0 && bl && unit.getOwner() == player && !unit.canEmbark((Unit)this.selectedUnits.get(0))) {
                                unit = null;
                            }
                        }
                        n13 = MathHelper.abs(i15 - this.pressX) < 25 && MathHelper.abs(i16 - this.pressY) < 25 ? 1 : 0;
                        this.hoveredUnits.clear();
                        if (n13 != 0) {
                            if (!this.dragging) {
                                if (!bl && this.pointerHeld && l5 > this.lastTapTime + 1000000000L) {
                                    this.dragging = true;
                                } else if (unit != null) {
                                    this.hoveredUnits.add(unit);
                                }
                            }
                        } else {
                            this.pointerHeld = false;
                        }
                        if ((this.dragging || TouchDeviceFlags.isTouchDevice() && !UserConfig.isTouchScroll() && n13 == 0) && !bl) {
                            int n15;
                            int n16;
                            if (TouchDeviceFlags.isTouchDevice() && UserConfig.isMouseControl()) {
                                this.groupBar.collapse();
                                if (!input.isKeyPressed(129) && !input.isKeyPressed(130)) {
                                    this.selectedUnits.clear();
                                    this.setScene(GameScene.Default);
                                }
                            }
                            this.dragging = true;
                            this.boxSelectStartX = i15;
                            this.boxSelectStartY = i16;
                            if (this.pressX < this.boxSelectStartX) {
                                n10 = this.pressX;
                                n16 = this.boxSelectStartX;
                            } else {
                                n10 = this.boxSelectStartX;
                                n16 = this.pressX;
                            }
                            if (this.pressY < this.boxSelectStartY) {
                                n15 = this.pressY;
                                n9 = this.boxSelectStartY;
                            } else {
                                n15 = this.boxSelectStartY;
                                n9 = this.pressY;
                            }
                            n10 = (int)((float)n10 - this.cameraPosition.getX());
                            n16 = (int)((float)n16 - this.cameraPosition.getX());
                            n15 = (int)((float)n15 - this.cameraPosition.getY());
                            n9 = (int)((float)n9 - this.cameraPosition.getY());
                            UnitList unitList = world.getUnits();
                            int n17 = unitList.size();
                            int n18 = 0;
                            while (n18 < this.domainCounts.length) {
                                this.domainCounts[n18] = 0;
                                ++n18;
                            }
                            n18 = 0;
                            while (n18 < n17) {
                                Unit unit4 = (Unit)unitList.get(n18);
                                if (!unit4.isImmobile() && unit4.getPosition().getUnit() == null && unit4.getOwner() == player && !unit4.getUnitType().isFlying()) {
                                    UnitPosition unitPosition = unit4.getPosition();
                                    int n19 = MathHelper.round(IsoMathHelper.toScreenX(unitPosition.getX(), unitPosition.getY(), 128));
                                    int n20 = MathHelper.round(IsoMathHelper.toScreenY(unitPosition.getX(), unitPosition.getY(), 64));
                                    if (n19 >= n10 && n19 <= n16 && n20 >= n15 && n20 <= n9) {
                                        int n21 = unit4.getUnitType().getDomain().ordinal();
                                        this.domainCounts[n21] = this.domainCounts[n21] + 1;
                                    }
                                }
                                ++n18;
                            }
                            n18 = this.domainCounts[0];
                            Domain domain = Domain.values()[0];
                            int n22 = 1;
                            while (n22 < this.domainCounts.length) {
                                if (this.domainCounts[n22] > n18) {
                                    n18 = this.domainCounts[n22];
                                    domain = Domain.values()[n22];
                                }
                                ++n22;
                            }
                            n22 = 0;
                            while (n22 < n17) {
                                Unit unit5 = (Unit)unitList.get(n22);
                                if (!unit5.isImmobile() && unit5.getPosition().getUnit() == null && unit5.getOwner() == player && unit5.getUnitType().getDomain() == domain && !unit5.getUnitType().isFlying()) {
                                    UnitPosition unitPosition = unit5.getPosition();
                                    int i42 = MathHelper.round(IsoMathHelper.toScreenX(unitPosition.getX(), unitPosition.getY(), 128));
                                    int i43 = MathHelper.round(IsoMathHelper.toScreenY(unitPosition.getX(), unitPosition.getY(), 64));
                                    if (i42 >= n10 && i42 <= n16 && i43 >= n15 && i43 <= n9) {
                                        this.hoveredUnits.add(unit5);
                                    }
                                }
                                ++n22;
                            }
                        }
                    }
                }
            } else if (this.pointerDown) {
                if (this.dragging || MathHelper.abs(i15 - this.pressX) < 25 && MathHelper.abs(i16 - this.pressY) < 25) {
                    int n23;
                    this.dragging = false;
                    if (!bl) {
                        if (this.groupBar.getSelectedGroup() >= 0) {
                            UnitGroup unitGroup = this.unitCommander.getPlayer().getUnitGroups().get(this.groupBar.getSelectedGroup());
                            if (unitGroup.getUnits().size() == 0) {
                                n23 = 0;
                                while (n23 < this.hoveredUnits.size()) {
                                    if (((Unit)this.hoveredUnits.get(n23)).isImmobile()) {
                                        this.hoveredUnits.remove(n23);
                                        continue;
                                    }
                                    ++n23;
                                }
                                if (this.hoveredUnits.size() > 0) {
                                    this.unitCommander.formSquad(unitGroup, this.hoveredUnits);
                                } else {
                                    this.groupBar.collapse();
                                }
                            }
                        } else {
                            this.groupBar.collapse();
                        }
                    }
                    this.keyboardScrolling = false;
                    boolean bl3 = this.isPointOnHud((float)i13, (float)i14);
                    n23 = 0;
                    if (!bl3 && (this.hoveredUnits.size() > 0 || this.world.getTerrainGrid().isInsideGrid(this.pointerTileX, this.pointerTileY))) {
                        if (bl) {
                            UnitList unitList;
                            int n24;
                            boolean bl4;
                            if (this.hoveredUnits.size() > 0) {
                                if (this.scene != GameScene.ForceAttack) {
                                    if (i10) {
                                        bl4 = false;
                                        n24 = 0;
                                        while (n24 < this.selectedUnits.size()) {
                                            unitList = ((Unit)this.selectedUnits.get(n24)).getSubUnits();
                                            if (unitList.size() > 0 && this.unitCommander.targetUnits(unitList, (Unit)this.hoveredUnits.get(0))) {
                                                bl4 = true;
                                            }
                                            ++n24;
                                        }
                                        if (bl4) {
                                            this.selectionScratch.addAll((Collection)this.selectedUnits);
                                            this.selectedUnits.clear();
                                            n24 = 0;
                                            while (n24 < this.selectionScratch.size()) {
                                                this.selectedUnits.addAll((Collection)((Unit)this.selectionScratch.get(n24)).getSubUnits());
                                                ++n24;
                                            }
                                            this.selectionScratch.clear();
                                            this.setScene(GameScene.Attack);
                                        }
                                    } else {
                                        bl4 = this.selectedUnits.size() == 1 ? this.unitCommander.targetUnit((Unit)this.selectedUnits.get(0), (Unit)this.hoveredUnits.get(0)) : this.unitCommander.targetUnits(this.selectedUnits, (Unit)this.hoveredUnits.get(0));
                                    }
                                } else {
                                    bl4 = this.unitCommander.attackUnit((Unit)this.selectedUnits.get(0), (Unit)this.hoveredUnits.get(0));
                                }
                            } else if (this.scene != GameScene.ForceAttack) {
                                if (i10) {
                                    bl4 = false;
                                    n24 = 0;
                                    while (n24 < this.selectedUnits.size()) {
                                        unitList = ((Unit)this.selectedUnits.get(n24)).getSubUnits();
                                        if (unitList.size() > 0 && this.unitCommander.moveUnitsTo(unitList, this.pointerTileX, this.pointerTileY)) {
                                            bl4 = true;
                                        }
                                        ++n24;
                                    }
                                    if (bl4) {
                                        this.selectionScratch.addAll((Collection)this.selectedUnits);
                                        this.selectedUnits.clear();
                                        n24 = 0;
                                        while (n24 < this.selectionScratch.size()) {
                                            this.selectedUnits.addAll((Collection)((Unit)this.selectionScratch.get(n24)).getSubUnits());
                                            ++n24;
                                        }
                                        this.selectionScratch.clear();
                                        this.setScene(GameScene.Attack);
                                    }
                                } else {
                                    bl4 = this.selectedUnits.size() == 1 ? (((Unit)this.selectedUnits.get(0)).getGuardPositionOrNull() != null && ((Unit)this.selectedUnits.get(0)).getPosition().distanceSquaredTo(this.pointerTileX, this.pointerTileY) <= 0.25f ? this.unitCommander.stopUnit((Unit)this.selectedUnits.get(0)) : this.unitCommander.moveUnitTo((Unit)this.selectedUnits.get(0), this.pointerTileX, this.pointerTileY)) : this.unitCommander.moveUnitsTo(this.selectedUnits, this.pointerTileX, this.pointerTileY);
                                }
                            } else {
                                bl4 = this.unitCommander.attackMoveUnitTo((Unit)this.selectedUnits.get(0), this.pointerTileX, this.pointerTileY);
                            }
                            if (bl4) {
                                if (!i11) {
                                    this.setScene(GameScene.Move);
                                    this.groupBar.collapse();
                                }
                                this.audio.playOk();
                            } else {
                                if (this.selectedUnits.size() >= 2) {
                                    this.selectedUnits.clear();
                                    this.groupBar.collapse();
                                    this.setScene(GameScene.Default);
                                } else if (this.selectedUnits.size() == 0) {
                                    this.groupBar.collapse();
                                    this.setScene(GameScene.Default);
                                } else {
                                    this.setScene(GameScene.Build);
                                }
                                this.audio.playInvalid();
                                this.clickMarkerUntilTime = this.world.getGameTime() + 0.2f;
                                this.clickedUnit = this.hoveredUnits.size() > 0 ? (Unit)this.hoveredUnits.get(0) : null;
                                this.clickMarkerScreenX = (int)IsoMathHelper.toScreenX(this.pointerTileX, this.pointerTileY, 128);
                                this.clickMarkerScreenY = (int)IsoMathHelper.toScreenY(this.pointerTileX, this.pointerTileY, 64);
                            }
                        } else if (!bl3 && this.scene.isMapScene()) {
                            if (this.hoveredUnits.size() > 0) {
                                if (((Unit)this.hoveredUnits.get(0)).getOwner() == this.unitCommander.getPlayer()) {
                                    int n25;
                                    if (TouchDeviceFlags.isTouchDevice() && UserConfig.isMouseControl() && this.doubleTap && this.selectedUnits.size() == 1 && !((Unit)this.selectedUnits.get(0)).isImmobile() && this.hoveredUnits.get(0) == this.selectedUnits.get(0) && !((Unit)this.selectedUnits.get(0)).getUnitType().isFlying()) {
                                        float f = 9.0f;
                                        UnitList unitList = this.world.getUnits();
                                        int n26 = unitList.size();
                                        n25 = 1;
                                        while (n25 != 0) {
                                            n25 = 0;
                                            n6 = 0;
                                            while (n6 < this.selectedUnits.size()) {
                                                Unit unit = (Unit)this.selectedUnits.get(n6);
                                                UnitType unitType = unit.getUnitType();
                                                float f10 = unit.getPosition().getX();
                                                float f11 = unit.getPosition().getY();
                                                i29 = 0;
                                                while (i29 < n26) {
                                                    float f12;
                                                    UnitPosition unitPosition;
                                                    float f13;
                                                    Unit unit6 = (Unit)unitList.get(i29);
                                                    if (!this.selectedUnits.contains(unit6) && unit6.getPosition().getUnit() == null && unit6.getOwner() == player && unit6.getUnitType() == unitType && (f13 = f10 - (unitPosition = unit6.getPosition()).getX()) * f13 + (f12 = f11 - unitPosition.getY()) * f12 <= f) {
                                                        this.selectedUnits.add(unit6);
                                                        n25 = 1;
                                                    }
                                                    ++i29;
                                                }
                                                ++n6;
                                            }
                                        }
                                    } else if ((TouchDeviceFlags.isTouchDevice() && UserConfig.isMouseControl() || this.scene == GameScene.Attack) && this.selectedUnits.size() > 0 && ((Unit)this.selectedUnits.get(0)).isImmobile() && i8) {
                                        if (this.selectedUnits.get(0) == this.hoveredUnits.get(0) && this.unitCommander.clearRallyPoint((Unit)this.selectedUnits.get(0))) {
                                            this.audio.playOk();
                                        } else if (this.unitCommander.setRallyPoint((Unit)this.selectedUnits.get(0), this.pointerTileX, this.pointerTileY)) {
                                            this.audio.playOk();
                                        } else {
                                            this.audio.playInvalid();
                                            this.clickMarkerUntilTime = this.world.getGameTime() + 0.2f;
                                            this.clickedUnit = null;
                                            this.clickMarkerScreenX = (int)IsoMathHelper.toScreenX(this.pointerTileX, this.pointerTileY, 128);
                                            this.clickMarkerScreenY = (int)IsoMathHelper.toScreenY(this.pointerTileX, this.pointerTileY, 64);
                                        }
                                        this.setScene(GameScene.Move);
                                    } else {
                                        if (input.isKeyPressed(129) || input.isKeyPressed(130)) {
                                            int n27 = 0;
                                            while (n27 < this.hoveredUnits.size()) {
                                                if (!this.selectedUnits.contains(this.hoveredUnits.get(n27))) {
                                                    this.selectedUnits.add((Unit)this.hoveredUnits.get(n27));
                                                }
                                                ++n27;
                                            }
                                        } else {
                                            this.selectedUnits.clear();
                                            this.selectedUnits.addAll((Collection)this.hoveredUnits);
                                        }
                                        if (this.groupBar.getSelectedGroup() >= 0) {
                                            UnitGroup unitGroup = this.unitCommander.getPlayer().getUnitGroups().get(this.groupBar.getSelectedGroup());
                                            boolean bl5 = true;
                                            UnitList unitList = unitGroup.getUnits();
                                            n25 = 0;
                                            while (n25 < this.selectedUnits.size()) {
                                                if (!unitList.contains(this.selectedUnits.get(n25))) {
                                                    bl5 = false;
                                                    break;
                                                }
                                                ++n25;
                                            }
                                            n25 = 0;
                                            while (n25 < unitList.size()) {
                                                if (!this.selectedUnits.contains(unitList.get(n25))) {
                                                    bl5 = false;
                                                    break;
                                                }
                                                ++n25;
                                            }
                                            if (!bl5) {
                                                this.groupBar.collapse();
                                            }
                                        }
                                    }
                                    this.setScene(GameScene.Build);
                                    if (this.selectedUnits.size() >= 2) {
                                        this.audio.playSquadReady();
                                    } else if (((Unit)this.selectedUnits.get(0)).isImmobile()) {
                                        this.audio.playStructureReady();
                                    } else {
                                        this.audio.playUnitReady();
                                    }
                                } else {
                                    this.audio.playInvalid();
                                    this.clickMarkerUntilTime = this.world.getGameTime() + 0.2f;
                                    this.clickedUnit = this.hoveredUnits.size() > 0 ? (Unit)this.hoveredUnits.get(0) : null;
                                    this.clickMarkerScreenX = (int)IsoMathHelper.toScreenX(this.pointerTileX, this.pointerTileY, 128);
                                    this.clickMarkerScreenY = (int)IsoMathHelper.toScreenY(this.pointerTileX, this.pointerTileY, 64);
                                }
                            } else if ((TouchDeviceFlags.isTouchDevice() && UserConfig.isMouseControl() || this.scene == GameScene.Attack) && this.selectedUnits.size() > 0 && ((Unit)this.selectedUnits.get(0)).isImmobile() && i8) {
                                if (this.unitCommander.setRallyPoint((Unit)this.selectedUnits.get(0), this.pointerTileX, this.pointerTileY)) {
                                    this.audio.playOk();
                                } else {
                                    this.audio.playInvalid();
                                    this.clickMarkerUntilTime = this.world.getGameTime() + 0.2f;
                                    this.clickedUnit = null;
                                    this.clickMarkerScreenX = (int)IsoMathHelper.toScreenX(this.pointerTileX, this.pointerTileY, 128);
                                    this.clickMarkerScreenY = (int)IsoMathHelper.toScreenY(this.pointerTileX, this.pointerTileY, 64);
                                }
                                this.setScene(GameScene.Move);
                            } else {
                                n23 = 1;
                            }
                        }
                    } else {
                        n23 = 1;
                    }
                    if (!(n23 == 0 || bl3 || this.selectedUnits.size() <= 0 || bl && this.scene != GameScene.Move)) {
                        this.tutorialHintBox.notifyStepEvent("ClosePanel");
                        this.selectedUnits.clear();
                        this.groupBar.collapse();
                        this.setScene(GameScene.Default);
                        GuiAssets.playClickSound();
                    }
                } else if (!this.scene.isPanelOpen()) {
                    this.tutorialHintBox.notifyStepEvent("MoveMap");
                    this.keyboardScrolling = true;
                    long l = l5 - 160000000L;
                    long l2 = Long.MAX_VALUE;
                    int n28 = this.pointerIndex;
                    int n29 = 0;
                    while (n29 < this.pointerDownTimes.length) {
                        n6 = (this.pointerDownTimes.length + this.pointerIndex - n29) % this.pointerDownTimes.length;
                        long l3 = MathHelper.abs(l - this.pointerDownTimes[n6]);
                        if (l3 < l2 && this.pointerX[n6] != Integer.MIN_VALUE) {
                            n28 = n6;
                            l2 = l3;
                        }
                        ++n29;
                    }
                    float f = (float)(l5 - this.pointerDownTimes[n28]) / 1.0E9f;
                    this.dragDx = (float)(i13 - this.pointerX[n28]) / f;
                    this.dragDy = (float)(i14 - this.pointerY[n28]) / f;
                }
                this.hoveredUnits.clear();
            } else {
                if (this.keyboardScrolling) {
                    float f = 2.64f * (f4 * 30.0f);
                    float f14 = MathHelper.sqrt(this.dragDx * this.dragDx + this.dragDy * this.dragDy);
                    if (f14 > f) {
                        this.dragDx -= f * this.dragDx / f14;
                        this.dragDy -= f * this.dragDy / f14;
                        float f15 = this.dragDx * f4;
                        float f16 = this.dragDy * f4;
                        this.setCameraPosition(this.cameraPosition.getX() + f15, this.cameraPosition.getY() + f16);
                    } else {
                        this.keyboardScrolling = false;
                    }
                }
                if (!this.scene.isPanelOpen() && TouchDeviceFlags.isTouchDevice()) {
                    int n30 = i13;
                    int n31 = i14;
                    if (UserConfig.isTouchScroll()) {
                        n30 = 2;
                        n31 = 2;
                    } else {
                        n30 = this.accumulatePointerX(n30, this.getWidth());
                        n31 = this.accumulatePointerY(n31, this.getHeight());
                    }
                    if (Gdx.input.isKeyPressed(UserConfig.getScrollKeyLeft())) {
                        n30 = 0;
                    } else if (Gdx.input.isKeyPressed(UserConfig.getScrollKeyRight())) {
                        n30 = this.getWidth();
                    } else if (Gdx.input.isKeyPressed(UserConfig.getScrollKeyUp())) {
                        n31 = 0;
                    } else if (Gdx.input.isKeyPressed(UserConfig.getScrollKeyDown())) {
                        n31 = this.getHeight();
                    }
                    int n32 = 0;
                    int n33 = 0;
                    if (n30 <= 1) {
                        n32 = UserConfig.getScrollSpeed();
                        this.tutorialHintBox.notifyStepEvent("MoveMap");
                    } else if (n30 >= this.getWidth() - 2) {
                        n32 = -UserConfig.getScrollSpeed();
                        this.tutorialHintBox.notifyStepEvent("MoveMap");
                    }
                    if (n31 <= 1) {
                        n33 = UserConfig.getScrollSpeed();
                        this.tutorialHintBox.notifyStepEvent("MoveMap");
                    } else if (n31 >= this.getHeight() - 2) {
                        n33 = -UserConfig.getScrollSpeed();
                        this.tutorialHintBox.notifyStepEvent("MoveMap");
                    }
                    this.setCameraPosition(this.cameraPosition.getX() + (float)n32 * f4, this.cameraPosition.getY() + (float)n33 * f4);
                }
            }
        }
        this.gameTimeNanos = l5;
        this.gameTimeMillis = this.gameTimeNanos / 1000000L;
        this.pointerDown = i7;
        this.pointerIndex = (this.pointerIndex + 1) % this.pointerDownTimes.length;
        this.pointerDownTimes[this.pointerIndex] = l5;
        this.pointerX[this.pointerIndex] = i13;
        this.pointerY[this.pointerIndex] = i14;
        this.cd = i8;
        this.ce = i9;
        this.cf = i11;
        this.timeStatusBar.setGameTime(player != null ? player.getResources() : 0L);
        if (UserConfig.isDisplayAi()) {
            this.fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
        }
        if ((gameMode = this.world.getGameMode()).hasTimeLimit()) {
            float f = this.world.getGameTime() - this.world.getGameMode().getTimeLimit();
            if (f > -60.0f) {
                this.audio.playTimeWarning();
            }
            if (f > 0.0f) {
                f = 0.0f;
            }
            this.timeStatusBar.setResourceAmount(f);
        } else if (gameMode instanceof CaptureTheFlagMode) {
            CaptureTheFlagMode captureTheFlagMode = (CaptureTheFlagMode)gameMode;
            // The flag structure can be missing from a loaded save; fall back to the idle timer.
            float f = captureTheFlagMode.getFlag() != null && captureTheFlagMode.getFlag().getOwner() != null ? -captureTheFlagMode.getHoldSeconds() + (this.world.getGameTime() - captureTheFlagMode.getHoldStartTime()) : -captureTheFlagMode.getHoldSeconds();
            if (f > 0.0f) {
                f = 0.0f;
            }
            this.timeStatusBar.setResourceAmount(f);
        }
        if (this.tutorialHintBox.isVisible()) {
            String string;
            boolean bl = true;
            boolean bl6 = true;
            UnitList unitList = this.world.getUnits();
            int n34 = 0;
            while (n34 < unitList.size()) {
                Unit unit = (Unit)unitList.get(n34);
                UnitType unitType = unit.getUnitType();
                if (!unitType.isImmobile() && unitType.canCarry()) {
                    if (unit.getGuardPositionOrNull() != null) {
                        int n35 = (int)unit.getGuardPositionOrNull().getX();
                        n4 = (int)unit.getGuardPositionOrNull().getY();
                        String string2 = this.tutorialHintBox.getActionCode();
                        if (string2 != null && string2.length() > 7 && string2.charAt(7) == 'Z') {
                            n2 = 8;
                            while (n2 < string2.length()) {
                                n = (string2.charAt(n2) - 48) * 10 + (string2.charAt(n2 + 1) - 48);
                                i29 = (string2.charAt(n2 + 2) - 48) * 10 + (string2.charAt(n2 + 3) - 48);
                                if (n35 == n && n4 == i29) {
                                    this.tutorialHintBox.notifyStepEvent("MoveMobileHost");
                                }
                                n2 += 4;
                            }
                        }
                    }
                    int n36 = (int)unit.getPosition().getX();
                    n4 = (int)unit.getPosition().getY();
                    String string3 = this.tutorialHintBox.getActionCode();
                    if (string3 != null && string3.charAt(0) == 'Z') {
                        n2 = 1;
                        while (n2 < string3.length()) {
                            n = (string3.charAt(n2) - 48) * 10 + (string3.charAt(n2 + 1) - 48);
                            i29 = (string3.charAt(n2 + 2) - 48) * 10 + (string3.charAt(n2 + 3) - 48);
                            if (n36 == n && n4 == i29) {
                                this.tutorialHintBox.notifyStepEvent("LandMobileHost");
                            }
                            n2 += 4;
                        }
                    }
                } else if (!unitType.isImmobile() && unitType.getDomain() == Domain.Ground) {
                    UnitPosition unitPosition = unit.getPosition();
                    UnitPosition unitPosition2 = unit.getGuardPositionOrNull();
                    if (unitPosition.getUnit() != null && !unitPosition.getUnit().getUnitType().isImmobile() && unitPosition.getUnit().getUnitType().canCarry()) {
                        this.tutorialHintBox.notifyStepEvent("LoadGroundIntoMobileHost");
                    } else if (unitPosition2 != null && unitPosition2.getUnit() != null && !unitPosition2.getUnit().getUnitType().isImmobile() && unitPosition2.getUnit().getUnitType().canCarry()) {
                        this.tutorialHintBox.notifyStepEvent("MoveGroundToMobileHost");
                    } else if (unitPosition2 != null && unitPosition2.getUnit() != null && unitPosition2.getUnit().getUnitType().isImmobile() && unitPosition2.getUnit().getOwner() != player) {
                        this.tutorialHintBox.notifyStepEvent("MoveGroundToEnemyFixed");
                    }
                } else if (unitType.isImmobile()) {
                    if (unit.getOwner() != player && unit.getOwner() != null) {
                        bl6 = false;
                    }
                    if (unit.getOwner() == null) {
                        bl = false;
                    }
                }
                ++n34;
            }
            if (bl6) {
                this.tutorialHintBox.notifyStepEvent("MoveGroundToEnemyFixed");
                this.tutorialHintBox.notifyStepEvent("DestroyEnemyFixed");
            }
            if (bl) {
                this.tutorialHintBox.notifyStepEvent("CapturedNeutral");
            }
            if (this.selectedUnits.size() > 0 && ((Unit)this.selectedUnits.get(0)).getUnitType().getDomain() == Domain.Ground) {
                this.tutorialHintBox.notifyStepEvent("SelectGround");
            }
            if ((string = this.tutorialHintBox.getActionCode()) != null && string.charAt(0) == 'A') {
                char c;
                if (string.charAt(1) == 'D') {
                    c = string.charAt(3);
                    n5 = this.selectedUnits.size() != 0 && this.scene != GameScene.Move ? 0 : 1;
                } else if (this.selectedUnits.size() == 0 || this.scene == GameScene.Move) {
                    c = string.charAt(3);
                    n5 = 1;
                } else {
                    c = string.charAt(7);
                    n5 = 0;
                }
                float f = 0.0f;
                float f17 = 0.0f;
                if (c == 'Z') {
                    f = (float)((string.charAt(8) - 48) * 10 + (string.charAt(9) - 48)) + 0.5f;
                    f17 = (float)((string.charAt(10) - 48) * 10 + (string.charAt(11) - 48)) + 0.5f;
                } else {
                    n3 = 0;
                    while (n3 < unitList.size()) {
                        Unit unit = (Unit)unitList.get(n3);
                        if (c == '1' && unit.isImmobile() && unit.getOwner() == player || c == '2' && unit.isImmobile() && unit.getOwner() != player && unit.getOwner() != null || c == '3' && !unit.isImmobile() && unit.getUnitType().canCarry() || c == '4' && !unit.isImmobile() && unit.getUnitType().getDomain() == Domain.Ground || c == '5' && unit.isImmobile() && unit.getOwner() == null) {
                            f = unit.getPosition().getX();
                            f17 = unit.getPosition().getY();
                            break;
                        }
                        ++n3;
                    }
                }
                if (n5 != 0) {
                    this.centerCameraOnPosition(f - 1.0f, f17 - 1.0f);
                }
                n3 = (short)((this.cameraPosition.getX() + IsoMathHelper.toScreenX(f, f17, 128)) * this.cameraZoom);
                n2 = (short)((this.cameraPosition.getY() + IsoMathHelper.toScreenY(f, f17, 64)) * this.cameraZoom);
                this.tutorialHintBox.showPointerAt(n3, n2 - 50);
            } else {
                this.tutorialHintBox.hidePointer();
            }
        }
        if (this.selectedUnits.size() > 0) {
            if (this.buildingPanel.isVisible()) {
                this.buildingPanel.setUnit((Unit)this.selectedUnits.get(0));
            }
            if (this.carrierUnitPanel.isVisible()) {
                this.carrierUnitPanel.setUnit((Unit)this.selectedUnits.get(0));
            }
            if (this.singleUnitPanel.isVisible()) {
                this.singleUnitPanel.setUnit((Unit)this.selectedUnits.get(0));
            }
            if (this.squadPanel.isVisible()) {
                this.squadPanel.setUnits(this.selectedUnits, this.groupBar.getSelectedGroup());
            }
        }
        if (this.groupBar.isVisible()) {
            this.groupBar.refresh();
        }
        if (this.tutorialHintBox.isWaitingForConfirmation()) {
            this.tutorialHintBox.handlePointer(i13, (float)i14, i7);
        } else if (this.scene.isPanelOpen()) {
            if (this.scene == GameScene.PauseMenu) {
                this.inGameMenu.handleInput(this.pauseMenuListener, i13, i14, i7);
            } else if (this.scene == GameScene.LanNotice) {
                this.lanNoticeDialog.handleInput(this.lanNoticeListener, i13, i14, i7);
            } else if (this.scene == GameScene.NationSelect) {
                this.nationSelectDialog.setPlayers(this.unitCommander.getWorld().getPlayers(), ((MultiplayerGameMode)this.gameMode).getClient().getPlayerNames());
                this.nationSelectDialog.handleInput(this.playerSlotListener, i13, i14, i7);
            } else if (this.scene == GameScene.WaitingForPlayers) {
                if (!this.loadingComplete) {
                    if (this.unitCommander.getPlayer() != null) {
                        this.centerCameraOnPlayer(this.unitCommander.getPlayer());
                        this.setScene(GameScene.MissionBriefing);
                    } else {
                        this.networkProgressDialog.setMessage(Messages.get("JoiningGame[i18n]: Joining Game..."));
                    }
                } else {
                    this.timeStatusBar.handleInput(this.inGameMenuButtonListener, i13, i14, i7);
                    this.networkProgressDialog.setMessage(String.valueOf(Messages.get("WaitingForPlayers[i18n]: Waiting for Players...")) + " (" + ((MultiplayerGameMode)this.gameMode).getClient().getPlayerCount() + "/" + this.unitCommander.getWorld().getPlayers().size() + ")");
                }
            } else if (this.scene == GameScene.MissionBriefing) {
                this.missionBriefingDialog.handleInput(this.briefingAcceptListener, i13, i14, i7);
            } else if (this.scene == GameScene.Loading) {
                if (this.backgroundTask.isDone()) {
                    this.loadingComplete = true;
                    this.setScene(GameScene.Default);
                    this.tutorialHintBox.notifyStepEvent("InitComplete");
                    if (this.gameMode instanceof MultiplayerGameMode) {
                        MultiplayerGameMode multiplayerGameMode = (MultiplayerGameMode)this.gameMode;
                        if (multiplayerGameMode.getServer() != null) {
                            multiplayerGameMode.getServer().shareTerrain(this.unitCommander.getWorld());
                        }
                        multiplayerGameMode.getClient().setReady(true);
                    }
                } else {
                    this.loadingProgressDialog.setProgress(this.backgroundTask.getProgress());
                    this.loadingProgressDialog.setMessage(String.valueOf(Messages.get("InitializingMap[i18n]: Initializing Map")) + "... " + (int)(this.backgroundTask.getProgress() * 100.0f) + "%");
                }
            } else if (this.scene == GameScene.ScoreDialog) {
                this.scoreDialog.handleInput(this.gameOverDismissListener, i13, i14, i7);
            } else if (this.scene == GameScene.RestartConfirm) {
                this.restartDialog.handleInput(this.restartConfirmListener, i13, i14, i7);
            } else if (this.scene == GameScene.QuitConfirm) {
                this.quitDialog.handleInput(this.quitConfirmListener, i13, i14, i7);
            }
        } else {
            this.timeStatusBar.handleInput(this.inGameMenuButtonListener, i13, i14, i7);
            this.minimapPanel.handleInput(this.zoomButtonListener, i13, i14, i7);
            this.buildingPanel.handleInput(this.buildPanelListener, i13, i14, i7);
            this.carrierUnitPanel.handleInput(this.productionPanelListener, i13, i14, i7);
            this.singleUnitPanel.handleInput(this.unitPanelListener, i13, i14, i7);
            this.squadPanel.handleInput(this.multiSelectPanelListener, i13, i14, i7);
            this.groupBar.handleInput(this.squadBarListener, i13, i14, i7);
            if (TouchDeviceFlags.isTouchDevice()) {
                boolean bl = false;
                int[] nArray = UserConfig.getSquadSelectKeys();
                int n37 = 0;
                while (n37 < nArray.length) {
                    if (Gdx.input.isKeyPressed(nArray[n37])) {
                        if (this.actionKeyCode != nArray[n37]) {
                            if (input.isKeyPressed(59) || input.isKeyPressed(60)) {
                                this.assignUnitsToGroup(n37);
                            } else if (input.isKeyPressed(129) || input.isKeyPressed(130)) {
                                this.addUnitsToGroup(n37);
                            } else {
                                this.selectUnitGroup(n37);
                            }
                            this.actionKeyCode = nArray[n37];
                        }
                        bl = true;
                    }
                    ++n37;
                }
                if (!bl) {
                    if (Gdx.input.isKeyPressed(UserConfig.getActionKeyStop())) {
                        if (this.actionKeyCode != UserConfig.getActionKeyStop()) {
                            this.commandStop();
                            this.actionKeyCode = UserConfig.getActionKeyStop();
                        }
                    } else if (Gdx.input.isKeyPressed(UserConfig.getActionKeyRepair())) {
                        if (this.actionKeyCode != UserConfig.getActionKeyRepair()) {
                            this.commandRepair();
                            this.actionKeyCode = UserConfig.getActionKeyRepair();
                        }
                    } else if (Gdx.input.isKeyPressed(UserConfig.getActionKeyPatrol())) {
                        if (this.actionKeyCode != UserConfig.getActionKeyPatrol()) {
                            this.commandPatrol();
                            this.actionKeyCode = UserConfig.getActionKeyPatrol();
                        }
                    } else if (Gdx.input.isKeyPressed(UserConfig.getActionKeyDeselect())) {
                        if (this.actionKeyCode != UserConfig.getActionKeyDeselect()) {
                            this.clearSelection();
                            this.actionKeyCode = UserConfig.getActionKeyDeselect();
                        }
                    } else if (Gdx.input.isKeyPressed(UserConfig.getActionKeyGroup())) {
                        if (this.actionKeyCode != UserConfig.getActionKeyGroup()) {
                            this.commandGroup();
                            this.actionKeyCode = UserConfig.getActionKeyGroup();
                        }
                    } else if (Gdx.input.isKeyPressed(UserConfig.getActionKeyUngroup())) {
                        if (this.actionKeyCode != UserConfig.getActionKeyUngroup()) {
                            this.commandUngroup();
                            this.actionKeyCode = UserConfig.getActionKeyUngroup();
                        }
                    } else {
                        this.actionKeyCode = -1;
                    }
                }
            }
        }
        if (this.gameMode instanceof MultiplayerGameMode) {
            MultiplayerGameMode multiplayerGameMode = (MultiplayerGameMode)this.gameMode;
            if (multiplayerGameMode.getClient().isConnected()) {
                if (multiplayerGameMode.getClient().update(this.gameEventListener)) {
                    if (this.loadingComplete && this.scene == GameScene.WaitingForPlayers) {
                        this.setScene(GameScene.Default);
                    }
                } else if (this.loadingComplete && this.scene != GameScene.WaitingForPlayers && this.scene != GameScene.PauseMenu && this.scene != GameScene.RestartGame && this.scene != GameScene.RestartConfirm && this.scene != GameScene.QuitConfirm && this.scene != GameScene.ExitToMenu) {
                    this.setScene(GameScene.WaitingForPlayers);
                }
            } else {
                return new MultiplayerLobbyScreen(Messages.get("DisconnectedETC[i18n]: Disconnected from game. Possible causes include:\n\n(1) Host Closed\n(2) Bad Connection"));
            }
            this.zoomLabel.setText(multiplayerGameMode.getClient().getChatText());
        } else if (!this.scene.isPanelOpen()) {
            this.aiElapsedTime += f4;
            if ((float)this.aiUpdateCount < 30.0f * this.aiElapsedTime) {
                this.aiPlanners[this.aiUpdateCount % this.aiPlanners.length].update();
                ++this.aiUpdateCount;
            }
            WorldSimulator.tick(this.world, com.desertstormfront.mod.ModGameEventListener.wrap(this.gameEventListener), f4);
        }
        if (!(this.scene.isPanelOpen() || !this.world.getGameMode().isGameOver() && this.unitCommander.getPlayer().isAlive())) {
            if (this.spectatingSince < this.world.getGameTime() - 7.0f) {
                String string;
                String string4;
                PlayerStatistics playerStatistics = this.unitCommander.getPlayer().getStatistics();
                PlayerStatistics playerStatistics2 = UserConfig.getStatistics();
                playerStatistics2.setDestroyedUnits(playerStatistics2.getDestroyedUnits() + playerStatistics.getDestroyedUnits());
                playerStatistics2.setLostUnits(playerStatistics2.getLostUnits() + playerStatistics.getLostUnits());
                playerStatistics2.setDamageInflicted(playerStatistics2.getDamageInflicted() + playerStatistics.getDamageInflicted());
                playerStatistics2.setDamageReceived(playerStatistics2.getDamageReceived() + playerStatistics.getDamageReceived());
                playerStatistics2.setBasesCaptured(playerStatistics2.getBasesCaptured() + playerStatistics.getBasesCaptured());
                playerStatistics2.setBasesLost(playerStatistics2.getBasesLost() + playerStatistics.getBasesLost());
                UserConfig.setStatistics(playerStatistics2);
                this.disposeAudioClips();
                if (this.world.getGameMode().isGameOver() && this.world.getGameMode().getPlayers().contains(this.unitCommander.getPlayer())) {
                    string4 = Messages.getFallback("Victory[i18n]: Victory!");
                    string = this.gameMode.getVictoryMessage(playerStatistics2);
                    this.audio.playVictoryMusic();
                } else {
                    string4 = Messages.getFallback("Defeat[i18n]: Defeat!");
                    string = this.gameMode.getDefeatMessage(playerStatistics2);
                    this.audio.playDefeatMusic();
                }
                this.scoreDialog.setTitle(string4);
                this.scoreDialog.setMessage(string);
                this.scoreDialog.setStatistics(this.unitCommander.getPlayer().getStatistics());
                this.setScene(GameScene.ScoreDialog);
            } else if (this.spectatingSince == Float.MAX_VALUE) {
                this.spectatingSince = this.world.getGameTime();
                this.audio.pauseMusic();
                this.audio.playMissionTerminated();
                this.resultBanner.setVictory(this.world.getGameMode().isGameOver() && this.world.getGameMode().getPlayers().contains(this.unitCommander.getPlayer()));
                this.selectedUnits.clear();
                this.groupBar.collapse();
                this.setScene(GameScene.MissionTerminated);
            }
        }
        if (this.scene == GameScene.ExitToMenu) {
            if (this.gameMode instanceof CampaignGameMode) {
                return new CampaignScreen();
            }
            if (this.gameMode instanceof SkirmishGameMode) {
                return new SkirmishSetupScreen((SkirmishGameMode)this.gameMode);
            }
            if (this.gameMode instanceof MultiplayerGameMode) {
                return new MultiplayerLobbyScreen();
            }
            return new MainMenuScreen();
        }
        if (this.scene == GameScene.RestartGame) {
            if (this.gameMode instanceof MultiplayerGameMode) {
                return new MultiplayerLobbyScreen();
            }
            return new LoadGameScreen(this.gameMode, true);
        }
        return null;
    }

    private boolean isSelectionCommandable() {
        return this.selectedUnits.size() > 0 && !((Unit)this.selectedUnits.get(0)).isImmobile() && (this.scene == GameScene.Attack || this.scene == GameScene.ForceAttack || this.scene == GameScene.Build && (this.selectedUnits.size() == 1 && this.unitCommander.isCommandable((Unit)this.selectedUnits.get(0)) || this.selectedUnits.size() > 1 && this.unitCommander.hasCommandableUnit(this.selectedUnits)));
    }

    private boolean isPointOnHud(float f1, float f2) {
        return this.buildingPanel.isVisible() && this.buildingPanel.contains(f1, f2) || this.carrierUnitPanel.isVisible() && this.carrierUnitPanel.contains(f1, f2) || this.singleUnitPanel.isVisible() && this.singleUnitPanel.contains(f1, f2) || this.squadPanel.isVisible() && this.squadPanel.contains(f1, f2) || this.minimapPanel.isVisible() && this.minimapPanel.contains(f1, f2) || this.groupBar.contains(f1, f2);
    }

    private void setScene(GameScene gameScene) {
        this.buildingPanel.setVisible(false);
        this.carrierUnitPanel.setVisible(false);
        this.singleUnitPanel.setVisible(false);
        this.squadPanel.setVisible(false);
        this.lanNoticeDialog.setVisible(false);
        this.nationSelectDialog.setVisible(false);
        this.networkProgressDialog.setVisible(false);
        this.missionBriefingDialog.setVisible(false);
        this.loadingProgressDialog.setVisible(false);
        this.inGameMenu.setVisible(false);
        this.resultBanner.setVisible(false);
        this.scoreDialog.setVisible(false);
        this.restartDialog.setVisible(false);
        this.quitDialog.setVisible(false);
        switch (GameScreen.getGameSceneSwitchMap()[gameScene.ordinal()]) {
            case 1: {
                this.lanNoticeDialog.setVisible(true);
                this.timeStatusBar.setVisible(false);
                this.groupBar.setVisible(false);
                this.minimapRenderer.setVisible(false);
                this.minimapPanel.setExpanded(false);
                break;
            }
            case 2: {
                this.nationSelectDialog.setVisible(true);
                this.timeStatusBar.setVisible(false);
                this.groupBar.setVisible(false);
                this.minimapRenderer.setVisible(false);
                this.minimapPanel.setExpanded(false);
                break;
            }
            case 3: {
                this.networkProgressDialog.setVisible(true);
                this.networkProgressDialog.setProgress(0.0f);
                break;
            }
            case 4: {
                this.missionBriefingDialog.setVisible(true);
                this.timeStatusBar.setVisible(true);
                this.groupBar.setVisible(true);
                this.minimapRenderer.setVisible(UserConfig.isRenderDetails());
                this.minimapPanel.setExpanded(UserConfig.isRenderDetails());
                break;
            }
            case 5: {
                this.loadingProgressDialog.setVisible(true);
                break;
            }
            case 6: {
                break;
            }
            case 7: {
                Unit unit = (Unit)this.selectedUnits.get(0);
                if (this.selectedUnits.size() >= 2) {
                    this.buildingPanel.setVisible(false);
                    this.carrierUnitPanel.setVisible(false);
                    this.singleUnitPanel.setVisible(false);
                    this.squadPanel.setVisible(true);
                    this.squadPanel.setUnits(this.selectedUnits, this.groupBar.getSelectedGroup());
                    break;
                }
                UnitType unitType = unit.getUnitType();
                if (unitType.isImmobile()) {
                    this.buildingPanel.setVisible(true);
                    this.buildingPanel.setUnit(unit);
                    this.carrierUnitPanel.setVisible(false);
                    this.singleUnitPanel.setVisible(false);
                    this.squadPanel.setVisible(false);
                    break;
                }
                if (unitType.getCapacity() > 0) {
                    this.buildingPanel.setVisible(false);
                    this.carrierUnitPanel.setVisible(true);
                    this.carrierUnitPanel.setUnit(unit);
                    this.singleUnitPanel.setVisible(false);
                    this.squadPanel.setVisible(false);
                    break;
                }
                this.buildingPanel.setVisible(false);
                this.carrierUnitPanel.setVisible(false);
                this.singleUnitPanel.setVisible(true);
                this.singleUnitPanel.setUnit(unit);
                this.squadPanel.setVisible(false);
                break;
            }
            case 8: {
                break;
            }
            case 9: {
                break;
            }
            case 10: {
                break;
            }
            case 11: {
                this.inGameMenu.setVisible(true);
                break;
            }
            case 12: {
                this.resultBanner.setVisible(true);
                break;
            }
            case 13: {
                this.scoreDialog.setVisible(true);
                break;
            }
            case 14: {
                this.restartDialog.setVisible(true);
                break;
            }
            case 15: {
                this.quitDialog.setVisible(true);
                break;
            }
            case 16: {
                break;
            }
            case 17: {
                break;
            }
            default: {
                OsfLog.error("Mode not implemented: " + (Object)((Object)gameScene));
            }
        }
        this.scene = gameScene;
    }

    private void selectUnitGroup(int i1) {
        if (i1 == -1) {
            this.selectedUnits.clear();
            this.groupBar.collapse();
            this.groupBar.expand();
            this.setScene(GameScene.Default);
            this.dragging = true;
            GuiAssets.playClickSound();
        } else {
            UnitGroup unitGroup = this.unitCommander.getPlayer().getUnitGroups().get(i1);
            if (this.groupBar.getSelectedGroup() == i1) {
                UnitList unitList = unitGroup.getUnits();
                if (unitList.size() > 0) {
                    float f4 = 0.0f;
                    float f5 = 0.0f;
                    int i6 = 0;
                    while (i6 < unitList.size()) {
                        f4 += ((Unit)unitList.get(i6)).getPosition().getX();
                        f5 += ((Unit)unitList.get(i6)).getPosition().getY();
                        ++i6;
                    }
                    this.centerCameraOnPosition(f4 /= (float)unitList.size(), f5 /= (float)unitList.size());
                }
            } else if (this.groupBar.getSelectedGroup() == -1 && this.selectedUnits.size() > 0 && !((Unit)this.selectedUnits.get(0)).isImmobile() && unitGroup.getUnits().size() == 0) {
                this.groupBar.selectGroup(i1);
                this.unitCommander.formSquad(unitGroup, this.selectedUnits);
                this.setScene(GameScene.Build);
                this.audio.playOk();
            } else {
                this.selectedUnits.clear();
                this.groupBar.selectGroup(i1);
                if (unitGroup.getUnits().size() == 0) {
                    this.setScene(GameScene.Default);
                    this.dragging = true;
                    GuiAssets.playClickSound();
                } else {
                    this.selectedUnits.addAll((Collection)unitGroup.getUnits());
                    this.setScene(GameScene.Build);
                    this.audio.playSquadReady();
                }
            }
        }
    }

    private void commandStop() {
        if (this.selectedUnits.size() > 0 && !((Unit)this.selectedUnits.get(0)).isImmobile()) {
            if (this.selectedUnits.size() == 1) {
                this.unitCommander.stopUnit((Unit)this.selectedUnits.get(0));
                this.selectedUnits.clear();
                this.groupBar.collapse();
                this.setScene(GameScene.Default);
                this.audio.playOk();
            } else {
                this.unitCommander.stopUnits(this.selectedUnits);
                this.selectedUnits.clear();
                this.groupBar.collapse();
                this.setScene(GameScene.Default);
                this.audio.playOk();
            }
        }
    }

    private void commandRepair() {
        if (this.selectedUnits.size() > 0 && !((Unit)this.selectedUnits.get(0)).isImmobile()) {
            if (this.selectedUnits.size() == 1) {
                this.unitCommander.repairUnit((Unit)this.selectedUnits.get(0));
                this.selectedUnits.clear();
                this.groupBar.collapse();
                this.setScene(GameScene.Default);
                this.audio.playOk();
            } else {
                this.unitCommander.repairUnits(this.selectedUnits);
                this.selectedUnits.clear();
                this.groupBar.collapse();
                this.setScene(GameScene.Default);
                this.audio.playOk();
            }
        }
    }

    private void commandPatrol() {
        if (this.selectedUnits.size() == 1 && !((Unit)this.selectedUnits.get(0)).isImmobile() && this.scene != GameScene.ForceAttack) {
            this.setScene(GameScene.ForceAttack);
            GuiAssets.playClickSound();
        }
    }

    private void assignUnitsToGroup(int i1) {
        if (this.selectedUnits.size() > 0 && !((Unit)this.selectedUnits.get(0)).isImmobile()) {
            UnitGroup unitGroup = this.unitCommander.getPlayer().getUnitGroups().getGroup(i1);
            this.unitCommander.formSquad(unitGroup, this.selectedUnits);
            this.groupBar.selectGroup(unitGroup.getId());
            this.setScene(GameScene.Build);
            this.audio.playOk();
        }
    }

    private void addUnitsToGroup(int i1) {
        if (this.selectedUnits.size() > 0 && !((Unit)this.selectedUnits.get(0)).isImmobile()) {
            UnitGroup unitGroup = this.unitCommander.getPlayer().getUnitGroups().getGroup(i1);
            if (unitGroup.getUnits().size() > 0) {
                Domain domain = ((Unit)unitGroup.getUnits().get(0)).getUnitType().getDomain();
                int i4 = 0;
                while (i4 < this.selectedUnits.size()) {
                    if (((Unit)this.selectedUnits.get(i4)).getUnitType().getDomain() == domain) {
                        ++i4;
                        continue;
                    }
                    this.selectedUnits.remove(i4);
                }
                if (this.selectedUnits.size() > 0) {
                    this.selectedUnits.addAll((Collection)unitGroup.getUnits());
                    this.unitCommander.formSquad(unitGroup, this.selectedUnits);
                    this.groupBar.selectGroup(unitGroup.getId());
                    this.setScene(GameScene.Build);
                    this.audio.playOk();
                }
            } else {
                this.unitCommander.formSquad(unitGroup, this.selectedUnits);
                this.groupBar.selectGroup(unitGroup.getId());
                this.setScene(GameScene.Build);
                this.audio.playOk();
            }
        }
    }

    private void commandGroup() {
        if (this.selectedUnits.size() > 0 && !((Unit)this.selectedUnits.get(0)).isImmobile()) {
            this.unitCommander.assignSquad(this.selectedUnits);
            this.groupBar.collapse();
            this.selectedUnits.clear();
            this.setScene(GameScene.Default);
            this.audio.playOk();
        }
    }

    private void commandUngroup() {
        if (this.selectedUnits.size() > 0 && !((Unit)this.selectedUnits.get(0)).isImmobile()) {
            if (this.selectedUnits.size() == 1) {
                this.unitCommander.leaveSquad((Unit)this.selectedUnits.get(0));
                this.groupBar.collapse();
                this.selectedUnits.clear();
                this.setScene(GameScene.Default);
                this.audio.playOk();
            } else {
                this.unitCommander.leaveSquadUnits(this.selectedUnits);
                this.groupBar.collapse();
                this.selectedUnits.clear();
                this.setScene(GameScene.Default);
                this.audio.playOk();
            }
        }
    }

    private void clearSelection() {
        this.selectedUnits.clear();
        this.groupBar.collapse();
        if (!this.scene.isPanelOpen()) {
            this.setScene(GameScene.Default);
        }
        this.pointerDown = false;
        this.hoveredUnits.clear();
    }

    private void centerCameraOnPlayer(Player player) {
        Unit unit = null;
        if (this.world.getGameMode() instanceof EscortMode) {
            UnitList unitList = this.unitCommander.getWorld().getUnits();
            int i4 = 0;
            while (i4 < unitList.size()) {
                Unit unit2 = (Unit)unitList.get(i4);
                if (unit2.getOwner() == this.unitCommander.getPlayer() && (unit == null || !unit.getUnitType().isTruck() && unit2.getUnitType().isTruck())) {
                    unit = unit2;
                }
                ++i4;
            }
        } else {
            UnitList unitList = this.unitCommander.getWorld().getUnits();
            int i4 = 0;
            while (i4 < unitList.size()) {
                Unit unit3 = (Unit)unitList.get(i4);
                if (unit3.getOwner() == this.unitCommander.getPlayer() && (unit == null || !unit.isImmobile() || !unit.isHighValue() && unit3.isHighValue())) {
                    unit = unit3;
                }
                ++i4;
            }
        }
        this.centerCameraOnUnit(unit);
    }

    private void centerCameraOnUnit(Unit unit) {
        int i2 = this.getWidth();
        int i3 = this.getHeight();
        if (unit != null) {
            UnitPosition unitPosition = unit.getPosition();
            this.setCameraPosition((float)(i2 / 2) / this.cameraZoom - IsoMathHelper.toScreenX(unitPosition.getX(), unitPosition.getY(), 128), (float)(i3 / 2) / this.cameraZoom - IsoMathHelper.toScreenY(unitPosition.getX(), unitPosition.getY(), 64));
        } else {
            this.setCameraPosition((float)(i2 / 2) / this.cameraZoom, 0.0f);
        }
    }

    private void centerCameraOnPosition(float f1, float f2) {
        int i3 = this.getWidth();
        int i4 = this.getHeight();
        this.setCameraPosition((float)(i3 / 2) / this.cameraZoom - IsoMathHelper.toScreenX(f1, f2, 128), (float)(i4 / 2) / this.cameraZoom - IsoMathHelper.toScreenY(f1, f2, 64));
    }

    private void setCameraPosition(float f1, float f2) {
        int i3 = this.getWidth();
        if (f1 < this.at) {
            f1 = this.at;
        } else if (f1 > this.au) {
            f1 = this.au;
        }
        if (f2 < f1 * 64.0f / 128.0f - (float)this.minimapHeight) {
            f2 = f1 * 64.0f / 128.0f - (float)this.minimapHeight;
        } else if (f2 > f1 * 64.0f / 128.0f) {
            f2 = f1 * 64.0f / 128.0f;
        }
        if (f2 < (f1 - (float)i3) * -64.0f / 128.0f - (float)this.minimapHeight) {
            f2 = (f1 - (float)i3) * -64.0f / 128.0f - (float)this.minimapHeight;
        } else if (f2 > (f1 - (float)i3) * -64.0f / 128.0f) {
            f2 = (f1 - (float)i3) * -64.0f / 128.0f;
        }
        this.cameraPosition.setX(f1);
        this.cameraPosition.setY(f2);
        this.minimapRenderer.setCameraX((int)f1);
        this.minimapRenderer.setCameraY((int)f2);
    }

    @Override
    public void show() {
        int n;
        this.cameraZoom = this.targetCameraZoom = 1.0f;
        this.frameCount = 0;
        this.inputMultiplexer = new InputMultiplexer(Gdx.input.getInputProcessor(), new GameInputProcessor(this));
        Gdx.input.setInputProcessor(this.inputMultiplexer);
        Gdx.input.setCatchBackKey(true);
        this.spectatingSince = Float.MAX_VALUE;
        this.world = this.gameMode.loadWorld();
        this.gameEventListener = new GameEventSoundListener(this);
        if (this.world.getGameMode() instanceof EscortMode) {
            this.escortTargetArea = ((EscortMode)this.world.getGameMode()).getTargetArea();
        }
        this.buildingSpriteTable = SpriteSheetLoader.loadBuildingSpriteTable(this.world.getMapDefinition());
        this.unitSpriteTable = SpriteSheetLoader.loadUnitSpriteTable(this.world.getMapDefinition());
        this.X = Long.MIN_VALUE;
        this.audio = new GameAudio(this.getMarket().getName());
        if (this.gameMode instanceof MultiplayerGameMode) {
            this.unitCommander = new NetworkedUnitCommander(((MultiplayerGameMode)this.gameMode).getClient());
        } else {
            PlayerList playerList = this.world.getPlayers();
            this.aiPlanners = new AIPlanner[playerList.size() - 1];
            int i5 = 0;
            int i6 = 0;
            while (i6 < playerList.size()) {
                Player player = (Player)playerList.get(i6);
                DirectUnitCommander directUnitCommander = new DirectUnitCommander(this.world, player);
                if (player.getController() == Controller.Human) {
                    this.unitCommander = directUnitCommander;
                } else {
                    this.aiPlanners[i5++] = new AIPlanner(directUnitCommander);
                }
                ++i6;
            }
            this.aiElapsedTime = 0.0f;
            this.aiUpdateCount = 0;
        }
        this.tutorialHintBox = this.gameMode instanceof TutorialGameMode ? new TutorialHintBox(GameConfig.getTutorialTexts(), GuiAssets.getDefaultFont(), GuiAssets.getHintBoxNinePatch(), GuiAssets.getHintBoxButtonStyle(), GuiAssets.getHintPointerRegion()) : new TutorialHintBox();
        this.selectedUnits = new UnitList(64);
        this.selectionScratch = new UnitList(64);
        this.hoveredUnits = new UnitList(64);
        this.lastTapTime = Long.MIN_VALUE;
        this.doubleTap = false;
        this.clickMarkerUntilTime = 0.0f;
        this.scene = this.unitCommander.getPlayer() == null ? (((MultiplayerGameMode)this.gameMode).getServer() != null && !((MultiplayerGameMode)this.gameMode).getServer().getMatchRecord().isPortOpen() ? GameScene.LanNotice : GameScene.NationSelect) : GameScene.MissionBriefing;
        this.backgroundTask = new BackgroundTask(this.world);
        this.loadingComplete = false;
        this.gameTimeNanos = 0L;
        this.gameTimeMillis = this.gameTimeNanos / 1000000L;
    }

    @Override
    public void createGlResources() {
        int i12;
        int i11;
        int i8;
        GL20 gL20 = Gdx.gl20;
        gL20.glBlendFunc(770, 771);
        gL20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        gL20.glEnable(3042);
        gL20.glDepthFunc(515);
        gL20.glClearDepthf(1.0f);
        gL20.glDisable(2929);
        gL20.glDisable(3024);
        gL20.glDisable(2960);
        gL20.glDisable(2884);
        gL20.glActiveTexture(33984);
        this.cameraPosition = new Vec3();
        this.pointerDown = false;
        this.keyboardScrolling = false;
        this.pointerIndex = 0;
        this.pointerDownTimes = new long[16];
        this.pointerX = new int[this.pointerDownTimes.length];
        this.pointerY = new int[this.pointerDownTimes.length];
        this.pointerHeld = false;
        this.cd = false;
        this.ce = false;
        this.cf = false;
        this.actionKeyCode = -1;
        this.dragging = false;
        this.domainCounts = new int[Domain.values().length];
        this.tilesTexture = this.getTilesTexture();
        this.unitsTexture = this.getUnitsTexture();
        this.spriteShaderProgram = new ShaderProgram("attribute vec4 a_position;                            \nattribute vec2 a_texCoord;                            \nvarying vec2 v_texCoord;                              \nuniform mat4 u_mvpMatrix;                             \nuniform vec2 u_translate;                             \nvoid main()                                           \n{                                                     \n   gl_Position = a_position;                          \n   v_texCoord = a_texCoord * 0.0078125;               \n   gl_Position.x += u_translate.x;                    \n   gl_Position.y += u_translate.y;                    \n   gl_Position *= u_mvpMatrix;                        \n}                                                     \n", "#ifdef GL_ES                                          \nprecision mediump float;                              \n#endif                                                \nvarying vec2 v_texCoord;                              \nuniform sampler2D s_texture;                          \nvoid main()                                           \n{                                                     \n  gl_FragColor = texture2D(s_texture, v_texCoord);    \n}                                                     \n");
        this.positionAttribute = this.spriteShaderProgram.getAttributeLocation("a_position");
        this.texCoordAttribute = this.spriteShaderProgram.getAttributeLocation("a_texCoord");
        this.textureUniform = this.spriteShaderProgram.getUniformLocation("s_texture");
        this.translateUniform = this.spriteShaderProgram.getUniformLocation("u_translate");
        this.mvpMatrixUniform = this.spriteShaderProgram.getUniformLocation("u_mvpMatrix");
        boolean bl = this.useFloatVertices = Gdx.app.getType() == Application.ApplicationType.Desktop || Gdx.app.getType() == Application.ApplicationType.WebGL;
        if (UserConfig.isDisplayAi()) {
            this.vectorTextRenderer = new VectorTextRenderer(4096);
        }
        this.hudRenderer = this.getHudRenderer();
        TerrainGrid terrainGrid = this.unitCommander.getWorld().getTerrainGrid();
        int i5 = terrainGrid.getWidth() + 1;
        int i6 = terrainGrid.getHeight() + 1;
        short[] sArray = new short[12 * i5 * i6];
        short[] sArray2 = new short[6 * i5 * i6];
        int n = 0;
        while (n < i6) {
            i8 = 0;
            while (i8 < i5) {
                int i13;
                int i14;
                int i15;
                int i16;
                float f;
                float f2;
                i11 = 128;
                i12 = 64;
                if (n < i6 - 1 && i8 < i5 - 1) {
                    f2 = IsoMathHelper.toScreenX(i8, n, 128);
                    f = IsoMathHelper.toScreenY(i8, n, 64);
                    i16 = (terrainGrid.isCornerLand(i8, n) ? 0 : 1) + (terrainGrid.isCornerLand(i8 + 1, n) ? 0 : 2) + (terrainGrid.isCornerLand(i8 + 1, n + 1) ? 0 : 4) + (terrainGrid.isCornerLand(i8, n + 1) ? 0 : 8);
                    RoadTile roadTile = terrainGrid.getRoadTile(i8, n);
                    if (roadTile != null) {
                        i15 = roadTile.getRoadMask().ordinal();
                        boolean i18 = roadTile.b();
                        if (i15 == 5 && (HashUtils.hash(i8, n) & Integer.MAX_VALUE) % 3 != 0) {
                            i14 = 832;
                            i13 = (i18 ? 0 : 512) + 128 * ((HashUtils.hash(i8, n) & Integer.MAX_VALUE) % 2);
                        } else if (i15 == 10 && (HashUtils.hash(i8, n) & Integer.MAX_VALUE) % 3 != 0) {
                            i14 = 832;
                            i13 = (i18 ? 256 : 768) + 128 * ((HashUtils.hash(i8, n) & Integer.MAX_VALUE) % 2);
                        } else {
                            i14 = (i18 ? 896 : 384) + 64 * (i15 / 8);
                            i13 = 128 * (i15 % 8);
                        }
                    } else {
                        switch (i16) {
                            case 0: {
                                i15 = (HashUtils.hash(i8, n) & Integer.MAX_VALUE) % 6;
                                i14 = 256;
                                i13 = 128 * i15;
                                break;
                            }
                            case 15: {
                                i15 = (HashUtils.hash(i8, n) & Integer.MAX_VALUE) % 6;
                                i14 = 320;
                                i13 = 128 * i15;
                                break;
                            }
                            default: {
                                i15 = (HashUtils.hash(i8, n) & Integer.MAX_VALUE) % 2;
                                i14 = 128 * i15 + (i16 >= 8 ? 64 : 0);
                                i13 = 128 * (i16 >= 8 ? i16 - 8 : i16 - 1);
                                break;
                            }
                        }
                    }
                } else {
                    i12 = 96;
                    i13 = 896;
                    if (n == i6 - 1 && i8 == i5 - 1) {
                        f2 = IsoMathHelper.toScreenX((float)i8 - 0.5f, (float)n - 0.5f, 128);
                        f = IsoMathHelper.toScreenY((float)i8 - 0.5f, (float)n - 0.5f, 64);
                        i14 = 96;
                    } else if (i8 == i5 - 1) {
                        f2 = IsoMathHelper.toScreenX((float)i8 - 0.5f, (float)n + 0.5f, 128);
                        f = IsoMathHelper.toScreenY((float)i8 - 0.5f, (float)n + 0.5f, 64);
                        i14 = 192;
                    } else {
                        f2 = IsoMathHelper.toScreenX((float)i8 + 0.5f, (float)n - 0.5f, 128);
                        f = IsoMathHelper.toScreenY((float)i8 + 0.5f, (float)n - 0.5f, 64);
                        i14 = 0;
                    }
                }
                i15 = n * i5 + i8;
                i16 = (short)f2 - 64;
                short s = (short)f;
                sArray[12 * i15 + 0] = (short)i16;
                sArray[12 * i15 + 1] = s;
                sArray[12 * i15 + 2] = this.packUvScaled(i13, i14);
                sArray[12 * i15 + 3] = (short)(i16 + i11);
                sArray[12 * i15 + 4] = s;
                sArray[12 * i15 + 5] = this.packUvScaled(i13 + i11, i14);
                sArray[12 * i15 + 6] = (short)(i16 + i11);
                sArray[12 * i15 + 7] = (short)(s + i12);
                sArray[12 * i15 + 8] = this.packUvScaled(i13 + i11, i14 + i12);
                sArray[12 * i15 + 9] = (short)i16;
                sArray[12 * i15 + 10] = (short)(s + i12);
                sArray[12 * i15 + 11] = this.packUvScaled(i13, i14 + i12);
                sArray2[6 * i15 + 0] = (short)(4 * i15 + 0);
                sArray2[6 * i15 + 1] = (short)(4 * i15 + 1);
                sArray2[6 * i15 + 2] = (short)(4 * i15 + 2);
                sArray2[6 * i15 + 3] = (short)(4 * i15 + 0);
                sArray2[6 * i15 + 4] = (short)(4 * i15 + 2);
                sArray2[6 * i15 + 5] = (short)(4 * i15 + 3);
                ++i8;
            }
            ++n;
        }
        if (this.useFloatVertices) {
            this.terrainVertexBuffer = new GlBuffer(34962, new float[16 * i5 * i6], 35044);
            FloatBuffer floatBuffer = (FloatBuffer)this.terrainVertexBuffer.getData();
            floatBuffer.position(0);
            i8 = 0;
            while (i8 < sArray.length) {
                if (i8 % 3 == 2) {
                    floatBuffer.put(sArray[i8] & 0xFF);
                    floatBuffer.put((sArray[i8] & 0xFF00) >>> 8);
                } else {
                    floatBuffer.put(sArray[i8]);
                }
                ++i8;
            }
            floatBuffer.position(0);
            this.terrainVertexBuffer.bind();
            this.terrainVertexBuffer.uploadAll();
            this.terrainVertexBuffer.unbind();
        } else {
            this.terrainVertexBuffer = new GlBuffer(34962, sArray, 35044);
        }
        this.terrainIndexBuffer = new GlBuffer(34963, sArray2, 35044);
        int n2 = 8;
        if (n2 == 0) {
            n2 = 1;
        }
        this.spriteVertexData = new short[1536];
        this.spriteVertexOffset = 0;
        this.spriteVertexCapacity = this.spriteVertexData.length;
        this.spriteVertexBuffers = new GlBuffer[n2];
        i8 = 0;
        while (i8 < n2) {
            this.spriteVertexBuffers[i8] = this.useFloatVertices ? new GlBuffer(34962, new float[2048], 35040) : new GlBuffer(34962, this.spriteVertexData, 35040);
            ++i8;
        }
        sArray2 = new short[768];
        i8 = 0;
        while (i8 < 128) {
            sArray2[6 * i8 + 0] = (short)(4 * i8 + 0);
            sArray2[6 * i8 + 1] = (short)(4 * i8 + 1);
            sArray2[6 * i8 + 2] = (short)(4 * i8 + 2);
            sArray2[6 * i8 + 3] = (short)(4 * i8 + 0);
            sArray2[6 * i8 + 4] = (short)(4 * i8 + 2);
            sArray2[6 * i8 + 5] = (short)(4 * i8 + 3);
            ++i8;
        }
        this.spriteIndexBuffers = new GlBuffer[n2];
        i8 = 0;
        while (i8 < n2) {
            this.spriteIndexBuffers[i8] = new GlBuffer(34963, sArray2, 35044);
            ++i8;
        }
        this.spriteBufferIndex = 0;
        sArray = new short[12 * terrainGrid.getWidth() * terrainGrid.getHeight()];
        sArray2 = new short[6 * terrainGrid.getWidth() * terrainGrid.getHeight()];
        i8 = 0;
        while (i8 < terrainGrid.getHeight()) {
            int n3 = 0;
            while (n3 < terrainGrid.getWidth()) {
                int n4 = i8 * terrainGrid.getWidth() + n3;
                sArray2[6 * n4 + 0] = (short)(4 * n4 + 0);
                sArray2[6 * n4 + 1] = (short)(4 * n4 + 1);
                sArray2[6 * n4 + 2] = (short)(4 * n4 + 2);
                sArray2[6 * n4 + 3] = (short)(4 * n4 + 0);
                sArray2[6 * n4 + 4] = (short)(4 * n4 + 2);
                sArray2[6 * n4 + 5] = (short)(4 * n4 + 3);
                ++n3;
            }
            ++i8;
        }
        this.lineVertexBuffer = this.useFloatVertices ? new GlBuffer(34962, new float[16 * terrainGrid.getWidth() * terrainGrid.getHeight()], 35040) : new GlBuffer(34962, sArray, 35040);
        this.lineIndexBuffer = new GlBuffer(34963, sArray2, 35044);
        this.lineVertexBuffer.bind();
        if (this.unitCommander.getPlayer() != null) {
            this.updateExplorationGeometry(0, terrainGrid.getWidth() * terrainGrid.getHeight());
        }
        sArray = new short[12 * terrainGrid.getWidth() * terrainGrid.getHeight()];
        sArray2 = new short[6 * terrainGrid.getWidth() * terrainGrid.getHeight()];
        i8 = 0;
        while (i8 < terrainGrid.getHeight()) {
            int n5 = 0;
            while (n5 < terrainGrid.getWidth()) {
                int n6 = i8 * terrainGrid.getWidth() + n5;
                sArray2[6 * n6 + 0] = (short)(4 * n6 + 0);
                sArray2[6 * n6 + 1] = (short)(4 * n6 + 1);
                sArray2[6 * n6 + 2] = (short)(4 * n6 + 2);
                sArray2[6 * n6 + 3] = (short)(4 * n6 + 0);
                sArray2[6 * n6 + 4] = (short)(4 * n6 + 2);
                sArray2[6 * n6 + 5] = (short)(4 * n6 + 3);
                ++n5;
            }
            ++i8;
        }
        this.fogVertexBuffer = this.useFloatVertices ? new GlBuffer(34962, new float[16 * terrainGrid.getWidth() * terrainGrid.getHeight()], 35040) : new GlBuffer(34962, sArray, 35040);
        this.fogIndexBuffer = new GlBuffer(34963, sArray2, 35044);
        this.fogVertexBuffer.bind();
        if (this.unitCommander.getPlayer() != null) {
            this.updateFogOfWarGeometry(0, terrainGrid.getWidth() * terrainGrid.getHeight());
        }
        i8 = 64;
        sArray = new short[i8 * 12];
        sArray2 = new short[i8 * 6];
        int n7 = 0;
        while (n7 < i8) {
            sArray2[6 * n7 + 0] = (short)(4 * n7 + 0);
            sArray2[6 * n7 + 1] = (short)(4 * n7 + 1);
            sArray2[6 * n7 + 2] = (short)(4 * n7 + 2);
            sArray2[6 * n7 + 3] = (short)(4 * n7 + 0);
            sArray2[6 * n7 + 4] = (short)(4 * n7 + 2);
            sArray2[6 * n7 + 5] = (short)(4 * n7 + 3);
            ++n7;
        }
        this.markerVertexBuffer = this.useFloatVertices ? new GlBuffer(34962, new float[i8 * 16], 35040) : new GlBuffer(34962, sArray, 35040);
        this.markerIndexBuffer = new GlBuffer(34963, sArray2, 35044);
        GameMode gameMode = this.world.getGameMode();
        this.timeStatusBar = new TimeStatusBar(gameMode.hasTimeLimit(), gameMode instanceof CaptureTheFlagMode);
        this.inGameMenuButtonListener = new InGameMenuButtonListener(this);
        this.minimapRenderer = new MinimapRenderer(this.unitCommander);
        this.minimapRenderer.setVisible(false);
        this.minimapPanel = new MinimapPanel(this.hudRenderer.getTexture());
        this.minimapPanel.setExpanded(false);
        this.minimapPanel.setZoomOutEnabled(this.cameraZoom > 0.5f);
        this.minimapPanel.setZoomInEnabled(this.cameraZoom < 1.0f);
        this.zoomButtonListener = new ZoomButtonListener(this);
        this.zoomLabel = new Label(GuiAssets.getFontDokchampa15());
        this.zoomLabel.setMaxWidth(350);
        this.zoomLabel.setText("");
        this.fpsLabel = new Label(GuiAssets.getDefaultFont());
        this.fpsLabel.a(-65536);
        this.fpsLabel.pack();
        this.singleUnitPanel = new SingleUnitPanel(this.unitCommander);
        this.unitPanelListener = new UnitPanelListener(this);
        this.carrierUnitPanel = new CarrierUnitPanel(this.unitCommander);
        this.productionPanelListener = new ProductionPanelListener(this);
        this.buildingPanel = new BuildingPanel(this.unitCommander);
        this.buildPanelListener = new BuildPanelListener(this);
        this.squadPanel = new SquadPanel(this.unitCommander);
        this.multiSelectPanelListener = new MultiSelectPanelListener(this);
        this.groupBar = new GroupBar(this.unitCommander);
        this.squadBarListener = new SquadBarListener(this);
        this.lanNoticeDialog = new MessageDialog(Messages.getFallback("Multiplayer[i18n]: Multiplayer"), Messages.format("LANGameOnlyETC[i18n]: Your game can only be played in your local LAN but not over the internet. To make your game playable over the internet consider the following options:\n\n(A) Enable UPnP in your Router\n(B) Forward Port {0} in your Router\n(C) Directly Connect to the Internet", String.valueOf(UserConfig.getMultiplayerPort())), new String[]{Messages.get("OK[i18n]: OK")});
        this.lanNoticeListener = new LanNoticeListener(this);
        this.nationSelectDialog = new NationSelectDialog(this.world.getPlayers());
        this.playerSlotListener = new PlayerSlotListener(this);
        this.networkProgressDialog = new LoadingProgressDialog(Messages.get("Network[i18n]: Network"), true);
        this.missionBriefingDialog = new MissionBriefingDialog();
        this.missionBriefingDialog.setTitle(Messages.getFallback("MissionObjective[i18n]: Mission Objective"));
        this.missionBriefingDialog.setMessage(this.unitCommander.getWorld().getGameMode().getObjective(this.unitCommander.getPlayer()));
        this.missionBriefingDialog.setPlayers(this.unitCommander.getWorld().getPlayers());
        this.briefingAcceptListener = new BriefingAcceptListener(this);
        this.loadingProgressDialog = new LoadingProgressDialog(Messages.get("Loading[i18n]: Loading"));
        this.inGameMenu = new InGameMenu(this.unitCommander.getWorld().getGameMode().getObjective(this.unitCommander.getPlayer()));
        this.pauseMenuListener = new PauseMenuListener(this);
        this.resultBanner = new ResultBanner();
        this.scoreDialog = new ScoreDialog();
        this.gameOverDismissListener = new GameOverDismissListener(this);
        this.restartDialog = new MessageDialog(Messages.getFallback("RestartGame[i18n]: Restart Game"), Messages.get("DoYouReallyWantToRestartETC[i18n]: Do you really want to restart the game? Your current game will be lost."), new String[]{Messages.get("Restart[i18n]: Restart"), Messages.get("Cancel[i18n]: Cancel")});
        this.restartConfirmListener = new RestartConfirmListener(this);
        this.quitDialog = new MessageDialog(Messages.getFallback("QuitGame[i18n]: Quit Game"), Messages.get("DoYouReallyWantToQuitETC[i18n]: Do you really want to quit the game? Your game will be saved automatically."), new String[]{Messages.get("Quit[i18n]: Quit"), Messages.get("Cancel[i18n]: Cancel")});
        this.quitConfirmListener = new QuitConfirmListener(this);
        Container container = new Container();
        container.addChild(this.timeStatusBar);
        container.addChild(this.zoomLabel);
        container.addChild(this.groupBar);
        container.addChild(this.minimapPanel);
        container.addChild(this.buildingPanel);
        container.addChild(this.carrierUnitPanel);
        container.addChild(this.singleUnitPanel);
        container.addChild(this.squadPanel);
        container.addChild(this.lanNoticeDialog);
        container.addChild(this.nationSelectDialog);
        container.addChild(this.networkProgressDialog);
        container.addChild(this.missionBriefingDialog);
        container.addChild(this.loadingProgressDialog);
        container.addChild(this.inGameMenu);
        container.addChild(this.resultBanner);
        container.addChild(this.scoreDialog);
        container.addChild(this.restartDialog);
        container.addChild(this.quitDialog);
        container.addChild(this.tutorialHintBox);
        if (UserConfig.isDisplayAi()) {
            container.addChild(this.fpsLabel);
        }
        this.hudRenderer.setRootWidget(container);
        this.underLayerQueue = new DrawQueue(terrainGrid.getWidth() * terrainGrid.getHeight() / 9);
        this.baseLayerQueue = new DrawQueue(terrainGrid.getWidth() * terrainGrid.getHeight() * 2);
        this.upperLayerQueue = new DrawQueue(terrainGrid.getWidth() * terrainGrid.getHeight() / 4);
        this.random = new FastRandom();
        i11 = terrainGrid.getWidth() * terrainGrid.getWidth() / 9;
        this.waterSparkleTimes = new long[i11];
        this.waterSparkleX = new int[i11];
        this.waterSparkleY = new int[i11];
        i12 = 0;
        while (i12 < i11) {
            this.waterSparkleTimes[i12] = this.gameTimeNanos + (long)i12 * 1000000000L / (long)i11;
            this.waterSparkleX[i12] = -1;
            this.waterSparkleY[i12] = -1;
            ++i12;
        }
        this.audio.playMusic();
        this.sessionStartTime = System.currentTimeMillis();
        this.setScene(this.scene);
    }

    @Override
    protected void layout() {
        GL20 gL20 = Gdx.gl20;
        int i2 = this.getWidth();
        int i3 = this.getHeight();
        gL20.glViewport(0, 0, i2, i3);
        this.updateProjection();
        TerrainGrid terrainGrid = this.unitCommander.getWorld().getTerrainGrid();
        this.minimapWidth = 128 * (terrainGrid.getWidth() + terrainGrid.getHeight()) / 2;
        this.minimapHeight = 64 * (terrainGrid.getWidth() + terrainGrid.getHeight()) / 2;
        this.at = 3 * i2 / 4 - this.minimapWidth / 2;
        this.au = i2 / 4 + this.minimapWidth / 2;
        this.hudRenderer.setViewportSize(i2, i3);
        this.minimapRenderer.setViewport(i2, i3, this.minimapWidth, this.minimapHeight);
        this.tutorialHintBox.setSize(i2, i3);
        this.timeStatusBar.setX(0.0f);
        this.timeStatusBar.setY(0.0f);
        this.fpsLabel.setX(220.0f);
        this.fpsLabel.setY(6.0f);
        this.minimapPanel.setX(i2 - 255);
        this.minimapPanel.setY(0.0f);
        this.inGameMenu.setX(((float)i2 - this.inGameMenu.getWidth()) / 2.0f);
        this.inGameMenu.setY(((float)i3 - this.inGameMenu.getHeight()) / 2.0f);
        this.lanNoticeDialog.setX(((float)i2 - this.lanNoticeDialog.getWidth()) / 2.0f);
        this.lanNoticeDialog.setY(((float)i3 - this.lanNoticeDialog.getHeight()) / 2.0f);
        this.nationSelectDialog.setX(((float)i2 - this.nationSelectDialog.getWidth()) / 2.0f);
        this.nationSelectDialog.setY(((float)i3 - this.nationSelectDialog.getHeight()) / 2.0f);
        this.networkProgressDialog.setX(((float)i2 - this.networkProgressDialog.getWidth()) / 2.0f);
        this.networkProgressDialog.setY(((float)i3 - this.networkProgressDialog.getHeight()) / 2.0f);
        this.missionBriefingDialog.setX(((float)i2 - this.missionBriefingDialog.getWidth()) / 2.0f);
        this.missionBriefingDialog.setY(((float)i3 - this.missionBriefingDialog.getHeight()) / 2.0f);
        this.loadingProgressDialog.setX(((float)i2 - this.loadingProgressDialog.getWidth()) / 2.0f);
        this.loadingProgressDialog.setY(((float)i3 - this.loadingProgressDialog.getHeight()) / 2.0f);
        this.resultBanner.resize(i2, i3);
        this.resultBanner.setX(((float)i2 - this.resultBanner.getWidth()) / 2.0f);
        this.resultBanner.setY(((float)i3 - this.resultBanner.getHeight()) / 2.0f);
        this.scoreDialog.setX(((float)i2 - this.scoreDialog.getWidth()) / 2.0f);
        this.scoreDialog.setY(((float)i3 - this.scoreDialog.getHeight()) / 2.0f);
        this.restartDialog.setX(((float)i2 - this.restartDialog.getWidth()) / 2.0f);
        this.restartDialog.setY(((float)i3 - this.restartDialog.getHeight()) / 2.0f);
        this.quitDialog.setX(((float)i2 - this.quitDialog.getWidth()) / 2.0f);
        this.quitDialog.setY(((float)i3 - this.quitDialog.getHeight()) / 2.0f);
        int i5 = 122;
        int i6 = 74;
        this.buildingPanel.setX(0.0f);
        this.buildingPanel.setY((float)i5 + ((float)(i3 - i5) - (this.buildingPanel.getHeight() - (float)i6)) / 2.0f);
        this.carrierUnitPanel.setX(0.0f);
        this.carrierUnitPanel.setY((float)i5 + ((float)(i3 - i5) - (this.carrierUnitPanel.getHeight() - (float)i6)) / 2.0f);
        this.singleUnitPanel.setX(0.0f);
        this.singleUnitPanel.setY((float)i5 + ((float)(i3 - i5) - (this.singleUnitPanel.getHeight() - (float)i6)) / 2.0f);
        this.squadPanel.setX(0.0f);
        this.squadPanel.setY((float)i5 + ((float)(i3 - i5) - (this.squadPanel.getHeight() - (float)i6)) / 2.0f);
        this.groupBar.setX(245.0f + ((float)(i2 - 245) - this.groupBar.getWidth()) / 2.0f);
        this.groupBar.setY((float)i3 - this.groupBar.getHeight());
        this.zoomLabel.setX(5.0f);
        this.zoomLabel.setY(this.timeStatusBar.getHeight() + 10.0f);
        this.centerCameraOnPlayer(this.unitCommander.getPlayer());
    }

    private void updateProjection() {
        GL20 gL20 = Gdx.gl20;
        int i2 = this.getWidth();
        int i3 = this.getHeight();
        if (this.vectorTextRenderer != null) {
            this.vectorTextRenderer.setViewport((int)((float)i2 / this.cameraZoom), (int)((float)i3 / this.cameraZoom));
        }
        this.minimapRenderer.setZoom(this.cameraZoom);
        float f4 = this.cameraZoom * 2.0f;
        FloatBuffer floatBuffer = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asFloatBuffer();
        floatBuffer.put(new float[]{f4 / (float)i2, 0.0f, 0.0f, -1.0f, 0.0f, -f4 / (float)i3, 0.0f, 1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f});
        floatBuffer.flip();
        this.spriteShaderProgram.bind();
        gL20.glUniformMatrix4fv(this.mvpMatrixUniform, 1, false, floatBuffer);
        gL20.glUniform1i(this.textureUniform, 0);
    }

    @Override
    public void render() {
        float f;
        int i3;
        int i2;
        ++this.frameCount;
        if (this.cameraZoom != this.targetCameraZoom) {
            this.cameraZoom = this.targetCameraZoom;
            this.updateProjection();
        }
        GL20 gL20 = Gdx.gl20;
        gL20.glClear(16384);
        this.spriteShaderProgram.bind();
        if (this.cameraZoom == 0.5f) {
            i2 = (int)this.cameraPosition.getX() / 2 * 2;
            i3 = (int)this.cameraPosition.getY() / 2 * 2;
        } else {
            i2 = (int)this.cameraPosition.getX();
            i3 = (int)this.cameraPosition.getY();
        }
        if (this.spectatingSince < Float.MAX_VALUE && !this.world.getGameMode().getPlayers().contains(this.unitCommander.getPlayer()) && (f = this.world.getGameTime() - this.spectatingSince) < 2.0f) {
            i2 += (int)((2.0f - f) * (float)(((int)(this.world.getGameTime() * 30.0f) & 1) == 0 ? -1 : 1));
        }
        gL20.glUniform2f(this.translateUniform, i2, i3);
        this.visibleMinTileX = -i2;
        this.visibleMinTileY = -i3;
        this.visibleMaxTileX = (int)((float)this.visibleMinTileX + (float)this.getWidth() / this.cameraZoom + 1.0f);
        this.visibleMaxTileY = (int)((float)this.visibleMinTileY + (float)this.getHeight() / this.cameraZoom + 1.0f);
        Player player = this.unitCommander.getPlayer();
        if (player != null) {
            Sortable sortable;
            int i24;
            ArrayList arrayList;
            int n;
            int n2;
            int i18;
            this.tilesTexture.bind();
            this.terrainVertexBuffer.bind();
            if (this.useFloatVertices) {
                gL20.glVertexAttribPointer(this.positionAttribute, 2, 5126, false, 16, 0);
                gL20.glEnableVertexAttribArray(this.positionAttribute);
                gL20.glVertexAttribPointer(this.texCoordAttribute, 2, 5126, false, 16, 8);
                gL20.glEnableVertexAttribArray(this.texCoordAttribute);
            } else {
                gL20.glVertexAttribPointer(this.positionAttribute, 2, 5122, false, 6, 0);
                gL20.glEnableVertexAttribArray(this.positionAttribute);
                gL20.glVertexAttribPointer(this.texCoordAttribute, 2, 5121, false, 6, 4);
                gL20.glEnableVertexAttribArray(this.texCoordAttribute);
            }
            this.terrainIndexBuffer.bind();
            gL20.glDrawElements(4, this.terrainIndexBuffer.getSize() / 2, 5123, 0);
            gL20.glDisableVertexAttribArray(this.positionAttribute);
            gL20.glDisableVertexAttribArray(this.texCoordAttribute);
            this.unitsTexture.bind();
            int i5 = (int)(this.gameTimeMillis / 100L);
            TerrainGrid terrainGrid = this.unitCommander.getWorld().getTerrainGrid();
            UnitList unitList = this.unitCommander.getWorld().getUnits();
            int i8 = unitList.size();
            int i9 = terrainGrid.getWidth() - 1;
            int i10 = terrainGrid.getHeight() - 1;
            int i11 = i9;
            int i12 = i10;
            int i13 = this.visibleMinTileX / 64 - 2;
            int i14 = this.visibleMinTileY / 32 - 2;
            int i15 = this.visibleMaxTileX / 64 + 2;
            int i16 = this.visibleMaxTileY / 32 + 2;
            do {
                int n3 = i11;
                i18 = i12;
                do {
                    Object object;
                    Sortable sortable2;
                    n2 = n3 - i18;
                    n = n3 + i18;
                    if (n2 >= i13 && n2 <= i15 && n >= i14 && n <= i16) {
                        sortable2 = terrainGrid.getSite(n3, i18);
                        if (sortable2 != null) {
                            this.baseLayerQueue.add(sortable2);
                        }
                        if ((object = terrainGrid.getUnitAtTile(n3, i18)) != null) {
                            this.baseLayerQueue.add((Sortable)object);
                        }
                        arrayList = terrainGrid.getUnitsAtTile(n3, i18);
                        i24 = 0;
                        while (i24 < arrayList.size()) {
                            object = (Unit)arrayList.get(i24);
                            if (((Unit)object).getPosition().getUnit() == null && player.getFogOfWar().isUnitVisible((Unit)object)) {
                                switch (GameScreen.getLayerSwitchMap()[((Unit)object).getUnitType().getLayer().ordinal()]) {
                                    case 1: {
                                        this.underLayerQueue.add((Sortable)object);
                                        this.baseLayerQueue.add((Sortable)object);
                                        break;
                                    }
                                    case 2: {
                                        this.baseLayerQueue.add((Sortable)object);
                                        break;
                                    }
                                    case 3: {
                                        this.upperLayerQueue.add((Sortable)object);
                                        break;
                                    }
                                    default: {
                                        OsfLog.error("Level not implemented: " + (Object)((Object)((Unit)object).getUnitType().getLayer()));
                                    }
                                }
                            }
                            ++i24;
                        }
                    }
                    if ((sortable2 = terrainGrid.getUnitAtTile(n3, i18)) == null || ((Unit)sortable2).getOwner() != player || !((Unit)sortable2).hasMoveTarget()) continue;
                    object = ((Unit)sortable2).getMoveTargetPosition();
                    short s = (short)MathHelper.round(IsoMathHelper.toScreenX(((UnitPosition)object).getX(), ((UnitPosition)object).getY(), 128));
                    i24 = (short)MathHelper.round(IsoMathHelper.toScreenY(((UnitPosition)object).getX(), ((UnitPosition)object).getY(), 64));
                    if (s < this.visibleMinTileX || s > this.visibleMaxTileX || i24 < this.visibleMinTileY || i24 > this.visibleMaxTileY) continue;
                    this.baseLayerQueue.add(((Unit)sortable2).getSortProxy());
                } while (++i18 <= i10 && --n3 >= 0);
                if (i12 > 0) {
                    --i12;
                    continue;
                }
                --i11;
            } while (i11 >= 0 && i12 >= 0);
            while ((sortable = this.underLayerQueue.poll()) != null) {
                this.drawUnitSpriteUnder((Unit)sortable, i5);
            }
            i18 = 0;
            while (i18 < i8) {
                Unit unit = (Unit)unitList.get(i18);
                if (unit.isVolleyActive() && unit.getActiveVolley().getAmmoType() == AmmoType.Torpedo) {
                    this.drawVolley(unit);
                }
                ++i18;
            }
            if (UserConfig.isRenderDetails()) {
                i18 = 0;
                while (i18 < this.waterSparkleTimes.length) {
                    int n4;
                    long l = this.waterSparkleTimes[i18];
                    if (l + 1000000000L < this.gameTimeNanos) {
                        int n5;
                        int n6 = this.random.nextInt(terrainGrid.getWidth());
                        if (terrainGrid.isAllWaterTile(n6, n5 = this.random.nextInt(terrainGrid.getHeight())) && terrainGrid.getSite(n6, n5) == null && terrainGrid.getUnitAtTile(n6, n5) == null && terrainGrid.getUnitsAtTile(n6, n5).size() == 0) {
                            this.waterSparkleX[i18] = n6;
                            this.waterSparkleY[i18] = n5;
                        } else {
                            this.waterSparkleX[i18] = -1;
                            this.waterSparkleY[i18] = -1;
                        }
                        int n7 = i18;
                        this.waterSparkleTimes[n7] = this.waterSparkleTimes[n7] + 1000000000L;
                    }
                    if (this.waterSparkleX[i18] >= 0 && (n4 = (int)((this.gameTimeNanos - this.waterSparkleTimes[i18]) / 1000000L / 140L)) >= 0 && n4 < 7) {
                        this.drawSprite((int)IsoMathHelper.toScreenX((float)this.waterSparkleX[i18] + 0.5f, (float)this.waterSparkleY[i18] + 0.5f, 128) - 8, (int)IsoMathHelper.toScreenY((float)this.waterSparkleX[i18] + 0.5f, (float)this.waterSparkleY[i18] + 0.5f, 64) - 8, 176 + n4 * 16, 1216 + i18 % 3 * 16, 16, 16);
                    }
                    ++i18;
                }
            }
            i18 = this.isSelectionCommandable() ? 1 : 0;
            n2 = 0;
            while (n2 < this.selectedUnits.size()) {
                this.drawUnitSelectionAndOrders((Unit)this.selectedUnits.get(n2), i18 != 0, true, this.selectedUnits.size() <= 1, i5);
                ++n2;
            }
            n2 = 0;
            while (n2 < this.hoveredUnits.size()) {
                this.drawUnitSelectionAndOrders((Unit)this.hoveredUnits.get(n2), false, false, false, i5);
                ++n2;
            }
            if (this.escortTargetArea != null) {
                n2 = (short)MathHelper.round(IsoMathHelper.toScreenX(this.escortTargetArea.getX(), this.escortTargetArea.getY(), 128));
                n = (short)MathHelper.round(IsoMathHelper.toScreenY(this.escortTargetArea.getX(), this.escortTargetArea.getY(), 64));
                int n8 = (int)(this.gameTimeMillis / 160L) % 6;
                if (n8 < 4) {
                    this.drawSprite(n2 - 48, n - 24, 384 + n8 * 96, 1024, 96, 48);
                } else {
                    this.drawSprite(n2 - 48, n - 24, 384 + (6 - n8) * 96, 1024, 96, 48);
                }
            }
            while ((sortable = this.baseLayerQueue.poll()) != null) {
                if (sortable.isSite()) {
                    this.drawSite((Site)sortable);
                    continue;
                }
                if (sortable.isMoveTargetMarker()) {
                    this.drawUnitMoveTargetMarker((PositionedSortable)sortable, i5);
                    continue;
                }
                this.drawUnitSpriteAndOverlays((Unit)sortable, i5);
            }
            n2 = 0;
            while (n2 < i8) {
                Unit unit = (Unit)unitList.get(n2);
                if (unit.isVolleyActive() && unit.getActiveVolley().getAmmoType() != AmmoType.Torpedo) {
                    this.drawVolley(unit);
                }
                ++n2;
            }
            if (UserConfig.isRenderDetails()) {
                BirdList birdList = this.world.getBirds();
                int n9 = 0;
                while (n9 < birdList.size()) {
                    this.drawBird((Bird)birdList.get(n9));
                    ++n9;
                }
            }
            while ((sortable = this.upperLayerQueue.poll()) != null) {
                this.drawUnitSpriteAndOverlays((Unit)sortable, i5);
            }
            if (this.dragging && this.pointerDown) {
                int n10;
                int n11;
                int n12;
                int n13;
                int n14 = 8 + (!TouchDeviceFlags.isTouchDevice() ? 16 : 0);
                if (this.pressX < this.boxSelectStartX) {
                    n13 = this.pressX - n14;
                    n12 = this.boxSelectStartX - this.pressX + n14 + n14;
                } else {
                    n13 = this.boxSelectStartX - n14;
                    n12 = this.pressX - this.boxSelectStartX + n14 + n14;
                }
                if (this.pressY < this.boxSelectStartY) {
                    n11 = this.pressY - n14;
                    n10 = this.boxSelectStartY - this.pressY + n14 + n14;
                } else {
                    n11 = this.boxSelectStartY - n14;
                    n10 = this.pressY - this.boxSelectStartY + n14 + n14;
                }
                this.drawSpriteRegion((n13 -= i2) + 1, (n11 -= i3) + 1, n12 - 1, n10 - 1, 304, 1232, 16, 16);
                this.drawSpriteRegion(n13 - 8, n11, 16, n10, 336, 1232, 16, 16);
                this.drawSpriteRegion(n13 + n12 - 8, n11, 16, n10, 336, 1232, 16, 16);
                this.drawSpriteRegion(n13, n11 - 8, n12, 16, 352, 1216, 16, 16);
                this.drawSpriteRegion(n13, n11 + n10 - 8, n12, 16, 352, 1216, 16, 16);
            }
            this.flushSprites();
            this.tilesTexture.bind();
            if (player.isFogEnabled()) {
                this.fogVertexBuffer.bind();
                FogOfWar fogOfWar = player.getFogOfWar();
                int n15 = fogOfWar.getWidth() * fogOfWar.getHeight();
                if (UserConfig.isRenderDetails()) {
                    if ((this.frameCount & 0xF) == 0) {
                        this.updateFogOfWarGeometry(0, n15);
                    }
                } else if ((this.frameCount & 1) == 0) {
                    int n16 = 64;
                    int n17 = (n15 - 1) / n16 + 1;
                    int n18 = (this.frameCount >> 1) % n17 * n16;
                    if (n18 + n16 > n15) {
                        n16 = n15 - n18;
                    }
                    this.updateFogOfWarGeometry(n18, n16);
                }
                if (this.useFloatVertices) {
                    gL20.glVertexAttribPointer(this.positionAttribute, 2, 5126, false, 16, 0);
                    gL20.glEnableVertexAttribArray(this.positionAttribute);
                    gL20.glVertexAttribPointer(this.texCoordAttribute, 2, 5126, false, 16, 8);
                    gL20.glEnableVertexAttribArray(this.texCoordAttribute);
                } else {
                    gL20.glVertexAttribPointer(this.positionAttribute, 2, 5122, false, 6, 0);
                    gL20.glEnableVertexAttribArray(this.positionAttribute);
                    gL20.glVertexAttribPointer(this.texCoordAttribute, 2, 5121, false, 6, 4);
                    gL20.glEnableVertexAttribArray(this.texCoordAttribute);
                }
                this.fogIndexBuffer.bind();
                gL20.glDrawElements(4, this.fogIndexBuffer.getSize() / 2, 5123, 0);
                gL20.glDisableVertexAttribArray(this.positionAttribute);
                gL20.glDisableVertexAttribArray(this.texCoordAttribute);
            }
            if (player.isExplorationEnabled()) {
                this.lineVertexBuffer.bind();
                FogOfWar fogOfWar = player.getFogOfWar();
                int n19 = fogOfWar.getWidth() * fogOfWar.getHeight();
                if (UserConfig.isRenderDetails()) {
                    if ((this.frameCount & 0xF) == 0) {
                        this.updateExplorationGeometry(0, n19);
                    }
                } else if ((this.frameCount & 1) == 1) {
                    int n20 = 64;
                    int n21 = (n19 - 1) / n20 + 1;
                    int n22 = (this.frameCount >> 1) % n21 * n20;
                    if (n22 + n20 > n19) {
                        n20 = n19 - n22;
                    }
                    this.updateExplorationGeometry(n22, n20);
                }
                if (this.useFloatVertices) {
                    gL20.glVertexAttribPointer(this.positionAttribute, 2, 5126, false, 16, 0);
                    gL20.glEnableVertexAttribArray(this.positionAttribute);
                    gL20.glVertexAttribPointer(this.texCoordAttribute, 2, 5126, false, 16, 8);
                    gL20.glEnableVertexAttribArray(this.texCoordAttribute);
                } else {
                    gL20.glVertexAttribPointer(this.positionAttribute, 2, 5122, false, 6, 0);
                    gL20.glEnableVertexAttribArray(this.positionAttribute);
                    gL20.glVertexAttribPointer(this.texCoordAttribute, 2, 5121, false, 6, 4);
                    gL20.glEnableVertexAttribArray(this.texCoordAttribute);
                }
                this.lineIndexBuffer.bind();
                gL20.glDrawElements(4, this.lineIndexBuffer.getSize() / 2, 5123, 0);
                gL20.glDisableVertexAttribArray(this.positionAttribute);
                gL20.glDisableVertexAttribArray(this.texCoordAttribute);
            }
            if (this.selectedUnits.size() > 0 && ((Unit)this.selectedUnits.get(0)).getGuardPositionOrNull() != null || this.clickMarkerUntilTime > this.world.getGameTime()) {
                Buffer buffer = this.markerVertexBuffer.getData();
                buffer.clear();
                int n23 = 0;
                while (n23 < this.selectedUnits.size()) {
                    Unit unit = (Unit)this.selectedUnits.get(n23);
                    if (unit.getGuardPositionOrNull() != null) {
                        UnitPosition unitPosition = unit.getGuardPositionOrNull();
                        short s = (short)MathHelper.round(IsoMathHelper.toScreenX(unitPosition.getX(), unitPosition.getY(), 128));
                        i24 = (short)MathHelper.round(IsoMathHelper.toScreenY(unitPosition.getX(), unitPosition.getY(), 64));
                        int n24 = (int)(this.gameTimeMillis / 120L) % 6;
                        boolean bl = unit.getGuardPositionOrNull().getUnit() != null && unit.getGuardPositionOrNull().getUnit().canEmbark(unit) || unit.isGuardingActive();
                        int n25 = unit.getGuardPositionOrNull().getUnit() != null ? (int)(unit.getGuardPositionOrNull().getUnit().getUnitType().getMoveFactor() * (float)this.zPixelScale) : 0;
                        this.writeSpriteToBuffer(buffer, s - 41, i24 - 72 - n25, (bl ? 480 : 0) + n24 * 80, 768, 80, 64);
                    }
                    ++n23;
                }
                if (this.clickMarkerUntilTime > this.world.getGameTime()) {
                    int n26;
                    if (this.clickedUnit == null) {
                        n23 = this.clickMarkerScreenX;
                        n26 = this.clickMarkerScreenY;
                    } else {
                        float f2 = this.clickedUnit.getUnitType().getDomain() == Domain.Air ? 0.9f : 0.0f;
                        n23 = (int)IsoMathHelper.toScreenX(this.clickedUnit.getPosition().getX() - f2, this.clickedUnit.getPosition().getY() - f2, 128);
                        n26 = (int)IsoMathHelper.toScreenY(this.clickedUnit.getPosition().getX() - f2, this.clickedUnit.getPosition().getY() - f2, 64);
                    }
                    this.writeSpriteToBuffer(buffer, n23 - 64, n26 - 32, 896, 320, 128, 64);
                }
                if (this.useFloatVertices) {
                    n23 = buffer.position() / 16;
                    if (n23 > 0) {
                        this.markerVertexBuffer.bind();
                        buffer.flip();
                        this.markerVertexBuffer.uploadRange(0, n23 * 64);
                        gL20.glVertexAttribPointer(this.positionAttribute, 2, 5126, false, 16, 0);
                        gL20.glEnableVertexAttribArray(this.positionAttribute);
                        gL20.glVertexAttribPointer(this.texCoordAttribute, 2, 5126, false, 16, 8);
                        gL20.glEnableVertexAttribArray(this.texCoordAttribute);
                        this.markerIndexBuffer.bind();
                        gL20.glDrawElements(4, n23 * 6, 5123, 0);
                        gL20.glDisableVertexAttribArray(this.positionAttribute);
                        gL20.glDisableVertexAttribArray(this.texCoordAttribute);
                    }
                } else {
                    n23 = buffer.position() / 12;
                    if (n23 > 0) {
                        this.markerVertexBuffer.bind();
                        buffer.flip();
                        this.markerVertexBuffer.uploadRange(0, n23 * 24);
                        gL20.glVertexAttribPointer(this.positionAttribute, 2, 5122, false, 6, 0);
                        gL20.glEnableVertexAttribArray(this.positionAttribute);
                        gL20.glVertexAttribPointer(this.texCoordAttribute, 2, 5121, false, 6, 4);
                        gL20.glEnableVertexAttribArray(this.texCoordAttribute);
                        this.markerIndexBuffer.bind();
                        gL20.glDrawElements(4, n23 * 6, 5123, 0);
                        gL20.glDisableVertexAttribArray(this.positionAttribute);
                        gL20.glDisableVertexAttribArray(this.texCoordAttribute);
                    }
                }
            }
            if (this.vectorTextRenderer != null && this.aiPlanners != null) {
                this.vectorTextRenderer.bindShader();
                int n27 = 0;
                while (n27 < this.aiPlanners.length) {
                    AIPlanner aIPlanner = this.aiPlanners[n27];
                    int n28 = this.playerColors[n27];
                    int n29 = (int)(20.0f / this.cameraZoom);
                    this.vectorTextRenderer.drawText(n29, (6 + n27) * n29, "P" + aIPlanner.getContext().c() + " M" + aIPlanner.getContext().getPlayer().getResources(), (int)((double)n29 * 0.8), n28);
                    arrayList = aIPlanner.getPlanNodes();
                    i24 = 0;
                    while (i24 < arrayList.size()) {
                        PlanContext planContext = (PlanContext)aIPlanner.getPlanContexts().get(i24);
                        PlanNode planNode = ((PlanNode)arrayList.get(i24)).getActiveNode(planContext);
                        Intent intent = planContext.getIntent();
                        Vec2 vec2 = intent.getTargetPosition();
                        if (vec2 != null) {
                            this.drawAiDebugCross(vec2, n28);
                            this.drawAiDebugLabel(vec2, intent.getType().getName(), n28);
                            IntentGroupList intentGroupList = intent.getGroupList();
                            UnitPosition unitPosition = null;
                            int i31 = 0;
                            while (i31 < intentGroupList.size()) {
                                IntentGroup intentGroup = (IntentGroup)intentGroupList.get(i31);
                                Unit unit = intentGroup.getHighestSortWeightUnit();
                                if (unit != null) {
                                    UnitPosition unitPosition2 = intentGroup.getHighestSortWeightUnit().getPosition();
                                    this.drawAiDebugTriangle(unitPosition2, n28);
                                    this.drawAiDebugLabel((Vec2)unitPosition2, planNode.getName(), n28);
                                    if (i31 == 0) {
                                        unitPosition = unitPosition2;
                                    } else if (unitPosition != null) {
                                        this.drawAiDebugLine((Vec2)unitPosition2, unitPosition, n28);
                                    }
                                    UnitList unitList2 = intentGroup.getUnits();
                                    int i36 = 0;
                                    while (i36 < unitList2.size()) {
                                        Unit unit2 = (Unit)unitList2.get(i36);
                                        if (unit2 != unit) {
                                            this.drawAiDebugBox(unit2.getPosition(), n28);
                                            this.drawAiDebugLine((Vec2)unitPosition2, unit2.getPosition(), n28);
                                        }
                                        ++i36;
                                    }
                                    if (intentGroup.getTargetPosition() != null) {
                                        this.drawAiDebugLine((Vec2)unitPosition2, intentGroup.getTargetPosition(), n28);
                                        if (i31 == 0 && vec2 != null) {
                                            this.drawAiDebugLine(intentGroup.getTargetPosition(), vec2, n28);
                                        }
                                    } else if (i31 == 0 && vec2 != null) {
                                        this.drawAiDebugLine((Vec2)unitPosition2, vec2, n28);
                                    }
                                }
                                ++i31;
                            }
                        }
                        ++i24;
                    }
                    ++n27;
                }
                this.vectorTextRenderer.flush();
            }
            this.minimapRenderer.render();
        }
        this.hudRenderer.render();
        this.audio.updateMusic();
        super.render();
    }

    private final void drawUnitSelectionAndOrders(Unit unit, boolean bl, boolean bl2, boolean bl3, int i5) {
        int n;
        int n2;
        UnitType unitType = unit.getUnitType();
        UnitPosition unitPosition = unit.getPosition();
        short i8 = (short)MathHelper.round(IsoMathHelper.toScreenX(unitPosition.getX(), unitPosition.getY(), 128));
        short i9 = (short)MathHelper.round(IsoMathHelper.toScreenY(unitPosition.getX(), unitPosition.getY(), 64));
        if (bl) {
            n2 = 1408 + i5 % 4 * 96;
            n = 1104;
            this.drawSprite(i8 - 48, i9 - 24, n2, n, 96, 48);
        } else if (unit.isImmobile()) {
            n2 = 1152 + i5 % 4 * 160;
            n = 1024;
            this.drawSprite(i8 - 80, i9 - 40, n2, n, 160, 80);
        } else if (!bl2) {
            n2 = i5 % 4 * 96;
            n = 1168;
            this.drawSprite(i8 - 48, i9 - 24, n2, n, 96, 48);
        }
        if (bl2) {
            if (bl3 && (unit.getOrderMode() == UnitOrderMode.b || unit.getOrderMode() == UnitOrderMode.c) && unit.getGuardPositionOrNull() != null && unit.getRallyPositionOrNull() != null) {
                Domain domain = unitType.getDomain();
                float f = unit.getRallyPositionOrNull().getX();
                float f2 = unit.getRallyPositionOrNull().getY();
                float f3 = unit.getGuardPositionOrNull().getX();
                float f14 = unit.getGuardPositionOrNull().getY();
                Neighbor neighbor = this.world.getTerrainGrid().findReachableNeighbor(domain, f, f2, f3, f14);
                float f16 = 0.0f;
                float f17 = f;
                float f18 = f2;
                float f19 = (float)((int)f17 + neighbor.getDx()) + 0.5f;
                float f20 = (float)((int)f18 + neighbor.getDy()) + 0.5f;
                while ((f17 != f3 || f18 != f14) && neighbor != Neighbor.Center && neighbor != Neighbor.None) {
                    float f21 = f19 - f17;
                    float f22 = f20 - f18;
                    float f23 = MathHelper.sqrt(f21 * f21 + f22 * f22);
                    if (f23 > f16) {
                        f16 = 0.4f;
                        float f24 = IsoMathHelper.toScreenX(f17 += f21 * f16 / f23, f18 += f22 * f16 / f23, 128);
                        float f25 = IsoMathHelper.toScreenY(f17, f18, 64);
                        this.drawSprite(MathHelper.round(f24) - 8, MathHelper.round(f25) - 8, 144, 1280, 16, 16);
                        continue;
                    }
                    if ((f16 -= f23) < 1.0E-4f) {
                        f16 = 1.0E-4f;
                    }
                    neighbor = this.world.getTerrainGrid().findReachableNeighbor(domain, f17, f18, f3, f14);
                    f17 = f19;
                    f18 = f20;
                    f19 = (float)((int)f17 + neighbor.getDx()) + 0.5f;
                    f20 = (float)((int)f18 + neighbor.getDy()) + 0.5f;
                    if ((int)f19 != (int)f3 || (int)f20 != (int)f14) continue;
                    f19 = f3;
                    f20 = f14;
                }
            }
            if (!(unit.getGuardPositionOrNull() == null || unit.getGuardPositionOrNull().getUnit() != null && unit.getGuardPositionOrNull().getUnit().isImmobile() || unit.isGuardingActive() || unit.getGuardPositionOrNull().getUnit() != null && unit.getGuardPositionOrNull().getUnit().canEmbark(unit))) {
                UnitPosition unitPosition2 = unit.getGuardPositionOrNull();
                short s = (short)MathHelper.round(IsoMathHelper.toScreenX(unitPosition2.getX(), unitPosition2.getY(), 128));
                short s2 = (short)MathHelper.round(IsoMathHelper.toScreenY(unitPosition2.getX(), unitPosition2.getY(), 64));
                int n3 = (int)(this.gameTimeMillis / 160L) % 6;
                if (n3 < 4) {
                    this.drawSprite(s - 48, s2 - 24, 768 + n3 * 96, 1024, 96, 48);
                } else {
                    this.drawSprite(s - 48, s2 - 24, 768 + (6 - n3) * 96, 1024, 96, 48);
                }
            }
            if (unit.hasMoveTarget()) {
                UnitPosition unitPosition3 = unit.getMoveTargetPosition();
                short s = (short)MathHelper.round(IsoMathHelper.toScreenX(unitPosition3.getX(), unitPosition3.getY(), 128));
                short s3 = (short)MathHelper.round(IsoMathHelper.toScreenY(unitPosition3.getX(), unitPosition3.getY(), 64));
                int n4 = (int)(this.gameTimeMillis / 60L) % 8;
                switch (n4) {
                    case 0: {
                        this.drawSprite(s - 32, s3 - 16, 256, 1120, 64, 32);
                        break;
                    }
                    case 1: {
                        this.drawSprite(s - 32, s3 - 16, 320, 1120, 64, 32);
                        break;
                    }
                    case 2: {
                        this.drawSprite(s - 32, s3 - 16, 320, 1264, 64, 32);
                        break;
                    }
                    case 3: {
                        this.drawSprite(s - 32, s3 - 16, 320, 1296, 64, 32);
                        break;
                    }
                    default: {
                        this.drawSprite(s - 32, s3 - 16, 384, 1200 + (n4 - 4) * 32, 64, 32);
                    }
                }
            }
        }
    }

    private final void drawSite(Site site) {
        Vec2 vec2 = site.getPosition();
        short i3 = (short)MathHelper.round(IsoMathHelper.toScreenX(vec2.getX(), vec2.getY(), 128));
        short i4 = (short)MathHelper.round(IsoMathHelper.toScreenY(vec2.getX(), vec2.getY(), 64));
        SpriteAnimationSet spriteAnimationSet = this.buildingSpriteTable.getAnimationSet(site);
        int i6 = spriteAnimationSet.getListCount();
        int i7 = 0;
        while (i7 < i6) {
            SpriteRegion spriteRegion = spriteAnimationSet.getRegion(i7, this.gameTimeMillis);
            this.drawSprite(i3 + spriteRegion.getX(), i4 + spriteRegion.getY(), spriteRegion.getWidth(), spriteRegion.getHeight(), spriteRegion.getOffsetX(), spriteRegion.getOffsetY());
            ++i7;
        }
    }

    private final void drawUnitSpriteUnder(Unit unit, int i2) {
        UnitType unitType = unit.getUnitType();
        UnitPosition unitPosition = unit.getPosition();
        short i5 = (short)MathHelper.round(IsoMathHelper.toScreenX(unitPosition.getX(), unitPosition.getY(), 128));
        short i6 = (short)MathHelper.round(IsoMathHelper.toScreenY(unitPosition.getX(), unitPosition.getY(), 64));
        if (!unit.isDestroyed()) {
            int i10;
            float f;
            EffectTimer effectTimer = unit.getActiveEffectTimer();
            if (effectTimer != null && (f = effectTimer.getTime()) < 0.4f) {
                i5 = (short)(i5 + (((int)(effectTimer.getTime() * 30.0f) & 1) == 0 ? -1 : 1));
            }
            // A unit type can be missing from graphic_unit.cfg (e.g. one added by a mod): the
            // table then holds null for it, so skip its sprite instead of dereferencing it.
            UnitSpriteAnimation unitSpriteAnimation = this.unitSpriteTable != null ? this.unitSpriteTable.getAnimation(unit) : null;
            SpriteRegion spriteRegion = unitSpriteAnimation == null ? null : unitSpriteAnimation.getProgressRegion(unit.isGuarding(), unit.getMoveProgress());
            if (spriteRegion != null) {
                this.drawSprite(i5 + spriteRegion.getX(), i6 + spriteRegion.getY(), spriteRegion.getWidth(), spriteRegion.getHeight(), spriteRegion.getOffsetX(), spriteRegion.getOffsetY());
            }
            if (unitSpriteAnimation != null && (spriteRegion == null || UserConfig.isRenderDetails()) && (spriteRegion = unitSpriteAnimation.getTimedRegion(this.gameTimeMillis)) != null) {
                this.drawSprite(i5 + spriteRegion.getX(), i6 + spriteRegion.getY(), spriteRegion.getWidth(), spriteRegion.getHeight(), spriteRegion.getOffsetX(), spriteRegion.getOffsetY());
            }
            if (effectTimer != null && (i10 = (int)(15.0f * effectTimer.getTime())) < 8) {
                int i11 = (int)(1000.0f * (this.world.getGameTime() - effectTimer.getTime()));
                int i12 = (i11 & 0xF) - 8 << 1;
                int i13 = ((i11 & 0xF0) >> 4) - 8 << 1;
                int i14 = (int)(unitType.getMoveFactor() * (float)this.zPixelScale);
                this.drawSprite(i12 + i5 - 48, i13 + i6 - 48 - i14, 384 + i10 * 80, 1072, 80, 64);
            }
        }
    }

    private final void drawUnitSpriteAndOverlays(Unit unit, int i2) {
        Player player = this.unitCommander.getPlayer();
        Player player2 = unit.getOwner();
        UnitType unitType = unit.getUnitType();
        UnitPosition unitPosition = unit.getPosition();
        boolean i7 = player.getFogOfWar().isUnitCurrentlyVisible(unit);
        short i8 = (short)MathHelper.round(IsoMathHelper.toScreenX(unitPosition.getX(), unitPosition.getY(), 128));
        short i9 = (short)MathHelper.round(IsoMathHelper.toScreenY(unitPosition.getX(), unitPosition.getY(), 64));
        if (unit.isDestroyed()) {
            if (i7 && unit.isEffectActive()) {
                EffectTimer effectTimer = unit.getActiveEffectTimer();
                int n = (int)(13.0f * effectTimer.getTime());
                if (this.world.getTerrainGrid().isAllWaterTileAtPosition(unit.getPosition())) {
                    if (n < 8) {
                        int n2 = (int)(unitType.getMoveFactor() * (float)this.zPixelScale);
                        this.drawSprite(i8 - 48, i9 - 48 - n2, 448 + n * 96, 1232, 96, 96);
                    } else if (n < 16) {
                        int n3 = (int)(unitType.getMoveFactor() * (float)this.zPixelScale);
                        this.drawSprite(i8 - 48, i9 - 48 - n3, 448 + (n - 8) * 96, 1136, 96, 96);
                    }
                } else if (n < 14) {
                    int n4 = (int)(unitType.getMoveFactor() * (float)this.zPixelScale);
                    this.drawSprite(i8 - 48, i9 - 48 - n4, 448 + n * 96, 1232, 96, 96);
                }
            }
        } else {
            GameMode gameMode;
            int i15;
            float f;
            EffectTimer effectTimer = unit.getActiveEffectTimer();
            if (i7 && effectTimer != null && (f = effectTimer.getTime()) < 0.4f) {
                i8 = (short)(i8 + (((int)(effectTimer.getTime() * 30.0f) & 1) == 0 ? -1 : 1));
            }
            // See drawUnitSpriteUnder: a unit type without sprite definitions yields null here.
            UnitSpriteAnimation unitSpriteAnimation = this.unitSpriteTable != null ? this.unitSpriteTable.getAnimation(unit) : null;
            if (unitSpriteAnimation != null && unitType.getLayer() != Layer.Under) {
                SpriteRegion spriteRegion = unitSpriteAnimation.getProgressRegion(unit.isGuarding(), unit.getMoveProgress());
                if (spriteRegion != null) {
                    this.drawSprite(i8 + spriteRegion.getX(), i9 + spriteRegion.getY(), spriteRegion.getWidth(), spriteRegion.getHeight(), spriteRegion.getOffsetX(), spriteRegion.getOffsetY());
                }
                if ((spriteRegion == null || UserConfig.isRenderDetails()) && (spriteRegion = unitSpriteAnimation.getTimedRegion(this.gameTimeMillis)) != null) {
                    this.drawSprite(i8 + spriteRegion.getX(), i9 + spriteRegion.getY(), spriteRegion.getWidth(), spriteRegion.getHeight(), spriteRegion.getOffsetX(), spriteRegion.getOffsetY());
                }
            }
            int n = i8 + (unitSpriteAnimation != null ? unitSpriteAnimation.getUnitTypeId() : 0);
            int i13 = i9 + (unitSpriteAnimation != null ? unitSpriteAnimation.getFacing() : 0);
            if (player2 != null) {
                int n5 = player2.getFaction().getId() * 32;
                i15 = (i2 + unit.getId()) % 8 * 32;
                this.drawSprite(n, i13 - 12, 1792 + i15, 1024 + n5, 32, 32);
                if (i7) {
                    int i19;
                    int i18;
                    int i16 = unit.getHealth() * 9 / unitType.getHealthScale();
                    this.drawSprite(n - 9, i13 - 18, 16 * i16, 1216, 16, 32);
                    if (unitType.isFlying()) {
                        int n6 = MathHelper.round(unit.getProgress() * 9.0f / unitType.getAltitude());
                        this.drawSprite(n - 16, i13 - 18, 16 * n6, 1248, 16, 32);
                    }
                    if (unit.isHighValue()) {
                        this.drawSprite(n + 6, i13 - 9, 144, 1296, 16, 16);
                    } else if (unit.getOwner() == player) {
                        if (unit.getOrderMode() == UnitOrderMode.b) {
                            this.drawSprite(n + 6, i13 - 9, 256, 1312, 16, 16);
                        } else if (unit.getOrderMode() == UnitOrderMode.c) {
                            this.drawSprite(n + 6, i13 - 9, 272, 1312, 16, 16);
                        } else if (unit.getOrderMode() == UnitOrderMode.d || unit.isHostedAuto()) {
                            this.drawSprite(n + 6, i13 - 9, 304, 1312, 16, 16);
                        }
                    }
                    switch (unit.getRank()) {
                        case 1: {
                            this.drawSprite(n - 2, i13 - 28, 416, 1136, 32, 16);
                            break;
                        }
                        case 2: {
                            this.drawSprite(n - 2, i13 - 28, 416, 1152, 32, 16);
                            break;
                        }
                        case 3: {
                            this.drawSprite(n - 2, i13 - 28, 416, 1168, 32, 16);
                        }
                    }
                    UnitGroup unitGroup = unit.getUnitGroup();
                    if (unitGroup != null && unit.getOwner() == player) {
                        i18 = unitType.getDomain() == Domain.Amphibian ? 1 : unitType.getDomain().ordinal();
                        this.drawSprite(n - 13, i13 - 35, 208 + 16 * i18, 1296, 16, 16);
                        this.drawSprite(n - 13, i13 - 35, 272 + 16 * unitGroup.getId(), 1152, 16, 16);
                    }
                    if (unitType.canCarry()) {
                        i18 = unit.getSubUnits().size();
                        this.drawSprite(n - 23, i13 - 19, 16 * i18, 1280, 16, 32);
                        i19 = unit.getSubUnits().getActiveCount();
                        if (i19 > 0) {
                            this.drawSprite(n - 23, i13 - 19, 176 + 16 * i19, 1264, 16, 32);
                        }
                    }
                    if (effectTimer != null && (i18 = (int)(15.0f * effectTimer.getTime())) < 8) {
                        i19 = (int)(1000.0f * (this.world.getGameTime() - effectTimer.getTime()));
                        int i20 = (i19 & 0xF) - 8 << 1;
                        int i21 = ((i19 & 0xF0) >> 4) - 8 << 1;
                        int i22 = (int)(unitType.getMoveFactor() * (float)this.zPixelScale);
                        this.drawSprite(i20 + i8 - 48, i21 + i9 - 48 - i22, 384 + i18 * 80, 1072, 80, 64);
                    }
                    if (unit.getHealth() <= 250) {
                        i18 = i2 % 6 * 64;
                        i19 = (int)(unitType.getMoveFactor() * (float)this.zPixelScale);
                        this.drawSprite(i8 - 27, i9 - 87 - i19, 1984, 1664 + i18, 64, 64);
                    }
                    if (unit.getUnitType().isBuilding() && unit.getOwner() == player && this.bM > this.gameTimeNanos - 1000000000L) {
                        i18 = (int)((this.gameTimeNanos - this.bM) / 20000000L);
                        this.drawSprite(i8 - 32, i9 - 64 - i18, 1024 + i18 / 2 % 2 * 64, 1072, 64, 64);
                    }
                }
            }
            if ((gameMode = this.world.getGameMode()) instanceof CaptureTheFlagMode && ((CaptureTheFlagMode)gameMode).getFlag() == unit) {
                i15 = i2 % 8 * 32;
                if (unit.getOwner() != null) {
                    i13 -= 26;
                }
                this.drawSprite(n, i13 - 12, 0 + i15, 1120, 32, 32);
            }
        }
    }

    private final void drawUnitMoveTargetMarker(PositionedSortable positionedSortable, int i2) {
        UnitPosition unitPosition = positionedSortable.getPosition();
        short i4 = (short)MathHelper.round(IsoMathHelper.toScreenX(unitPosition.getX(), unitPosition.getY(), 128));
        short i5 = (short)MathHelper.round(IsoMathHelper.toScreenY(unitPosition.getX(), unitPosition.getY(), 64));
        this.drawSprite(i4 - 4, i5 - 70, i2 % 8 * 48, 1040, 48, 80);
    }

    private final void drawVolley(Unit unit) {
        Player player = this.unitCommander.getPlayer();
        Volley volley = unit.getActiveVolley();
        Unit unit2 = volley.getTargetUnit();
        if ((player.getFogOfWar().isUnitVisible(unit) || unit2.getOwner() == player) && volley.getIntensity() > 0.35f) {
            Vec3 vec3 = volley.getOrigin();
            float f6 = IsoMathHelper.toScreenX(vec3.getX(), vec3.getY(), 128);
            float f7 = IsoMathHelper.toScreenY(vec3.getX(), vec3.getY(), 64);
            float f8 = volley.getDirection().getX();
            float f9 = volley.getDirection().getY();
            float f10 = MathHelper.atan2(f9, f8);
            int i11 = MathHelper.round(f10 * 8.0f / (float)Math.PI) + 18 & 0xF;
            int i12 = (int)(volley.getOrigin().getZ() * (float)this.zPixelScale);
            AmmoType ammoType = volley.getAmmoType();
            switch (GameScreen.getAmmoTypeSwitchMap()[ammoType.ordinal()]) {
                case 1: {
                    this.drawSprite(MathHelper.round(f6) - 8, MathHelper.round(f7) - 8 - i12, 160, 1280, 16, 16);
                    break;
                }
                case 2: {
                    this.drawSprite(MathHelper.round(f6) - 8, MathHelper.round(f7) - 8 - i12, i11 * 16, 1152, 16, 16);
                    break;
                }
                case 3: {
                    float f = volley.getDirection().getZ();
                    float f2 = MathHelper.atan2(f, MathHelper.sqrt(f8 * f8 + f9 * f9));
                    if (f2 < -0.2f) {
                        this.drawSprite(MathHelper.round(f6) - 32, MathHelper.round(f7) - 24 - i12, i11 * 64, 976, 64, 48);
                        break;
                    }
                    if (f2 > 0.2f) {
                        this.drawSprite(MathHelper.round(f6) - 32, MathHelper.round(f7) - 24 - i12, i11 * 64, 928, 64, 48);
                        break;
                    }
                    this.drawSprite(MathHelper.round(f6) - 32, MathHelper.round(f7) - 16 - i12, i11 * 64, 896, 64, 32);
                    break;
                }
                case 4: {
                    int n = (int)(this.gameTimeMillis / 25L) % 4 * 16;
                    this.drawSprite(MathHelper.round(f6) - 8, MathHelper.round(f7) - 8 - i12, 256 + n, 1296, 16, 16);
                    break;
                }
                case 5: {
                    this.drawSprite(MathHelper.round(f6) - 32, MathHelper.round(f7) - 16 - i12, 0 + i11 * 64, 1328, 64, 32);
                    break;
                }
                default: {
                    OsfLog.error("Projectile cannot be rendered: " + (Object)((Object)ammoType));
                }
            }
            if (UserConfig.isRenderDetails() && ammoType.hasSubProjectiles()) {
                ProjectileList projectileList = volley.getProjectiles();
                int n = 0;
                while (n < projectileList.size()) {
                    Projectile projectile = (Projectile)projectileList.get(n);
                    if (projectile.isActive()) {
                        Vec3 vec32 = projectile.getPosition();
                        int i18 = (int)(projectile.getPosition().getZ() * (float)this.zPixelScale);
                        int i19 = ammoType == AmmoType.Missile ? 0 : 128;
                        int i20 = (int)((this.world.getGameTime() - projectile.getLaunchTime()) / ammoType.getAnimationDuration() * 2.0f);
                        if (i20 < 8) {
                            this.drawSprite(MathHelper.round(IsoMathHelper.toScreenX(vec32.getX(), vec32.getY(), 128)) - 8, MathHelper.round(IsoMathHelper.toScreenY(vec32.getX(), vec32.getY(), 64)) - 8 - i18, i19 + i20 * 16, 1312, 16, 16);
                        }
                    }
                    ++n;
                }
            }
        }
    }

    private final void drawBird(Bird bird) {
        boolean i8;
        boolean i7;
        Vec3 vec3 = bird.getPosition();
        float f3 = IsoMathHelper.toScreenX(vec3.getX(), vec3.getY(), 128);
        float f4 = IsoMathHelper.toScreenY(vec3.getX(), vec3.getY(), 64);
        int i5 = (int)(bird.getDistance() * 40.0f) % 12;
        int i6 = (int)(4.0f * (bird.getAngle() + 0.7853982f) / ((float)Math.PI * 2));
        switch (i6) {
            case 0: {
                i7 = true;
                i8 = true;
                break;
            }
            case 1: {
                i7 = false;
                i8 = true;
                break;
            }
            case 2: {
                i7 = false;
                i8 = false;
                break;
            }
            case 3: {
                i7 = true;
                i8 = false;
                break;
            }
            default: {
                OsfLog.info("Illegal bird direction: " + i6);
                i7 = false;
                i8 = false;
            }
        }
        switch (GameScreen.getBirdSwitchMap()[bird.getType().ordinal()]) {
            case 1: {
                this.drawSpriteFlipped(MathHelper.round(f3) - 24, MathHelper.round(f4) - 32, 1408 + i5 * 48, 1728 + (i8 ? 80 : 0), 48, 80, i7, false);
                break;
            }
            case 2: {
                this.drawSpriteFlipped(MathHelper.round(f3) - 24, MathHelper.round(f4) - 32, 1408 + i5 * 48, 1888 + (i8 ? 80 : 0), 48, 80, i7, false);
                break;
            }
            default: {
                OsfLog.info("Bird cannot be rendered: " + (Object)((Object)bird.getType()));
            }
        }
    }

    private final void drawAiDebugLabel(Vec2 vec2, String string, int i3) {
        int i4 = MathHelper.round(IsoMathHelper.toScreenX(vec2.getX(), vec2.getY(), 128) - 25.0f);
        int i5 = MathHelper.round(IsoMathHelper.toScreenY(vec2.getX(), vec2.getY(), 64) + 16.0f);
        this.vectorTextRenderer.drawText((float)i4 + this.cameraPosition.getX(), (float)i5 + this.cameraPosition.getY(), string, 16, i3);
    }

    private final void drawAiDebugBox(Vec2 vec2, int i2) {
        float f3 = IsoMathHelper.toScreenX(vec2.getX(), vec2.getY(), 128);
        float f4 = IsoMathHelper.toScreenY(vec2.getX(), vec2.getY(), 64);
        int i5 = MathHelper.round(f3 - 16.0f);
        int i6 = MathHelper.round(f4 - 8.0f);
        int i7 = MathHelper.round(f3 + 16.0f);
        int i8 = MathHelper.round(f4 + 8.0f);
        this.drawAiDebugLinePixels(i5, i6, i7, i6, i2);
        this.drawAiDebugLinePixels(i7, i6, i7, i8, i2);
        this.drawAiDebugLinePixels(i7, i8, i5, i8, i2);
        this.drawAiDebugLinePixels(i5, i8, i5, i6, i2);
    }

    private final void drawAiDebugCross(Vec2 vec2, int i2) {
        float f3 = IsoMathHelper.toScreenX(vec2.getX(), vec2.getY(), 128);
        float f4 = IsoMathHelper.toScreenY(vec2.getX(), vec2.getY(), 64);
        int i5 = MathHelper.round(f3 - 25.0f);
        int i6 = MathHelper.round(f4 - 12.0f);
        int i7 = MathHelper.round(f3 + 25.0f);
        int i8 = MathHelper.round(f4 + 12.0f);
        this.drawAiDebugLinePixels(i5, i6, i7, i8, i2);
        this.drawAiDebugLinePixels(i5, i8, i7, i6, i2);
    }

    private final void drawAiDebugTriangle(Vec2 vec2, int i2) {
        float f3 = IsoMathHelper.toScreenX(vec2.getX(), vec2.getY(), 128);
        float f4 = IsoMathHelper.toScreenY(vec2.getX(), vec2.getY(), 64);
        int i5 = MathHelper.round(f3 - 25.0f);
        int i6 = MathHelper.round(f4 + 12.0f);
        int i7 = MathHelper.round(f3 + 25.0f);
        int i8 = MathHelper.round(f4 + 12.0f);
        int i9 = MathHelper.round(f3);
        int i10 = MathHelper.round(f4 - 16.0f);
        this.drawAiDebugLinePixels(i5, i6, i7, i8, i2);
        this.drawAiDebugLinePixels(i7, i8, i9, i10, i2);
        this.drawAiDebugLinePixels(i9, i10, i5, i6, i2);
    }

    private final void drawAiDebugLine(Vec2 vec2, Vec2 vec22, int i3) {
        this.drawAiDebugLinePixels((int)IsoMathHelper.toScreenX(vec2.getX(), vec2.getY(), 128), (int)IsoMathHelper.toScreenY(vec2.getX(), vec2.getY(), 64), (int)IsoMathHelper.toScreenX(vec22.getX(), vec22.getY(), 128), (int)IsoMathHelper.toScreenY(vec22.getX(), vec22.getY(), 64), i3);
    }

    private final void drawAiDebugLinePixels(int i1, int i2, int i3, int i4, int i5) {
        this.vectorTextRenderer.drawLine((float)i1 + this.cameraPosition.getX(), (float)i2 + this.cameraPosition.getY(), (float)i3 + this.cameraPosition.getX(), (float)i4 + this.cameraPosition.getY(), i5);
    }

    private final void updateExplorationGeometry(int i1, int i2) {
        if (this.useFloatVertices) {
            FloatBuffer floatBuffer = (FloatBuffer)this.lineVertexBuffer.getData();
            floatBuffer.position(0);
            floatBuffer.limit(i2 * 4 * 4);
            int i4 = i1 + i2;
            FogOfWar fogOfWar = this.unitCommander.getPlayer().getFogOfWar();
            int i6 = 128;
            int i7 = 64;
            int i8 = i1;
            while (i8 < i4) {
                int i15;
                int i14;
                int i10;
                int i9 = i8 % fogOfWar.getWidth();
                int i11 = (fogOfWar.isCornerExplored(i9, i10 = i8 / fogOfWar.getHeight()) ? 0 : 1) + (fogOfWar.isCornerExplored(i9 + 1, i10) ? 0 : 2) + (fogOfWar.isCornerExplored(i9 + 1, i10 + 1) ? 0 : 4) + (fogOfWar.isCornerExplored(i9, i10 + 1) ? 0 : 8);
                int i12 = 128 * (i11 % 8);
                int i13 = 640 + (i11 >= 8 ? 64 : 0);
                if (i11 > 0) {
                    i14 = (short)IsoMathHelper.toScreenX(i9, i10, 128) - 64;
                    i15 = (short)IsoMathHelper.toScreenY(i9, i10, 64);
                    floatBuffer.put((short)i14);
                    floatBuffer.put((short)i15);
                    floatBuffer.put(this.packUvScaled(i12, i13) & 0xFF);
                    floatBuffer.put((this.packUvScaled(i12, i13) & 0xFF00) >>> 8);
                    floatBuffer.put((short)(i14 + i6));
                    floatBuffer.put((short)i15);
                    floatBuffer.put(this.packUvScaled(i12 + i6, i13) & 0xFF);
                    floatBuffer.put((this.packUvScaled(i12 + i6, i13) & 0xFF00) >>> 8);
                    floatBuffer.put((short)(i14 + i6));
                    floatBuffer.put((short)(i15 + i7));
                    floatBuffer.put(this.packUvScaled(i12 + i6, i13 + i7) & 0xFF);
                    floatBuffer.put((this.packUvScaled(i12 + i6, i13 + i7) & 0xFF00) >>> 8);
                    floatBuffer.put((short)i14);
                    floatBuffer.put((short)(i15 + i7));
                    floatBuffer.put(this.packUvScaled(i12, i13 + i7) & 0xFF);
                    floatBuffer.put((this.packUvScaled(i12, i13 + i7) & 0xFF00) >>> 8);
                } else {
                    i14 = -30000;
                    i15 = -30000;
                    floatBuffer.put((short)i14);
                    floatBuffer.put((short)(i15 - 1));
                    floatBuffer.put(this.packUvScaled(i12, i13) & 0xFF);
                    floatBuffer.put((this.packUvScaled(i12, i13) & 0xFF00) >>> 8);
                    floatBuffer.put((short)(i14 + 1));
                    floatBuffer.put((short)(i15 - 1));
                    floatBuffer.put(this.packUvScaled(i12 + 16, i13) & 0xFF);
                    floatBuffer.put((this.packUvScaled(i12 + 16, i13) & 0xFF00) >>> 8);
                    floatBuffer.put((short)(i14 + 1));
                    floatBuffer.put((short)i15);
                    floatBuffer.put(this.packUvScaled(i12 + 16, i13 + 16) & 0xFF);
                    floatBuffer.put((this.packUvScaled(i12 + 16, i13 + 16) & 0xFF00) >>> 8);
                    floatBuffer.put((short)i14);
                    floatBuffer.put((short)i15);
                    floatBuffer.put(this.packUvScaled(i12, i13 + 16) & 0xFF);
                    floatBuffer.put((this.packUvScaled(i12, i13 + 16) & 0xFF00) >>> 8);
                }
                ++i8;
            }
            floatBuffer.flip();
            this.lineVertexBuffer.uploadRange(i1 * 4 * 4 * 4, i2 * 4 * 4 * 4);
        } else {
            ShortBuffer shortBuffer = (ShortBuffer)this.lineVertexBuffer.getData();
            shortBuffer.position(0);
            shortBuffer.limit(i2 * 4 * 3);
            int i4 = i1 + i2;
            FogOfWar fogOfWar = this.unitCommander.getPlayer().getFogOfWar();
            int i6 = 128;
            int i7 = 64;
            int i8 = i1;
            while (i8 < i4) {
                int i15;
                int i14;
                int i10;
                int i9 = i8 % fogOfWar.getWidth();
                int i11 = (fogOfWar.isCornerExplored(i9, i10 = i8 / fogOfWar.getHeight()) ? 0 : 1) + (fogOfWar.isCornerExplored(i9 + 1, i10) ? 0 : 2) + (fogOfWar.isCornerExplored(i9 + 1, i10 + 1) ? 0 : 4) + (fogOfWar.isCornerExplored(i9, i10 + 1) ? 0 : 8);
                int i12 = 128 * (i11 % 8);
                int i13 = 640 + (i11 >= 8 ? 64 : 0);
                if (i11 > 0) {
                    i14 = (short)IsoMathHelper.toScreenX(i9, i10, 128) - 64;
                    i15 = (short)IsoMathHelper.toScreenY(i9, i10, 64);
                    shortBuffer.put((short)i14);
                    shortBuffer.put((short)i15);
                    shortBuffer.put(this.packUvScaled(i12, i13));
                    shortBuffer.put((short)(i14 + i6));
                    shortBuffer.put((short)i15);
                    shortBuffer.put(this.packUvScaled(i12 + i6, i13));
                    shortBuffer.put((short)(i14 + i6));
                    shortBuffer.put((short)(i15 + i7));
                    shortBuffer.put(this.packUvScaled(i12 + i6, i13 + i7));
                    shortBuffer.put((short)i14);
                    shortBuffer.put((short)(i15 + i7));
                    shortBuffer.put(this.packUvScaled(i12, i13 + i7));
                } else {
                    i14 = -30000;
                    i15 = -30000;
                    shortBuffer.put((short)i14);
                    shortBuffer.put((short)(i15 - 1));
                    shortBuffer.put(this.packUvScaled(i12, i13));
                    shortBuffer.put((short)(i14 + 1));
                    shortBuffer.put((short)(i15 - 1));
                    shortBuffer.put(this.packUvScaled(i12 + 16, i13));
                    shortBuffer.put((short)(i14 + 1));
                    shortBuffer.put((short)i15);
                    shortBuffer.put(this.packUvScaled(i12 + 16, i13 + 16));
                    shortBuffer.put((short)i14);
                    shortBuffer.put((short)i15);
                    shortBuffer.put(this.packUvScaled(i12, i13 + 16));
                }
                ++i8;
            }
            shortBuffer.flip();
            this.lineVertexBuffer.uploadRange(i1 * 4 * 3 * 2, i2 * 4 * 3 * 2);
        }
    }

    private final void updateFogOfWarGeometry(int i1, int i2) {
        if (this.useFloatVertices) {
            FloatBuffer floatBuffer = (FloatBuffer)this.fogVertexBuffer.getData();
            floatBuffer.position(0);
            floatBuffer.limit(i2 * 4 * 4);
            int i4 = i1 + i2;
            FogOfWar fogOfWar = this.unitCommander.getPlayer().getFogOfWar();
            int i6 = 128;
            int i7 = 64;
            int i8 = i1;
            while (i8 < i4) {
                int i15;
                int i14;
                int i10;
                int i9 = i8 % fogOfWar.getWidth();
                int i11 = (fogOfWar.isCornerExplored(i9, i10 = i8 / fogOfWar.getHeight()) ? 0 : 1) + (fogOfWar.isCornerExplored(i9 + 1, i10) ? 0 : 2) + (fogOfWar.isCornerExplored(i9 + 1, i10 + 1) ? 0 : 4) + (fogOfWar.isCornerExplored(i9, i10 + 1) ? 0 : 8) == 15 ? 0 : (fogOfWar.isCornerVisible(i9, i10) ? 0 : 1) + (fogOfWar.isCornerVisible(i9 + 1, i10) ? 0 : 2) + (fogOfWar.isCornerVisible(i9 + 1, i10 + 1) ? 0 : 4) + (fogOfWar.isCornerVisible(i9, i10 + 1) ? 0 : 8);
                int i12 = 128 * (i11 % 8);
                int i13 = 512 + (i11 >= 8 ? 64 : 0);
                if (i11 > 0) {
                    i14 = (short)IsoMathHelper.toScreenX(i9, i10, 128) - 64;
                    i15 = (short)IsoMathHelper.toScreenY(i9, i10, 64);
                    floatBuffer.put((short)i14);
                    floatBuffer.put((short)i15);
                    floatBuffer.put(this.packUvScaled(i12, i13) & 0xFF);
                    floatBuffer.put((this.packUvScaled(i12, i13) & 0xFF00) >>> 8);
                    floatBuffer.put((short)(i14 + i6));
                    floatBuffer.put((short)i15);
                    floatBuffer.put(this.packUvScaled(i12 + i6, i13) & 0xFF);
                    floatBuffer.put((this.packUvScaled(i12 + i6, i13) & 0xFF00) >>> 8);
                    floatBuffer.put((short)(i14 + i6));
                    floatBuffer.put((short)(i15 + i7));
                    floatBuffer.put(this.packUvScaled(i12 + i6, i13 + i7) & 0xFF);
                    floatBuffer.put((this.packUvScaled(i12 + i6, i13 + i7) & 0xFF00) >>> 8);
                    floatBuffer.put((short)i14);
                    floatBuffer.put((short)(i15 + i7));
                    floatBuffer.put(this.packUvScaled(i12, i13 + i7) & 0xFF);
                    floatBuffer.put((this.packUvScaled(i12, i13 + i7) & 0xFF00) >>> 8);
                } else {
                    i14 = -30000;
                    i15 = -30000;
                    floatBuffer.put((short)i14);
                    floatBuffer.put((short)(i15 - 1));
                    floatBuffer.put(this.packUvScaled(i12, i13) & 0xFF);
                    floatBuffer.put((this.packUvScaled(i12, i13) & 0xFF00) >>> 8);
                    floatBuffer.put((short)(i14 + 1));
                    floatBuffer.put((short)(i15 - 1));
                    floatBuffer.put(this.packUvScaled(i12 + 16, i13) & 0xFF);
                    floatBuffer.put((this.packUvScaled(i12 + 16, i13) & 0xFF00) >>> 8);
                    floatBuffer.put((short)(i14 + 1));
                    floatBuffer.put((short)i15);
                    floatBuffer.put(this.packUvScaled(i12 + 16, i13 + 16) & 0xFF);
                    floatBuffer.put((this.packUvScaled(i12 + 16, i13 + 16) & 0xFF00) >>> 8);
                    floatBuffer.put((short)i14);
                    floatBuffer.put((short)i15);
                    floatBuffer.put(this.packUvScaled(i12, i13 + 16) & 0xFF);
                    floatBuffer.put((this.packUvScaled(i12, i13 + 16) & 0xFF00) >>> 8);
                }
                ++i8;
            }
            floatBuffer.flip();
            this.fogVertexBuffer.uploadRange(i1 * 4 * 4 * 4, i2 * 4 * 4 * 4);
        } else {
            ShortBuffer shortBuffer = (ShortBuffer)this.fogVertexBuffer.getData();
            shortBuffer.position(0);
            shortBuffer.limit(i2 * 4 * 3);
            int i4 = i1 + i2;
            FogOfWar fogOfWar = this.unitCommander.getPlayer().getFogOfWar();
            int i6 = 128;
            int i7 = 64;
            int i8 = i1;
            while (i8 < i4) {
                int i15;
                int i14;
                int i10;
                int i9 = i8 % fogOfWar.getWidth();
                int i11 = (fogOfWar.isCornerExplored(i9, i10 = i8 / fogOfWar.getHeight()) ? 0 : 1) + (fogOfWar.isCornerExplored(i9 + 1, i10) ? 0 : 2) + (fogOfWar.isCornerExplored(i9 + 1, i10 + 1) ? 0 : 4) + (fogOfWar.isCornerExplored(i9, i10 + 1) ? 0 : 8) == 15 ? 0 : (fogOfWar.isCornerVisible(i9, i10) ? 0 : 1) + (fogOfWar.isCornerVisible(i9 + 1, i10) ? 0 : 2) + (fogOfWar.isCornerVisible(i9 + 1, i10 + 1) ? 0 : 4) + (fogOfWar.isCornerVisible(i9, i10 + 1) ? 0 : 8);
                int i12 = 128 * (i11 % 8);
                int i13 = 512 + (i11 >= 8 ? 64 : 0);
                if (i11 > 0) {
                    i14 = (short)IsoMathHelper.toScreenX(i9, i10, 128) - 64;
                    i15 = (short)IsoMathHelper.toScreenY(i9, i10, 64);
                    shortBuffer.put((short)i14);
                    shortBuffer.put((short)i15);
                    shortBuffer.put(this.packUvScaled(i12, i13));
                    shortBuffer.put((short)(i14 + i6));
                    shortBuffer.put((short)i15);
                    shortBuffer.put(this.packUvScaled(i12 + i6, i13));
                    shortBuffer.put((short)(i14 + i6));
                    shortBuffer.put((short)(i15 + i7));
                    shortBuffer.put(this.packUvScaled(i12 + i6, i13 + i7));
                    shortBuffer.put((short)i14);
                    shortBuffer.put((short)(i15 + i7));
                    shortBuffer.put(this.packUvScaled(i12, i13 + i7));
                } else {
                    i14 = -30000;
                    i15 = -30000;
                    shortBuffer.put((short)i14);
                    shortBuffer.put((short)(i15 - 1));
                    shortBuffer.put(this.packUvScaled(i12, i13));
                    shortBuffer.put((short)(i14 + 1));
                    shortBuffer.put((short)(i15 - 1));
                    shortBuffer.put(this.packUvScaled(i12 + 16, i13));
                    shortBuffer.put((short)(i14 + 1));
                    shortBuffer.put((short)i15);
                    shortBuffer.put(this.packUvScaled(i12 + 16, i13 + 16));
                    shortBuffer.put((short)i14);
                    shortBuffer.put((short)i15);
                    shortBuffer.put(this.packUvScaled(i12, i13 + 16));
                }
                ++i8;
            }
            shortBuffer.flip();
            this.fogVertexBuffer.uploadRange(i1 * 4 * 3 * 2, i2 * 4 * 3 * 2);
        }
    }

    private final void drawSprite(int i1, int i2, int i3, int i4, int i5, int i6) {
        if (this.spriteVertexOffset >= this.spriteVertexCapacity) {
            this.flushSprites();
        }
        if (i1 <= this.visibleMaxTileX && i2 <= this.visibleMaxTileY && i1 + i5 >= this.visibleMinTileX && i2 + i6 >= this.visibleMinTileY) {
            this.spriteVertexData[this.spriteVertexOffset++] = (short)i1;
            this.spriteVertexData[this.spriteVertexOffset++] = (short)i2;
            this.spriteVertexData[this.spriteVertexOffset++] = this.packUv(i3, i4);
            this.spriteVertexData[this.spriteVertexOffset++] = (short)(i1 + i5);
            this.spriteVertexData[this.spriteVertexOffset++] = (short)i2;
            this.spriteVertexData[this.spriteVertexOffset++] = this.packUv(i3 + i5, i4);
            this.spriteVertexData[this.spriteVertexOffset++] = (short)(i1 + i5);
            this.spriteVertexData[this.spriteVertexOffset++] = (short)(i2 + i6);
            this.spriteVertexData[this.spriteVertexOffset++] = this.packUv(i3 + i5, i4 + i6);
            this.spriteVertexData[this.spriteVertexOffset++] = (short)i1;
            this.spriteVertexData[this.spriteVertexOffset++] = (short)(i2 + i6);
            this.spriteVertexData[this.spriteVertexOffset++] = this.packUv(i3, i4 + i6);
        }
    }

    private final void drawSpriteRegion(int i1, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        if (this.spriteVertexOffset >= this.spriteVertexCapacity) {
            this.flushSprites();
        }
        if (i1 <= this.visibleMaxTileX && i2 <= this.visibleMaxTileY && i1 + i3 >= this.visibleMinTileX && i2 + i4 >= this.visibleMinTileY) {
            this.spriteVertexData[this.spriteVertexOffset++] = (short)i1;
            this.spriteVertexData[this.spriteVertexOffset++] = (short)i2;
            this.spriteVertexData[this.spriteVertexOffset++] = this.packUv(i5, i6);
            this.spriteVertexData[this.spriteVertexOffset++] = (short)(i1 + i3);
            this.spriteVertexData[this.spriteVertexOffset++] = (short)i2;
            this.spriteVertexData[this.spriteVertexOffset++] = this.packUv(i5 + i7, i6);
            this.spriteVertexData[this.spriteVertexOffset++] = (short)(i1 + i3);
            this.spriteVertexData[this.spriteVertexOffset++] = (short)(i2 + i4);
            this.spriteVertexData[this.spriteVertexOffset++] = this.packUv(i5 + i7, i6 + i8);
            this.spriteVertexData[this.spriteVertexOffset++] = (short)i1;
            this.spriteVertexData[this.spriteVertexOffset++] = (short)(i2 + i4);
            this.spriteVertexData[this.spriteVertexOffset++] = this.packUv(i5, i6 + i8);
        }
    }

    private final void drawSpriteFlipped(int i1, int i2, int i3, int i4, int i5, int i6, boolean bl, boolean bl2) {
        if (this.spriteVertexOffset >= this.spriteVertexCapacity) {
            this.flushSprites();
        }
        this.spriteVertexData[this.spriteVertexOffset++] = (short)i1;
        this.spriteVertexData[this.spriteVertexOffset++] = (short)i2;
        this.spriteVertexData[this.spriteVertexOffset++] = this.packUv(bl ? i3 + i5 : i3, bl2 ? i4 + i6 : i4);
        this.spriteVertexData[this.spriteVertexOffset++] = (short)(i1 + i5);
        this.spriteVertexData[this.spriteVertexOffset++] = (short)i2;
        this.spriteVertexData[this.spriteVertexOffset++] = this.packUv(bl ? i3 : i3 + i5, bl2 ? i4 + i6 : i4);
        this.spriteVertexData[this.spriteVertexOffset++] = (short)(i1 + i5);
        this.spriteVertexData[this.spriteVertexOffset++] = (short)(i2 + i6);
        this.spriteVertexData[this.spriteVertexOffset++] = this.packUv(bl ? i3 : i3 + i5, bl2 ? i4 : i4 + i6);
        this.spriteVertexData[this.spriteVertexOffset++] = (short)i1;
        this.spriteVertexData[this.spriteVertexOffset++] = (short)(i2 + i6);
        this.spriteVertexData[this.spriteVertexOffset++] = this.packUv(bl ? i3 + i5 : i3, bl2 ? i4 : i4 + i6);
    }

    private void flushSprites() {
        int i1 = this.spriteVertexOffset / 12;
        if (i1 > 0) {
            Object object;
            GlBuffer glBuffer = this.spriteVertexBuffers[this.spriteBufferIndex];
            if (this.useFloatVertices) {
                object = (FloatBuffer)glBuffer.getData();
                ((FloatBuffer)object).position(0);
                ((FloatBuffer)object).limit(this.spriteVertexOffset * 4 / 3);
                int i4 = 0;
                while (i4 < this.spriteVertexOffset) {
                    if (i4 % 3 == 2) {
                        ((FloatBuffer)object).put(this.spriteVertexData[i4] & 0xFF);
                        ((FloatBuffer)object).put((this.spriteVertexData[i4] & 0xFF00) >>> 8);
                    } else {
                        ((FloatBuffer)object).put(this.spriteVertexData[i4]);
                    }
                    ++i4;
                }
                ((FloatBuffer)object).flip();
            } else {
                object = (ShortBuffer)glBuffer.getData();
                ((ShortBuffer)object).position(0);
                ((ShortBuffer)object).limit(this.spriteVertexOffset);
                ((ShortBuffer)object).put(this.spriteVertexData, 0, this.spriteVertexOffset);
                ((ShortBuffer)object).flip();
            }
            glBuffer.bind();
            glBuffer.uploadRange(0, i1 * 24);
            GL20 gl20 = Gdx.gl20;
            if (this.useFloatVertices) {
                gl20.glVertexAttribPointer(this.positionAttribute, 2, 5126, false, 16, 0);
                gl20.glEnableVertexAttribArray(this.positionAttribute);
                gl20.glVertexAttribPointer(this.texCoordAttribute, 2, 5126, false, 16, 8);
                gl20.glEnableVertexAttribArray(this.texCoordAttribute);
            } else {
                gl20.glVertexAttribPointer(this.positionAttribute, 2, 5122, false, 6, 0);
                gl20.glEnableVertexAttribArray(this.positionAttribute);
                gl20.glVertexAttribPointer(this.texCoordAttribute, 2, 5121, false, 6, 4);
                gl20.glEnableVertexAttribArray(this.texCoordAttribute);
            }
            this.spriteIndexBuffers[this.spriteBufferIndex].bind();
            gl20.glDrawElements(4, i1 * 6, 5123, 0);
            gl20.glDisableVertexAttribArray(this.positionAttribute);
            gl20.glDisableVertexAttribArray(this.texCoordAttribute);
            this.spriteBufferIndex = (this.spriteBufferIndex + 1) % this.spriteVertexBuffers.length;
            this.spriteVertexOffset = 0;
        }
    }

    private final short packUv(int i1, int i2) {
        return (short)(i1 / 16 + 256 * (i2 / 16));
    }

    private final void writeSpriteToBuffer(Buffer buffer, int i2, int i3, int i4, int i5, int i6, int i7) {
        if (buffer.hasRemaining() && i2 <= this.visibleMaxTileX && i3 <= this.visibleMaxTileY && i2 + i6 >= this.visibleMinTileX && i3 + i7 >= this.visibleMinTileY) {
            if (this.useFloatVertices) {
                FloatBuffer floatBuffer = (FloatBuffer)buffer;
                floatBuffer.put((short)i2);
                floatBuffer.put((short)i3);
                floatBuffer.put(this.packUvScaled(i4, i5) & 0xFF);
                floatBuffer.put((this.packUvScaled(i4, i5) & 0xFF00) >>> 8);
                floatBuffer.put((short)(i2 + i6));
                floatBuffer.put((short)i3);
                floatBuffer.put(this.packUvScaled(i4 + i6, i5) & 0xFF);
                floatBuffer.put((this.packUvScaled(i4 + i6, i5) & 0xFF00) >>> 8);
                floatBuffer.put((short)(i2 + i6));
                floatBuffer.put((short)(i3 + i7));
                floatBuffer.put(this.packUvScaled(i4 + i6, i5 + i7) & 0xFF);
                floatBuffer.put((this.packUvScaled(i4 + i6, i5 + i7) & 0xFF00) >>> 8);
                floatBuffer.put((short)i2);
                floatBuffer.put((short)(i3 + i7));
                floatBuffer.put(this.packUvScaled(i4, i5 + i7) & 0xFF);
                floatBuffer.put((this.packUvScaled(i4, i5 + i7) & 0xFF00) >>> 8);
            } else {
                ShortBuffer shortBuffer = (ShortBuffer)buffer;
                shortBuffer.put((short)i2);
                shortBuffer.put((short)i3);
                shortBuffer.put(this.packUvScaled(i4, i5));
                shortBuffer.put((short)(i2 + i6));
                shortBuffer.put((short)i3);
                shortBuffer.put(this.packUvScaled(i4 + i6, i5));
                shortBuffer.put((short)(i2 + i6));
                shortBuffer.put((short)(i3 + i7));
                shortBuffer.put(this.packUvScaled(i4 + i6, i5 + i7));
                shortBuffer.put((short)i2);
                shortBuffer.put((short)(i3 + i7));
                shortBuffer.put(this.packUvScaled(i4, i5 + i7));
            }
        }
    }

    private final short packUvScaled(int i1, int i2) {
        return (short)((i1 << 1) / 16 + 256 * ((i2 << 1) / 16));
    }

    @Override
    public void pause() {
        UserConfig.setPlayingTime(UserConfig.getPlayingTime() + (System.currentTimeMillis() - this.sessionStartTime) / 1000L);
        this.audio.pauseMusic();
        int i1 = 0;
        while (i1 < this.spriteIndexBuffers.length) {
            this.spriteIndexBuffers[i1].dispose();
            ++i1;
        }
        i1 = 0;
        while (i1 < this.spriteVertexBuffers.length) {
            this.spriteVertexBuffers[i1].dispose();
            ++i1;
        }
        this.terrainIndexBuffer.dispose();
        this.terrainVertexBuffer.dispose();
        this.fogIndexBuffer.dispose();
        this.fogVertexBuffer.dispose();
        this.markerIndexBuffer.dispose();
        this.markerVertexBuffer.dispose();
        this.disposeTilesTexture();
        this.tilesTexture = null;
        this.disposeUnitsTexture();
        this.unitsTexture = null;
        this.spriteShaderProgram.dispose();
        if (this.vectorTextRenderer != null) {
            this.vectorTextRenderer.dispose();
        }
        this.minimapRenderer.dispose();
    }

    private void disposeAudioClips() {
        this.audio.disposeClips();
    }

    @Override
    public void dispose() {
        this.gameMode.saveWorld(this.world);
        if (this.gameMode instanceof MultiplayerGameMode) {
            ((MultiplayerGameMode)this.gameMode).disconnect();
        }
        Gdx.input.setCatchBackKey(false);
        this.inputMultiplexer = null;
        this.audio.dispose();
    }

    static /* synthetic */ int[] getGameSceneSwitchMap() {
        if (cp != null) {
            return cp;
        }
        int[] nArray = new int[GameScene.values().length];
        try {
            nArray[GameScene.ScoreDialog.ordinal()] = 13;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            nArray[GameScene.MissionTerminated.ordinal()] = 12;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            nArray[GameScene.ExitToMenu.ordinal()] = 17;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            nArray[GameScene.MissionBriefing.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            nArray[GameScene.Loading.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            nArray[GameScene.RestartGame.ordinal()] = 16;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            nArray[GameScene.PauseMenu.ordinal()] = 11;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            nArray[GameScene.Attack.ordinal()] = 8;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            nArray[GameScene.ForceAttack.ordinal()] = 9;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            nArray[GameScene.Default.ordinal()] = 6;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            nArray[GameScene.NationSelect.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            nArray[GameScene.QuitConfirm.ordinal()] = 15;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            nArray[GameScene.RestartConfirm.ordinal()] = 14;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            nArray[GameScene.Move.ordinal()] = 10;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            nArray[GameScene.Build.ordinal()] = 7;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            nArray[GameScene.WaitingForPlayers.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            nArray[GameScene.LanNotice.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        cp = nArray;
        return nArray;
    }

    static /* synthetic */ float getZoom(GameScreen gameScreen) {
        return gameScreen.cameraZoom;
    }

    static /* synthetic */ void setTargetZoom(GameScreen gameScreen, float f1) {
        gameScreen.targetCameraZoom = f1;
    }

    static /* synthetic */ MinimapPanel getMinimapPanel(GameScreen gameScreen) {
        return gameScreen.minimapPanel;
    }

    /** The match sound bank (T07: extracted from this screen; used by the event/panel listeners). */
    GameAudio getAudio() {
        return this.audio;
    }

    static /* synthetic */ UnitList getSelectedUnits(GameScreen gameScreen) {
        return gameScreen.selectedUnits;
    }

    static /* synthetic */ GroupBar getGroupBar(GameScreen gameScreen) {
        return gameScreen.groupBar;
    }

    static /* synthetic */ void invokeSetScene(GameScreen gameScreen, GameScene gameScene) {
        gameScreen.setScene(gameScene);
    }

    static /* synthetic */ UnitCommander getUnitCommander(GameScreen gameScreen) {
        return gameScreen.unitCommander;
    }

    static /* synthetic */ long getGameTimeNanos(GameScreen gameScreen) {
        return gameScreen.gameTimeNanos;
    }

    static /* synthetic */ void setLastTickTime(GameScreen gameScreen, long l1) {
        gameScreen.bM = l1;
    }

    static /* synthetic */ MinimapRenderer getMinimapRenderer(GameScreen gameScreen) {
        return gameScreen.minimapRenderer;
    }

    static /* synthetic */ float getTargetZoom(GameScreen gameScreen) {
        return gameScreen.targetCameraZoom;
    }

    static /* synthetic */ TutorialHintBox getTutorialHintBox(GameScreen gameScreen) {
        return gameScreen.tutorialHintBox;
    }

    static /* synthetic */ void invokeCommandUngroup(GameScreen gameScreen) {
        gameScreen.commandUngroup();
    }

    static /* synthetic */ void invokeCommandPatrol(GameScreen gameScreen) {
        gameScreen.commandPatrol();
    }

    static /* synthetic */ void invokeCommandRepair(GameScreen gameScreen) {
        gameScreen.commandRepair();
    }

    static /* synthetic */ void invokeCommandStop(GameScreen gameScreen) {
        gameScreen.commandStop();
    }

    static /* synthetic */ long getLastBuildActionTime(GameScreen gameScreen) {
        return gameScreen.X;
    }


    static /* synthetic */ void setLastBuildActionTime(GameScreen gameScreen, long l1) {
        gameScreen.X = l1;
    }

    static /* synthetic */ UnitList getMultiSelectBuffer(GameScreen gameScreen) {
        return gameScreen.selectionScratch;
    }

    static /* synthetic */ void invokeCommandGroup(GameScreen gameScreen) {
        gameScreen.commandGroup();
    }

    static /* synthetic */ void invokeSelectUnitGroup(GameScreen gameScreen, int i1) {
        gameScreen.selectUnitGroup(i1);
    }

    static /* synthetic */ SessionMode getSessionMode(GameScreen gameScreen) {
        return gameScreen.gameMode;
    }

    static /* synthetic */ int[] getLayerSwitchMap() {
        if (cq != null) {
            return cq;
        }
        int[] nArray = new int[Layer.values().length];
        try {
            nArray[Layer.Base.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            nArray[Layer.Under.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            nArray[Layer.Upper.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        cq = nArray;
        return nArray;
    }

    static /* synthetic */ int[] getAmmoTypeSwitchMap() {
        if (cr != null) {
            return cr;
        }
        int[] nArray = new int[AmmoType.values().length];
        try {
            nArray[AmmoType.Bullets.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            nArray[AmmoType.Cannonball.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            nArray[AmmoType.Missile.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            nArray[AmmoType.Shell.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            nArray[AmmoType.Torpedo.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        cr = nArray;
        return nArray;
    }

    static /* synthetic */ int[] getBirdSwitchMap() {
        if (cs != null) {
            return cs;
        }
        int[] nArray = new int[BirdType.values().length];
        try {
            nArray[BirdType.Seagulls.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            nArray[BirdType.Sparrows.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        cs = nArray;
        return nArray;
    }
}

