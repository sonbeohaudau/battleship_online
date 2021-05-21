package model.utils;

import java.awt.image.BufferedImage;

import controller.FXMLUtilsController;

public enum ImageCollection {
	INSTANCE;
	
	public BufferedImage seaFrames;
	public BufferedImage waterExplosionFrames;
	public BufferedImage shipExplosionFrames;
	public BufferedImage destroyerImg;
	// vertical image
	public BufferedImage carrierVerticalImg;
	public BufferedImage battleshipVerticalImg;
	public BufferedImage cruiserVerticalImg;
	
	// horizontal image
	public BufferedImage carrierHorizontalImg;
	public BufferedImage battleshipHorizontalImg;
	public BufferedImage cruiserHorizontalImg;
	
	public void initImageCollection() {
		seaFrames = loadImageByLocation("sea_frame.png", "animation");
		waterExplosionFrames = loadImageByLocation("water_explosion.jpg", "animation"); 
		shipExplosionFrames = loadImageByLocation("ship_explosion.jpg", "animation");
		destroyerImg = loadImageByLocation("Destroyer.png", "Destroyer");
		
		carrierVerticalImg = loadImageByLocation("Carrier.png", "Carrier");
		battleshipVerticalImg = loadImageByLocation("Battleship.png", "Battleship");
		cruiserVerticalImg = loadImageByLocation("Cruiser.png", "Cruiser");
		
		carrierHorizontalImg = loadImageByLocation("Carrier2.png", "Carrier");
		battleshipHorizontalImg = loadImageByLocation("Battleship2.png", "Battleship");
		cruiserHorizontalImg = loadImageByLocation("Cruiser2.png", "Cruiser");
	}
	
	private BufferedImage loadImageByLocation(String url, String type) {
		String src = "resources/";
		if (type.equals("animation")) {
			src += "animation/";
		} else if (type.equals("Destroyer")) {
			src += "imgs/Battleship/Destroyer/";
		} else if (type.equals("Cruiser")) {
			src += "imgs/Battleship/Cruiser/";
		} else if (type.equals("Battleship")) {
			src += "imgs/Battleship/Battleship/";
		} else if (type.equals("Carrier")) {
			src += "imgs/Battleship/Carrier/";
		}
		return FXMLUtilsController.getBufferedImageByName(src + url);
	}
}
