package io.github.allaudin.yabk.model;

import javax.lang.model.element.TypeElement;

import io.github.allaudin.yabk.Methods;
import io.github.allaudin.yabk.Utils;
import io.github.allaudin.yabk.YabkProcess;

/**
 * Created on 6/17/17.
 *
 * @author M.Allaudin
 */

public class ClassMetaModel {

    private String className;
    private String classPackage;
    private String parentClass;
    private Methods methods;

    private boolean nonNullStrings;

    public ClassMetaModel(TypeElement e) {
        String type = e.getQualifiedName().toString();
        parentClass = Utils.getClassName(type);
        classPackage = Utils.getPackage(type);

        YabkProcess yabkProcess = e.getAnnotation(YabkProcess.class);
        if (yabkProcess.className().length() == 0) {
            className = cleanClassName(Utils.getClassName(type));
        } else {
            className = yabkProcess.className();
        }

        methods = yabkProcess.methods();
        nonNullStrings = yabkProcess.nonNullStrings();
    } // ClassMetaModel

    private String cleanClassName(String className) {
        return className.charAt(0) == "$".charAt(0) ? className.replaceFirst("\\$", "") : "Yabk" + className;
    } // cleanClassName

    String getClassName() {
        return className;
    }

    String getClassPackage() {
        return classPackage;
    }

    String getParentClass() {
        return parentClass;
    }

    Methods getMethods() {
        return methods;
    }

    boolean nonNullStrings() {
        return nonNullStrings;
    }
} // ClassMetaModel
