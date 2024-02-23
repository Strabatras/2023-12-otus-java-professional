package ru.otus.service.impl;

import static ru.otus.util.AtmUtil.validateAmount;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import ru.otus.domain.Banknote;
import ru.otus.domain.Billing;
import ru.otus.enums.Nominal;
import ru.otus.exception.AtmEmptyCellException;
import ru.otus.repository.AtmRepository;
import ru.otus.service.AtmService;
import ru.otus.util.AtmUtil;

@RequiredArgsConstructor
public class AtmServiceImpl implements AtmService {
    private final AtmRepository atmRepository;

    @Override
    public void addBanknote(Banknote banknote) {
        atmRepository.addBanknote(banknote);
    }

    @Override
    public void addBanknote(List<Banknote> banknoteList) {
        for (Banknote banknote : banknoteList) {
            atmRepository.addBanknote(banknote);
        }
    }

    @Override
    public List<Billing> billing() {
        Map<Integer, Long> cellBox = atmRepository.cellBox();
        return cellBox.entrySet().stream().map(AtmUtil::entryToBilling).toList();
    }

    @Override
    public List<Banknote> giveMoney(long amount) {
        long totalCellBoxSum = atmRepository.totalCellBoxSum();
        Nominal minNominal = atmRepository.minNominalInCellBox();
        validateAmount(amount, totalCellBoxSum, minNominal);

        List<Banknote> banknotes = new ArrayList<>();

        recursiveBanknoteSelection(amount, banknotes);

        return banknotes;
    }

    private void recursiveBanknoteSelection(long amount, List<Banknote> banknotes) {
        Nominal nominal = atmRepository.releventNominal(amount);
        while (amount > 0) {
            if (amount < nominal.getDignity() || (amount - nominal.getDignity()) < 0) {
                recursiveBanknoteSelection(amount, banknotes);
                return;
            }
            try {
                Banknote banknote = atmRepository.banknoteByNominal(nominal);
                banknotes.add(banknote);
                amount -= nominal.getDignity();
            } catch (AtmEmptyCellException e) {
                recursiveBanknoteSelection(amount, banknotes);
                return;
            }
        }
    }
}
