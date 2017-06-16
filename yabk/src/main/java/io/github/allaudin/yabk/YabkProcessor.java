package io.github.allaudin.yabk;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class YabkProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        // round completed
        if (roundEnvironment.processingOver()) {
            printNote("Yabk round completed.");
            return true;
        }


        for (Element e : roundEnvironment.getElementsAnnotatedWith(YabkProcess.class)) {

            if (!e.getModifiers().contains(Modifier.ABSTRACT)) {
                printNote("skipping " + e.getSimpleName() + ", it is not abstract.");
                continue;
            }

            List<? extends Element> elements = ((TypeElement) e).getEnclosedElements();

            for (Element ee: elements){
                printNote(ee.getKind().toString());
            }

            printNote("processing " + e.toString());
        }


        return true;
    } // process

    private void printNote(String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, msg);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<String>() {
            {
                add("io.github.allaudin.yabk.YabkProcess");
                add("io.github.allaudin.yabk.TestAnno");
            }
        };
    } // getSupportedAnnotationTypes

} // YabkProcessor
