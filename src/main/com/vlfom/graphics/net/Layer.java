package com.vlfom.graphics.net;

import com.vlfom.graphics.window.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by @vlfom.
 */
public class Layer extends JComponent {
    public static final byte MARK_FIRST = -1;
    public static final byte MARK_REGULAR = 0;
    public static final byte MARK_LAST = 1;
    public static final int WIDTH = 73;
    public static final int HEIGHT = 400;

    private String title;
    private int layerSize;
    private int layerID;
    private byte mark;

    private JPopupMenu popupMenu;

    private class MovingAdapter extends MouseAdapter {
        private MainWindow window;

        public MovingAdapter(MainWindow window) {
            this.window = window;
        }

        @Override
        public void mousePressed (MouseEvent e) {
            System.out.println(window.net.getLayersCount());
            window.net.removeLayer(layerID);
            window.clearScreen();
            window.repaintNetwork();
        }
    }

    public Layer(String title, int layerID, int layerSize, byte mark, MainWindow mainWindow) {
        this.title = title;
        this.layerID = layerID;
        this.layerSize = layerSize;
        this.mark = mark;

        setLayout(null);
        addMouseListener(new MyAdapter());

        popupMenu = new JPopupMenu();
        JMenuItem txtFileItem = new JMenuItem("Remove layer");
        Font font = new Font("Monospace", Font.PLAIN, 14);
        txtFileItem.setFont(font);
        txtFileItem.setPreferredSize(new Dimension(140, 25));
        txtFileItem.addMouseListener(new MovingAdapter(mainWindow));
        txtFileItem.addMouseMotionListener(new MovingAdapter(mainWindow));
        popupMenu.add(txtFileItem);
    }

    private class MyAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        if (mark == MARK_FIRST)
            g2D.setColor(new Color(10, 200, 10, 20));
        else if(mark == MARK_REGULAR)
            g2D.setColor(new Color(10, 10, 200, 20));
        else if(mark == MARK_LAST)
            g2D.setColor(new Color(200, 10, 10, 20));
        g2D.fillRoundRect(0, 0, WIDTH, HEIGHT, 10, 10);
        g2D.setColor(Color.BLACK);
        g2D.drawRoundRect(0, 0, WIDTH, HEIGHT, 10, 10);

        g2D.setFont(new Font("Verdana", Font.BOLD, 14));
        g2D.drawString(title, 8, 20);

        g2D.setFont(new Font("Verdana", Font.PLAIN, 12));
        if (mark == MARK_FIRST)
            g2D.drawString("(input)", 14, 38);
        else if(mark == MARK_LAST)
            g2D.drawString("(output)", 10, 38);

        g2D.rotate(-Math.PI / 2, 30, 60);
        Font font = new Font("Verdana", Font.PLAIN, 24);
        int width = g2D.getFontMetrics(font).stringWidth("Size: " + layerSize);
        g2D.setFont(new Font("Verdana", Font.PLAIN, 24));
        g2D.drawString("Size: " + layerSize, (80 - 300) / 2 - width / 2, 75);

        revalidate();
        repaint();
    }
}
