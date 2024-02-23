package ru.otus.repository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.otus.DataFactory.banknote;

import java.util.Map;
import java.util.TreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.domain.Banknote;
import ru.otus.enums.Nominal;
import ru.otus.exception.AtmEmptyCellException;
import ru.otus.exception.AtmNominalException;

@DisplayName("ATM repository")
class AtmRepositoryTest {

    private AtmRepository atmRepository;

    @BeforeEach
    public void setUp() {
        this.atmRepository = new AtmRepository();
    }

    @DisplayName("добавляет банкноту в хранилище")
    @Test
    void shouldBeAddedBanknote() {
        Map<Integer, Long> cellBoxEmpty = atmRepository.cellBox();

        assertDoesNotThrow(() -> atmRepository.addBanknote(banknote(Nominal.FIFTY)));

        Map<Integer, Long> cellBoxNotEmpty = atmRepository.cellBox();

        assertTrue(cellBoxEmpty.isEmpty());
        assertFalse(cellBoxNotEmpty.isEmpty());
    }

    @DisplayName("добавляет банкноту в нужную ячейку хранилища")
    @Test
    void shouldBeAddedBanknoteToCorrectCell() {
        Banknote banknote = banknote(Nominal.FIFTY);
        Nominal nominal = banknote.getNominal();
        Map<Integer, Long> cellBoxEmpty = atmRepository.cellBox();
        long cellSizeZero = cellBoxEmpty.computeIfAbsent(nominal.getDignity(), key -> 0L);

        assertDoesNotThrow(() -> atmRepository.addBanknote(banknote));

        Map<Integer, Long> cellBox = atmRepository.cellBox();
        long cellSize = cellBox.computeIfAbsent(nominal.getDignity(), key -> 0L);

        assertEquals(0, cellSizeZero);
        assertEquals(1, cellSize);
    }

    @DisplayName("возвращает банкноту запрошенного номинала")
    @Test
    void shouldReturnBanknoteByNominal() {
        Banknote banknote = banknote(Nominal.FIFTY);
        Nominal nominal = banknote.getNominal();
        atmRepository.addBanknote(banknote);

        Banknote banknoteByNominal = atmRepository.banknoteByNominal(nominal);

        assertEquals(
                banknote.getNominal().getDignity(),
                banknoteByNominal.getNominal().getDignity());
    }

    @DisplayName("возвращает исключение если в хранилище нет банкноты нужного номинала")
    @Test
    void shouldReturnBanknoteByNominalAtmEmptyCellException() {
        Map<Integer, Long> cellBoxEmpty = atmRepository.cellBox();

        var exception = assertThrows(AtmEmptyCellException.class, () -> atmRepository.banknoteByNominal(Nominal.FIVE));

        assertTrue(cellBoxEmpty.isEmpty());
        assertEquals("Ошибка получения банкноты Ячейка пуста", exception.getMessage());
    }

    @DisplayName("уменьшает размер ячейки после получения банкноты")
    @Test
    void shouldReturnBanknoteByNominalDecrementCellBox() {
        Banknote banknote = banknote(Nominal.FIFTY);
        Nominal nominal = banknote.getNominal();
        atmRepository.addBanknote(banknote);
        atmRepository.addBanknote(banknote);

        long cellSize = atmRepository.cellBox().computeIfAbsent(nominal.getDignity(), key -> 0L);

        atmRepository.banknoteByNominal(nominal);

        long cellSizeDecrement = atmRepository.cellBox().computeIfAbsent(nominal.getDignity(), key -> 0L);

        assertEquals(2, cellSize);
        assertEquals(1, cellSizeDecrement);
    }

    @DisplayName("возвращает минимальный номинал найденый в хранилище")
    @Test
    void shouldReturnCorrectMinNominalInCellBox() {
        atmRepository.addBanknote(banknote(Nominal.TWO_THOUSAND));
        atmRepository.addBanknote(banknote(Nominal.TWO_HUNDRED));
        atmRepository.addBanknote(banknote(Nominal.FIVE));
        atmRepository.addBanknote(banknote(Nominal.FIFTY));

        Nominal nominal = atmRepository.minNominalInCellBox();
        assertEquals(Nominal.FIVE, nominal);
    }

    @DisplayName("возвращает исключение если номинал не найден в хранилище")
    @Test
    void shouldReturnAtmNominalExceptionWhenEmptyCellBox() {
        var exception = assertThrows(AtmNominalException.class, () -> atmRepository.minNominalInCellBox());
        assertEquals("Ошибка получения минимального номинала, хранилище пусто", exception.getMessage());
    }

    @DisplayName("возвращает корректную сумму находящихся в хранилище банкнот")
    @Test
    void shouldReturnCorrectTotalCellBoxSum() {
        long zeroCellBox = atmRepository.totalCellBoxSum();

        atmRepository.addBanknote(banknote(Nominal.FIVE));
        atmRepository.addBanknote(banknote(Nominal.THEN));
        atmRepository.addBanknote(banknote(Nominal.ONE_HUNDRED));
        atmRepository.addBanknote(banknote(Nominal.FIVE_THOUSAND));

        long sumCellBox = atmRepository.totalCellBoxSum();

        assertEquals(0, zeroCellBox);
        assertEquals(5115, sumCellBox);
    }

    @DisplayName("возвращает исключение если хранилище пусто")
    @Test
    void shouldReturnAtmNominalExceptionForReleventNominalWhenEmptyCellBox() {
        var exception = assertThrows(AtmNominalException.class, () -> atmRepository.releventNominal(10));
        assertEquals("Невозможно подобрать подходящий номинал", exception.getMessage());
    }

    @DisplayName("если запрашиваемый номинал меньше чем есть в наличии")
    @Test
    void shouldReturnAtmNominalExceptionForReleventNominalWhenSmallAmount() {
        atmRepository.addBanknote(banknote(Nominal.ONE_HUNDRED));

        var exception = assertThrows(AtmNominalException.class, () -> atmRepository.releventNominal(50));
        assertEquals("Невозможно подобрать подходящий номинал", exception.getMessage());
    }

    @DisplayName("возвращает корректный номинал")
    @Test
    void shouldReturnCorrectReleventNominal() {
        atmRepository.addBanknote(banknote(Nominal.FIVE));
        atmRepository.addBanknote(banknote(Nominal.FIFTY));
        atmRepository.addBanknote(banknote(Nominal.ONE_HUNDRED));

        assertEquals(5, atmRepository.releventNominal(5).getDignity());
        assertEquals(50, atmRepository.releventNominal(75).getDignity());
        assertEquals(100, atmRepository.releventNominal(100).getDignity());
    }

    @DisplayName("возвращает корректно заполненное хранилище")
    @Test
    void shouldReturnCorrectCellBox() {
        final Map<Integer, Long> cellBox = new TreeMap<>();
        cellBox.put(50, 1L);
        cellBox.put(100, 2L);
        cellBox.put(500, 5L);
        cellBox.put(1000, 3L);

        atmRepository.addBanknote(banknote(Nominal.FIFTY));
        atmRepository.addBanknote(banknote(Nominal.ONE_HUNDRED));
        atmRepository.addBanknote(banknote(Nominal.ONE_HUNDRED));
        atmRepository.addBanknote(banknote(Nominal.FIVE_HUNDRED));
        atmRepository.addBanknote(banknote(Nominal.FIVE_HUNDRED));
        atmRepository.addBanknote(banknote(Nominal.FIVE_HUNDRED));
        atmRepository.addBanknote(banknote(Nominal.FIVE_HUNDRED));
        atmRepository.addBanknote(banknote(Nominal.FIVE_HUNDRED));
        atmRepository.addBanknote(banknote(Nominal.THOUSAND));
        atmRepository.addBanknote(banknote(Nominal.THOUSAND));
        atmRepository.addBanknote(banknote(Nominal.THOUSAND));

        assertEquals(cellBox, atmRepository.cellBox());
    }
}
