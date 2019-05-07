package sk.hor1zon.javago.game;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

// TODO - validation
public class Settings {
	public final static String DEFAULT_PATH = "settings.json";
	public GameType type;
	public InetAddress ip;
	public int port;
	public String player1;
	public String player2;
	public int board;
	public int handicap;
	public double komi;
	public int byoyomi;
	public String playerColor;
	public String rules;

	public Settings() {
		useDefaults();
	}

	public void useDefaults() {
		type = GameType.LOCAL;
		try {
			ip = InetAddress.getByName(null);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		port = 30303;
		player1 = "";
		player2 = "";
		board = 19;
		handicap = 0;
		komi = 6.5;
		byoyomi = 0;
		playerColor = "black";
		rules = "japanese";
	}

	public void useDefaults(String var) {
		switch (var) {
		case "type":
			type = GameType.LOCAL;
			break;
		case "port":
			port = 30303;
			break;
		case "player1":
			player1 = "";
			break;
		case "player2":
			player2 = "";
			break;
		case "board":
			board = 19;
			break;
		case "handicap":
			handicap = 0;
			break;
		case "komi":
			komi = 6.5;
			break;
		case "byoyomi":
			byoyomi = 0;
			break;
		case "playerColor":
			playerColor = "black";
			break;
		case "rules":
			rules = "japanese";
			break;
		case "ip":
			try {
				ip = InetAddress.getByName(null);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			break;
		}
	}

	public static Settings loadSettings(String path) {
		// Settings loader
		ObjectMapper objectMapper = new ObjectMapper();
		Settings settings = new Settings();
		try {
			settings = objectMapper.readValue(new File(path), Settings.class);
			System.out.println("Settings loaded from: " + path);
		} catch (Exception e) {
			System.err.println("Cannot read settings, using defaults.");
			settings.useDefaults();
		}
		return settings;
	}

	public static Settings loadSettings() {
		return loadSettings(DEFAULT_PATH);
	}

	public boolean writeSettings(String path) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			objectMapper.writeValue(new File(path), this);
		} catch (Exception e) {
			System.err.println("Cannot write settings.");
			return false;
		}
		System.out.println("Settings written as: " + path);
		return true;
	}

	public boolean writeSettings() {
		return writeSettings(DEFAULT_PATH);
	}

	public Map<String, String> toMap() {
		return new HashMap<String, String>(11, 1f) {
			{
				put("type", type.name());
				put("ip", ip.getHostAddress());
				put("port", Integer.toString(port));
				put("player1", player1);
				put("player2", player2);
				put("board", Integer.toString(board));
				put("handicap", Integer.toString(handicap));
				put("komi", Double.toString(komi));
				put("byoyomi", Integer.toString(byoyomi));
				put("playerColor", playerColor);
				put("rules", rules);
			}
		};
	}

	public boolean parseMap(Map<String, String> map) {
		for (Map.Entry<String, String> entry : map.entrySet()) {
			switch (entry.getKey()) {
			case "type":
				type = GameType.valueOf(entry.getValue());
				break;
			case "port":
				try {
					port = Integer.valueOf(entry.getValue());
				} catch (Exception e) {
					useDefaults("port");
				}
				break;
			case "player1":
				player1 = entry.getValue();
				break;
			case "player2":
				player2 = entry.getValue();
				break;
			case "board":
				board = Integer.valueOf(entry.getValue());
				break;
			case "handicap":
				handicap = Integer.valueOf(entry.getValue());
				break;
			case "komi":
				komi = Double.valueOf(entry.getValue());
				break;
			case "byoyomi":
				try {
					byoyomi = Integer.valueOf(entry.getValue());
				} catch (Exception e) {
					useDefaults("byoyomi");
				}
				break;
			case "playerColor":
				playerColor = entry.getValue();
				break;
			case "rules":
				rules = entry.getValue();
				break;
			case "ip":
				try {
					ip = InetAddress.getByName(entry.getValue());
				} catch (Exception e) {
					useDefaults("ip");
				}
				break;
			default:
				System.err.println("Cannot parse provided Map due to invalid Map entry, using defaults.");
				useDefaults();
				return false;
			}
		}
		return true;
	}
}
