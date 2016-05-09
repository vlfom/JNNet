package net;

import com.vlfom.data.DataCompressor;
import com.vlfom.data.DataLoader;
import com.vlfom.neuralnet.NeuralNetwork;
import com.vlfom.neuralnet.activation.ActivationFunction;
import com.vlfom.neuralnet.metrics.Metrics;
import org.junit.Test;
import com.vlfom.utils.Pair;
import com.vlfom.utils.Vector2D;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by @vlfom.
 */
public class NeuralNetworkTest {

    @Test
    public void testSaveLoad() throws Exception {
        InputStream inputStream = new FileInputStream(new File("res/data/mnist/train.csv"));
        List<Pair<Vector2D>> data = DataLoader.loadData(inputStream, 784, 10, 10000);
        DataCompressor.compressData(data, 28, 14);

        NeuralNetwork net = new NeuralNetwork(3, new int[] {196, 50, 10}, ActivationFunction.SIGMOID);

        List<Pair<Vector2D>> trainData = data;
        List<Pair<Vector2D>> testData = data;

        net.launchSGD(trainData, testData, 50, 100, 1, Metrics.ACCURACY);

        File netFile = new File("res/net/testNetwork.jnnet");
        net.saveToFile(new FileOutputStream(netFile));

        net = NeuralNetwork.readFromFile(new FileInputStream(netFile));
        System.out.println("Loaded network test accuracy: " + Metrics.ACCURACY.evaluateScore(net, testData));
    }

    @Test
    public void testMNISTNetwork() throws Exception {
        InputStream inputStream = new FileInputStream(new File("res/data/mnist/train.csv"));
        List<Pair<Vector2D>> data = DataLoader.loadData(inputStream, 784, 10, 1000);
        DataCompressor.compressData(data, 28, 14);

        NeuralNetwork net = new NeuralNetwork(3, new int[] {196, 50, 10}, ActivationFunction.SIGMOID);

        List<Pair<Vector2D>> trainData = data;
        List<Pair<Vector2D>> testData = data;

        net.launchSGD(trainData, testData, 100, 100, 1, Metrics.ACCURACY);
    }

}
