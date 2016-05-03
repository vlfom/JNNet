import utils.Pair;
import utils.Vector2D;
import utils.VectorMath;

import java.util.ArrayList;

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

    public Pair<Vector2D[]> backPropagation(Vector2D x, Vector2D y) {
        Vector2D[] gradB = new Vector2D[numLayers - 1];
        Vector2D[] gradW = new Vector2D[numLayers - 1];
        Vector2D activation = x;
        Vector2D[] activations = new Vector2D[numLayers - 1];
        Vector2D z;
        Vector2D[] zs = new Vector2D[numLayers - 1];
        for (int i = 0; i < numLayers - 1; ++i) {
            z = weights[i].transpose().dot(activation).add(biases[i]);
            zs[i] = z;
            activation = ActivationFunction.sigmoid(z);
            activations[i] = activation;
        }
        Vector2D delta = calculateCostDerivative(activations[numLayers-2], y).mul(
                ActivationFunction.sigmoidDerivaive(zs[numLayers-2])
        );
        gradB[numLayers-2] = delta;
        gradW[numLayers-2] = delta.dot(activations[numLayers-2].transpose());
        for (int i = numLayers-3; i >= 1; --i) {
            z = zs[i];
            Vector2D sp = ActivationFunction.sigmoidDerivaive(z);
            delta = weights[i+1].dot(delta).mul(sp);
            gradB[i] = delta;
            gradW[i] = delta.dot(activations[i-1].transpose());
        }
        return new Pair<>(gradB, gradW);
    }

    public Vector2D calculateCostDerivative(Vector2D outActivations, Vector2D y) {
        return outActivations.sub(y);
    }

    public void launchSGD(Vector2D train_data, Vector2D test_data, int epochs, int batch_size, double eta) {

    }

}