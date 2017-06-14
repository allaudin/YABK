package io.github.allaudin.yabk;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class YabkProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        // round completed
        if (roundEnvironment.processingOver()) {
            return true;
        }


        List<TypeElement> types = new ArrayList<>();

        for (TypeElement e : set) {
            types.add(e);
        }

        String str = "";
        return true;
    } // process

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<String>() {
            {
                add("io.github.allaudin.yabk.YabkProcess");
            }
        };
    } // getSupportedAnnotationTypes

} // YabkProcessor
