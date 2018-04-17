package pl.pawellukaszewski.testFX.dao;

import java.util.List;

public interface ContactDao {
    List<String> getAllContactsName(String username);
    String getNumber(String contact);
    boolean addContact(String name, String number);
    void removeContact(String name);
    boolean editContact(String newName, String number, String oldName);
}
