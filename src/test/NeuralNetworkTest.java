import org.junit.Test;
import utils.Pair;
import utils.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by @vlfom.
 */
public class NeuralNetworkTest {

    private ArrayList<Pair<Vector2D>> generateData(int dataSize) {
        ArrayList<Pair<Vector2D>> data = new ArrayList<>();
        Random random = new Random(1346);
        double a, b, c, d;
        while (dataSize-- > 0) {
            a = random.nextDouble();
            b = random.nextDouble();
            if (a > b) {
                c = 1; d = 0;
            }
            else {
                c = 0; d = 1;
            }
            data.add(new Pair<> (new Vector2D(new double[] {a, b}), new Vector2D(new double[] {c, d})));
        }
        return data;
    }

    @Test
    public void testConstructors() throws Exception {
        NeuralNetwork net = new NeuralNetwork(4, new int[]{2, 10, 10, 2});

        ArrayList<Pair<Vector2D>> trainData = generateData(1000);
        ArrayList<Pair<Vector2D>> testData = generateData(500);

        net.launchSGD(trainData, testData, 50, 1, 0.01);
    }

}
