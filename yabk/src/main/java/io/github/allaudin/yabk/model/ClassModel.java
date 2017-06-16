package io.github.allaudin.yabk.model;

import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Set;

/**
 * Created on 6/16/17.
 *
 * @author M.Allaudin
 */

public final class ClassModel {

    private static final char DOLLAR_CHAR = "$".charAt(0);

    // type-field name
    private Set<FieldModel> fields;

    private String className;
    private String classPackage;

    public ClassModel(String className) {
        this.classPackage = getPackage(className);
        this.className = cleanClassName(getName(className));
        fields = new HashSet<>();
    }

    private String cleanClassName(String className) {
        return className.charAt(0) == DOLLAR_CHAR ? className.replaceFirst(String.valueOf(DOLLAR_CHAR), "") : "Yabk" + className;
    } // cleanClassName

    public void add(String type, String field) {

        final FieldModel fieldModel = new FieldModel();
        if (!type.contains(".")) {
            fieldModel.setFieldName(field);
        } else {
            fieldModel.setPackageName(getPackage(type));
            fieldModel.setFieldName(getName(type));
        }
        fields.add(fieldModel);
    }

    private String getPackage(String type) {
        return type.substring(0, type.lastIndexOf("."));
    }

    private String getName(String type) {
        return type.substring(type.lastIndexOf(".") + 1);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
