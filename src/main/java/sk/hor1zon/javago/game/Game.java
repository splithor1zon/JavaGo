package sk.hor1zon.javago.game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;

import sk.hor1zon.javago.game.boards.BoardCanvas;
import sk.hor1zon.javago.utils.Settings;

/**
 * Main game data structure, used for saving games to file or read from file.
 * @author splithor1zon
 *
 */
public class Game {
	
	/**
	 * Stores settings of the game.
	 */
	public Settings settings;
	
	/**
	 * Stores the whole history of the game.
	 */
	public History history;
	
	/**
	 * Stores the stone placement of the game.
	 */
	public Stone[][] board;
	
	/**
	 * Stores whether the game was finished
	 */
	public boolean isFinal;
	private static Logger l = Logger.getLogger(LocalController.class.getName());

	public Game() {
		settings = Settings.currentRef;
		history = History.getRef();
		board = BoardCanvas.getStoneGrid();
		isFinal = history.isFinal();
	}

	/**
	 * Writes the current instance to provided file.
	 * @param file Output file.
	 * @return Returns true if the file was successfully written.
	 */
	public boolean write(File file) {
		String serializedGame = new Gson().toJson(this);
		try {
			FileOutputStream outStream = new FileOutputStream(file, false);
			OutputStreamWriter outWriter = new OutputStreamWriter(outStream);
			outWriter.write(serializedGame);
			outWriter.close();
			outStream.close();
		} catch (Exception e) {
			l.log(Level.WARNING, "Cannot write game.");
			return false;
		}
		l.log(Level.INFO, "Game written as: " + file.getPath());
		return true;
	}
 
	/**
	 * Static function.
	 * Loads game from file provided.
	 * @param file Saave game file.
	 * @return Retuns loaded Game data structure.
	 */
	public static Game load(File file) {
		Game game = new Game();
		String json = "";
		try {
			FileInputStream inStream = new FileInputStream(file);
			BufferedReader inReader = new BufferedReader(new InputStreamReader(inStream));
			String dataRow = "";
			while ((dataRow = inReader.readLine()) != null) {
				json += dataRow;
			}
			inReader.close();
			game = new Gson().fromJson(json, Game.class);
		} catch (Exception e) {
			l.log(Level.WARNING, "Cannot read game.");
			return null;
		}
		l.log(Level.INFO, "Game loaded from: " + file.getPath());
		return game;

	}
}
