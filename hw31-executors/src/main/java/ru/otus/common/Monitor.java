package ru.otus.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.otus.enums.NameThread;

@Getter
@Setter
@AllArgsConstructor
public class Monitor {
    private NameThread nameThread;
}
