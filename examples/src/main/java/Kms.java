import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.kms.model.GenerateDataKeyRequest;
import com.amazonaws.services.kms.model.GenerateDataKeyResult;
import javax.crypto.spec.SecretKeySpec;

class Kms {
    private final Integer kmsBits = 128;
    private final String algorithm = "AES";

    SecretKeySpec getKeySpec() {
        AWSKMS kmsClient = AWSKMSClientBuilder.defaultClient();
        String keySpec = algorithm + "_" + kmsBits;
        final GenerateDataKeyRequest request = new GenerateDataKeyRequest().withKeySpec(keySpec);
        final GenerateDataKeyResult response = kmsClient.generateDataKey(request);
        return new SecretKeySpec(response.getPlaintext().array(), algorithm);
    }
}
