package io.github.allaudin.yabk;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import io.github.allaudin.yabk.generator.ClassGenerator;
import io.github.allaudin.yabk.generator.FieldGenerator;
import io.github.allaudin.yabk.model.ClassModel;
import io.github.allaudin.yabk.model.FieldModel;
import io.github.allaudin.yabk.processor.ClassProcessor;
import io.github.allaudin.yabk.processor.FieldProcessor;

public class YabkProcessor extends AbstractProcessor {


    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        // round completed
        if (roundEnvironment.processingOver()) {
            note("%s", "YABK round completed [Everything is OK]");
            return true;
        }


        for (Element e : roundEnvironment.getElementsAnnotatedWith(YabkProcess.class)) {


            boolean shouldSkip = !e.getModifiers().contains(Modifier.ABSTRACT) || !e.getKind().isClass();

            if (shouldSkip) {
                note("Skipping %s  [%s]", e.getKind(), e.getSimpleName());
                continue;
            }

            TypeElement type = (TypeElement) e;

            note("Processing %s", e.toString());

            String packageName = Utils.getPackage(type.getQualifiedName().toString());

            ClassModel classModel = ClassProcessor.newInstance(type).process();
            FieldGenerator fieldGenerator = FieldGenerator.getInstance();

            ClassGenerator classGenerator = new ClassGenerator(classModel, fieldGenerator);

            List<? extends Element> enclosedElements = type.getEnclosedElements();

            for (Element ee : enclosedElements) {
                FieldModel fieldModel = FieldProcessor.newInstance(packageName, ee).process();
                classGenerator.add(fieldModel);
            }

            try {
                classGenerator.getFile().writeTo(processingEnv.getFiler());
            } catch (Exception ioe) {
                ioe.printStackTrace();
                error(e, "%s", "Error while writing file");
            }

        } // end for


        return true;
    } // process

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    } // getSupportedSourceVersion


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
