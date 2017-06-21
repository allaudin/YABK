package io.github.allaudin.yabk.generator;

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
import io.github.allaudin.yabk.model.ClassModel;
import io.github.allaudin.yabk.model.FieldModel;

/**
 * Generates class from given fields.
 *
 * @author M.Allaudin
 */

public final class ClassGenerator {

    private Set<FieldModel> fields;
    private ClassModel classModel;
    private FieldGenerator fieldGenerator;


    public ClassGenerator(ClassModel classModel, FieldGenerator fieldGenerator) {
        this.fieldGenerator = fieldGenerator;
        this.classModel = classModel;
        fields = new HashSet<>();
    }


    public void add(FieldModel fieldModel) {
        fields.add(fieldModel);
    }


    public JavaFile getFile() {

        ClassName parcel = ClassName.get("android.os", "Parcel");
        ClassName parcelable = ClassName.get("android.os", "Parcelable");


        MethodSpec noArgConstructor = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC).build();

        TypeSpec.Builder clazzBuilder = TypeSpec.classBuilder(classModel.getClassName());
        clazzBuilder.superclass(ClassName.get(classModel.getClassPackage(), classModel.getParentClass()))
                .addSuperinterface(parcelable)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(noArgConstructor);

        boolean nonNullString = classModel.isNonNullStrings();
        boolean mutatorOnly = classModel.getMethods() == Methods.MUTATORS;
        boolean accessorOnly = classModel.getMethods() == Methods.ACCESSORS;

        MethodSpec.Builder parcelReadConstructor = MethodSpec.constructorBuilder().addModifiers(Modifier.PROTECTED)
                .addParameter(parcel, "in");


        MethodSpec.Builder parcelWriter = MethodSpec.methodBuilder("writeToParcel")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addAnnotation(Override.class)
                .addParameter(parcel, "dest")
                .addParameter(int.class, "flags");


        for (FieldModel field : fields) {

            addParcelStatements(parcelWriter, field, false);
            addParcelStatements(parcelReadConstructor, field, true);

            if (mutatorOnly) {
                clazzBuilder.addMethod(fieldGenerator.getMutator(field));
            } else if (accessorOnly) {
                clazzBuilder.addMethod(fieldGenerator.getAccessor(field, nonNullString));
            } else {
                clazzBuilder.addMethod(fieldGenerator.getMutator(field));
                clazzBuilder.addMethod(fieldGenerator.getAccessor(field, nonNullString));
            }
        } // end for


        MethodSpec.Builder describeContents = MethodSpec.methodBuilder("describeContents")
                .addModifiers(Modifier.PUBLIC)
                .returns(int.class)
                .addAnnotation(Override.class)
                .addStatement("return $L", 0);

        // add parcel methods
        clazzBuilder.addMethod(parcelReadConstructor.build());
        clazzBuilder.addMethod(parcelWriter.build());
        clazzBuilder.addMethod(describeContents.build());
        clazzBuilder.addField(getParcelCreateField());

        return JavaFile.builder(classModel.getClassPackage(), clazzBuilder.build()).build();

    } // writeTo


    private FieldSpec getParcelCreateField() {

        ClassName creator = ClassName.get("android.os.Parcelable", "Creator");
        ClassName creatorParamType = ClassName.get(classModel.getClassPackage(), classModel.getClassName());
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
                "    }", classModel.getClassName());

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
    private void addParcelStatements(MethodSpec.Builder builder, FieldModel field, boolean read) {
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

}
