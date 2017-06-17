package io.github.allaudin.yabk.model;

import com.google.gson.Gson;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Modifier;

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

    private String getCapitalizedString(String string){
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
    public MethodSpec getAccessor() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("get" + getCapitalizedString(fieldName));

        if (isPrimitive()) {
            builder.addStatement("return this.$N", fieldName);
            builder.returns(getType(fieldType));
        } else {
            ClassName clazz = ClassName.get(packageName, fieldType);

            // TODO: 6/17/17 pass it via annotation
            if(fieldType.equals("String")){
                String format = "return this.$1N == null? $2S: this.$1N ";
                builder.addStatement(format, fieldName, "");
            }else {
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
