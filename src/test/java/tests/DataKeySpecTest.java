// This test ensures that com.amazonaws.services.kms.model.DataKeySpec only has
// two enum values: AES_256 and AES_128. If not, then this package needs to be
// updated:
// 1. New tests should be added similar to the test data in tests/kms/EnumCheck.java for AES_256 and AES_128.
// 2. The spec in src/main/java/com/amazon/checkerframework/compliance/kms/GenerateDataKeyRequest.astub needs to be modified.
// 3. This test should be updated.

import com.amazonaws.services.kms.model.DataKeySpec;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class DataKeySpecTest {
    @Test
    public void ensureDataKeySpecOnlyHasTwoValues() {
        List<DataKeySpec> values = Arrays.asList(DataKeySpec.values());
        Assert.assertEquals("Expected that com.amazonaws.services.kms.model.DataKeySpec only has 2 elements.",
                2, values.size());
        Assert.assertTrue("Expected that com.amazonaws.services.kms.model.DataKeySpec should have a member called AES_256.",
                values.contains(DataKeySpec.AES_256));
        Assert.assertTrue("Expected that com.amazonaws.services.kms.model.DataKeySpec should have a member called AES_128.",
                values.contains(DataKeySpec.AES_128));
    }
}
