/*
 * Sound bank for one match: loads every clip once, owns the background music playlist and exposes
 * named play methods.
 *
 * Extracted from GameScreen (T07) so the 3800-line screen is no longer also the audio owner. The
 * split is behaviour-preserving: every clip is loaded from the same file, in the same order, and
 * clips that are loaded and disposed but never triggered are kept verbatim so file-loading
 * behaviour is unchanged.
 */
package com.desertstormfront.audio;

import com.desertstormfront.config.GameConfig;
import com.desertstormfront.config.UserConfig;
import com.desertstormfront.game.model.AmmoType;
import com.noblemaster.lib.data.DateTime;
import com.noblemaster.lib.io.GameFile;
import com.noblemaster.lib.util.FastRandom;
import java.util.ArrayList;
import java.util.List;

public final class GameAudio {

    private MusicPlaylist music;
    private boolean timeWarningPlayed;
    private float underAttackReadyTime;
    private FastRandom random;

    private AudioClip time;
    private AudioClip unitReady;
    private AudioClip constructing;
    private AudioClip unitConstructed;
    private AudioClip structureReady;
    private AudioClip squadReady;
    private AudioClip[] ok;
    private AudioClip invalid;
    private AudioClip structureLost;
    private AudioClip[] projectiles;
    private AudioClip impact;
    private AudioClip explosion;
    private AudioClip structureCaptured;
    private AudioClip flagLost;
    private AudioClip flagTaken;
    private AudioClip underAttack;
    private AudioClip playerEliminated;
    private AudioClip unitLost;
    private AudioClip income;
    private AudioClip missionTerminated;
    private AudioClip outcomeText;
    private AudioClip outcomeSong;

    /**
     * Loads the bank. {@code marketName} reproduces the original festive-sound switch: on an April 1
     * build of the GooglePlay market a few file names gain the "_funny" suffix.
     */
    public GameAudio(String marketName) {
        List<GameFile> tracks = new ArrayList<GameFile>();
        String[] musicTracks = GameConfig.getMusicTracks();
        for (int i = 0; i < musicTracks.length; ++i) {
            tracks.add(GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getConfigDir()) + musicTracks[i]));
        }
        this.music = new MusicPlaylist(tracks);
        this.music.shuffle();

        String suffix = DateTime.now().isSameMonthDay(4, 1) && marketName != null && marketName.equals("GooglePlay") ? "_funny" : "";
        this.timeWarningPlayed = false;
        this.random = new FastRandom();
        this.time = new AudioClip(shared("audio_time.wav"));
        this.unitReady = new AudioClip(shared("audio_unit_ready.wav"));
        this.constructing = new AudioClip(shared("audio_constructing.wav"));
        this.unitConstructed = new AudioClip(shared("audio_unit_constructed.wav"));
        this.structureReady = new AudioClip(shared("audio_structure_ready.wav"));
        this.squadReady = new AudioClip(shared("audio_squad_ready.wav"));
        this.ok = new AudioClip[6];
        for (int i = 0; i < this.ok.length; ++i) {
            this.ok[i] = new AudioClip(shared("audio_ok_" + i + ".wav"));
        }
        this.invalid = new AudioClip(shared("audio_invalid.wav"));
        this.structureLost = new AudioClip(shared("audio_structure_lost.wav"));
        this.projectiles = new AudioClip[AmmoType.values().length];
        for (int i = 0; i < this.projectiles.length; ++i) {
            this.projectiles[i] = new AudioClip(shared("audio_projectile_" + AmmoType.values()[i].getKey().toLowerCase() + suffix + ".wav"));
        }
        this.impact = new AudioClip(shared("audio_impact" + suffix + ".wav"));
        this.explosion = new AudioClip(shared("audio_explosion" + suffix + ".wav"));
        this.structureCaptured = new AudioClip(shared("audio_structure_captured.wav"));
        this.flagLost = new AudioClip(shared("audio_flag_lost.wav"));
        this.flagTaken = new AudioClip(shared("audio_flag_taken.wav"));
        if (UserConfig.isRenderDetails()) {
            this.underAttack = new AudioClip(shared("audio_under_attack.wav"));
            this.playerEliminated = new AudioClip(shared("audio_player_eliminated.wav"));
            this.unitLost = new AudioClip(shared("audio_unit_lost.wav"));
            this.income = new AudioClip(shared("audio_income.wav"));
        }
    }

    private static GameFile shared(String name) {
        return GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getSharedDir()) + name);
    }

    /** Plays the low-time warning, at most once per match. */
    public void playTimeWarning() {
        if (!this.timeWarningPlayed) {
            this.timeWarningPlayed = true;
            this.time.play();
        }
    }

    /** Plays the "unit ready" cue. */
    public void playUnitReady() {
        this.unitReady.play();
    }

    /** Plays the "structure ready" cue. */
    public void playStructureReady() {
        this.structureReady.play();
    }

    /** Plays the "squad ready" cue. */
    public void playSquadReady() {
        this.squadReady.play();
    }

    /** Plays a randomly chosen acknowledgement cue. */
    public void playOk() {
        this.ok[this.random.nextInt(this.ok.length)].play();
    }

    /** Plays the "invalid order" cue. */
    public void playInvalid() {
        this.invalid.play();
    }

    /** Plays the "constructing" cue (build panel acknowledgement). */
    public void playConstructing() {
        this.constructing.play();
    }

    /** Plays the "unit constructed" cue. */
    public void playUnitConstructed() {
        this.unitConstructed.play();
    }

    /** Plays the "structure captured" cue. */
    public void playStructureCaptured() {
        this.structureCaptured.play();
    }

    /** Plays the "structure lost" cue. */
    public void playStructureLost() {
        this.structureLost.play();
    }

    /** Plays the "explosion" cue. */
    public void playExplosion() {
        this.explosion.play();
    }

    /** Plays the "unit lost" cue. */
    public void playUnitLost() {
        this.unitLost.play();
    }

    /** Plays the "impact" cue. */
    public void playImpact() {
        this.impact.play();
    }

    /** Plays the cue for the given projectile ammo type. */
    public void playProjectile(AmmoType ammo) {
        this.projectiles[ammo.ordinal()].play();
    }

    /** Plays the "flag taken" cue. */
    public void playFlagTaken() {
        this.flagTaken.play();
    }

    /** Plays the "flag lost" cue. */
    public void playFlagLost() {
        this.flagLost.play();
    }

    /** Plays the "income" cue. */
    public void playIncome() {
        this.income.play();
    }

    /** Plays the "player eliminated" cue. */
    public void playPlayerEliminated() {
        this.playerEliminated.play();
    }

    /**
     * Plays the "under attack" cue at most once every six game seconds; the caller still decides
     * whether the event concerns the local player.
     */
    public void playUnderAttack(float gameTime) {
        if (this.underAttackReadyTime <= gameTime) {
            if (this.underAttack != null) {
                this.underAttack.play();
            }
            this.underAttackReadyTime = gameTime + 6.0f;
        }
    }

    /** Plays the one-shot "mission terminated" cue. */
    public void playMissionTerminated() {
        this.missionTerminated = new AudioClip(shared("audio_mission_terminated.wav"), false);
        this.missionTerminated.play();
    }

    /** Swaps the playlist for the victory song + spoken text. */
    public void playVictoryMusic() {
        playOutcome("music_success_song.mp3", "music_success_text.mp3");
    }

    /** Swaps the playlist for the defeat song + spoken text. */
    public void playDefeatMusic() {
        playOutcome("music_failure_song.mp3", "music_failure_text.mp3");
    }

    private void playOutcome(String song, String text) {
        this.outcomeSong = new AudioClip(shared(song), false);
        this.outcomeSong.play(UserConfig.getMusicVolume());
        this.outcomeText = new AudioClip(shared(text), false);
        this.outcomeText.play();
    }

    /** Shuffles the playlist (called once after construction). */
    public void shuffleMusic() {
        this.music.shuffle();
    }

    /** Starts/resumes the background music. */
    public void playMusic() {
        this.music.resume();
    }

    /** Pauses the background music. */
    public void pauseMusic() {
        this.music.pause();
    }

    /** Per-frame playlist tick (advances tracks, applies volume changes). */
    public void updateMusic() {
        this.music.update();
    }

    /** Disposes the shared cue clips; called when a match ends. */
    public void disposeClips() {
        if (this.time != null) {
            this.time.dispose();
            this.time = null;
        }
        if (this.unitReady != null) {
            this.unitReady.dispose();
            this.unitReady = null;
        }
        if (this.constructing != null) {
            this.constructing.dispose();
            this.constructing = null;
        }
        if (this.unitConstructed != null) {
            this.unitConstructed.dispose();
            this.unitConstructed = null;
        }
        if (this.structureReady != null) {
            this.structureReady.dispose();
            this.structureReady = null;
        }
        if (this.squadReady != null) {
            this.squadReady.dispose();
            this.squadReady = null;
        }
        if (this.ok != null) {
            for (int i = 0; i < this.ok.length; ++i) {
                this.ok[i].dispose();
            }
            this.ok = null;
        }
        if (this.invalid != null) {
            this.invalid.dispose();
            this.invalid = null;
        }
        if (this.structureLost != null) {
            this.structureLost.dispose();
            this.structureLost = null;
        }
        if (this.projectiles != null) {
            for (int i = 0; i < this.projectiles.length; ++i) {
                this.projectiles[i].dispose();
            }
            this.projectiles = null;
        }
        if (this.impact != null) {
            this.impact.dispose();
            this.impact = null;
        }
        if (this.explosion != null) {
            this.explosion.dispose();
            this.explosion = null;
        }
        if (this.structureCaptured != null) {
            this.structureCaptured.dispose();
            this.structureCaptured = null;
        }
        if (this.flagLost != null) {
            this.flagLost.dispose();
            this.flagLost = null;
        }
        if (this.flagTaken != null) {
            this.flagTaken.dispose();
            this.flagTaken = null;
        }
        if (UserConfig.isRenderDetails()) {
            if (this.underAttack != null) {
                this.underAttack.dispose();
                this.underAttack = null;
            }
            if (this.playerEliminated != null) {
                this.playerEliminated.dispose();
                this.playerEliminated = null;
            }
            if (this.unitLost != null) {
                this.unitLost.dispose();
                this.unitLost = null;
            }
            if (this.income != null) {
                this.income.dispose();
                this.income = null;
            }
        }
    }

    /** Disposes everything: playlist, cue clips and the outcome clips. */
    public void dispose() {
        if (this.music != null) {
            this.music.dispose();
            this.music = null;
        }
        this.disposeClips();
        if (this.missionTerminated != null) {
            this.missionTerminated.dispose();
            this.missionTerminated = null;
        }
        if (this.outcomeText != null) {
            this.outcomeText.dispose();
            this.outcomeText = null;
        }
        if (this.outcomeSong != null) {
            this.outcomeSong.dispose();
            this.outcomeSong = null;
        }
    }
}
