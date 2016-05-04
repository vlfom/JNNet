package utils;

import java.util.Random;

/**
 * Created by @vlfom.
 */
public class VectorMath {
    public static Vector2D exp(Vector2D x) {
        for (int i = 0; i < x.getL1(); ++i) {
            for (int j = 0; j < x.getL2(); ++j) {
                x.setVal(i, j, Math.exp(x.getVal(i, j)));
            }
        }
        return x;
    }

    public static Vector2D zeros(int length) {
        Vector2D v = new Vector2D(length);
        return v;
    }

    public static Vector2D zeros(int l1, int l2) {
        Vector2D v = new Vector2D(l1, l2);
        return v;
    }

    public static Vector2D zeros(Vector2D x) {
        return zeros(x.getL1(), x.getL2());
    }

    public static Vector2D ones(int length) {
        Vector2D v = new Vector2D(length);
        for (int i = 0; i < length; ++i)
            v.setVal(i, 1);
        return v;
    }

    public static Vector2D ones(int l1, int l2) {
        Vector2D v = new Vector2D(l1, l2);
        for (int i = 0; i < l1; ++i)
            for (int j = 0; j < l2; ++j)
                v.setVal(i, j, 1);
        return v;
    }

    public static Vector2D ones(Vector2D x) {
        return ones(x.getL1(), x.getL2());
    }

    public static Vector2D gaussianRandom(int l1, int l2) {
        Random r = new Random();
        Vector2D v = new Vector2D(l1, l2);
        for (int i = 0; i < l1; ++i)
            for (int j = 0; j < l2; ++j)
                v.setVal(i, j, r.nextGaussian());
        return v;
    }
}
