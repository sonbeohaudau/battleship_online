package model.utilities;

import java.util.ArrayList;
import model.platform.Cell;
import model.player.Player;

public class HorizontalShot extends Ammo{

	public HorizontalShot() {
		super(AmmoType.HorizontalShot.getAmmoTypeID(), AmmoType.HorizontalShot.getQuantity());
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
		for (int i = 1; i<=4; i++) {
			int temp = xPos + i;
			if (temp < 10) {
				c = player.getBoard().getCellByCoordinate(temp, yPos);
				targetArea.add(c);
			}
		}
		return (ArrayList<Cell>) targetArea;
	}

}
