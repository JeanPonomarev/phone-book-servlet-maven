package ru.jeanponomarev.servlet;

import ru.jeanponomarev.PhoneBook;
import ru.jeanponomarev.converter.ContactConverter;
import ru.jeanponomarev.converter.ContactValidationConverter;
import ru.jeanponomarev.model.Contact;
import ru.jeanponomarev.service.ContactService;
import ru.jeanponomarev.service.ContactValidation;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.stream.Collectors;

public class AddContactServlet extends HttpServlet {

    private static final long serialVersionUID = -1870327806654358823L;

    private ContactService contactService = PhoneBook.contactService;
    private ContactConverter contactConverter = PhoneBook.contactConverter;
    private ContactValidationConverter contactValidationConverter = PhoneBook.contactValidationConverter;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        try (OutputStream outputStream = response.getOutputStream()) {

            String contactJson = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

            Contact contact = contactConverter.convertFromJson(contactJson);

            ContactValidation contactValidation = contactService.addContact(contact);

            String contactValidationJson = contactValidationConverter.convertToJson(contactValidation);

            if (!contactValidation.isValid()) {
                response.setStatus(500);
            }

            outputStream.write(contactValidationJson.getBytes(Charset.forName("UTF-8")));
        } catch (Exception e) {
            System.out.println("error in AddContactServlet POST: ");
            e.printStackTrace();
        }
    }
}