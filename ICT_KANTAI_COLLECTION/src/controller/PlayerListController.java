package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;

public class PlayerListController implements Initializable {
	
	@FXML
	private Tab PlayerListTab;
	
	@FXML
	private Tab ChallengerTab;
	
	@FXML
	private ListView<String> PlayerList;
	
	@FXML
	private ListView<String> ChallengerList;
	
	@FXML
	private Button RandomBtn;
	
	@FXML
	private Button ResetListBtn1;
	
	@FXML
	private Button AcceptBtn1;
	
	@FXML
	private Button ResetListBtn2;
	
	@FXML
	private Button AcceptBtn2;

	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		updatePlayerList();
		updateChallengerList();
		//Challenger List
		ChallengerList.getItems().add("Hung" + "     " + "Playing");
		
		//set selection in List into 1 row
		PlayerList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		ChallengerList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	}

	//Update Player in the Player List
	private void updatePlayerList() {
		PlayerList.getItems().add("Duy" + "     " + "Playing");
		PlayerList.getItems().add("Son" + "     " + "Playing");
		PlayerList.getItems().add("Duong" + "     " + "Online");
		PlayerList.getItems().add("HA" + "     " + "Online");
	}
	
	//Update Challenger in the Challenger List
	private void updateChallengerList() {
		ChallengerList.getItems().add("Hung" + "     " + "Playing");
	}
	
	@FXML
	//Action click on Random button
	private void RandomPickPlayer(ActionEvent evt) {
		
		((Node) (evt.getSource())).getScene().getWindow().hide();
	}
	
	@FXML
	// Action click on Reset Button in the Player List tag
	private void ResetListPlayer(ActionEvent evt) {
		
	}
	
	@FXML
	//Action click on Accept button in the Player List tag
	private void ChallengePlayer(ActionEvent evt) {
		String ChosenPlayer = PlayerList.getSelectionModel().getSelectedItem();
		String message = "Player " + ChosenPlayer + " has been chosen !";
		System.out.print(message);
		((Node) (evt.getSource())).getScene().getWindow().hide();
	}
	
	@FXML
	//Action click on Reset Button in the Challenger tag
	private void ResetListChallenger(ActionEvent evt) {
		
	}
	
	@FXML
	//Action click on Accept button in the Challenger tag
	private void ConfirmChallenge(ActionEvent evt) {
		String ChosenPlayer = ChallengerList.getSelectionModel().getSelectedItem();
		String message = "Player " + ChosenPlayer + " has been chosen !";
		System.out.print(message);
		((Node) (evt.getSource())).getScene().getWindow().hide();
	}
}
