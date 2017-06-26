package io.github.allaudin.yabk.processor;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import io.github.allaudin.yabk.ListTypes;
import io.github.allaudin.yabk.Utils;
import io.github.allaudin.yabk.YabkLogger;
import io.github.allaudin.yabk.model.FieldModel;

/**
 * Field processor
 *
 * @author M.Allaudin
 */

public class FieldProcessor {

    private Element element;
    private Elements elementUtils;
    private Types typeUtils;

    private FieldProcessor(Element element, ProcessingEnvironment env) {
        this.element = element;
        this.typeUtils = env.getTypeUtils();
        this.elementUtils = env.getElementUtils();
    }


    public static FieldProcessor newInstance(Element element, ProcessingEnvironment env) {
        return new FieldProcessor(element, env);
    }

    private String packageOfElement(Element element) {
        return elementUtils.getPackageOf(element).toString();
    }

    private String packageOfElement() {
        return packageOfElement(element);
    }

    // no declared type check. check it before
    private String packageOfDeclaredElement() {
        return elementUtils.getPackageOf(((DeclaredType) element.asType()).asElement()).toString();
    }


    public FieldModel process() {
        final FieldModel model = new FieldModel();

        model.setFieldName(element.getSimpleName().toString());

        // primitive processing
        if (isPrimitiveOrStringType()) {
            model.setParcelable(true);
            model.setFieldType(getFieldType());
            model.setPackageName(packageOfElement());
            model.setPrimitive(isPrimitiveType());
            model.setString(isStringType());
            return model;

        }

        // array type processing

        if (element.asType().getKind() == TypeKind.ARRAY) {
            model.setArray(true);
            TypeMirror componentType = ((ArrayType) element.asType()).getComponentType();
            boolean isPrimitive = componentType.getKind().isPrimitive();
            Element thisElement = ((DeclaredType) componentType).asElement();
            model.setPrimitive(isPrimitive);
            model.setFieldType(thisElement.getSimpleName().toString());

            if (!isPrimitive) {
                model.setParcelable(isParcelable(thisElement));
                model.setString(isStringType(typeUtils.asElement(componentType)));
                model.setPackageName(packageOfElement(typeUtils.asElement(componentType)));
            }
            return model;
        }

        // list processing
        if (isListOfStrings()) {

            model.setParcelable(true);
            model.setStringList(true);
            model.setFieldType(ListTypes.STRING_LIST);
            model.setPackageName("java.util");
            return model;

        }

        // typed list processing
        if (isList()) {
            List<? extends TypeMirror> args = ((DeclaredType) element.asType()).getTypeArguments();
            if (!args.isEmpty()) {
                Element listElement = typeUtils.asElement(args.get(0));
                model.setPackageName(packageOfElement(listElement));
                model.setFieldType(getFieldType(listElement));
                model.setList(true);
                model.setParcelableTypedList(isParcelable(listElement));
                return model;
            } // end if

        }

        // TODO: 6/23/17 add array type check
        // other declared type - add Array Type check
        model.setParcelable(isParcelable(element));
        model.setFieldType(getFieldType());
        model.setPackageName(packageOfDeclaredElement());
        YabkLogger.note("%s", model.toString());

        if (isGeneric()) {

            model.setGeneric(true);

            FieldModel.ActualTypeInfo actualTypes = new FieldModel.ActualTypeInfo();

            model.setFieldType(Utils.getClassName(typeUtils.erasure(element.asType()).toString()));
            List<? extends TypeMirror> args = ((DeclaredType) element.asType()).getTypeArguments();
            for (TypeMirror type : args) {
                Element typeElement = typeUtils.asElement(type);
                FieldModel field = new FieldModel();
                field.setParcelable(isParcelable(typeElement));
                field.setFieldType(getFieldType(typeElement));
                field.setPackageName(packageOfElement(typeElement));
                actualTypes.add(field);
            }

            model.setActualTypeInfo(actualTypes);

        } // end generic if

        return model;
    } // process

    private boolean isParcelable(Element e) {
        TypeMirror parcelType = elementUtils.getTypeElement("android.os.Parcelable").asType();
        return typeUtils.isAssignable(e.asType(), parcelType);
    }

    private boolean isGeneric() {
        return !((DeclaredType) element.asType()).getTypeArguments().isEmpty();
    }


    private boolean isList() {
        TypeMirror listType = elementUtils.getTypeElement("java.util.List").asType();
        TypeMirror thisType = typeUtils.asElement(element.asType()).asType();
        // FIXME: 6/23/17 fix this type
        return listType.toString().equals(thisType.toString());
//        return typeUtils.isSameType(listType, thisType);
    }


    private boolean isStringType() {
        return isStringType(element);
    }

    private boolean isStringType(Element element) {
        return element.asType().toString().equals(String.class.getCanonicalName());
    }


    private boolean isPrimitiveType() {
        return element.asType().getKind().isPrimitive();
    }

    private boolean isPrimitiveOrStringType() {
        return element.asType().getKind().isPrimitive() ||
                element.asType().toString().equals(String.class.getCanonicalName());
    }

    private String getFieldType(Element element) {
        String fieldType = element.asType().toString();
        return isPrimitiveType() ? fieldType : Utils.getClassName(fieldType);
    }

    private String getFieldType() {
        return getFieldType(element);
    }


    private boolean isListOfStrings() {
        if (element.asType().getKind() == TypeKind.DECLARED) {

            if (!isList()) {
                return false;
            }

            DeclaredType type = (DeclaredType) element.asType();
            List<? extends TypeMirror> args = type.getTypeArguments();

            return !args.isEmpty() && args.get(0).toString().equals(String.class.getCanonicalName());
        }

        return false;
    }

} // FieldProcessor
