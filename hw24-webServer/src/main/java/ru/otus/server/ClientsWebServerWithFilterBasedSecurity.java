package ru.otus.server;

import com.google.gson.Gson;
import java.util.Arrays;
import org.eclipse.jetty.ee10.servlet.FilterHolder;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Handler;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.UserAuthService;
import ru.otus.servlet.AuthorizationFilter;
import ru.otus.servlet.LoginServlet;

public class ClientsWebServerWithFilterBasedSecurity extends ClientsWebServerSimple {
    private final UserAuthService authService;

    public ClientsWebServerWithFilterBasedSecurity(
            int port,
            UserAuthService authService,
            DbServiceClientImpl dbServiceClient,
            Gson gson,
            TemplateProcessor templateProcessor) {
        super(port, dbServiceClient, gson, templateProcessor);
        this.authService = authService;
    }

    @Override
    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor, authService)), "/login");
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths)
                .forEachOrdered(
                        path -> servletContextHandler.addFilter(new FilterHolder(authorizationFilter), path, null));
        return servletContextHandler;
    }
}
