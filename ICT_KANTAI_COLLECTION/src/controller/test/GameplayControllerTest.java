//package controller.test;
//
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.ResourceBundle;
//
//import controller.FXMLUtilsController;
//import javafx.animation.KeyFrame;
//import javafx.animation.Timeline;
//import javafx.application.Platform;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.geometry.Orientation;
//import javafx.scene.control.CheckMenuItem;
//import javafx.scene.control.ComboBox;
//import javafx.scene.control.Label;
//import javafx.scene.control.MenuItem;
//import javafx.scene.image.ImageView;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//import javafx.util.Duration;
//import model.platform.Board;
//import model.platform.Cell;
//import model.player.Player;
//import model.system.GameConfig;
//import model.system.GameMode;
//import model.unit.warship.Ship;
//import model.utilities.Ammo;
//import model.utilities.AmmoCollection;
//import model.utilities.AmmoType;
//import model.utilities.BigShot;
//import model.utilities.HorizontalShot;
//import model.utilities.NormalShot;
//import model.utilities.VerticalShot;
//import model.utils.MagicGenerator;
//import model.utils.SoundCollection;
//
//public class GameplayControllerTest implements Initializable {
//	// data for presenting
//	@FXML
//	private BorderPane twoPlayerMatchPane;
//	@FXML
//	private HBox gameField;
//	@FXML
//	private VBox battleField1;
//	@FXML
//	private VBox battleField2;
//	@FXML
//	private Label player1Name;
//	@FXML
//	private Label player2Name;
//	@FXML
//	private ImageView arrowTurn;
//	@FXML
//	private ComboBox<String> selectAmmoBox1;
//	@FXML
//	private ComboBox<String> selectAmmoBox2;
//
//	// data for action
//	@FXML
//	private CheckMenuItem bgmCheckMenu;
//	@FXML
//	private CheckMenuItem seCheckMenu;
//	@FXML
//	private MenuItem backToMenuItem;
//	@FXML
//	private MenuItem exitMenuItem;
//
//	// data for processing
//	private Player currentPlayer;
//	private Player oppoPlayer;
//
//	private boolean shipHit = false;
//	private boolean lastTurn = false;
//	private int botHit = 0;
//	private Cell lastBotHitCell;
//	private Orientation playerDamagedShipOrien;
//
//	private Ammo currentAmmo = new NormalShot();
//	private List<Ammo> player1AmmoCollection = null;
//	private List<Ammo> player2AmmoCollection = null;
//	private int ammoCollectionSize1;
//	private int ammoCollectionSize2;
//
//	// data for storing
//	private Player player1;
//	private Player player2;
//
//	public void initialize(URL location, ResourceBundle resources) {
//		System.out.println("[DEBUGGING]: ");
//		// play sound
//		SoundCollection.INSTANCE.playLinkStartSFX();
//		SoundCollection.INSTANCE.playGamePlayBackGroundSound();
//
//		// setup data for sound check menu
//		bgmCheckMenu.setSelected(GameConfig.checkBGM());
//		seCheckMenu.setSelected(GameConfig.checkSE());
//
//		// load player's data (name, board...)
//		player1 = GameConfig.getPlayer1();
//		player2 = GameConfig.getPlayer2();
//		
//		// set labels for 2 players
//		player1Name.setText(player1.getPlayerName());
//		player2Name.setText(player2.getPlayerName());	
//
//		// draw board
//		drawBoard();
//
//		// set up event handler
//		setUpGameplayEventHandler();
//
//		processPlayer1Turn(); // Player 1 will go first by default
//	}
//
//	@FXML
//	public void handleBtnMenu(ActionEvent evt) {
//		if (evt.getSource() == bgmCheckMenu) {
//			// the button sound effect
//			SoundCollection.INSTANCE.playButtonClickSound();
//			GameConfig.setBGM2On(bgmCheckMenu.isSelected());
//		}
//		if (evt.getSource() == seCheckMenu) {
//			// the button sound effect
//			SoundCollection.INSTANCE.playButtonClickSound();
//			GameConfig.setSEOn(seCheckMenu.isSelected());
//		}
//		if (evt.getSource() == backToMenuItem) {
//			// the button sound effect
//			SoundCollection.INSTANCE.playButtonClickSound();
//			// stop the GamePlayBackGroundSound
//			SoundCollection.INSTANCE.stopGamePlayBackGroundSound();
//			System.out.println("Back to main menu");
//			FXMLUtilsController.loadSubStage("StartMenu.fxml", "show", GameConfig.getGameTitle());
//			twoPlayerMatchPane.getScene().getWindow().hide();
//			System.gc();
//		}
//		if (evt.getSource() == exitMenuItem) {
//			// the button sound effect
//			SoundCollection.INSTANCE.playButtonClickSound();
//			// stop the GamePlayBackGroundSound
//			SoundCollection.INSTANCE.stopGamePlayBackGroundSound();
//			System.out.println("Game is shutting down...");
//			Platform.exit();
//			System.exit(0);
//		}
//	}
//
//	private void drawBoard() {
//		// hide all the ships
//		player1.getBoard().hideAllCellOfBoard();
//		player2.getBoard().hideAllCellOfBoard();
//		// add boards to the battlefields
//		battleField1.getChildren().addAll(player1.getBoard());
//		battleField2.getChildren().addAll(player2.getBoard());
//		// add ammo collections combo box to 2 players
//		setupAmmoView(1);
//		setupAmmoView(2);
//		// set image to show whose turn
//		arrowTurn.setImage(FXMLUtilsController.getImageByName("Arrow.png", "image"));
//	}
//
//	// function works only if game is advance mode
//	private void setupAmmoView(int playerNum) {
//		if (GameConfig.isAdvancedMode()) {
//			if (playerNum == 1) {
//				player1AmmoCollection = player1.getAmmoCollection();
//				int ammoLSize = player1AmmoCollection.size();
//				ObservableList<String> ammoList = FXCollections.observableArrayList(
//						AmmoCollection.INSTANCE.getAmmoTypeByTypeID(player1AmmoCollection.get(0).getAmmoTypeID()));
//				for (int i = 1; i < ammoLSize; i++) {
//					ammoList.add(
//							AmmoCollection.INSTANCE.getAmmoTypeByTypeID(player1AmmoCollection.get(i).getAmmoTypeID()));
//				}
//				ammoCollectionSize1 = ammoList.size();
//				selectAmmoBox1.setItems(ammoList);
//				selectAmmoBox1.setValue(
//						AmmoCollection.INSTANCE.getAmmoTypeByTypeID(player1AmmoCollection.get(0).getAmmoTypeID()));
//				setCurrentAmmo(player1AmmoCollection.get(0).getAmmoTypeID());
//				selectAmmoBox1.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
//					int cnt = 0;
//					for (String ammoName : selectAmmoBox1.getItems()) {
//						if (ammoName.equals(newValue)) {
//							setCurrentAmmo(player1AmmoCollection.get(cnt).getAmmoTypeID());
//							break;
//						}
//						cnt++;
//					}
//
//				});
//			} else {
//				player2AmmoCollection = player2.getAmmoCollection();
//				int ammoLSize = player2AmmoCollection.size();
//				ObservableList<String> ammoList = FXCollections.observableArrayList(
//						AmmoCollection.INSTANCE.getAmmoTypeByTypeID(player2AmmoCollection.get(0).getAmmoTypeID()));
//				for (int i = 1; i < ammoLSize; i++) {
//					ammoList.add(
//							AmmoCollection.INSTANCE.getAmmoTypeByTypeID(player2AmmoCollection.get(i).getAmmoTypeID()));
//				}
//				ammoCollectionSize2 = ammoList.size();
//				selectAmmoBox2.setItems(ammoList);
//				selectAmmoBox2.setValue(
//						AmmoCollection.INSTANCE.getAmmoTypeByTypeID(player2AmmoCollection.get(0).getAmmoTypeID()));
//				setCurrentAmmo(player1AmmoCollection.get(0).getAmmoTypeID());
//				selectAmmoBox2.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
//					int cnt = 0;
//					for (String ammoName : selectAmmoBox2.getItems()) {
//						if (ammoName.equals(newValue)) {
//							setCurrentAmmo(player2AmmoCollection.get(cnt).getAmmoTypeID());
//							break;
//						}
//						cnt++;
//					}
//				});
//			}
//		} else {
//			if (playerNum == 1) {
//				selectAmmoBox1.setVisible(false);
//			} else {
//				selectAmmoBox2.setVisible(false);
//			}
//		}
//
//	}
//
//	private void setUpGameplayEventHandler() {
//		// pass event handler to each cell in the board of 2 players
//		for (int i = 0; i < GameConfig.NUM_OF_COLS; i++) {
//			for (int j = 0; j < GameConfig.NUM_OF_ROWS; j++) {
//				// board of player 1
//				Cell c = player1.getBoard().getCellByCoordinate(i, j);
//				c.setMouseEvtHandler(this::cellOfPlayer1Entered, this::cellOfPlayer1Exited, this::cellOfPlayer1Click);
//				c.setSeaAnimation();
//				// board of player 2
//				c = player2.getBoard().getCellByCoordinate(i, j);
//				c.setMouseEvtHandler(this::cellOfPlayer2Entered, this::cellOfPlayer2Exited, this::cellOfPlayer2Click);
//				c.setSeaAnimation();
//			}
//		}
//
//	}
//
//	// Mouse event [Click] Handler for cell:
//	// Left Mouse: choose a cell and take an action on it (fire the cell of
//	// opponent's board)
//	private void cellOfPlayer1Click(MouseEvent evt) {
//		if (GameConfig.getGameMode() == GameMode.TwoPlayers) {
//			if (currentPlayer == player2) {
//				Cell cell = (Cell) evt.getSource();
//				// if (cell.isFired() == false) { // fire if the cell is not yet fired
//				// fire(cell, oppoPlayer);
//				// }
//				ArrayList<Cell> target = currentAmmo.getTargetArea(player1, cell);
//				if (currentAmmo.isValidTarget()) {
//					fire(target, player1);
//				}
//			}
//		}
//	}
//
//	private void cellOfPlayer2Click(MouseEvent evt) {
//		if (currentPlayer == player1) {
//			Cell cell = (Cell) evt.getSource();
//			ArrayList<Cell> target = currentAmmo.getTargetArea(player2, cell);
//			if (currentAmmo.isValidTarget()) {
//				fire(target, player2);
//			}
//		}
//	}
//
//	// Mouse event [Enter] Handler for cell: show cell targeted
//	private void cellOfPlayer1Entered(MouseEvent evt) {
//		Cell cell = (Cell) evt.getSource();
//		ArrayList<Cell> target = currentAmmo.getTargetArea(player1, cell);
//		for (Cell c : target) {
//			if (!c.isSunk()) {
//				c.stopSeaAnimation();
//				// c.showNewColor(ColorCollection.DARKPURPLE.getRGBColor());
//			}
//		}
//	}
//
//	private void cellOfPlayer2Entered(MouseEvent evt) {
//		Cell cell = (Cell) evt.getSource();
//		ArrayList<Cell> target = currentAmmo.getTargetArea(player2, cell);
//		for (Cell c : target) {
//			if (!c.isSunk()) {
//				c.stopSeaAnimation();
//				// c.showNewColor(ColorCollection.DARKPURPLE.getRGBColor());
//			}
//		}
//	}
//
//	// Mouse event [Exit] Handler for cell: restore to default cell state or show
//	// appropriate view
//	private void cellOfPlayer1Exited(MouseEvent evt) {
//		Cell cell = (Cell) evt.getSource();
//		ArrayList<Cell> target = currentAmmo.getTargetArea(player1, cell);
//		for (Cell c : target) {
//			c.showAppropriateView();
//		}
//	}
//
//	private void cellOfPlayer2Exited(MouseEvent evt) {
//		Cell cell = (Cell) evt.getSource();
//		ArrayList<Cell> target = currentAmmo.getTargetArea(player2, cell);
//		for (Cell c : target) {
//			c.showAppropriateView();
//		}
//	}
//
//	// choose a cell randomly to fire (used for bot)
//	private void chooseCellRandom() {
//		if (botHit == 0) { // no damaged ship (not sunk) of player on the board
//			while (true) {
//				int cXPos = MagicGenerator.getRandInt(GameConfig.NUM_OF_COLS);
//				int cYPos = MagicGenerator.getRandInt(GameConfig.NUM_OF_ROWS);
//				Cell cell = oppoPlayer.getBoard().getCellByCoordinate(cXPos, cYPos);
//				if (cell.isFired() == false) { // fire if the cell is not yet fired
//					fire(cell, oppoPlayer);
//					break;
//				}
//			}
//		} else if (botHit == 1) { // detected a damaged ship (not sunk) of player on the board but not the
//									// orientation of the ship
//			while (true) {
//				int rCell = MagicGenerator.getRandInt(4);
//				int cXPos = lastBotHitCell.getXPosition();
//				int cYPos = lastBotHitCell.getYPosition();
//				Cell cell = null;
//				switch (rCell) {
//				case 0:
//					cXPos++;
//					break;
//				case 1:
//					cXPos--;
//					break;
//				case 2:
//					cYPos++;
//					break;
//				case 3:
//					cYPos--;
//					break;
//				default:
//					break;
//				}
//				if (Board.isValidCell(cXPos, cYPos)) {
//					cell = oppoPlayer.getBoard().getCellByCoordinate(cXPos, cYPos);
//					if (cell.isFired() == false) { // fire if the cell is not yet fired
//
//						fire(cell, oppoPlayer);
//						break;
//					}
//				}
//
//			}
//		} else if (botHit >= 2) { // detected a damaged ship (not sunk) of player on the board and the orientation
//									// of the ship
//			int cXPos = lastBotHitCell.getXPosition();
//			int cYPos = lastBotHitCell.getYPosition();
//			Cell cell = null;
//			boolean validCellFound = false;
//
//			if (playerDamagedShipOrien == Orientation.HORIZONTAL) {
//				while (!validCellFound) {
//					cXPos = lastBotHitCell.getXPosition();
//					int rCell = MagicGenerator.getRandInt(2);
//					if (rCell == 0) { // fire to the right
//						while (true) {
//							cXPos++;
//							if (Board.isValidCell(cXPos, cYPos)) {
//								cell = oppoPlayer.getBoard().getCellByCoordinate(cXPos, cYPos);
//								if (!cell.isFired()) {
//									validCellFound = true;
//									break;
//								} else { // cell.isFired() == true
//									if (cell.isBlank()) {
//										break;
//									} else {
//										// not break to continue the while loop (keep finding a cell to fire)
//									}
//								}
//							} else {
//								break;
//							}
//						}
//					} else { // fire to the left
//						while (true) {
//							cXPos--;
//							if (Board.isValidCell(cXPos, cYPos)) {
//								cell = oppoPlayer.getBoard().getCellByCoordinate(cXPos, cYPos);
//								if (!cell.isFired()) {
//									validCellFound = true;
//									break;
//								} else { // cell.isFired() == true
//									if (cell.isBlank()) {
//										break;
//									} else {
//										// not break to continue the while loop (keep finding a cell to fire)
//									}
//								}
//							} else {
//								break;
//							}
//						}
//					}
//				}
//			} else { // playerDamagedShipOrien == Orientation.VERTICAL
//				while (!validCellFound) {
//					cYPos = lastBotHitCell.getYPosition();
//					int rCell = MagicGenerator.getRandInt(2);
//					if (rCell == 0) { // fire to the top
//						while (true) {
//							cYPos++;
//							if (Board.isValidCell(cXPos, cYPos)) {
//								cell = oppoPlayer.getBoard().getCellByCoordinate(cXPos, cYPos);
//								if (!cell.isFired()) {
//									validCellFound = true;
//									break;
//								} else { // cell.isFired() == true
//									if (cell.isBlank()) {
//										break;
//									} else {
//										// not break to continue the while loop (keep finding a cell to fire)
//									}
//								}
//							} else {
//								break;
//							}
//						}
//					} else { // fire to the bottom
//						while (true) {
//							cYPos--;
//							if (Board.isValidCell(cXPos, cYPos)) {
//								cell = oppoPlayer.getBoard().getCellByCoordinate(cXPos, cYPos);
//								if (!cell.isFired()) {
//									validCellFound = true;
//									break;
//								} else { // cell.isFired() == true
//									if (cell.isBlank()) {
//										break;
//									} else {
//										// not break to continue the while loop (keep finding a cell to fire)
//									}
//								}
//							} else {
//								break;
//							}
//						}
//					}
//				}
//			}
//
//			fire(cell, oppoPlayer);
//		}
//	}
//
//	private void fire(Cell cell, Player targetPlayer) {
//		boolean fireResult = cell.fireCell();
//		if (fireResult == false) { 
//			if (GameConfig.getGameMode() == GameMode.VersusBot && currentPlayer == player2)
//				endTurn(0);
//		} else {
//			Ship curShip = cell.getShip();
//			// case for versus bot only
//			if (GameConfig.getGameMode() == GameMode.VersusBot && currentPlayer == player2) {
//				botHit++;
//				lastBotHitCell = cell;
//				if (botHit >= 2) {
//					playerDamagedShipOrien = curShip.getOrien();
//				}
//			}
//			if (curShip.isSunk() == true) {
//				curShip.sink();
//				// remove ship from board
//				targetPlayer.getBoard().removeShipFromArmy(curShip);
//				
//				if (GameConfig.getGameMode() == GameMode.VersusBot && currentPlayer == player2) {
//					botHit = 0;
//				}
//			}
//			shipHit = true;
//			if (GameConfig.getGameMode() == GameMode.VersusBot && currentPlayer == player2)
//				endTurn(1);
//		}
//		cell.showStoredPaint();
//	}
//
//	private void fire(ArrayList<Cell> targetArea, Player targetPlayer) {
//		for (Cell c : targetArea) {
//			if (c.isFired() == false) { // fire if the cell is not yet fired
//				fire(c, targetPlayer);
//				// update the ammo collection whenever player use the ammo
//				updateAmmoCollection();
//			}
//		}
//
//		if (shipHit) {
//			endTurn(1);
//			shipHit = false;
//		} else
//			endTurn(0);
//
//	}
//
//	private void switchPlayer() {
//		arrowTurn.setRotate(arrowTurn.getRotate() + 180);
//		if (currentPlayer == player1) {
//			processPlayer2Turn();
//		} else { // currentPlayer == player2
//			processPlayer1Turn();
//		}
//	}
//
//	private void endTurn(int state) {
//		// state == 0: miss, state == 1: hit
//		if (state == 0) {
//			if (currentPlayer == player2 && lastTurn) {
//				gameOver(1);
//			} else {
//				switchPlayer();
//			}
//		} else { // state == 1
//			if (currentPlayer == player1) {
//				if (player2.getBoard().getNumOfShipLeft() == 0) {
//					lastTurn = true;
//					switchPlayer();
//				}
//			} else { // currentPlayer == player2
//				if (player1.getBoard().getNumOfShipLeft() == 0) {
//					if (lastTurn)
//						gameOver(0);
//					else
//						gameOver(2);
//				} else {
//
//					if (GameConfig.getGameMode() == GameMode.VersusBot) {
//
//						processPlayer2Turn();
//
//					}
//				}
//			}
//
//		}
//
//	}
//
//	private void processPlayer1Turn() {
//		currentPlayer = player1;
//		oppoPlayer = player2;
//		// lock opponent combobox
//		lockAmmoBox();
//		shipHit = false;
//	}
//
//	private void processPlayer2Turn() {
//		currentPlayer = player2;
//		oppoPlayer = player1;
//		// lock opponent combobox
//		lockAmmoBox();
//		shipHit = false;
//
//		if (GameConfig.getGameMode() == GameMode.VersusBot) {
//
//			// try {
//			// TimeUnit.SECONDS.sleep(1);
//			// } catch (InterruptedException e) {
//			// e.printStackTrace();
//			// }
//
//			Timeline timeline = new Timeline();
//			timeline.setCycleCount(1);
//			timeline.setAutoReverse(true);
//			timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000), (ActionEvent event) -> {
//				chooseCellRandom();
//			}));
//			timeline.play();
//			// chooseCellRandom();
//		}
//	}
//
//	// disable the box depends on whose turn
//	// function works only in advance mode
//	private void lockAmmoBox() {
//		if (GameConfig.isAdvancedMode()) {
//			if (currentPlayer == player1) {
//				if (ammoCollectionSize1 <= 1)
//					selectAmmoBox1.setDisable(true);
//				else
//					selectAmmoBox1.setDisable(false);
//				selectAmmoBox2.setDisable(true);
//			} else {
//				selectAmmoBox1.setDisable(true);
//				if (ammoCollectionSize2 <= 1)
//					selectAmmoBox2.setDisable(true);
//				else
//					selectAmmoBox2.setDisable(false);
//			}
//		}
//	}
//
//	// set current ammo depending on current ammo type id
//	private void setCurrentAmmo(int currentAmmoTypeID) {
//		if (currentAmmoTypeID == AmmoType.BigShot.getAmmoTypeID()) {
//			currentAmmo = new BigShot();
//		} else if (currentAmmoTypeID == AmmoType.HorizontalShot.getAmmoTypeID()) {
//			currentAmmo = new HorizontalShot();
//		} else if (currentAmmoTypeID == AmmoType.VerticalShot.getAmmoTypeID()) {
//			currentAmmo = new VerticalShot();
//		} else {
//			currentAmmo = new NormalShot();
//		}
//	}
//
//	// update the ammo collection of 2 players (in data and in view)
//	// function works only in advance mode
//	private void updateAmmoCollection() {
//		if (GameConfig.isAdvancedMode()) {
//			// if current ammo is special one, disable it from the ammo combo box, change
//			// the current ammo to normal
//			if (currentPlayer == player1) {
//				String shotAmmo = selectAmmoBox1.getValue();
//				if (!shotAmmo.equals(AmmoType.NormalShot.getAmmoType())) {
//					ObservableList<String> ammoList = selectAmmoBox1.getItems();
//					for (Ammo ammo : player1AmmoCollection) {
//						if (AmmoCollection.INSTANCE.getAmmoTypeByTypeID(ammo.getAmmoTypeID()).equals(shotAmmo)) {
//							player1AmmoCollection.remove(ammo);
//							ammoList.remove(shotAmmo);
//							break;
//						}
//					}
//					ammoCollectionSize1 = ammoList.size();
//					selectAmmoBox1.setItems(ammoList);
//					selectAmmoBox1.setValue(
//							AmmoCollection.INSTANCE.getAmmoTypeByTypeID(player1AmmoCollection.get(0).getAmmoTypeID()));
//					setCurrentAmmo(player1AmmoCollection.get(0).getAmmoTypeID());
//				}
//			} else {
//				String shotAmmo = selectAmmoBox2.getValue();
//				if (!shotAmmo.equals(AmmoType.NormalShot.getAmmoType())) {
//					ObservableList<String> ammoList = selectAmmoBox2.getItems();
//					for (Ammo ammo : player2AmmoCollection) {
//						if (AmmoCollection.INSTANCE.getAmmoTypeByTypeID(ammo.getAmmoTypeID()).equals(shotAmmo)) {
//							player2AmmoCollection.remove(ammo);
//							ammoList.remove(shotAmmo);
//							break;
//						}
//					}
//					ammoCollectionSize2 = ammoList.size();
//					selectAmmoBox2.setItems(ammoList);
//					selectAmmoBox2.setValue(
//							AmmoCollection.INSTANCE.getAmmoTypeByTypeID(player2AmmoCollection.get(0).getAmmoTypeID()));
//					setCurrentAmmo(player2AmmoCollection.get(0).getAmmoTypeID());
//				}
//			}
//		}
//	}
//
//	private void gameOver(int result) {
//		// result = 1: player 1 win
//		// = 2: player 2 win
//		// = 0: draw
//
//		GameConfig.saveGameResult(result);
//
//		Timeline timeline = new Timeline();
//		timeline.setCycleCount(1);
//		timeline.setAutoReverse(true);
//		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1500), (ActionEvent event) -> {
//			FXMLUtilsController.loadSubStage("GameOver.fxml", "show", GameConfig.getGameTitle());
//			twoPlayerMatchPane.getScene().getWindow().hide();
//		}));
//		timeline.play();
//
//		// stop the GamePlayBackGroundSound
//		SoundCollection.INSTANCE.stopGamePlayBackGroundSound();
//	}
//}
