package io.github.allaudin.yabk.generator;

/**
 * Generate Parcelable implemenation of class
 *
 * @author M.Allaudin
 */

public class ParcelGenerator {

    private static final ParcelGenerator instance = new ParcelGenerator();

    private ParcelGenerator() {
    }

    public static ParcelGenerator getInstance() {
        return instance;
    }

}
