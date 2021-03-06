package com.vlfom.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;

/**
 * Created by @vlfom.
 */
public class Vector2D implements Serializable {
    private int l1, l2;
    private double[][] val;

    public Vector2D(int l1) {
        this.l1 = l1;
        this.l2 = 1;
        val = new double[l1][1];
    }

    public Vector2D(int l1, int l2) {
        this.l1 = l1;
        this.l2 = l2;
        val = new double[l1][l2];
    }

    public Vector2D(double[] arr) {
        l1 = arr.length;
        l2 = 1;
        val = new double[l1][l2];
        for (int i = 0; i < l1; ++i) {
            val[i][0] = arr[i];
        }
    }

    public int getL1() {
        return l1;
    }

    public int getL2() {
        return l2;
    }

    public double getVal(int i, int j) {
        return val[i][j];
    }

    public double getVal(int i) {
        return val[i][0];
    }

    public void setVal(int i, int j, double val) {
        this.val[i][j] = val;
    }

    public void setVal(int i, double val) {
        this.val[i][0] = val;
    }

    public double[] toArray() {
        double[] values = new double[l1];
        for (int i = 0; i < l1; ++i) {
            values[i] = val[i][0];
        }
        return values;
    }

    public Vector2D add(double x) {
        Vector2D v = new Vector2D(l1, l2);
        for (int i = 0; i < l1; ++i) {
            for (int j = 0; j < l2; ++j) {
                v.val[i][j] = val[i][j] + x;
            }
        }
        return v;
    }

    public Vector2D add(Vector2D x) {
        if (x.l1 != l1 || x.l2 != l2) {
            throw new RuntimeException("Vector2D lengths must be equal");
        }
        Vector2D v = new Vector2D(l1, l2);
        for (int i = 0; i < l1; ++i) {
            for (int j = 0; j < l2; ++j) {
                v.val[i][j] = val[i][j] + x.val[i][j];
            }
        }
        return v;
    }

    public Vector2D sub(double x) {
        Vector2D v = new Vector2D(l1, l2);
        for (int i = 0; i < l1; ++i) {
            for (int j = 0; j < l2; ++j) {
                v.val[i][j] = val[i][j] - x;
            }
        }
        return v;
    }

    public Vector2D sub(Vector2D x) {
        if (x.l1 != l1 || x.l2 != l2) {
            throw new RuntimeException("Vector2D lengths must be equal");
        }
        Vector2D v = new Vector2D(l1, l2);
        for (int i = 0; i < l1; ++i) {
            for (int j = 0; j < l2; ++j) {
                v.val[i][j] = val[i][j] - x.val[i][j];
            }
        }
        return v;
    }

    public Vector2D mul(double x) {
        Vector2D v = new Vector2D(l1, l2);
        for (int i = 0; i < l1; ++i) {
            for (int j = 0; j < l2; ++j) {
                v.val[i][j] = val[i][j] * x;
            }
        }
        return v;
    }

    public Vector2D mul(Vector2D x) {
        if (x.l1 != l1 || x.l2 != l2) {
            throw new RuntimeException("Vector2D lengths must be equal");
        }
        Vector2D v = new Vector2D(l1, l2);
        for (int i = 0; i < l1; ++i) {
            for (int j = 0; j < l2; ++j) {
                v.val[i][j] = val[i][j] * x.val[i][j];
            }
        }
        return v;
    }

    public Vector2D div(double x) {
        Vector2D v = new Vector2D(l1, l2);
        for (int i = 0; i < l1; ++i) {
            for (int j = 0; j < l2; ++j) {
                v.val[i][j] = val[i][j] / x;
            }
        }
        return v;
    }

    public Vector2D div(Vector2D x) {
        if (x.l1 != l1 || x.l2 != l2) {
            throw new RuntimeException("Vector2D lengths must be equal");
        }
        Vector2D v = new Vector2D(l1, l2);
        for (int i = 0; i < l1; ++i) {
            for (int j = 0; j < l2; ++j) {
                v.val[i][j] = val[i][j] / x.val[i][j];
            }
        }
        return v;
    }

    public Vector2D dot(Vector2D x) {
        if (x.l1 != l2) {
            throw new RuntimeException("Vectors' dimensions must agree: found " +
                    "(" + l1 + ", " + l2 + ") and (" + x.l1 + ", " + x.l2 + ")");
        }
        Vector2D v = new Vector2D(l1, x.l2);
        for (int i = 0; i < l1; ++i) {
            for (int j = 0; j < x.l2; ++j) {
                for (int k = 0; k < l2; ++k) {
                    v.val[i][j] += val[i][k] * x.val[k][j];
                }
            }
        }
        return v;
    }

    public Vector2D transpose() {
        Vector2D v = new Vector2D(l2, l1);
        for (int i = 0; i < l1; ++i) {
            for (int j = 0; j < l2; ++j) {
                v.val[j][i] = val[i][j];
            }
        }
        return v;
    }

    public Pair<Integer> getArgMax() {
        double maxValue = Double.MIN_VALUE;
        int maxI = 0, maxJ = 0;
        for (int i = 0; i < l1; ++i) {
            for (int j = 0; j < l2; ++j) {
                if (val[i][j] > maxValue) {
                    maxValue = val[i][j];
                    maxI = i;
                    maxJ = j;
                }
            }
        }
        return new Pair<>(maxI, maxJ);
    }

    public void printShape() {
        System.out.println("[" + l1 + ", " + l2 + "]");
    }

    public Vector2D copy() {
        Vector2D v = new Vector2D(l1, l2);
        for (int i = 0; i < l1; ++i) {
            for (int j = 0; j < l2; ++j) {
                v.setVal(i, j, val[i][j]);
            }
        }
        return v;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector2D)) {
            return false;
        }
        Vector2D v = (Vector2D) obj;
        if (v.getL1() != l1 || v.getL2() != l2) {
            return false;
        }
        for (int i = 0; i < l1; ++i) {
            for (int j = 0; j < l2; ++j) {
                if (val[i][j] != v.getVal(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("[");
        for (int i = 0; i < l1; ++i) {
            s.append('[');
            for (int j = 0; j < l2; ++j) {
                s.append(val[i][j]);
                if (j < l2 - 1) {
                    s.append(", ");
                }
            }
            s.append(']');
            if (i < l1 - 1) {
                s.append(", ");
            }
        }
        s.append(']');
        return s.toString();
    }

    public void saveToFile(OutputStream outputStream) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(this);
        objectOutputStream.close();
    }

    public static Vector2D readFromFile(InputStream inputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Vector2D vector2D = (Vector2D) objectInputStream.readObject();
        objectInputStream.close();
        return vector2D;
    }

    public void saveAsImageToFile(File file, int width, int height) throws IOException {
        BufferedImage image = new BufferedImage(14, 14, BufferedImage.TYPE_INT_RGB);

        int brightness;
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                brightness = 255 - (int) val[i * width + j][0];
                image.setRGB(j, i, new Color(brightness, brightness, brightness).getRGB());
            }
        }

        ImageIO.write(image, "jpg", file);
    }

    public static Vector2D readFromImage(File file) throws IOException {
        BufferedImage image;
        image = ImageIO.read(file);
        int width = image.getWidth();
        int height = image.getHeight();
        Vector2D pic = new Vector2D(width * height);
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                pic.setVal(i * height + j, 255 - new Color(image.getRGB(j, i)).getRed());
            }
        }
        return pic;
    }

}
