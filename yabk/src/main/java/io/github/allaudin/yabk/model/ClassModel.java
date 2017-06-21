package io.github.allaudin.yabk.model;

import io.github.allaudin.yabk.Methods;

/**
 * Created on 6/21/17.
 *
 * @author M.Allaudin
 */

public class ClassModel {

    private Methods methods;
    private String className;
    private String parentClass;
    private String classPackage;
    private boolean nonNullStrings;

    public Methods getMethods() {
        return methods;
    }

    public void setMethods(Methods methods) {
        this.methods = methods;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getParentClass() {
        return parentClass;
    }

    public void setParentClass(String parentClass) {
        this.parentClass = parentClass;
    }

    public String getClassPackage() {
        return classPackage;
    }

    public void setClassPackage(String classPackage) {
        this.classPackage = classPackage;
    }

    public boolean isNonNullStrings() {
        return nonNullStrings;
    }

    public void setNonNullStrings(boolean nonNullStrings) {
        this.nonNullStrings = nonNullStrings;
    }
} // ClassModel
