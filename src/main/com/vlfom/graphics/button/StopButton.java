package com.vlfom.graphics.button;

import java.awt.*;

/**
 * Created by @vlfom.
 */
public class StopButton extends StyledButton {
    public StopButton(LayoutManager layout) {
        super(layout);
    }

    @Override
    protected void paintImage(Graphics g) {
        g.setColor(new Color(240, 130, 130));
        g.fillRect(4, 4, 10, 10);
    }
}
