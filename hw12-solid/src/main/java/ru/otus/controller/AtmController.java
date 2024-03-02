package ru.otus.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import ru.otus.domain.Banknote;
import ru.otus.domain.Billing;
import ru.otus.service.AtmService;

@RequiredArgsConstructor
public class AtmController {
    private final AtmService atmService;

    public void addBanknote(Banknote banknote) {
        atmService.addBanknote(banknote);
    }

    public void addBanknote(List<Banknote> banknoteList) {
        atmService.addBanknote(banknoteList);
    }

    public List<Banknote> giveMoney(long amount) {
        return atmService.giveMoney(amount);
    }

    public List<Billing> billing() {
        return atmService.billing();
    }
}
