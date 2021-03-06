package com.vlfom.neuralnet.metrics;

import com.vlfom.neuralnet.NeuralNetwork;
import com.vlfom.utils.Pair;
import com.vlfom.utils.Vector2D;

import java.io.Serializable;
import java.util.List;

/**
 * Created by @vlfom.
 */
public abstract class Metrics implements Serializable {
    public final static Metrics ACCURACY = new Metrics() {
        @Override
        public double evaluateScore(NeuralNetwork net, List<Pair<Vector2D>> testData) {
            Vector2D caseResult;
            int score = 0;
            for (Pair<Vector2D> testCase : testData) {
                caseResult = net.makePredictions(testCase.getFirst());
                if (caseResult.getArgMax().equals(testCase.getSecond().getArgMax())) {
                    score += 1;
                }
            }
            return score * 1.0 / testData.size();
        }

        @Override
        public String getName() {
            return "Accuracy";
        }
    };

    public final static Metrics F1_SCORE = new Metrics() {
        @Override
        public double evaluateScore(NeuralNetwork net, List<Pair<Vector2D>> testData) {
            Vector2D caseResult;
            int testSize = testData.size();
            int positiveExamplesSize = 0;
            double precision = 0;
            double recall = 0;
            for (Pair<Vector2D> testCase : testData) {
                caseResult = net.makePredictions(testCase.getFirst());
                if (testCase.getSecond().getArgMax().getFirst() == 1) {
                    positiveExamplesSize += 1;
                    if (caseResult.getArgMax().equals(testCase.getSecond().getArgMax())) {
                        recall += 1;
                    }
                }
                if (caseResult.getArgMax().equals(testCase.getSecond().getArgMax())) {
                    precision += 1;
                }
            }
            precision /= testSize;
            recall /= positiveExamplesSize;
            return 2 * precision * recall / (precision + recall);
        }

        @Override
        public String getName() {
            return "F1 Score";
        }
    };

    public final static Metrics MSE = new Metrics() {
        @Override
        public double evaluateScore(NeuralNetwork net, List<Pair<Vector2D>> testData) {
            Vector2D caseResult;
            int len = testData.get(0).getSecond().getL1();
            double score = 0;
            for (Pair<Vector2D> testCase : testData) {
                caseResult = net.makePredictions(testCase.getFirst());
                for (int i = 0; i < len; ++i) {
                    score += Math.pow(caseResult.getVal(i) - testCase.getSecond().getVal(i), 2);
                }
            }
            return score * 1.0 / (len * testData.size());
        }

        @Override
        public String getName() {
            return "Mean Squared Error";
        }
    };

    public abstract double evaluateScore(NeuralNetwork net, List<Pair<Vector2D>> testData);

    public abstract String getName();
}
