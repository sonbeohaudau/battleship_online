package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import model.system.GameConfig;
import model.utils.SoundCollection;
import socket.client.ClientSocket;
import socket.client.ClientState;

public class PlayerListController implements Initializable {
	
	@FXML
	private Label playerName;
	
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
	
	@FXML
	private CheckMenuItem bgmCheckMenu;
	
	@FXML
	private CheckMenuItem seCheckMenu;
	
	@FXML
	private MenuItem backToMenuItem;
	
	@FXML
	private MenuItem exitMenuItem;

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
		
		// setup data for sound check menu
		bgmCheckMenu.setSelected(GameConfig.checkBGM());
		seCheckMenu.setSelected(GameConfig.checkSE());
		
		playerName.setText(StartMenuController.getPlayerName());
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

//	//Update Player in the Player List
//	private void addSamplePlayerList() {
//		PlayerList.getItems().add("Duy" + " - " + "Playing");
//		PlayerList.getItems().add("Son" + " - " + "Playing");
//		PlayerList.getItems().add("Duong" + " - " + "Online");
//		PlayerList.getItems().add("HA" + " - " + "Online");
//	}
	
	//Update Challenger in the Challenger List
	private void addSampleChallengerList() {
		ChallengerList.getItems().add("Hung" + "     " + "Playing");
	}
	
	//action on Help and Setting menuBar
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
			
			// Todo: load startMenu and client exit server
//			FXMLUtilsController.loadSubStage("StartMenu.fxml", "show", GameConfig.getGameTitle());

			System.gc();
		}
		if (evt.getSource() == exitMenuItem) {
			// the button sound effect
			SoundCollection.INSTANCE.playButtonClickSound();
			// stop the FormationBackGroundSound
			SoundCollection.INSTANCE.stopSetupFormationBackGroundSound();

			//Todo: client exit server
//			System.out.println("Game is shutting down...");
//			Platform.exit();
//			System.exit(0);
		}
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
		System.out.println(message);
		
		String[] params = ChosenPlayer.split(" / ");
		if (params[1].indexOf("idle") != -1) {	// if the player chosen is idle, challenge him/her
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
