package com.vlfom.graphics;

/**
 * Created by @vlfom.
 */
import com.vlfom.utils.Pair;
import com.vlfom.utils.Vector2D;
import java.awt.Color;

import java.awt.Graphics;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.swing.JComponent;
import javax.swing.JFrame;

class MyCanvas extends JComponent {

    private double[] color;

    public MyCanvas(double[] color) {
        this.color = color;
    }

    private void drawGrid(Graphics g, int w, int h) {
        int scale = 10;
        for (int i = 0 ; i < w;  ++i ){
            for (int j = 0 ; j < h; ++j ) {
                g.setColor(new Color(0, 0, 0, (float) color[j*w + i]));
                g.fillRect(scale + i * scale, scale + j * scale, scale, scale);
            }
        }
    }

    public void paint(Graphics g) {
        drawGrid(g, 14, 14);
    }
}

public class Playground {

    public static ArrayList<Pair<Vector2D>> loadMNIST() throws Exception {
        Scanner sc = new Scanner(new FileInputStream(new File("res/data/mnist_train.csv")));
        sc.useDelimiter(Pattern.compile(",|\n"));
        ArrayList<Pair<Vector2D>> data = new ArrayList<>();
        int label;
        double[] labels = new double[10];
        double[] values = new double[784];
        for (int i = 0; i < 100; ++i) {
            label = sc.nextInt();
            labels[label] = 1;
            for (int j = 0; j < 784; ++j)
                values[j] = sc.nextInt();
            data.add(new Pair<>(new Vector2D(values), new Vector2D(labels)));
            labels[label] = 0;
        }
        return data;
    }

    public static ArrayList<Pair<Vector2D>> cutMNIST(ArrayList<Pair<Vector2D>> data) throws Exception {
        for (int i = 0; i < data.size(); ++i) {
            double[] values = data.get(i).getFirst().toArray();
            double[] newValues = new double[196];
            for (int w = 0; w < 14; ++w) {
                for (int h = 0; h < 14; ++h) {
                    newValues[w*14 + h] = (values[2 * w * 28 + 2 * h] + values[2 * w * 28 + 28 + 2 * h] +
                            + values[2 * w * 28 + 2 * h + 1] + values[2 * w * 28 + 28 + 2 * h + 1]) / 4.0;
                }
            }
            System.out.println(Arrays.toString(newValues));
            data.get(i).setFirst(new Vector2D(newValues));

        }
        return data;
    }

    public static void main(String... args) throws Exception {
        Scanner sc = new Scanner(System.in);

        ArrayList<Pair<Vector2D>> data = cutMNIST(loadMNIST());

        for (int pictureID = 0; pictureID < 100; ++pictureID) {
            double[] values = data.get(pictureID).getFirst().toArray();
            for (int i = 0; i < values.length; ++i)
                values[i] /= 255.0;

            JFrame window = new JFrame();
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setBounds(30, 30, 170, 170);
            window.setVisible(true);

            MyCanvas picture;

            picture = new MyCanvas(values);
            window.getContentPane().add(picture);

            System.out.println("Press any key to continue...");
            sc.next();
        }
    }
}