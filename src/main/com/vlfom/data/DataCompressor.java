package com.vlfom.data;

import com.vlfom.utils.Pair;
import com.vlfom.utils.Vector2D;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by @vlfom.
 */
public class DataCompressor {
    public static void compressData(List<Pair<Vector2D>> data, int oldSize, int newSize) {
        double[] values;
        double[] newValues;
        for (int i = 0; i < data.size(); ++i) {
            values = data.get(i).getFirst().toArray();
            newValues = new double[newSize * newSize];
            for (int w = 0; w < newSize; ++w) {
                for (int h = 0; h < newSize; ++h) {
                    newValues[w * newSize + h] = (
                            values[2 * w * oldSize + 2 * h] + values[2 * w * oldSize + oldSize + 2 * h] +
                            values[2 * w * oldSize + 2 * h + 1] + values[2 * w * oldSize + oldSize + 2 * h + 1]
                    ) / 4.0;
                }
            }
            data.get(i).setFirst(new Vector2D(newValues));
        }
    }
}
