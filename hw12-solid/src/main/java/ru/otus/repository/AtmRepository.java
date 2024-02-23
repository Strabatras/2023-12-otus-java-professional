package ru.otus.repository;

import static ru.otus.util.AtmUtil.banknote;
import static ru.otus.util.AtmUtil.nominalByDignity;

import java.util.Map;
import java.util.TreeMap;
import ru.otus.domain.Banknote;
import ru.otus.enums.Nominal;
import ru.otus.exception.AtmEmptyCellException;
import ru.otus.exception.AtmNominalException;

public class AtmRepository {

    private final Map<Integer, Long> cellBox = new TreeMap<>();

    public void addBanknote(Banknote banknote) {
        int dignity = banknote.getNominal().getDignity();
        Long cellSize = cellBox.computeIfAbsent(dignity, key -> 0L) + 1;
        cellBox.put(dignity, cellSize);
    }

    public Banknote banknoteByNominal(Nominal nominal) {
        Long cellSize = cellBox.computeIfAbsent(nominal.getDignity(), key -> 0L);
        if (cellSize == 0) {
            cellBox.remove(nominal.getDignity());
            throw new AtmEmptyCellException("Ошибка получения банкноты Ячейка пуста");
        }
        cellBox.put(nominal.getDignity(), --cellSize);
        return banknote(nominal);
    }

    public Nominal minNominalInCellBox() {
        if (cellBox.size() == 0) {
            throw new AtmNominalException("Ошибка получения минимального номинала, хранилище пусто");
        }
        int dignity = ((TreeMap<Integer, Long>) cellBox).firstKey();
        if (cellBox.putIfAbsent(dignity, 0L) < 1) {
            throw new AtmNominalException("Ошибка получения минимального номинала");
        }
        return nominalByDignity(dignity);
    }

    public long totalCellBoxSum() {
        return cellBox.entrySet().stream()
                .mapToLong(e -> e.getKey() * e.getValue())
                .sum();
    }

    public Nominal releventNominal(long amount) {
        int dignity = ((TreeMap<Integer, Long>) cellBox)
                .descendingKeySet().stream()
                        .filter(k -> k <= amount)
                        .findFirst()
                        .orElseThrow(() -> new AtmNominalException("Невозможно подобрать подходящий номинал"));
        return nominalByDignity(dignity);
    }

    @SuppressWarnings("unchecked")
    public Map<Integer, Long> cellBox() {
        return (TreeMap<Integer, Long>) ((TreeMap<Integer, Long>) cellBox).clone();
    }
}
