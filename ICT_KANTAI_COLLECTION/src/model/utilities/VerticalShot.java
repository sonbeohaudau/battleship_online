package model.utilities;

import java.util.ArrayList;
import model.platform.Cell;
import model.player.Player;

public class VerticalShot extends Ammo{

	public VerticalShot() {
		super(AmmoType.VerticalShot.getAmmoTypeID(), AmmoType.VerticalShot.getQuantity());
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
			int temp = yPos + i;
			if (temp < 10) {
				c = player.getBoard().getCellByCoordinate(xPos, temp);
				targetArea.add(c);
			}
		}
		return (ArrayList<Cell>) targetArea;
	}

}
