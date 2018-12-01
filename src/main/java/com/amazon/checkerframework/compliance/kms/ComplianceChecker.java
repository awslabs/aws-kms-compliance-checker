package com.amazon.checkerframework.compliance.kms;

import org.checkerframework.common.value.ValueChecker;
import org.checkerframework.framework.qual.StubFiles;
import org.checkerframework.framework.source.SuppressWarningsKeys;

/**
 * An extension of the Checker Framework's Constant Value Checker
 * (https://checkerframework.org/manual/#constant-value-checker)
 * for compliance checking. The extension exists for two reasons:
 * it adds "kms", "compliance", and "kms-compliance" as custom keys to the {@code @SuppressWarnings}
 * annotation (see annotation above on the class definition) and it
 * adds annotations on KMS's DataKeySpec.AES_256 and
 * DataKeySpec.AES_128.
 *
 * <p>It would be better to write the DataKeySpec annotations in a stub file,
 * but that does not work because of a Checker Framework bug:
 * https://github.com/typetools/checker-framework/issues/2147.
 * This checker does so programmatically to work around the issue.
 */
@StubFiles("GenerateDataKeyRequest.astub")
@SuppressWarningsKeys({"compliance", "kms", "kms-compliance"})
public class ComplianceChecker extends ValueChecker {
    @Override
    protected ComplianceVisitor createSourceVisitor() {
        return new ComplianceVisitor(this);
    }
}
