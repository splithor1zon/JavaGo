package sk.hor1zon.javago.game;

import javafx.scene.paint.Color;

public enum StoneColor {
	BLACK(Color.BLACK), WHITE(Color.WHITE);
	
	private final Color color;
	
	StoneColor(Color color) {
		this.color = color;
	}
	
	public Color value() {
		return this.color;
	}
}
