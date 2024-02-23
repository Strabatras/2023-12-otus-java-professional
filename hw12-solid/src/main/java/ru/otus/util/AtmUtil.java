package ru.otus.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import ru.otus.domain.Banknote;
import ru.otus.domain.Billing;
import ru.otus.enums.Nominal;
import ru.otus.exception.AtmNominalException;
import ru.otus.exception.AtmValidationException;

@SuppressWarnings({"java:S106"})
public class AtmUtil {
    private static final String MESSAGE_BILLING_NOMINAL_INFO = "Банкноты номиналом %d. Кол-во : %d. Сумма : %d.";
    private static final String MESSAGE_BILLING_NOMINAL_TOTAL_INFO =
            "Номиналов банкнот : %d. Кол-во банкнот : %d. На сумму : %d.";

    private static final Random random = new Random();

    private AtmUtil() {}

    public static void printBilling(List<Billing> billingList) {
        long sum = 0;
        long cnt = 0;
        for (Billing billing : billingList) {
            sum += billing.getSum();
            cnt += billing.getCount();
            System.out.printf(
                    MESSAGE_BILLING_NOMINAL_INFO + "%n", billing.getNominal(), billing.getCount(), billing.getSum());
        }
        System.out.printf((MESSAGE_BILLING_NOMINAL_TOTAL_INFO) + "%n", billingList.size(), cnt, sum);
    }

    public static Banknote banknote(Nominal nominal) {
        return new Banknote(nominal);
    }

    public static List<Banknote> banknoteList(Nominal nominal, Long capacity) {
        List<Banknote> banknoteList = new ArrayList<>();
        while (capacity-- > 0) {
            banknoteList.add(banknote(nominal));
        }
        return banknoteList;
    }

    public static List<Banknote> randomBanknoteList() {
        List<Banknote> banknoteList = new ArrayList<>();
        randomSumSameBanknoteList(banknoteList);
        return banknoteList;
    }

    public static void randomSumSameBanknoteList(List<Banknote> banknoteList) {
        var nominal = randomNominal();
        int random = randomIntFromRange(5, 25);
        while (random-- > 0) {
            banknoteList.add(banknote(nominal));
        }
    }

    public static Nominal nominalByDignity(int dignity) {
        return Arrays.stream(Nominal.values())
                .filter(nominal -> nominal.getDignity() == dignity)
                .findFirst()
                .orElseThrow(() -> new AtmNominalException("Недопустимый номинал"));
    }

    public static Billing entryToBilling(Map.Entry<Integer, Long> entry) {
        return new Billing(entry.getKey(), entry.getValue(), entry.getKey() * entry.getValue());
    }

    public static void validateAmount(long amount, long totalCellBoxSum, Nominal minNominal) {
        if (amount < 1) {
            throw new AtmValidationException("Запрашиваемая сумма не может быть меньше или равно нулю");
        }
        if (amount % minNominal.getDignity() > 0) {
            throw new AtmValidationException("Нет купюр нужного номинала");
        }
        if (amount > totalCellBoxSum) {
            throw new AtmValidationException("Запрашиваемая сумма превышает наличие");
        }
    }

    private static int[] nominalDignity() {
        Nominal[] nominals = Nominal.values();
        int[] dignity = new int[nominals.length];
        for (int i = 0; i < nominals.length; i++) {
            dignity[i] = nominals[i].getDignity();
        }
        return dignity;
    }

    private static Nominal randomNominal() {
        final int[] array = nominalDignity();
        final int rnd = random.nextInt(array.length);
        return nominalByDignity(array[rnd]);
    }

    private static int randomIntFromRange(int min, int max) {
        return random.ints(min, max).findFirst().getAsInt();
    }
}
