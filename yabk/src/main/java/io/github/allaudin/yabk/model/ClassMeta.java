package io.github.allaudin.yabk.model;

import javax.lang.model.element.TypeElement;

import io.github.allaudin.yabk.Utils;
import io.github.allaudin.yabk.YabkProcess;

/**
 * Created on 6/17/17.
 *
 * @author M.Allaudin
 */

public class ClassMeta {

    private String className;
    private String classPackage;
    private String parentClass;

    public ClassMeta(TypeElement e) {
        String type = e.getQualifiedName().toString();
        parentClass = Utils.getClassName(type);
        classPackage = Utils.getPackage(type);

        YabkProcess yabkProcess = e.getAnnotation(YabkProcess.class);
        if (yabkProcess.genClassName().length() == 0) {
            className = cleanClassName(Utils.getClassName(type));
        } else {
            className = yabkProcess.genClassName();
        }
    }

    private String cleanClassName(String className) {
        return className.charAt(0) == "$".charAt(0) ? className.replaceFirst("\\$", "") : "Yabk" + className;
    } // cleanClassName

    public String getClassName() {
        return className;
    }

    public String getClassPackage() {
        return classPackage;
    }

    public String getParentClass() {
        return parentClass;
    }
} // ClassMeta
