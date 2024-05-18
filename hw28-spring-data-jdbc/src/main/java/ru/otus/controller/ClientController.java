package ru.otus.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.request.ClientRequest;
import ru.otus.service.ClientService;

@Controller
@RequiredArgsConstructor
public class ClientController {
    private static final String SESSION_ERROR_MESSAGE_ATTRIBUTE_NAME = "errorMessage";
    private static final String SESSION_SUCCESS_MESSAGE_ATTRIBUTE_NAME = "successMessage";
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);
    private final ClientService clientService;

    @PostMapping(path = "/clients")
    public String createClient(HttpSession session, ClientRequest clientRequest) {
        try {
            clientService.save(clientRequest);
            session.setAttribute(SESSION_SUCCESS_MESSAGE_ATTRIBUTE_NAME, "Клиент сохранен");
        } catch (Exception e){
            logger.atError().setCause(e).log();
            session.setAttribute(SESSION_ERROR_MESSAGE_ATTRIBUTE_NAME, "Ошибка сохранения клиента");
        }
        return "redirect:/clients";
    }

    @GetMapping("/clients")
    public String clients(HttpSession session, Model model) {
        var clients = clientService.findAll();
        model.addAttribute("clients", clients);
        model.addAttribute(SESSION_ERROR_MESSAGE_ATTRIBUTE_NAME, session.getAttribute(SESSION_ERROR_MESSAGE_ATTRIBUTE_NAME));
        model.addAttribute(SESSION_SUCCESS_MESSAGE_ATTRIBUTE_NAME, session.getAttribute(SESSION_SUCCESS_MESSAGE_ATTRIBUTE_NAME));
        model.addAttribute("authorized", true);
        session.setAttribute(SESSION_ERROR_MESSAGE_ATTRIBUTE_NAME, null);
        session.setAttribute(SESSION_SUCCESS_MESSAGE_ATTRIBUTE_NAME, null);
        return "clients/list";
    }

    @GetMapping("/clients/form")
    public String form(){
        return "clients/form";
    }
}
