package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.otus.services.TemplateProcessor;

import java.io.IOException;

import static ru.otus.enums.ClientsWebServerUri.CLIENTS_LIST_PAGE;

public class ClientsFormServlet extends HttpServlet {
    private static final String CLIENTS_FORM_TEMPLATE = "clients/form.html";
    private final TemplateProcessor templateProcessor;
    public ClientsFormServlet(TemplateProcessor templateProcessor){
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(CLIENTS_LIST_PAGE.getUri());
            return;
        }
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CLIENTS_FORM_TEMPLATE, null));
    }
}
