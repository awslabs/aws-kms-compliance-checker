package com.amazon.checkerframework.compliance.kms;

import com.sun.source.tree.MemberSelectTree;
import org.checkerframework.common.basetype.BaseTypeChecker;
import org.checkerframework.common.value.ValueAnnotatedTypeFactory;
import org.checkerframework.framework.type.AnnotatedTypeMirror;
import org.checkerframework.framework.type.treeannotator.ListTreeAnnotator;
import org.checkerframework.framework.type.treeannotator.TreeAnnotator;

import java.util.Collections;

/**
 * Adds new type introduction rules so that DataKeySpec
 * enums are treated correctly.
 */
public class ComplianceAnnotatedTypeFactory extends ValueAnnotatedTypeFactory {

    public ComplianceAnnotatedTypeFactory(BaseTypeChecker checker) {
        super(checker);
        super.postInit();
    }

    @Override
    public TreeAnnotator createTreeAnnotator() {
        return new ListTreeAnnotator(
                new ComplianceTreeAnnotator(this), super.createTreeAnnotator());
    }

    /**
     * The tree annotator is responsible for placing default annotations on trees.
     */
    private class ComplianceTreeAnnotator extends TreeAnnotator {

        private static final String DATA_KEY_SPEC = "com.amazonaws.services.kms.model.DataKeySpec";

        ComplianceTreeAnnotator(ComplianceAnnotatedTypeFactory typeFactory) {
            super(typeFactory);
        }

        /**
         * The type of a value of the DataKeySpec enum is a StringVal with a
         * value equal to the name of the member. So, for example, DataKeySpec.AES_256's
         * type is @StringVal("AES_256").
         */
        @Override
        public Void visitMemberSelect(MemberSelectTree tree, AnnotatedTypeMirror type) {
            if (DATA_KEY_SPEC.equals(getAnnotatedType(tree.getExpression()).getUnderlyingType().toString())) {
                String identifier = tree.getIdentifier().toString();
                type.replaceAnnotation(createStringAnnotation(Collections.singletonList(identifier)));
            }
            return super.visitMemberSelect(tree, type);
        }
    }
}
