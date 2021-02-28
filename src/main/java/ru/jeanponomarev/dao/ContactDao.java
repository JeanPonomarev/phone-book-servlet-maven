package ru.jeanponomarev.dao;

import ru.jeanponomarev.model.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ContactDao {
    private final List<Contact> contactList = new ArrayList<Contact>();
    private final AtomicInteger idSequence = new AtomicInteger(0);

    public ContactDao() {
        Contact firstContact = new Contact();
        firstContact.setId(getNewId());
        firstContact.setFirstName("Jean");
        firstContact.setLastName("Ponomarev");
        firstContact.setPhoneNumber("89134714748");

        contactList.add(firstContact);

        Contact secondContact = new Contact();

        secondContact.setId(getNewId());
        secondContact.setFirstName("John");
        secondContact.setLastName("Doe");
        secondContact.setPhoneNumber("3469755");

        contactList.add(secondContact);

        Contact thirdContact = new Contact();

        thirdContact.setId(getNewId());
        thirdContact.setFirstName("Ivan");
        thirdContact.setLastName("Ivanov");
        thirdContact.setPhoneNumber("35723399");

        contactList.add(thirdContact);
    }

    private int getNewId() {
        return idSequence.addAndGet(1);
    }

    public List<Contact> getAllContacts() {
        return contactList;
    }

    public List<Contact> getFilteredContacts(String targetContactParameter) {
        return contactList
                .stream()
                .filter(
                        contact -> contact.getFirstName().equals(targetContactParameter) ||
                                contact.getLastName().equals(targetContactParameter) ||
                                contact.getPhoneNumber().equals(targetContactParameter))
                .collect(Collectors.toList());
    }

    public void addContact(Contact contact) {
        contact.setId(getNewId());
        contactList.add(contact);
    }

    public void deleteContact(Contact targetContact) {
        contactList.remove(targetContact);
    }

    public void deleteContactList(List<Contact> targetContacts) {
        contactList.removeAll(targetContacts);
    }
}