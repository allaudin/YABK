package io.github.allaudin.yabk.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Modifier;

import io.github.allaudin.yabk.model.FieldModel;

/**
 * Stores information about field and generates getters/setters for the field (if asked)
 *
 * @author M.Allaudin
 */

public final class FieldGenerator {


    private static final FieldGenerator instance = new FieldGenerator();

    private FieldGenerator() {
    }

    public static FieldGenerator getInstance() {
        return instance;
    }

    MethodSpec getMutator(FieldModel fieldModel) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("set" + getCapitalizedString(fieldModel.getFieldName()));

        if (fieldModel.isPrimitive()) {
            builder.addParameter(getType(fieldModel.getFieldType()), fieldModel.getFieldName());
            builder.addStatement("this.$1N = $1N", fieldModel.getFieldName());
        }else if (fieldModel.isStringList()) {
            ClassName list = ClassName.get("java.util", "List");
            ClassName string = ClassName.get("java.lang", "String");
            TypeName typeName = ParameterizedTypeName.get(list, string);
            builder.addParameter(typeName, fieldModel.getFieldName());
            builder.addStatement("this.$1N = $1N", fieldModel.getFieldName());
        } else {
            ClassName clazz = ClassName.get(fieldModel.getPackageName(), fieldModel.getFieldType());
            builder.addParameter(clazz, fieldModel.getFieldName());
            builder.addStatement("this.$1N = $1N", fieldModel.getFieldName());
        }

        builder.addModifiers(Modifier.PUBLIC);
        builder.returns(void.class);
        return builder.build();
    } // getMutator

    private String getCapitalizedString(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    MethodSpec getAccessor(FieldModel fieldModel, boolean nonNullString) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("get" + getCapitalizedString(fieldModel.getFieldName()));

        if (fieldModel.isPrimitive()) {
            builder.addStatement("return this.$N", fieldModel.getFieldName());
            builder.returns(getType(fieldModel.getFieldType()));
        } else {
            ClassName clazz = ClassName.get(fieldModel.getPackageName(), fieldModel.getFieldType());

            if (nonNullString && fieldModel.getFieldType().equals("String")) {
                String format = "return this.$1N == null? $2S: this.$1N";
                builder.addStatement(format, fieldModel.getFieldName(), "");
            } else {
                builder.addStatement("return this.$1N = $1N", fieldModel.getFieldName());
            }

            if(fieldModel.isStringList()){

                ClassName list = ClassName.get("java.util", "List");
                ClassName string = ClassName.get("java.lang", "String");
                TypeName typeName = ParameterizedTypeName.get(list, string);
                builder.returns(typeName);
            }else {

                builder.returns(clazz);
            }
        }

        builder.addModifiers(Modifier.PUBLIC);
        return builder.build();
    } // getAccessor

    private TypeName getType(String name) {
        switch (name) {
            case "boolean":
                return TypeName.BOOLEAN;
            case "byte":
                return TypeName.BYTE;
            case "short":
                return TypeName.SHORT;
            case "int":
                return TypeName.INT;
            case "long":
                return TypeName.LONG;
            case "char":
                return TypeName.CHAR;
            case "float":
                return TypeName.FLOAT;
            case "double":
                return TypeName.DOUBLE;
        }
        throw new IllegalArgumentException("Unknown type " + name);
    } // getType

}
