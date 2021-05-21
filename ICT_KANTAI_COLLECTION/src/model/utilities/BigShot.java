package model.utilities;

import java.util.ArrayList;
import model.platform.Cell;
import model.player.Player;

public class BigShot extends Ammo{

	public BigShot() {
		super(AmmoType.BigShot.getAmmoTypeID(), AmmoType.BigShot.getQuantity());
	}

	@Override
	public ArrayList<Cell> getTargetArea(Player player, Cell targetCell) {
		// clear the old target area
		targetArea.clear();
		int xPos = targetCell.getXPosition();
		int yPos = targetCell.getYPosition();
		// set and get new target area
		targetArea.add(targetCell);
		Cell c = null;
		
		if (xPos < 9) {
			c = player.getBoard().getCellByCoordinate(xPos + 1, yPos);
			targetArea.add(c);
		}
		
		if (yPos < 9) {
			c = player.getBoard().getCellByCoordinate(xPos, yPos + 1);
			targetArea.add(c);
		}
		
		if (xPos < 9 && yPos < 9) {
			c = player.getBoard().getCellByCoordinate(xPos + 1, yPos + 1);
			targetArea.add(c);
		}
		
		return (ArrayList<Cell>) targetArea;
	}

}
