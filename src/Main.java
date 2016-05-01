import utils.Vector2D;
import utils.VectorMath;

/**
 * Created by @vlfom.
 */
public class Main {
    public static void main(String... args) {
        Vector2D x = VectorMath.ones(3, 2);
        Vector2D y = VectorMath.ones(2, 4);
        System.out.println(x);
        System.out.println(y);
        System.out.println(x.mul(2).dot(y.div(2)));
        System.out.println(x);
        System.out.println(y);
        System.out.println();

        NeuralNetwork net = new NeuralNetwork(3, new int[]{3, 4, 5});
        net.feedForwardPropagation(new Vector2D(3));
    }
}
