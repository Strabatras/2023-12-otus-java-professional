package ru.otus.service;

import java.util.List;
import ru.otus.domain.Banknote;
import ru.otus.domain.Billing;

public interface AtmService {
    void addBanknote(Banknote banknote);

    void addBanknote(List<Banknote> banknoteList);

    List<Billing> billing();

    List<Banknote> giveMoney(long amount);
}
