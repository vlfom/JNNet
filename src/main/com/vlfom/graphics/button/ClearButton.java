package com.vlfom.graphics.button;

import java.awt.*;

/**
 * Created by @vlfom.
 */
public class ClearButton extends StyledButton {

    public ClearButton(LayoutManager layout) {
        super(layout);
    }

    @Override
    protected void paintImage(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.setStroke(new BasicStroke(3));
        g2D.setColor(new Color(130, 130, 255));
        g2D.drawLine(5, 5, 13, 13);
        g2D.drawLine(13, 5, 5, 13);
    }
}