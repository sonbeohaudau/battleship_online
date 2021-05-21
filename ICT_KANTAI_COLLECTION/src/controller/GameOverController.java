package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import model.system.GameConfig;
import model.system.GameMode;
import model.utils.SoundCollection;

public class GameOverController implements Initializable {
	@FXML
	public AnchorPane gameOverPane;
	@FXML
	public Label resultDisplay;
	@FXML
	public Button backToMenuBt;

	public void initialize(URL location, ResourceBundle resources) {
		
		int gameResult = GameConfig.getGameResult();
		// check if game is draw
		if (gameResult == 0) {
			SoundCollection.INSTANCE.playDrawSound();
			resultDisplay.setText("DRAW!!");
		} else {
			// check if pvp, show who wins
			// else show victory or defeated
			if (GameConfig.getGameMode() == GameMode.TwoPlayers) {
				if (gameResult == 1) {
					SoundCollection.INSTANCE.playVictorySound();
					resultDisplay.setText(GameConfig.getPlayer1().getPlayerName() + " WINS!!");
					System.out.println(GameConfig.getPlayer1().getPlayerName() + " WINS!!");
				} else {
					SoundCollection.INSTANCE.playVictorySound();
					resultDisplay.setText(GameConfig.getPlayer2().getPlayerName() + " WINS!!");
					System.out.println(GameConfig.getPlayer2().getPlayerName() + " WINS!!");
				}
			} else if (GameConfig.getGameMode() == GameMode.VersusBot) {
				if (gameResult == 1) {
					SoundCollection.INSTANCE.playVictorySound();
					resultDisplay.setText("VICTORY!!");
					System.out.println("VICTORY!!");
				} else {
					SoundCollection.INSTANCE.playDefeatSound();
					resultDisplay.setText("DEFEAT!!");
					System.out.println("DEFEAT!!");
				}
			}
		}

	}

	@FXML
	public void handleBtnMenu(ActionEvent evt) {
		if (evt.getSource() == backToMenuBt) {
			SoundCollection.INSTANCE.playConfirmSound();
			System.out.println("Back to main menu");
			FXMLUtilsController.loadSubStage("StartMenu.fxml", "show", GameConfig.getGameTitle());
			gameOverPane.getScene().getWindow().hide();
			System.gc();
			SoundCollection.INSTANCE.stopGameOverMusic();
		}

	}

}
