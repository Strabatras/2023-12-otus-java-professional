package ru.otus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;
import ru.otus.exceptions.AssertException;

public class TestRunner {
    private static final Logger logger = LoggerFactory.getLogger(TestRunner.class);

    private TestRunner() {}

    public static void run(String className) {
        final TestStructure testStructure = new TestStructure(className);
        final TestStatistics testStatistics = new TestStatistics(className);
        try {
            prepareTestClass(testStructure);

            copyPrivateMethodNameToStatistic(testStructure, testStatistics);

            for (AnnotatedMethod testAnnotatedMethod : testStructure.testAnnotatedMethodList()) {
                executeTest(testAnnotatedMethod, testStructure, testStatistics);
            }
        } catch (ClassNotFoundException
                | NoSuchMethodException
                | InvocationTargetException
                | InstantiationException
                | IllegalAccessException e) {
            logger.atError().setMessage(e.getMessage()).log();
        } finally {
            testStatistics.printInfo();
        }
    }

    private static Class<?> getClazz(String className) throws ClassNotFoundException {
        return Class.forName(className);
    }

    private static void copyPrivateMethodNameToStatistic(TestStructure testStructure, TestStatistics testStatistics) {
        testStructure.beforePrivateAnnotatedMethodList().stream()
                .forEach(annotatedMethod -> testStatistics.addBeforePrivateTestName(annotatedMethod.getMethodName()));
        testStructure.afterPrivateAnnotatedMethodList().stream()
                .forEach(annotatedMethod -> testStatistics.addAfterPrivateTestName(annotatedMethod.getMethodName()));
        testStructure.testPrivateAnnotatedMethodList().stream()
                .forEach(annotatedMethod -> testStatistics.addTestPrivateTestName(annotatedMethod.getMethodName()));
    }

    private static void prepareMethodWithAnnotationBefore(Method method, TestStructure testStructure) {
        Class<Before> beforeClass = Before.class;
        if (!method.isAnnotationPresent(beforeClass)) {
            return;
        }
        AnnotatedMethod annotatedMethod = new AnnotatedMethod(method.getName(), beforeClass.getName());
        if (!Modifier.isPublic(method.getModifiers())) {
            testStructure.addBeforePrivateAnnotatedMethod(annotatedMethod);
            return;
        }
        testStructure.addBeforeAnnotatedMethod(annotatedMethod);
    }

    private static void prepareMethodWithAnnotationAfter(Method method, TestStructure testStructure) {
        Class<After> afterClass = After.class;
        if (!method.isAnnotationPresent(afterClass)) {
            return;
        }
        AnnotatedMethod annotatedMethod = new AnnotatedMethod(method.getName(), afterClass.getName());
        if (!Modifier.isPublic(method.getModifiers())) {
            testStructure.addAfterPrivateAnnotatedMethod(annotatedMethod);
            return;
        }
        testStructure.addAfterAnnotatedMethod(annotatedMethod);
    }

    private static void prepareMethodWithAnnotationTest(Method method, TestStructure testStructure) {
        Class<Test> testClass = Test.class;
        if (!method.isAnnotationPresent(testClass)) {
            return;
        }
        AnnotatedMethod annotatedMethod = new AnnotatedMethod(method.getName(), testClass.getName());
        if (!Modifier.isPublic(method.getModifiers())) {
            testStructure.addTestPrivateAnnotatedMethod(annotatedMethod);
            return;
        }
        testStructure.addTestAnnotatedMethod(annotatedMethod);
    }

    private static void prepareTestClass(TestStructure testStructure) throws ClassNotFoundException {
        final Class<?> clazz = getClazz(testStructure.getClassName());
        for (Method method : clazz.getDeclaredMethods()) {
            prepareMethodWithAnnotationBefore(method, testStructure);
            prepareMethodWithAnnotationAfter(method, testStructure);
            prepareMethodWithAnnotationTest(method, testStructure);
        }
    }

    private static void invokeMethod(
            Class<?> clazz, Object obj, List<AnnotatedMethod> annotatedMethodList, TestStatistics testStatistics)
            throws NoSuchMethodException, IllegalAccessException {
        for (AnnotatedMethod annotatedMethod : annotatedMethodList) {
            invokeMethod(clazz, obj, annotatedMethod, testStatistics);
        }
    }

    private static void invokeMethod(
            Class<?> clazz, Object obj, AnnotatedMethod annotatedMethod, TestStatistics testStatistics)
            throws NoSuchMethodException, IllegalAccessException {
        Method method = clazz.getMethod(annotatedMethod.getMethodName());
        try {
            method.invoke(obj);
            if (annotatedMethod.getAnnotationName().equals(Test.class.getName())) {
                testStatistics.addSuccessfulTestName(annotatedMethod.getMethodName());
            }
        } catch (InvocationTargetException ite) {
            if (ite.getCause() instanceof AssertException) {
                testStatistics.addFailedTestName(annotatedMethod.getMethodName());
            } else {
                testStatistics.addErrorTestName(annotatedMethod.getMethodName());
            }
        }
    }

    private static void executeTest(
            AnnotatedMethod annotatedMethod, TestStructure testStructure, TestStatistics testStatistics)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException,
                    IllegalAccessException {
        final Class<?> clazz = getClazz(testStructure.getClassName());
        Object obj = clazz.getDeclaredConstructor().newInstance();

        invokeMethod(clazz, obj, testStructure.beforeAnnotatedMethodList(), testStatistics);
        invokeMethod(clazz, obj, annotatedMethod, testStatistics);
        invokeMethod(clazz, obj, testStructure.afterAnnotatedMethodList(), testStatistics);
    }
}
