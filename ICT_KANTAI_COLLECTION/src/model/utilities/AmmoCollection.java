package model.utilities;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public enum AmmoCollection {
	INSTANCE;
	List<Ammo> ammoCollection;
	ListIterator<Ammo> ammoCollectionIterator;
	HashMap<Integer, IntegerProperty> ammoCollectionTrack;
	
	public void setupAmmoCollection () {
		 ammoCollection = new ArrayList<Ammo>();
		 
		 ammoCollection.add(new NormalShot());
		 
		 ammoCollection.add(new BigShot());
		 
		 ammoCollection.add(new HorizontalShot());
		 
		 ammoCollection.add(new VerticalShot());
		 
		// set the iterator when data is fully pushed into list
		 ammoCollectionIterator = ammoCollection.listIterator();
	}

	public void setupAmmoCollectionTrack() {
		ammoCollectionTrack = new HashMap<Integer, IntegerProperty>();
		ammoCollectionTrack.put(AmmoType.NormalShot.getAmmoTypeID(), new SimpleIntegerProperty(20));
		ammoCollectionTrack.put(AmmoType.BigShot.getAmmoTypeID(), new SimpleIntegerProperty(1));
		ammoCollectionTrack.put(AmmoType.HorizontalShot.getAmmoTypeID(), new SimpleIntegerProperty(1));
		ammoCollectionTrack.put(AmmoType.VerticalShot.getAmmoTypeID(), new SimpleIntegerProperty(1));
	}
	
	public List<Ammo> getAmmoCollection() {
		return ammoCollection;
	}
	
	public Ammo getNextAmmo() {
		if (ammoCollectionIterator.hasNext()) {
			return ammoCollectionIterator.next();
		}
		return null;
	}
	
	public int getAmmoCollectionNumber() {
		return ammoCollection.size();
	}
	
	public String getAmmoTypeByTypeID(int typeID) {
		if (typeID == AmmoType.BigShot.getAmmoTypeID()) {
			return AmmoType.BigShot.getAmmoType();
		}else if (typeID == AmmoType.HorizontalShot.getAmmoTypeID()) {
			return AmmoType.HorizontalShot.getAmmoType();
		}else if (typeID == AmmoType.VerticalShot.getAmmoTypeID()) {
			return AmmoType.VerticalShot.getAmmoType();
		}else {
			return AmmoType.NormalShot.getAmmoType();
		}
	}
	
	public int getAmmoCollectionTypeSize() {
		return ammoCollectionTrack.size();
	}
	
	public int getNumberOfAmmoLeftByTypeID (int typeID) {
		return ammoCollectionTrack.get(typeID).get();
	}
	
	public IntegerProperty getNumPropertyOfAmmoLeftByTypeID (int typeID) {
		return ammoCollectionTrack.get(typeID);
	}
	
	public void updateNumOfAmmoLeftByTypeID(int typeID) {
		ammoCollectionTrack.get(typeID).set(ammoCollectionTrack.get(typeID).get() - 1);
	}
	
}
