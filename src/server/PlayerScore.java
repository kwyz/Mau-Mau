package server;

import java.util.HashMap;

public class PlayerScore {
	
	public PlayerScore(){}
	
	public static int playerScore;
	public static String playerName;
	public static String playerHandCards;
	
	public Integer returnUserScore(String username, String cardInHand, int currentPlayerScore){
		PlayerScore.playerName = username;
		PlayerScore.playerHandCards = cardInHand;
		PlayerScore.playerScore = currentPlayerScore;
		CountUserScore();
		
	return playerScore;
	}

	public Integer CountUserScoreForWinner(String username, String card,int currentPlayerScore){
		System.out.println("PLAYER SCORE"+username+"  "+card+"   "+ currentPlayerScore);
		
		if(card.contains("V-Q")){
			currentPlayerScore = currentPlayerScore - 40;
		}else if(card.contains("C-Q") || card.contains("D-Q") || card.contains("R-Q")){
			currentPlayerScore = currentPlayerScore - 30;
		}else if(card.contains("J")){
			currentPlayerScore = currentPlayerScore - 20;
		}
		System.out.println( username+"  ["+currentPlayerScore+"]");
		return currentPlayerScore;
	}
	
	private void CountUserScore(){
		String [] cardInHandArray = playerHandCards.split("--");
		if(cardInHandArray.length > 0 && cardInHandArray.length <2){
			if(cardInHandArray[0].contains("V-Q")){
				playerScore = 40;
			}else if(cardInHandArray[0].contains("C-Q") || cardInHandArray[0].contains("D-Q") || cardInHandArray[0].contains("R-Q")){
				playerScore = 30;
			}else if(cardInHandArray[0].contains("J")){
				playerScore = 20;
			}
		}else{
			for(int i = 0; i< cardInHandArray.length; i++){
				if(cardInHandArray[i].contains("J")){
					playerScore+=2;
				}else if(cardInHandArray[i].contains("Q")){
					playerScore+=3;
				}else if(cardInHandArray[i].contains("K")){
					playerScore+=4;
				}else if(cardInHandArray[i].contains("A")){
					playerScore+=11;
				}else if(cardInHandArray[i].contains("6")){
					playerScore+=6;
				}else if(cardInHandArray[i].contains("7")){
					playerScore+=7;
				}else if(cardInHandArray[i].contains("8")){
					playerScore+=8;
				}else if(cardInHandArray[i].contains("9")){
					playerScore+=9;
				}else if(cardInHandArray[i].contains("10")){
					playerScore+=10;
				}
			}
			
		}
	}
}
