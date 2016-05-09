package com.vlfom.graphics;

/**
 * Created by @vlfom.
 */
import com.vlfom.data.DataCompressor;
import com.vlfom.data.DataLoader;
import com.vlfom.graphics.img.GridImage;
import com.vlfom.neuralnet.NeuralNetwork;
import com.vlfom.neuralnet.activation.ActivationFunction;
import com.vlfom.utils.Pair;
import com.vlfom.utils.Vector2D;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private void clearScreen() {
        getContentPane().removeAll();
        repaint();
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        Font font = new Font("Verdana", Font.PLAIN, 11);

        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(font);

        JMenu newMenu = new JMenu("New");
        newMenu.setFont(font);
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

        JMenuItem paintMenu = new JMenuItem("Paint");
        paintMenu.setFont(font);
        viewMenu.add(paintMenu);
        paintMenu.addActionListener(e -> {
            try {
                paintImages(this);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        JMenu constructorMenu = new JMenu("Constructor");
        constructorMenu.setFont(font);

        JMenuItem newNetwork = new JMenuItem("New network...");
        newNetwork.setFont(font);
        constructorMenu.add(newNetwork);

        newNetwork.addActionListener(e -> {
            clearScreen();
            clearScreen();
        });

        JMenuItem addLayer = new JMenuItem("Add layer");
        addLayer.setFont(font);
        constructorMenu.add(addLayer);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(constructorMenu);
        return menuBar;
    }

    private void paintImages(JFrame window) throws Exception {
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
            window.add(picture);
        }

        window.add(new JComponent() {
        });

        repaint();
    }

    public MainWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1600, 900));
        setJMenuBar(createMenuBar());
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void initNeuralNetwork() {
        NeuralNetwork net = new NeuralNetwork(ActivationFunction.SIGMOID);

    }

    public static void main(String... args) throws Exception {
        new MainWindow();
    }
}