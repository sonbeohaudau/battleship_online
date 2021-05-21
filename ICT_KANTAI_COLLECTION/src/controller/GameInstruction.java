package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.system.GameConfig;
import model.utils.SoundCollection;

public class GameInstruction implements Initializable {
	@FXML
	public AnchorPane helpPane;
	@FXML
	public Label howToPlayLabel;
	@FXML
	public Text instructText;
	@FXML
	public Button backToMenuBt;

	public void initialize(URL location, ResourceBundle resources) {
		instructText.setText(
				"The objective of the game is to try and sink all of the other player's ships before they sink all of your ones." +
				"The two player's ships are somewhere on their 2D board which is oriented (horizontally or verticallly) by them before the match begin." +
				"The ships are not allowed to be placed adjacent to each others." +
				"Once the game begins, the players may not move the ships.\n\n" +
				"Two players will take turn to fire a cell on other's board while not knowing where the enemy ships are located." +
				"If the shot does not land on a ship, it's a miss (gray color marked). Otherwise, it's a hit (red color marked) and the current player can fire for another turn." +
				"If all cells of a ship are fired, the ship is sunk. The game ends when all the ship of a player (or both at the same turn) are sunk. \n\n" +
				"HAVE FUN!!\n"
				);
	}

	@FXML
	public void handleBtnMenu(ActionEvent evt) {
		if (evt.getSource() == backToMenuBt) {
			SoundCollection.INSTANCE.playConfirmSound();
			System.out.println("Back to main menu");
			SoundCollection.INSTANCE.stopStartMenuBackGroundIntro();
			FXMLUtilsController.loadSubStage("StartMenu.fxml", "show", GameConfig.getGameTitle());
			helpPane.getScene().getWindow().hide();
			System.gc();
			
		}

	}

}
