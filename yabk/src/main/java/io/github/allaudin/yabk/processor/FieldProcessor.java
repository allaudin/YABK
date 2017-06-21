package io.github.allaudin.yabk.processor;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;

import io.github.allaudin.yabk.Utils;
import io.github.allaudin.yabk.YabkGenerated;
import io.github.allaudin.yabk.YabkSkip;
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

        model.setShouldBeAdded(shouldBeAdded());

        if (!shouldBeAdded()) {
            return model;
        }

        TypeMirror parcelType = env.getElementUtils().getTypeElement("android.os.Parcelable").asType();
        boolean isParcelable = env.getTypeUtils().isAssignable(element.asType(), parcelType);

        model.setParcelable(isParcelable);
        model.setGeneratedByYabk(isYabkGenerated());
        model.setPrimitive(isPrimitive());
        model.setFieldType(getFieldType());
        model.setPackageName(getPackage());
        model.setFieldName(element.getSimpleName().toString());

        return model;
    } // process

    private boolean shouldBeAdded() {
        boolean isNotSkipped = element.getAnnotation(YabkSkip.class) == null;
        boolean isProtected = element.getModifiers().contains(Modifier.PROTECTED) || element.getModifiers().isEmpty();
        boolean isField = element.getKind() == ElementKind.FIELD;
        return isNotSkipped && isField && isProtected;
    }


    private boolean isYabkGenerated() {
        return element.getAnnotation(YabkGenerated.class) != null;
    }

    private String getPackage() {
        return isPrimitive() ? "" : isYabkGenerated() ? packageName : Utils.getPackage(element.asType().toString());
    }

    private boolean isPrimitive() {
        return element.asType().getKind().isPrimitive();
    }

    private String getFieldType() {
        String fieldType = element.asType().toString();
        return isPrimitive() ? fieldType : Utils.getClassName(fieldType);
    }
} // FieldProcessor
