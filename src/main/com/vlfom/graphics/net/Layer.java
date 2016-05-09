package com.vlfom.graphics.net;

import javax.swing.*;
import java.awt.*;

/**
 * Created by @vlfom.
 */
public class Layer extends JComponent {
    public static final int WIDTH = 73;
    public static final int HEIGHT = 400;

    private int layerSize;
    private int layerID;
    private LayersConnection left;
    private LayersConnection right;

    public Layer(int layerID, int layerSize) {
        this.layerID = layerID;
        this.layerSize = layerSize;
    }

    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(new Color(10, 10, 200, 20));
        g2D.fillRect(0, 0, WIDTH, HEIGHT);
        g2D.setColor(Color.BLACK);
        g2D.drawRect(0, 0, WIDTH, HEIGHT);
        g2D.setFont(new Font("Verdana", Font.BOLD, 14));
        g2D.drawString("Layer " + layerID, 8, 20);
        g2D.rotate(-Math.PI / 2, 30, 60);
        Font font = new Font("Verdana", Font.PLAIN, 24);
        System.out.println(g2D.getFontMetrics(font).stringWidth("Size: " + layerSize));
        int width = g2D.getFontMetrics(font).stringWidth("Size: " + layerSize);
        g2D.setFont(new Font("Verdana", Font.PLAIN, 24));
        g2D.drawString("Size: " + layerSize, (80 - 300) / 2 - width / 2, 75);

    }
}
