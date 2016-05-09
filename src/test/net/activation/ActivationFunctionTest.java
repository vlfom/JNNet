package net.activation;

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
public class ActivationFunctionTest {

    @Test
    public void testSigmoid() throws Exception {
        NeuralNetwork net = new NeuralNetwork(4, new int[]{2, 10, 10, 2}, ActivationFunction.SIGMOID);

        ArrayList<Pair<Vector2D>> trainData = DataGenerator.generateData(1000, 0);
        ArrayList<Pair<Vector2D>> testData = DataGenerator.generateData(500, 0);

        net.launchSGD(trainData, testData, 50, 1, 0.01, Metrics.ACCURACY);
    }

    @Test
    public void testTanh() throws Exception {
        NeuralNetwork net = new NeuralNetwork(4, new int[]{2, 10, 10, 2}, ActivationFunction.TANH);

        ArrayList<Pair<Vector2D>> trainData = DataGenerator.generateData(1000, 0);
        ArrayList<Pair<Vector2D>> testData = DataGenerator.generateData(500, 0);

        net.launchSGD(trainData, testData, 50, 100, 0.01, Metrics.ACCURACY);
    }
}
