package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

class ServerClientConnect extends Thread implements Runnable {

	public ServerSocket servers;
	String names[] = null;
	public int own_num;
	public Socket socket = null;
	public Socket[] mySocket = null;
	String query = null;
	int count = 0;
	String sendMessge = "";
	public static int readyCount = 0;
	public static int playerInGame = 0;
	public static int sitPlayerCount = 0;
	public boolean oldStatus = false;
	public boolean gameWasStarted = false;
	public boolean gameIsEnd = false;
	public static String dataClear;
	public static boolean initDeck = false;
	public static String[] users = new String[6];
	public static String UsersPositions[] = new String[6];
	public static int isrefreshedGame = 0; 
	String userList = "";

	ServerUsersRequestManagement surm = new ServerUsersRequestManagement();
	GameEngine gameEngine = new GameEngine();
	


	public ServerClientConnect(Socket mysock, Socket[] mysocks, int count, String[] names, String query) {
		super("ServerClientConnect");
		this.socket = mysock;
		this.mySocket = mysocks;
		own_num = count;
		this.names = names;
		this.query = query;
	}

	public void setPlayerStatus(String getData) {

	}

	@Override
	public void run() {
		while (true) {
			try {
				if (mySocket[own_num] != null) {
					DataInputStream dataInputStream = new DataInputStream(mySocket[own_num].getInputStream());
					DataOutputStream dataOutputStream = new DataOutputStream(mySocket[own_num].getOutputStream());
					String getData = dataInputStream.readUTF();

					if (getData.contains("WHO:")) {
						for (String user : users) {
							if (user != null) {
								sendMessge += user + "---";
							} else {
								break;
							}
						}
						try {
							dataOutputStream.writeUTF(sendMessge);
						} catch (Exception e) {
							e.printStackTrace();
						}
						sendMessge = "";
					}
					if (getData.contains("getGameStatus")) {
						if (readyCount == sitPlayerCount && gameWasStarted == false && readyCount >= 2&& sitPlayerCount >= 2) {
							gameWasStarted = true;
							if(initDeck == false){
								new Deck().initDeck();
								initDeck = true;
								gameEngine.setInitialBetCard();
								String pathname = ClassLoader.getSystemClassLoader().getResource(".").getPath();
						        new File(pathname+"GamebetDeck.txt").createNewFile();
						        
						        try (PrintWriter writer = new PrintWriter(new File(pathname+"GameBetDeck.txt"))) {
						            writer.println("1");
						            writer.close();
						        }
							}
							
							String userData[] = getData.split("--");
							gameEngine.setPlayerInGame(sitPlayerCount);
							gameEngine.setPlayersHand(userData[1], new Deck().imitPlayer());

							for(int i = 0; i< UsersPositions.length; i++){
								if(UsersPositions[i]!=null){
									gameEngine.setPlayersPriority(UsersPositions[i]);
								}
							}
							dataOutputStream.writeUTF("true");
							dataOutputStream.flush();
						}else {
							dataOutputStream.writeUTF("false");
							dataOutputStream.flush();						
						}
					}
					
					if(getData.contains("GetOneCard")){
						String[] userData = getData.split("--");
						String gettingCard = new Deck().getRandomCards();
						gameEngine.addCard(userData[1], gettingCard);
						dataOutputStream.writeUTF("NewCardIsAdded");
						dataOutputStream.flush();
					}
					if(getData.contains("SKIP")){
						gameEngine.changeUserPriority();
						dataOutputStream.writeUTF("IsChanged");
						dataOutputStream.flush();
					}
					
					if(getData.contains("checkGameEnd")){
						if (gameWasStarted == true && sitPlayerCount < 2) {
							gameWasStarted = false;
							readyCount = 0;
							sitPlayerCount = 0;
							dataClear = gameEngine.clearPlayersPositions();
							if (dataClear.equals("AllDataClear")) {
								dataOutputStream.writeUTF("true");
								dataOutputStream.flush();
							}
						}else {
							dataOutputStream.writeUTF("false");
							dataOutputStream.flush();						
						}
					}
					if(getData.contains("CHECKGAMEREFRESH")){
						boolean partideEnd = gameEngine.getGameStatus();
						if(partideEnd == true){
							gameWasStarted = false;
							initDeck = false;
							if(isrefreshedGame == sitPlayerCount){
								gameEngine.setGameStatus(false);
								isrefreshedGame = 0;
							}else{ isrefreshedGame++;}
							dataOutputStream.writeUTF("NEWGAMEISSTARTED");
							dataOutputStream.flush();
							System.err.println("NEW GAME IS STARTED");
						}else{
							dataOutputStream.writeUTF("isnotrefreshedzzzzz");
							dataOutputStream.flush();
						}
					}
					if (getData.contains("getMyCards")) {
						String userData[] = getData.split("--");
						String hand = gameEngine.getPlayerHand(userData[1]);
						dataOutputStream.writeUTF("@@"+hand);
						dataOutputStream.flush();
					}
					if(getData.contains("ChangeTrump")){
						String userData[] = getData.split("--");
						gameEngine.changeDecktrump(userData[1]);
						dataOutputStream.writeUTF("trumpIsCganged");
						dataOutputStream.flush();
					}
					if(getData.contains("GetGameTrump")){
						dataOutputStream.writeUTF("GameTrumpIS--"+gameEngine.getGametrump());
						dataOutputStream.flush();
					}
					if (getData.contains("PutCard")) {
						String userData[] = getData.split("--");
						String s = gameEngine.checkCard(userData[1]);
						dataOutputStream.writeUTF("##" + s);
						dataOutputStream.flush();
						if(gameEngine.CanBeBet()){
							if(userData[1].contains("8") == false){
								gameEngine.changeUserPriority();
							}
							gameEngine.removeCard(userData[2], userData[1]);
							new Deck().CardRemove(userData[1] + "--");	
							gameEngine.checkGivenCard(userData[1], userData[2]);
						}

					}
					if (getData.contains("GetPosit")) {
						String response = "";
						for (int i = 0; i < UsersPositions.length; i++) {
							response += UsersPositions[i] + "-" + i + "---";
						}
						dataOutputStream.writeUTF(response);
						dataOutputStream.flush();
					}
					if (getData.contains("Position")) {
						String userData[] = getData.split("--");
						String username = userData[2];
						int position = Integer.parseInt(userData[1]) - 1;
						if (UsersPositions[position] == null) {
							UsersPositions[position] = username;
							sitPlayerCount++;
							dataOutputStream.writeUTF("Succes");
							dataOutputStream.flush();
						} else {
							dataOutputStream.writeUTF("NotSucces");
							dataOutputStream.flush();
						}

					}
					if (getData.contains("LeavePlace")) {
						String userData[] = getData.split("--");
						int position = Integer.parseInt(userData[2]) - 1;
						UsersPositions[position] = null;
						sitPlayerCount--;
						dataOutputStream.writeUTF("true");
					}
					if(getData.contains("Acces")){
						dataOutputStream.writeUTF("Acces->"+gameEngine.getAcces());
					}

					if (getData.contains("ClientDisconnected")) {
						own_num = new ServerMain().decrementCount(own_num);
						mySocket[own_num] = null;
						readyCount--;
						playerInGame--;
						String[] disconnoectClient = getData.split("--");
						for (int i = 0; i < 6; i++) {
							if (users[i] != null) {
								String[] trimUsers = users[i].split("--");
								if (trimUsers[0].equals(disconnoectClient[1])) {
									users[i] = null;
									break;
								}
							}
						}
						boolean isDisctonected = surm.DisconectPlayer(disconnoectClient[1]);
						if (isDisctonected == true) {
							surm.disconnectPlayerFromGame(disconnoectClient[1]);
						}
					}

					if (getData.contains("HOW_MANY")) {

						dataOutputStream.writeUTF(readyCount + "/" + playerInGame);

					}

					if (getData.contains("Username")) {
						boolean acces = surm.DetectTypeOfQuery(getData);
						String[] array = getData.split("-");
						if (acces == true) {
							playerInGame++;
							gameEngine.setInitialUserScore(array[1]);
							for (int i = 0; i < 6; i++) {
								if (users[i] == null) {
									users[i] = array[1] + "--not ready";
									i = 5;
								}
							}
						}

						dataOutputStream.writeUTF(String.valueOf(acces));
					}
					
					if(getData.contains("GETPLAYERSCORE")){
						
						dataOutputStream.writeUTF("PlayerScoreIs-#-"+gameEngine.getPlayerScore());
						dataOutputStream.flush();
					}
					
					
					if (getData.contains("DiconnectUser")) {
						String[] DisconnectPlayed = getData.split("--");

						for (int i = 0; i < users.length; i++) {
							if (users[i].contains(DisconnectPlayed[1])) {
								users[i] = DisconnectPlayed[1] + "--not ready";
								readyCount--;
								boolean isEndGame = surm.endGame(readyCount);
								dataOutputStream.writeBoolean(isEndGame);
								break;
							}
						}
					}
					if (getData.contains("AvailabilityStatus")) {
						String getDataArray[] = getData.split("--");
						String username = getDataArray[1];
						String status = getDataArray[2];

						for (int i = 0; i < users.length; i++) {
							if (users[i].contains(username)) {
								if (status.equals("true")) {
									status = "ready";
									readyCount++;
									surm.addUserToGame(username);
								}
								if (status.equals("false")) {
									status = "not ready";
									readyCount--;
									surm.disconnectPlayerFromGame(username);
								}
								users[i] = username + "--" + status;
								break;
							}
						}
						dataOutputStream.writeUTF("true");
					}
					if (getData.contains("checkIfGameIsStart")) {
						dataOutputStream.writeUTF(String.valueOf(surm.gameStart(readyCount, playerInGame)));
					}
					if (getData.contains("CheckIfGameIsEnd")) {
						dataOutputStream.writeUTF(String.valueOf(surm.checkIfGameIsEnd(readyCount)));
					}
				}
			} catch (IOException ex) {

				break;
			}
		}

	}

}
