package io.github.allaudin.yabk.model;

import com.google.gson.Gson;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.tools.JavaFileObject;

/**
 * Created on 6/16/17.
 *
 * @author M.Allaudin
 */

public final class ClassModel {

    private static final char DOLLAR_CHAR = "$".charAt(0);

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
        return "Yabk" + className;
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

    public void writeTo(Filer filer) throws Exception {
        JavaFileObject fileObject = filer.createSourceFile(classPackage);
        Writer writer = fileObject.openWriter();
        try {
            TypeSpec.Builder clazzBuilder = TypeSpec.classBuilder(className);
            clazzBuilder.superclass(ClassName.get(classPackage, parentClass));
            for (FieldModel field : fields) {
                clazzBuilder.addMethod(field.getAccessor());
                clazzBuilder.addMethod(field.getMutator());
            }


            JavaFile.builder(classPackage, clazzBuilder.build()).build().writeTo(writer);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new Exception();
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }


    } // writeTo

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
