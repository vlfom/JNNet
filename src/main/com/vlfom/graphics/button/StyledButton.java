package com.vlfom.graphics.button;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by @vlfom.
 */
public abstract class StyledButton extends JPanel {
    protected boolean hovered;

    public StyledButton(LayoutManager layout) {
        super(layout);
        hovered = false;
        setBackground(new Color(232, 232, 232));
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                setBackground(new Color(232, 232, 232));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                setBackground(new Color(200, 200, 200));
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (hovered) {
            g.setColor(new Color(130, 130, 130));
            g.drawRoundRect(0, 0, 17, 17, 3, 3);
        }
        paintImage(g);
    }

    protected abstract void paintImage(Graphics g);
}