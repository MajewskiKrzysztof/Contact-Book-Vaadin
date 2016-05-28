package ninja.majewski.model;

import java.util.UUID;

public class Contact {

    private String id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;

    public Contact() {
        id = UUID.randomUUID().toString();
    }

    public Contact(String firstName, String lastName, String phone, String email) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object obj) {
        Contact contact = (Contact) obj;

        if (!this.firstName.equals(contact.getFirstName()))
            return false;

        if (!this.lastName.equals(contact.getLastName()))
            return false;

        if (!this.phone.equals(contact.getPhone()))
            return false;

        if (!this.email.equals(contact.getEmail()))
            return false;

        return true;
    }
}
