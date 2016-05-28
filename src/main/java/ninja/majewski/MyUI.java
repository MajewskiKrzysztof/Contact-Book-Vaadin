package ninja.majewski;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import ninja.majewski.data.ContactList;
import ninja.majewski.model.Contact;
import ninja.majewski.util.InputValidator;

import java.util.List;

@Theme("mytheme")
@Widgetset("ninja.majewski.MyAppWidgetset")
public class MyUI extends UI {

    private ContactList contactList;

    private VerticalLayout leftSide;
    private VerticalLayout rightSide;

    private Button newContactButton;
    private Button saveButton;
    private Button cancelButton;
    private Button deleteButton;

    private TextField firstNameTA;
    private TextField lastNameTA;
    private TextField phoneTA;
    private TextField emailTA;

    private Table table;

    private String selectedItemId;

    // TODO SHOW INFO IF USER TRIES TO ADD ALREADY EXISTING CONTACT

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        contactList = new ContactList();
        selectedItemId = "";

        setMainLayout();
        addOnClickListeners();

        loadDataToTable();
        setTableOnClickListener();
    }

    private void setMainLayout() {
        final HorizontalLayout layout = new HorizontalLayout();
        layout.setResponsive(true);
        layout.setSizeFull();
        layout.setMargin(true);
        layout.setSpacing(true);

        createLeftSide();
        createRightSide();

        layout.addComponent(leftSide);
        layout.addComponent(rightSide);

        layout.setExpandRatio(leftSide, 0.75f);
        layout.setExpandRatio(rightSide, 0.25f);

        setContent(layout);
    }


    private void createLeftSide() {
        newContactButton = new Button("New contact");
        newContactButton.addStyleName("primary");
        Label title = new Label("My contacts:");

        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.addComponents(title, newContactButton);
        topLayout.setSpacing(true);
        topLayout.setSizeFull();
        topLayout.setExpandRatio(title, 1.0f);

        table = new Table();
        table.setWidth(100, Unit.PERCENTAGE);
        table.setSelectable(true);
        table.addContainerProperty("First Name", String.class, null);
        table.addContainerProperty("Last Name", String.class, null);
        table.addContainerProperty("Phone", String.class, null);
        table.addContainerProperty("Email", String.class, null);

        leftSide = new VerticalLayout();
        leftSide.addComponents(topLayout, table);
        leftSide.setSpacing(true);
    }

    private void createRightSide() {
        saveButton = new Button("Save");
        saveButton.addStyleName("friendly");
        cancelButton = new Button("Cancel");
        HorizontalLayout buttonLayout = new HorizontalLayout();
        saveButton.setWidth(100, Unit.PERCENTAGE);
        cancelButton.setWidth(100, Unit.PERCENTAGE);
        buttonLayout.addComponents(saveButton, cancelButton);
        buttonLayout.setSpacing(true);

        firstNameTA = new TextField("First Name");
        lastNameTA = new TextField("Last Name");
        phoneTA = new TextField("Phone");
        emailTA = new TextField("Email");

        deleteButton = new Button("Delete");
        deleteButton.addStyleName("danger");
        deleteButton.setEnabled(false);

        rightSide = new VerticalLayout();
        rightSide.addComponents(buttonLayout, firstNameTA, lastNameTA, phoneTA, emailTA, deleteButton);
        rightSide.setSpacing(true);
    }

    private void addOnClickListeners() {
        addNewContactButtonListener();
        addSaveButtonListener();
        addCancelButtonListener();
        addDeleteButtonListener();
    }

    private void addNewContactButtonListener() {
        newContactButton.addClickListener(clickEvent -> {
            clearInputFields();
            firstNameTA.focus();
        });
    }

    private void clearInputFields() {
        firstNameTA.clear();
        lastNameTA.clear();
        phoneTA.clear();
        emailTA.clear();

        table.select(null);
        selectedItemId = "";
        deleteButton.setEnabled(false);
    }

    private void addSaveButtonListener() {
        saveButton.addClickListener(clickEvent -> {
            StringBuilder message = new StringBuilder("Wrong input:<br/>");
            boolean wrongInput = false;

            String firstName = firstNameTA.getValue();
            if (!InputValidator.isStringOk(firstName)) {
                message.append("First Name<br/>");
                wrongInput = true;
            }

            String lastName = lastNameTA.getValue();
            if (!InputValidator.isStringOk(lastName)) {
                message.append("Last Name<br/>");
                wrongInput = true;
            }

            String phone = phoneTA.getValue();
            if (!InputValidator.isPhoneOk(phone)) {
                message.append("Phone<br/>");
                wrongInput = true;
            }

            String email = emailTA.getValue();
            if (!InputValidator.isEmailOk(email)) {
                message.append("Email<br/>");
                wrongInput = true;
            }

            if (wrongInput) {
                showNotification(message.toString(), Notification.TYPE_WARNING_MESSAGE);
                return;
            }

            if (selectedItemId.equals(""))
                contactList.addContact(new Contact(firstName, lastName, phone, email));
            else
                contactList.updateContact(selectedItemId, new Contact(firstName, lastName, phone, email));
            contactList.saveToFile();
            loadDataToTable();
            clearInputFields();
        });
    }

    private void addCancelButtonListener() {
        cancelButton.addClickListener(clickEvent -> clearInputFields());
    }

    private void addDeleteButtonListener() {
        deleteButton.addClickListener(clickEvent -> {
            contactList.removeContact(selectedItemId);
            clearInputFields();
            loadDataToTable();
        });
    }

    private void loadDataToTable() {
        table.removeAllItems();

        List<Contact> contacts = contactList.getContacts();

        for (Contact contact : contacts) {
            String firstName = contact.getFirstName();
            String lastName = contact.getLastName();
            String phone = contact.getPhone();
            String email = contact.getEmail();
            String id = contact.getId();

            Item item = table.addItem(id);
            Property p_field1 = item.getItemProperty("First Name");
            Property p_field2 = item.getItemProperty("Last Name");
            Property p_field3 = item.getItemProperty("Phone");
            Property p_field4 = item.getItemProperty("Email");
            p_field1.setValue(firstName);
            p_field2.setValue(lastName);
            p_field3.setValue(phone);
            p_field4.setValue(email);
            table.setValue(id);
        }

        table.select(null);
    }

    private void setTableOnClickListener() {
        table.addItemClickListener(itemClickEvent -> {
            Item item = itemClickEvent.getItem();
            firstNameTA.setValue(item.getItemProperty("First Name").getValue().toString());
            lastNameTA.setValue(item.getItemProperty("Last Name").getValue().toString());
            phoneTA.setValue(item.getItemProperty("Phone").getValue().toString());
            emailTA.setValue(item.getItemProperty("Email").getValue().toString());

            selectedItemId = (String) itemClickEvent.getItemId();
            deleteButton.setEnabled(true);
        });
    }
}