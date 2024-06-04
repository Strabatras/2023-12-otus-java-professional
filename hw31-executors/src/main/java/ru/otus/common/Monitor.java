package ru.otus.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import ru.otus.enums.NameThread;

@Getter
@Setter
public class Monitor {
    private NameThread nameThread;

    @Setter(AccessLevel.NONE)
    private Integer counter = 1;

    public Monitor(NameThread nameThread) {
        this.nameThread = nameThread;
    }

    public void increaseCounter(){
        counter++;
    }
}
