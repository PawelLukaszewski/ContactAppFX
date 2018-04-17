package pl.pawellukaszewski.testFX.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pl.pawellukaszewski.testFX.dao.ContactDao;
import pl.pawellukaszewski.testFX.dao.impl.ContactDaoImpl;
import pl.pawellukaszewski.testFX.models.UserSession;
import pl.pawellukaszewski.testFX.models.Utils;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    TextField textNumber, textName, textNewName, textNewNumber;

    @FXML
    ListView<String> listsContact;

    @FXML
    Button buttonLogout, buttonAdd, buttonRemoveContact;


    private ObservableList contactsItems;

    private UserSession session = UserSession.getInstance();
    private ContactDao contactDao = new ContactDaoImpl();


    public void initialize(URL location, ResourceBundle resources) {
        textName.setEditable(false);
        textNumber.setEditable(false);
        loadContacts();


        listsContact.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            textName.setText(newValue);
            textNumber.setText(contactDao.getNumber(newValue));
        });

        buttonLogout.setOnMouseClicked(event -> logOut());

        updateActionsname();
        updateActionsnumber();


        textNewName.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER:
                    System.out.println("zalogowano");
                    addContacts();
            }
        });
        textNewNumber.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER:
                    System.out.println("Dodano kontakt");
                    addContacts();
            }
        });

        buttonAdd.setOnMouseClicked(e -> addContacts());
        buttonRemoveContact.setOnMouseClicked(e -> deleteContact());
    }

    private void deleteContact() {
        contactDao.removeContact(listsContact.getSelectionModel().getSelectedItem());
        Utils.createSimpleDialog("Usuwanie", "", "Poprawnie wyrzuciłes kontakt");
        loadContacts();
    }

    private boolean checkAddContactData() {
        String name = textNewName.getText();
        String number = textNewNumber.getText();


        if (name.isEmpty() || number.isEmpty() ) {
            Utils.createSimpleDialog("Dodawanie", "", "Pola nie moga byc puste");
            return false;
        }


        return true;
    }


    private void addContacts() {
        String name = textNewName.getText();
        String number = textNewNumber.getText();


        if (!checkAddContactData()) {
            return;
        }
        if (contactDao.addContact(name, number)) {

            Utils.createSimpleDialog("Logowanie", "", "Poprawnie dodano kontakt");
        } else {
            Utils.createSimpleDialog("Logowanie", "", "Nazwy kontaktów musza być rózne");
        }

        textNewName.clear();
        textNewNumber.clear();

        loadContacts();
    }

    private void updateActionsname() {
        textName.setOnMouseClicked(e -> {
            if (e.getClickCount() >= 2) {
                textName.setEditable(true);
            }
        });
        textName.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                contactDao.editContact(textName.getText(), textNumber.getText(), listsContact.getSelectionModel().getSelectedItem());
                loadContacts();
                textName.setEditable(false);
            }
        });
    }

    private void updateActionsnumber() {
        textNumber.setOnMouseClicked(e -> {
            if (e.getClickCount() >= 2) {
                textNumber.setEditable(true);
            }
        });
        textNumber.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                contactDao.editContact(textName.getText(), textNumber.getText(), listsContact.getSelectionModel().getSelectedItem());
                loadContacts();
                textNumber.setEditable(false);
            }
        });
    }

    private void logOut() {
        session.setLogedIn(false);
        session.setUsername(null);
        session.setId(0);

        Stage stage = (Stage) buttonLogout.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("loginView.fxml"));
            stage.setScene(new Scene(root, 600, 400));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void loadContacts() {

        contactsItems = FXCollections.observableArrayList(contactDao.getAllContactsName(session.getUsername()));
        listsContact.setItems(contactsItems);
    }
}

