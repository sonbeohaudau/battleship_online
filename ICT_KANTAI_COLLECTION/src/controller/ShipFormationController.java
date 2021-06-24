package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.platform.Board;
import model.platform.Cell;
import model.player.Player;
import model.system.GameConfig;
import model.system.GameMode;
import model.unit.warship.Ship;
import model.unit.warship.ShipArmy;
import model.utilities.Ammo;
import model.utilities.AmmoCollection;
import model.utils.ColorCollection;
import model.utils.MagicGenerator;
import model.utils.SoundCollection;
import socket.client.ClientSocket;

public class ShipFormationController implements Initializable {
	// data for presenting
	@FXML
	private BorderPane shipFormationPane;
	@FXML
	private Label shipArmyLabel;
	@FXML
	private Label numOfShipLeftLabel;
	@FXML
	private Label playerNameLabel;
	@FXML
	private VBox ShipArmySelectionBox;
	@FXML
	private VBox battleField;

	// data for action
	@FXML
	private Button rotShipButton;
	@FXML
	private Button confirmBtn;
	@FXML
	private Button randPlacementBtn;
	@FXML
	private CheckMenuItem bgmCheckMenu;
	@FXML
	private CheckMenuItem seCheckMenu;
	@FXML
	private MenuItem backToMenuItem;
	@FXML
	private MenuItem exitMenuItem;

	// data for processing
	private Ship curShip;
	// create a temporary group of cells holding by the current Ship
	private List<Cell> curShipArea = new ArrayList<>();
	private Board board;
	private List<Ammo> ammoCollection;
	private boolean isCurShipAreaValid = false;

	// data for storing
	private String playerName;
	private String opponentName = ClientSocket.getInstance().getOpponent();
	private int playerNum = 1;
	// number of ship that not set placement in this board
	// use integerProperty for binding this value to fxml
	private IntegerProperty numOfShipLeft = new SimpleIntegerProperty();

	// listener
	private ChangeListener<String> numOfShipTrackListener = new ChangeListener<String>() {
		@Override
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			// when running out of ships, toggle the buttons and prepare for entering the
			// battlefield
			if (newValue.compareTo("0") == 0) {
				toggleGUIButtons();
			}
		}
	};

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// play background sound
		SoundCollection.INSTANCE.playSetupFormationBackGroundSound();
		// start setup
		earlySetup();
	}

	@FXML
	public void handleBtnMenu(ActionEvent evt) {
		if (evt.getSource() == bgmCheckMenu) {
			SoundCollection.INSTANCE.playButtonClickSound();
			GameConfig.setBGM1On(bgmCheckMenu.isSelected());
		}
		if (evt.getSource() == seCheckMenu) {
			SoundCollection.INSTANCE.playButtonClickSound();
			GameConfig.setSEOn(seCheckMenu.isSelected());
		}
		if (evt.getSource() == backToMenuItem) {
			// the button sound effect
			SoundCollection.INSTANCE.playButtonClickSound();
			// stop the FormationBackGroundSound
			SoundCollection.INSTANCE.stopSetupFormationBackGroundSound();
			System.out.println("Back to main menu");
			FXMLUtilsController.loadSubStage("StartMenu.fxml", "show", GameConfig.getGameTitle());
			shipFormationPane.getScene().getWindow().hide();
			System.gc();
		}
		if (evt.getSource() == exitMenuItem) {
			// the button sound effect
			SoundCollection.INSTANCE.playButtonClickSound();
			// stop the FormationBackGroundSound
			SoundCollection.INSTANCE.stopSetupFormationBackGroundSound();

			System.out.println("Game is shutting down...");
			Platform.exit();
			System.exit(0);
		}
		if (evt.getSource() == rotShipButton) {

			// the button sound effect
			SoundCollection.INSTANCE.playButtonClickSound();
			if (numOfShipLeft.get() > 0) {
				// hide the old placement
				// board.hidePlacementArea();
				hidePlacementArea();
				// rotate the orientation for the current ship
				curShip.rotateShip();
			}
		} else if (evt.getSource() == randPlacementBtn) {

			// the button sound effect
			SoundCollection.INSTANCE.playButtonClickSound();

			randShipFormation();
		} else if (evt.getSource() == confirmBtn) {

			// the button sound effect
			SoundCollection.INSTANCE.playConfirmSound();
			
			confirmSetUpBoard();
		}
	}
	
	public void confirmSetUpBoard() {
		
		if (GameConfig.getGameMode() == GameMode.Online) {
			// send board data to server and wait for match start
			boolean b = ClientSocket.getInstance().setupShip(board);
			
			if (!b) {
				return;
			}
				
		}


		if (playerNum == 1) {
			// load the data for player 1 depends on difficulty
			if (GameConfig.isAdvancedMode() == true) {
//				if (goFirst)
					GameConfig.loadDataPlayer1(new Player(playerName, board, ammoCollection));
//				else 
//					GameConfig.loadDataPlayer2(new Player(playerName, board, ammoCollection));
				System.out.println("Advance mode ");
			} else {
				GameConfig.loadDataPlayer1(new Player(playerName, board));
				System.out.println("Normal mode");
			}

		}
		
		if (GameConfig.getGameMode() == GameMode.TwoPlayers) {
			if (playerNum == 1) {
				playerNum++;
				earlySetup();
			} else {
				// load the data for player 2 depends on difficulty
				if (GameConfig.isAdvancedMode() == true) {
					GameConfig.loadDataPlayer2(new Player(playerName, board, ammoCollection));
					System.out.println("Advance mode");
				} else {
					GameConfig.loadDataPlayer2(new Player(playerName, board));
					System.out.println("Normal mode");
				}
																																												FXMLUtilsController.loadSubStage("GamePlay.fxml", "show", GameConfig.getGameTitle());
																																												shipFormationPane.getScene().getWindow().hide();
																																												// stop the FormationBackGroundSound
																																												SoundCollection.INSTANCE.stopSetupFormationBackGroundSound();
			}
//		} else if (GameConfig.getGameMode() == GameMode.VersusBot) { 
		} else {  	// Versus bot or Online mode
			// stop the FormationBackGroundSound
			SoundCollection.INSTANCE.stopSetupFormationBackGroundSound();
			if (playerNum == 1) {
				playerNum++;
				fullSetup();
				Timeline timeline = new Timeline();
				timeline.setCycleCount(1);
				timeline.setAutoReverse(true);
				timeline.getKeyFrames().add(new KeyFrame(Duration.millis(10), (ActionEvent event) -> {
					// random places for ship in bot's board
					randShipFormation();
					try {
						TimeUnit.MILLISECONDS.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					// load the data for player 2 depends on difficulty
					// still need to develop more
					// if (GameConfig.isAdvancedMode()==true) {
					// GameConfig.loadDataPlayer2(new Player("BADASS BOT", board, ammoCollection ));
					// } else {
					if (GameConfig.getGameMode() == GameMode.Online) {
						GameConfig.loadDataPlayer2(new Player(this.opponentName, new Board()));
					}
					else
						GameConfig.loadDataPlayer2(new Player("BADASS BOT", board));
					// }
					FXMLUtilsController.loadSubStage("GamePlay.fxml", "show", GameConfig.getGameTitle());
					shipFormationPane.getScene().getWindow().hide();
				}));
				timeline.play();

			}
		}
	}

	//
	// helper method: refresh, setup data and action for the battlefield and ship
	// army
	//

	// full setup data for the ship formation and ask player for name
	private void earlySetup() {
		fullSetup();
		Platform.runLater(() -> {
			try {
				setNameForPlayer();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	// refresh prev data and init new data
	private void fullSetup() {
		System.out.println("Refreshing data...");
		initDataForSoundCheckMenu();
		refreshBoardSetup();
		System.out.println("Initializing data for the battlefield and ship army ...");
		initBattleFieldSetup();
		initShipArmyViewSetup();
		initAmmoCollection();
	}

	// set data for sound check menu
	private void initDataForSoundCheckMenu() {
		bgmCheckMenu.setSelected(GameConfig.checkBGM());
		seCheckMenu.setSelected(GameConfig.checkSE());
	}

	// delete the prev data and set new value for the board (GUI)
	private void refreshBoardSetup() {
		// remove the board & army only, not the label
		if (battleField.getChildren().size() > 1)
			battleField.getChildren().remove(1);
		if (ShipArmySelectionBox.getChildren().size() > 1) {
			ShipArmySelectionBox.getChildren().clear();
			ShipArmySelectionBox.getChildren().add(shipArmyLabel);
		}
		// enable rotate, random button & disable confirm button
		rotShipButton.setDisable(false);
		randPlacementBtn.setDisable(false);
		confirmBtn.setDisable(true);
		// remove the previous listener
		numOfShipLeftLabel.textProperty().removeListener(numOfShipTrackListener);
	}

	// set new list of ships and create new board
	private void initBattleFieldSetup() {
		// setup new army and new track
		ShipArmy.INSTANCE.setupArmy();
		ShipArmy.INSTANCE.setupArmyTrack();
		// get next ship if the current ship is null
		do {
			curShip = ShipArmy.INSTANCE.getNextShip();
		} while (curShip == null);
		// init the board with handlers for cells
		board = new Board(this::boardEntered, this::boardExited, this::boardClick, ShipArmy.INSTANCE.getShipArmy());
		// init the number of ship not set placement = size of ship army
		numOfShipLeft.set(ShipArmy.INSTANCE.getShipArmyNumber());
		// add processed board to the fxml
		battleField.getChildren().addAll(board);
	}

	private void initAmmoCollection() {
		AmmoCollection.INSTANCE.setupAmmoCollection();
		ammoCollection = AmmoCollection.INSTANCE.getAmmoCollection();
	}

	// set data for the ship Army GUI
	private void initShipArmyViewSetup() {
		int numOfShipType = ShipArmy.INSTANCE.getShipArmyTypeSize();
		Label tmpLabel;
		for (int i = 0; i < numOfShipType; i++) {
			HBox hBound = new HBox();
			tmpLabel = new Label(ShipArmy.INSTANCE.getShipTypeByTypeID(i) + " x ");
			hBound.getChildren().add(tmpLabel);
			tmpLabel = new Label();
			tmpLabel.textProperty().bind(ShipArmy.INSTANCE.getNumPropertyOfShipLeftByTypeID(i).asString());
			hBound.getChildren().add(tmpLabel);
			ShipArmySelectionBox.getChildren().add(hBound);
		}
		// bind the number of ship left to label in fxml
		numOfShipLeftLabel.textProperty().bind(numOfShipLeft.asString());
		numOfShipLeftLabel.textProperty().addListener(numOfShipTrackListener);
	}

//	// load a popup for entering nickname
	private void setNameForPlayer() throws IOException {
//		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../resources/fxml/PlayerList.fxml"));
//		Parent root = fxmlLoader.load();
////		PlayerListController popupController = fxmlLoader.<PlayerListController>getController();
//		// pass the player number to popup
////		popupController.initData(playerNum);
//		Stage stage = new Stage();
//		stage.setTitle(GameConfig.getGameTitle());
//		stage.getIcons().add(FXMLUtilsController.getImageByName("Kancolle_kai_logo.jpg", "icon"));
//		stage.setScene(new Scene(root));
//		stage.initModality(Modality.APPLICATION_MODAL);
//		stage.showAndWait();
//		// get player name from the popup
		playerName = StartMenuController.getPlayerName();
//		if (playerName == null || playerName.isEmpty())
//			playerName = "Player " + playerNum;
		playerNameLabel.setText(playerName);
	}

	private void randShipFormation() {
		// if we ran out of ship, init the battlefield again
		if (curShip == null) {
			fullSetup();
		}
		// keep setting the ship down at random position
		while (curShip != null) {
			setShipDownRand(curShip);
			// decrease number of ship not set placement
			numOfShipLeft.set(numOfShipLeft.get() - 1);
			ShipArmy.INSTANCE.updateNumOfShipLeftByTypeID(curShip.getShipTypeID());
			// get next ship
			curShip = ShipArmy.INSTANCE.getNextShip();
		}
	}

	// Mouse event [Click] Handler for cell:
	// Left Mouse: set the placement for current ship, get next ship
	// Right Mouse: rotate
	private void boardClick(MouseEvent evt) {
		// click handler works only with not null ship
		if (curShip != null) {
			Cell cell = (Cell) evt.getSource();
			// click the left mouse to set the ship down
			// if cursor is pointed at valid position, ship will be placed successfully
			if (evt.getButton() == MouseButton.PRIMARY) {
				if (isCurShipAreaValid) {
					SoundCollection.INSTANCE.playShipCorrect();
					// set the ship down, set area cursor pointing to invalid, get next ship
					setShipDown(curShip);
					// decrease number of ship not set placement
					numOfShipLeft.set(numOfShipLeft.get() - 1);
					ShipArmy.INSTANCE.updateNumOfShipLeftByTypeID(curShip.getShipTypeID());
					isCurShipAreaValid = false;
					curShip = ShipArmy.INSTANCE.getNextShip();
				} else {
					SoundCollection.INSTANCE.playShipWrong();
				}
			}
			// click right mouse button to rotate orientation
			else if (evt.getButton() == MouseButton.SECONDARY) {
				// hide old placement, rotate the orientation, show new placement
				hidePlacementArea();
				curShip.rotateShip();
				isCurShipAreaValid = showPlacementArea(curShip, cell);
			} else {
				System.out.println("Wrong input haha");
			}
		}
	}

	// Mouse event [Enter] Handler for cell: show placement of current ship
	private void boardEntered(MouseEvent evt) {
		Cell cell = (Cell) evt.getSource();
		isCurShipAreaValid = showPlacementArea(curShip, cell);
	}

	// Mouse event [Exit] Handler for cell: remove placement of current ship
	private void boardExited(MouseEvent evt) {
		hidePlacementArea();
	}

	private void toggleGUIButtons() {
		if (rotShipButton.isDisable()) {
			rotShipButton.setDisable(false);
		} else {
			rotShipButton.setDisable(true);
		}
		if (confirmBtn.isDisable()) {
			confirmBtn.setDisable(false);
		} else {
			confirmBtn.setDisable(true);
		}

	}
	
	//
	// helper method: action for fxml
	//

	// show group of area when cursor moves to a cell when exec ship formation
	// if a place is valid, show green and return true
	// else show red and return false
	private boolean showPlacementArea(Ship ship, Cell cell) {
		// check if ship is null
		if (ship == null)
			return false;
		// validation ship placement and add data to curShipArea
		// set color fill and color border for cell
		boolean validatePlacement = isValidPlacement(ship, cell.getXPosition(), cell.getYPosition());
		if (validatePlacement) {
			showColorForPlacement(ColorCollection.GREEN.getRGBColor(), ColorCollection.GREENBORDER.getRGBColor());
		} else {
			showColorForPlacement(ColorCollection.RED.getRGBColor(), ColorCollection.REDBORDER.getRGBColor());
		}
		return validatePlacement;
	}

	// when cursor moves to another cell, restore the original color of the cell +
	// clear the stored area of current ship
	private void hidePlacementArea() {
		// loop through stored cells for restore the old color
		for (Cell cell : curShipArea) {
			cell.showStoredPaint();
		}
		// clear the stored area
		curShipArea.clear();
	}
	
	// store and show new color for all cells holding by current ship
	private void showColorForPlacement(Color newColor, Color newColorBorder) {
		for (Cell curC : curShipArea) {
			curC.showColor(newColor, newColorBorder);
		}
	}
	
	//
	// helper method: ship formation
	//

	// set the ship down
	private void setShipDown(Ship targetShip) {
		targetShip.launchShip(curShipArea);
		curShipArea.clear();
	}

	// set the ship down randomly
	private void setShipDownRand(Ship ship) {
		while (true) {
			curShipArea.clear();
			int cXPos = MagicGenerator.getRandInt(GameConfig.NUM_OF_COLS);
			int cYPos = MagicGenerator.getRandInt(GameConfig.NUM_OF_ROWS);
			int rot = MagicGenerator.getRandInt(10);
			if (rot % 2 == 0)
				ship.rotateShip();
			if (isValidPlacement(ship, cXPos, cYPos)) {
				setShipDown(ship);
				break;
			}
		}
	}
	
	//
	// helper method: valid data for fxml
	//

	// check if cursor position points to a valid position for current ship
	private boolean isValidPlacement(Ship ship, int cXPos, int cYPos) {
		int shipLength = ship.getShipLength();
		Cell c = null;
		boolean isValid = true;
		// if orientation of ship is horizontal
		if (ship.getOrien() == Orientation.HORIZONTAL) {
			for (int i = cXPos; i < cXPos + shipLength; i++) {
				// check if cell is valid
				if (Board.isValidCell(i, cYPos)) {
					c = board.getCellByCoordinate(i, cYPos);
					if (c != null)
						curShipArea.add(c);
				} else
					isValid = false;
				if (isValid) {
					// check if cell has any ship inside
					if (!c.isBlank()) {
						isValid = false;
					}
					if (isValid) {
						// get list of surrounding cells
						List<Cell> adjacentCellList = board.getAdjacentCellList(i, cYPos);
						// check if any cell has any ship inside
						for (Cell adjCell : adjacentCellList) {
							if (!adjCell.isBlank()) {
								isValid = false;
								break;
							}
						}
					}
				}
			}
		} else {
			for (int j = cYPos; j < cYPos + shipLength; j++) {
				if (Board.isValidCell(cXPos, j)) {
					c = board.getCellByCoordinate(cXPos, j);
					if (c != null)
						curShipArea.add(c);
				} else
					isValid = false;
				if (isValid) {
					if (!c.isBlank()) {
						isValid = false;
					}
					if (isValid) {
						List<Cell> adjacentCellList = board.getAdjacentCellList(cXPos, j);
						for (Cell adjCell : adjacentCellList) {
							if (!adjCell.isBlank()) {
								isValid = false;
								break;
							}
						}
					}
				}
			}
		}
		return isValid;
	}
}
