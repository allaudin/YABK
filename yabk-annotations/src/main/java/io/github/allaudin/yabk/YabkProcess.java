package io.github.allaudin.yabk;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
 * <p>
 * <b>Note 1:</b> For classes without {@code $} sign and no class name declared in {@link YabkProcess} annotation, this
 * processor generates classes with name <em>prefixed with {@code Yabk}</em> e.g.
 *
 * For {@code MyModel} class, generated class name will be <b>{@code YabkMyModel}</b>
 * </p>
 *
 * <p><b>Note 2:</b> {@link YabkProcess} annotation on <em>non-abstract</em> classes is simple ignored. No class is generated
 * for non-abstract classes.</p>
 *
 * @author M.Allaudin
 */


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface YabkProcess {

    /**
     * Generated class name for this class.
     *
     * @return classname - classname for generated file, default if not specified.
     */
    String genClassName() default "";

    boolean accessorOnly() default false;
    boolean mutatorOnly() default false;

    // TODO: 6/17/17 check if class if final - skip in this case 
    // TODO: 6/17/17 check if fields are package private or not 
    // TODO: 6/17/17 check if annotation is declared on class or not 
    // TODO: 6/17/17 add annotations like setteronly, getteronly or skip anno for fields.
}
