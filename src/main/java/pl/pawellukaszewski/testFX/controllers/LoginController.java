package pl.pawellukaszewski.testFX.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import pl.pawellukaszewski.testFX.dao.UserDao;
import pl.pawellukaszewski.testFX.dao.impl.UserDaoImpl;
import pl.pawellukaszewski.testFX.models.UserSession;
import pl.pawellukaszewski.testFX.models.Utils;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    TextField textLogin, textLoginR;

    @FXML
    PasswordField textPassword, textPasswordR, textPasswordRepeatR;

    @FXML
    Button buttonLogin, buttonRegister;

    @FXML
    CheckBox checkboxSavePass;

    private UserSession userSession = UserSession.getInstance();
    private UserDao userDao = new UserDaoImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textLogin.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER:
                    System.out.println("zalogowano");
                    tryLogin();
            }
        });
        textPassword.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER:
                    System.out.println("zalogowano");
                    tryLogin();
            }
        });

        buttonLogin.setOnMouseClicked(e -> tryLogin());
        buttonRegister.setOnMouseClicked(e -> tryRegister());
    }


    private boolean checkLoginData() {
        String login = textLogin.getText();
        String password = textPassword.getText();

        if (login.isEmpty() || password.isEmpty()) {
            Utils.createSimpleDialog("Logowanie", "", "Pola nie moga byc puste");
            return false;
        }
        if (login.length() <= 3 || password.length() <= 5) {
            Utils.createSimpleDialog("Logowanie", "", "Dane za krótkie");
            return false;
        }
        return true;
    }

    private void tryLogin() {
        String login = textLogin.getText();
        String password = textPassword.getText();

        if (!checkLoginData()) {
            return;
        }

        if (userDao.login(login, password)) {
            userSession.setUsername(login);
            userSession.setLogedIn(true);

            try {
                Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("viewMain.fxml"));
                Stage stageRoot = (Stage) buttonLogin.getScene().getWindow();
                stageRoot.setScene(new Scene(root, 600, 400));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Utils.createSimpleDialog("Logowanie", "", "Masz bład w danych");
        }
    }


    private boolean checkRegistrationData() {
        String login = textLoginR.getText();
        String password = textPasswordR.getText();
        String passwordRepeat = textPasswordRepeatR.getText();

        if (login.isEmpty() || password.isEmpty() || passwordRepeat.isEmpty()) {
            Utils.createSimpleDialog("Rejestracja", "", "Pola nie moga byc puste");
            return false;
        }
        if (!password.equals(passwordRepeat)) {
            Utils.createSimpleDialog("Rejestracja", "", "Hasła musza byc takie same");
            return false;
        }
        if (password.length() < 5 || login.length() < 5) {
            Utils.createSimpleDialog("Rejestracja", "", "Hasło musi byc dluzsze niz 5 znakow");
            return false;
        }
        if (login.equals(password)) {
            Utils.createSimpleDialog("Rejestracja", "", "Login i haslo musza sie roznic");
            return false;
        }

        return true;
    }

    private void tryRegister() {
        String login = textLoginR.getText();
        String password = textPasswordR.getText();


        if (!checkRegistrationData()) {
            return;
        }
        if (userDao.register(login, password)) {

            Utils.createSimpleDialog("Logowanie", "", "Zarejestrowano poprawnie");
        } else {
            Utils.createSimpleDialog("Logowanie", "", "Login juz istnieje");
        }
    }
}
