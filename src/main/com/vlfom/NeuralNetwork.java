package com.vlfom;

import com.vlfom.metrics.Metrics;
import com.vlfom.utils.Pair;
import com.vlfom.utils.Vector2D;
import com.vlfom.utils.VectorMath;

import java.util.Collections;
import java.util.List;

/**
 * Created by @vlfom.
 */
public class NeuralNetwork {
    private int numLayers;
    private int[] layerSizes;
    public Vector2D[] biases;
    public Vector2D[] weights;

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

    public Vector2D feedforwardPropagation(Vector2D x) {
        for (int i = 0; i < numLayers - 1; ++i) {
            x = ActivationFunction.sigmoid(weights[i].transpose().dot(x).add(biases[i]));
        }
        return x;
    }

    public Pair<Vector2D[]> backPropagation(Vector2D x, Vector2D y) {
        Vector2D[] nablaB = new Vector2D[numLayers - 1];
        Vector2D[] nablaW = new Vector2D[numLayers - 1];
        Vector2D activation = x;
        Vector2D[] activations = new Vector2D[numLayers];
        Vector2D z;
        Vector2D[] zs = new Vector2D[numLayers - 1];
        activations[0] = x;
        for (int i = 0; i < numLayers - 1; ++i) {
            z = weights[i].transpose().dot(activation).add(biases[i]);
            zs[i] = z;
            activation = ActivationFunction.sigmoid(z);
            activations[i+1] = activation;
        }
        Vector2D delta = calculateCostDerivative(activations[numLayers-1], y).mul(
                ActivationFunction.sigmoidDerivaive(zs[numLayers-2])
        );
        nablaB[numLayers-2] = delta;
        nablaW[numLayers-2] = delta.dot(activations[numLayers-2].transpose()).transpose();
        for (int i = numLayers-3; i >= 0; --i) {
            z = zs[i];
            Vector2D sp = ActivationFunction.sigmoidDerivaive(z);
            delta = weights[i+1].dot(delta).mul(sp);
            nablaB[i] = delta;
            nablaW[i] = delta.dot(activations[i].transpose()).transpose();
        }
        return new Pair<>(nablaB, nablaW);
    }

    public void updateMiniBatch(List<Pair<Vector2D>> miniBatch, double learningRate) {
        Vector2D[] nablaB = new Vector2D[numLayers - 1];
        Vector2D[] nablaW = new Vector2D[numLayers - 1];
        for (int i = 0; i < numLayers - 1; ++i) {
            nablaB[i] = new Vector2D(layerSizes[i+1]);
            nablaW[i] = new Vector2D(layerSizes[i], layerSizes[i+1]);
        }
        Pair<Vector2D[]> deltaNablas;
        int batchSize = miniBatch.size();
        for (int i = 0; i < batchSize; ++i) {
            deltaNablas = backPropagation(miniBatch.get(i).getFirst(), miniBatch.get(i).getSecond());
            for (int j = 0; j < numLayers-1; ++j) {
                nablaB[j] = nablaB[j].add(deltaNablas.getFirst()[j]);
                nablaW[j] = nablaW[j].add(deltaNablas.getSecond()[j]);
            }
        }
        for (int i = 0; i < numLayers - 1; ++i) {
            biases[i] = biases[i].sub(nablaB[i].mul(learningRate/batchSize));
        }
        for (int i = 0; i < numLayers - 1; ++i) {
            weights[i] = weights[i].sub(nablaW[i].mul(learningRate/batchSize));
        }
    }

    public void launchSGD(List<Pair<Vector2D>> trainData, List<Pair<Vector2D>> testData, int epochsCount, int batchSize,
                          double learningRate, Metrics metrics) {
        for (int e = 0; e < epochsCount; ++e) {
            Collections.shuffle(trainData);
            for (int i = batchSize; i <= trainData.size(); i += batchSize) {
                updateMiniBatch(trainData.subList(i-batchSize, i), learningRate);
            }
            System.out.printf("Epoch: %d. Score: %.3f\n", e, metrics.evaluateScore(this, testData));
        }
    }

    public static Vector2D calculateCostDerivative(Vector2D outActivations, Vector2D y) {
        return outActivations.sub(y);
    }

}