package data;

import com.vlfom.data.DataCompressor;
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
public class DataCompressorTest {

    @Test
    public void testCompressData() throws Exception {
        InputStream inputStream = new FileInputStream(new File("res/data/mnist/train.csv"));
        List<Pair<Vector2D>> data = DataLoader.loadData(inputStream, 784, 10, -1);
        DataCompressor.compressData(data, 28, 14);

        assertEquals(74.5, data.get(0).getFirst().getVal(53));
        assertEquals(84.5, data.get(1).getFirst().getVal(48));
        assertEquals(165.75, data.get(2).getFirst().getVal(52));
    }
}
