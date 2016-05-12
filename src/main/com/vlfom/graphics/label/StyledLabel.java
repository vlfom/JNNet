package com.vlfom.graphics.label;

import javax.swing.*;
import java.awt.*;

/**
 * Created by @vlfom.
 */
public class StyledLabel extends JPanel{
    public JLabel label;

    public StyledLabel(String text, int width, Color backgroundColor, Color borderColor,
                       int topBorder, int leftBorder, int bottomBorder, int rightBorder) {
        label = new JLabel(text);
        label.setBackground(new Color(0, 0, 0, 0));
        label.setFont(new Font("Monospace", Font.PLAIN, 13));
        label.setOpaque(true);
        label.setBounds(9, 0, 244, 18);
        label.setSize(new Dimension(width-1, 19));

        setLayout(null);
        setBounds(0, 0, width, 20);
        setBorder(BorderFactory.createMatteBorder(topBorder, leftBorder, bottomBorder, rightBorder, borderColor));
        setBackground(backgroundColor);

        add(label);
    }
}
