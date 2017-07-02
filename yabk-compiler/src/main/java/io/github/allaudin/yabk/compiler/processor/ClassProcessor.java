package io.github.allaudin.yabk.compiler.processor;

import javax.lang.model.element.TypeElement;

import io.github.allaudin.yabk.compiler.Utils;
import io.github.allaudin.yabk.YabkProcess;
import io.github.allaudin.yabk.compiler.model.ClassModel;

/**
 * Class processor
 *
 * @author M.Allaudin
 */

public class ClassProcessor {

    /**
     * TypeElement of this class
     */
    private TypeElement element;

    private ClassProcessor(TypeElement element) {
        this.element = element;
    }

    public static ClassProcessor newInstance(TypeElement element) {
        return new ClassProcessor(element);
    }

    public ClassModel process() {
        ClassModel model = new ClassModel();

        String type = element.getQualifiedName().toString();
        model.setClassPackage(Utils.getPackage(type));
        model.setParentClass(Utils.getClassName(type));

        YabkProcess yabkProcess = element.getAnnotation(YabkProcess.class);

        if (yabkProcess.className().length() == 0) {
            model.setClassName(cleanClassName(Utils.getClassName(type)));
        } else {
            model.setClassName(yabkProcess.className());
        }

        model.setMethods(yabkProcess.methods());
        model.setNonNullStrings(yabkProcess.nonNullStrings());

        return model;
    }

    private String cleanClassName(String className) {
        return className.charAt(0) == "$".charAt(0) ? className.replaceFirst("\\$", "") : "Yabk" + className;
    } // cleanClassName

}
