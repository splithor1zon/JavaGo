package sk.hor1zon.javago;

import java.util.HashMap;
import java.util.Map;

// TODO - validation
public class Settings {
	public String gameType;
	public int port;
	public int board;
	public int handicap;
	public double komi;
	public int byoyomi;
	public String defaultColor;
	public String rules;

	public Settings() {
	}
	
	public void useDefaults() {
		gameType = "local";
		port = 30303;
		board = 19;
		handicap = 0;
		komi = 6.5;
		byoyomi = 0;
		defaultColor = "black";
		rules = "japanese";
	}
}
