package utils;

import com.sun.deploy.config.VerboseDefaultConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by @vlfom.
 */
public class Vector2DTest {

    @Test
    public void testConstructors() throws Exception {
        Vector2D v;

        v = new Vector2D(3);
        assertEquals(v.getL1(), 3);
        assertEquals(v.getL2(), 1);
        v = new Vector2D(100);
        assertEquals(v.getL1(), 100);
        assertEquals(v.getL2(), 1);

        v = new Vector2D(5, 6);
        assertEquals(v.getL1(), 5);
        assertEquals(v.getL2(), 6);
        v = new Vector2D(1000, 1001);
        assertEquals(v.getL1(), 1000);
        assertEquals(v.getL2(), 1001);

        v = new Vector2D(new double[] {-1});
        assertEquals(v.getL1(), 1);
        assertEquals(v.getL2(), 1);
        assertEquals(v.getVal(0), -1, 1e-15);
        assertEquals(v.getVal(0, 0), -1, 1e-15);

        v = new Vector2D(new double[] {-1, -10, -100});
        assertEquals(v.getL1(), 3);
        assertEquals(v.getL2(), 1);
        assertEquals(v.getVal(1), -10, 1e-15);
        assertEquals(v.getVal(2, 0), -100, 1e-15);

        v.setVal(1, 0, 100);
        assertEquals(v.getVal(1, 0), 100, 1e-15);

        v.setVal(2, 1000);
        assertEquals(v.getVal(2, 0), 1000, 1e-15);
    }

    @Test
    public void testAdd() {
        Vector2D v0, v1, v2;

        v1 = new Vector2D(3, 4);
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 4; ++j) {
                v1.setVal(i, j, (i + 1) * (j + 1));
            }
        }

        v2 = new Vector2D(3, 4);
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 4; ++j) {
                v2.setVal(i, j, (i + 1) * (j + 1));
            }
        }

        v0 = v1.copy();

        assertEquals(v1.add(v2), v1.mul(2));
        assertEquals(v0, v1);
        assertEquals(v0, v2);
    }

    @Test
    public void testSub() {
        Vector2D v1, v2;

        v1 = VectorMath.gaussianRandom(10, 20);
        v2 = v1.copy();

        assertEquals(v1.sub(v2), new Vector2D(10, 20));
    }

    @Test
    public void testDot() {
        Vector2D v1, v2, v3;

        v1 = new Vector2D(2, 3);
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 3; ++j) {
                v1.setVal(i, j, i * 3 + j + 1);
            }
        }
        v2 = new Vector2D(3, 2);
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 2; ++j) {
                v2.setVal(i, j, i * 2 + j + 7);
            }
        }
        v3 = new Vector2D(2, 2);
        v3.setVal(0, 0, 58);
        v3.setVal(0, 1, 64);
        v3.setVal(1, 0, 139);
        v3.setVal(1, 1, 154);
        assertEquals(v1.dot(v2), v3);

        v1 = new Vector2D(3, 2);
        v1.setVal(0, 0, 1);
        v1.setVal(0, 1, -2);
        v1.setVal(1, 0, 0);
        v1.setVal(1, 1, 3);
        v1.setVal(2, 0, -1);
        v1.setVal(2, 1, 4);
        v2 = new Vector2D(2, 2);
        v2.setVal(0, 0, 4);
        v2.setVal(0, 1, 1);
        v2.setVal(1, 0, -1);
        v2.setVal(1, 1, 2);
        v3 = new Vector2D(3, 2);
        v3.setVal(0, 0, 6);
        v3.setVal(0, 1, -3);
        v3.setVal(1, 0, -3);
        v3.setVal(1, 1, 6);
        v3.setVal(2, 0, -8);
        v3.setVal(2, 1, 7);
        assertEquals(v1.dot(v2), v3);
    }
}
