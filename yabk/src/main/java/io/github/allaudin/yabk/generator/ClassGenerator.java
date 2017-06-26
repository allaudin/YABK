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

import io.github.allaudin.yabk.ListTypes;
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

        MethodSpec.Builder parcelReadBuilder = MethodSpec.constructorBuilder().addModifiers(Modifier.PROTECTED)
                .addParameter(parcel, "in");


        MethodSpec.Builder parcelWriteBuilder = MethodSpec.methodBuilder("writeToParcel")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addAnnotation(Override.class)
                .addParameter(parcel, "dest")
                .addParameter(int.class, "flags");


        for (FieldModel field : fields) {

            if (field.canBeAddedToParcel()) {
                // TODO: 6/21/17 what if parcelable class is abstract, it will not have CREATE field. 
                addWriteParcel(parcelWriteBuilder, field);
                addReadParcel(parcelReadBuilder, field);
            }

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
        clazzBuilder.addMethod(parcelReadBuilder.build());
        clazzBuilder.addMethod(parcelWriteBuilder.build());
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
     */
    private void addReadParcel(MethodSpec.Builder builder, FieldModel field) {
        String type = field.getFieldType();
        String name = field.getFieldName();
        String format = "";
        boolean isArray = field.isArray();

        switch (type) {

            case "boolean":
                format = isArray ? "this.$N = in.createBooleanArray()" : "this.$N = in.readByte() != 0";
                break;
            case "byte":
                format = isArray ? "this.$N = in.createByteArray()" : "this.$N = in.readByte()";
                break;
            case "int":
                format = isArray ? "this.$N = in.createIntArray()" : "this.$N = in.readInt()";
                break;
            case "long":
                format = isArray ? "this.$N = in.createLongArray()" : "this.$N = in.readLong()";
                break;
            case "float":
                format = isArray ? "this.$N = in.createFloatArray()" : "this.$N = in.readFloat()";
                break;
            case "String":
                format = isArray ? "this.$N = in.createStringArray()" : "this.$N = in.readString()";
                break;
            case "double":
                format = isArray ? "this.$N = in.createDoubleArray()" : "this.$N = in.readDouble()";
                break;
            case ListTypes.STRING_LIST:
                format = "this.$N = in.createStringArrayList()";
                break;

            default: {
                ClassName typeName = ClassName.get(field.getPackageName(), field.getFieldType());
                if (field.isParcelableTypedList()) {
                    builder.addStatement("this.$N = in.createTypedArrayList($T.CREATOR)", name, typeName);
                } else if(field.isParcelableArray()){
                    builder.addStatement("this.$N = in.createTypedArray($T.CREATOR)", name, typeName);
                }else if (field.isParcelable()) {
                    builder.addStatement("this.$N = in.readParcelable($T.class.getClassLoader())", name, typeName);
                }
            }
        } // switch


        if (format.length() > 0) {
            builder.addStatement(format, name);
        }
    } // addReadParcel

    private void addWriteParcel(MethodSpec.Builder builder, FieldModel field) {
        String type = field.getFieldType();
        String name = field.getFieldName();
        String format = "";
        boolean isArray = field.isArray();

        switch (type) {

            case "boolean":
                format = isArray ? "dest.writeBooleanArray($N)" : "dest.writeByte((byte) ($N ? 1 : 0))";
                break;
            case "byte":
                format = isArray ? "dest.writeByteArray($N)" : "dest.writeByte($N)";
                break;
            case "int":
                format = isArray ? "dest.writeIntArray($N)" : "dest.writeInt($N)";
                break;
            case "long":
                format = isArray ? "dest.writeLongArray($N)" : "dest.writeLong($N)";
                break;
            case "float":
                format = isArray ? "dest.writeFloatArray($N)" : "dest.writeFloat($N)";
                break;
            case "String":
                format = isArray ? "dest.writeStringArray($N)" : "dest.writeString($N)";
                break;
            case "double":
                format = isArray ? "dest.writeDoubleArray($N)" : "dest.writeDouble($N)";
                break;
            case ListTypes.STRING_LIST:
                format = "dest.writeStringList($N)";
                break;

            default: {
                if (field.isParcelableTypedList()) {
                    format = "dest.writeTypedList($N)";
                } else if(field.isParcelableArray()){
                    format = "dest.writeTypedArray($N, flags)";
                }else if (field.isParcelable()) {
                    format = "dest.writeParcelable($N, flags)";
                }
            }
        } // switch

        if (format.length() > 0) {
            builder.addStatement(format, name);
        }
    } // addWriteParcel


}
