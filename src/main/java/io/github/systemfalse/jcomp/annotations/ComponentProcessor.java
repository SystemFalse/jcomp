package io.github.systemfalse.jcomp.annotations;

import com.sun.source.util.Trees;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * {@code ComponentProcessor} is annotation processor that can generate new classes
 * (components and component types) and edit existing classes for optimizing
 * component usage. Example: {@code component.get("name")} will be transformed into
 * {@code component.getName()}.
 */
@SupportedAnnotationTypes("io.github.systemfalse.jcomp.annotations.ComponentRef")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class ComponentProcessor extends AbstractProcessor {
    private Trees trees;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        trees = Trees.instance(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return true;
    }
}
