package ru.otus.util;

import org.hibernate.cfg.Configuration;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.dbmigrations.MigrationsExecutorFlyway;

public class DBHibernateUtil<T> {
    private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private final Configuration configuration;
    private final Class<T> dataTemplateClazz;
    private final Class<?>[] annotatedClasses;

    public DBHibernateUtil(Configuration configuration, Class<T> dataTemplateClazz, Class<?>[] annotatedClasses) {
        this.configuration = configuration.configure(HIBERNATE_CFG_FILE);;
        this.dataTemplateClazz = dataTemplateClazz;
        this.annotatedClasses = annotatedClasses;
    }

    public TransactionManager getTransactionManager(){
        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();
        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, annotatedClasses);
        return new TransactionManagerHibernate(sessionFactory);
    }

    public DataTemplate<T> getDataTemplate(){
        return new DataTemplateHibernate<T>(dataTemplateClazz);
    }
}
