import com.vlfom.NeuralNetwork;
import com.vlfom.metrics.Metrics;
import org.junit.Test;
import com.vlfom.utils.Pair;
import com.vlfom.utils.Vector2D;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by @vlfom.
 */
public class NeuralNetworkTest {

    private ArrayList<Pair<Vector2D>> generateData(int dataSize, int dataType) {
        ArrayList<Pair<Vector2D>> data = new ArrayList<>();
        Random random = new Random(1346);
        double a, b, c, d;
        while (dataSize-- > 0) {
            a = random.nextDouble();
            b = random.nextDouble();
            if (dataType == 0) {
                if (a > b) {
                    c = 1;
                    d = 0;
                } else {
                    c = 0;
                    d = 1;
                }
                data.add(new Pair<>(new Vector2D(new double[] {a, b}), new Vector2D(new double[] {c, d})));
            }
            else if (dataType == 1){
                data.add(new Pair<>(
                        new Vector2D(new double[] {a, b}),
                        new Vector2D(new double[] {
                                Math.pow(a * b, b) + Math.sin(a) * Math.cos(b) +
                                        Math.exp(a) * Math.acos(b) + Math.random()
                        })
                ));
            }
        }
        return data;
    }

    @Test
    public void testClassificationAccuracy() throws Exception {
        NeuralNetwork net = new NeuralNetwork(4, new int[]{2, 10, 10, 2});

        ArrayList<Pair<Vector2D>> trainData = generateData(1000, 0);
        ArrayList<Pair<Vector2D>> testData = generateData(500, 0);

        net.launchSGD(trainData, testData, 50, 1, 0.01, Metrics.ACCURACY);
    }

    @Test
    public void testClassificationF1_Score() throws Exception {
        NeuralNetwork net = new NeuralNetwork(4, new int[]{2, 10, 10, 2});

        ArrayList<Pair<Vector2D>> trainData = generateData(1000, 0);
        ArrayList<Pair<Vector2D>> testData = generateData(500, 0);

        net.launchSGD(trainData, testData, 50, 1, 0.01, Metrics.F1_SCORE);
    }

    @Test
    public void testRegressionMSE() throws Exception {
        NeuralNetwork net = new NeuralNetwork(4, new int[]{2, 10, 10, 1});

        ArrayList<Pair<Vector2D>> trainData = generateData(10000, 1);
        ArrayList<Pair<Vector2D>> testData = generateData(5000, 1);

        net.launchSGD(trainData, testData, 50, 100, 0.01, Metrics.MSE);
    }

}
