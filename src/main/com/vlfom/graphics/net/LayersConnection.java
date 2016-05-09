package com.vlfom.graphics.net;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;

/**
 * Created by @vlfom.
 */
public class LayersConnection extends JComponent {
    private Layer left;
    private Layer right;

    public LayersConnection(Layer left, Layer right) {
        this.left = left;
        this.right = right;
    }

    private void drawArrow(Graphics2D g2, Point tail, Point head) {
        g2.drawLine(
                (int) tail.getX(), (int) tail.getY(),
                (int) head.getX(), (int) head.getY()
        );

        double phi = Math.toRadians(40);
        int barb = 10;
        double dy = head.y - tail.y;
        double dx = head.x - tail.x;
        double theta = Math.atan2(dy, dx);
        int[] x = new int[3];
        int[] y = new int[3];
        x[0] = head.x;
        x[1] = (int) (head.x - barb * Math.cos(theta + phi));
        x[2] = (int) (head.x - barb * Math.cos(theta - phi));
        y[0] = head.y;
        y[1] = (int) (head.y- barb * Math.sin(theta + phi));
        y[2] = (int) (head.y - barb * Math.sin(theta - phi));
        g2.fillPolygon(x, y, 3);
    }

    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        Point locLeft;
        Point locRight;

        locLeft = (Point) left.getLocation().clone();
        locRight = (Point) right.getLocation().clone();

        locLeft.translate(-26, 80);
        locRight.translate(-26 - Layer.WIDTH, 80);

        locLeft.translate(0, -50);
        locRight.translate(0, -50);

        locLeft.translate(0, -80);

        locLeft.translate(0, 80);
        locRight.translate(0, 80);
        drawArrow(g2D, locLeft, locRight);

        locLeft.translate(0, 80);
        locRight.translate(0, 80);
        drawArrow(g2D, locLeft, locRight);

        locLeft.translate(0, 80);
        locRight.translate(0, 80);
        drawArrow(g2D, locLeft, locRight);

        locLeft = (Point) left.getLocation().clone();
        locRight = (Point) right.getLocation().clone();

        locLeft.translate(-26, 80);
        locRight.translate(-26 - Layer.WIDTH, 80);

        locLeft.translate(0, -50);
        locRight.translate(0, -50);

        locRight.translate(0, -80);
        drawArrow(g2D, locLeft, locRight);

        locLeft.translate(0, 80);
        locRight.translate(0, 80);
        drawArrow(g2D, locLeft, locRight);

        locLeft.translate(0, 80);
        locRight.translate(0, 80);
        drawArrow(g2D, locLeft, locRight);

        locLeft = (Point) left.getLocation().clone();
        locRight = (Point) right.getLocation().clone();

        locLeft.translate(-26, 80);
        locRight.translate(-26 - Layer.WIDTH, 80);

        locLeft.translate(0, -50);
        locRight.translate(0, -50);

        drawArrow(g2D, locLeft, locRight);

        locLeft.translate(0, 80);
        locRight.translate(0, 80);
        drawArrow(g2D, locLeft, locRight);

        locLeft.translate(0, 80);
        locRight.translate(0, 80);
        drawArrow(g2D, locLeft, locRight);
    }
}
