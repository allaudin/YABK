package io.github.allaudin.yabk.model;

import com.google.gson.Gson;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.Modifier;

/**
 * Created on 6/16/17.
 *
 * @author M.Allaudin
 */

public final class ClassModel {


    // type-field name
    private Set<FieldModel> fields;

    private String className;
    private String classPackage;
    private String parentClass;

    public ClassModel(String className) {
        this.parentClass = getName(className);
        this.classPackage = getPackage(className);
        this.className = cleanClassName(getName(className));
        fields = new HashSet<>();
    }

    private String cleanClassName(String className) {
        return className.charAt(0) == "$".charAt(0) ? className.replaceFirst("\\$", "") : "Yabk" + className;
    } // cleanClassName

    public void add(String type, String field) {

        final FieldModel fieldModel = new FieldModel();
        fieldModel.setFieldName(field);
        if (!type.contains(".")) {
            fieldModel.setFieldType(getName(type));
        } else {
            fieldModel.setPackageName(getPackage(type));
            fieldModel.setFieldType(getName(type));
        }
        fields.add(fieldModel);
    }

    private String getPackage(String type) {
        return type.substring(0, type.lastIndexOf("."));
    }

    private String getName(String type) {
        return type.substring(type.lastIndexOf(".") + 1);
    }

    public JavaFile getFile() {

        TypeSpec.Builder clazzBuilder = TypeSpec.classBuilder(className);
        clazzBuilder.superclass(ClassName.get(classPackage, parentClass));
        clazzBuilder.addModifiers(Modifier.PUBLIC);
        for (FieldModel field : fields) {
            clazzBuilder.addMethod(field.getAccessor());
            clazzBuilder.addMethod(field.getMutator());
        }

        return JavaFile.builder(classPackage, clazzBuilder.build()).build();

    } // writeTo

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
