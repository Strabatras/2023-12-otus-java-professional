package ru.otus.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Billing {
    private final int nominal;
    private final long count;
    private final long sum;
}
