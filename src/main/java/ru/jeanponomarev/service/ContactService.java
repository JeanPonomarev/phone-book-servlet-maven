package ru.jeanponomarev.service;

import ru.jeanponomarev.PhoneBook;
import ru.jeanponomarev.dao.ContactDao;
import ru.jeanponomarev.model.Contact;

import java.util.List;

public class ContactService {

    private ContactDao contactDao = PhoneBook.contactDao;

    private boolean isExistContactWithPhoneNumber(final String phoneNumber) {
        List<Contact> contactList = contactDao.getAllContacts();

        return contactList.stream().anyMatch(contact -> contact.getPhoneNumber().equals(phoneNumber));
    }

    public ContactValidation validateContact(Contact contact) {

        ContactValidation contactValidation = new ContactValidation();

        contactValidation.setValid(true);

        if (contact.getFirstName().isEmpty()) {
            contactValidation.setValid(false);
            contactValidation.setError("First name field should be filled");

            return contactValidation;
        }

        if (contact.getLastName().isEmpty()) {
            contactValidation.setValid(false);
            contactValidation.setError("Last name field should be filled");

            return contactValidation;
        }

        if (contact.getPhoneNumber().isEmpty()) {
            contactValidation.setValid(false);
            contactValidation.setError("Phone number field should be filled");

            return contactValidation;
        }

        if (isExistContactWithPhoneNumber(contact.getPhoneNumber())) {
            contactValidation.setValid(false);
            contactValidation.setError("Phone number is already exist");

            return contactValidation;
        }

        return contactValidation;
    }

    public ContactValidation addContact(Contact contact) {

        ContactValidation contactValidation = validateContact(contact);

        if (contactValidation.isValid()) {
            contactDao.add(contact);
        }

        return contactValidation;
    }

    public List<Contact> getAllContacts() {
        return contactDao.getAllContacts();
    }
}