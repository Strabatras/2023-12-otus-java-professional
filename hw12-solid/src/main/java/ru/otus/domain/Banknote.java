package ru.otus.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.otus.enums.Nominal;

@Getter
@RequiredArgsConstructor
public final class Banknote {
    private final Nominal nominal;
}
