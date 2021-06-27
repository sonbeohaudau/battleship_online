package model.unit.warship;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Orientation;
import javafx.scene.paint.ImagePattern;
import model.platform.Cell;
import model.utils.ColorCollection;
import model.utils.ImageCollection;
import model.utils.SoundCollection;

public class Ship {
	private int shipLength;
	private Orientation orien = Orientation.HORIZONTAL;
	private int shipHealth;
	private int shipTypeID;
	private List<Cell> cellList = new ArrayList<Cell>();

	public Ship(int shipTypeID, int shipLength) {
		super();
		this.shipTypeID = shipTypeID;
		this.shipLength = shipLength;
		shipHealth = shipLength;
	}

	public int getShipLength() {
		return shipLength;
	}

	public Orientation getOrien() {
		return orien;
	}

	public int getShipTypeID() {
		return shipTypeID;
	}

	public boolean isSunk() {
		return shipHealth <= 0;
	}
	
	public List<Cell> getCellList() {
		return cellList;
	}

	public void rotateShip() {
		// function works only with size > 1
		if (shipLength == 1)
			return;
		// switch orientation from Horizontal to Vertical and vice versa
		if (this.orien == Orientation.HORIZONTAL) {
			this.orien = Orientation.VERTICAL;
		} else {
			this.orien = Orientation.HORIZONTAL;
		}
	}

	// return true if ship is sunk, else return false
	public boolean damage() {
		// decrease health
		shipHealth--;
		if (shipHealth > 0)
			return false;
		
		sink();
		return true;
	}
	
	public boolean damageSimple() {
		// decrease health
		shipHealth--;
		if (shipHealth > 0)
			return false;
		// terminate all cells around the ship
		Cell c = null;
		List<Cell> adjacentCellList;
		for (int pos = 0; pos < shipLength; pos++) {
			c = cellList.get(pos);
			// terminate surrounding cells
			adjacentCellList = c.getBoard().getAdjacentCellList(c.getXPosition(), c.getYPosition());
			for (Cell adjCell : adjacentCellList) {
				adjCell.terminateCellSimple();
			}
		}

		return true;
	}

	// set the properties for cells that ship holds
	public void launchShip(List<Cell> curShipArea) {
		BufferedImage bufferedImage = null, subBufferedImage = null;
		int div = 0, frameWidth = 0, frameHeight = 0, pos = 0;
		// divide the image into multiple frames
		if (shipTypeID == ShipType.Destroyer.getShipTypeID()) {
			div = 1;
			bufferedImage = ImageCollection.INSTANCE.destroyerImg;
		} else if (shipTypeID == ShipType.Cruiser.getShipTypeID()) {
			div = 2;
			if (orien == Orientation.HORIZONTAL)
				bufferedImage = ImageCollection.INSTANCE.cruiserHorizontalImg;
			else
				bufferedImage = ImageCollection.INSTANCE.cruiserVerticalImg;
		} else if (shipTypeID == ShipType.Battleship.getShipTypeID()) {
			div = 3;
			if (orien == Orientation.HORIZONTAL)
				bufferedImage = ImageCollection.INSTANCE.battleshipHorizontalImg;
			else
				bufferedImage = ImageCollection.INSTANCE.battleshipVerticalImg;
		} else if (shipTypeID == ShipType.Carrier.getShipTypeID()) {
			div = 4;
			if (orien == Orientation.HORIZONTAL)
				bufferedImage = ImageCollection.INSTANCE.carrierHorizontalImg;
			else
				bufferedImage = ImageCollection.INSTANCE.carrierVerticalImg;
		}
		// calculate width, height for frame
		if (orien == Orientation.VERTICAL) {
			frameWidth = bufferedImage.getWidth();
			frameHeight = bufferedImage.getHeight() / div;
		} else {
			frameWidth = bufferedImage.getWidth() / div;
			frameHeight = bufferedImage.getHeight();
		}
		for (Cell cell : curShipArea) {
			// get the right sub image from the main ship image
			if (orien == Orientation.VERTICAL) {
				subBufferedImage = bufferedImage.getSubimage(0, frameHeight * pos, frameWidth, frameHeight);
			} else {
				subBufferedImage = bufferedImage.getSubimage(frameWidth * pos, 0, frameWidth, frameHeight);
			}
			// set ship for cell
			cell.setShip(this, new ImagePattern(SwingFXUtils.toFXImage(subBufferedImage, null)));
			// add current cell to current ship
			cellList.add(cell);
			pos++;

			// DEBUG: show new color for cells
			// cell.storeNewColor(ColorCollection.ORANGE.getRGBColor(),
			// ColorCollection.ORANGE.getRGBColor());
			// cell.showStoredPaint();
		}
	}
	
	public void stealthMode() {
		Cell c = null;
		for (int pos = 0; pos < shipLength; pos++) {
			c = cellList.get(pos);
			c.storeNewColor(ColorCollection.WATERBLUE.getRGBColor(), ColorCollection.WATERBORDER.getRGBColor());
			c.showStoredPaint();
		}
	}
	
	public void sink() {
		
		// terminate all cells around the ship
		Cell c = null;
		List<Cell> adjacentCellList;
		for (int pos = 0; pos < shipLength; pos++) {
			c = cellList.get(pos);
			// show explosion for each cell
			c.showExplosion();
			// terminate surrounding cells
			adjacentCellList = c.getBoard().getAdjacentCellList(c.getXPosition(), c.getYPosition());
			for (Cell adjCell : adjacentCellList) {
				adjCell.terminateCell();
			}
		}
		
		// play sunk SE
		SoundCollection.INSTANCE.playShipExploSFX();
		
		// play music for ship when sunk
		if (shipTypeID == 0) {
			SoundCollection.INSTANCE.playShipSinkSFX(getShipTypeID());
		} else if (shipTypeID == 1) {
			SoundCollection.INSTANCE.playShipSinkSFX(getShipTypeID());
		} else if (shipTypeID == 2) {
			SoundCollection.INSTANCE.playShipSinkSFX(getShipTypeID());
		} else if (shipTypeID == 3) {
			SoundCollection.INSTANCE.playShipSinkSFX(getShipTypeID());
		}
	}
}