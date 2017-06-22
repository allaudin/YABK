package io.github.allaudin.yabk;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

/**
 * Created on 6/22/17.
 *
 * @author M.Allaudin
 */

public final class YabkLogger {

    private static Messager instance;

    private YabkLogger() {
        throw new AssertionError("Can't instantiate YabkLogger");
    }

    public static void init(Messager messager) {
        instance = messager;
    }

    public static void note(String format, Object... objects) {
        instance.printMessage(Diagnostic.Kind.NOTE, String.format(format, objects));
    }

    public static void error(Element e, String format, Object... objects) {
        instance.printMessage(Diagnostic.Kind.ERROR, String.format(format, objects), e);
    }

}
