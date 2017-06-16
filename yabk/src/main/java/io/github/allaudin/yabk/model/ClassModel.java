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

    // type-field name
    private Set<FieldModel> fields;


    public ClassModel() {
        fields = new HashSet<>();
    }

    public void add(String type, String field) {

        final FieldModel fieldModel = new FieldModel();
        if (!type.contains(".")) {
            fieldModel.setFieldName(field);
        } else {
            fieldModel.setPackageName(type.substring(0, type.lastIndexOf(".")));
            fieldModel.setFieldName(type.substring(type.lastIndexOf(".") + 1));
        }
        fields.add(fieldModel);
    }


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
