package io.github.allaudin.yabk.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Model representing the field.
 *
 * @author M.Allaudin
 */

public class FieldModel {


    /**
     * Package name of this field
     */
    private String packageName;

    /**
     * Field name of this field
     */
    private String fieldName;

    /**
     * Type of this field
     */
    private String fieldType;

    /**
     * Set if field type is primitive or not
     */
    private boolean isPrimitive;

    /**
     * Typed list
     */
    private boolean isParcelableTypedList;

    /**
     * Filed is parcelable or not e.g. implements {@code android.os.Parcelable} interface
     */
    private boolean isParcelable;


    /**
     * True if current field type is List of Strings
     */
    private boolean isStringList;

    /**
     * Is string type
     */
    private boolean isString;


    /**
     * True if type is List
     */
    private boolean isList;

    /**
     * Is generic type
     */
    private boolean isGeneric;


    /**
     * Actual type info if this is generic type
     */
    private ActualTypeInfo actualTypeInfo;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public boolean isPrimitive() {
        return isPrimitive;
    }

    public void setPrimitive(boolean primitive) {
        isPrimitive = primitive;
    }

    public boolean isParcelable() {
        return isParcelable;
    }

    public void setParcelable(boolean parcelable) {
        isParcelable = parcelable;
    }

    public boolean isStringList() {
        return isStringList;
    }

    public void setStringList(boolean stringList) {
        isStringList = stringList;
    }

    public boolean isString() {
        return isString;
    }

    public void setString(boolean string) {
        isString = string;
    }

    public boolean canBeAddedToParcel() {
        return isPrimitive() || isParcelable() || isString() || isParcelableTypedList();
    }

    public boolean isParcelableTypedList() {
        return isParcelableTypedList;
    }

    public void setParcelableTypedList(boolean parcelableTypedList) {
        isParcelableTypedList = parcelableTypedList;
    }

    public ActualTypeInfo getActualTypeInfo() {
        return actualTypeInfo;
    }

    public void setActualTypeInfo(ActualTypeInfo actualTypeInfo) {
        this.actualTypeInfo = actualTypeInfo;
    }

    public boolean isList() {
        return isList;
    }

    public void setList(boolean list) {
        isList = list;
    }

    public boolean isGeneric() {
        return isGeneric;
    }

    public void setGeneric(boolean generic) {
        isGeneric = generic;
    }

    public static class ActualTypeInfo {
        private List<FieldModel> actualTypes;

        public ActualTypeInfo() {
            actualTypes = new LinkedList<>();
        }

        public void add(FieldModel model) {
            actualTypes.add(model);
        }

        public List<FieldModel> getActualTypes() {
            return actualTypes;
        }

        @Override
        public String toString() {
            return "{" +
                    "actualTypes=" + actualTypes +
                    '}';
        }
    } // ActualTypeInfo

    @Override
    public String toString() {
        return "{" +
                "packageName='" + packageName + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", fieldType='" + fieldType + '\'' +
                ", isPrimitive=" + isPrimitive +
                ", isParcelableTypedList=" + isParcelableTypedList +
                ", isParcelable=" + isParcelable +
                ", isStringList=" + isStringList +
                ", isString=" + isString +
                ", isList=" + isList +
                ", isGeneric=" + isGeneric +
                '}';
    }
} // FieldModel
