/*
 * Core-logic unit test for com.desertstormfront.world.Vec2.
 *
 * Vec2 is the 2D point used for positions, guard/rally targets and distance checks.
 * distanceSquaredTo is on hot paths (neighbour scans, collision) and equalsInt is used
 * to compare tile-aligned positions, so their exact semantics matter.
 *
 * Runs head-less (no libGDX application), so it is part of run\dev.ps1 test.
 * Exit code 0 and "VEC2 PASSED" mean success.
 */
package com.desertstormfront.test;

import com.desertstormfront.world.Vec2;

public final class Vec2Test {

    private Vec2Test() {
    }

    public static void main(String[] args) {
        final float eps = 1e-5f;

        // --- constructors --------------------------------------------------------
        Vec2 zero = new Vec2();
        Assert.check("default ctor is (0,0)", zero.getX() == 0.0f && zero.getY() == 0.0f);
        Vec2 v = new Vec2(3.0f, 4.0f);
        Assert.check("xy ctor stores components", v.getX() == 3.0f && v.getY() == 4.0f);

        // --- distance / distanceSquared (3-4-5 triangle) -------------------------
        Assert.close("distanceTo(0,0)=5", v.distanceTo(0.0f, 0.0f), 5.0f, eps);
        Assert.close("distanceTo(Vec2)=5", v.distanceTo(new Vec2(0.0f, 0.0f)), 5.0f, eps);
        Assert.close("distanceSquaredTo(0,0)=25", v.distanceSquaredTo(0.0f, 0.0f), 25.0f, eps);
        Assert.close("distanceSquaredTo(Vec2)=25", v.distanceSquaredTo(new Vec2(0.0f, 0.0f)), 25.0f, eps);
        Assert.close("distanceTo self=0", v.distanceTo(v), 0.0f, eps);

        // --- equalsInt truncates to int (tile comparison) ------------------------
        Vec2 frac = new Vec2(1.9f, 2.9f);
        Assert.check("equalsInt truncates", frac.equalsInt(new Vec2(1.0f, 2.0f)));
        Assert.check("equalsInt(float,float) truncates", frac.equalsInt(1.0f, 2.0f));
        Assert.check("equalsInt rejects different tile", !frac.equalsInt(new Vec2(2.0f, 2.0f)));

        // --- equals / hashCode ---------------------------------------------------
        Vec2 a = new Vec2(1.0f, 2.0f);
        Assert.check("equals value", a.equals(new Vec2(1.0f, 2.0f)));
        Assert.check("not equals different value", !a.equals(new Vec2(1.0f, 3.0f)));
        Assert.check("not equals null", !a.equals(null));
        Assert.check("not equals other type", !a.equals("x"));
        Assert.check("equal vectors share hashCode", a.hashCode() == new Vec2(1.0f, 2.0f).hashCode());

        // --- toString ------------------------------------------------------------
        Assert.equal("toString", a.toString(), "(1.0, 2.0)");

        Assert.report("VEC2", "ctor/distance/distanceSquared/equalsInt/equals/hashCode/toString ok");
    }
}
