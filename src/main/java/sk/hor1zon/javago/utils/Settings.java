package sk.hor1zon.javago.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;

import sk.hor1zon.javago.game.GameType;

/**
 * Settings data storage.
 * 
 * @author splithor1zon
 *
 */
public class Settings {
	public final static String DEFAULT_PATH = "settings.json";
	public GameType type;
	public InetAddress ip; //
	public int port;
	public String player1;
	public String player2; //
	public int board;
	public int handicap;
	public double komi;
	public int byoyomi;
	public String playerColor;
	public static Settings currentRef;
	private static Logger l = Logger.getLogger(Settings.class.getName());

	public Settings() {
		useDefaults();
		currentRef = this;
	}

	/**
	 * Set public static settings reference to provided settings.
	 * 
	 * @param settings Settings to set to.
	 */
	public static void setSettings(Settings settings) {
		currentRef = settings;
	}

	/**
	 * Use default settings.
	 */
	public void useDefaults() {
		type = GameType.LOCAL;
		try {
			ip = InetAddress.getByName(null);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		port = 30303;
		player1 = "Alice";
		player2 = "Bob";
		board = 19;
		handicap = 0;
		komi = 6.5;
		byoyomi = 0;
		playerColor = "black";
	}

	/**
	 * Use specified default setting.
	 * 
	 * @param var Setting to set to default.
	 */
	public void useDefaults(String var) {
		switch (var) {
		case "type":
			type = GameType.LOCAL;
			break;
		case "port":
			port = 30303;
			break;
		case "player1":
			player1 = "Alice";
			break;
		case "player2":
			player2 = "Bob";
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
		case "ip":
			try {
				ip = InetAddress.getByName(null);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			break;
		}
	}

	/**
	 * Loads settings from specified file.
	 * 
	 * @param path Path to settings file location.
	 * @return Returns loaded settings.
	 */
	public static Settings load(String path) {
		// Settings loader
		String json = "";
		Settings settings = new Settings();
		try {
			FileInputStream inStream = new FileInputStream(path);
			BufferedReader inReader = new BufferedReader(new InputStreamReader(inStream));
			String dataRow = "";
			while ((dataRow = inReader.readLine()) != null) {
				json += dataRow;
			}
			inReader.close();

			Gson gson = new Gson();
			settings = gson.fromJson(json, Settings.class);
		} catch (Exception e) {
			l.log(Level.WARNING, "Cannot read settings, using defaults.");
			settings.useDefaults();
		}

		l.log(Level.INFO, "Settings loaded from: " + path);
		return settings;
	}

	/**
	 * Loads settings from default location.
	 * 
	 * @return Returns loaded settings.
	 */
	public static Settings load() {
		return load(DEFAULT_PATH);
	}

	/**
	 * Writes current setting into specified file.
	 * 
	 * @param path Where to write settings to.
	 * @return Returns true if written successfully
	 */
	public boolean write(String path) {
		String serializedSettings = new Gson().toJson(this);
		try {
			FileOutputStream outStream = new FileOutputStream(path, false);
			OutputStreamWriter outWriter = new OutputStreamWriter(outStream);
			outWriter.write(serializedSettings);
			outWriter.close();
			outStream.close();
		} catch (Exception e) {
			l.log(Level.WARNING, "Cannot write settings.");
			return false;
		}
		l.log(Level.INFO, "Settings written as: " + path);
		return true;
	}

	/**
	 * Writes current setting into default file.
	 * 
	 * @return Returns true if written successfully
	 */
	public boolean write() {
		return write(DEFAULT_PATH);
	}

	/**
	 * Converts settings into map.
	 * 
	 * @return Returns settings representation in map.
	 */
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
			}
		};
	}

	/**
	 * Loads settings from map.
	 * 
	 * @param map Map in correct settings format.
	 * @return Returns true if parsed successfully.
	 */
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
				if (playerColor == "nigiri") {
					Random random = new Random();
					playerColor = random.nextBoolean() ? "black" : "white";
				}
				break;
			case "ip":
				try {
					ip = InetAddress.getByName(entry.getValue());
				} catch (Exception e) {
					useDefaults("ip");
				}
				break;
			default:
				l.log(Level.WARNING, "Cannot parse provided Map due to invalid Map entry, using defaults.");
				useDefaults();
				return false;
			}
		}
		return true;
	}
}
