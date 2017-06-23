package io.github.allaudin.yabk.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.List;

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

        ClassName list = ClassName.get("java.util", "List");
        builder.addModifiers(Modifier.PUBLIC);
        builder.returns(void.class);

        if (fieldModel.isPrimitive()) {
            builder.addParameter(getType(fieldModel.getFieldType()), fieldModel.getFieldName());
            builder.addStatement("this.$1N = $1N", fieldModel.getFieldName());
            return builder.build();

        }

        if (fieldModel.isStringList()) {
            ClassName string = ClassName.get("java.lang", "String");
            TypeName typeName = ParameterizedTypeName.get(list, string);
            builder.addParameter(typeName, fieldModel.getFieldName());
            builder.addStatement("this.$1N = $1N", fieldModel.getFieldName());
            return builder.build();
        }

        if (fieldModel.isList()) {
            ClassName string = ClassName.get(fieldModel.getPackageName(), fieldModel.getFieldType());
            TypeName typeName = ParameterizedTypeName.get(list, string);
            builder.addParameter(typeName, fieldModel.getFieldName());
            builder.addStatement("this.$1N = $1N", fieldModel.getFieldName());
            return builder.build();
        }

        if (fieldModel.isGeneric()) {
            ClassName genType = ClassName.get(fieldModel.getPackageName(), fieldModel.getFieldType());

            List<ClassName> classes = new ArrayList<>();
            List<FieldModel> fields = fieldModel.getActualTypeInfo().getActualTypes();

            for (FieldModel field : fields) {
                ClassName cls = ClassName.get(field.getPackageName(), field.getFieldType());
                classes.add(cls);
            }

            ClassName[] classesArray = classes.toArray(new ClassName[]{});
            TypeName types = ParameterizedTypeName.get(genType, classesArray);

            builder.addParameter(types, fieldModel.getFieldName());
            builder.addStatement("this.$1N = $1N", fieldModel.getFieldName());

            return builder.build();
        }
        ClassName clazz = ClassName.get(fieldModel.getPackageName(), fieldModel.getFieldType());
        builder.addParameter(clazz, fieldModel.getFieldName());
        builder.addStatement("this.$1N = $1N", fieldModel.getFieldName());


        return builder.build();
    } // getMutator

    private String getCapitalizedString(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    MethodSpec getAccessor(FieldModel fieldModel, boolean nonNullString) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("get" + getCapitalizedString(fieldModel.getFieldName()));
        builder.addModifiers(Modifier.PUBLIC);

        if (fieldModel.isPrimitive()) {
            builder.addStatement("return this.$N", fieldModel.getFieldName());
            builder.returns(getType(fieldModel.getFieldType()));
            return builder.build();
        }


        if (fieldModel.isGeneric()) {
            ClassName genType = ClassName.get(fieldModel.getPackageName(), fieldModel.getFieldType());

            List<ClassName> classes = new ArrayList<>();
            List<FieldModel> fields = fieldModel.getActualTypeInfo().getActualTypes();

            for (FieldModel field : fields) {
                ClassName cls = ClassName.get(field.getPackageName(), field.getFieldType());
                classes.add(cls);
            }

            ClassName[] classesArray = classes.toArray(new ClassName[]{});
            TypeName types = ParameterizedTypeName.get(genType, classesArray);
            builder.returns(types);
            builder.addStatement("return this.$1N = $1N", fieldModel.getFieldName());
            return builder.build();
        }

        ClassName clazz = ClassName.get(fieldModel.getPackageName(), fieldModel.getFieldType());

        if (nonNullString && fieldModel.isString()) {
            String format = "return this.$1N == null? $2S: this.$1N";
            builder.addStatement(format, fieldModel.getFieldName(), "");
        } else {
            builder.addStatement("return this.$1N = $1N", fieldModel.getFieldName());
        }
        ClassName list = ClassName.get("java.util", "List");

        if (fieldModel.isStringList()) {
            ClassName string = ClassName.get("java.lang", "String");
            TypeName typeName = ParameterizedTypeName.get(list, string);
            builder.returns(typeName);
        } else if (fieldModel.isList()) {
            ClassName string = ClassName.get(fieldModel.getPackageName(), fieldModel.getFieldType());
            TypeName typeName = ParameterizedTypeName.get(list, string);
            builder.returns(typeName);
        } else {
            builder.returns(clazz);
        }


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
