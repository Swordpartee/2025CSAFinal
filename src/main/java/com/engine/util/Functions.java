package com.engine.util;

import com.engine.game.collision.Collider;

public class Functions {
    public static double getTime() {
        return System.currentTimeMillis();
    }
    public static double clamp(double value, double min, double max) {
        return value < min ? min : value > max ? max : value;
    }

    public static double setMax(double value, double max) {
        return value > max ? max : value;
    }

    public static double getMax(double... values) {
        double max = values[0];
        for (double value : values) {
            if (value > max)
                max = value;
        }
        return max;
    }

    public static double setMin(double value, double min) {
        return value < min ? min : value;
    }

    public static double getMin(double... values) {
        double min = values[0];
        for (double value : values) {
            if (value < min)
                min = value;
        }
        return min;
    }

    public static double abs(double value) {
        return value < 0 ? -value : value;
    }
    
    public static double keepSignPow(double base, double exponent) {
        return base * java.lang.Math.pow(java.lang.Math.abs(base), exponent - 1);
    }

    public static double pow(double base, int exponent) {
        return java.lang.Math.pow(base, exponent);
    }

    public static double sqrt(double d) {
        return java.lang.Math.sqrt(d);
    }

    public static double sin(double d) {
        return java.lang.Math.sin(java.lang.Math.toRadians(d));
    }

    public static double cos(double d) {
        return java.lang.Math.cos(java.lang.Math.toRadians(d));
    }

    public static double tan(double d) {
        return java.lang.Math.tan(java.lang.Math.toRadians(d));
    }

    public static double atan2(double y, double x) {
        return java.lang.Math.toDegrees(java.lang.Math.atan2(y, x));
    }

    public static double toRadians(double d) {
        return java.lang.Math.toRadians(d);
    }

    public static boolean collidingWithAny(Collider collidable, Collider... others) {
        for (Collider other : others) {
            if (other == null) { continue; }

            if (collidable.colliding(other)) {
                return true;
            }
        }
        return false;
    }
}