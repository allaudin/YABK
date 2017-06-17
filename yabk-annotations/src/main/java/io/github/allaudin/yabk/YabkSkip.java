package io.github.allaudin.yabk;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created on 6/17/17.
 *
 * @author M.Allaudin
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface YabkSkip {
}
