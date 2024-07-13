package ru.otus.common;

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;

@UtilityClass
public final class Util {

    public static void secondsToSleep(long second, Logger logger) {
        try {
            Thread.sleep(second * 1000);
        } catch (InterruptedException e) {
            logger.atError().setCause(e).log();
        }
    }
}
