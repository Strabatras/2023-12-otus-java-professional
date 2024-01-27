package ru.otus;

public class AnnotatedMethod {
    private final String methodName;
    private final String annotationName;

    public AnnotatedMethod(String methodName, String annotationName) {
        this.methodName = methodName;
        this.annotationName = annotationName;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getAnnotationName() {
        return annotationName;
    }
}
