package io.github.allaudin.yabk.processor;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;

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

    public FieldModel process() {
        final FieldModel model = new FieldModel();
        model.setShouldBeAdded(shouldBeAdded());

        model.setGeneratedByYabk(isYabkGenerated());
        model.setPrimitive(isPrimitive());
        model.setFieldType(getFieldType());
        model.setPackageName(getPackage());

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
        return isPrimitive() ? "" : Utils.getPackage(element.asType().toString());
    }

    private boolean isPrimitive() {
        return element.asType().getKind().isPrimitive();
    }

    private String getFieldType() {
        String fieldType = element.asType().toString();

        if (isPrimitive()) {
            return fieldType;
        }

        return isYabkGenerated() ? packageName + "." + fieldType : Utils.getClassName(fieldType);
    }
} // FieldProcessor
