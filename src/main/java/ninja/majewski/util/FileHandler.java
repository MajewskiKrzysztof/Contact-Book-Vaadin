package ninja.majewski.util;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import ninja.majewski.model.Contact;
import org.w3c.dom.*;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    private static FileHandler instance;

    public static FileHandler getInstance() {
        if (instance == null)
            instance = new FileHandler();

        return instance;
    }

    public List<Contact> readContacts() {
        List<Contact> contacts = new ArrayList<>();

        try {
            URL resource = getClass().getResource("/contacts.xml");
            File fXmlFile = new File(resource.getPath());
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("contact");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Contact newContact = new Contact();
                    Element eElement = (Element) nNode;
                    newContact.setFirstName(eElement.getElementsByTagName("firstName").item(0).getTextContent());
                    newContact.setLastName(eElement.getElementsByTagName("lastName").item(0).getTextContent());
                    newContact.setPhone(eElement.getElementsByTagName("phone").item(0).getTextContent());
                    newContact.setEmail(eElement.getElementsByTagName("email").item(0).getTextContent());

                    contacts.add(newContact);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return contacts;
    }

    public void saveContacts(List<Contact> contacts) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("contacts");
            doc.appendChild(rootElement);

            for (Contact contact : contacts) {
                Element staff = doc.createElement("contact");
                rootElement.appendChild(staff);

                Element firstName = doc.createElement("firstName");
                firstName.appendChild(doc.createTextNode(contact.getFirstName()));
                staff.appendChild(firstName);

                Element lastName = doc.createElement("lastName");
                lastName.appendChild(doc.createTextNode(contact.getLastName()));
                staff.appendChild(lastName);

                Element nickname = doc.createElement("phone");
                nickname.appendChild(doc.createTextNode(contact.getPhone()));
                staff.appendChild(nickname);

                Element salary = doc.createElement("email");
                salary.appendChild(doc.createTextNode(contact.getEmail()));
                staff.appendChild(salary);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            URL resource = getClass().getResource("/contacts.xml");
            File fXmlFile = new File(resource.getPath());
            StreamResult result = new StreamResult(fXmlFile);

            transformer.transform(source, result);
        } catch (ParserConfigurationException | TransformerException pce) {
            pce.printStackTrace();
        }
    }
}