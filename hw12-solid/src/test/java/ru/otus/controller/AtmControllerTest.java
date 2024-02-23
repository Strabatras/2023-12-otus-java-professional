package ru.otus.controller;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.otus.DataFactory.banknote;
import static ru.otus.DataFactory.banknoteList;
import static ru.otus.DataFactory.billingList;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ru.otus.domain.Banknote;
import ru.otus.domain.Billing;
import ru.otus.enums.Nominal;
import ru.otus.service.AtmService;

@DisplayName("ATM controller")
class AtmControllerTest {

    private AtmController atmController;
    private AtmService atmService;

    @BeforeEach
    public void setUp() {
        this.atmService = mock(AtmService.class);
        this.atmController = new AtmController(atmService);
    }

    @DisplayName("вызов сервиса для добавления банкноты")
    @Test
    void shouldBeAddedBanknote() {
        Banknote banknote = banknote(Nominal.FIFTY);
        ArgumentCaptor<Banknote> param = ArgumentCaptor.forClass(Banknote.class);

        doNothing().when(atmService).addBanknote(param.capture());

        assertDoesNotThrow(() -> atmController.addBanknote(banknote));

        assertEquals(banknote.getNominal().name(), param.getValue().getNominal().name());
        verify(atmService, times(1)).addBanknote(any(Banknote.class));
    }

    @SuppressWarnings("unchecked")
    @DisplayName("вызов сервиса для добавления банкнот")
    @Test
    void shouldBeAddedBanknotes() {
        List<Banknote> banknoteList = banknoteList();
        ArgumentCaptor<List<Banknote>> param = ArgumentCaptor.forClass(List.class);

        doNothing().when(atmService).addBanknote(param.capture());

        assertDoesNotThrow(() -> atmController.addBanknote(banknoteList));

        assertEquals(banknoteList, param.getValue());
        verify(atmService, times(1)).addBanknote(anyList());
    }

    @DisplayName("возвращает запрошенные банкноты переданные сервисом")
    @Test
    void shouldReturnBanknotesFromService() {
        long amount = 100L;
        List<Banknote> banknoteList = banknoteList();
        ArgumentCaptor<Long> param = ArgumentCaptor.forClass(Long.class);

        when(atmService.giveMoney(param.capture())).thenReturn(banknoteList);

        List<Banknote> banknotes = atmController.giveMoney(amount);

        assertEquals(amount, param.getValue());
        assertEquals(banknoteList, banknotes);
        verify(atmService, times(1)).giveMoney(amount);
    }

    @DisplayName("возвращает запрошенную статистику переданную сервисом")
    @Test
    void shouldReturnBillingListFromService() {
        List<Billing> billingList = billingList();
        when(atmService.billing()).thenReturn(billingList);

        List<Billing> billings = atmController.billing();

        assertEquals(billingList, billings);
        verify(atmService, times(1)).billing();
    }
}
