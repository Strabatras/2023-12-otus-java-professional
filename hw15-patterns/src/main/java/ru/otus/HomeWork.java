package ru.otus;

import static ru.otus.util.ExceptionUtil.complexProcessorExceptionHandler;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.Processor;
import ru.otus.processor.homework.ProcessorEvenSecondException;
import ru.otus.processor.homework.ProcessorField11Field12Changer;

@SuppressWarnings({"java:S106"})
public class HomeWork {

    /*
    Реализовать to do:
      1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
      2. Сделать процессор, который поменяет местами значения field11 и field12
      3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
            Секунда должна определяьться во время выполнения.
            Тест - важная часть задания
            Обязательно посмотрите пример к паттерну Мементо!
      4. Сделать Listener для ведения истории (подумайте, как сделать, чтобы сообщения не портились)
         Уже есть заготовка - класс HistoryListener, надо сделать его реализацию
         Для него уже есть тест, убедитесь, что тест проходит
    */

    public static void main(String[] args) {
        /*
          по аналогии с Demo.class
          из элеменов "to do" создать new ComplexProcessor и обработать сообщение
        */
        List<Processor> processors =
                List.of(new ProcessorField11Field12Changer(), new ProcessorEvenSecondException(LocalDateTime::now));
        var complexProcessor = new ComplexProcessor(processors, ex -> complexProcessorExceptionHandler(ex));
        var historyListener = new HistoryListener();

        complexProcessor.addListener(historyListener);

        List<String> data = Arrays.asList("Hello", "გამარჯობა", "Hej", "Kia ora");

        ObjectForMessage field13 = new ObjectForMessage();
        field13.setData(data);

        var message = new Message.Builder(1L)
                .field11("field11")
                .field12("field12")
                .field13(field13)
                .build();

        var result = complexProcessor.handle(message);
        System.out.println("result:" + result);

        complexProcessor.removeListener(historyListener);

        try {
            var historyMessage = historyListener
                    .findMessageById(1L)
                    .orElseThrow(() -> new RuntimeException("History message with id #1 not found"));
            System.out.println("History message : " + historyMessage.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
