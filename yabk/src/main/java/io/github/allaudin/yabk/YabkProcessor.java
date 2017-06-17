package io.github.allaudin.yabk;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import io.github.allaudin.yabk.generator.ClassGenerator;
import io.github.allaudin.yabk.generator.ClassMeta;

public class YabkProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        // round completed
        if (roundEnvironment.processingOver()) {
            note("%s", "YABK round completed [Everything is OK]");
            return true;
        }


        for (Element e : roundEnvironment.getElementsAnnotatedWith(YabkProcess.class)) {


            boolean shouldSkip = !e.getModifiers().contains(Modifier.ABSTRACT) || e.getKind() != ElementKind.CLASS;

            if (shouldSkip) {
                note("Skipping %s  [%s]", e.getKind(), e.getSimpleName());
                continue;
            }
            TypeElement type = (TypeElement) e;

            note("Processing %s", e.toString());

            ClassGenerator classGenerator = new ClassGenerator(new ClassMeta(type));

            List<? extends Element> enclosedElements = type.getEnclosedElements();

            for (Element ee : enclosedElements) {
                processField(classGenerator, ee);
            }

            try {
                classGenerator.getFile().writeTo(processingEnv.getFiler());
            } catch (Exception ioe) {
                ioe.printStackTrace();
                error(e, "%s", "Error while writing file [500]");
            }

        } // end for


        return true;
    } // process

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    } // getSupportedSourceVersion

    private void processField(ClassGenerator classGenerator, Element ee) {

        boolean isNotSkipped = ee.getAnnotation(YabkSkip.class) == null;
        boolean isProtected = ee.getModifiers().contains(Modifier.PROTECTED);
        boolean isField = ee.getKind() == ElementKind.FIELD;

        if (isField && isProtected && isNotSkipped) {
            String fieldType = ee.asType().toString();
            classGenerator.add(fieldType, ee.getSimpleName().toString());
        }
    } // processField

    private void note(String format, Object... objects) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, String.format(format + " ...", objects));
    }

    private void error(Element e, String format, Object... objects) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, String.format(format + " ...", objects), e);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(YabkProcess.class.getCanonicalName());
        return annotations;
    } // getSupportedAnnotationTypes

} // YabkProcessor
