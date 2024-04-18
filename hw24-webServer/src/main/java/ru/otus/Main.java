package ru.otus;

import org.hibernate.cfg.Configuration;
import ru.otus.dao.InMemoryUserDao;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.server.ClientsWebServer;
import ru.otus.server.impl.ClientsWebServerImpl;
import ru.otus.services.DBServiceClient;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.UserAuthService;
import ru.otus.services.impl.DbServiceClientImpl;
import ru.otus.services.impl.TemplateProcessorImpl;
import ru.otus.services.impl.UserAuthServiceImpl;
import ru.otus.util.DBHibernateUtil;

public class Main {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {
        Class<?>[] annotatedClasses = {Client.class, Address.class, Phone.class};

        var dbHibernateUtil = new DBHibernateUtil<>(
                new Configuration(),
                Client.class,
                annotatedClasses
        );

        var transactionManager = dbHibernateUtil.getTransactionManager();
        var clientTemplate = dbHibernateUtil.getDataTemplate();
        DBServiceClient dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl(new InMemoryUserDao());

        ClientsWebServer usersWebServer = new ClientsWebServerImpl(
                WEB_SERVER_PORT, authService, dbServiceClient, templateProcessor);

        usersWebServer.start();
        usersWebServer.join();
    }
}