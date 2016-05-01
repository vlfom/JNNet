import utils.Vector2D;
import utils.VectorMath;

/**
 * Created by @vlfom.
 */
public class NeuralNetwork {
    private int numLayers;
    private int[] layerSizes;
    private Vector2D[] biases;
    private Vector2D[] weights;

    public NeuralNetwork(int numLayers, int[] layerSizes) {
        this.numLayers = numLayers;
        this.layerSizes = layerSizes;

        biases = new Vector2D[numLayers - 1];
        weights = new Vector2D[numLayers - 1];

        for (int i = 0; i < numLayers - 1; ++i) {
            biases[i] = VectorMath.gaussianRandom(layerSizes[i + 1], 1);
        }

        for (int i = 0; i < numLayers - 1; ++i) {
            weights[i] = VectorMath.gaussianRandom(layerSizes[i], layerSizes[i + 1]);
        }
    }

    public Vector2D feedForwardPropagation(Vector2D x) {
        for (int i = 0; i < numLayers - 1; ++i) {
            x = ActivationFunction.sigmoid(weights[i].transpose().dot(x).add(biases[i]));
        }
        return x;
    }

    public void launchSGD(Vector2D train_data, Vector2D test_data, int epochs, int batch_size, double eta) {

    }

}