package ru.otus.processor.homework;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.exception.EvenSecondException;
import ru.otus.model.Message;
import ru.otus.provider.homework.DateTimeProvider;

@DisplayName("Процессор")
class ProcessorEvenSecondExceptionTest {
    private LocalDateTime localDateTime;
    private DateTimeProvider dateTimeProvider;
    private ProcessorEvenSecondException processor;

    @BeforeEach
    void setUp() {
        localDateTime = mock(LocalDateTime.class);
        dateTimeProvider = mock(DateTimeProvider.class);
        processor = new ProcessorEvenSecondException(this.dateTimeProvider);
    }

    @DisplayName("выбрасывает исключение на четную секунду")
    @Test
    void shouldReturnEvenSecondExceptionWhenEvenSecond() {
        final int evenSecond = 0;
        final String expected = format("even second : %d", evenSecond);

        when(localDateTime.getSecond()).thenReturn(evenSecond);
        when(dateTimeProvider.get()).thenReturn(localDateTime);
        Message message = new Message.Builder(1L).build();

        final var exception = assertThrows(EvenSecondException.class, () -> processor.process(message));

        assertEquals(expected, exception.getMessage());

        verify(dateTimeProvider, times(1)).get();
        verify(localDateTime, times(1)).getSecond();
    }

    @DisplayName("возвращает корректный объект на не четную секунду")
    @Test
    void shouldReturnCorrectMessageWhenNotEvenSecond() {
        final int evenSecond = 11;
        when(localDateTime.getSecond()).thenReturn(evenSecond);
        when(dateTimeProvider.get()).thenReturn(localDateTime);
        Message message =
                new Message.Builder(1L).field1("field1").field11("field11").build();

        var actual = processor.process(message);

        assertEquals(message, actual);

        verify(dateTimeProvider, times(1)).get();
        verify(localDateTime, times(1)).getSecond();
    }
}
