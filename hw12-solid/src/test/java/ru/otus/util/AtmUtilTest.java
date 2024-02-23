package ru.otus.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.otus.util.AtmUtil.banknoteList;
import static ru.otus.util.AtmUtil.entryToBilling;
import static ru.otus.util.AtmUtil.nominalByDignity;
import static ru.otus.util.AtmUtil.randomBanknoteList;
import static ru.otus.util.AtmUtil.randomSumSameBanknoteList;
import static ru.otus.util.AtmUtil.validateAmount;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.domain.Banknote;
import ru.otus.domain.Billing;
import ru.otus.enums.Nominal;
import ru.otus.exception.AtmNominalException;
import ru.otus.exception.AtmValidationException;

@DisplayName("ATM utils")
class AtmUtilTest {

    @DisplayName("возвращает банкноту запрошенного номинала")
    @Test
    void shouldReturnCorrectBanknote() {
        Banknote banknote = AtmUtil.banknote(Nominal.ONE_HUNDRED);
        assertEquals(Nominal.ONE_HUNDRED.name(), banknote.getNominal().name());
    }

    @DisplayName("возвращает корректный список банкнот")
    @Test
    void shouldReturnCorrectBanknoteBanknoteList() {
        Long capacity = 5L;
        Nominal nominal = Nominal.FIVE;
        List<Banknote> banknoteList = banknoteList(nominal, capacity);

        assertThat(banknoteList)
                .hasSize(capacity.intValue())
                .extracting(Banknote::getNominal)
                .contains(nominal);
    }

    @DisplayName("возвращает корректный не пустой случайный список банкнот")
    @Test
    void shouldReturnNotEmptyRandomBanknoteList() {
        List<Banknote> banknoteList = randomBanknoteList();
        assertFalse(banknoteList.isEmpty());
    }

    @DisplayName("возвращает корректный случайный список банкнот одного номинала")
    @Test
    void shouldReturnNotEmptyRandomSumSameBanknoteList() {
        List<Banknote> banknoteList = new ArrayList<>();
        randomSumSameBanknoteList(banknoteList);

        Nominal nominal = banknoteList.getFirst().getNominal();

        assertFalse(banknoteList.isEmpty());
        assertThat(banknoteList).extracting(Banknote::getNominal).contains(nominal);
    }

    @DisplayName("возвращает корректный номинал")
    @Test
    void shouldReturnCorrectNominalByDignity() {
        int dignity = Nominal.TWO_HUNDRED.getDignity();
        Nominal nominal = nominalByDignity(dignity);

        assertEquals(dignity, nominal.getDignity());
    }

    @DisplayName("выбрасывает исключение когда запрошен несуществующий номинал")
    @Test
    void shouldReturnAtmNominalExceptionWhenNoSuchDignity() {
        int dignity = 3;
        var exception = assertThrows(AtmNominalException.class, () -> nominalByDignity(dignity));
        assertEquals("Недопустимый номинал", exception.getMessage());
    }

    @DisplayName("возвращает корректный корректно заполненный биллиг")
    @Test
    void shouldReturnCorrectBillingByMapEntry() {
        Map<Integer, Long> map = new TreeMap<>();
        map.put(10, 15L);
        var entry = map.entrySet().stream().findFirst().get();
        Billing billing = entryToBilling(entry);

        assertEquals(10, billing.getNominal());
        assertEquals(15, billing.getCount());
        assertEquals(150, billing.getSum());
    }

    @DisplayName("выбрасывает исключение если значение amount меньше единицы")
    @Test
    void shouldReturnAtmValidationExceptionWhenAmountIsZero() {
        long amount = 0;
        long sum = 100;
        Nominal nominal = Nominal.FIVE;

        var exception = assertThrows(AtmValidationException.class, () -> validateAmount(amount, sum, nominal));
        assertEquals("Запрашиваемая сумма не может быть меньше или равно нулю", exception.getMessage());
        assertEquals(0, amount);
    }

    @DisplayName("выбрасывает исключение если значение amount не кратено nominal.getDignity()")
    @Test
    void shouldReturnAtmValidationExceptionWhenAmountNotMultipleNominal() {
        long amount = 11;
        long sum = 100;
        Nominal nominal = Nominal.FIVE;

        var exception = assertThrows(AtmValidationException.class, () -> validateAmount(amount, sum, nominal));

        assertEquals("Нет купюр нужного номинала", exception.getMessage());
        assertTrue(amount % nominal.getDignity() > 0);
    }

    @DisplayName("выбрасывает исключение если значение amount больше значения sum")
    @Test
    void shouldReturnAtmValidationExceptionWhenAmountIsToLarge() {
        long amount = 105;
        long sum = 100;
        Nominal nominal = Nominal.FIVE;

        var exception = assertThrows(AtmValidationException.class, () -> validateAmount(amount, sum, nominal));

        assertEquals("Запрашиваемая сумма превышает наличие", exception.getMessage());
        assertTrue(amount > sum);
    }

    @DisplayName("валидация не выбрасывает исключения")
    @Test
    void shouldBeExecutedCorrectlyValidateAmount() {
        long amount = 10;
        long sum = 100;
        Nominal nominal = Nominal.FIVE;

        assertDoesNotThrow(() -> validateAmount(amount, sum, nominal));

        assertTrue(amount > 0);
        assertEquals(0, amount % nominal.getDignity());
        assertTrue(amount <= sum);
    }
}
