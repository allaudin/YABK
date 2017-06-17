package io.github.allaudin.yabk;

/**
 * Created on 6/17/17.
 *
 * @author M.Allaudin
 */

public final class Utils {

    private Utils() {
        throw new AssertionError("Can't instantiate " + Utils.class.getSimpleName());
    }

    public static String getClassName(String type) {
        if (type.contains(".")) {
            return type.substring(type.lastIndexOf(".") + 1);
        }
        return type;
    } // getName

    public static String getPackage(String type) {
        if (type.contains(".")) {
            return type.substring(0, type.lastIndexOf("."));
        }
        return type;
    }

} // Utils
