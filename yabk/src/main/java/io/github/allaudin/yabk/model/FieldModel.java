package io.github.allaudin.yabk.model;

import com.google.gson.Gson;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

/**
 * Created on 6/16/17.
 *
 * @author M.Allaudin
 */

public class FieldModel {

    private String packageName;
    private String fieldName;
    private String fieldType;

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldModel that = (FieldModel) o;
        return packageName != null ? packageName.equals(that.packageName) : that.packageName == null && fieldName.equals(that.fieldName);

    }

    @Override
    public int hashCode() {
        int result = packageName != null ? packageName.hashCode() : 0;
        result = 31 * result + fieldName.hashCode();
        return result;
    }

    public MethodSpec getMutator() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("set" + fieldName);

        if (isPrimitive()) {
            builder.addParameter(getType(fieldType), fieldName);
            builder.addStatement("this.$1N = $1N", fieldName);
        } else {
            ClassName clazz = ClassName.get(packageName, fieldType);
            builder.addParameter(clazz, fieldName);
            builder.addStatement("this.$1N = $1N", fieldName);
        }

        builder.returns(void.class);
        return builder.build();
    } // getMutator

    public MethodSpec getAccessor() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("get" + fieldName);

        if (isPrimitive()) {
            builder.addStatement("return this.$N", fieldName);
            builder.returns(getType(fieldType));
        } else {
            ClassName clazz = ClassName.get(packageName, fieldType);
            builder.addStatement("return this.$N", fieldName);
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

    private boolean isPrimitive() {
        return packageName == null || packageName.length() == 0;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getFieldName() {
        return fieldName;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
