package com.amazon.checkerframework.compliance.kms;

import org.checkerframework.common.basetype.BaseTypeChecker;
import org.checkerframework.common.value.ValueAnnotatedTypeFactory;

/**
 * Empty annotated type factory, in case we ever need one.
 *
 * Used to handle defaulting for enums until https://github.com/typetools/checker-framework/issues/2147
 * was fixed.
 */
public class ComplianceAnnotatedTypeFactory extends ValueAnnotatedTypeFactory {

    public ComplianceAnnotatedTypeFactory(BaseTypeChecker checker) {
        super(checker);
        super.postInit();
    }
}
