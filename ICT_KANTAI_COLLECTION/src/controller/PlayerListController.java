package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.player.Player;

public class PlayerListController implements Initializable {
	
	@FXML
	private Tab PlayerListTab;
	
	@FXML
	private Tab ChallengerTab;
	
	@FXML
	private TableView<Player> PlayerListTable;
	
	@FXML
	private TableView<Player> ChallengerListTable;
	
	@FXML
	private TableColumn<Player,String> NameCol;
	
	@FXML
	private TableColumn<Player,String> StatusCol;
	
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
		
//		TableColumn PlayerNameCol1 = new TableColumn("Player Name");
//		TableColumn StatusCol1 = new TableColumn("Status");
//		PlayerListTable.getColumns().addAll(PlayerNameCol1, StatusCol1);
//		
//		TableColumn PlayerNameCol2 = new TableColumn("Player Name");
//		TableColumn StatusCol2 = new TableColumn("Status");
//		ChallengerListTable.getColumns().addAll(PlayerNameCol2, StatusCol2);
		
		
	}
	
	@FXML
	private void RandomPickPlayer(ActionEvent evt) {
		
		((Node) (evt.getSource())).getScene().getWindow().hide();
	}
	
	@FXML
	private void ResetListPlayer(ActionEvent evt) {
		
		((Node) (evt.getSource())).getScene().getWindow().hide();
	}
	
	@FXML
	private void ChallengePlayer(ActionEvent evt) {
		
		((Node) (evt.getSource())).getScene().getWindow().hide();
	}
	
	@FXML
	private void ResetListChallenger(ActionEvent evt) {
		
		((Node) (evt.getSource())).getScene().getWindow().hide();
	}
	
	@FXML
	private void ConfirmChallenge(ActionEvent evt) {
		
		((Node) (evt.getSource())).getScene().getWindow().hide();
	}
}
