package ru.otus.listener.homework;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import ru.otus.listener.Listener;
import ru.otus.model.Message;

public class HistoryListener implements Listener, HistoryReader {

    private final Map<Long, Message> messageHistory = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        if (nonNull(msg)) {
            messageHistory.put(msg.getId(), msg);
        }
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return ofNullable(messageHistory.get(id));
    }
}
