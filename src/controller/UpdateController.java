
package controller;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import databaseConnect.DbConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import utils.AlertHelper;


public class UpdateController implements Initializable {

    @FXML
    private JFXTextField nameField;
    @FXML
    private JFXTextField surnameField;
    
    @FXML
    private JFXTextField phoneField;
    @FXML
    private JFXTextField paymentField;
    @FXML
    private Button updateBtn;
    @FXML
    private JFXTextField birthField;
    
    
    private boolean update;
    int patientId;
  
    String query = null;
    ResultSet resultSet = null;
    PreparedStatement preparedStatement;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    private final Connection con;
    public UpdateController(){
        con = DbConnection.getConnection(true);
    }

    @FXML
    private void updateClicked(MouseEvent event) {
        if(event.getSource()==updateBtn){
            if(isValidated()){
                getQuery();
                insert();
                clearFields();
                
                Stage stage = (Stage) updateBtn.getScene().getWindow();
                stage.close();
                
            }
        }
    }
    
     private void clearFields(){
        nameField.setText("");
        surnameField.setText("");
        birthField.setText("");
        phoneField.setText("");
        paymentField.setText("");
    }
    
    
     private void getQuery() {

        if(update==true){
            query = "UPDATE patients1 SET "
                    + "name=?,"
                    + "surname=?,"
                    + "borthDate=?,"
                    + "phone=?,"
                    + "tolov=?"
                    + " WHERE deleted = false AND id = "+patientId+"";
        }

    }

    private void insert() {

        try {

            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, nameField.getText());
            preparedStatement.setString(2, surnameField.getText());
            preparedStatement.setString(3, birthField.getText());
            preparedStatement.setString(4, phoneField.getText());
            preparedStatement.setString(5, paymentField.getText());
            preparedStatement.execute();

        } catch (SQLException ex) {
        }

    }

    
     public void setUpdate(boolean update) {
        this.update = update;
    }
    
    public void setTextField(int id, String name, String surname, String birthDate,String phone, int payment) {
       patientId = id;
       nameField.setText(name);
       surnameField.setText(surname);
       birthField.setText(birthDate);
       phoneField.setText(phone);
       paymentField.setText(String.valueOf(payment));
               
    }
    
     private boolean isValidated(){
        
        if(nameField.getText().equals("")){
            
            AlertHelper.showAlert(AnimationType.SLIDE, "Ism kiritmadiz", "", NotificationType.WARNING, 2000);
            
            nameField.requestFocus();
        } else if (surnameField.getText().equals("")) {
             AlertHelper.showAlert(AnimationType.SLIDE, "Familiya kiritmadiz", "", NotificationType.WARNING, 2000);

            surnameField.requestFocus();
        }else if (birthField.getText().equals("")) {
             AlertHelper.showAlert(AnimationType.SLIDE, "Tugulgan sana kiritmadiz", "", NotificationType.WARNING, 2000);

            birthField.requestFocus();
        }else if (checkPhone()==false) {
            
            AlertHelper.showAlert(AnimationType.SLIDE, "Telefon xato yoki kiritilmadi\nNa'muna 901234567\nRaqamlar orasida joy tashlamang", "", NotificationType.WARNING, 8000);
             
            phoneField.requestFocus();
        }else if (checkPayment()==false) {
             AlertHelper.showAlert(AnimationType.SLIDE, "To'lov xato yoki kiritilmadi\nBarchasi raqamlar ekanligiga etibor bering", "", NotificationType.WARNING, 6000);

            paymentField.requestFocus();
        }  else {
            return true;
        }
        return false;
    }
     
     private boolean checkPhone(){
        return  phoneField.getText().length() == 9 && phoneField.getText().chars().allMatch(x -> Character.isDigit(x));
    }
    
    private boolean checkPayment(){
        return  !paymentField.getText().isEmpty() && paymentField.getText().chars().allMatch(x -> Character.isDigit(x));
    }
    
}
