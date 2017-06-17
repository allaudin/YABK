package io.github.allaudin.yabk.generator;

import com.google.gson.Gson;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
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

public final class ClassGenerator {

    private Set<FieldGenerator> fields;
    private ClassMeta classMeta;


    public ClassGenerator(ClassMeta classMeta) {
        this.classMeta = classMeta;
        fields = new HashSet<>();
    }


    public void add(String type, String field) {

        final FieldGenerator fieldGenerator = new FieldGenerator();
        fieldGenerator.setFieldName(field);
        if (!type.contains(".")) {
            fieldGenerator.setFieldType(Utils.getClassName(type));
        } else {
            fieldGenerator.setPackageName(Utils.getPackage(type));
            fieldGenerator.setFieldType(Utils.getClassName(type));
        }
        fields.add(fieldGenerator);
    }


    public JavaFile getFile() {

        ClassName parcel = ClassName.get("android.os", "Parcel");
        ClassName parcelable = ClassName.get("android.os", "Parcelable");


        MethodSpec noArgConstructor = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC).build();

        TypeSpec.Builder clazzBuilder = TypeSpec.classBuilder(classMeta.getClassName());
        clazzBuilder.superclass(ClassName.get(classMeta.getClassPackage(), classMeta.getParentClass()))
                .addSuperinterface(parcelable)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(noArgConstructor);

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


        for (FieldGenerator field : fields) {

            addParcelStatements(parcelWrite, field, false);
            addParcelStatements(parcelConstructor, field, true);

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

        // add parcel methods
        clazzBuilder.addMethod(parcelConstructor.build());
        clazzBuilder.addMethod(parcelWrite.build());
        clazzBuilder.addMethod(describeContents.build());
        clazzBuilder.addField(getParcelCreateField());

        return JavaFile.builder(classMeta.getClassPackage(), clazzBuilder.build()).build();

    } // writeTo


    private FieldSpec getParcelCreateField() {

        ClassName creator = ClassName.get("android.os.Parcelable", "Creator");
        ClassName creatorParamType = ClassName.get(classMeta.getClassPackage(), classMeta.getClassName());
        TypeName creatorType = ParameterizedTypeName.get(creator, creatorParamType);

        CodeBlock block = CodeBlock.of("new Creator<$1N>() {\n" +
                "        @Override\n" +
                "        public $1N createFromParcel(Parcel in) {\n" +
                "            return new $1N(in);\n" +
                "        }\n\n" +
                "        @Override\n" +
                "        public $1N[] newArray(int size) {\n" +
                "            return new $1N[size];\n" +
                "        }\n" +
                "    }", classMeta.getClassName());

        FieldSpec.Builder creatorFieldBuilder = FieldSpec.builder(creatorType, "CREATOR",
                Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer(block);
        return creatorFieldBuilder.build();

    } // getParcelCreateField

    /**
     * Add parcelable statements
     *
     * @param builder method builder
     * @param field   parcelable field
     * @param read    true - if it is read statement, false if it is write.
     */
    private void addParcelStatements(MethodSpec.Builder builder, FieldGenerator field, boolean read) {
        String type = field.getFieldType();
        String name = field.getFieldName();
        String format = "";

        switch (type) {

            case "boolean":
                format = read ? "this.$N = in.readByte() != 0" : "dest.writeByte((byte) ($N ? 1 : 0))";
                break;
            case "byte":
                format = read ? "this.$N = in.readByte()" : "dest.writeByte($N)";
                break;
            case "int":
                format = read ? "this.$N = in.readInt()" : "dest.writeInt($N)";
                break;
            case "long":
                format = read ? "this.$N = in.readLong()" : "dest.writeLong($N)";
                break;
            case "float":
                format = read ? "this.$N = in.readFloat()" : "dest.writeFloat($N)";
                break;
            case "String":
                format = read ? "this.$N = in.readString()" : "dest.writeString($N)";
                break;
            case "double":
                format = read ? "this.$N = in.readDouble()" : "dest.writeDouble($N)";
                break;

        } // switch

        if (format.length() > 0) {
            builder.addStatement(format, name);
        }
    } // addParcelStatements


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
