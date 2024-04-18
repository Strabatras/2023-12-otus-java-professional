package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.model.Client;
import ru.otus.services.DBServiceClient;
import ru.otus.services.TemplateProcessor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.nonNull;
import static ru.otus.enums.ClientsWebServerUri.CLIENTS_LIST_PAGE;
import static ru.otus.enums.ClientsWebServerUri.LOGIN_FORM_PAGE;
import static ru.otus.helpers.RequestHelper.requestToClient;

public class ClientsServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ClientsServlet.class);
    private static final String CLIENTS_PAGE_TEMPLATE = "clients/clients.html";
    private static final String SESSION_SUCCESS_MESSAGE_ATTRIBUTE = "successMessage";
    private static final String SESSION_ERROR_MESSAGE_ATTRIBUTE = "errorMessage";
    private static final String SUCCESS_MESSAGE_CREATE_CLIENT = "Клиент создан";
    private static final String ERROR_MESSAGE_CAN_NOT_CREATE_CLIENT = "Ошибка добавления клиента";


    private final DBServiceClient dbServiceClient;
    private final TemplateProcessor templateProcessor;

    public ClientsServlet(DBServiceClient dbServiceClient, TemplateProcessor templateProcessor){
        this.dbServiceClient = dbServiceClient;
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        HttpSession session = request.getSession(false);
        boolean authorized = nonNull(session);

        Object successMessage = null;
        Object errorMessage = null;
        if (authorized){
            successMessage = session.getAttribute(SESSION_SUCCESS_MESSAGE_ATTRIBUTE);
            errorMessage = session.getAttribute(SESSION_ERROR_MESSAGE_ATTRIBUTE);

            session.setAttribute(SESSION_SUCCESS_MESSAGE_ATTRIBUTE, null);
            session.setAttribute(SESSION_ERROR_MESSAGE_ATTRIBUTE, null);
        }

        var clients = dbServiceClient.findAll();
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("clients", clients);
        paramsMap.put("authorized", authorized);
        paramsMap.put("successMessage", nonNull(successMessage) ? successMessage : "");
        paramsMap.put("errorMessage", nonNull(errorMessage) ? errorMessage : "");
        response.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        try {
            Client client = requestToClient(request);
            dbServiceClient.saveClient(client);
            session.setAttribute(SESSION_SUCCESS_MESSAGE_ATTRIBUTE, SUCCESS_MESSAGE_CREATE_CLIENT);
        } catch (Exception e) {
            logger.atError().setMessage(e.getMessage()).log();
            session.setAttribute(SESSION_ERROR_MESSAGE_ATTRIBUTE, ERROR_MESSAGE_CAN_NOT_CREATE_CLIENT);
        }

        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        response.sendRedirect(CLIENTS_LIST_PAGE.getUri());
    }
}
