package server;

public class GameRules {
	
	public GameRules(){}
	
	public static boolean itCanBeBet = false;
	public static String deckTrump = "";
	public static String betCardTrump = null;
	GameEngine gameEngine = new GameEngine();
	
	public void setGameTrump(String trump){
		GameRules.deckTrump = trump;
	}
	public boolean getCard(String betCard, String putCard){
		if(betCard!=null && putCard!=null){
			String [] getingCardData = putCard.split("-");
			if(betCard.contains(getingCardData[0]) ||
					betCard.contains(getingCardData[1]) ||
					deckTrump.contains(getingCardData[0]) || putCard.contains("J")){
				itCanBeBet = true;
				analysebetCard(putCard);
			}
			else itCanBeBet = false;
		}else{
			itCanBeBet = true;
		}
		
		return itCanBeBet;
	}
	public void analysebetCard(String card){
		if(card.equals("D-9")){
			gameEngine.giveFiveCards();
			gameEngine.skipPlayer();
		}else if(card.contains("6")){
			gameEngine.giveOneCardBack();
			gameEngine.skipPlayer();
		}else if(card.contains("7")){
			gameEngine.giveTwoCardsForward();
			gameEngine.skipPlayer();
		}else if(card.contains("A")){
			gameEngine.skipPlayer();
		}
	}
}
