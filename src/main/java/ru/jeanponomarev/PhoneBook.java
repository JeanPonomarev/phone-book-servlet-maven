package ru.jeanponomarev;

import ru.jeanponomarev.converter.ContactConverter;
import ru.jeanponomarev.converter.ContactValidationConverter;
import ru.jeanponomarev.dao.ContactDao;
import ru.jeanponomarev.service.ContactService;

public class PhoneBook {

    public static ContactDao contactDao = new ContactDao();

    public static ContactService contactService = new ContactService();

    public static ContactConverter contactConverter = new ContactConverter();

    public static ContactValidationConverter contactValidationConverter = new ContactValidationConverter();
}
