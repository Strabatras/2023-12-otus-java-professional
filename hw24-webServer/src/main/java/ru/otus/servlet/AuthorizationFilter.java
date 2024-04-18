package ru.otus.servlet;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.eclipse.jetty.http.HttpMethod;
import org.hibernate.Session;

import java.io.IOException;
import java.util.Objects;

import static java.util.Objects.isNull;
import static ru.otus.enums.ClientsWebServerUri.CLIENTS_LIST_PAGE;
import static ru.otus.enums.ClientsWebServerUri.LOGIN_FORM_PAGE;

public class AuthorizationFilter implements Filter {
    private ServletContext context;

    @Override
    public void init(FilterConfig filterConfig) {
        this.context = filterConfig.getServletContext();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String uri = request.getRequestURI();
        this.context.log("Requested Resource:" + uri);

        if (useAuthentication(request)) {
            response.sendRedirect(LOGIN_FORM_PAGE.getUri());
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {
        // Not implemented
    }

    private boolean useAuthentication(HttpServletRequest request){
        if(HttpMethod.GET.name().equals(request.getMethod())
                && CLIENTS_LIST_PAGE.getUri().equals(request.getRequestURI())){
            return false;
        }
        HttpSession session = request.getSession(false);
        return isNull(session);
    }
}
