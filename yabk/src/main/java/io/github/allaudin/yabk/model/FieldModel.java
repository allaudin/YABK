package io.github.allaudin.yabk.model;

/**
 * Model representing the field.
 *
 * @author M.Allaudin
 */

public class FieldModel {

    /**
     * Either <em>this field</em> should be added to generated class or not.
     * <p>
     * <b>Note:</b> Processor may through exception if a field which is not supposed to add is added.
     * <p>
     * Always check this while adding field to class.
     *
     * @see io.github.allaudin.yabk.generator.ClassGenerator#add(FieldModel)
     */
    private boolean shouldBeAdded;



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

    public boolean isShouldBeAdded() {
        return shouldBeAdded;
    }

    public void setShouldBeAdded(boolean shouldBeAdded) {
        this.shouldBeAdded = shouldBeAdded;
    }

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

    public boolean canBeAddedToParcel(){
        return isPrimitive() || isParcelable() || isString();
    }
} // FieldModel
