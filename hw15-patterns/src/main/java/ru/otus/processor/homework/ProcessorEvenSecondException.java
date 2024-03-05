package ru.otus.processor.homework;

import ru.otus.exception.EvenSecondException;
import ru.otus.model.Message;
import ru.otus.processor.Processor;
import ru.otus.provider.homework.DateTimeProvider;

@SuppressWarnings({"java:S2166"})
public class ProcessorEvenSecondException implements Processor {

    private final DateTimeProvider dateTimeProvider;

    public ProcessorEvenSecondException(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        int second = dateTimeProvider.get().getSecond();
        if (second % 2 == 0) {
            throw new EvenSecondException("even second : " + second);
        }
        return message;
    }
}
