package com.bruce.annotation.processor;

import com.bruce.annotation.Serializable;
import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.util.List;
import java.util.Set;

/**
 * <p>description<p/>
 *
 * @author likun
 * @date： 2022/4/29 17:35
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.bruce.annotation.Serializable"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class SerializableProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Messager messager = processingEnv.getMessager();
        Elements elementUtils = processingEnv.getElementUtils();
        for (Element element : roundEnv.getElementsAnnotatedWith(Serializable.class)) {
            if (element.getKind() != ElementKind.CLASS) {
                messager.printMessage(Diagnostic.Kind.ERROR,
                        String.format("Only classes can be annotated with @%s", Serializable.class.getSimpleName()));
                return true;
            }
            TypeElement typeElement = (TypeElement) element;
            List<? extends TypeMirror> interfaces = typeElement.getInterfaces();
            if (interfaces == null || interfaces.size() == 0) {
                messager.printMessage(Diagnostic.Kind.ERROR,
                        String.format("%s必须实现Serializable接口", typeElement.getSimpleName()));
                return true;
            }

            boolean anyMatch = interfaces.stream().anyMatch(e -> e.toString().equals(java.io.Serializable.class.getName()));
            if (!anyMatch) {
                messager.printMessage(Diagnostic.Kind.ERROR,
                        String.format("%s必须实现Serializable接口!", typeElement.getSimpleName()));
                return true;
            }
        }
        return false;
    }
}
