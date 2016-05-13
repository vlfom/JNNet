package com.vlfom.graphics.window;

/**
 * Created by @vlfom.
 */

import com.vlfom.data.DataCompressor;
import com.vlfom.data.DataLoader;
import com.vlfom.graphics.button.ClearButton;
import com.vlfom.graphics.label.StyledLabel;
import com.vlfom.graphics.button.ResumeButton;
import com.vlfom.graphics.button.StopButton;
import com.vlfom.graphics.img.VectorPicture;
import com.vlfom.graphics.list.StyledList;
import com.vlfom.graphics.mouse.RequestFocusListener;
import com.vlfom.graphics.net.Layer;
import com.vlfom.graphics.net.LayersConnection;
import com.vlfom.graphics.textarea.TextAreaOutputStream;
import com.vlfom.neuralnet.NeuralNetwork;
import com.vlfom.neuralnet.activation.ActivationFunction;
import com.vlfom.neuralnet.metrics.Metrics;
import com.vlfom.utils.Pair;
import com.vlfom.utils.Vector2D;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

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
    private JMenuItem showNetwork;
    private JMenuItem showDataset;
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

    private boolean dataLoaded;

    private void markDataLoaded() {
        launchSGDItem.setEnabled(true);
        makePredictionItem.setEnabled(true);
        showDataset.setEnabled(true);
        dataLoaded = true;
    }

    private void markDataNotLoaded() {
        launchSGDItem.setEnabled(false);
        makePredictionItem.setEnabled(false);
        showDataset.setEnabled(false);
        dataLoaded = false;
    }

    private void markNetworkLoaded() {
        addLayerItem.setEnabled(true);
        loadDataItem.setEnabled(true);
        saveItem.setEnabled(true);
        closeItem.setEnabled(true);
        showNetwork.setEnabled(true);
    }

    private void markNetworkNotLoaded() {
        addLayerItem.setEnabled(false);
        launchSGDItem.setEnabled(false);
        makePredictionItem.setEnabled(false);
        showNetwork.setEnabled(false);
        saveItem.setEnabled(false);
        closeItem.setEnabled(false);
    }

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
        closeItem.setEnabled(false);
        closeItem.addActionListener(e -> {
            clearScreen();
            layersList.clear();

            markNetworkNotLoaded();

            revalidate();
            repaint();
        });
        fileMenu.add(closeItem);

        fileMenu.addSeparator();

        exitItem = new JMenuItem("Exit");
        exitItem.setFont(font);
        fileMenu.add(exitItem);

        exitItem.addActionListener(e -> System.exit(0));

        viewMenu = new JMenu("View");
        viewMenu.setFont(font);
        viewMenu.setPreferredSize(new Dimension(50, 25));

        showNetwork = new JMenuItem("Display network");
        showNetwork.setFont(font);
        showNetwork.setPreferredSize(new Dimension(160, 25));
        showNetwork.addActionListener(e -> {
            clearScreen();
            repaintNetwork();
        });
        viewMenu.add(showNetwork);

        showDataset = new JMenuItem("Display dataset");
        showDataset.setFont(font);
        showDataset.addActionListener(e -> {
            try {
                clearScreen();
                paintImages();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        viewMenu.add(showDataset);

        constructorMenu = new JMenu("Constructor");
        constructorMenu.setFont(font);
        constructorMenu.setPreferredSize(new Dimension(100, 25));

        newNetworkItem = new JMenuItem("New network...");
        newNetworkItem.setFont(font);
        newNetworkItem.addActionListener(e -> {
            clearScreen();
            initNeuralNetwork();
            repaintNetwork();

            markNetworkLoaded();
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
        loadDataItem.addActionListener(e -> {
            loadDataset();
        });
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
            JOptionPane.showMessageDialog(null, "JNNet - Java Neural Net library\nVolodymyr Fomenko IP-42", "Info", JOptionPane.INFORMATION_MESSAGE);
        });
        helpMenu.add(aboutItem);

        markNetworkNotLoaded();
        markDataNotLoaded();

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(constructorMenu);
        menuBar.add(dataMenu);
        menuBar.add(netMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }

    private void paintImages() throws Exception {
        Random random = new Random();

        //data.get(0).getFirst().saveToFile(new FileOutputStream(new File("res/img/pic0.jnnpg")));

        for (int pictureID = 0; pictureID < 32; ++pictureID) {
            double[] values = data.get(random.nextInt(data.size())).getFirst().toArray();
            for (int i = 0; i < values.length; ++i) {
                values[i] /= 255.0;
            }

            int x = pictureID % 8;
            int y = pictureID / 8;

            VectorPicture picture = new VectorPicture(values, 5, this);
            int scale = 100;
            picture.setBounds(270 + scale * x, 90 + scale * y, scale, scale);
            mainPanel.add(picture);
        }

        revalidate();
        repaint();
    }

    private StyledList layersList;
    private StyledList dataInfoList;

    private void addLeftPanel() {
        leftPanel = new JPanel(null);

        leftPanel.setBounds(0, 0, 250, 600);
        leftPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 1, new Color(160, 160, 160)));
        leftPanel.setBackground(new Color(255, 255, 255));

        layersList = new StyledList(0, 20, 249, true, this);
        StyledLabel netBrowserLabel = new StyledLabel(
                "Network browser", 9, 0, 250, 18, new Color(232, 232, 232), new Color(160, 160, 160), 0, 0, 1, 1
        );
        leftPanel.add(netBrowserLabel);

        StyledLabel dataInfoLabel = new StyledLabel(
                "Dataset information", 9, 460, 250, 18, new Color(232, 232, 232), new Color(160, 160, 160), 1, 0, 1, 1
        );
        leftPanel.add(dataInfoLabel);

        StyledList dataList = new StyledList(0, 480, 130, false, this);
        dataList.add("Dataset name");
        dataList.add("Dataset size");
        dataList.add("Train size");
        dataList.add("Test size");
        dataList.add("Features count");
        dataList.add("Targets count");
        add(dataList);

        dataInfoList = new StyledList(130, 480, 119, false, this);
        dataInfoList.add("None");
        dataInfoList.add("None");
        dataInfoList.add("None");
        dataInfoList.add("None");
        dataInfoList.add("None");
        dataInfoList.add("None");
        add(dataInfoList);

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

    private JTextArea logsArea;

    public void changeNetworkLayerSize(int layerID) {
        int layerSize;
        try {
            layerSize = Integer.valueOf(JOptionPane.showInputDialog(this, "Enter layer size:"));
        } catch (Exception exception) {
            return;
        }
        List<Integer> layerSizes = net.getLayerSizes();
        layerSizes.set(layerID, layerSize);
        int[] primLayerSizes = new int[layerSizes.size()];
        for (int i = 0; i < layerSizes.size(); ++i) {
            primLayerSizes[i] = layerSizes.get(i);
        }

        net = new NeuralNetwork(layerSizes.size(), primLayerSizes, ActivationFunction.SIGMOID);

        clearScreen();
        repaintNetwork();
    }

    private void addBottomPanel() {
        bottomPanel = new JPanel(null);

        bottomPanel.setBounds(0, 600, 1600, 300);
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, new Color(160, 160, 160)));
        bottomPanel.setBackground(new Color(255, 255, 255));

        bottomPanel.add(new StyledLabel("Logs", 9, 0, 1600, 18, new Color(232, 232, 232), new Color(160, 160, 160), 1, 0, 1, 0));

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
                if (net == null || sgdThread == null || !sgdFinished)
                    return;

                sgdThread.stop();
                sgdFinished = true;
                logsArea.append("\nInterrupting...\n\n");
            }
        });

        JPanel clearButton = new ClearButton(null);
        clearButton.setBounds(3, 43, 18, 18);
        leftSubPanel.add(clearButton);

        clearButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                logsArea.setText("");
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
    }

    private Thread sgdThread;
    private boolean sgdFinished;

    private Thread dataThread;

    private void loadDataset() {
        FileDialog fd = new FileDialog(this, "Choose dataset...", FileDialog.LOAD);
        fd.setDirectory("res/data");
        fd.setFilenameFilter((dir, name) -> name.endsWith(".jnndata"));
        fd.setVisible(true);
        if (fd.getFile() != null) {
            dataThread = new Thread() {
                @Override
                public void run() {
                    logsArea.append("Loading dataset...  ");
                    loadDataItem.setEnabled(false);
                    try {
                        Scanner sc = new Scanner(new FileInputStream(new File(fd.getDirectory() + fd.getFile())));
                        sc.useDelimiter("\n");
                        String datasetName = sc.next();
                        String datasetSize = sc.next();
                        String trainSize = sc.next();
                        String testSize = sc.next();
                        String featuresCount = sc.next();
                        String targetsCount = sc.next();
                        sc.close();
                        data = DataLoader.loadData(new FileInputStream(new File(fd.getDirectory() + fd.getFile())), 784, 10, 60000);
                        logsArea.append("compressing dataset...  ");
                        DataCompressor.compressData(data, 28, 14);
                        loadDataItem.setEnabled(true);
                        logsArea.append("finished.\n\n");
                        dataInfoList.clear();
                        dataInfoList.add(datasetName);
                        dataInfoList.add(datasetSize);
                        dataInfoList.add(trainSize);
                        dataInfoList.add(testSize);
                        dataInfoList.add(featuresCount);
                        dataInfoList.add(targetsCount);
                        markDataLoaded();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            dataThread.start();
        }
    }

    private List<Pair<Vector2D>> data;

    public void launchSGD() {
        if (net == null || !dataLoaded)
            return;

        if (sgdThread != null && !sgdFinished) {
            JOptionPane.showMessageDialog(
                    null,
                    "Can't start new learning process before the previous one is running",
                    "Info", JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        sgdFinished = false;

        int numIterations;
        double learningRate;
        int batchSize;
        Metrics metrics;
        try {
            JPanel p = new JPanel(new BorderLayout(5,5));

            JPanel labels = new JPanel(new GridLayout(0,1,2,2));
            labels.add(new JLabel("Number of iterations:", SwingConstants.RIGHT));
            labels.add(new JLabel("Learning rate:", SwingConstants.RIGHT));
            labels.add(new JLabel("Batch size:", SwingConstants.RIGHT));
            labels.add(new JLabel("Metrics:", SwingConstants.RIGHT));
            p.add(labels, BorderLayout.WEST);

            JPanel controls = new JPanel(new GridLayout(0,1,2,2));

            JTextField numIterationsField = new JTextField("20");
            numIterationsField.setPreferredSize(new Dimension(140, 20));
            controls.add(numIterationsField);

            JTextField learningRateField = new JTextField("1");
            learningRateField.addAncestorListener(new RequestFocusListener(false));
            controls.add(learningRateField);

            JTextField batchSizeField = new JTextField("128");
            batchSizeField.addAncestorListener(new RequestFocusListener(false));
            controls.add(batchSizeField);

            String[] metricsList = { "Accuracy", "MSE" };

            JComboBox metricsBox = new JComboBox(metricsList);
            metricsBox.setSelectedIndex(0);
            metricsBox.setLayout(null);
            metricsBox.setFont(new Font("Monospace", Font.PLAIN, 14));
            controls.add(metricsBox);

            p.add(controls, BorderLayout.CENTER);

            JOptionPane.showMessageDialog(
                    this, p, "SGD training", JOptionPane.PLAIN_MESSAGE);

            numIterations = Integer.valueOf(numIterationsField.getText());
            learningRate = Double.valueOf(learningRateField.getText());
            batchSize = Integer.valueOf(batchSizeField.getText());
            if (metricsBox.getSelectedIndex() == 0)
                metrics = Metrics.ACCURACY;
            else
                metrics = Metrics.MSE;
        } catch (Exception exception) {
            return;
        }
        sgdThread = new Thread() {
            public void run() {
                PrintStream logStream = new PrintStream(new TextAreaOutputStream(logsArea));
                try {
                    logStream.printf(
                            "Launching stochastic gradient descent with parameters: " +
                                    "(%d iterations, %.2f learning rate, %d batch size) on MNIST dataset...\n",
                            numIterations, learningRate, batchSize);

                    data.get(0).getFirst().saveAsImageToFile(new File("res/img/numbers/pic0.jpg"), 14, 14);
                    data.get(1).getFirst().saveAsImageToFile(new File("res/img/numbers/pic1.jpg"), 14, 14);
                    data.get(2).getFirst().saveAsImageToFile(new File("res/img/numbers/pic2.jpg"), 14, 14);
                    data.get(3).getFirst().saveAsImageToFile(new File("res/img/numbers/pic3.jpg"), 14, 14);
                    data.get(4).getFirst().saveAsImageToFile(new File("res/img/numbers/pic4.jpg"), 14, 14);
                    data.get(5).getFirst().saveAsImageToFile(new File("res/img/numbers/pic5.jpg"), 14, 14);
                    data.get(6).getFirst().saveAsImageToFile(new File("res/img/numbers/pic6.jpg"), 14, 14);
                    data.get(7).getFirst().saveAsImageToFile(new File("res/img/numbers/pic7.jpg"), 14, 14);
                    data.get(8).getFirst().saveAsImageToFile(new File("res/img/numbers/pic8.jpg"), 14, 14);
                    data.get(9).getFirst().saveAsImageToFile(new File("res/img/numbers/pic9.jpg"), 14, 14);
                    data.get(10).getFirst().saveAsImageToFile(new File("res/img/numbers/pic10.jpg"), 14, 14);

                    List<Pair<Vector2D>> trainData = data.subList(0, 800);
                    List<Pair<Vector2D>> testData = data.subList(800, 1000);

                    net.launchSGD(trainData, testData, numIterations, batchSize, learningRate, metrics, logStream);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                logStream.print("\nFinished training.\n");
                logStream.println();
                logStream.close();

                sgdFinished = true;
            }
        };

        sgdThread.start();
    }

    public void makeNetworkPrediction() {
        FileDialog fd = new FileDialog(this, "Choose a picture", FileDialog.LOAD);
        fd.setDirectory("res/img/jpg");
        fd.setFilenameFilter((dir, name) -> name.endsWith(".jpg"));
        fd.setVisible(true);
        if (fd.getFile() != null) {
            clearScreen();
            try {
                Vector2D pic = Vector2D.readFromImage(new File(fd.getDirectory() + fd.getFile()));

                double[] values = pic.toArray();

                for (int i = 0; i < values.length; ++i) {
                    values[i] /= 255.0;
                }
                int scale = 15;
                VectorPicture picture = new VectorPicture(values, scale, this);
                picture.setBounds(400, 200, 14 * scale + 1, 14 * scale + 1);

                mainPanel.add(picture);

                Vector2D prediction = net.makePredictions(pic);
                System.out.println(prediction.getArgMax().getFirst());
            } catch (Exception e) {
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

        markNetworkLoaded();
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
            layerSize = Integer.valueOf(JOptionPane.showInputDialog(this, "Enter layer size:"));
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