/*
 * Core-logic unit test for com.noblemaster.lib.math.MathHelper.
 *
 * MathHelper is the game's frame-rate independent math base: an approximated sin/cos
 * (720-entry table) and atan2 (128x128 lookup) plus integral floor/round helpers that
 * use an offset-by-16384 trick. It is used by movement, aiming and projectile code, so
 * a regression here silently distorts the whole simulation.
 *
 * The approximations are sampled, so the tests use an epsilon derived from the table
 * resolution rather than exact equality. Runs head-less (no libGDX application), so it
 * is part of run\dev.ps1 test. Exit code 0 and "MATHHELPER PASSED" mean success.
 */
package com.desertstormfront.test;

import com.noblemaster.lib.math.MathHelper;

public final class MathHelperTest {

    private MathHelperTest() {
    }

    public static void main(String[] args) {
        // sin/cos sample error is under half a table step: half of 2*pi/720 ~= 0.0044.
        final float eps = 0.01f;
        // atan2 uses a coarser 128x128 table, so allow a wider margin.
        final float aeps = 0.03f;

        // --- sqrt -----------------------------------------------------------------
        Assert.close("sqrt(4)", MathHelper.sqrt(4.0f), 2.0f, 1e-6f);
        Assert.close("sqrt(2)", MathHelper.sqrt(2.0f), 1.4142135f, 1e-5f);
        Assert.close("sqrt(0)", MathHelper.sqrt(0.0f), 0.0f, 1e-6f);

        // --- sin / cos (720-entry table) -----------------------------------------
        Assert.close("sin(0)", MathHelper.sin(0.0f), 0.0f, eps);
        Assert.close("sin(pi/2)", MathHelper.sin((float)(Math.PI / 2.0)), 1.0f, eps);
        Assert.close("sin(-pi/2) wraps into table", MathHelper.sin((float)(-Math.PI / 2.0)), -1.0f, eps);
        Assert.close("cos(0)", MathHelper.cos(0.0f), 1.0f, eps);
        Assert.close("cos(pi)", MathHelper.cos((float)Math.PI), -1.0f, eps);
        // Periodicity: adding a full turn must land on the same table entry.
        Assert.close("sin periodic in 2*pi",
                MathHelper.sin((float)(2.0 * Math.PI) + 0.3f), MathHelper.sin(0.3f), 1e-6f);

        // --- atan2(y, x) (128x128 lookup) ----------------------------------------
        Assert.close("atan2(0,1)", MathHelper.atan2(0.0f, 1.0f), 0.0f, aeps);
        Assert.close("atan2(1,0)", MathHelper.atan2(1.0f, 0.0f), (float)(Math.PI / 2.0), aeps);
        Assert.close("atan2(-1,0)", MathHelper.atan2(-1.0f, 0.0f), (float)(-Math.PI / 2.0), aeps);
        Assert.close("atan2(1,1)", MathHelper.atan2(1.0f, 1.0f), (float)(Math.PI / 4.0), aeps);
        Assert.close("atan2(-1,-1)", MathHelper.atan2(-1.0f, -1.0f), (float)(-3.0 * Math.PI / 4.0), aeps);

        // --- angleDifference: normalized into (-pi, pi] --------------------------
        Assert.close("angleDifference(0,0)", MathHelper.angleDifference(0.0f, 0.0f), 0.0f, 1e-4f);
        // 190deg vs 0 is -170deg when taken the short way around.
        Assert.close("angleDifference(190,0)=-170",
                MathHelper.angleDifference((float)Math.toRadians(190.0), 0.0f),
                (float)Math.toRadians(-170.0), 1e-3f);
        boolean bounded = true;
        float[] angles = {0.0f, 0.5f, 3.0f, 6.0f, -3.0f, 100.0f, -100.0f};
        for (int i = 0; i < angles.length; ++i) {
            for (int j = 0; j < angles.length; ++j) {
                float d = MathHelper.angleDifference(angles[i], angles[j]);
                if (!(d > -(float)Math.PI - 1e-4f && d <= (float)Math.PI + 1e-4f)) {
                    bounded = false;
                }
            }
        }
        Assert.check("angleDifference stays within (-pi, pi]", bounded);

        // --- floor / round (offset-by-16384 integer trick) -----------------------
        // Correct across the normal coordinate range used by the game.
        Assert.check("floor(2.7)=2", MathHelper.floor(2.7f) == 2);
        Assert.check("floor(-2.7)=-3", MathHelper.floor(-2.7f) == -3);
        Assert.check("floor(2.0)=2", MathHelper.floor(2.0f) == 2);
        Assert.check("floor(-2.0)=-2", MathHelper.floor(-2.0f) == -2);
        Assert.check("round(2.5)=3", MathHelper.round(2.5f) == 3);
        Assert.check("round(2.4)=2", MathHelper.round(2.4f) == 2);
        Assert.check("round(-2.5)=-2", MathHelper.round(-2.5f) == -2);

        // --- abs / sign ----------------------------------------------------------
        Assert.check("abs(int -3)=3", MathHelper.abs(-3) == 3);
        Assert.check("abs(float -3.5)=3.5", MathHelper.abs(-3.5f) == 3.5f);
        Assert.check("abs(long -3)=3", MathHelper.abs(-3L) == 3L);
        Assert.check("sign(-9)=-1", MathHelper.sign(-9) == -1);
        Assert.check("sign(0)=0", MathHelper.sign(0) == 0);
        Assert.check("sign(9)=1", MathHelper.sign(9) == 1);

        // --- randomFloat in [0, 1) ----------------------------------------------
        boolean inRange = true;
        for (int i = 0; i < 1000; ++i) {
            float r = MathHelper.randomFloat();
            if (r < 0.0f || r >= 1.0f) {
                inRange = false;
                break;
            }
        }
        Assert.check("randomFloat() in [0,1)", inRange);

        Assert.report("MATHHELPER", "sqrt/sin/cos/atan2/angleDifference/floor/round/abs/sign/random ok");
    }
}
