package ru.jeanponomarev.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.jeanponomarev.model.Contact;

import java.lang.reflect.Type;
import java.util.List;

public class ContactConverter {
    private final Gson gson = new GsonBuilder().create();

    public String convertToJson(List<Contact> contactList) {
        return gson.toJson(contactList);
    }

    public Contact convertFromJson(String contactJson) {
        return gson.fromJson(contactJson, Contact.class);
    }

    public List<Contact> convertListFromJson(String contactListJson) {
        Type contactListType = new TypeToken<List<Contact>>() {}.getType();

        return gson.fromJson(contactListJson, contactListType);
    }
}