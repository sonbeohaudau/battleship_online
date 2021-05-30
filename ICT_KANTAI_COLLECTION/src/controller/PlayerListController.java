package controller;

import java.net.URL;
import java.util.ArrayList;
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
import model.system.GameConfig;
import model.utils.SoundCollection;
import socket.client.ClientSocket;
import socket.client.ClientState;

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
//		addSamplePlayerList();
//		addSampleChallengerList();
		//Challenger List
//		ChallengerList.getItems().add("Hung" + "     " + "Playing");
		
		updatePlayerList();
		updateChallengerList();
		
		//set selection in List into 1 row
		PlayerList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		ChallengerList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	}

	private void updatePlayerList() {
		// clear the current list
		PlayerList.getItems().clear();
		
		// get the list from server and display it
		ArrayList<String> userList = ClientSocket.getInstance().getUserList();
		for (String user: userList) {
			PlayerList.getItems().add(user);
		}
	}

	private void updateChallengerList() {
		// TODO
	}

	//Update Player in the Player List
	private void addSamplePlayerList() {
		PlayerList.getItems().add("Duy" + " - " + "Playing");
		PlayerList.getItems().add("Son" + " - " + "Playing");
		PlayerList.getItems().add("Duong" + " - " + "Online");
		PlayerList.getItems().add("HA" + " - " + "Online");
	}
	
	//Update Challenger in the Challenger List
	private void addSampleChallengerList() {
		ChallengerList.getItems().add("Hung" + "     " + "Playing");
	}
	
	@FXML
	//Action click on Random button
	private void RandomPickPlayer(ActionEvent evt) {
		
		// TODO: add a Cancel Button
		
		if (ClientSocket.getInstance().matchRandom()) {
			// match found with random opponent
			FXMLUtilsController.loadSubStage("ShipFormation.fxml", "show", GameConfig.getGameTitle());
			
			// stop music
			SoundCollection.INSTANCE.stopStartMenuBackGroundIntro();
						
			// hide main stage
			((Node) (evt.getSource())).getScene().getWindow().hide();
			
			System.gc();
		}
		
//		((Node) (evt.getSource())).getScene().getWindow().hide();
	}
	
	@FXML
	// Action click on Reset Button in the Player List tag
	private void ResetListPlayer(ActionEvent evt) {
		// test piece of code replacing for cancel matching / pending
		if (ClientSocket.getInstance().getClientState() != ClientState.Idle) {
			ClientSocket.getInstance().setClientState(ClientState.Idle);
		}
		
		// update new list
		updatePlayerList();
	}
	
	@FXML
	//Action click on Accept button in the Player List tag
	private void ChallengePlayer(ActionEvent evt) {
		String ChosenPlayer = PlayerList.getSelectionModel().getSelectedItem();
		String message = "Player " + ChosenPlayer + " has been chosen !";
		System.out.print(message);
		
		String[] params = ChosenPlayer.split(" / ");
		if (params[1].indexOf("Idle") != -1) {	// if the player chosen is idle, challenge him/her
			if (ClientSocket.getInstance().challenge(params[0])) {
				// if opponent accept challenge
				FXMLUtilsController.loadSubStage("ShipFormation.fxml", "show", GameConfig.getGameTitle());
				
				// stop music
				SoundCollection.INSTANCE.stopStartMenuBackGroundIntro();
							
				// hide main stage
				((Node) (evt.getSource())).getScene().getWindow().hide();
				
				System.gc();
			}
		}
	}
	
	@FXML
	//Action click on Reset Button in the Challenger tag
	private void ResetListChallenger(ActionEvent evt) {
		updateChallengerList();
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
