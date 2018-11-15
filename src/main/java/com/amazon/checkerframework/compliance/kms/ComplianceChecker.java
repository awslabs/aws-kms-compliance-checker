package com.amazon.checkerframework.compliance.kms;

import org.checkerframework.common.value.ValueChecker;
import org.checkerframework.framework.qual.StubFiles;
import org.checkerframework.framework.source.SuppressWarningsKeys;

/**
 * An extension of the Checker Framework's Constant Value Checker
 * (https://checkerframework.org/manual/#constant-value-checker)
 * for compliance checking. The extension exists for two reasons:
 * it adds "kms" and "compliance" as custom keys to the @SuppressWarnings
 * annotation (see annotation above on the class definition) and so
 * that we can work around this Checker Framework issue:
 * https://github.com/typetools/checker-framework/issues/2147.
 *
 * That workaround uses the annotated type factory to
 * place annotations on KMS' DataKeySpec.AES_256 and
 * DataKeySpec.AES_128, which would otherwise appear
 * in stub files.
 */
@StubFiles("GenerateDataKeyRequest.astub")
@SuppressWarningsKeys({"compliance", "kms", "kms-compliance"})
public class ComplianceChecker extends ValueChecker {
    @Override
    protected ComplianceVisitor createSourceVisitor() {
        return new ComplianceVisitor(this);
    }
}
