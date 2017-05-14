package application;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;



public class Main extends Application {
	
    AnchorPane pane;
    InetAddress ipAdress;
    public String resp;
    static String username;
    public static  DataOutputStream dataOutputStream;
    public static DataInputStream dataInputStream; 
    public static  Socket socket;
    
    public Main(){}
    
	public void getUsername(String Username){
		Main.username = Username;
	}
	
    protected void DisconectUser(String username) throws IOException{
        dataOutputStream.writeUTF("ClientDisconnected--"+username);
        socket.close();
    }
    
    protected void Disconecting() throws IOException{
        socket.close();
    }

    
    protected String Push(String query) throws IOException{
    	dataOutputStream.writeUTF(query);
    	resp = dataInputStream.readUTF();
    	//System.out.println(resp);
    	return resp;
    } 
    
    
	@Override
	public void start(Stage stage) throws IOException, InterruptedException {

	    Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
	    Scene scene = new Scene(root);
	    stage.setScene(scene);
	    stage.show();   
	    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	    	public void handle(WindowEvent we) {
	    		try {
	    			dataOutputStream.writeUTF("ClientDisconnected--"+username);
	    			dataOutputStream.flush();
	    			dataOutputStream.close();
	    			dataInputStream.close();
					socket.close();
					} catch (IOException e) {}
	                stage.close();
	            }
	        });           
	}  
   
	public static void main(String[] args) throws IOException {
        socket = new Socket(InetAddress.getLocalHost().getHostAddress(), 16000);
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataInputStream = new DataInputStream(socket.getInputStream());
		launch(args);
	}
}
