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

public class MainWindow extends JFrame {
    private NeuralNetwork net;

    private void clearScreen() {
        getContentPane().removeAll();
        repaint();
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setPreferredSize(new Dimension(1600, 25));
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

        add(picturePanel);
        revalidate();
        repaint();
    }

    public MainWindow(String name) {
        super(name);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1600, 900));
        setJMenuBar(createMenuBar());
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void initNeuralNetwork() {
        //net = new NeuralNetwork(ActivationFunction.SIGMOID);
        net = new NeuralNetwork(3, new int[] {196, 200, 10}, ActivationFunction.SIGMOID);
        int layersCount = 3;
        List<Integer> layerSizes = net.getLayerSizes();
        List<Layer> layers = new ArrayList<>(layersCount);

        JPanel netPanel = new JPanel(null);
        for (int i = 0; i < layersCount; ++i) {
            Layer layer = new Layer(i + 1, layerSizes.get(i));
            layer.setBounds(600 + i * 300, 200, Layer.WIDTH + 1, Layer.HEIGHT + 1);
            netPanel.add(layer);

            MovingAdapter movingAdapter = new MovingAdapter(layer);
            layer.addMouseListener(movingAdapter);
            layer.addMouseMotionListener(movingAdapter);

            layers.add(layer);
        }

        for (int i = 0; i < layerSizes.size() - 1; ++i) {
            LayersConnection connection = new LayersConnection(layers.get(i), layers.get(i+1));
            connection.setBounds(100, 100, 10000, 10000);
            netPanel.add(connection);
        }

        add(netPanel);
        revalidate();
        repaint();
    }

    public static void main(String... args) throws Exception {
        new MainWindow("JNNet GUI");
    }
}