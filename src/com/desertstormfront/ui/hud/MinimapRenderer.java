/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.desertstormfront.app.support.IsoMathHelper;
import com.desertstormfront.command.UnitCommander;
import com.desertstormfront.config.UserConfig;
import com.desertstormfront.game.World;
import com.desertstormfront.game.mode.EscortMode;
import com.desertstormfront.game.model.Unit;
import com.desertstormfront.game.model.UnitList;
import com.desertstormfront.game.player.FogOfWar;
import com.desertstormfront.game.player.Player;
import com.desertstormfront.game.player.Team;
import com.desertstormfront.ui.FrameBuffer;
import com.desertstormfront.ui.GlBuffer;
import com.desertstormfront.ui.GlTexture;
import com.desertstormfront.ui.ShaderProgram;
import com.desertstormfront.ui.VectorTextRenderer;
import com.desertstormfront.world.TerrainGrid;
import com.desertstormfront.world.UnitPosition;
import com.desertstormfront.world.Vec2;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Draws the corner minimap: terrain under fog, friendly/enemy unit blips and the current viewport
 * box, rendered into an off-screen {@link FrameBuffer} and composited by the HUD.
 *
 * <p>Geometry is set up once in {@link #setViewport}: the screen projection matrix, the quad that
 * blits the framebuffer and the per-tile fog texture. Fog is refreshed incrementally - one batch of
 * tiles per frame (see {@link #frame}) - so a large map never stalls a single frame. Units are drawn
 * only when the player's {@link FogOfWar} reports them visible.
 */
public final class MinimapRenderer {
    private int mapX;
    private int mapY;
    private int cameraX;
    private int cameraY;
    private int viewportWidth;
    private int viewportHeight;
    private int worldWidth;
    private int worldHeight;
    private UnitCommander commander;
    private int frame;
    private boolean visible;
    private float zoom;
    private boolean fogInitialized;
    private FrameBuffer frameBuffer;
    private VectorTextRenderer terrainRenderer;
    private VectorTextRenderer overlayRenderer;
    private ShaderProgram shader;
    private int positionAttr;
    private int texCoordAttr;
    private int textureUniform;
    private int mvpUniform;
    private final int v = 128;
    private final int w = 128;
    private GlTexture minimapTexture;
    private GlBuffer vertexBuffer;
    private GlBuffer indexBuffer;

    public MinimapRenderer(UnitCommander unitCommander) {
        this.commander = unitCommander;
        this.frame = 0;
        this.visible = true;
        this.zoom = 1.0f;
        this.fogInitialized = false;
        this.shader = new ShaderProgram("attribute vec4 a_position;                            \nattribute vec2 a_texCoord;                            \nvarying vec2 v_texCoord;                              \nuniform mat4 u_mvpMatrix;                             \nvoid main()                                           \n{                                                     \n   gl_Position = a_position;                          \n   v_texCoord = a_texCoord;                           \n   gl_Position *= u_mvpMatrix;                        \n}                                                     \n", "#ifdef GL_ES                                          \nprecision mediump float;                              \n#endif                                                \nvarying vec2 v_texCoord;                              \nuniform sampler2D s_texture;                          \nvoid main()                                           \n{                                                     \n  gl_FragColor = texture2D(s_texture, v_texCoord);    \n}                                                     \n");
        this.positionAttr = this.shader.getAttributeLocation("a_position");
        this.texCoordAttr = this.shader.getAttributeLocation("a_texCoord");
        this.textureUniform = this.shader.getUniformLocation("s_texture");
        this.mvpUniform = this.shader.getUniformLocation("u_mvpMatrix");
        this.minimapTexture = new GlTexture(6408, 128, 128, 33071, 33071, 9728, 9728);
        float[] fArray = new float[16];
        short[] sArray = new short[]{0, 1, 2, 0, 2, 3};
        this.vertexBuffer = new GlBuffer(34962, fArray, 35040);
        this.indexBuffer = new GlBuffer(34963, sArray, 35044);
        this.frameBuffer = new FrameBuffer(36160, this.minimapTexture);
        this.terrainRenderer = new VectorTextRenderer(1024);
        this.terrainRenderer.setViewport(this.frameBuffer.getWidth(), this.frameBuffer.getHeight());
        this.overlayRenderer = new VectorTextRenderer(1024);
    }

    /** Shows or hides the minimap; a hidden minimap skips the whole per-frame draw. */
    public void setVisible(boolean bl) {
        this.visible = bl;
    }

    /** Sets the zoom applied to the viewport box drawn over the minimap. */
    public void setZoom(float f1) {
        this.zoom = f1;
    }

    /** Sets the camera column that maps to the horizontal origin of the viewport box. */
    public void setCameraX(int i1) {
        this.cameraX = i1;
    }

    /** Sets the camera row that maps to the vertical origin of the viewport box. */
    public void setCameraY(int i1) {
        this.cameraY = i1;
    }

    /**
     * Configures the render size and world extent, rebuilding the projection matrix, the blit quad
     * and the fog texture. Parameters are viewport width/height (pixels) and the isometric world
     * width/height (see {@code GameScreen}: {@code 128*(w+h)/2} and {@code 64*(w+h)/2}).
     */
    public void setViewport(int i1, int i2, int i3, int i4) {
        this.viewportWidth = i1;
        this.viewportHeight = i2;
        this.worldWidth = i3;
        this.worldHeight = i4;
        FloatBuffer floatBuffer = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asFloatBuffer();
        floatBuffer.put(new float[]{2.0f / (float)i1, 0.0f, 0.0f, -1.0f, 0.0f, -2.0f / (float)i2, 0.0f, 1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f});
        floatBuffer.flip();
        this.shader.bind();
        GL20 gL20 = Gdx.gl20;
        gL20.glUniformMatrix4fv(this.mvpUniform, 1, false, floatBuffer);
        gL20.glUniform1i(this.textureUniform, 0);
        this.vertexBuffer.bind();
        FloatBuffer floatBuffer2 = (FloatBuffer)this.vertexBuffer.getData();
        floatBuffer2.clear();
        this.mapX = i1 - 234 + 110;
        this.mapY = 9;
        floatBuffer2.put(this.mapX - 110);
        floatBuffer2.put(this.mapY + 55);
        floatBuffer2.put(0.0f);
        floatBuffer2.put(0.0f);
        floatBuffer2.put(this.mapX);
        floatBuffer2.put(this.mapY + 110);
        floatBuffer2.put(1.0f);
        floatBuffer2.put(0.0f);
        floatBuffer2.put(this.mapX + 110);
        floatBuffer2.put(this.mapY + 55);
        floatBuffer2.put(1.0f);
        floatBuffer2.put(1.0f);
        floatBuffer2.put(this.mapX);
        floatBuffer2.put(this.mapY);
        floatBuffer2.put(0.0f);
        floatBuffer2.put(1.0f);
        floatBuffer2.flip();
        this.vertexBuffer.uploadRange(0, 64);
        this.overlayRenderer.setViewport(i1, i2);
        this.rebuildFogTexture();
    }

    /** Draws one minimap frame (no-op when hidden); the fog texture is advanced incrementally. */
    public void render() {
        if (this.visible) {
            int i14;
            int i13;
            int i12;
            Object object;
            if (!this.fogInitialized) {
                this.rebuildFogTexture();
            }
            GL20 gL20 = Gdx.gl20;
            if ((this.frame & 0xF) == 3) {
                object = this.commander.getPlayer().getFogOfWar();
                int n = 32;
                int n2 = ((FogOfWar)object).getWidth() * ((FogOfWar)object).getHeight();
                int n3 = (n2 - 1) / n + 1;
                int n4 = (this.frame >> 4) % n3 * n;
                if (n4 + n > n2) {
                    n = n2 - n4;
                }
                this.updateFogRegion(n4, n);
            }
            this.shader.bind();
            this.minimapTexture.bind();
            this.vertexBuffer.bind();
            gL20.glVertexAttribPointer(this.positionAttr, 2, 5126, false, 16, 0);
            gL20.glEnableVertexAttribArray(this.positionAttr);
            gL20.glVertexAttribPointer(this.texCoordAttr, 2, 5126, false, 16, 8);
            gL20.glEnableVertexAttribArray(this.texCoordAttr);
            this.indexBuffer.bind();
            gL20.glDrawElements(4, 6, 5123, 0);
            gL20.glDisableVertexAttribArray(this.positionAttr);
            gL20.glDisableVertexAttribArray(this.texCoordAttr);
            this.overlayRenderer.bindShader();
            object = this.commander.getWorld();
            Player player = this.commander.getPlayer();
            Team team = player.getTeam();
            FogOfWar fogOfWar = player.getFogOfWar();
            UnitList unitList = ((World)object).getUnits();
            int i7 = ((World)object).getTerrainGrid().getWidth();
            int i8 = ((World)object).getTerrainGrid().getHeight();
            int n = 0;
            while (n < unitList.size()) {
                Unit unit = (Unit)unitList.get(n);
                if (fogOfWar.isUnitVisible(unit)) {
                    UnitPosition unitPosition = unit.getPosition();
                    i12 = Math.round(IsoMathHelper.toScreenX(unitPosition.getX(), unitPosition.getY(), 220) / (float)i7);
                    i13 = Math.round(IsoMathHelper.toScreenY(unitPosition.getX(), unitPosition.getY(), 110) / (float)i8);
                    Player player2 = unit.getOwner();
                    i14 = player2 == null ? -8398083 : (player2 == player ? -3584 : (team != null && player2.getTeam() == team ? -16711936 : -65536));
                    if (unit.isImmobile()) {
                        this.overlayRenderer.fillRectangle((float)(this.mapX + i12 - 1), (float)(this.mapY + i13 - 1), 3.0f, 3.0f, i14);
                    } else {
                        this.overlayRenderer.fillRectangle((float)(this.mapX + i12), (float)(this.mapY + i13), 2.0f, 2.0f, i14);
                    }
                }
                ++n;
            }
            if (((World)object).getGameMode() instanceof EscortMode && ((int)(((World)object).getGameTime() * 2.0f) & 1) == 0) {
                Vec2 vec2 = ((EscortMode)((World)object).getGameMode()).getTargetArea();
                short s = (short)Math.round(IsoMathHelper.toScreenX(vec2.getX(), vec2.getY(), 220) / (float)i7);
                short s2 = (short)Math.round(IsoMathHelper.toScreenY(vec2.getX(), vec2.getY(), 110) / (float)i8);
                this.overlayRenderer.fillRectangle((float)(this.mapX + s - 2), (float)(this.mapY + s2 - 2), 4.0f, 4.0f, -1);
            }
            if (UserConfig.isRenderDetails()) {
                int i16;
                int n5 = -this.cameraX * 220 / this.worldWidth;
                int n6 = -this.cameraY * 110 / this.worldHeight;
                int n7 = (int)((float)n5 + (float)this.viewportWidth / this.zoom * 220.0f / (float)this.worldWidth);
                i12 = (int)((float)n6 + (float)this.viewportHeight / this.zoom * 110.0f / (float)this.worldHeight);
                if (n6 >= 0) {
                    i13 = -2 * n6;
                    i14 = -i13;
                    if (n5 < i13 && n7 > i14) {
                        this.overlayRenderer.drawLine(this.mapX + i13, this.mapY + n6, this.mapX + i14, this.mapY + n6, -1);
                    } else if (n5 < i13) {
                        this.overlayRenderer.drawLine(this.mapX + i13, this.mapY + n6, this.mapX + n7, this.mapY + n6, -1);
                    } else if (n7 > i14) {
                        this.overlayRenderer.drawLine(this.mapX + n5, this.mapY + n6, this.mapX + i14, this.mapY + n6, -1);
                    } else {
                        this.overlayRenderer.drawLine(this.mapX + n5, this.mapY + n6, this.mapX + n7, this.mapY + n6, -1);
                    }
                }
                if (i12 < 110) {
                    i13 = 2 * (i12 - 110);
                    i14 = -i13;
                    if (n5 < i13 && n7 > i14) {
                        this.overlayRenderer.drawLine(this.mapX + i13, this.mapY + i12, this.mapX + i14, this.mapY + i12, -1);
                    } else if (n5 < i13) {
                        this.overlayRenderer.drawLine(this.mapX + i13, this.mapY + i12, this.mapX + n7, this.mapY + i12, -1);
                    } else if (n7 > i14) {
                        this.overlayRenderer.drawLine(this.mapX + n5, this.mapY + i12, this.mapX + i14, this.mapY + i12, -1);
                    } else {
                        this.overlayRenderer.drawLine(this.mapX + n5, this.mapY + i12, this.mapX + n7, this.mapY + i12, -1);
                    }
                }
                if (n5 >= -110) {
                    int n8 = -n5 / 2;
                    i16 = 110 - n8;
                    if (n6 < n8 && i12 > i16) {
                        this.overlayRenderer.drawLine(this.mapX + n5, this.mapY + n8, this.mapX + n5, this.mapY + i16, -1);
                    } else if (n6 < n8) {
                        this.overlayRenderer.drawLine(this.mapX + n5, this.mapY + n8, this.mapX + n5, this.mapY + i12, -1);
                    } else if (i12 > i16) {
                        this.overlayRenderer.drawLine(this.mapX + n5, this.mapY + n6, this.mapX + n5, this.mapY + i16, -1);
                    } else {
                        this.overlayRenderer.drawLine(this.mapX + n5, this.mapY + n6, this.mapX + n5, this.mapY + i12, -1);
                    }
                }
                if (n7 < 110) {
                    int n9 = n7 / 2;
                    i16 = 110 - n9;
                    if (n6 < n9 && i12 > i16) {
                        this.overlayRenderer.drawLine(this.mapX + n7, this.mapY + n9, this.mapX + n7, this.mapY + i16, -1);
                    } else if (n6 < n9) {
                        this.overlayRenderer.drawLine(this.mapX + n7, this.mapY + n9, this.mapX + n7, this.mapY + i12, -1);
                    } else if (i12 > i16) {
                        this.overlayRenderer.drawLine(this.mapX + n7, this.mapY + n6, this.mapX + n7, this.mapY + i16, -1);
                    } else {
                        this.overlayRenderer.drawLine(this.mapX + n7, this.mapY + n6, this.mapX + n7, this.mapY + i12, -1);
                    }
                }
            }
            this.overlayRenderer.flush();
            ++this.frame;
        }
    }

    private void rebuildFogTexture() {
        if (this.commander.getPlayer() != null) {
            FogOfWar fogOfWar = this.commander.getPlayer().getFogOfWar();
            int i2 = 64;
            int i3 = fogOfWar.getWidth() * fogOfWar.getHeight();
            int i4 = (i3 - 1) / i2 + 1;
            int i5 = 0;
            while (i5 < i4) {
                int i6 = i5 % i4 * i2;
                if (i6 + i2 > i3) {
                    i2 = i3 - i6;
                }
                this.updateFogRegion(i6, i2);
                ++i5;
            }
            this.fogInitialized = true;
        }
    }

    private void updateFogRegion(int i1, int i2) {
        this.frameBuffer.bind();
        this.terrainRenderer.bindShader();
        int i3 = i1 + i2;
        FogOfWar fogOfWar = this.commander.getPlayer().getFogOfWar();
        TerrainGrid terrainGrid = this.commander.getWorld().getTerrainGrid();
        int i6 = fogOfWar.getWidth();
        int i7 = fogOfWar.getHeight();
        int i8 = i1;
        while (i8 < i3) {
            int i9 = i8 % fogOfWar.getWidth();
            int i10 = i8 / fogOfWar.getHeight();
            int i11 = 128 * i9 / i6;
            int i12 = 128 * i10 / i7;
            int i13 = 128 * (i9 + 1) / i6 - i11;
            int i14 = 128 * (i10 + 1) / i7 - i12;
            if (fogOfWar.isExplored(i9, i10)) {
                int i15 = terrainGrid.isAllLandTile(i9, i10) ? -15318447 : (terrainGrid.isCoastTile(i9, i10) ? -15173994 : -15300174);
                this.terrainRenderer.fillRectangle((float)i11, (float)i12, (float)i13, (float)i14, i15);
            } else {
                this.terrainRenderer.fillRectangle((float)i11, (float)i12, (float)i13, (float)i14, -14671840);
            }
            ++i8;
        }
        this.terrainRenderer.flush();
        this.frameBuffer.unbind();
    }

    /** Releases the framebuffer, texture, vertex/index buffers and shader. */
    public void dispose() {
        this.frameBuffer.dispose();
        this.minimapTexture.dispose();
        this.vertexBuffer.dispose();
        this.indexBuffer.dispose();
        this.shader.dispose();
    }
}

