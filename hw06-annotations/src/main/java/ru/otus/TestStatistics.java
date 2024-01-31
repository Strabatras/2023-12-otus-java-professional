package ru.otus;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestStatistics {
    private final String className;
    private List<String> successfulTestNamelList = new ArrayList<>();
    private List<String> failedTestNameList = new ArrayList<>();
    private List<String> errorTestNameList = new ArrayList<>();

    private List<String> beforePrivateTestNameList = new ArrayList<>();
    private List<String> afterPrivateTestNameList = new ArrayList<>();
    private List<String> testPrivateTestNameList = new ArrayList<>();

    private static final Logger logger = LoggerFactory.getLogger(TestStatistics.class);

    public TestStatistics(String className) {
        this.className = className;
    }

    public void addSuccessfulTestName(String testName) {
        successfulTestNamelList.add(testName);
    }

    public void addFailedTestName(String testName) {
        failedTestNameList.add(testName);
    }

    public void addErrorTestName(String testName) {
        errorTestNameList.add(testName);
    }

    public void addBeforePrivateTestName(String testName) {
        beforePrivateTestNameList.add(testName);
    }

    public void addAfterPrivateTestName(String testName) {
        afterPrivateTestNameList.add(testName);
    }

    public void addTestPrivateTestName(String testName) {
        testPrivateTestNameList.add(testName);
    }

    public void printInfo() {
        logger.atInfo().setMessage("\n").log();
        logger.atInfo()
                .setMessage("==========================================================")
                .log();
        logger.atInfo().setMessage("Tested class name : " + className).log();
        logger.atInfo().setMessage("- Test results").log();
        logger.atInfo()
                .setMessage("--- Successful test : " + String.join(", ", successfulTestNamelList))
                .log();
        logger.atInfo()
                .setMessage("--- Failed test : " + String.join(", ", failedTestNameList))
                .log();
        logger.atInfo()
                .setMessage("--- Interrupted test : " + String.join(", ", errorTestNameList))
                .log();
        logger.atInfo()
                .setMessage("==========================================================")
                .log();
    }
}
