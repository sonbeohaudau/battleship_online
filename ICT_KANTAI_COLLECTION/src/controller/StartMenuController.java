package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.system.GameConfig;
import model.system.GameMode;
import model.utils.SoundCollection;

public class StartMenuController implements Initializable {
	@FXML
	private Button singlePlayerBtn;
	@FXML
	private Button multiplayerBtn;
	@FXML
	private Button helpBtn;
	@FXML
	private Button exitBtn;
	@FXML
	public TextField nameInput;

	private static String playerName;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// start background music
		SoundCollection.INSTANCE.playStartMenuBackGroundIntro();
		// set difficulty to original
		GameConfig.setAdvancedMode(false);
	}

	@FXML
	private void handleHomeBtn(javafx.event.ActionEvent evt) {
		if (evt.getSource() == multiplayerBtn) {
			// set game mode as pvp
			SoundCollection.INSTANCE.playButtonClickSound();
			GameConfig.setGameMode(GameMode.TwoPlayers);
			// pop up to ask players to choose advance mode
//			FXMLUtilsController.loadSubStage("AskAdvancedMode.fxml", "showAndWait", GameConfig.getGameTitle());
			playerName = nameInput.getText();
			if (playerName.isEmpty() || playerName == null) {
				playerName = "Noname"; 
			}
			// Pop up the player List 
			FXMLUtilsController.loadSubStage("PlayerList.fxml", "showAndWait", GameConfig.getGameTitle());
			// load the ship formation stage
//			FXMLUtilsController.loadSubStage("ShipFormation.fxml", "show", GameConfig.getGameTitle());
			// stop music
			SoundCollection.INSTANCE.stopStartMenuBackGroundIntro();
		} else if (evt.getSource() == singlePlayerBtn) {
			// set game mode as versus bot
			SoundCollection.INSTANCE.playButtonClickSound();
			playerName = nameInput.getText();
			if (playerName.isEmpty() || playerName == null) {
				playerName = "Noname"; 
			}
			GameConfig.setGameMode(GameMode.VersusBot);
			FXMLUtilsController.loadSubStage("ShipFormation.fxml", "show", GameConfig.getGameTitle());
			// stop music
			SoundCollection.INSTANCE.stopStartMenuBackGroundIntro();
		} else if (evt.getSource() == helpBtn) {
			// open game instruction window
			SoundCollection.INSTANCE.playButtonClickSound();
			FXMLUtilsController.loadSubStage("HelpWindow.fxml", "show", GameConfig.getGameTitle());
		}
		// hide main stage
		((Node) (evt.getSource())).getScene().getWindow().hide();
		System.gc();
	}

	@FXML
	private void exitProgram(ActionEvent event) {
		SoundCollection.INSTANCE.playButtonClickSound();
		System.out.println("Gud byeeeee");
		System.exit(0);
	}

	public static String getPlayerName() {
		return playerName;
	}

}
