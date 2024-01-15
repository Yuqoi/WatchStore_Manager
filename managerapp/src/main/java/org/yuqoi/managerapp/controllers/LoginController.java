package org.yuqoi.managerapp.controllers;

import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.yuqoi.managerapp.utils.DatabaseConnector;
import org.yuqoi.managerapp.utils.PasswordHasher;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;



public class LoginController implements Initializable {


    // BUTTONS
    public Button exitBtn;
    public Button minBtn;
    public Button loginBtn;

    // TEXTFIELDS
    public TextField loginTextPanel;
    public PasswordField passwordTextPanel;
    public Label warningText;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loginBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!loginTextPanel.getText().isBlank() || !passwordTextPanel.getText().isBlank()){
                    String hashedPassword = PasswordHasher.passwordHasher(passwordTextPanel.getText());
                    validateLogin(loginTextPanel.getText(), hashedPassword);
                }else{
                    // if the data is null it will show us a warning
                    warningText.setText("Please enter data");
                    warningText.setTextFill(Color.RED);
                }
            }
        });

        minBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage stage = (Stage) minBtn.getScene().getWindow();
                stage.setIconified(true);
            }
        });

        exitBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage stage = (Stage) exitBtn.getScene().getWindow();
                stage.close();
            }
        });
    }

    // check if the given namne and password is correct
    public void validateLogin(String givenName, String givenPassword){
        Connection conn = DatabaseConnector.getConnection();

        String verify = "SELECT count(1) FROM login WHERE name = ? AND password = ?;";

        try {
            PreparedStatement st = conn.prepareStatement(verify);
            st.setString(1, givenName);
            st.setString(2, givenPassword);

            ResultSet queryResult = st.executeQuery();

            while (queryResult.next()){
                if (queryResult.getInt(1) == 1){
                    warningText.setText("-Login Approved-");
                    warningText.setTextFill(Color.GREEN);

                }else{
                    warningText.setText("-Invalid Login-");
                    warningText.setTextFill(Color.RED);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
