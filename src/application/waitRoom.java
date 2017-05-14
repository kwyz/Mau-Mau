package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class waitRoom  implements Initializable{
	
	public static  String myUsername;
	Main main = new Main();
	public String answer;
    private String userListString;
	
	@FXML
	private Button readyButton;
	@FXML
	private Button notReadyButton;
	@FXML
	private ListView<String> userList;
	@FXML
	private Label userNumber;
	@FXML
	private Button Play;
	
	boolean gameIsStart;
	
	public waitRoom(String username){
		waitRoom.myUsername = username;
	}
	
	
	public waitRoom(){}	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		notReadyButton.setDisable(true);
		
		Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {

		    @Override
		    public void handle(ActionEvent event) {
		    	try {
					userListString = main.Push("WHO:");
					String userReadyToPlay = main.Push("HOW_MANY");
					userNumber.setText("READY TO PLAY "+userReadyToPlay);
					 String[] DefaultModel = userListString.split("---");
			         for (int i = 0; i < DefaultModel.length; i++) {
			        	 DefaultModel[i] = DefaultModel[i].replaceAll("--", " is ");
			         }
					ObservableList<String> usersData = FXCollections.observableArrayList(DefaultModel);
					userList.setItems(usersData);
					userNumber.setText("READY TO PLAY "+userReadyToPlay);
					gameIsStart = Boolean.parseBoolean(main.Push("checkIfGameIsStart"));
					if(gameIsStart == true){
						Play.setVisible(true);
						gameIsStart = false;
						new playRoomControl().setUsername(myUsername);
						
					}
		    	} catch (IOException e) {
		    		
					e.printStackTrace();
				}
		    }
		}));
		fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
		fiveSecondsWonder.play();
		
	}
	
	
	@FXML
	public void changePane() {

		Play.setOnMouseClicked(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
			        Parent home_page_parent = null;
					try {
						home_page_parent = FXMLLoader.load(getClass().getResource("playRoomFXML.fxml"));
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

	@FXML
	public void readyToPlay(){
		readyButton.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
			    try {
			    	answer = main.Push("AvailabilityStatus--"+myUsername+"--"+String.valueOf(true));
			    	readyButton.setDisable(true);
			    	notReadyButton.setDisable(false);
	            } catch (IOException ex) {
	                Logger.getLogger(waitRoom.class.getName()).log(Level.SEVERE, null, ex);
	            }
			}
		});
	}
	
	@FXML
	public void notReadyToPlay(){
		notReadyButton.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				try {
			    	readyButton.setDisable(false);
			    	notReadyButton.setDisable(true);
					answer = main.Push("AvailabilityStatus--"+myUsername+"--"+String.valueOf(false));
	            } catch (IOException ex) {
	                Logger.getLogger(waitRoom.class.getName()).log(Level.SEVERE, null, ex);
	            }
			}
		});	
	}
}
