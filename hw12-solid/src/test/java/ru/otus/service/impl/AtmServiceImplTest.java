package ru.otus.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.otus.DataFactory.banknote;
import static ru.otus.DataFactory.banknoteList;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ru.otus.domain.Banknote;
import ru.otus.domain.Billing;
import ru.otus.enums.Nominal;
import ru.otus.exception.AtmValidationException;
import ru.otus.repository.AtmRepository;
import ru.otus.service.AtmService;

@DisplayName("ATM service")
class AtmServiceImplTest {
    private AtmRepository atmRepository;
    private AtmService atmService;

    @BeforeEach
    public void setUp() {
        this.atmRepository = mock(AtmRepository.class);
        this.atmService = new AtmServiceImpl(atmRepository);
    }

    @DisplayName("вызов репозитория для добавления банкноты")
    @Test
    void shouldBeAddedBanknote() {
        Banknote banknote = banknote(Nominal.TWO_THOUSAND);
        ArgumentCaptor<Banknote> param = ArgumentCaptor.forClass(Banknote.class);
        doNothing().when(atmRepository).addBanknote(param.capture());

        assertDoesNotThrow(() -> atmService.addBanknote(banknote));

        assertEquals(banknote.getNominal().name(), param.getValue().getNominal().name());
        verify(atmRepository, times(1)).addBanknote(any(Banknote.class));
    }

    @DisplayName("вызов репозитория для добавления банкнот")
    @Test
    void shouldBeAddedBanknotes() {
        List<Banknote> banknoteList = banknoteList();
        doNothing().when(atmRepository).addBanknote(any(Banknote.class));

        assertDoesNotThrow(() -> atmService.addBanknote(banknoteList));

        verify(atmRepository, times(banknoteList.size())).addBanknote(any(Banknote.class));
    }

    @DisplayName("возвращает корректную статистику хранилища")
    @Test
    void shouldReturnCorrectBilling() {
        Map<Integer, Long> cellBox = new TreeMap<>();
        cellBox.put(10, 1L);
        cellBox.put(50, 2L);
        cellBox.put(5000, 3L);

        when(atmRepository.cellBox()).thenReturn(cellBox);

        List<Billing> actual = atmService.billing();

        assertThat(actual)
                .hasSize(3)
                .extracting(Billing::getNominal, Billing::getCount, Billing::getSum)
                .contains(tuple(10, 1L, 10L), tuple(50, 2L, 100L), tuple(5000, 3L, 15000L));
        verify(atmRepository, times(1)).cellBox();
    }

    @DisplayName("возвращает банкноты запрошенной суммы")
    @Test
    void shouldReturnCorrectMoney() {
        when(atmRepository.totalCellBoxSum()).thenReturn(1110L);
        when(atmRepository.minNominalInCellBox()).thenReturn(Nominal.FIVE);
        when(atmRepository.releventNominal(anyLong()))
                .thenReturn(Nominal.ONE_HUNDRED, Nominal.THEN, Nominal.THEN, Nominal.THEN, Nominal.FIVE);
        when(atmRepository.banknoteByNominal(any(Nominal.class)))
                .thenReturn(
                        banknote(Nominal.ONE_HUNDRED),
                        banknote(Nominal.THEN),
                        banknote(Nominal.THEN),
                        banknote(Nominal.THEN),
                        banknote(Nominal.FIVE));

        var banknoteList = atmService.giveMoney(135);

        assertThat(banknoteList)
                .hasSize(5)
                .extracting(Banknote::getNominal)
                .contains(Nominal.ONE_HUNDRED, Nominal.THEN, Nominal.THEN, Nominal.THEN, Nominal.FIVE);

        verify(atmRepository, times(1)).totalCellBoxSum();
        verify(atmRepository, times(1)).minNominalInCellBox();
        verify(atmRepository, times(5)).releventNominal(anyLong());
        verify(atmRepository, times(5)).banknoteByNominal(any(Nominal.class));
    }

    @DisplayName("возвращает исключение если запрашиваемая сумма меньше единицы")
    @Test
    void shouldReturnAtmValidationExceptionWhenAmountIsZero() {
        when(atmRepository.totalCellBoxSum()).thenReturn(1110L);
        when(atmRepository.minNominalInCellBox()).thenReturn(Nominal.FIVE);

        var exception = assertThrows(AtmValidationException.class, () -> atmService.giveMoney(0));

        assertEquals("Запрашиваемая сумма не может быть меньше или равно нулю", exception.getMessage());

        verify(atmRepository, times(1)).totalCellBoxSum();
        verify(atmRepository, times(1)).minNominalInCellBox();
    }

    @DisplayName("возвращает исключение если не можем подобрать номиналы для выдачи запрашиваемой суммы")
    @Test
    void shouldReturnAtmValidationExceptionWhenNoRequiredNominal() {
        when(atmRepository.totalCellBoxSum()).thenReturn(1110L);
        when(atmRepository.minNominalInCellBox()).thenReturn(Nominal.FIVE);

        var exception = assertThrows(AtmValidationException.class, () -> atmService.giveMoney(153));

        assertEquals("Нет купюр нужного номинала", exception.getMessage());

        verify(atmRepository, times(1)).totalCellBoxSum();
        verify(atmRepository, times(1)).minNominalInCellBox();
    }

    @DisplayName("возвращает исключение если запрашиваемая сумма больше наличия")
    @Test
    void shouldReturnAtmValidationExceptionWhenAmountToLarge() {
        when(atmRepository.totalCellBoxSum()).thenReturn(1100L);
        when(atmRepository.minNominalInCellBox()).thenReturn(Nominal.ONE_HUNDRED);

        var exception = assertThrows(AtmValidationException.class, () -> atmService.giveMoney(1200));

        assertEquals("Запрашиваемая сумма превышает наличие", exception.getMessage());

        verify(atmRepository, times(1)).totalCellBoxSum();
        verify(atmRepository, times(1)).minNominalInCellBox();
    }
}
