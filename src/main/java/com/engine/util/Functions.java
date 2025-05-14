package com.engine.util;

import com.engine.game.collision.Collidable;

public class Functions {
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

    public static Vector addVectors(Vector vector1, Vector vector2) {
        return new Vector(
                vector1.getX() + vector2.getX(),
                vector1.getY() + vector2.getY(),
                vector1.getZ() + vector2.getZ());
    }

    public static Vector subtractVectors(Vector vector1, Vector vector2) {
        return new Vector(
                vector1.getX() - vector2.getX(),
                vector1.getY() - vector2.getY(),
                vector1.getZ() - vector2.getZ());
    }

    public static Vector scaleVector(Vector vector, double scalar) {
        return new Vector(
                vector.getX() * scalar,
                vector.getY() * scalar,
                vector.getZ() * scalar);
    }

    public static double getDotProduct(Vector vector1, Vector vector2) {
        return vector1.getX() * vector2.getX() + vector1.getY() * vector2.getY() + vector1.getZ() * vector2.getZ();
    }

    public static Vector crossProduct(Vector vector1, Vector vector2) {
        double newX = vector1.getY() * vector2.getZ() - vector1.getZ() * vector2.getY();
        double newY = vector1.getZ() * vector2.getX() - vector1.getX() * vector2.getZ();
        double newZ = vector1.getX() * vector2.getY() - vector1.getY() * vector2.getX();
        return new Vector(newX, newY, newZ);
    }

    public static Vector normalize(Vector vector) {
        return scaleVector(vector, 1 / vector.getMagnitude());
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

    public static boolean collidingWithAny(Collidable collidable, Collidable... others) {
        for (Collidable other : others) {
            if (collidable.colliding(other)) {
                return true;
            }
        }
        return false;
    }
}