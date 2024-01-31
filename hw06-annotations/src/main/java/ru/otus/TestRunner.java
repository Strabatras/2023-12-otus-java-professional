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
import ru.otus.exceptions.BeforeTestException;

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
                | IllegalAccessException
                | BeforeTestException e) {
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
        AnnotatedMethod annotatedMethod = new AnnotatedMethod(method.getName());
        if (!Modifier.isPublic(method.getModifiers())) {
            testStructure.addBeforePrivateAnnotatedMethod(annotatedMethod);
            throw new BeforeTestException("Method '" + method.getName() + "' is not public");
        }
        testStructure.addBeforeAnnotatedMethod(annotatedMethod);
    }

    private static void prepareMethodWithAnnotationAfter(Method method, TestStructure testStructure) {
        Class<After> afterClass = After.class;
        if (!method.isAnnotationPresent(afterClass)) {
            return;
        }
        AnnotatedMethod annotatedMethod = new AnnotatedMethod(method.getName());
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
        AnnotatedMethod annotatedMethod = new AnnotatedMethod(method.getName());
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

    private static void invokeMethodBefore(Class<?> clazz, Object obj, List<AnnotatedMethod> annotatedMethodList) {
        for (AnnotatedMethod annotatedMethod : annotatedMethodList) {
            try {
                Method method = clazz.getMethod(annotatedMethod.getMethodName());
                method.invoke(obj);
            } catch (Exception e) {
                throw new BeforeTestException("Method " + annotatedMethod.getMethodName() + ": "
                        + e.getCause().getMessage());
            }
        }
    }

    private static void invokeMethod(
            Class<?> clazz, Object obj, AnnotatedMethod annotatedMethod, TestStatistics testStatistics)
            throws NoSuchMethodException, IllegalAccessException {
        Method method = clazz.getMethod(annotatedMethod.getMethodName());
        try {
            method.invoke(obj);
            testStatistics.addSuccessfulTestName(annotatedMethod.getMethodName());
        } catch (InvocationTargetException ite) {
            if (ite.getCause() instanceof AssertException) {
                testStatistics.addFailedTestName(annotatedMethod.getMethodName());
            } else {
                testStatistics.addErrorTestName(annotatedMethod.getMethodName());
            }
        }
    }

    private static void invokeMethodAfter(Class<?> clazz, Object obj, List<AnnotatedMethod> annotatedMethodList) {
        for (AnnotatedMethod annotatedMethod : annotatedMethodList) {
            try {
                Method method = clazz.getMethod(annotatedMethod.getMethodName());
                method.invoke(obj);
            } catch (Exception e) {
                logger.atError()
                        .setMessage("Method " + annotatedMethod.getMethodName() + ": "
                                + e.getCause().getMessage())
                        .log();
            }
        }
    }

    private static void executeTest(
            AnnotatedMethod annotatedMethod, TestStructure testStructure, TestStatistics testStatistics)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException,
                    IllegalAccessException, BeforeTestException {
        final Class<?> clazz = getClazz(testStructure.getClassName());
        Object obj = clazz.getDeclaredConstructor().newInstance();

        invokeMethodBefore(clazz, obj, testStructure.beforeAnnotatedMethodList());
        invokeMethod(clazz, obj, annotatedMethod, testStatistics);
        invokeMethodAfter(clazz, obj, testStructure.afterAnnotatedMethodList());
    }
}
