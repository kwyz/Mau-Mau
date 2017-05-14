package server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map.Entry;

public class GameEngine {
	public static HashMap<String, String> user = new HashMap<String, String>();
	public static HashMap<String, Integer> userScore = new HashMap<String, Integer>();
	public static String playerHand = "";
	public static String closeGamePlayer = "";
	public static String closeGamePlayerCard ="";
	PlayerScore playerScoreObj = new PlayerScore();
	public static boolean gameIsClose = false;
	public static String[] betDeck = new String[36];
	public static boolean fiindCard = false;
	public static String[] userInGame;
	public static String acces = "";
	public static int userInGameCount;
	public static boolean itCanBeBet = false;
	public static String gameTrump ="";
    BufferedReader reader;
    PrintWriter prnwriter;
    FileWriter filewrt;
    String sCurrentLineFromFile = null;
    String pathname = ClassLoader.getSystemClassLoader().getResource(".").getPath();
	public static String[] matches = new String[]{"Q","J","K","10"};
	public boolean someoneLoseGame = false;

	public GameEngine() {
	}

	public Boolean getGameStatus(){
		return gameIsClose;
	}
	public void setGameStatus(Boolean gameStatus){
		GameEngine.gameIsClose = gameStatus;
	}
	
	public void setInitialUserScore(String username){
			userScore.put(username, 0);
	}
	public String[] returnBetDeck(){
		String[] returnedDeck = new String[betDeck.length-1];
		for(int i = 1; i < returnedDeck.length; i++){
			System.err.print(betDeck[i]+" ");
			returnedDeck[i - 1] = betDeck[i];
			betDeck[i] = null;
		}
		
		return returnedDeck;
	}
	
	

	public void setPlayerInGame(int sitPlayerCount) {
		if (userInGame == null) {
			userInGame = new String[sitPlayerCount];
		}
	}

	public void setPlayersPriority(String playerName) {
		for (int i = 0; i < userInGame.length; i++) {
			if (userInGame[i] == null) {
				userInGame[i] = playerName;
				break;
			}
		}
		
	}

	public void changeUserPriority() {
		String firstUser = userInGame[0];
		for (int userID = 0; userID < userInGame.length - 1; userID++) {
			userInGame[userID] = userInGame[userID + 1];
		}
		userInGame[userInGame.length - 1] = firstUser;
		itCanBeBet = false;
	}

	public String getAcces() {
		return userInGame[0];
	}

	public void setPlayersHand(String playerName, String playerHands) {
		user.put(playerName, playerHands);
	}


	public void removeCard(String username, String card) {

		String userCards = null;
		for (Entry<String, String> entry : user.entrySet()) {
			if (entry.getKey().equals(username)) {
				userCards = entry.getValue().replaceAll(card + "--", "");
			}

		}
		user.remove(username);
		user.put(username, userCards);
	}

	public String getPlayerHand(String playerName) {
		for (Entry<String, String> uniqueUser : user.entrySet()) {
			if (uniqueUser.getKey().equals(playerName)) {
				playerHand = uniqueUser.getValue();
				break;
			}
		}
		System.out.println(playerName +"   "+ playerHand);
		return playerHand + "-->" + betDeck[0];
	}

	public boolean fiindCard(String getCard) throws IOException {
		try {
			reader = new BufferedReader(new FileReader(pathname + "UserDataBase.txt"));
			while ((sCurrentLineFromFile = reader.readLine()) != null) {
	            if (sCurrentLineFromFile.equals(getCard)) {
	                reader.close();
	                fiindCard = true;
	                break;
	            } else {
	            	fiindCard = false;
	            }
	        }
		} catch (FileNotFoundException e) {}
        
		return fiindCard;
	}

	public String shiftBetDeck(String getCard) throws IOException {
		if (new GameRules().getCard(betDeck[0], getCard)) {
			itCanBeBet = true;
			if (fiindCard(getCard) == false) {
				try {
					prnwriter = new PrintWriter(filewrt = new FileWriter(pathname + "GamebetDeck.txt", true));
		            prnwriter.println(getCard);
		            prnwriter.flush();
		            filewrt.flush();
		            reader.close();
		        }
				catch (IOException e) {}
				} 
				betDeck[0] = getCard;
				String [] gameTrumpArray = betDeck[0].split("-");
				gameTrump = gameTrumpArray[0];
			}
		return betDeck[0];
	}

	public boolean CanBeBet() {
		return itCanBeBet;
	}

	public String checkCard(String getCard) throws IOException {
		return shiftBetDeck(getCard);
	}

	public String clearPlayersPositions() {
		user.clear();
		for (int i = 0; i < betDeck.length; i++) {
			betDeck[i] = null;
		}
		return "AllDataClear";
	}

	public void setInitialBetCard() {
		betDeck[0] = new Deck().getRandomCards();
		String[] array = betDeck[0].split("-");
		changeDecktrump(array[0]);
		

	}
	public String returnBetCard(){
		return betDeck[0];
	}

	public void giveFiveCards() {
		String userCards = null;
		playerHand = "";
		for (Entry<String, String> entry : user.entrySet()) {
			if (entry.getKey().equals(userInGame[1])) {
				
				for (int i = 0; i < 5; i++) {
					String randomCaard = new Deck().getRandomCards() + "--";
					new Deck().CardRemove(randomCaard);
					playerHand += randomCaard;
				}
				userCards = entry.getValue()+playerHand;
				break;
			}
		}
		user.remove(userInGame[1]);
		user.put(userInGame[1], userCards);
	}
	public void addCard(String username, String card) {
		String userCards = null;
		for (Entry<String, String> entry : user.entrySet()) {
			if (entry.getKey().equals(username)) {
				userCards = entry.getValue() + card + "--";
			}

		}
		user.remove(username);
		user.put(username, userCards);

	}
	public void giveOneCardBack() {
		String userCards = null;
		playerHand = "";
		for (Entry<String, String> entry : user.entrySet()) {
			if (entry.getKey().equals(userInGame[userInGame.length-1])) {
				
				for (int i = 0; i < 1; i++) {
					String randomCaard = new Deck().getRandomCards() + "--";
					new Deck().CardRemove(randomCaard);
					playerHand += randomCaard;
				}
				userCards = entry.getValue()+playerHand;
			}
		}
		user.remove(userInGame[1]);
		user.put(userInGame[1], userCards);
	}

	public void giveTwoCardsForward() {
		String userCards = null;
		playerHand = "";
		for (Entry<String, String> entry : user.entrySet()) {
			if (entry.getKey().equals(userInGame[1])) {
				
				for (int i = 0; i < 2; i++) {
					playerHand += new Deck().getRandomCards() + "--";
					new Deck().CardRemove(playerHand);
				}
				userCards = entry.getValue()+playerHand;
			}
		}
		user.remove(userInGame[1]);
		user.put(userInGame[1], userCards);
		
	}
	public void skipPlayer() {
	 changeUserPriority();
	}

	public void changeDecktrump(String trump) {
		GameEngine.gameTrump = trump;
	}

	public String getGametrump() {
		new GameRules().setGameTrump(gameTrump);
		return gameTrump;
	}

	public void checkGivenCard(String card, String username) {
		System.out.print("CHECK GIVEN CARD "+ card+"   "+username);

		for(int i = 0; i < matches.length; i++){
			if(card.contains(matches[i])){
				System.out.println("checkGivenCard");
				checkGameEnd(username, card);
			}
		}
	}
	private void checkGameEnd(String username,String card) {
		for (Entry<String, String> uniqueUser : user.entrySet()) {
			if (uniqueUser.getKey().equals(username)) {
				System.out.println("CheckGameEnd "+ uniqueUser.getKey()+"   " + uniqueUser.getValue());
				if(uniqueUser.getValue().length()<1){
					GameEngine.closeGamePlayer = username;
					setGameEnd(card);
					gameIsClose = true;
					System.err.println("GAME IS CLOSE");
				}
				break;
			}
		}
	}
	private void setGameEnd(String closeCard) {
		System.out.print("SETGAMEEND "+closeGamePlayer);
		for (Entry<String, Integer> entry : userScore.entrySet()) {
			if(!entry.getKey().equals(closeGamePlayer)){
				System.out.println("NOT EQUAL");
				for(Entry<String, String> userCards : user.entrySet()){
					if(userCards.getKey().equals(entry.getKey())){
						int currentPlayerScore = playerScoreObj.returnUserScore(entry.getKey(), userCards.getValue(), entry.getValue());
						new Deck().CardRemove(userCards.getValue());
						userScore.replace(entry.getKey(), entry.getValue(), currentPlayerScore);
						System.out.print(entry.getKey()+"   "+entry.getValue()+"   "+currentPlayerScore);
					}
				}
			}else if(entry.getKey().equals(closeGamePlayer)){
				for(Entry<String, String> userCards : user.entrySet()){
					if(userCards.getKey().equals(closeGamePlayer)){
						Integer currentPlayerScore =  playerScoreObj.CountUserScoreForWinner(entry.getKey(), closeCard, entry.getValue());
						userScore.replace(entry.getKey(), entry.getValue(), currentPlayerScore);
						System.out.print(entry.getKey()+"   "+entry.getValue()+"   "+currentPlayerScore);
					}
				}
			}
		}
		
	}
	public String getPlayerScore() {
		String playersScore = "";
		for(Entry<String, Integer> entry : userScore.entrySet()){
			System.out.println(entry.getKey() +"    "+entry.getValue());
			if(entry.getValue() > 101){
				ClearUserScore();
			}
			if(entry.getValue() == 100){
				userScore.replace(entry.getKey(), 0);
			}
			playersScore = playersScore.concat(entry.getKey()+"="+entry.getValue()+"-----");
		}		
		return playersScore;
	}

	public void ClearUserScore() {
		for(Entry<String, Integer> entry : userScore.entrySet()){
			userScore.replace(entry.getKey(), 0);
		}
	}
}
