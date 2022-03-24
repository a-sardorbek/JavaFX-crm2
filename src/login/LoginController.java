
package login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import databaseConnect.DbConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import utils.AlertHelper;


public class LoginController implements Initializable {
    
    @FXML
    private JFXTextField usernameField;
    @FXML
    private JFXPasswordField passwordField;
    @FXML
    private JFXButton kirishBtn;
    @FXML
    private ImageView exitIBtn;
    
    Window window;
    @FXML
    private Text registerText;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DbConnection.setUser();
        DbConnection.setPatientTable();
    } 
    
    
    private final Connection con;
    
    public LoginController(){
        con =  DbConnection.getConnection(true);
    }
        private double x,y;
    @FXML
    private void kirishBtnPress(ActionEvent event) throws IOException {
       
        if(event.getSource() == kirishBtn){
            if(this.isValidated()){
                PreparedStatement prs;
                ResultSet rs;
                
                String query = "select * from lorentusers where username=? and password=?";
                try{
                        prs = con.prepareStatement(query);
                        prs.setString(1, usernameField.getText());
                        prs.setString(2, passwordField.getText());
                        rs = prs.executeQuery();
                        
                        if(rs.next()){
                            AlertHelper.showAlert(AnimationType.SLIDE, "To'gri", "Siz 'Lorent' dasturidan foydalanishiz mumkun.", NotificationType.SUCCESS, 2000);
                            
                            Stage stage = (Stage) kirishBtn.getScene().getWindow();
                            stage.close();
                    
                            Parent root = FXMLLoader.load(getClass().getResource("/view/Dashboard.fxml"));
                            Scene scene = new Scene(root);
                            root.setOnMousePressed((MouseEvent event1) -> {
                            x = event1.getSceneX();
                            y= event1.getSceneY();
                             });
        
        
                            root.setOnMouseDragged((MouseEvent event2) -> {
                                stage.setX(event2.getScreenX()-x);
                                stage.setY(event2.getScreenY()-y);
                            });
                            stage.setScene(scene);
                            stage.setTitle("Admin Panel");
                            stage.show();
                        }else{
                            AlertHelper.showAlert(AnimationType.SLIDE, "Xato", "Qayta urunib ko'ring", NotificationType.ERROR, 2000);
                        }
                }catch(SQLException | IOException e){
                    
                }
                
            }
        }
    }

    @FXML
    private void exit(MouseEvent event) {
        DbConnection.getConnection(false);
        Stage stage = (Stage) exitIBtn.getScene().getWindow();
        stage.close();
        System.exit(0);
    }
    
    private boolean isValidated(){
        window = kirishBtn.getScene().getWindow();
        
        if(usernameField.getText().equals("")){
            
            AlertHelper.showAlert(AnimationType.SLIDE, "Username kiritmadiz", "Username kiritish kerak", NotificationType.WARNING, 2000);
            
            usernameField.requestFocus();
        } else if (passwordField.getText().equals("")) {
             AlertHelper.showAlert(AnimationType.SLIDE, "Password kiritmadiz", "Password kiritish kerak", NotificationType.WARNING, 2000);

            passwordField.requestFocus();
        } else {
            return true;
        }
        return false;
    }

    @FXML
    private void registerClick(MouseEvent event) {
        
            loadUI("Setting");
        
    }
    
     private void loadUI(String ui){
        Parent root;
        try{
            root = FXMLLoader.load(getClass().getResource("/view/"+ui+".fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
             stage.show();
        }catch(IOException e){
            System.out.println("error in loadPAge");
        }
       
    }

  
    
}
