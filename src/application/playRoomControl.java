package application;

import javafx.scene.paint.Color;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javafx.util.Duration;

public class playRoomControl implements Initializable {

	public playRoomControl() {
	}

	Main main = new Main();
	@FXML
	private Button nextCard;
	@FXML
	private Button prevCard;
	@FXML
	private Button leaveGame;
	@FXML
	private Button leavePlace;
	@FXML
	private Button quitRoom;
	@FXML
	private ImageView betCard;
	@FXML
	private ImageView myCard1;
	@FXML
	private ImageView myCard2;
	@FXML
	private ImageView myCard3;
	@FXML
	private ImageView myCard4;
	
	@FXML
	private ImageView Doba;
	@FXML
	private ImageView Rosu;
	@FXML
	private ImageView Cruce;
	@FXML
	private ImageView Verde;
	@FXML
	private Label choseSuits;
	
	
	@FXML
	private ImageView userIcon1;
	@FXML
	private ImageView userIcon2;
	@FXML
	private ImageView userIcon3;
	@FXML
	private ImageView userIcon4;
	@FXML
	private ImageView userIcon5;
	@FXML
	private ImageView userIcon6;

	@FXML
	private Label userName1;
	@FXML
	private Label userName2;
	@FXML
	private Label userName3;
	@FXML
	private Label userName4;
	@FXML
	private Label userName5;
	@FXML
	private Label userName6;
	@FXML
	private Label leaveGameTimer;
	@FXML
	private ProgressIndicator thinkingTimer;
	@FXML
	private Label timeThinkingLabel;
	@FXML
	private Label UserIsThinking;
	@FXML
	private ImageView deck;
	@FXML
	private Label trumpType;
	@FXML
	private Button skipButton;
	@FXML
	private ListView<String> userListScore;
	
	

	Image userImg = new Image("user.png");
	Image unknownUserImg = new Image("unknown_user.png");
	Image betImage;

	public static Image[] myCardImages = new Image[36];
	public static Image[] myCardImagesToDisplay = new Image[4];
	public static HashMap<Image, String> myCardsValues = new HashMap<>();
	public static String myUsername;
	public static String myPosition;
	public static String myCardHand;
	public static String myOldCardHand = "";
	public static String myAcces = "";
	public static boolean alreadyTake = false;

	String playerNamePosition;
	public String gameIsStarted = "";
	public static int iterator = 0;

	public static double cardX = 0;
	public static double cardY = 0;

	public void setUsername(String username) {
		playRoomControl.myUsername = username;
	}

	@FXML
	public void skip() {
		skipButton.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				try {
					String s = main.Push("SKIP");
					if (alreadyTake == false) {
						String message = main.Push("GetOneCard--" + myUsername);
					}
				} catch (IOException e) {
				}

			}
		});
	}

	public void getMyCardsToDisplay(Image[] myCardImage) {
		int j = 0;
		for (int i = 0; i < myCardImagesToDisplay.length; i++) {
			myCardImagesToDisplay[i] = null;
		}
		for (int i = 0; i < myCardImage.length; i++) {
			if (myCardImage[i] != null) {
				myCardImagesToDisplay[j] = myCardImage[i];
				j++;
			}
			if (j > 3) {
				break;
			}
		}
		setCardOnLayout();
	}

	@FXML
	public void setCardOnLayout() {
		myCard1.setImage(myCardImagesToDisplay[0]);
		myCard2.setImage(myCardImagesToDisplay[1]);
		myCard3.setImage(myCardImagesToDisplay[2]);
		myCard4.setImage(myCardImagesToDisplay[3]);
		betCard.setImage(betImage);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		leavePlace.setDisable(true);
		
		
		Timeline getMyCards = new Timeline(new KeyFrame(Duration.seconds(0.2), new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (gameIsStarted.equals("true")) {
					try {
						myCardHand = main.Push("getMyCards--" + myUsername);
						myAcces = main.Push("Acces");
						if (myAcces.contains("Acces->")) {
							String[] getingData = myAcces.split("->");
							UserIsThinking.setText("User " + getingData[1] + " is thinking...");
							if (myAcces.equals("Acces->" + myUsername)) {
								skipButton.setVisible(true);
								skipButton.setDisable(false);
							} else {
								skipButton.setVisible(false);
								skipButton.setDisable(true);
								alreadyTake = false;
							}
						}

						if (myCardHand.contains("@@")) {
							String[] allReturnedCards = myCardHand.replaceAll("@@", "").split("-->");
							betImage = new Image("Cards/" + allReturnedCards[1] + ".png");
							String[] cardsInHand = allReturnedCards[0].split("--");
							if (!myOldCardHand.equals(allReturnedCards[0])) {
								myOldCardHand = allReturnedCards[0];
								for (int i = 0; i < myCardImages.length; i++) {
									myCardImages[i] = null;
								}
								for (int i = 0; i < cardsInHand.length; i++) {
									if (cardsInHand[i].length() > 1 ) {
										cardsInHand[i] = cardsInHand[i].replaceAll("null", "");
										if( cardsInHand[i]!=""){
											myCardImages[i] = new Image("Cards/" + cardsInHand[i] + ".png");
											myCardsValues.put(myCardImages[i], cardsInHand[i]);
										}
										}
								}
							}
							getMyCardsToDisplay(myCardImages);
							String gameTrump = main.Push("GetGameTrump");
							if (gameTrump.contains("GameTrumpIS")) {
								String trumpArray[] = gameTrump.split("--");
								if (trumpArray[1].contains("R")) {
									trumpType.setTextFill(Color.web("#ff0000"));
									trumpType.setText("♥");
								}
								if (trumpArray[1].contains("D")) {
									trumpType.setTextFill(Color.web("#ff0000"));
									trumpType.setText("♦");
								}
								if (trumpArray[1].contains("C")) {
									trumpType.setTextFill(Color.web("#000000"));
									trumpType.setText("♣");
								}
								if (trumpArray[1].contains("V")) {
									trumpType.setTextFill(Color.web("#000000"));
									trumpType.setText("♠");
								}
							}
							String playerScore = null;
							playerScore = main.Push("GETPLAYERSCORE");
							if(playerScore.contains("PlayerScoreIs")){
								System.out.println(" PLAYERSCORE IS "+ playerScore);
								String [] getData = playerScore.split("-#-");
								String [] userDataScore = getData[1].split("-----");
								for(int i = 0; i< userDataScore.length; i++){
									userDataScore[i] = userDataScore[i].replaceAll("=", " have ");
								}
								ObservableList<String> usersData = FXCollections.observableArrayList(userDataScore);
								userListScore.setItems(usersData);
							}

						}
					} catch (IOException e) {
					}
				}
			}

		}));

		getMyCards.setCycleCount(Timeline.INDEFINITE);
		getMyCards.play();

		Timeline gameStartTimer = new Timeline(new KeyFrame(Duration.seconds(0.2), new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (!gameIsStarted.equals("true")) {
					try {
						gameIsStarted = main.Push("getGameStatus--" + myUsername);
					} catch (IOException e) {
					}
				}
			}
		}));

		gameStartTimer.setCycleCount(Timeline.INDEFINITE);
		gameStartTimer.play();
		
		Timeline gameRefresh = new Timeline(new KeyFrame(Duration.seconds(0.2), new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (gameIsStarted.equals("true")) {
					try {
						String gameRefresh = main.Push("CHECKGAMEREFRESH");
						if(gameRefresh.contains("NEWGAMEISSTARTED")){
							gameIsStarted = "false";
							choseSuits.setVisible(false);
						}
						} catch (IOException e) {
					}
				}
			}
		}));

		gameRefresh.setCycleCount(Timeline.INDEFINITE);
		gameRefresh.play();

		

		Timeline timer = new Timeline(new KeyFrame(Duration.seconds(0.3), new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					playerNamePosition = main.Push("GetPosit");
					
				} catch (IOException e) {
				}
				String userData[] = playerNamePosition.split("---");
				for (String username : userData) {
					String concretUsername[] = username.split("-");
					if (!concretUsername[0].equals("null") && !concretUsername[0].equals("true")) {
						switch (concretUsername[1]) {
						case "0":
							if (concretUsername[0].equals(myUsername)) {
								userName1.setTextFill(Color.web("#e44747"));
							}
							userName1.setText(concretUsername[0]);
							userIcon1.setImage(userImg);
							break;
						case "1":
							if (concretUsername[0].equals(myUsername)) {
								userName2.setTextFill(Color.web("#e44747"));
							}
							userName2.setText(concretUsername[0]);
							userIcon2.setImage(userImg);
							break;
						case "2":
							if (concretUsername[0].equals(myUsername)) {
								userName3.setTextFill(Color.web("#e44747"));
							}
							userName3.setText(concretUsername[0]);
							userIcon3.setImage(userImg);
							break;
						case "3":
							if (concretUsername[0].equals(myUsername)) {
								userName4.setTextFill(Color.web("#e44747"));
							}
							userName4.setText(concretUsername[0]);
							userIcon4.setImage(userImg);
							break;
						case "4":
							if (concretUsername[0].equals(myUsername)) {
								userName5.setTextFill(Color.web("#e44747"));
							}
							userName5.setText(concretUsername[0]);
							userIcon5.setImage(userImg);
							break;
						case "5":
							if (concretUsername[0].equals(myUsername)) {
								userName6.setTextFill(Color.web("#e44747"));
							}
							userName6.setText(concretUsername[0]);
							userIcon6.setImage(userImg);
							break;
						}
					} else if (concretUsername[0].equals("null")) {
						switch (concretUsername[1]) {
						case "0":
							userName1.setText("");
							userIcon1.setImage(unknownUserImg);
							break;
						case "1":
							userName2.setText("");
							userIcon2.setImage(unknownUserImg);
							break;
						case "2":
							userName3.setText("");
							userIcon3.setImage(unknownUserImg);
							break;
						case "3":
							userName4.setText("");
							userIcon4.setImage(unknownUserImg);
							break;
						case "4":
							userName5.setText("");
							userIcon5.setImage(unknownUserImg);
							break;
						case "5":
							userName6.setText("");
							userIcon6.setImage(unknownUserImg);
							break;
						}
					}
				}
			}
		}));

		timer.setCycleCount(Timeline.INDEFINITE);
		timer.play();
	}

	@FXML
	public void NextCard() {
		nextCard.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				Image firstCard = myCardImages[0];
				for (int i = 0; i < myCardImages.length - 1; i++) {
					myCardImages[i] = myCardImages[i + 1];
				}
				myCardImages[myCardImages.length - 1] = firstCard;
				getMyCardsToDisplay(myCardImages);
			}
		});
	}

	@FXML
	public void PrevCard() {
		prevCard.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				Image lastCard = myCardImages[myCardImages.length - 1];
				for (int i = myCardImages.length - 2; i >= 0; i--) {
					myCardImages[i + 1] = myCardImages[i];
				}
				myCardImages[0] = lastCard;
				getMyCardsToDisplay(myCardImages);

			}
		});
	}

	public void getCardXcord(double x, double y) {
		playRoomControl.cardX = x;
		playRoomControl.cardY = y;
	}

	public double returnCardXcord() {
		return cardX;
	}

	public double returnCardYcord() {
		return cardY;
	}

	@FXML
	public void animatCardUp() {

		myCard1.setOnMouseEntered(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				getCardXcord(myCard1.getLayoutX(), myCard1.getLayoutY());
				myCard1.setLayoutX(returnCardXcord());
				myCard1.setLayoutY(returnCardYcord() - 20);

			}
		});
		myCard2.setOnMouseEntered(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				getCardXcord(myCard2.getLayoutX(), myCard2.getLayoutY());
				myCard2.setLayoutX(returnCardXcord());
				myCard2.setLayoutY(returnCardYcord() - 20);

			}
		});
		myCard3.setOnMouseEntered(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				getCardXcord(myCard3.getLayoutX(), myCard3.getLayoutY());
				myCard3.setLayoutX(returnCardXcord());
				myCard3.setLayoutY(returnCardYcord() - 20);

			}
		});
		myCard4.setOnMouseEntered(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				getCardXcord(myCard4.getLayoutX(), myCard4.getLayoutY());
				myCard4.setLayoutX(returnCardXcord());
				myCard4.setLayoutY(returnCardYcord() - 20);

			}
		});

	}

	@FXML
	public void getOneCard() {
		deck.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				if (myAcces.equals("Acces->" + myUsername) && alreadyTake == false) {
					try {
						String message = main.Push("GetOneCard--" + myUsername);
						message = message + "-";
						alreadyTake = true;
					} catch (IOException e) {
					}
				}
			}
		});
	}

	public void sendCardToServer(Image TempImage) {
		String cardValue;
		if (myAcces.equals("Acces->" + myUsername)) {
			for (Map.Entry<Image, String> entry : myCardsValues.entrySet()) {
				if (entry.getKey().equals(TempImage)) {
					cardValue = entry.getValue();
					try {
						main.Push("PutCard--" + cardValue + "--" + myUsername);
						if(cardValue.contains("J")){
							Rosu.setVisible(true);
							Cruce.setVisible(true);
							Verde.setVisible(true);
							Doba.setVisible(true);
							choseSuits.setVisible(true);
							choseSuits.setText("CHOSE GAME TRUMP");
						}else if(cardValue.contains("8") || cardValue.contains("A") || cardValue.contains("6")|| cardValue.contains("7") ){
							alreadyTake = false;
						}
					} catch (IOException e) {
					}
				}
			}
		}
	}
	@FXML
	public void hideChoseSuits(){
		Rosu.setVisible(false);
		Cruce.setVisible(false);
		Verde.setVisible(false);
		Doba.setVisible(false);
		choseSuits.setVisible(false);
	}
	
	@FXML
	public void changeSuit(){
		Rosu.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				try {
					String s = main.Push("ChangeTrump--R");
					hideChoseSuits();
				} catch (IOException e) {}
				
			}
		});
		
		Verde.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				try {
					String s = main.Push("ChangeTrump--V");
					hideChoseSuits();
				} catch (IOException e) {}
			}
		});
		
		Doba.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				try {
					String s = main.Push("ChangeTrump--D");
					hideChoseSuits();
				} catch (IOException e) {}
				
			}
		});
		
		Cruce.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				try {
					String s = main.Push("ChangeTrump--C");
					hideChoseSuits();
				} catch (IOException e) {}
				
			}
		});
	}

	@FXML
	public void putCard() {

		myCard1.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				sendCardToServer(myCard1.getImage());
			}
		});
		myCard2.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				Image TempImage = myCard2.getImage();
				sendCardToServer(TempImage);
			}
		});
		myCard3.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				Image TempImage = myCard3.getImage();
				sendCardToServer(TempImage);
			}
		});
		myCard4.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				Image TempImage = myCard4.getImage();
				sendCardToServer(TempImage);
			}
		});
	}

	@FXML
	public void animatCardDown() {

		myCard1.setOnMouseExited(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				myCard1.setLayoutX(returnCardXcord());
				myCard1.setLayoutY(returnCardYcord());
			}
		});
		myCard2.setOnMouseExited(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				myCard2.setLayoutX(returnCardXcord());
				myCard2.setLayoutY(returnCardYcord());
			}
		});
		myCard3.setOnMouseExited(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				myCard3.setLayoutX(returnCardXcord());
				myCard3.setLayoutY(returnCardYcord());
			}
		});
		myCard4.setOnMouseExited(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				myCard4.setLayoutX(returnCardXcord());
				myCard4.setLayoutY(returnCardYcord());
			}
		});
	}

	public void disablePosition(boolean key) {
		userIcon1.setDisable(key);
		userIcon2.setDisable(key);
		userIcon3.setDisable(key);
		userIcon4.setDisable(key);
		userIcon5.setDisable(key);
		userIcon6.setDisable(key);
	}

	@FXML
	public void leavePlace() {

		leavePlace.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				try {
					boolean canLeave = Boolean.parseBoolean(main.Push("LeavePlace--" + myUsername + "--" + myPosition));
					if (canLeave == true) {
						leavePlace.setDisable(true);
						disablePosition(false);
					}
				} catch (IOException e) {
				}
			}
		});

	}

	@FXML
	public void ChangeUserIcon() {
		userIcon1.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				String response = null;
				try {
					response = main.Push("Position--" + 1 + "--" + myUsername);
				} catch (IOException e) {

				}
				if (response.equals("Succes")) {
					userIcon1.setImage(userImg);
					userName1.setText(myUsername);
					myPosition = "1";
					disablePosition(true);
				}
				event.consume();
			}
		});

		userIcon2.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				String response = null;
				try {
					response = main.Push("Position--" + 2 + "--" + myUsername);
				} catch (IOException e) {
				}
				if (response.equals("Succes")) {
					userIcon2.setImage(userImg);
					userName2.setText(myUsername);
					myPosition = "2";
					disablePosition(true);
				}
				event.consume();
			}
		});

		userIcon3.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				String response = null;
				try {
					response = main.Push("Position--" + 3 + "--" + myUsername);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (response.equals("Succes")) {
					userIcon3.setImage(userImg);
					userName3.setText(myUsername);
					myPosition = "3";
					disablePosition(true);
				}
				event.consume();
			}
		});

		userIcon4.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				String response = null;
				try {
					response = main.Push("Position--" + 4 + "--" + myUsername);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (response.equals("Succes")) {
					userIcon4.setImage(userImg);
					userName4.setText(myUsername);
					myPosition = "4";
					disablePosition(true);
				}
				event.consume();
			}
		});

		userIcon5.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				String response = null;
				try {
					response = main.Push("Position--" + 5 + "--" + myUsername);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (response.equals("Succes")) {
					userIcon5.setImage(userImg);
					userName5.setText(myUsername);
					myPosition = "5";
					disablePosition(true);
				}
				event.consume();
			}
		});

		userIcon6.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				String response = null;
				try {
					response = main.Push("Position--" + 6 + "--" + myUsername);
				} catch (IOException e) {}
				if (response.equals("Succes")) {
					userIcon6.setImage(userImg);
					userName6.setText(myUsername);
					myPosition = "6";
					disablePosition(true);
				}
				event.consume();
			}
		});
		leavePlace.setDisable(false);
	}
}
