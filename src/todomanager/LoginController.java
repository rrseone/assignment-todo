/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package todomanager;

import static com.sun.javaws.ui.SplashScreen.hide;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author rashid
 */
public class LoginController implements Initializable {

    @FXML
    private TextField todoUser;
    @FXML
    private PasswordField todoPass;
    @FXML
    private Label todoNotification;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void onActionLogin(ActionEvent event) throws IOException {
        String user = todoUser.getText();
        String pass = todoPass.getText();
        if(user.equals("user") && pass.equals("pass")) {
            todoNotification.setText("Login Success!");
            Parent root = FXMLLoader.load(getClass().getResource("TodoManager.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            ((Node)event.getSource()).getScene().getWindow().hide();
        } else {
            todoNotification.setText("Login Failed!");
        }
    }

    @FXML
    private void onActionSignUp(ActionEvent event) {
    }
    
}
