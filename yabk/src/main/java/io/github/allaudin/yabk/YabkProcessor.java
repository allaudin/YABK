package io.github.allaudin.yabk;

import java.util.LinkedHashSet;
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

import io.github.allaudin.yabk.model.ClassMetaModel;
import io.github.allaudin.yabk.model.ClassModel;

@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class YabkProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        // round completed
        if (roundEnvironment.processingOver()) {
            note("%s", "YABK round completed [Everything is OK]");
            return true;
        }


        for (Element e : roundEnvironment.getElementsAnnotatedWith(YabkProcess.class)) {

            TypeElement type = (TypeElement) e;

            // should be abstract class
            if (!e.getModifiers().contains(Modifier.ABSTRACT) || e.getKind() != ElementKind.CLASS) {
                note("Skipping %s  [%s]", e.getKind(), type.getQualifiedName().toString());
                continue;
            }

            note("Processing %s", e.toString());


            List<? extends Element> enclosedElements = type.getEnclosedElements();

            ClassModel classModel = new ClassModel(new ClassMetaModel(type));


            for (Element ee : enclosedElements) {
                processField(classModel, ee);
            }

            try {
                classModel.getFile().writeTo(processingEnv.getFiler());
            } catch (Exception ioe) {
                ioe.printStackTrace();
                error(e, "%s", "Error while writing file [500]");
            }

        } // end for


        return true;
    } // process

    private void processField(ClassModel classModel, Element ee) {

        boolean isNotSkipped = ee.getAnnotation(YabkSkip.class) == null;
        boolean isProtected = ee.getModifiers().contains(Modifier.PROTECTED);
        boolean isField = ee.getKind() == ElementKind.FIELD;

        if (isField && isProtected && isNotSkipped) {
            String fieldType = ee.asType().toString();
            classModel.add(fieldType, ee.getSimpleName().toString());
        }
    } // processField

    private void note(String format, Object... objects) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, String.format(format + " ...\n\n", objects));
    }

    private void error(Element e, String format, Object... objects) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, String.format(format + " ...\n\n", objects), e);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(YabkProcess.class.getCanonicalName());
        return annotations;
    } // getSupportedAnnotationTypes

} // YabkProcessor
