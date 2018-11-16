// This is test data for the KMSComplianceChecker. This test makes sure that the checker treats enums of the
// type com.amazonaws.services.kms.model.DataKeySpec in a special way to work around this CF issue:
// https://github.com/typetools/checker-framework/issues/2147

import org.checkerframework.common.value.qual.*;

import com.amazonaws.services.kms.model.DataKeySpec;

class EnumCheck {
    void test() {
        @StringVal("AES_256") DataKeySpec d1 = DataKeySpec.AES_256;
        @StringVal("AES_128") DataKeySpec d2 = DataKeySpec.AES_128;

        // :: error: assignment.type.incompatible
        @StringVal("AES_256") DataKeySpec d3 = DataKeySpec.AES_128;
        // :: error: assignment.type.incompatible
        @StringVal("AES_128") DataKeySpec d4 = DataKeySpec.AES_256;
    }

    final static DataKeySpec myDataKeySpec = DataKeySpec.AES_256;

    void test_member() {
        @StringVal("AES_256") DataKeySpec d5 = EnumCheck.myDataKeySpec;

        // :: error: assignment.type.incompatible
        @StringVal("myDataKeySpec") DataKeySpec d6 = EnumCheck.myDataKeySpec;
    }
}