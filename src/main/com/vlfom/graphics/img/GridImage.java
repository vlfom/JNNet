package com.vlfom.graphics.img;

import javax.swing.*;
import java.awt.*;

/**
 * Created by @vlfom.
 */
public class GridImage extends JComponent {
    private double[] color;
    private int scale;

    public GridImage(double[] color, int scale) {
        this.color = color;
        this.scale = scale;
    }

    private void drawGrid(Graphics g, int w, int h) {
        g.drawRect(0, 0, 80, 80);
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
