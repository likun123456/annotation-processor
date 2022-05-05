# annotation-processor
编译时注解处理
创建注解@Serializable，只能注解在class上，注解之后该类必须实现Serializable接口，否则编译不通过
创建注解处理器
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
