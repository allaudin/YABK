package io.github.allaudin.yabk.processor;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

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

    private String packageOfElement() {
        return elementUtils.getPackageOf(element).toString();
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
            // TODO: 6/22/17 just a placeholder - make it better
            model.setFieldType("StringList");
            model.setPackageName("java.util");

        } else {

            model.setParcelable(isParcelable());
            model.setFieldType(getFieldType());
            // TODO: 6/22/17 check why it returns default package
            model.setPackageName(packageOfElement());
            note("package %s - %s", model.getPackageName(), element.getSimpleName().toString());
        }

        model.setFieldName(element.getSimpleName().toString());
        return model;
    } // process

    private boolean isParcelable() {
        TypeMirror parcelType = elementUtils.getTypeElement("android.os.Parcelable").asType();
        return typeUtils.isAssignable(element.asType(), parcelType);
    }


    private boolean isList() {
        TypeMirror listType = elementUtils.getTypeElement("java.util.List").asType();
        TypeMirror thisType = typeUtils.asElement(element.asType()).asType();
        return typeUtils.isSameType(listType, thisType);
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

    private String getFieldType() {
        String fieldType = element.asType().toString();
        return isPrimitiveType() ? fieldType : Utils.getClassName(fieldType);
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
