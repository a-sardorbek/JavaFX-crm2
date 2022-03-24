
package login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Login extends Application {
    private double x,y;
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        
        Scene scene = new Scene(root);
        
         root.setOnMousePressed((MouseEvent event) -> {
            x = event.getSceneX();
            y= event.getSceneY();
        });
        
        
        root.setOnMouseDragged((MouseEvent event) -> {
            stage.setX(event.getScreenX()-x);
            stage.setY(event.getScreenY()-y);
        });
        stage.getIcons().add(new Image("/images/st.png"));
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        
        scene.setFill(Color.TRANSPARENT);
        stage.show();
    }

   
    public static void main(String[] args) {
        launch(args);
    }
    
}
