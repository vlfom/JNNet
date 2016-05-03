import utils.Vector2D;
import utils.VectorMath;

/**
 * Created by @vlfom.
 */
public class ActivationFunction {
    public static Vector2D sigmoid(Vector2D x) {
        return VectorMath.ones(x).div(
                VectorMath.ones(x).add(VectorMath.exp(x.mul(-1f)))
        );
    }

    public static Vector2D sigmoidDerivaive(Vector2D x) {
        return x.mul(VectorMath.ones(x).sub(x));
    }
}
