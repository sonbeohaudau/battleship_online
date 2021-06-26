package controller;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import model.system.GameConfig;
import model.system.GameMode;
import model.utils.SoundCollection;

public class GameOverController implements Initializable {
	@FXML
	public AnchorPane gameOverPane;
	@FXML
	public Button backToMenuBt;
	@FXML
	public ImageView backGround;

	public void initialize(URL location, ResourceBundle resources) {

		Class<?> clazz = this.getClass();
		InputStream input1 = clazz.getResourceAsStream("/resources/imgs/Victory1.jpg");
		InputStream input2 = clazz.getResourceAsStream("/resources/imgs/Defeat1.jpg");
		Image victoryImage = new Image(input1);
		Image defeatImage = new Image(input2);

		int gameResult = GameConfig.getGameResult();
		// check if game is draw
		if (gameResult == 0) {
			SoundCollection.INSTANCE.playDrawSound();
//			resultDisplay.setText("DRAW!!");
		} else {
			// check if pvp, show who wins
			// else show victory or defeated
			if (GameConfig.getGameMode() == GameMode.TwoPlayers) {
				if (gameResult == 1) {
					SoundCollection.INSTANCE.playVictorySound();
//					resultDisplay.setText(GameConfig.getPlayer1().getPlayerName() + " WINS!!");
					backGround.setImage(victoryImage);
					System.out.println(GameConfig.getPlayer1().getPlayerName() + " WINS!!");
				} else {
					SoundCollection.INSTANCE.playVictorySound();
//					resultDisplay.setText(GameConfig.getPlayer2().getPlayerName() + " WINS!!");
					backGround.setImage(victoryImage);
					System.out.println(GameConfig.getPlayer2().getPlayerName() + " WINS!!");
				}
			} else if (GameConfig.getGameMode() == GameMode.VersusBot) {
				if (gameResult == 1) {
					SoundCollection.INSTANCE.playVictorySound();
//					resultDisplay.setText("VICTORY!!");
					backGround.setImage(victoryImage);
					System.out.println("VICTORY!!");
				} else {
					SoundCollection.INSTANCE.playDefeatSound();
//					resultDisplay.setText("DEFEAT!!");
					backGround.setImage(defeatImage);
					System.out.println("DEFEAT!!");
				}
			} else if (GameConfig.getGameMode() == GameMode.Online) {
				if (gameResult == 1) {
					SoundCollection.INSTANCE.playVictorySound();
//					resultDisplay.setText("VICTORY!!");
					backGround.setImage(victoryImage);
					System.out.println("VICTORY!!");
				} else {
					SoundCollection.INSTANCE.playDefeatSound();
//					resultDisplay.setText("DEFEAT!!");
					backGround.setImage(defeatImage);
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
