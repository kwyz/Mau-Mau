package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Node;

public class StartUpcController implements Initializable {

	Main main = new Main();
	public String username;
	boolean acces = false;
	@FXML
	private Button signUp;
	@FXML
	private TextField usernameField;
	@FXML
	private Label usernameStatus;
	@FXML
	private Button Continue;
	
	public StartUpcController() {
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	@FXML
	public void changePane() {

			Continue.setOnMouseClicked(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
			        Parent home_page_parent = null;
					try {
						home_page_parent = FXMLLoader.load(getClass().getResource("waitRoomFXML.fxml"));
					} catch (IOException e) {
						e.printStackTrace();
					}
			        Scene home_page_scene = new Scene(home_page_parent);
			        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	                app_stage.hide(); 
	                app_stage.setScene(home_page_scene);
	                app_stage.show(); 

				}
				
			});
	}

	public boolean checkUserLength(String userName) {
		boolean goodUsername = false;
		if(userName.length() >1 && userName.length() <9){
			goodUsername = true;
		}
		return goodUsername;
	}

	@FXML
	public void checkUsername() {

		signUp.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				username = usernameField.getText();
				if (checkUserLength(username)) {
					try {
						new waitRoom(username);
						main.getUsername(username);
						acces = Boolean.parseBoolean(main.Push("Username-"+username));
					} catch (IOException ex) {
						Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
					}
					if (acces == true) {
						usernameStatus.setTextFill(Color.web("#1cae65"));
						usernameStatus.setText("This username is avaible");
						Continue.setVisible(true);
		
					} else if (acces == false) {
						usernameStatus.setTextFill(Color.web("#ff0000"));
						usernameStatus.setText("Sorry. Username [" + username + "] is already in use!");
					}

				} else {
					usernameStatus.setTextFill(Color.web("#ff0000"));
					usernameStatus.setText("Username should be bigger than 1 character \n and less than 8 characters");
				}

			}
		});
	}
}
