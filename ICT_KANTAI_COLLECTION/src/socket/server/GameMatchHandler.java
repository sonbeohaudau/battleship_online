package socket.server;

import model.player.Player;
import model.utils.MagicGenerator;

public class GameMatchHandler {
	private int numOfFormationReceived = 0;
	private Player player1;
	private Player player2;
	private Player turn;		// indicate whose turn it is
	private boolean battleStage = false;
	
	public GameMatchHandler(String userID1, String userID2) {
		player1 = new Player(userID1);
		player2 = new Player(userID2);
	}
	
	public Player getPlayer(String userID) {
		if (player1.getPlayerName().equals(userID))
			return player1;
		if (player2.getPlayerName().equals(userID))
			return player2;
		return null;
	}
	
	public void processShipSetUp(String userID, String formation) {
		String[] shipLocations = formation.split(",");	// split into strings containing location of each ship on board
		String[] params;	
		String shipDirection;
		for (String s: shipLocations) {
			params = s.split("-");
			if (params[0].indexOf("V")==0)
				shipDirection = "vertical";
			else
				shipDirection = "horizontal";
			System.out.println("Ship: " + shipDirection + ", length=" + params[1] + ", x=" + params[2] + ", y=" + params[3]);
		}
		
		this.numOfFormationReceived++;
		
		// TODO: process and set the board for Player
		
		// when server received formation from both players, switch to battle stage
		if (this.numOfFormationReceived == 2)
			enterBattleStage();
		
	}
	
	private void enterBattleStage() {
		int playerGoFirst = MagicGenerator.getRandInt(1);
		if (playerGoFirst == 0)
			turn = player1;
		else
			turn = player2;
		
		System.out.println("Setup stage completed. Player " + turn.getPlayerName() + " go first.");
		
		this.battleStage = true;
	}

	public int getNumOfFormationReceived() {
		return numOfFormationReceived;
	}
	
	public boolean isBattleStage() {
		return this.battleStage;
	}
	
	public String getCurrentTurnPlayerID() {
		return turn.getPlayerName();
	}
}
