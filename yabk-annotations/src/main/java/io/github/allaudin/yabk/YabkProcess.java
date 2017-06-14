package io.github.allaudin.yabk;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for registering a class with YABK to process.
 * <p>
 * By default, YABK expects <b>abstract</b> classes with name, starting with <b>$</b> sign and
 * generates classes with same name <em>by removing <b>$</b> sign</em> .e.g.
 * <p>
 * YABK generates <b>{@code SomeModel}</b> class for a class <b>{@code $SomeModel}</b>, observe the <b>$</b>
 * sign in second name.
 * <p>
 * <b>$</b> sign is just a configuration and it doesn't limit your ability to produce
 * classes with name of your own choice.
 * <p>
 * For generating classes with different name, pass <b>generated class name</b> to {@link YabkProcess} annotation. e.g.
 * <pre>{@code @YabkProcess(genClassName = "MyGeneratedClass")}</pre>
 *
 * Above annotation will generate a class with name {@code MyGeneratedClass} for a given class.
 *
 * @author M.Allaudin
 */

@Retention(RetentionPolicy.CLASS)
public @interface YabkProcess {

    /**
     * Generated class name for this class.
     *
     * @return classname - classname for generated file, default if not specified.
     */
    String genClassName() default "";
}
