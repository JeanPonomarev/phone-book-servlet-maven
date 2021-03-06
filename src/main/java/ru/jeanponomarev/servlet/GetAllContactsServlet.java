package ru.jeanponomarev.servlet;

import ru.jeanponomarev.PhoneBook;
import ru.jeanponomarev.converter.ContactConverter;
import ru.jeanponomarev.model.Contact;
import ru.jeanponomarev.service.ContactService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class GetAllContactsServlet extends HttpServlet {
    private static final long serialVersionUID = 2838716451909659163L;

    private final ContactService contactService = PhoneBook.contactService;
    private final ContactConverter contactConverter = PhoneBook.contactConverter;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try (OutputStream outputStream = response.getOutputStream()) {
            List<Contact> contactList;

            String targetContactParameter = request.getParameter("term");

            if (targetContactParameter == null || targetContactParameter.isEmpty()) {
                contactList = contactService.getAllContacts();
            } else {
                contactList = contactService.getFilteredContacts(targetContactParameter);
            }

            String contactListJson = contactConverter.convertToJson(contactList);

            outputStream.write(contactListJson.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            System.out.println("error in GetAllContactsServlet GET: ");
            e.printStackTrace();
        }
    }
}