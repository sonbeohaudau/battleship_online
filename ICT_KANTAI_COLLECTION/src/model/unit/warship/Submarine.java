package model.unit.warship;

public class Submarine extends Ship {

	public Submarine() {
		super(ShipType.Submarine.getShipTypeID(), ShipType.Submarine.getShipLength());
	}

	@Override
	public void sink() {

	}

	// public Submarine(int iD, int shipLength) {
	// super(iD, shipLength);
	// this.image = "";
	// }
}
