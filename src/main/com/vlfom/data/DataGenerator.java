package com.vlfom.data;

import com.vlfom.utils.Pair;
import com.vlfom.utils.Vector2D;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by @vlfom.
 */
public class DataGenerator {

    public static ArrayList<Pair<Vector2D>> generateData(int dataSize, int dataType) {
        ArrayList<Pair<Vector2D>> data = new ArrayList<>();
        Random random = new Random(1346);
        double a, b, c, d;
        while (dataSize-- > 0) {
            a = random.nextDouble();
            b = random.nextDouble();
            if (dataType == 0) {
                if (a > b) {
                    c = 1;
                    d = 0;
                } else {
                    c = 0;
                    d = 1;
                }
                data.add(new Pair<>(new Vector2D(new double[] {a, b}), new Vector2D(new double[] {c, d})));
            } else if (dataType == 1) {
                data.add(new Pair<>(
                        new Vector2D(new double[] {a, b}),
                        new Vector2D(new double[] {Math.pow(a * b, b) + Math.sin(a) * Math.cos(b) +
                                Math.exp(a) * Math.acos(b) + Math.random()})));
            }
        }
        return data;
    }
}
