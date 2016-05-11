package com.vlfom.graphics.window;

/**
 * Created by @vlfom.
 */
import com.vlfom.data.DataCompressor;
import com.vlfom.data.DataLoader;
import com.vlfom.graphics.StyledLabel;
import com.vlfom.graphics.img.GridImage;
import com.vlfom.graphics.list.StyledList;
import com.vlfom.graphics.net.Layer;
import com.vlfom.graphics.net.LayersConnection;
import com.vlfom.neuralnet.NeuralNetwork;
import com.vlfom.neuralnet.activation.ActivationFunction;
import com.vlfom.utils.Pair;
import com.vlfom.utils.Vector2D;

import java.awt.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class MainWindow extends JFrame {
    public NeuralNetwork net;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel mainPanel;
    private JPanel bottomPanel;

    public void clearScreen() {
        mainPanel.removeAll();
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setPreferredSize(new Dimension(1600, 25));
        menuBar.setBackground(new Color(232, 232, 232));
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(160, 160, 160)));

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
        constructorMenu.setPreferredSize(new Dimension(100, 25));

        JMenuItem newNetwork = new JMenuItem("New network...");
        newNetwork.setFont(font);
        constructorMenu.add(newNetwork);

        newNetwork.addActionListener(e -> {
            clearScreen();
            initNeuralNetwork();
            repaintNetwork();
        });

        JMenuItem addLayer = new JMenuItem("Add layer");
        addLayer.setFont(font);
        addLayer.setPreferredSize(new Dimension(160, 25));
        constructorMenu.add(addLayer);

        addLayer.addActionListener(e -> {
            clearScreen();
            net.addLayer(10);
            repaintNetwork();
            revalidate();
            repaint();
        });

        JMenu dataMenu = new JMenu("Data");
        dataMenu.setFont(font);
        dataMenu.setPreferredSize(new Dimension(50, 25));

        JMenuItem loadData = new JMenuItem("Load data...");
        loadData.setFont(font);
        loadData.setPreferredSize(new Dimension(160, 25));
        dataMenu.add(loadData);

        JMenu netMenu = new JMenu("Network");
        netMenu.setFont(font);
        netMenu.setPreferredSize(new Dimension(80, 25));

        JMenuItem launchSGD = new JMenuItem("Launch SGD...");
        launchSGD.setFont(font);
        launchSGD.setPreferredSize(new Dimension(160, 25));
        netMenu.add(launchSGD);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(constructorMenu);
        menuBar.add(dataMenu);
        menuBar.add(netMenu);
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
            if (SwingUtilities.isLeftMouseButton(e)) {
                object.setLocation(coordinates.x + e.getXOnScreen() - mouseCoords.x, coordinates.y + e.getYOnScreen() - mouseCoords.y);
                repaint();
            }
        }

        @Override
        public void mousePressed (MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                coordinates = object.getLocation();
                mouseCoords = e.getLocationOnScreen();
            }
        }
    }

    private void paintImages() throws Exception {
        InputStream inputStream = new FileInputStream(new File("res/data/mnist/train.csv"));
        List<Pair<Vector2D>> data = DataLoader.loadData(inputStream, 784, 10, 32);
        DataCompressor.compressData(data, 28, 14);

        for (int pictureID = 0; pictureID < 32; ++pictureID) {
            double[] values = data.get(pictureID).getFirst().toArray();
            for (int i = 0; i < values.length; ++i)
                values[i] /= 255.0;

            int x = pictureID % 8;
            int y = pictureID / 8;

            GridImage picture = new GridImage(values, 5);
            int scale = 100;
            picture.setBounds(200 + scale * x, 90 + scale * y, scale, scale);
            mainPanel.add(picture);
            MovingAdapter movingAdapter = new MovingAdapter(picture);
            picture.addMouseListener(movingAdapter);
            picture.addMouseMotionListener(movingAdapter);
        }

        revalidate();
        repaint();
    }

    StyledList layersList;

    private void addLeftPanel() {
        leftPanel = new JPanel(null);

        leftPanel.setBounds(0, 0, 250, 600);
        leftPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 1, new Color(160, 160, 160)));
        leftPanel.setBackground(new Color(255, 255, 255));

        leftPanel.add(new StyledLabel(
                "Object browser", 250, new Color(232, 232, 232), new Color(160, 160, 160), 0, 0, 1, 1
        ));

        layersList = new StyledList(0, 20, 249, this);
        add(layersList);

        add(leftPanel);

        revalidate();
        repaint();
    }

    private void addRightPanel() {
        rightPanel = new JPanel(null);

        rightPanel.setBounds(1580, 0, 20, 600);
        rightPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(160, 160, 160)));
        rightPanel.setBackground(new Color(232, 232, 232));

        add(rightPanel);

        revalidate();
        repaint();
    }

    private void addBottomPanel() {
        bottomPanel = new JPanel(null);

        bottomPanel.setBounds(0, 600, 1600, 300);
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, new Color(160, 160, 160)));
        bottomPanel.setBackground(new Color(255, 255, 255));

        bottomPanel.add(new StyledLabel(
                "Logs", 1600, new Color(232, 232, 232), new Color(160, 160, 160), 1, 0, 1, 0
        ));

        JPanel leftSubPanel = new JPanel(null);
        leftSubPanel.setBounds(0, 20, 20, 300);
        leftSubPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(160, 160, 160)));
        leftSubPanel.setBackground(new Color(232, 232, 232));
        bottomPanel.add(leftSubPanel);

        add(bottomPanel);

        revalidate();
        repaint();
    }

    public void addMainPanel() {
        mainPanel = new JPanel(null);

        mainPanel.setBounds(250, 0, 1330, 600);
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

        pack();
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);

        addLeftPanel();
        addRightPanel();
        addBottomPanel();
        addMainPanel();
    }

    public void repaintNetwork() {
        int layersCount = net.getLayersCount();
        List<Integer> layerSizes = net.getLayerSizes();
        List<Layer> layers = new ArrayList<>(layersCount);

        double segmentWidth = mainPanel.getWidth() * 1.0 / layersCount;

        for (int i = 0; i < layersCount; ++i) {
            Layer layer = new Layer(
                    "Layer " + (i + 1),
                    i,
                    layerSizes.get(i),
                    (i == 0 ? Layer.MARK_FIRST :i == layersCount - 1 ? Layer.MARK_LAST : Layer.MARK_REGULAR),
                    this);
            layer.setBounds((int) (segmentWidth * i + (segmentWidth - Layer.WIDTH) / 2.0), 100, Layer.WIDTH + 1, Layer.HEIGHT + 1);
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

    private void initNeuralNetwork() {
        net = new NeuralNetwork(3, new int[] {196, 300, 10}, ActivationFunction.SIGMOID);
        List<Integer> layerSizes = net.getLayerSizes();
        for (int i = 0; i < layerSizes.size(); ++i)
            layersList.add("Layer " + (i+1) + ", " + layerSizes.get(i) + " neurons");
    }

    public static void main(String... args) throws Exception {
        new MainWindow("JNNet GUI");
    }
}