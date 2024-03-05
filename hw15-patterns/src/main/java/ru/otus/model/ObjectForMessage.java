package ru.otus.model;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"java:S1182", "java:S2157", "java:S2975"})
public class ObjectForMessage {
    private List<String> data;

    public List<String> getData() {
        return this.data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public ObjectForMessage clone() {
        ObjectForMessage objectForMessage = new ObjectForMessage();
        if (!isNull(this.data)) {
            objectForMessage.setData(new ArrayList<>(this.data));
        }
        return objectForMessage;
    }

    @Override
    public String toString() {
        return this.data.stream().map(String::valueOf).collect(Collectors.joining(", ", "[", "]"));
    }
}
