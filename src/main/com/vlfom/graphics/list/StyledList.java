package com.vlfom.graphics.list;

import com.vlfom.graphics.label.StyledLabel;
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
    public List<StyledLabel> items;
    private int width;
    private int x, y;
    private int selectedItem;
    private JPopupMenu popupMenu;
    private boolean interactive;
    private MainWindow mainWindow;

    public StyledList(int x, int y, int width, boolean interactive, MainWindow mainWindow) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.interactive = interactive;
        this.mainWindow = mainWindow;

        selectedItem = -1;
        items = new ArrayList<>();
        setLayout(null);

        if (interactive) {

            popupMenu = new JPopupMenu();

            JMenuItem changeSizeItem = new JMenuItem("Change size...");
            Font font = new Font("Monospace", Font.PLAIN, 14);
            changeSizeItem.setFont(font);
            changeSizeItem.setPreferredSize(new Dimension(140, 25));
            changeSizeItem.addMouseListener(new MenuAdapter(mainWindow, 0));
            changeSizeItem.addMouseMotionListener(new MenuAdapter(mainWindow, 0));
            popupMenu.add(changeSizeItem);

            JMenuItem removeLayerItem = new JMenuItem("Remove layer");
            removeLayerItem.setFont(font);
            removeLayerItem.setPreferredSize(new Dimension(140, 25));
            removeLayerItem.addMouseListener(new MenuAdapter(mainWindow, 1));
            removeLayerItem.addMouseMotionListener(new MenuAdapter(mainWindow, 1));
            popupMenu.add(removeLayerItem);
        }

    }

    public void add(String name) {
        StyledLabel item = new StyledLabel(name, 9, 0, width, 18, Color.white, new Color(232, 232, 232), 0, 1, 1, 1);
        if (interactive)
            item.addMouseListener(new ListItemMouseListener(this, item, items.size()));
        items.add(item);
        redraw();
    }

    public void redraw() {
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

    public void clear() {
        removeAll();
        items.clear();
        selectedItem = -1;
        setBounds(x, y, width, 20 * items.size());
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
        public void mousePressed(MouseEvent e) {
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
        private int type;

        public MenuAdapter(MainWindow window, int type) {
            this.window = window;
            this.type = type;
        }

        @Override
        public void mousePressed (MouseEvent e) {
            if (type == 0) {
                window.changeNetworkLayerSize(selectedItem);
            }
            else if (type == 1)
                window.removeNetworkLayer(selectedItem);
            selectedItem = -1;
        }
    }
}


