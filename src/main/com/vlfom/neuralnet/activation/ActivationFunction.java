package com.vlfom.neuralnet.activation;

import com.vlfom.utils.Vector2D;
import com.vlfom.utils.VectorMath;

import java.io.Serializable;

/**
 * Created by @vlfom.
 */
public abstract class ActivationFunction implements Serializable {
    public static final ActivationFunction SIGMOID = new ActivationFunction() {
        @Override
        public Vector2D apply(Vector2D x) {
            return VectorMath.ones(x).div(
                    VectorMath.ones(x).add(VectorMath.exp(x.mul(-1f)))
            );
        }

        @Override
        public Vector2D calculateDerivative(Vector2D x) {
            Vector2D sigmoid = apply(x);
            return sigmoid.mul(VectorMath.ones(x).sub(sigmoid));
        }

    };
    public static final ActivationFunction TANH = new ActivationFunction() {
        @Override
        public Vector2D apply(Vector2D x) {
            Vector2D expPos = VectorMath.exp(x);
            Vector2D expNeg = VectorMath.exp(x.mul(-1));
            return expPos.sub(expNeg).div(expPos.add(expNeg));
        }

        @Override
        public Vector2D calculateDerivative(Vector2D x) {
            Vector2D tanh = apply(x);
            return VectorMath.ones(x).sub(tanh.mul(tanh));
        }
    };

    public abstract Vector2D apply(Vector2D x);
    public abstract Vector2D calculateDerivative(Vector2D x);
}
