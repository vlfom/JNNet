package com.vlfom.graphics.mouse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by @vlfom.
 */
public class MovingAdapter extends MouseAdapter {
    private Point mouseCoords;
    private Point coordinates;
    private JComponent object;
    private JFrame parent;

    public MovingAdapter(JComponent object, JFrame parent) {
        this.object = object;
        this.parent = parent;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            object.setLocation(coordinates.x + e.getXOnScreen() - mouseCoords.x, coordinates.y + e.getYOnScreen() - mouseCoords.y);
            parent.repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            coordinates = object.getLocation();
            mouseCoords = e.getLocationOnScreen();
        }
    }
}