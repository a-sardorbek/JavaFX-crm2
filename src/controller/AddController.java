package controller;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import databaseConnect.DbConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import utils.AlertHelper;


public class AddController implements Initializable {

    @FXML
    private JFXTextField nameField;
    @FXML
    private JFXTextField surnameField;
    @FXML
    private JFXDatePicker borhField;
    @FXML
    private JFXTextField phoneField;
    @FXML
    private JFXTextField paymentField;
    @FXML
    private Button addBtn;

    
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }   
    
    private final Connection con;
    
    public AddController(){
        con = DbConnection.getConnection(true);
    }    
    
    
    private void saveRecord(){
        SimpleDateFormat regDate1 = new SimpleDateFormat("dd-MM-yyyy");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedString = borhField.getValue().format(formatter);
        String query = "insert into patients1 (name,surname,borthDate,phone,tolov,regDate,deleted) values ("+
                
                                                     
                                                     "'"+nameField.getText()+
                                                    "','"+surnameField.getText()+
                                                    "','"+formattedString+
                                                    "','"+phoneField.getText()+
                                                    "',"+paymentField.getText()+
                                                    ",'"+regDate1.format(new Date())+
                                                    "',"+false+")";
        executeSave(query);
        AlertHelper.showAlert(AnimationType.SLIDE, "Qo'shildi", "Yangi bemor kiritildi", NotificationType.SUCCESS, 2000);
       
    }
    
    private void executeSave(String query) {
        Statement st;
        try {
            st = con.createStatement();
            st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addClicked(MouseEvent event) {
        if(isValidated()){
        if(event.getSource() == addBtn){
            saveRecord();
            clearFields();
        }
        }
    }
    
    private void clearFields(){
        nameField.setText("");
        surnameField.setText("");
        borhField.getEditor().clear();
        phoneField.setText("");
        paymentField.setText("");
        
    }

    @FXML
    private void cancelClicked(MouseEvent event) {
        clearFields();
    }
    
    private boolean isValidated(){
        
        if(nameField.getText().equals("")){
            
            AlertHelper.showAlert(AnimationType.SLIDE, "Ism kiritmadiz", "", NotificationType.WARNING, 2000);
            
            nameField.requestFocus();
        } else if (surnameField.getText().equals("")) {
             AlertHelper.showAlert(AnimationType.SLIDE, "Familiya kiritmadiz", "", NotificationType.WARNING, 2000);

            surnameField.requestFocus();
        }else if (borhField.getValue()==null) {
             AlertHelper.showAlert(AnimationType.SLIDE, "Tugulgan sana kiritmadiz", "", NotificationType.WARNING, 2000);

            borhField.requestFocus();
        }else if (checkPhone()==false) {
            
            AlertHelper.showAlert(AnimationType.SLIDE, "Telefon xato yoki kiritilmadi\nNa'muna 901234567\nRaqamlar orasida joy tashlamang\nRaqamlar soni 9 ta bo'lish kerak", "", NotificationType.WARNING, 8000);
             
            borhField.requestFocus();
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
    
