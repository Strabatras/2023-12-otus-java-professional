package ru.otus;

import static ru.otus.Assert.assertEquals;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

@SuppressWarnings("java:S1144")
public class DemoTest {
    private final Logger logger;

    public DemoTest() {
        logger = LoggerFactory.getLogger(DemoTest.class);
    }

    @Before
    public void up() {
        setLoggerInfoMessage("Method UP. Annotation Before");
    }

    private void upPrivate() {
        setLoggerInfoMessage("Method UP private. Annotation Before");
    }

    @After
    public void down() {
        setLoggerInfoMessage("Method DOWN. Annotation After");
    }

    @Test
    public void oneTest() {
        setLoggerInfoMessage("Test One - int values");
        int expected = 20;
        int actual = 200 * 2 / 40 / 5 * 10;
        assertEquals(expected, actual);
    }

    @Test
    public void twoTest() {
        setLoggerInfoMessage("Test Two - string values");
        String expected = "My name is Earl";
        String actual = "My name is Earl";
        assertEquals(expected, actual);
    }

    @Test
    public void threeTest() {
        setLoggerInfoMessage("Test Three - ArithmeticException");
        int a = 100;
        int b = 20;
        int c = a / (b - 10 * 2);
        assertEquals(20, c);
    }

    @Test
    public void fiveTest() {
        setLoggerInfoMessage("Test Five - string values are not equal");
        String expected = "My name is Jony Cash";
        String actual = "I am Johnny Rebel";
        assertEquals(expected, actual);
    }

    @Test
    private void privateTest() {
        setLoggerInfoMessage("Private Test - will not start");
        assertEquals(100, 100);
    }

    private void setLoggerInfoMessage(String message) {
        logger.atInfo().setMessage(message).log();
    }
}
