package io.github.allaudin.yabk.model;

import com.google.gson.Gson;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.Modifier;

import io.github.allaudin.yabk.Methods;
import io.github.allaudin.yabk.Utils;

/**
 * Created on 6/16/17.
 *
 * @author M.Allaudin
 */

public final class ClassModel {

    private Set<FieldModel> fields;
    private ClassMetaModel classMeta;


    public ClassModel(ClassMetaModel classMeta) {
        this.classMeta = classMeta;
        fields = new HashSet<>();
    }


    public void add(String type, String field) {

        final FieldModel fieldModel = new FieldModel();
        fieldModel.setFieldName(field);
        if (!type.contains(".")) {
            fieldModel.setFieldType(Utils.getClassName(type));
        } else {
            fieldModel.setPackageName(Utils.getPackage(type));
            fieldModel.setFieldType(Utils.getClassName(type));
        }
        fields.add(fieldModel);
    }


    public JavaFile getFile() {

        TypeSpec.Builder clazzBuilder = TypeSpec.classBuilder(classMeta.getClassName());
        clazzBuilder.superclass(ClassName.get(classMeta.getClassPackage(), classMeta.getParentClass()));
        clazzBuilder.addModifiers(Modifier.PUBLIC);

        boolean nonNullString = classMeta.nonNullStrings();
        boolean mutatorOnly = classMeta.getMethods() == Methods.MUTATORS;
        boolean accessorOnly = classMeta.getMethods() == Methods.ACCESSORS;


        for (FieldModel field : fields) {
            if (mutatorOnly) {
                clazzBuilder.addMethod(field.getMutator());
            } else if (accessorOnly) {
                clazzBuilder.addMethod(field.getAccessor(nonNullString));
            } else {
                clazzBuilder.addMethod(field.getMutator());
                clazzBuilder.addMethod(field.getAccessor(nonNullString));
            }
        } // end for

        return JavaFile.builder(classMeta.getClassPackage(), clazzBuilder.build()).build();

    } // writeTo

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
