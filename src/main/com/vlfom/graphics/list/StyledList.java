package com.vlfom.graphics.list;

import com.vlfom.graphics.StyledLabel;
import com.vlfom.graphics.window.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by @vlfom.
 */
public class StyledList extends JPanel {
    private List<StyledLabel> items;
    private int width;
    private int x, y;
    private int selectedItem;
    private JPopupMenu popupMenu;
    private MainWindow mainWindow;

    public StyledList(int x, int y, int width, MainWindow mainWindow) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.mainWindow = mainWindow;

        selectedItem = -1;
        items = new ArrayList<>();
        setLayout(null);

        popupMenu = new JPopupMenu();
        JMenuItem txtFileItem = new JMenuItem("Remove layer");
        Font font = new Font("Monospace", Font.PLAIN, 14);
        txtFileItem.setFont(font);
        txtFileItem.setPreferredSize(new Dimension(140, 25));
        txtFileItem.addMouseListener(new MenuAdapter(mainWindow));
        txtFileItem.addMouseMotionListener(new MenuAdapter(mainWindow));
        popupMenu.add(txtFileItem);
    }

    public void add(String name) {
        StyledLabel item = new StyledLabel(
                name, width, Color.white, new Color(232, 232, 232), 0, 1, 1, 1
        );
        item.addMouseListener(new ListItemMouseListener(this, item, items.size()));
        items.add(item);
        redraw();
    }

    private void redraw() {
        removeAll();
        for (int i = 0; i < items.size(); ++i) {
            items.get(i).setBounds(0, 20 * i, width, 20);
            add(items.get(i));
        }
        setBounds(x, y, width, 20 * items.size());
        setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
    }

    private void unselectItem() {
        if (selectedItem > -1) {
            items.get(selectedItem).setBackground(Color.white);
            selectedItem = -1;
        }
    }

    private class ListItemMouseListener extends MouseAdapter {
        private StyledList list;
        private StyledLabel item;
        private int itemIndex;

        public ListItemMouseListener(StyledList list, StyledLabel item, int itemIndex) {
            this.list = list;
            this.item = item;
            this.itemIndex = itemIndex;
        }

        @Override
        public void mousePressed (MouseEvent e) {
            unselectItem();
            item.setBackground(new Color(230, 230, 250));
            list.selectedItem = itemIndex;
            if (SwingUtilities.isRightMouseButton(e)) {
                popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    private class MenuAdapter extends MouseAdapter {
        private MainWindow window;

        public MenuAdapter(MainWindow window) {
            this.window = window;
        }

        @Override
        public void mousePressed (MouseEvent e) {
            window.net.removeLayer(selectedItem);
            window.clearScreen();
            window.repaintNetwork();
        }
    }
}


