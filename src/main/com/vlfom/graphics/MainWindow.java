package com.vlfom.graphics;

/**
 * Created by @vlfom.
 */
import com.vlfom.data.DataCompressor;
import com.vlfom.data.DataLoader;
import com.vlfom.graphics.img.GridImage;
import com.vlfom.graphics.net.Layer;
import com.vlfom.graphics.net.LayersConnection;
import com.vlfom.neuralnet.NeuralNetwork;
import com.vlfom.neuralnet.activation.ActivationFunction;
import com.vlfom.utils.Pair;
import com.vlfom.utils.Vector2D;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.border.Border;

public class MainWindow extends JFrame {
    private NeuralNetwork net;
    private JPanel leftPanel;
    private JPanel mainPanel;
    private JPanel bottomPanel;

    private void clearScreen() {
        mainPanel.removeAll();
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setPreferredSize(new Dimension(1600, 25));
        menuBar.setBackground(new Color(232, 232, 232));

        Font font = new Font("Monospace", Font.PLAIN, 14);

        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(font);
        fileMenu.setPreferredSize(new Dimension(40, 25));

        JMenu newMenu = new JMenu("New");
        newMenu.setFont(font);
        newMenu.setPreferredSize(new Dimension(160, 25));
        fileMenu.add(newMenu);

        JMenuItem txtFileItem = new JMenuItem("Text file");
        txtFileItem.setFont(font);
        newMenu.add(txtFileItem);

        JMenuItem imgFileItem = new JMenuItem("GridImage file");
        imgFileItem.setFont(font);
        newMenu.add(imgFileItem);

        JMenuItem folderItem = new JMenuItem("Folder");
        folderItem.setFont(font);
        newMenu.add(folderItem);

        JMenuItem openItem = new JMenuItem("Open");
        openItem.setFont(font);
        fileMenu.add(openItem);

        JMenuItem closeItem = new JMenuItem("Close");
        closeItem.setFont(font);
        fileMenu.add(closeItem);

        JMenuItem closeAllItem = new JMenuItem("Close all");
        closeAllItem.setFont(font);
        fileMenu.add(closeAllItem);

        fileMenu.addSeparator();

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setFont(font);
        fileMenu.add(exitItem);

        exitItem.addActionListener(e -> System.exit(0));

        JMenu viewMenu = new JMenu("View");
        viewMenu.setFont(font);
        viewMenu.setPreferredSize(new Dimension(50, 25));

        JMenuItem paintMenu = new JMenuItem("Paint");
        paintMenu.setFont(font);
        paintMenu.setPreferredSize(new Dimension(160, 25));
        viewMenu.add(paintMenu);
        paintMenu.addActionListener(e -> {
            try {
                clearScreen();
                paintImages();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        JMenu constructorMenu = new JMenu("Constructor");
        constructorMenu.setFont(font);
        constructorMenu.setPreferredSize(new Dimension(110, 25));

        JMenuItem newNetwork = new JMenuItem("New network...");
        newNetwork.setFont(font);
        constructorMenu.add(newNetwork);

        newNetwork.addActionListener(e -> {
            clearScreen();
            initNeuralNetwork();
        });

        JMenuItem addLayer = new JMenuItem("Add layer");
        addLayer.setFont(font);
        addLayer.setPreferredSize(new Dimension(160, 25));
        constructorMenu.add(addLayer);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(constructorMenu);
        return menuBar;
    }

    private class MovingAdapter extends MouseAdapter {
        private Point mouseCoords;
        private Point coordinates;
        private JComponent object;

        public MovingAdapter(JComponent object) {
            this.object = object;
        }

        @Override
        public void mouseDragged (MouseEvent e) {
            object.setLocation(coordinates.x + e.getXOnScreen() - mouseCoords.x, coordinates.y + e.getYOnScreen() - mouseCoords.y);
            repaint();
        }

        @Override
        public void mousePressed (MouseEvent e) {
            coordinates = object.getLocation();
            mouseCoords = e.getLocationOnScreen();
        }
    }

    private void paintImages() throws Exception {
        JPanel picturePanel = new JPanel(null);

        InputStream inputStream = new FileInputStream(new File("res/data/mnist/train.csv"));
        List<Pair<Vector2D>> data = DataLoader.loadData(inputStream, 784, 10, 32);
        DataCompressor.compressData(data, 28, 14);

        for (int pictureID = 0; pictureID < 32; ++pictureID) {
            double[] values = data.get(pictureID).getFirst().toArray();
            for (int i = 0; i < values.length; ++i)
                values[i] /= 255.0;

            int x = pictureID % 8;
            int y = pictureID / 8;

            GridImage picture = new GridImage(values);
            picture.setBounds(200 + 150 * x, 90 + 150 * y, 151, 151);
            picturePanel.add(picture);
            MovingAdapter movingAdapter = new MovingAdapter(picture);
            picture.addMouseListener(movingAdapter);
            picture.addMouseMotionListener(movingAdapter);
        }

        picturePanel.setOpaque(true);
        picturePanel.setBackground(new Color(0,0,0,0));
        add(picturePanel);
        revalidate();
        repaint();
    }

    private void addLeftPanel() {
        leftPanel = new JPanel(null);

        leftPanel.setBounds(0, 0, 250, 600);
        leftPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 1, new Color(160, 160, 160)));
        leftPanel.setBackground(new Color(255, 255, 255));

        JPanel header = new JPanel(null);
        header.setBounds(0, 0, 250, 20);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(160, 160, 160)));
        header.setBackground(new Color(232, 232, 232));

        JLabel headerTitle = new JLabel("Object browser");
        headerTitle.setBackground(new Color(0, 0, 0, 0));
        headerTitle.setFont(new Font("Monospace", Font.PLAIN, 13));
        headerTitle.setOpaque(true);
        headerTitle.setBounds(9, 0, 244, 18);
        headerTitle.setSize(new Dimension(249, 19));

        header.add(headerTitle);

        leftPanel.add(header);

        add(leftPanel);

        revalidate();
        repaint();
    }

    private void addBottomPanel() {
        bottomPanel = new JPanel(null);

        bottomPanel.setBounds(0, 600, 1600, 300);
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, new Color(160, 160, 160)));
        bottomPanel.setBackground(new Color(255, 255, 255));

        JPanel header = new JPanel(null);
        header.setBounds(0, 0, 1600, 20);
        header.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, new Color(160, 160, 160)));
        header.setBackground(new Color(232, 232, 232));
        bottomPanel.add(header);

        JPanel leftSubPanel = new JPanel(null);
        leftSubPanel.setBounds(0, 20, 20, 300);
        leftSubPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(160, 160, 160)));
        leftSubPanel.setBackground(new Color(232, 232, 232));
        bottomPanel.add(leftSubPanel);

        JLabel headerTitle = new JLabel("Logs");
        headerTitle.setBackground(new Color(0, 0, 0, 0));
        headerTitle.setFont(new Font("Monospace", Font.PLAIN, 13));
        headerTitle.setOpaque(true);
        headerTitle.setBounds(9, 0, 244, 18);
        headerTitle.setSize(new Dimension(249, 19));
        header.add(headerTitle);

        add(bottomPanel);

        revalidate();
        repaint();
    }

    public void addMainPanel() {
        mainPanel = new JPanel(null);

        mainPanel.setBounds(250, 0, 1350, 600);
        mainPanel.setBackground(new Color(255, 255, 255));

        add(mainPanel);

        revalidate();
        repaint();
    }

    public MainWindow(String name) {
        super(name);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1600, 900));
        setJMenuBar(createMenuBar());
        setLayout(null);

//        JPanel panel = new JPanel(null);
//        JPanel header = new JPanel(null);
//        Component component = new Component() {
//            @Override
//            public void paint(Graphics g) {
//                g.setColor(Color.BLACK);
//                g.fillRect(100, 100, 500, 500);
//            }
//        };
//        component.setBounds(0, 0, 600, 600);
//        header.setBounds(100, 0, 600, 600);
//        header.setBorder(BorderFactory.createLineBorder(Color.black, 10));
//        panel.setBounds(0, 0, 600, 600);
//        panel.setBorder(BorderFactory.createLineBorder(Color.blue, 10));
//        header.add(component);
//        header.revalidate();
//        header.repaint();
//        panel.add(header);
//        panel.revalidate();
//        panel.repaint();
//        add(panel);

        pack();
        setVisible(true);
        setLocationRelativeTo(null);

        addLeftPanel();
        addBottomPanel();
        addMainPanel();
    }

    private void initNeuralNetwork() {
        //net = new NeuralNetwork(ActivationFunction.SIGMOID);
        net = new NeuralNetwork(4, new int[] {196, 200, 200, 10}, ActivationFunction.SIGMOID);
        int layersCount = net.getLayersCount();
        List<Integer> layerSizes = net.getLayerSizes();
        List<Layer> layers = new ArrayList<>(layersCount);

        int segmentWidth = mainPanel.getWidth() / layersCount;

        for (int i = 0; i < layersCount; ++i) {
            Layer layer = new Layer("Layer " + (i + 1), layerSizes.get(i), (i == 0 ? Layer.MARK_FIRST : i == layersCount - 1 ? Layer.MARK_LAST : Layer.MARK_REGULAR));
            layer.setBounds(segmentWidth * i + 100, 100, Layer.WIDTH + 1, Layer.HEIGHT + 1);
            mainPanel.add(layer);

            MovingAdapter movingAdapter = new MovingAdapter(layer);
            layer.addMouseListener(movingAdapter);
            layer.addMouseMotionListener(movingAdapter);

            layers.add(layer);
        }

        for (int i = 0; i < layerSizes.size() - 1; ++i) {
            LayersConnection connection = new LayersConnection(layers.get(i), layers.get(i+1));
            connection.setBounds(0, 0, 10000, 10000);
            mainPanel.add(connection);
        }

        revalidate();
        repaint();
    }

    public static void main(String... args) throws Exception {
        new MainWindow("JNNet GUI");
    }
}