package ru.otus;

import java.util.ArrayList;
import java.util.List;
import ru.otus.domain.Banknote;
import ru.otus.domain.Billing;
import ru.otus.enums.Nominal;

public class DataFactory {
    public static Banknote banknote(Nominal nominal) {
        return new Banknote(nominal);
    }

    public static List<Banknote> banknoteList() {
        List<Banknote> banknoteList = new ArrayList<>();
        int i = 0;
        while (i < 5) {
            banknoteList.add(new Banknote(Nominal.FIFTY));
            i++;
        }
        return banknoteList;
    }

    public static List<Billing> billingList() {
        List<Billing> billingList = new ArrayList<>();

        int i = 0;
        while (i < 5) {
            billingList.add(new Billing(10, 20L, 200L));
            i++;
        }
        return billingList;
    }
}
