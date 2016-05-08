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
public class DataLoader {
    public static List<Pair<Vector2D>> loadData(InputStream inputStream, int numFeatures, int numLabels, int numExamples) {
        if (numExamples == -1)
            numExamples = Integer.MAX_VALUE;

        Scanner sc = new Scanner(inputStream);
        sc.useDelimiter(Pattern.compile(",|\n"));
        ArrayList<Pair<Vector2D>> data = new ArrayList<>();
        int label;
        double[] labels = new double[numLabels];
        double[] values = new double[numFeatures];
        int examplesCount = 0;
        while (sc.hasNextInt() && examplesCount < numExamples) {
            label = sc.nextInt();
            labels[label] = 1;
            for (int j = 0; j < numFeatures; ++j)
                values[j] = sc.nextInt();
            data.add(new Pair<>(new Vector2D(values), new Vector2D(labels)));
            labels[label] = 0;
            examplesCount += 1;
        }
        sc.close();
        return data;
    }
}
