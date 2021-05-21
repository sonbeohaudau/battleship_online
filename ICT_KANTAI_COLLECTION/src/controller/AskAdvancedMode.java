package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import model.system.GameConfig;
import model.utils.SoundCollection;

public class AskAdvancedMode implements Initializable {
	@FXML
	public Button originalBtn;
	@FXML
	public Button advanceBtn;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}
	
	@FXML
	private void confirmDifficultMenuAction(ActionEvent evt) {
		// play confirm button sound
		SoundCollection.INSTANCE.playConfirmSound();
		// if player choose advance button
		if (evt.getSource() == advanceBtn) {
			GameConfig.setAdvancedMode(true);
		}
		((Node) (evt.getSource())).getScene().getWindow().hide();
	}
}
