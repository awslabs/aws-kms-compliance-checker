// This is test data for the KMS compliance checker. This test is intended to check that the type system is working
// correctly in basic cases.

import org.checkerframework.common.value.qual.*;

import com.amazonaws.services.kms.model.DataKeySpec;
import com.amazonaws.services.kms.model.GenerateDataKeyRequest;

class Basics {
    void goodInt() {
        GenerateDataKeyRequest request = new GenerateDataKeyRequest().withNumberOfBytes(32);
        request.setNumberOfBytes(32);
    }

    void badInt() {
        // :: error: argument.type.incompatible
        GenerateDataKeyRequest request = new GenerateDataKeyRequest().withNumberOfBytes(16);
        // :: error: argument.type.incompatible
        request.setNumberOfBytes(16);
    }

    void goodString() {
        GenerateDataKeyRequest request = new GenerateDataKeyRequest().withKeySpec("AES_256");
        request.setKeySpec("AES_256");
    }

    void badString() {
        // :: error: argument.type.incompatible
        GenerateDataKeyRequest request = new GenerateDataKeyRequest().withKeySpec("AES_128");
        // :: error: argument.type.incompatible
        request.setKeySpec("AES_128");
    }

    void goodEnum() {
        GenerateDataKeyRequest request = new GenerateDataKeyRequest().withKeySpec(DataKeySpec.AES_256);
        request.setKeySpec(DataKeySpec.AES_256);
    }

    void badEnum() {
        // :: error: argument.type.incompatible
        GenerateDataKeyRequest request = new GenerateDataKeyRequest().withKeySpec(DataKeySpec.AES_128);
        // :: error: argument.type.incompatible
        request.setKeySpec(DataKeySpec.AES_128);
    }
}