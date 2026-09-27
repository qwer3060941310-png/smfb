/*
 * libGDX backend probe (no game code involved).
 *
 * The pure-LWJGL RenderProbe proves the driver/GL path works. This probe adds the
 * next layer: libGDX LwjglApplication + LwjglGraphics. If this window renders red
 * but the game window stays black, the fault is inside the game's own drawing
 * code; if this one is black too, the libGDX backend is the culprit.
 *
 * Usage: java -cp <probe-out;lib/*> GdxProbe
 */
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;

public final class GdxProbe implements ApplicationListener {

    private int frames;

    @Override
    public void create() {
        System.out.println("GDXPROBE create");
    }

    @Override
    public void resize(int width, int height) {
        System.out.println("GDXPROBE resize " + width + "x" + height);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (this.frames == 0) {
            System.out.println("GDXPROBE first render: gl=" + Gdx.gl + " gl20=" + Gdx.gl20
                    + " graphics=" + Gdx.graphics.getWidth() + "x" + Gdx.graphics.getHeight());
        }
        ++this.frames;
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        System.out.println("GDXPROBE frames=" + this.frames);
    }

    public static void main(String[] args) throws Exception {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "GdxProbe";
        cfg.width = 1280;
        cfg.height = 800;
        cfg.useGL30 = false;
        cfg.vSyncEnabled = true;
        cfg.foregroundFPS = 60;
        cfg.backgroundFPS = 60;
        new LwjglApplication(new GdxProbe(), cfg);
        Thread.sleep(8000L);
        System.out.println("GDXPROBE done");
        System.exit(0);
    }
}
