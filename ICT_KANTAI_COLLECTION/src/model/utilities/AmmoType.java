package model.utilities;

public enum AmmoType {
	NormalShot("NormalShot", 0, 100),
	BigShot("Bigshot", 1, 1),
	HorizontalShot("HorizontalShot", 2, 1),
	VerticalShot("VerticalShot", 3, 1);
	
 	private final String ammoType;
	private final int ammoTypeID;
	private final int quantity;
	
	private AmmoType(String ammoType, int ammoTypeID, int quantity) {
		this.ammoType = ammoType;
		this.ammoTypeID = ammoTypeID;
		this.quantity = quantity;
	}

	public String getAmmoType() {
		return ammoType;
	}

	public int getAmmoTypeID() {
		return ammoTypeID;
	}

	public int getQuantity() {
		return quantity;
	}
	
}
