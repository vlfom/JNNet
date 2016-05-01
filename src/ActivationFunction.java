import utils.Vector2D;
import utils.VectorMath;

/**
 * Created by @vlfom.
 */
public class ActivationFunction {
    public static Vector2D sigmoid(Vector2D x) {
        return VectorMath.ones(x.getL1(), x.getL2()).div(
                VectorMath.ones(x.getL1(), x.getL2()).add(
                        VectorMath.exp(x.mul(-1f))
                )
        );
    }
}
