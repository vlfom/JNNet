package com.vlfom.graphics.window;

/**
 * Created by @vlfom.
 */

import com.vlfom.data.DataCompressor;
import com.vlfom.data.DataLoader;
import com.vlfom.graphics.label.StyledLabel;
import com.vlfom.graphics.button.ResumeButton;
import com.vlfom.graphics.button.StopButton;
import com.vlfom.graphics.img.InputPicture;
import com.vlfom.graphics.list.StyledList;
import com.vlfom.graphics.net.Layer;
import com.vlfom.graphics.net.LayersConnection;
import com.vlfom.neuralnet.NeuralNetwork;
import com.vlfom.neuralnet.activation.ActivationFunction;
import com.vlfom.neuralnet.metrics.Metrics;
import com.vlfom.utils.Pair;
import com.vlfom.utils.Vector2D;

import java.awt.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

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

    private JMenu fileMenu;
    private JMenuItem openItem;
    private JMenuItem saveItem;
    private JMenuItem closeItem;
    private JMenuItem exitItem;
    private JMenu viewMenu;
    private JMenuItem paintItem;
    private JMenu constructorMenu;
    private JMenuItem newNetworkItem;
    private JMenuItem addLayerItem;
    private JMenu dataMenu;
    private JMenuItem loadDataItem;
    private JMenu netMenu;
    private JMenuItem launchSGDItem;
    private JMenuItem makePredictionItem;
    private JMenu helpMenu;
    private JMenuItem aboutItem;


    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setPreferredSize(new Dimension(1600, 25));
        menuBar.setBackground(new Color(232, 232, 232));
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(160, 160, 160)));

        Font font = new Font("Monospace", Font.PLAIN, 14);

        fileMenu = new JMenu("File");
        fileMenu.setFont(font);
        fileMenu.setPreferredSize(new Dimension(40, 25));

        openItem = new JMenuItem("Open...");
        openItem.setFont(font);
        openItem.setPreferredSize(new Dimension(160, 25));
        openItem.addActionListener(e -> {
            FileDialog fd = new FileDialog(this, "Choose a file", FileDialog.LOAD);
            fd.setFilenameFilter((dir, name) -> name.endsWith(".jnnet"));
            fd.setVisible(true);
            if (fd.getFile() != null) {
                loadNeuralNetwork(fd.getDirectory() + fd.getFile());
                saveItem.setEnabled(true);
            }
        });
        fileMenu.add(openItem);

        saveItem = new JMenuItem("Save");
        saveItem.setFont(font);
        saveItem.setPreferredSize(new Dimension(160, 25));
        saveItem.setEnabled(false);
        saveItem.addActionListener(e -> {
            FileDialog fileDialog = new FileDialog(new Frame(), "Save", FileDialog.SAVE);
            fileDialog.setFilenameFilter((dir, name) -> name.endsWith(".jnnet"));
            fileDialog.setFile(".jnnet");
            fileDialog.setVisible(true);
            if (fileDialog.getFile() != null) {
                saveNeuralNetwork(fileDialog.getDirectory() + fileDialog.getFile());
            }
        });
        fileMenu.add(saveItem);

        closeItem = new JMenuItem("Close");
        closeItem.setFont(font);
        fileMenu.add(closeItem);

        fileMenu.addSeparator();

        exitItem = new JMenuItem("Exit");
        exitItem.setFont(font);
        fileMenu.add(exitItem);

        exitItem.addActionListener(e -> System.exit(0));

        viewMenu = new JMenu("View");
        viewMenu.setFont(font);
        viewMenu.setPreferredSize(new Dimension(50, 25));

        paintItem = new JMenuItem("Paint");
        paintItem.setFont(font);
        paintItem.setPreferredSize(new Dimension(160, 25));
        viewMenu.add(paintItem);
        paintItem.addActionListener(e -> {
            try {
                clearScreen();
                paintImages();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        constructorMenu = new JMenu("Constructor");
        constructorMenu.setFont(font);
        constructorMenu.setPreferredSize(new Dimension(100, 25));

        newNetworkItem = new JMenuItem("New network...");
        newNetworkItem.setFont(font);
        newNetworkItem.addActionListener(e -> {
            clearScreen();
            initNeuralNetwork();
            repaintNetwork();
            addLayerItem.setEnabled(true);
            launchSGDItem.setEnabled(true);
            makePredictionItem.setEnabled(true);
            saveItem.setEnabled(true);
        });
        constructorMenu.add(newNetworkItem);

        addLayerItem = new JMenuItem("Add layer...");
        addLayerItem.setFont(font);
        addLayerItem.setPreferredSize(new Dimension(160, 25));
        addLayerItem.addActionListener(e -> {
            addNetworkLayer();
            clearScreen();
            repaintNetwork();
        });
        addLayerItem.setEnabled(false);
        constructorMenu.add(addLayerItem);

        dataMenu = new JMenu("Data");
        dataMenu.setFont(font);
        dataMenu.setPreferredSize(new Dimension(50, 25));

        loadDataItem = new JMenuItem("Load data...");
        loadDataItem.setFont(font);
        loadDataItem.setPreferredSize(new Dimension(160, 25));
        dataMenu.add(loadDataItem);

        netMenu = new JMenu("Network");
        netMenu.setFont(font);
        netMenu.setPreferredSize(new Dimension(80, 25));

        launchSGDItem = new JMenuItem("Launch SGD...");
        launchSGDItem.setFont(font);
        launchSGDItem.setPreferredSize(new Dimension(160, 25));
        launchSGDItem.addActionListener(e -> {
            launchSGD();
        });
        launchSGDItem.setEnabled(false);
        netMenu.add(launchSGDItem);

        makePredictionItem = new JMenuItem("Make prediction...");
        makePredictionItem.setFont(font);
        makePredictionItem.setPreferredSize(new Dimension(160, 25));
        makePredictionItem.addActionListener(e -> {
            makeNetworkPrediction();
        });
        makePredictionItem.setEnabled(false);
        netMenu.add(makePredictionItem);

        helpMenu = new JMenu("Help");
        helpMenu.setFont(font);
        helpMenu.setPreferredSize(new Dimension(80, 25));

        aboutItem = new JMenuItem("About");
        aboutItem.setFont(font);
        aboutItem.setPreferredSize(new Dimension(160, 25));
        aboutItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "JNNet - Java Neural Net library\nVolodymyr Fomenko IP-42", "Author", JOptionPane.INFORMATION_MESSAGE);
        });
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(constructorMenu);
        menuBar.add(dataMenu);
        menuBar.add(netMenu);
        menuBar.add(helpMenu);

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
        public void mouseDragged(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                object.setLocation(coordinates.x + e.getXOnScreen() - mouseCoords.x, coordinates.y + e.getYOnScreen() - mouseCoords.y);
                repaint();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
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

        data.get(0).getFirst().saveToFile(new FileOutputStream(new File("res/img/pic0.jnnpg")));

        for (int pictureID = 0; pictureID < 32; ++pictureID) {
            double[] values = data.get(pictureID).getFirst().toArray();
            for (int i = 0; i < values.length; ++i) {
                values[i] /= 255.0;
            }

            int x = pictureID % 8;
            int y = pictureID / 8;

            InputPicture picture = new InputPicture(values, 5);
            int scale = 100;
            picture.setBounds(250 + scale * x, 90 + scale * y, scale, scale);
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

        leftPanel.add(new StyledLabel("Object browser", 250, new Color(232, 232, 232), new Color(160, 160, 160), 0, 0, 1, 1));

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

    JTextArea logsArea;

    private void addBottomPanel() {
        bottomPanel = new JPanel(null);

        bottomPanel.setBounds(0, 600, 1600, 300);
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, new Color(160, 160, 160)));
        bottomPanel.setBackground(new Color(255, 255, 255));

        bottomPanel.add(new StyledLabel("Logs", 1600, new Color(232, 232, 232), new Color(160, 160, 160), 1, 0, 1, 0));

        JPanel leftSubPanel = new JPanel(null);
        leftSubPanel.setBounds(0, 20, 25, 300);
        leftSubPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(160, 160, 160)));
        leftSubPanel.setBackground(new Color(232, 232, 232));
        bottomPanel.add(leftSubPanel);

        JPanel resumeButton = new ResumeButton(null);
        resumeButton.setBounds(3, 3, 18, 18);
        leftSubPanel.add(resumeButton);

        resumeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                launchSGD();
            }
        });

        JPanel stopButton = new StopButton(null);
        stopButton.setBounds(3, 23, 18, 18);
        leftSubPanel.add(stopButton);

        stopButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                sgd.stop();
            }
        });

        logsArea = new JTextArea("", 2, 10);
        logsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logsArea);
        scrollPane.setBounds(45, 20, 1547, 225);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBackground(Color.white);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected JButton createDecreaseButton(int orientation) {
                JButton button = super.createDecreaseButton(orientation);
                button.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, new Color(160, 160, 160)));
                button.setBackground(new Color(232, 232, 232));
                button.setForeground(Color.green);
                return button;
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                JButton button = super.createIncreaseButton(orientation);
                button.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, new Color(160, 160, 160)));
                button.setBackground(new Color(232, 232, 232));
                return button;
            }

            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(212, 212, 212);
            }
        });
        bottomPanel.add(scrollPane);

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

        //exportImage("res/img/raw/me_28x28.jpg", "res/img/me_28x28.jnnpg");
    }

    private void exportImage(String pathFrom, String pathTo) {
        try {
            File input = new File(pathFrom);
            BufferedImage image = null;
            image = ImageIO.read(input);
            int width = image.getWidth();
            int height = image.getHeight();
            Vector2D pic = new Vector2D(width * height);
            for (int i = 0; i < width; ++i) {
                for (int j = 0; j < height; ++j) {
                    pic.setVal(i * height + j, 255 - new Color(image.getRGB(j, i)).getRed());
                }
            }
            pic.saveToFile(new FileOutputStream(new File(pathTo)));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    Thread sgd;
    public void launchSGD() {
        sgd = new Thread() {
            public void run() {
                PrintStream printStream = new PrintStream(new JTextAreaOutputStream(logsArea));
                try {
                    printStream.println("Launching stochastic gradient descent on MNIST dataset...\n");

                    InputStream inputStream = new FileInputStream(new File("res/data/mnist/train.csv"));
                    List<Pair<Vector2D>> data = DataLoader.loadData(inputStream, 784, 10, 10000);
                    DataCompressor.compressData(data, 28, 14);

                    List<Pair<Vector2D>> trainData = data;
                    List<Pair<Vector2D>> testData = data;

                    net.launchSGD(trainData, testData, 10, 100, 1, Metrics.ACCURACY, printStream);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                printStream.println();
                printStream.close();
            }
        };

        sgd.start();
    }

    public class JTextAreaOutputStream extends OutputStream {
        private final JTextArea destination;

        public JTextAreaOutputStream(JTextArea destination) {
            if (destination == null) {
                throw new IllegalArgumentException("Destination is null");
            }

            this.destination = destination;
        }

        @Override
        public void write(byte[] buffer, int offset, int length) throws IOException {
            final String text = new String(buffer, offset, length);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    destination.append(text);
                }
            });
        }

        @Override
        public void write(int b) throws IOException {
            write(new byte[] {(byte) b}, 0, 1);
        }
    }

    public void makeNetworkPrediction() {
        FileDialog fd = new FileDialog(this, "Choose a picture", FileDialog.LOAD);
        fd.setFilenameFilter((dir, name) -> name.endsWith(".jnnpg"));
        fd.setVisible(true);
        if (fd.getFile() != null) {
            clearScreen();
            try {
                Vector2D pic = Vector2D.readFromFile(new FileInputStream(new File(fd.getDirectory() + fd.getFile())));

                double[] values = pic.toArray();
                for (int i = 0; i < values.length; ++i) {
                    values[i] /= 255.0;
                }
                int scale = 15;
                InputPicture picture = new InputPicture(values, scale);
                picture.setBounds(400, 200, 14 * scale + 1, 14 * scale + 1);
                mainPanel.add(picture);
                MovingAdapter movingAdapter = new MovingAdapter(picture);
                picture.addMouseListener(movingAdapter);
                picture.addMouseMotionListener(movingAdapter);

                Vector2D prediction = net.makePredictions(pic);
                System.out.println(prediction.getArgMax().getFirst());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void repaintNetwork() {
        int layersCount = net.getLayersCount();
        List<Integer> layerSizes = net.getLayerSizes();
        List<Layer> layers = new ArrayList<>(layersCount);

        double segmentWidth = mainPanel.getWidth() * 1.0 / layersCount;

        for (int i = 0; i < layersCount; ++i) {
            Layer layer = new Layer("Layer " + (i + 1), i, layerSizes.get(i), (i == 0 ? Layer.MARK_FIRST : i == layersCount - 1 ? Layer.MARK_LAST : Layer.MARK_REGULAR), this);
            layer.setBounds((int) (segmentWidth * i + (segmentWidth - Layer.WIDTH) / 2.0), 100, Layer.WIDTH + 1, Layer.HEIGHT + 1);
            mainPanel.add(layer);

            MovingAdapter movingAdapter = new MovingAdapter(layer);
            layer.addMouseListener(movingAdapter);
            layer.addMouseMotionListener(movingAdapter);

            layers.add(layer);
        }

        for (int i = 0; i < layerSizes.size() - 1; ++i) {
            LayersConnection connection = new LayersConnection(layers.get(i), layers.get(i + 1));
            connection.setBounds(0, 0, 10000, 10000);
            mainPanel.add(connection);
        }

        layersList.clear();
        for (int i = 0; i < layerSizes.size(); ++i) {
            layersList.add("Layer " + (i + 1) + ", " + layerSizes.get(i) + " neurons");
        }

        revalidate();
        repaint();
    }

    private void initNeuralNetwork() {
        net = new NeuralNetwork(3, new int[] {196, 300, 10}, ActivationFunction.SIGMOID);
    }

    private void loadNeuralNetwork(String fileName) {
        try {
            net = NeuralNetwork.readFromFile(new FileInputStream(fileName));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        clearScreen();
        repaintNetwork();
    }

    private void saveNeuralNetwork(String fileName) {
        try {
            net.saveToFile(new FileOutputStream(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addNetworkLayer() {
        int layerSize;
        try {
            layerSize = Integer.valueOf(JOptionPane.showInputDialog(this, "Specify layer size"));
        } catch (Exception exception) {
            return;
        }
        net.addLayer(layerSize);
        List<Integer> layerSizes = net.getLayerSizes();
        layersList.add("Layer " + layerSizes.size() + ", " + layerSizes.get(layerSizes.size() - 1) + " neurons");
    }

    public void removeNetworkLayer(int layerIndex) {
        if (layersList.items.size() > 1) {
            net.removeLayer(layerIndex);
            layersList.clear();
            List<Integer> layerSizes = net.getLayerSizes();
            for (int i = 0; i < layerSizes.size(); ++i) {
                layersList.add("Layer " + (i + 1) + ", " + layerSizes.get(i) + " neurons");
            }

            clearScreen();
            repaintNetwork();
        }
    }

    public static void main(String... args) throws Exception {
        new MainWindow("JNNet GUI");
    }
}