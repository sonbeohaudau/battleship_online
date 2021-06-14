package socket.server;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Orientation;
import model.platform.Board;
import model.platform.Cell;
import model.player.Player;
import model.unit.warship.Ship;
import model.unit.warship.ShipType;
import model.utils.MagicGenerator;

public class GameMatchHandler {
	private int numOfFormationReceived = 0;
	private Player player1;
	private Player player2;
	private Player turn;		// indicate whose turn it is
	private boolean battleStage = false;
	
	public GameMatchHandler(String userID1, String userID2) {
		player1 = new Player(userID1, new Board());
		player2 = new Player(userID2, new Board());
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
			
			// for each ship, get its direction and coordination
			params = s.split("-");
			if (params[0].indexOf("V")==0)
				shipDirection = "vertical";
			else
				shipDirection = "horizontal";
			System.out.println("Ship: " + shipDirection + ", length=" + params[1] + ", x=" + params[2] + ", y=" + params[3]);
			
			setShip(userID, shipDirection, params[1], params[2], params[3]);
		}
		
		this.numOfFormationReceived++;
		
		// TODO: process and set the board for Player
		
		// when server received formation from both players, switch to battle stage
		if (this.numOfFormationReceived == 2)
			enterBattleStage();
		
	}
	
	private void setShip(String userID, String shipDirection, String shipLength, String xPos, String yPos) {
		int length, x, y;
		Ship ship;
		Board board = getPlayer(userID).getBoard();
		
		// preprocess parameters
		length = Integer.parseInt(shipLength);
		x = Integer.parseInt(xPos);
		y = Integer.parseInt(yPos);
		
		// get the ship type according to length
		switch(length) {
		case 4:
			ship = new Ship (ShipType.Carrier.getShipTypeID(), ShipType.Carrier.getShipLength());
			break;
		case 3:
			ship = new Ship (ShipType.Battleship.getShipTypeID(), ShipType.Battleship.getShipLength());
			break;
		case 2:
			ship = new Ship (ShipType.Cruiser.getShipTypeID(), ShipType.Cruiser.getShipLength());
			break;
		case 1:
			ship = new Ship (ShipType.Destroyer.getShipTypeID(), ShipType.Destroyer.getShipLength());
			break;
		default:
			//TODO: error?
			ship = new Ship (ShipType.Destroyer.getShipTypeID(), ShipType.Destroyer.getShipLength());
			break;
		}
		
		if (shipDirection.indexOf("vertical") != -1) {
			ship.rotateShip();
		} 
		
		// add the cells to the cell list of ship
		Cell c = board.getCellByCoordinate(x, y);
		ship.getCellList().add(c);
		if (length > 1) {
			if (ship.getOrien() == Orientation.HORIZONTAL) {	// Horizontal ship
				for (int i = 2; i <= length; i++) {
					c = board.getCellByCoordinate(x + i - 1, y);
					ship.getCellList().add(c);
				}
			} else {	// Vertical ship
				for (int i = 2; i <= length; i++) {
					c = board.getCellByCoordinate(x, y + i - 1);
					ship.getCellList().add(c);
				}
			}
		}
		
		
		// add the ship to player's board/army 
		board.addShipToArmy(ship);
		
		
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
	
	public String processPlayerFire(String user, int x, int y) {
		String result = "";
		Player player = getPlayer(user);
		// TODO: process fire action from player
		
		return result;
	}
}
