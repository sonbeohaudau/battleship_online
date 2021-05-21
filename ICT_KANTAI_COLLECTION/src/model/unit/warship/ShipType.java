package model.unit.warship;

public enum ShipType {
	Carrier("Carrier", 0, 4),
	Battleship("Battleship", 1, 3),
	Cruiser("Cruiser", 2, 2),
	Destroyer("Destroyer", 3, 1),
	Submarine("Submarine", 4, 3);
	
	private final String shipType;
	private final int shipTypeID;
	private final int shipLength;
	
	ShipType(String shipType, int shipTypeID, int shipLength) {
		this.shipType = shipType;
		this.shipTypeID = shipTypeID;
		this.shipLength = shipLength;
	}
	
	public String getShipType() {
		return shipType;
	}
	
	public int getShipTypeID() {
		return shipTypeID;
	}
	
	public int getShipLength() {
		return shipLength;
	}
	
}
