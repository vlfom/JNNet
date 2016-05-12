package com.vlfom.graphics.button;

import java.awt.*;

/**
 * Created by @vlfom.
 */
public class ResumeButton extends StyledButton {

    public ResumeButton(LayoutManager layout) {
        super(layout);
    }

    @Override
    protected void paintImage(Graphics g) {
        g.setColor(new Color(130, 170, 130));
        g.fillPolygon(new int[] {4, 15, 4}, new int[] {3, 9, 15}, 3);
    }
}