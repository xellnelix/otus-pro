package ru.otus.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DbServiceClientImpl;

@SuppressWarnings({"squid:S1948"})
public class ClientsApiServlet extends HttpServlet {

    private final DbServiceClientImpl dbServiceClient;
    private final Gson gson;

    public ClientsApiServlet(DbServiceClientImpl dbServiceClient, Gson gson) {
        this.dbServiceClient = dbServiceClient;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Client> clients = dbServiceClient.findAll();
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(clients));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String nameParam = request.getParameter("name");
        String addressParam = request.getParameter("address");
        String phoneParam = request.getParameter("phone");
        Address address = new Address(null, addressParam);
        Phone phone = new Phone(null, phoneParam);
        dbServiceClient.saveClient(new Client(null, nameParam, address, List.of(phone)));
        response.sendRedirect("/clients");
    }
}
