package io.github.allaudin.yabk.processor;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import io.github.allaudin.yabk.Utils;
import io.github.allaudin.yabk.YabkGenerated;
import io.github.allaudin.yabk.YabkLogger;
import io.github.allaudin.yabk.model.FieldModel;

/**
 * Field processor
 *
 * @author M.Allaudin
 */

public class FieldProcessor {

    private Element element;
    private String packageName;

    private FieldProcessor(String packageName, Element element) {
        this.element = element;
        this.packageName = packageName;
    }


    public static FieldProcessor newInstance(String packageName, Element element) {
        return new FieldProcessor(packageName, element);
    }

    public FieldModel process(ProcessingEnvironment env) {
        final FieldModel model = new FieldModel();

        boolean isPrimitive = isPrimitive();

        if (!isPrimitive) {
            Element e = env.getTypeUtils().asElement(element.asType());
            PackageElement pkg = env.getElementUtils().getPackageOf(e);
            YabkLogger.note("[%s] - %s", element.getSimpleName().toString(), pkg.toString());
        }


        TypeMirror parcelType = env.getElementUtils().getTypeElement("android.os.Parcelable").asType();
        boolean isParcelable = env.getTypeUtils().isAssignable(element.asType(), parcelType);

        if (isListOfStrings(env)) {
            model.setPackageName("java.lang");
            model.setStringList(true);
            model.setFieldType("String");
            model.setParcelable(true);
        } else {
            model.setParcelable(isParcelable);
            model.setFieldType(getFieldType());
            model.setPackageName(getPackage());
        }

        model.setPrimitive(isPrimitive);
        model.setString(isStringType());
        model.setFieldName(element.getSimpleName().toString());

        return model;
    } // process


    private boolean isList(ProcessingEnvironment env) {
        TypeMirror listType = env.getElementUtils().getTypeElement("java.util.List").asType();
        TypeMirror thisType = env.getTypeUtils().asElement(element.asType()).asType();
        return env.getTypeUtils().isSameType(listType, thisType);
    }

    private boolean isYabkGenerated() {
        return element.getAnnotation(YabkGenerated.class) != null;
    }

    private String getPackage() {
        return isPrimitive() ? "" : isYabkGenerated() ? packageName : Utils.getPackage(element.asType().toString());
    }

    private boolean isStringType() {
        return element.asType().toString().equals(String.class.getCanonicalName());
    }

    private boolean isPrimitive() {
        return element.asType().getKind().isPrimitive();
    }

    private String getFieldType() {
        String fieldType = element.asType().toString();
        return isPrimitive() ? fieldType : Utils.getClassName(fieldType);
    }

    private boolean isListOfStrings(ProcessingEnvironment env) {
        if (element.asType().getKind() == TypeKind.DECLARED) {

            if (!isList(env)) {
                return false;
            }

            DeclaredType type = (DeclaredType) element.asType();
            List<? extends TypeMirror> args = type.getTypeArguments();

            return !args.isEmpty() && args.get(0).toString().equals(String.class.getCanonicalName());
        }

        return false;
    }

} // FieldProcessor
