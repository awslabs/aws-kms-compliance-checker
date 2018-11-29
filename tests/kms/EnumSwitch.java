// This is test data for the KMSComplianceChecker. This test makes sure that the checker
// correctly handles switch statements involving DataKeySpec enums, because enum switch
// produces nightmarishly complicated bytecode.

import org.checkerframework.common.value.qual.*;

import com.amazonaws.services.kms.model.DataKeySpec;
import com.amazonaws.services.kms.model.GenerateDataKeyRequest;

class EnumSwitch {

    public DataKeySpec unknownKeySpec;

    public void test() {
        switch (unknownKeySpec) {
            case AES_128:
                // :: error: argument.type.incompatible
                GenerateDataKeyRequest request1 = new GenerateDataKeyRequest().withKeySpec(unknownKeySpec);
                break;
            case AES_256:
                // In theory, this ought to be sufficient to prove that the call below is safe. The typechecker,
                // however, conservatively estimates that unknownKeySpec could be anything. I think it's not refined
                // because switch statements on enums are rough to reason about. This is a false positive.
                //
                // It might be possible to eliminate this false positive by adding a ComplianceTransfer class
                // that extends org.checkerframework.framework.flow.CFTransfer, and overriding the method that
                // handles refinement after switch statements.
                //
                @SuppressWarnings("kms-compliance")
                GenerateDataKeyRequest request2 = new GenerateDataKeyRequest().withKeySpec(unknownKeySpec);
                break;
        }
    }
}
