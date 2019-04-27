package sk.hor1zon.javago.game;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

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
	
	public Settings(String filename) {
		// Settings loader
		ObjectMapper objectMapper = new ObjectMapper();
		Settings settings = new Settings();
		try {
			settings = objectMapper.readValue(new File(filename), Settings.class);
			System.out.println("Settings loaded from: " + filename);
		} catch (Exception e) {
			System.err.println("Cannot read settings, using defaults.");
			settings.useDefaults();
		}
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
