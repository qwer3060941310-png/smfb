/*
 * Headless-friendly OpenGL probe (no game code involved).
 *
 * Purpose: decide whether the "black screen" comes from our port or from the
 * runtime combination (LWJGL 2.9.2 + JDK 9+). It renders a red clear colour and
 * reads the pixel back, so the result is a fact we can print, not a guess.
 *
 * Mode "plain": standalone LWJGL Display (no AWT embedding).
 * Mode "awt"  : libGDX style embedding - Display.setParent(java.awt.Canvas).
 *
 * If plain succeeds (red pixel) but awt fails (black pixel / exception), the
 * AWT embedding path is broken and that is exactly what libGDX LwjglApplication
 * uses -> explains a running process with a black window and no exception.
 *
 * Usage: java -cp <probe-out;lib/gdx-backend-lwjgl.jar;lib/gdx.jar> RenderProbe [plain|awt]
 */
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Frame;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public final class RenderProbe {

    private static final int W = 320;
    private static final int H = 240;

    private RenderProbe() {
    }

    public static void main(String[] args) {
        boolean embedAwt = args.length > 0 && "awt".equalsIgnoreCase(args[0]);
        System.out.println("PROBE mode=" + (embedAwt ? "awt" : "plain") + " jvm=" + System.getProperty("java.version"));

        Frame frame = null;
        Canvas canvas = null;
        try {
            loadNatives();

            if (embedAwt) {
                frame = new Frame("RenderProbe");
                frame.setLayout(new BorderLayout());
                canvas = new Canvas();
                frame.add(canvas, BorderLayout.CENTER);
                frame.setSize(W, H);
                frame.setVisible(true);
                long deadline = System.currentTimeMillis() + 5000;
                while (!canvas.isDisplayable() && System.currentTimeMillis() < deadline) {
                    Thread.sleep(50L);
                }
                Display.setParent(canvas);
            } else {
                Display.setDisplayMode(new DisplayMode(W, H));
            }
            Display.create();

            System.out.println("GL_VENDOR=" + GL11.glGetString(GL11.GL_VENDOR));
            System.out.println("GL_RENDERER=" + GL11.glGetString(GL11.GL_RENDERER));
            System.out.println("GL_VERSION=" + GL11.glGetString(GL11.GL_VERSION));

            GL11.glViewport(0, 0, W, H);
            GL11.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
            GL11.glFlush();
            GL11.glFinish();

            ByteBuffer pixels = ByteBuffer.allocateDirect(4);
            GL11.glReadPixels(W / 2, H / 2, 1, 1, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);
            int r = pixels.get(0) & 0xFF;
            int g = pixels.get(1) & 0xFF;
            int b = pixels.get(2) & 0xFF;
            System.out.println("PIXEL r=" + r + " g=" + g + " b=" + b);
            System.out.println(r > 200 && g < 60 && b < 60 ? "RESULT=RED_OK" : "RESULT=NOT_RED");

            Display.update();
            Thread.sleep(300L);
            Display.destroy();
        } catch (Throwable t) {
            System.out.println("RESULT=ERROR " + t.getClass().getName() + ": " + t.getMessage());
        } finally {
            if (frame != null) {
                frame.dispose();
            }
        }
    }

    /**
     * LWJGL 2 needs its native library on java.library.path; the project ships the
     * natives inside the libGDX jars, so let libGDX extract and load them for us.
     */
    private static void loadNatives() {
        try {
            Class<?> loader = Class.forName("com.badlogic.gdx.backends.lwjgl.LwjglNativesLoader");
            java.lang.reflect.Method load = loader.getMethod("load");
            load.invoke(null);
            System.out.println("natives loaded via LwjglNativesLoader");
            return;
        } catch (Throwable t) {
            System.out.println("LwjglNativesLoader failed: " + t);
        }
        try {
            Class<?> loader = Class.forName("com.badlogic.gdx.utils.SharedLibraryLoader");
            Object instance = loader.newInstance();
            java.lang.reflect.Method load = loader.getMethod("load", String.class);
            load.invoke(instance, "lwjgl");
            System.out.println("natives loaded via SharedLibraryLoader");
            return;
        } catch (Throwable t) {
            System.out.println("SharedLibraryLoader failed: " + t);
        }
        try {
            String dll = extract("lwjgl64.dll");
            if (dll == null) {
                dll = extract("lwjgl.dll");
            }
            if (dll == null) {
                throw new IllegalStateException("no lwjgl dll found on classpath");
            }
            System.load(dll);
            System.out.println("natives loaded via extracted dll: " + dll);
        } catch (Throwable t) {
            System.out.println("native extraction failed: " + t);
            System.loadLibrary("lwjgl");
        }
    }

    /** Extracts the given dll (by simple name) from any jar on the classpath. */
    private static String extract(String name) throws Exception {
        for (String element : System.getProperty("java.class.path").split(";")) {
            java.io.File jar = new java.io.File(element);
            if (!jar.isFile() || !element.toLowerCase().endsWith(".jar")) {
                continue;
            }
            try (java.util.zip.ZipFile zip = new java.util.zip.ZipFile(jar)) {
                java.util.Enumeration<? extends java.util.zip.ZipEntry> entries = zip.entries();
                while (entries.hasMoreElements()) {
                    java.util.zip.ZipEntry entry = entries.nextElement();
                    String path = entry.getName();
                    if (!entry.isDirectory() && path.substring(path.lastIndexOf('/') + 1).equals(name)) {
                        java.io.File out = new java.io.File(System.getProperty("java.io.tmpdir"), "dsfprobe-" + name);
                        try (java.io.InputStream in = zip.getInputStream(entry)) {
                            java.nio.file.Files.copy(in, out.toPath(),
                                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                        }
                        return out.getAbsolutePath();
                    }
                }
            }
        }
        return null;
    }
}
