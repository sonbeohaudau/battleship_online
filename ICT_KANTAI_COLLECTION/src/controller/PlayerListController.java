package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
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
import javafx.scene.layout.AnchorPane;
import model.system.GameConfig;
import model.utils.SoundCollection;
import socket.client.ClientSocket;
import socket.client.ClientState;

public class PlayerListController implements Initializable {
	
	@FXML
	public AnchorPane playerListPane;
	
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
	private Button ChallengeBtn;
	
	@FXML
	private Button DeclineBtn;
	
	@FXML
	private Button ResetListBtn2;
	
	@FXML
	private Button AcceptBtn;
	
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

		
		//set selection in List into 1 row
		PlayerList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		ChallengerList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		SoundCollection.INSTANCE.playSetupFormationBackGroundSound();
		initDataForSoundCheckMenu();
		
		playerName.setText(StartMenuController.getPlayerName());
		
		GameConfig.setOnlineLobby(this);
		
		updatePlayerList();
		updateChallengerList();
	}

	// setup data for sound check menu
	private void initDataForSoundCheckMenu() {
		bgmCheckMenu.setSelected(GameConfig.checkBGM());
		seCheckMenu.setSelected(GameConfig.checkSE());
	}
	
	private void updatePlayerList() {
//		// clear the current list
//		PlayerList.getItems().clear();
//		
//		// get the list from server and display it
//		ArrayList<String> userList = ClientSocket.getInstance().getUserList();
//		for (String user: userList) {
//			PlayerList.getItems().add(user);
//		}
		
		ClientSocket.getInstance().getUserList();
	}

	private void updateChallengerList() {
		ClientSocket.getInstance().getChallengeList();
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
			// load startMenu and client exit server
			ClientSocket.getInstance().quitOnline();
			
			System.out.println("Back to main menu");
			FXMLUtilsController.loadSubStage("StartMenu.fxml", "show", GameConfig.getGameTitle());
			playerListPane.getScene().getWindow().hide();
			System.gc();
			
		}
		if (evt.getSource() == exitMenuItem) {
			// the button sound effect
			SoundCollection.INSTANCE.playButtonClickSound();
			// stop the FormationBackGroundSound
			SoundCollection.INSTANCE.stopSetupFormationBackGroundSound();

			// client exit server
			
			ClientSocket.getInstance().quitOnline();
			
			System.out.println("Game is shutting down...");
			Platform.exit();
			System.exit(0);
		}
	}
	
	@FXML
	//Action click on Random button
	private void RandomPickPlayer(ActionEvent evt) {	// searching for an opponent to play with
		SoundCollection.INSTANCE.playButtonClickSound();
		
		if (ClientSocket.getInstance().getClientState() == ClientState.Idle) {
			// get into matching mode and disable some buttons
			ClientSocket.getInstance().matchRandom();
			
			RandomBtn.setText("Cancel");
			ResetListBtn1.setDisable(true);
			ResetListBtn2.setDisable(true);
			ChallengeBtn.setDisable(true);
			AcceptBtn.setDisable(true);
			DeclineBtn.setDisable(true);

			
		} else if (ClientSocket.getInstance().getClientState() == ClientState.Matching) {
			// return to idle mode and enable some buttons
			ClientSocket.getInstance().setClientState(ClientState.Idle);
			
			RandomBtn.setText("Random");
			ResetListBtn1.setDisable(false);
			ResetListBtn2.setDisable(false);
			ChallengeBtn.setDisable(false);
			AcceptBtn.setDisable(false);
			DeclineBtn.setDisable(false);
		}
		
	}
	
	@FXML
	// Action click on Reset Button in the Player List tag
	private void ResetListPlayer(ActionEvent evt) {
		SoundCollection.INSTANCE.playButtonClickSound();
		
		// update new list
		updatePlayerList();
	}
	
	@FXML
	//Action click on ResetList Button in the Challenger tag
	private void ResetListChallenger(ActionEvent evt) {
		SoundCollection.INSTANCE.playButtonClickSound();
		
		// update new list
		updateChallengerList();
	}

	@FXML
	//Action click on Challenge button in the Player List tag
	private void ChallengePlayer(ActionEvent evt) {
		SoundCollection.INSTANCE.playButtonClickSound();
		
		if (ClientSocket.getInstance().getClientState() == ClientState.Idle) {
			ChallengeBtn.setText("Cancel");
			ResetListBtn1.setDisable(true);
			ResetListBtn2.setDisable(true);
			RandomBtn.setDisable(true);
			AcceptBtn.setDisable(true);
			DeclineBtn.setDisable(true);
			
			String chosenPlayer = PlayerList.getSelectionModel().getSelectedItem();
			String message = "Player " + chosenPlayer + " has been chosen !";
			System.out.println(message);
			
			String[] params = chosenPlayer.split(" / ");
			if (params[1].indexOf("idle") != -1) {	// if the player chosen is idle, challenge him/her
				
				ClientSocket.getInstance().challenge(chosenPlayer);
				
			}
		} else if (ClientSocket.getInstance().getClientState() == ClientState.Pending) {
			// return to idle mode and enable some buttons
			ClientSocket.getInstance().setClientState(ClientState.Idle);
			
			ChallengeBtn.setText("Challenge");
			ResetListBtn1.setDisable(false);
			ResetListBtn2.setDisable(false);
			RandomBtn.setDisable(false);
			AcceptBtn.setDisable(false);
			DeclineBtn.setDisable(false);
		}
		
		
		
		
	}
	
	@FXML
	//Action click on Decline Button in the Challenger tag
	private void DeclineChallenger(ActionEvent evt) {
		SoundCollection.INSTANCE.playButtonClickSound();
		
		String ChosenPlayer = ChallengerList.getSelectionModel().getSelectedItem();
		String message = "Player " + ChosenPlayer + " has been chosen !";
		System.out.print(message);
		
		ClientSocket.getInstance().responseChallenge(ChosenPlayer, false);
	}
	
	@FXML
	//Action click on Accept button in the Challenger tag
	private void AcceptChallenge(ActionEvent evt) {
		SoundCollection.INSTANCE.playButtonClickSound();
		
		String ChosenPlayer = ChallengerList.getSelectionModel().getSelectedItem();
		String message = "Player " + ChosenPlayer + " has been chosen !";
		System.out.print(message);
		
		ClientSocket.getInstance().responseChallenge(ChosenPlayer, true);
	}
	
	@FXML
	public void updatePlayerList(ArrayList<String> userList) {
		Platform.runLater(new Runnable(){

			@Override
			public void run() {
				// clear the current list
				PlayerList.getItems().clear();
				
				// update the list got from server
				for (String user: userList) {
					PlayerList.getItems().add(user);
				}
			}
			
		});
		
		
	}
	
	@FXML
	public void updateChallengeList(ArrayList<String> challengeList) {
		Platform.runLater(new Runnable(){

			@Override
			public void run() {
				// clear the current list
				ChallengerList.getItems().clear();
						
				// update the list got from server
				for (String challenge: challengeList) {
					ChallengerList.getItems().add(challenge);
				}
			}
			
		});
		
		
	}
	
	@FXML
	public void goToGameWindow() {
		Platform.runLater(new Runnable(){

			@Override
			public void run() {
				// get to ship set up window
				FXMLUtilsController.loadSubStage("ShipFormation.fxml", "show", GameConfig.getGameTitle());
				
				// stop music
				SoundCollection.INSTANCE.stopStartMenuBackGroundIntro();
							
				// hide main stage
				playerName.getScene().getWindow().hide();
				
				System.gc();
			}
			
		});
		
	}
}
