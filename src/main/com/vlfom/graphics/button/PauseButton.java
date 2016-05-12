package com.vlfom.graphics.button;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by @vlfom.
 */
public class PauseButton extends StyledButton {

    public PauseButton(LayoutManager layout) {
        super(layout);
    }

    @Override
    protected void paintImage(Graphics g) {
        g.setColor(new Color(130, 130, 255));
        g.fillRect(4, 3, 4, 12);
        g.fillRect(10, 3, 4, 12);
    }
}