package com.yk.onedemo.utils;

/**
 * Created by yk on 2017/7/20.
 * 解方程的辅助类
 */

public class MathUtils {
    public static float getK(float Xa, float Ya, float Xo, float Yo) {
        return (Xo - Xa) / (Ya - Yo);
    }

    public static float getD(float Xa, float Ya, float Xo, float Yo, float r) {
        float up = (Xa * Xo) - (Xo * Xo) + (Ya * Yo) - (Yo * Yo) + (r * r);
        return up / (Ya - Yo);
    }


    public static float getA(float k) {
        return ((k * k) + 1);
    }

    public static float getB(float k, float d, float Xo, float Yo) {
        return (2 * k * d) - (2 * Xo) - (2 * Yo * k);
    }

    public static float getC(float d, float Xo, float Yo, float r) {
//        return (-2 * Yo * d) + (Yo * Yo) - (r * r) + (Xo * Xo);
        return -(r * r) + (Xo * Xo) + ((d - Yo) * (d - Yo));
    }

    public static float getX1(float a, float b, float c) {
        float up = (float) (-b + Math.sqrt((b * b) - (4 * a * c)));
        return up / (2 * a);
    }

    public static float getX2(float a, float b, float c) {
        float up = (float) (-b - Math.sqrt(((b * b) - (4 * a * c))));
        return up / (2 * a);
    }

    public static float getY(float k, float x, float d) {
        return (k * x) + d;
    }

}
