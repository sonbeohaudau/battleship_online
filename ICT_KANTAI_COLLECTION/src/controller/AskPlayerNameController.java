package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.utils.SoundCollection;

public class AskPlayerNameController implements Initializable {
	@FXML
	public Label infoLabel;
	@FXML
	public TextField nameInput;
	@FXML
	public Button confirmNameBtn;
	
	private String playerName;
	private int playerNum;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Platform.runLater(() -> {
			infoLabel.setText("Enter name for player " + playerNum);
		});
	}
	
	@FXML
	private void confirmNameAction(ActionEvent evt) {
		SoundCollection.INSTANCE.playConfirmSound();
		playerName = nameInput.getText();
		if (playerName.isEmpty() || playerName == null) {
			playerName = "Player " + playerNum; 
		}
		((Node) (evt.getSource())).getScene().getWindow().hide();
	}
	
	// name will be filled before this fxml is shown
	public void initData(int playerNum) {
		this.playerNum = playerNum;
	}
	
	public String getPlayerName() {
		return playerName;
	}

}
