package io.github.allaudin.yabk.compiler;

/**
 * Utility class for YABK processor.
 *
 * @author M.Allaudin
 */

public final class Utils {

    private Utils() {
        throw new AssertionError("Can't instantiate " + Utils.class.getSimpleName());
    }


    /**
     * Get class name from given type
     *
     * @param type type of field
     * @return string - class name without package if it fully qualified name, same name otherwise
     */
    public static String getClassName(String type) {
        if (type.contains(".")) {
            return type.substring(type.lastIndexOf(".") + 1);
        }
        return type;
    } // getName

    /**
     * Get package name from given type
     *
     * @param type type of field
     * @return string - package name if available, same field otherwise
     */
    public static String getPackage(String type) {
        if (type.contains(".")) {
            return type.substring(0, type.lastIndexOf("."));
        }
        return type;
    }



} // Utils
