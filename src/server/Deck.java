package server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Deck {

	private static String[] cardsInHandArray = new String[36];
	private String returnCard = null;
    BufferedReader reader;
    PrintWriter prnwriter;
    FileWriter filewrt;
    String sCurrentLineFromFile = null;

	private static ArrayList<String> cards = new ArrayList<>();
	private static ArrayList<String> betCards = new ArrayList<>();

	String[] suit = { "R", "D", "C", "V" };
	String[] rank = { "6", "7", "8", "9", "10", "J", "Q", "K", "A" };
	String pathname = ClassLoader.getSystemClassLoader().getResource(".").getPath();
	public Deck() {}

	public void CardRemove(String cardsInHand) {
		String [] getdata = cardsInHand.split("--");
		
		for (String cardInHand : getdata) {
			if(cardInHand!=null){
				cards.remove(cardInHand);
			}
		}
		System.out.println(cards.size());
		if(cards.size() == 0){
			shufleBetDeck();
			cards.clear();
			for(int i = 0; i < betCards.size() ; i++){
				cards.add(betCards.get(i));
			}
			shufleCards();
			clearBetDeckFile();
		}
	}
	
	private void clearBetDeckFile(){
		try {
			prnwriter = new PrintWriter(filewrt = new FileWriter(pathname + "GamebetDeck.txt", false));
			prnwriter.println("1");
			prnwriter.println(new GameEngine().returnBetCard());
			prnwriter.close();
		} catch (IOException e) {}
	}
	
	
	public void shufleBetDeck(){

        try {
			reader = new BufferedReader(new FileReader(pathname + "GamebetDeck.txt"));
			while ((sCurrentLineFromFile = reader.readLine()) != null) {
	        	if(!sCurrentLineFromFile.equals("1")){
	        		if(!betCards.contains(sCurrentLineFromFile)){
	        			betCards.add(sCurrentLineFromFile);
	        		}
	        	}
	        }
	        reader.close();
		} catch (FileNotFoundException e) {} catch (IOException e) {}

	}
	
	//Main method
	public String imitPlayer() {
		int cardToGive = 3;
		shufleCards();
		String playerHand = "";
		for (int i = 0; i < cardToGive; i++) {
			playerHand += getCards() + "--";
			CardRemove(playerHand);
		}
		shufleCards();
		return playerHand;
	}

	
	

	private void shufleCards() {
		int randomFirst;
		int randomSeccond;
		String tempBuffer = "";
		for (int i = 0; i < cards.size(); i++) {
			randomFirst = (int) (Math.random() * cards.size());
			randomSeccond = (int) (Math.random() * cards.size());
			tempBuffer = cards.get(randomFirst);
			cards.set(randomFirst, cards.get(randomSeccond));
			cards.set(randomSeccond, tempBuffer);
		}
	}

	public void initDeck() {
		int randomRank = 0;
		int randomSuit = 0;
		while (cards.size() < 36) {
			randomRank = (int) (Math.random() * 9);
			randomSuit = (int) (Math.random() * 4);
			String card = suit[randomSuit] + "-" + rank[randomRank];
			if (!cards.contains(card)) {
				cards.add(card);
			}
		}
	}
	
	public String getRandomCards(){
		return getCards();
	}

	private String getCards() {
		for (int i = 0; i < cards.size(); i++) {
			returnCard = cards.get(i);
			if (cardsInHandArray[i] != null) {
				if (!cardsInHandArray[i].equals(returnCard)) {
					cardsInHandArray[i] = returnCard;
					cards.remove(i);
					break;
				}
			} else {
				cardsInHandArray[i] = returnCard;
				break;

			}
		}
		CardRemove(returnCard);
		return returnCard;
	}
}
