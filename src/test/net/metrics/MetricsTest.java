package net.metrics;

import com.vlfom.data.DataGenerator;
import com.vlfom.neuralnet.NeuralNetwork;
import com.vlfom.neuralnet.activation.ActivationFunction;
import com.vlfom.neuralnet.metrics.Metrics;
import com.vlfom.utils.Pair;
import com.vlfom.utils.Vector2D;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by @vlfom.
 */
public class MetricsTest {

    @Test
    public void testClassificationF1_Score() throws Exception {
        NeuralNetwork net = new NeuralNetwork(4, new int[]{2, 10, 10, 2}, ActivationFunction.SIGMOID);

        ArrayList<Pair<Vector2D>> trainData = DataGenerator.generateData(1000, 0);
        ArrayList<Pair<Vector2D>> testData = DataGenerator.generateData(500, 0);

        net.launchSGD(trainData, testData, 50, 1, 0.01, Metrics.F1_SCORE, System.out);
    }

    @Test
    public void testRegressionMSE() throws Exception {
        NeuralNetwork net = new NeuralNetwork(4, new int[]{2, 10, 10, 1}, ActivationFunction.SIGMOID);

        ArrayList<Pair<Vector2D>> trainData = DataGenerator.generateData(10000, 1);
        ArrayList<Pair<Vector2D>> testData = DataGenerator.generateData(5000, 1);

        net.launchSGD(trainData, testData, 50, 100, 0.01, Metrics.MSE, System.out);
    }

    @Test
    public void testClassificationAccuracy() throws Exception {
        NeuralNetwork net = new NeuralNetwork(4, new int[]{2, 10, 10, 2}, ActivationFunction.SIGMOID);

        ArrayList<Pair<Vector2D>> trainData = DataGenerator.generateData(1000, 0);
        ArrayList<Pair<Vector2D>> testData = DataGenerator.generateData(500, 0);

        net.launchSGD(trainData, testData, 50, 1, 0.01, Metrics.ACCURACY, System.out);
    }
}
