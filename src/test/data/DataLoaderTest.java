package data;

import com.vlfom.data.DataLoader;
import com.vlfom.utils.Pair;
import com.vlfom.utils.Vector2D;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by @vlfom.
 */
public class DataLoaderTest {

    @Test
    public void testLoadData() throws Exception {
        InputStream inputStream = new FileInputStream(new File("res/data/mnist/train.csv"));
        List<Pair<Vector2D>> data = DataLoader.loadData(inputStream, 784, 10, -1);

        assertEquals(70.0, data.get(0).getFirst().getVal(350));
        assertEquals(253.0, data.get(1).getFirst().getVal(300));
        assertEquals(254.0, data.get(2).getFirst().getVal(550));

        assertEquals(5, data.get(0).getSecond().getArgMax().getFirst().intValue());
        assertEquals(0, data.get(1).getSecond().getArgMax().getFirst().intValue());
        assertEquals(4, data.get(2).getSecond().getArgMax().getFirst().intValue());
    }
}
