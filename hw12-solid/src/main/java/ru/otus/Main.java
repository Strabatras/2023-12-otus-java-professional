package ru.otus;

import static ru.otus.util.AtmUtil.printBilling;
import static ru.otus.util.AtmUtil.randomBanknoteList;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.controller.AtmController;
import ru.otus.domain.Banknote;
import ru.otus.domain.Billing;
import ru.otus.enums.Nominal;
import ru.otus.exception.AtmValidationException;
import ru.otus.repository.AtmRepository;
import ru.otus.service.AtmService;
import ru.otus.service.impl.AtmServiceImpl;

@SuppressWarnings({"java:S106", "java:S1144", "java:S125"})
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.atInfo().setMessage("HomeWork 12").log();

        AtmRepository atmRepository = new AtmRepository();
        AtmService atmService = new AtmServiceImpl(atmRepository);
        AtmController atmController = new AtmController(atmService);

        // uploadRandomMoneyToAtm(atmController);
        uploadMoneyToAtm(atmController);

        billing(atmController, "Загружено");

        giveMoney(atmController, 325);
        giveMoney(atmController, 333);
        giveMoney(atmController, 0);
    }

    private static void billing(AtmController atmController, String header) {
        List<Billing> billingList = atmController.billing();
        System.out.println("\n" + header);
        printBilling(billingList);
    }

    private static void giveMoney(AtmController atmController, long amount) {
        System.out.println("\nЗапрошенная сумма : " + amount);
        try {
            List<Banknote> banknotes = atmController.giveMoney(amount);

            System.out.println("Выданы банкноты:");
            for (Banknote banknote : banknotes) {
                System.out.println("Номинал : " + banknote.getNominal().getDignity());
            }
            billing(atmController, "Остаток");
        } catch (AtmValidationException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Что-то пошло не так. Обратитесь в службу поддержки.");
            logger.atError().setCause(e).log();
        }
    }

    private static void uploadRandomMoneyToAtm(AtmController atmController) {

        for (int i = 0; i < Nominal.values().length; i++) {
            atmController.addBanknote(randomBanknoteList());
        }
    }

    private static void uploadMoneyToAtm(AtmController atmController) {

        for (int i = 0; i < 5; i++) {
            atmController.addBanknote(new Banknote(Nominal.FIVE));
            atmController.addBanknote(new Banknote(Nominal.THEN));
            atmController.addBanknote(new Banknote(Nominal.FIFTY));
            atmController.addBanknote(new Banknote(Nominal.ONE_HUNDRED));
            atmController.addBanknote(new Banknote(Nominal.TWO_HUNDRED));
            atmController.addBanknote(new Banknote(Nominal.FIVE_HUNDRED));
            atmController.addBanknote(new Banknote(Nominal.THOUSAND));
            atmController.addBanknote(new Banknote(Nominal.TWO_THOUSAND));
            atmController.addBanknote(new Banknote(Nominal.FIVE_THOUSAND));
        }
    }
}
