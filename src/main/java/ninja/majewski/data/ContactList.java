package ninja.majewski.data;

import ninja.majewski.model.Contact;
import ninja.majewski.util.FileHandler;

import java.util.List;

public class ContactList {

    private List<Contact> contacts;

    public ContactList() {
        contacts = FileHandler.getInstance().readContacts();
    }

    public void addContact(Contact newContact) {
        if (contacts.contains(newContact))
            contacts.remove(newContact);

        contacts.add(newContact);
    }

    public void updateContact(String id, Contact newData) {
        for (Contact contact : contacts) {
            if (contact.getId().equals(id)) {
                contact.setFirstName(newData.getFirstName());
                contact.setLastName(newData.getLastName());
                contact.setPhone(newData.getPhone());
                contact.setEmail(newData.getEmail());
            }
        }
    }

    public void removeContact(String id) {
        Contact toRemove = null;
        for (Contact contact : contacts) {
            if (contact.getId().equals(id))
                toRemove = contact;
        }

        if (toRemove != null)
            contacts.remove(toRemove);
    }

    public void saveToFile() {
        FileHandler.getInstance().saveContacts(contacts);
    }

    public List<Contact> getContacts() {
        return contacts;
    }
}
