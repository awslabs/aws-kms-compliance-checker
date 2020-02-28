package com.amazon.checkerframework.compliance.kms;

import org.checkerframework.common.basetype.BaseTypeChecker;
import org.checkerframework.common.value.ValueAnnotatedTypeFactory;
import org.checkerframework.common.value.qual.ArrayLen;
import org.checkerframework.common.value.qual.ArrayLenRange;
import org.checkerframework.common.value.qual.BoolVal;
import org.checkerframework.common.value.qual.BottomVal;
import org.checkerframework.common.value.qual.DoubleVal;
import org.checkerframework.common.value.qual.IntRange;
import org.checkerframework.common.value.qual.IntRangeFromGTENegativeOne;
import org.checkerframework.common.value.qual.IntRangeFromNonNegative;
import org.checkerframework.common.value.qual.IntRangeFromPositive;
import org.checkerframework.common.value.qual.IntVal;
import org.checkerframework.common.value.qual.PolyValue;
import org.checkerframework.common.value.qual.StringVal;
import org.checkerframework.common.value.qual.UnknownVal;

import java.lang.annotation.Annotation;
import java.util.Set;

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

    @Override
    protected Set<Class<? extends Annotation>> createSupportedTypeQualifiers() {
        return getBundledTypeQualifiers(ArrayLen.class,
                ArrayLenRange.class,
                IntVal.class,
                IntRange.class,
                BoolVal.class,
                StringVal.class,
                DoubleVal.class,
                BottomVal.class,
                UnknownVal.class,
                IntRangeFromPositive.class,
                IntRangeFromNonNegative.class,
                IntRangeFromGTENegativeOne.class,
                PolyValue.class);
    }
}
