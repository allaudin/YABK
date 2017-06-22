package io.github.allaudin.yabk.processor;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import io.github.allaudin.yabk.ListTypes;
import io.github.allaudin.yabk.Utils;
import io.github.allaudin.yabk.YabkGenerated;
import io.github.allaudin.yabk.model.FieldModel;

import static io.github.allaudin.yabk.YabkLogger.note;

/**
 * Field processor
 *
 * @author M.Allaudin
 */

public class FieldProcessor {

    private Element element;
    private String packageName;
    private Elements elementUtils;
    private Types typeUtils;

    private FieldProcessor(String packageName, Element element, ProcessingEnvironment env) {
        this.element = element;
        this.packageName = packageName;
        this.typeUtils = env.getTypeUtils();
        this.elementUtils = env.getElementUtils();
    }


    public static FieldProcessor newInstance(String packageName, Element element, ProcessingEnvironment env) {
        return new FieldProcessor(packageName, element, env);
    }

    private String packageOfElement(Element element) {
        return elementUtils.getPackageOf(element).toString();
    }

    private String packageOfElement() {
        return packageOfElement(element);
    }


    public FieldModel process() {
        final FieldModel model = new FieldModel();

        if (isPrimitiveOrStringType()) {

            note("primitive or String type - %s", element.getSimpleName());
            model.setParcelable(true);
            model.setFieldType(getFieldType());
            model.setPackageName(packageOfElement());
            model.setPrimitive(isPrimitiveType());
            model.setString(isStringType());

        } else if (isListOfStrings()) {

            model.setParcelable(true);
            model.setStringList(true);
            model.setFieldType(ListTypes.STRING_LIST);
            model.setPackageName("java.util");

        } else if (isList()) {
            // TODO: 6/22/17 list of java.lang.Object
            List<? extends TypeMirror> args = ((DeclaredType) element.asType()).getTypeArguments();
            if (!args.isEmpty()) {
                Element listElement = typeUtils.asElement(args.get(0));
                model.setPackageName(packageOfElement(listElement));
                model.setFieldType(getFieldType(listElement));
                model.setParcelableTypedList(true);
            } // end if

        } else {
            model.setParcelable(isParcelable(element));
            model.setFieldType(getFieldType());
            model.setPackageName(packageOfElement());
        }


        model.setFieldName(element.getSimpleName().toString());
        return model;
    } // process

    private boolean isParcelable(Element e) {
        TypeMirror parcelType = elementUtils.getTypeElement("android.os.Parcelable").asType();
        return typeUtils.isAssignable(e.asType(), parcelType);
    }


    private boolean isList() {
        TypeMirror listType = elementUtils.getTypeElement("java.util.List").asType();
        TypeMirror thisType = typeUtils.asElement(element.asType()).asType();
        // FIXME: 6/23/17 fix this type
        return listType.toString().equals(thisType.toString());
//        return typeUtils.isSameType(listType, thisType);
    }

    private boolean isYabkGenerated() {
        return element.getAnnotation(YabkGenerated.class) != null;
    }

    private String getPackage() {
        return isPrimitiveType() ? "" : isYabkGenerated() ? packageName : Utils.getPackage(element.asType().toString());
    }

    private boolean isStringType() {
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
