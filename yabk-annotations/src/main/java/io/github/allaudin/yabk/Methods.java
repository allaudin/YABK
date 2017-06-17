package io.github.allaudin.yabk;

/**
 * Type of method to be generated.
 *
 * @author M.Allaudin
 */

public enum Methods {
    /**
     * Accessor methods only.
     */
    ACCESSORS,

    /**
     * Mutator methods only.
     */
    MUTATORS,

    /**
     * Both accessor and mutator methods.
     */
    BOTH
}
