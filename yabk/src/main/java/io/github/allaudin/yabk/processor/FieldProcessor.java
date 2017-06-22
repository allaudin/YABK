package io.github.allaudin.yabk.processor;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
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
    private Elements elements;
    private Types types;

    private FieldProcessor(String packageName, Element element, ProcessingEnvironment env) {
        this.element = element;
        this.packageName = packageName;
        elements = env.getElementUtils();
        types = env.getTypeUtils();
    }


    public static FieldProcessor newInstance(String packageName, Element element, ProcessingEnvironment env) {
        return new FieldProcessor(packageName, element, env);
    }

    public FieldModel process() {
        final FieldModel model = new FieldModel();

        boolean isPrimitive = isPrimitiveType();

        if (!isPrimitive) {
            Element e = types.asElement(element.asType());
            PackageElement pkg = elements.getPackageOf(e);
            note("[%s] - %s", element.getSimpleName().toString(), pkg.toString());
            if (e.asType() instanceof DeclaredType) {
                note("args size [%s]", ((DeclaredType) e.asType()).getTypeArguments().size());
            }
        }


        TypeMirror parcelType = elements.getTypeElement("android.os.Parcelable").asType();
        boolean isParcelable = types.isAssignable(element.asType(), parcelType);

        if (isListOfStrings()) {
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


    private boolean isList() {
        TypeMirror listType = elements.getTypeElement("java.util.List").asType();
        TypeMirror thisType = types.asElement(element.asType()).asType();
        return types.isSameType(listType, thisType);
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
