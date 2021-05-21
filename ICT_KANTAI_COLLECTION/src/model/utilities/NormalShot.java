package model.utilities;

import java.util.ArrayList;
import model.platform.Cell;
import model.player.Player;

public class NormalShot extends Ammo{


	public NormalShot() {
		super(AmmoType.NormalShot.getAmmoTypeID(), AmmoType.NormalShot.getQuantity());
	}

	@Override
	public ArrayList<Cell> getTargetArea(Player player, Cell targetCell) {
		// clear the old target area
		targetArea.clear();
		
		// set and get new target area
		targetArea.add(targetCell);
		
		return (ArrayList<Cell>) targetArea;
	}

}
