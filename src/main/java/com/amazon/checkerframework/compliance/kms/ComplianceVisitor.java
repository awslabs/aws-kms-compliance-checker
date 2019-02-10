package com.amazon.checkerframework.compliance.kms;

import org.checkerframework.common.basetype.BaseTypeChecker;
import org.checkerframework.common.value.ValueVisitor;

/**
 * It's necessary to create this class to override the Checker Framework's standard
 * reflection loading mechanism. If there is no explicit ComplianceVisitor that names
 * the ComplianceAnnotatedTypeFactory, the CF thinks that this is a ValueChecker instance
 * and goes looking for something named ValueAnnotatedTypeFactory and fails.
 *
 * <p>This class does nothing beyond naming ComplianceAnnotatedTypeFactory to disable the
 * reflection loading mechanism.
 *
 * <p>See https://checkerframework.org/manual/#creating-compiler-interface for the documentation of
 * the reflective class-loading mechanism of the Checker Framework.
 * org.checkerframework.common.basetype.BaseTypeVisitor#createTypeFactory
 * implements the loading mechanism; see its documentation and implementation for more details.
 */
public class ComplianceVisitor extends ValueVisitor {
    public ComplianceVisitor(BaseTypeChecker checker) {
        super(checker);
    }

    @Override
    protected ComplianceAnnotatedTypeFactory createTypeFactory() {
        return new ComplianceAnnotatedTypeFactory(this.checker);
    }
}
