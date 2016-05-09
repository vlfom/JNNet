package com.vlfom.graphics.img;

import javax.swing.*;
import java.awt.*;

/**
 * Created by @vlfom.
 */
public class GridImage extends JComponent {
    private double[] color;

    public GridImage(double[] color) {
        this.color = color;
    }

    private void drawGrid(Graphics g, int w, int h) {
        g.drawRect(0, 0, 150, 150);
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
