package model.utilities;

import java.util.ArrayList;
import java.util.List;

import model.platform.Cell;
import model.player.Player;

public abstract class Ammo {
	protected List<Cell> targetArea = new ArrayList<>();
	
	private int ammoTypeID;
	private int quantity;
	
	public abstract ArrayList<Cell> getTargetArea (Player player, Cell targetCell);
	
	public boolean isValidTarget() {
		for (Cell c: targetArea) {
			if (!c.isFired()) return true;
		}
		return false;
	}
	
	public int getAmmoQuantity() {
		return quantity;
	}
	
	public int getAmmoTypeID() {
		return ammoTypeID;
	}

	public Ammo(int ammoTypeID, int quantity) {
		super();
		this.ammoTypeID = ammoTypeID;
		this.quantity = quantity;
	}
}
