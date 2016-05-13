package com.vlfom.neuralnet;

import com.sun.corba.se.impl.io.OutputStreamHook;
import com.sun.corba.se.impl.orbutil.ObjectWriter;
import com.vlfom.neuralnet.activation.ActivationFunction;
import com.vlfom.neuralnet.metrics.Metrics;
import com.vlfom.utils.Pair;
import com.vlfom.utils.Vector2D;
import com.vlfom.utils.VectorMath;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by @vlfom.
 */
public class NeuralNetwork implements Serializable {
    private int numLayers;
    private List<Integer> layerSizes;
    private List<Vector2D> biases;
    private List<Vector2D> weights;
    private ActivationFunction activFunc;

    private static ArrayList<Integer> toArrayList(int[] array) {
        ArrayList<Integer> arrayList = new ArrayList<>(array.length);
        for (int value : array) {
            arrayList.add(value);
        }
        return arrayList;
    }

    public NeuralNetwork(ActivationFunction activFunc) {
        numLayers = 1;
        layerSizes = new ArrayList<>();
        layerSizes.add(0);
        biases = new ArrayList<>();
        weights = new ArrayList<>();
        this.activFunc = activFunc;
    }

    public NeuralNetwork(int numLayers, int[] layerSizes, ActivationFunction activFunc) {
        this.numLayers = numLayers;
        this.layerSizes = toArrayList(layerSizes);
        this.activFunc = activFunc;

        biases = new ArrayList<>(numLayers - 1);
        weights = new ArrayList<>(numLayers - 1);

        for (int i = 0; i < numLayers - 1; ++i) {
            biases.add(VectorMath.gaussianRandom(layerSizes[i + 1], 1));
            weights.add(VectorMath.gaussianRandom(layerSizes[i], layerSizes[i + 1]));
        }
    }

    public int getLayersCount() {
        return numLayers;
    }

    public void addLayer(int layerSize) {
        numLayers += 1;
        layerSizes.add(layerSize);
        biases.add(VectorMath.gaussianRandom(layerSize, 1));
        weights.add(VectorMath.gaussianRandom(layerSizes.get(numLayers - 2), layerSize));
    }

    public void removeLayer(int ind) {
        if (numLayers == 1 || ind < 0 || ind >= numLayers) {
            return;
        }
        layerSizes.remove(ind);
        numLayers -= 1;

        biases = new ArrayList<>(numLayers - 1);
        weights = new ArrayList<>(numLayers - 1);

        for (int i = 0; i < numLayers - 1; ++i) {
            biases.add(VectorMath.gaussianRandom(layerSizes.get(i + 1), 1));
            weights.add(VectorMath.gaussianRandom(layerSizes.get(i), layerSizes.get(i + 1)));
        }
    }

    public List<Integer> getLayerSizes() {
        return layerSizes;
    }

    private Vector2D feedforwardPropagation(Vector2D x) {
        for (int i = 0; i < numLayers - 1; ++i) {
            x = activFunc.apply(weights.get(i).transpose().dot(x).add(biases.get(i)));
        }
        return x;
    }

    public Vector2D makePredictions(Vector2D x) {
        return feedforwardPropagation(x);
    }

    private Pair<Vector2D[]> backPropagation(Vector2D x, Vector2D y) {
        Vector2D[] nablaB = new Vector2D[numLayers - 1];
        Vector2D[] nablaW = new Vector2D[numLayers - 1];
        Vector2D activation = x;
        Vector2D[] activations = new Vector2D[numLayers];
        Vector2D z;
        Vector2D[] zs = new Vector2D[numLayers - 1];
        activations[0] = x;
        for (int i = 0; i < numLayers - 1; ++i) {
            z = weights.get(i).transpose().dot(activation).add(biases.get(i));
            zs[i] = z;
            activation = activFunc.apply(z);
            activations[i + 1] = activation;
        }
        Vector2D delta = calculateCostDerivative(activations[numLayers - 1], y).mul(activFunc.calculateDerivative(zs[numLayers - 2]));
        nablaB[numLayers - 2] = delta;
        nablaW[numLayers - 2] = delta.dot(activations[numLayers - 2].transpose()).transpose();
        for (int i = numLayers - 3; i >= 0; --i) {
            z = zs[i];
            Vector2D sp = activFunc.calculateDerivative(z);
            delta = weights.get(i + 1).dot(delta).mul(sp);
            nablaB[i] = delta;
            nablaW[i] = delta.dot(activations[i].transpose()).transpose();
        }
        return new Pair<>(nablaB, nablaW);
    }

    private void updateMiniBatch(List<Pair<Vector2D>> miniBatch, double learningRate) {
        Vector2D[] nablaB = new Vector2D[numLayers - 1];
        Vector2D[] nablaW = new Vector2D[numLayers - 1];
        for (int i = 0; i < numLayers - 1; ++i) {
            nablaB[i] = new Vector2D(layerSizes.get(i + 1));
            nablaW[i] = new Vector2D(layerSizes.get(i), layerSizes.get(i + 1));
        }
        Pair<Vector2D[]> deltaNablas;
        int batchSize = miniBatch.size();
        for (Pair<Vector2D> aMiniBatch : miniBatch) {
            deltaNablas = backPropagation(aMiniBatch.getFirst(), aMiniBatch.getSecond());
            for (int j = 0; j < numLayers - 1; ++j) {
                nablaB[j] = nablaB[j].add(deltaNablas.getFirst()[j]);
                nablaW[j] = nablaW[j].add(deltaNablas.getSecond()[j]);
            }
        }
        for (int i = 0; i < numLayers - 1; ++i) {
            biases.set(i, biases.get(i).sub(nablaB[i].mul(learningRate / batchSize)));
            weights.set(i, weights.get(i).sub(nablaW[i].mul(learningRate / batchSize)));
        }
    }

    private static Vector2D calculateCostDerivative(Vector2D outActivations, Vector2D y) {
        return outActivations.sub(y);
    }

    public void launchSGD(List<Pair<Vector2D>> trainData, List<Pair<Vector2D>> testData, int epochsCount, int batchSize, double learningRate, Metrics metrics, PrintStream printStream) {
        for (int e = 0; e < epochsCount; ++e) {
            Collections.shuffle(trainData);
            for (int i = batchSize; i <= trainData.size(); i += batchSize) {
                updateMiniBatch(trainData.subList(i - batchSize, i), learningRate);
            }
            printStream.printf("Finished epoch:  %d.      " + metrics.getName() + " :  %.3f.\n", e + 1, metrics.evaluateScore(this, testData));
        }
    }

    public void saveToFile(OutputStream outputStream) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(this);
        objectOutputStream.close();
    }

    public static NeuralNetwork readFromFile(InputStream inputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        NeuralNetwork network = (NeuralNetwork) objectInputStream.readObject();
        objectInputStream.close();
        return network;
    }

}