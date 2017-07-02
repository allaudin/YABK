package io.github.allaudin.yabk.compiler.model;

import io.github.allaudin.yabk.Methods;

/**
 * Model for class type
 *
 * @author M.Allaudin
 */

public class ClassModel {

    /**
     * Stores type of methods to be generated for  this class
     */
    private Methods methods;

    /**
     * Classname of generated class
     */
    private String className;

    /**
     * Parent class of generated class
     */
    private String parentClass;

    /**
     * Package name for generated class
     */
    private String classPackage;

    /**
     * Either to add non-null check on strings or not.
     * <p>
     * If true, all strings will be checked for null type and if any string is null, empty string is returned
     * from accessor.
     */
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
