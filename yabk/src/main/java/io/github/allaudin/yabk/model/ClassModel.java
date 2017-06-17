package io.github.allaudin.yabk.model;

import com.google.gson.Gson;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
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

        ClassName parcel = ClassName.get("android.os", "Parcel");
        ClassName parcelable = ClassName.get("android.os", "Parcelable");

        TypeSpec.Builder clazzBuilder = TypeSpec.classBuilder(classMeta.getClassName());
        clazzBuilder.superclass(ClassName.get(classMeta.getClassPackage(), classMeta.getParentClass()));
        clazzBuilder.addSuperinterface(parcelable);

        clazzBuilder.addModifiers(Modifier.PUBLIC);

        boolean nonNullString = classMeta.nonNullStrings();
        boolean mutatorOnly = classMeta.getMethods() == Methods.MUTATORS;
        boolean accessorOnly = classMeta.getMethods() == Methods.ACCESSORS;

        MethodSpec.Builder parcelConstructor = MethodSpec.constructorBuilder().addModifiers(Modifier.PROTECTED)
                .addParameter(parcel, "in");


        MethodSpec.Builder parcelWrite = MethodSpec.methodBuilder("writeToParcel")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addAnnotation(Override.class)
                .addParameter(parcel, "dest")
                .addParameter(int.class, "flags");


        for (FieldModel field : fields) {

            addWriteToParcel(parcelWrite, field);
            addReadFromParcel(parcelConstructor, field);

            if (mutatorOnly) {
                clazzBuilder.addMethod(field.getMutator());
            } else if (accessorOnly) {
                clazzBuilder.addMethod(field.getAccessor(nonNullString));
            } else {
                clazzBuilder.addMethod(field.getMutator());
                clazzBuilder.addMethod(field.getAccessor(nonNullString));
            }
        } // end for


        MethodSpec.Builder describeContents = MethodSpec.methodBuilder("describeContents")
                .addModifiers(Modifier.PUBLIC)
                .returns(int.class)
                .addAnnotation(Override.class)
                .addStatement("return $L", 0);

        clazzBuilder.addMethod(parcelConstructor.build());
        clazzBuilder.addMethod(parcelWrite.build());
        clazzBuilder.addMethod(describeContents.build());
        
        return JavaFile.builder(classMeta.getClassPackage(), clazzBuilder.build()).build();

    } // writeTo


    private void addReadFromParcel(MethodSpec.Builder builder, FieldModel field) {
        String type = field.getFieldType();
        String name = field.getFieldName();
        String format = "";

        switch (type) {

            case "boolean":
                format = "this.$N = in.readByte() != 0";
                break;
            case "byte":
                format = "this.$N = in.readByte()";
                break;
            case "int":
                format = "this.$N = in.readInt()";
                break;
            case "long":
                format = "this.$N = in.readLong()";
                break;

            case "float":
                format = "this.$N = in.readFloat()";
                break;
            case "double":
                format = "this.$N = in.readDouble()";
                break;
        } // switch

        if (format.length() > 0) {
            builder.addStatement(format, name);
        }
    } // addReadFromParcel


    private void addWriteToParcel(MethodSpec.Builder builder, FieldModel field) {
        String type = field.getFieldType();
        String name = field.getFieldName();
        String format = "";

        switch (type) {

            case "boolean":
                format = "dest.writeByte((byte) ($N ? 1 : 0))";
                break;
            case "byte":
                format = " dest.writeByte($N)";
                break;

            case "int":
                format = " dest.writeInt($N)";
                break;
            case "long":
                format = " dest.writeLong($N)";
                break;

            case "float":
                format = " dest.writeFloat($N)";
                break;
            case "double":
                format = " dest.writeDouble($N)";
                break;
        } // switch

        if (format.length() > 0) {
            builder.addStatement(format, name);
        }
    } // addReadFromParcel

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
