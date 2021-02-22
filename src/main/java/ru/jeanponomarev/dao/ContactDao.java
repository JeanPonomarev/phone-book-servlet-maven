package ru.jeanponomarev.dao;

import ru.jeanponomarev.model.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ContactDao {
    private List<Contact> contactList = new ArrayList<Contact>();
    private AtomicInteger idSequence = new AtomicInteger(0);

    public ContactDao() {
        Contact contact = new Contact();
        contact.setId(getNewId());
        contact.setFirstName("Jean");
        contact.setLastName("Ponomarev");
        contact.setPhoneNumber("89134714748");

        contactList.add(contact);
    }

    private int getNewId() {
        return idSequence.addAndGet(1);
    }

    public List<Contact> getAllContacts() {
        return contactList;
    }

    public void add(Contact contact) {
        contact.setId(getNewId());
        contactList.add(contact);
    }
}