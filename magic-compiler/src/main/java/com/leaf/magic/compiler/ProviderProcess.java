package com.leaf.magic.compiler;

import com.google.auto.service.AutoService;
import com.leaf.magic.annotation.Provider;
import com.leaf.magic.annotation.ProviderInfo;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import static javax.lang.model.element.Modifier.PUBLIC;

@AutoService(Processor.class)
public class ProviderProcess extends AbstractProcessor {
    private static final String MAGIC_PKG_NAME_PROVIDER = "com.leaf.magic.provider";
    private static final String MAGIC_PROVIDER_NAME = "com.leaf.magic.api.template.IProvider";
    private ProcessingEnvironment mProcessingEnvironment;
    private Messager mMessager;
    private Elements mElementsUtils;
    private String moduleName = "module";

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.mProcessingEnvironment = processingEnvironment;
        mElementsUtils = processingEnvironment.getElementUtils();
        mMessager = processingEnvironment.getMessager();
        Map<String, String> options = processingEnvironment.getOptions();
        if (options != null) {
            moduleName = options.get("moduleName");
            mMessager.printMessage(Diagnostic.Kind.NOTE,
                    "EasyRouterProcessor moduleName = " + moduleName);
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<Element> elementSet = (Set<Element>) roundEnvironment.getElementsAnnotatedWith(Provider.class);
        MethodSpec.Builder builder = MethodSpec.methodBuilder("loadInfo")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Map.class, "infoMap", Modifier.FINAL)
                .addAnnotation(Override.class)
                .returns(void.class);

        for (Element element : elementSet) {
            TypeElement typeElement = (TypeElement) element;
            String fullName = typeElement.getQualifiedName().toString();
            Provider provider = typeElement.getAnnotation(Provider.class);
            int type = provider.type();
            TypeName typeName = ClassName.get(getProvider(provider));
            String key = typeName.toString();
            if (type > 0) {
                key = key + "." + type;
            }
            builder.addStatement("infoMap.put($S,new $T($L,$S))", key, ProviderInfo.class, type, fullName);
        }

        MethodSpec providerMethod = builder.build();

        TypeElement providerElement = mElementsUtils.getTypeElement(MAGIC_PROVIDER_NAME);
        ClassName iProvider = ClassName.get(providerElement);
        String className = "Provider$$" + moduleName;
        TypeSpec providerClass = TypeSpec.classBuilder(className)
                .addModifiers(PUBLIC)
                .addMethod(providerMethod)
                .addSuperinterface(iProvider)
                .build();
        JavaFile javaFile = JavaFile.builder(MAGIC_PKG_NAME_PROVIDER, providerClass)
                .build();
        try {
            javaFile.writeTo(mProcessingEnvironment.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(Provider.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }

    private TypeMirror getProvider(Provider annotation) {
        try {
            annotation.provider();
        } catch (MirroredTypeException mte) {
            return mte.getTypeMirror();
        }
        return null;
    }
}
