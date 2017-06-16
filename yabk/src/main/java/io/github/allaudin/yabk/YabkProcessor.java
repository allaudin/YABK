package io.github.allaudin.yabk;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import io.github.allaudin.yabk.model.ClassModel;

@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class YabkProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        // round completed
        if (roundEnvironment.processingOver()) {
            note("%s", "YABK round completed");
            return true;
        }


        for (Element e : roundEnvironment.getElementsAnnotatedWith(YabkProcess.class)) {

            if (!e.getModifiers().contains(Modifier.ABSTRACT)) {
                note("Skipping non-abstract class [%s]", e.getSimpleName());
                continue;
            }
            note("Processing %s", e.toString());

            TypeElement type = (TypeElement) e;
            List<? extends Element> enclosedElements = type.getEnclosedElements();

            ClassModel classModel = new ClassModel(type.getQualifiedName().toString());


            for (Element ee : enclosedElements) {

                boolean isProtectedOrPrivate = ee.getModifiers().contains(Modifier.PROTECTED) || ee.getModifiers().contains(Modifier.PRIVATE);
                if (ee.getKind() == ElementKind.FIELD && isProtectedOrPrivate) {
                    note("%s", ee.getSimpleName());
                    String fieldType = ee.asType().toString();
                    classModel.add(fieldType, ee.getSimpleName().toString());
                } // end if
            }

            try {
                classModel.getFile().writeTo(processingEnv.getFiler());
            } catch (Exception ioe) {
                ioe.printStackTrace();
                error("%s", "Error while writing file.");
            }
            note("%s", classModel.toString());
        } // end for


        return true;
    } // process

    private void note(String format, Object... objects) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, String.format(format + " ...", objects));
    }

    private void error(String format, Object... objects) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, String.format(format + " ...", objects));
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<String>() {
            {
                add("io.github.allaudin.yabk.YabkProcess");
            }
        };
    } // getSupportedAnnotationTypes

} // YabkProcessor
