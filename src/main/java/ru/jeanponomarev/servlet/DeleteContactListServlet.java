package ru.jeanponomarev.servlet;

import ru.jeanponomarev.PhoneBook;
import ru.jeanponomarev.converter.ContactConverter;
import ru.jeanponomarev.model.Contact;
import ru.jeanponomarev.service.ContactService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class DeleteContactListServlet extends HttpServlet {
    private static final long serialVersionUID = -4575517219047797775L;

    private final ContactService contactService = PhoneBook.contactService;
    private final ContactConverter contactConverter = PhoneBook.contactConverter;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        List<Contact> targetContacts = contactConverter.convertListFromJson(requestBody);

        contactService.deleteContactList(targetContacts);

        response.setStatus(200);
    }
}