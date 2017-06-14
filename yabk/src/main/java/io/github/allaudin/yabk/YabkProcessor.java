package io.github.allaudin.yabk;

import java.util.HashSet;
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
        String str = "";
        return false;
    } // process

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<String>() {
            {
                add("io.github.allaudin.yabk.Allaudin");
            }
        };
    } // getSupportedAnnotationTypes

    @Override
    public Set<String> getSupportedOptions() {
        return super.getSupportedOptions();
    }

} // YabkProcessor
