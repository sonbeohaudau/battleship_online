package model.utils;

import javafx.scene.paint.Color;

public enum ColorCollection {
	// color for background
	WATERBLUE(144, 203, 253),
	WATERBORDER(22, 27, 209),
	RULER(193, 200, 186),
	RULERBORDER(152, 181, 201),
	
	// color for presenting
	
	GREENBORDER(48, 45, 206),
	REDBORDER(113, 43, 17),
	
	
	// common color
	GREEN(148, 254, 0),
	RED(231, 10, 107),
	GRAY(102, 153, 153),
	DARKPURPLE(131, 1, 140),
	
	// color for warship
	ORANGE(213, 144, 37);
	
	private final int R;
	private final int G;
	private final int B;
	
	ColorCollection(int R, int G, int B) {
		this.R = R;
		this.G = G;
		this.B = B;
	}
	
	public Color getRGBColor() {
		return Color.rgb(R, G, B);
	}
	
}
