package ru.otus;

import java.util.ArrayList;
import java.util.List;

public class TestStructure {
    private String className;
    private List<AnnotatedMethod> beforeAnnotatedMethodList = new ArrayList<>();
    private List<AnnotatedMethod> afterAnnotatedMethodList = new ArrayList<>();
    private List<AnnotatedMethod> testAnnotatedMethodList = new ArrayList<>();
    private List<AnnotatedMethod> beforePrivateAnnotatedMethodList = new ArrayList<>();
    private List<AnnotatedMethod> afterPrivateAnnotatedMethodList = new ArrayList<>();
    private List<AnnotatedMethod> testPrivateAnnotatedMethodList = new ArrayList<>();

    public TestStructure(String className) {
        this.className = className;
    }

    public void addBeforeAnnotatedMethod(AnnotatedMethod annotatedMethod) {
        beforeAnnotatedMethodList.add(annotatedMethod);
    }

    public void addAfterAnnotatedMethod(AnnotatedMethod annotatedMethod) {
        afterAnnotatedMethodList.add(annotatedMethod);
    }

    public void addTestAnnotatedMethod(AnnotatedMethod annotatedMethod) {
        testAnnotatedMethodList.add(annotatedMethod);
    }

    public void addBeforePrivateAnnotatedMethod(AnnotatedMethod annotatedMethod) {
        beforePrivateAnnotatedMethodList.add(annotatedMethod);
    }

    public void addAfterPrivateAnnotatedMethod(AnnotatedMethod annotatedMethod) {
        afterPrivateAnnotatedMethodList.add(annotatedMethod);
    }

    public void addTestPrivateAnnotatedMethod(AnnotatedMethod annotatedMethod) {
        testPrivateAnnotatedMethodList.add(annotatedMethod);
    }

    public String getClassName() {
        return className;
    }

    public List<AnnotatedMethod> beforeAnnotatedMethodList() {
        return beforeAnnotatedMethodList;
    }

    public List<AnnotatedMethod> afterAnnotatedMethodList() {
        return afterAnnotatedMethodList;
    }

    public List<AnnotatedMethod> testAnnotatedMethodList() {
        return testAnnotatedMethodList;
    }

    public List<AnnotatedMethod> beforePrivateAnnotatedMethodList() {
        return beforePrivateAnnotatedMethodList;
    }

    public List<AnnotatedMethod> afterPrivateAnnotatedMethodList() {
        return afterPrivateAnnotatedMethodList;
    }

    public List<AnnotatedMethod> testPrivateAnnotatedMethodList() {
        return testPrivateAnnotatedMethodList;
    }
}
