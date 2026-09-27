/*
 * Core-logic unit test for com.desertstormfront.world.Vec3.
 *
 * Vec3 is the 3D vector used for projectile origins/directions and bird flight, where
 * normalize() feeds the aim direction, so its unit-length guarantee is load-bearing.
 *
 * Runs head-less (no libGDX application), so it is part of run\dev.ps1 test.
 * Exit code 0 and "VEC3 PASSED" mean success.
 */
package com.desertstormfront.test;

import com.desertstormfront.world.Vec3;

public final class Vec3Test {

    private Vec3Test() {
    }

    public static void main(String[] args) {
        final float eps = 1e-5f;

        // --- constructors --------------------------------------------------------
        Vec3 zero = new Vec3();
        Assert.check("default ctor is (0,0,0)",
                zero.getX() == 0.0f && zero.getY() == 0.0f && zero.getZ() == 0.0f);
        Vec3 v = new Vec3(3.0f, 4.0f, 0.0f);
        Assert.check("xyz ctor stores components",
                v.getX() == 3.0f && v.getY() == 4.0f && v.getZ() == 0.0f);

        // --- normalize (3-4-5 -> unit length) ------------------------------------
        v.normalize();
        Assert.close("normalize x", v.getX(), 0.6f, eps);
        Assert.close("normalize y", v.getY(), 0.8f, eps);
        Assert.close("normalize z", v.getZ(), 0.0f, eps);
        Assert.close("normalized length=1",
                (float)Math.sqrt(v.getX() * v.getX() + v.getY() * v.getY() + v.getZ() * v.getZ()),
                1.0f, eps);

        // --- set / add -----------------------------------------------------------
        Vec3 s = new Vec3();
        s.set(1.0f, 2.0f, 3.0f);
        Assert.check("set(x,y,z)", s.getX() == 1.0f && s.getY() == 2.0f && s.getZ() == 3.0f);
        s.set(new Vec3(4.0f, 5.0f, 6.0f));
        Assert.check("set(Vec3)", s.getX() == 4.0f && s.getY() == 5.0f && s.getZ() == 6.0f);
        s.add(1.0f, -1.0f, 2.0f);
        Assert.check("add", s.getX() == 5.0f && s.getY() == 4.0f && s.getZ() == 8.0f);

        // --- individual setters --------------------------------------------------
        Vec3 t = new Vec3();
        t.setX(1.0f);
        t.setY(2.0f);
        t.setZ(3.0f);
        Assert.check("setX/setY/setZ", t.getX() == 1.0f && t.getY() == 2.0f && t.getZ() == 3.0f);

        // --- copy / equals -------------------------------------------------------
        Vec3 original = new Vec3(1.5f, 2.5f, 3.5f);
        Vec3 copy = original.copy();
        Assert.check("copy is independent", copy.equals(original) && copy != original);
        copy.setX(9.0f);
        Assert.check("mutating copy does not touch source", original.getX() == 1.5f);
        Assert.check("equals value", original.equals(new Vec3(1.5f, 2.5f, 3.5f)));
        Assert.check("equalsComponents", original.equalsComponents(1.5f, 2.5f, 3.5f));
        Assert.check("not equals different", !original.equals(new Vec3(1.5f, 2.5f, 4.0f)));
        Assert.check("not equals null", !original.equals(null));
        Assert.check("not equals other type", !original.equals("x"));
        Assert.check("equal vectors share hashCode",
                original.hashCode() == new Vec3(1.5f, 2.5f, 3.5f).hashCode());

        // --- toString ------------------------------------------------------------
        Assert.equal("toString", new Vec3(1.0f, 2.0f, 3.0f).toString(), "(1.0, 2.0, 3.0)");

        Assert.report("VEC3", "ctor/normalize/set/add/setters/copy/equals/hashCode/toString ok");
    }
}
