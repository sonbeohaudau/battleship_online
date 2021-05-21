package model.unit.warship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

// prepare ships for ship formation
public enum ShipArmy {
	INSTANCE;
	List<Ship> shipArmy;
	ListIterator<Ship> shipArmyIterator;
	HashMap<Integer, IntegerProperty> shipArmyTrack;

	public void setupArmy() {
		shipArmy = new ArrayList<Ship>();

		shipArmy.add(new Ship (ShipType.Carrier.getShipTypeID(), ShipType.Carrier.getShipLength()));

		shipArmy.add(new Ship (ShipType.Battleship.getShipTypeID(), ShipType.Battleship.getShipLength()));
		shipArmy.add(new Ship (ShipType.Battleship.getShipTypeID(), ShipType.Battleship.getShipLength()));

		shipArmy.add(new Ship (ShipType.Cruiser.getShipTypeID(), ShipType.Cruiser.getShipLength()));
		shipArmy.add(new Ship (ShipType.Cruiser.getShipTypeID(), ShipType.Cruiser.getShipLength()));
		shipArmy.add(new Ship (ShipType.Cruiser.getShipTypeID(), ShipType.Cruiser.getShipLength()));

		shipArmy.add(new Ship (ShipType.Destroyer.getShipTypeID(), ShipType.Destroyer.getShipLength()));
		shipArmy.add(new Ship (ShipType.Destroyer.getShipTypeID(), ShipType.Destroyer.getShipLength()));
		shipArmy.add(new Ship (ShipType.Destroyer.getShipTypeID(), ShipType.Destroyer.getShipLength()));
		shipArmy.add(new Ship (ShipType.Destroyer.getShipTypeID(), ShipType.Destroyer.getShipLength()));

		// set the iterator when data is fully pushed into list
		shipArmyIterator = shipArmy.listIterator();
	}

	public void setupArmyTrack() {
		shipArmyTrack = new HashMap<Integer, IntegerProperty>();
		shipArmyTrack.put(ShipType.Carrier.getShipTypeID(), new SimpleIntegerProperty(1));
		shipArmyTrack.put(ShipType.Battleship.getShipTypeID(), new SimpleIntegerProperty(2));
		shipArmyTrack.put(ShipType.Cruiser.getShipTypeID(), new SimpleIntegerProperty(3));
		shipArmyTrack.put(ShipType.Destroyer.getShipTypeID(), new SimpleIntegerProperty(4));
	}

	public List<Ship> getShipArmy() {
		return shipArmy;
	}

	public Ship getNextShip() {
		if (shipArmyIterator.hasNext()) {
			return shipArmyIterator.next();
		}
		return null;
	}

	public int getShipArmyNumber() {
		return shipArmy.size();
	}

	public String getShipTypeByTypeID(int typeID) {
		if (typeID == ShipType.Carrier.getShipTypeID()) {
			return ShipType.Carrier.getShipType();
		} else if (typeID == ShipType.Battleship.getShipTypeID()) {
			return ShipType.Battleship.getShipType();
		} else if (typeID == ShipType.Cruiser.getShipTypeID()) {
			return ShipType.Cruiser.getShipType();
		} else {
			return ShipType.Destroyer.getShipType();
		}
	}

	public int getShipArmyTypeSize() {
		return shipArmyTrack.size();
	}

	public int getNumOfShipLeftByTypeID(int typeID) {
		return shipArmyTrack.get(typeID).get();
	}

	public IntegerProperty getNumPropertyOfShipLeftByTypeID(int typeID) {
		return shipArmyTrack.get(typeID);
	}

	public void updateNumOfShipLeftByTypeID(int typeID) {
		shipArmyTrack.get(typeID).set(shipArmyTrack.get(typeID).get() - 1);
	}

}
