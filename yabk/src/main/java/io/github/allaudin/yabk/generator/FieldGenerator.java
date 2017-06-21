package io.github.allaudin.yabk.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Modifier;

/**
 * Stores information about field and generates getters/setters for the field (if asked)
 *
 * @author M.Allaudin
 */

class FieldGenerator {

    /**
     * Package name of this field
     */
    private String packageName;

    /**
     * Field name of this field
     */
    private String fieldName;

    /**
     * Type of this field
     */
    private String fieldType;

    /**
     * Set if field type is primitive or not
     */
    private boolean isPrimitive;

    void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    String getFieldName() {
        return fieldName;
    }

    String getFieldType() {
        return fieldType;
    }

    MethodSpec getMutator() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("set" + getCapitalizedString(fieldName));

        if (isPrimitive()) {
            builder.addParameter(getType(fieldType), fieldName);
            builder.addStatement("this.$1N = $1N", fieldName);
        } else {
            ClassName clazz = ClassName.get(packageName, fieldType);
            builder.addParameter(clazz, fieldName);
            builder.addStatement("this.$1N = $1N", fieldName);
        }

        builder.addModifiers(Modifier.PUBLIC);
        builder.returns(void.class);
        return builder.build();
    } // getMutator

    private String getCapitalizedString(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    MethodSpec getAccessor(boolean nonNullString) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("get" + getCapitalizedString(fieldName));

        if (isPrimitive()) {
            builder.addStatement("return this.$N", fieldName);
            builder.returns(getType(fieldType));
        } else {
            ClassName clazz = ClassName.get(packageName, fieldType);

            if (nonNullString && fieldType.equals("String")) {
                String format = "return this.$1N == null? $2S: this.$1N";
                builder.addStatement(format, fieldName, "");
            } else {
                builder.addStatement("return this.$1N = $1N", fieldName);
            }
            builder.returns(clazz);
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

    void setPrimitive(boolean primitive) {
        isPrimitive = primitive;
    }

    private boolean isPrimitive() {
        return isPrimitive;
    }

    void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

}
