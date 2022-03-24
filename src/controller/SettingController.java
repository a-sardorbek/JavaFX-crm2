
package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import databaseConnect.DbConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import utils.AlertHelper;


public class SettingController implements Initializable {

    @FXML
    private JFXTextField usernameField;
    @FXML
    private JFXTextField passwordField;
    @FXML
    private JFXTextField confirmPasswordFieldd;
    @FXML
    private JFXButton saveBtn;
    @FXML
    private JFXButton cancelBtn;

 
    private final Connection con;
    public SettingController(){
        con = DbConnection.getConnection(true);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }   
    
    
    
     private void executeSave(String query) {
        Statement st;
        try {
            st = con.createStatement();
            st.executeUpdate(query);
        } catch (SQLException e) {
        }
    }
     
     private void saveRecord(){
        String query = "insert into lorentusers (username,password) values ('"+usernameField.getText()+
                                                    "','"+passwordField.getText()+"')";
        executeSave(query);
        AlertHelper.showAlert(AnimationType.POPUP, "Yangi parol va username qo'shildi", "Iltimos username va parolni eslab qoling", NotificationType.SUCCESS, 8000);
       
    }
     
      private boolean isValidated(){
        
        if(usernameField.getText().equals("")){
            
            AlertHelper.showAlert(AnimationType.SLIDE, "Username kiritmadiz", "", NotificationType.WARNING, 2000);
            
            usernameField.requestFocus();
        } else if (passwordField.getText().equals("")) {
             AlertHelper.showAlert(AnimationType.SLIDE, "Parol kiritmadiz", "", NotificationType.WARNING, 2000);

            passwordField.requestFocus();
        }else if (confirmPasswordFieldd.getText().equals("")) {
             AlertHelper.showAlert(AnimationType.SLIDE, "Parolni tasdiqlang", "", NotificationType.WARNING, 2000);

            confirmPasswordFieldd.requestFocus();
        }else if (!passwordField.getText().equals(confirmPasswordFieldd.getText())) {
            
            AlertHelper.showAlert(AnimationType.SLIDE, "Parollar birxil emas", "", NotificationType.WARNING, 2000);
             
            confirmPasswordFieldd.requestFocus();
        }else {
            return true;
        }
        return false;
    }
    
    

    @FXML
    private void SabeClicked(MouseEvent event) {
        if(event.getSource()==saveBtn){
            if(isValidated()){
                saveRecord();
                clearFields();
            }
        }
    }

    @FXML
    private void cancelClicked(MouseEvent event) {
        if(event.getSource()==cancelBtn){
            clearFields();
        }
    }

    private boolean checkPasswords() {
        return passwordField.equals(confirmPasswordFieldd);
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        confirmPasswordFieldd.setText("");
    }
    
}
