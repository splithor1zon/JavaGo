package sk.hor1zon.javago.game;

import sk.hor1zon.javago.game.boards.Board;
import sk.hor1zon.javago.game.boards.Board19;
import sk.hor1zon.javago.utils.Settings;

public class Game {
	private Settings settings;
	private Board board;
	private History history;
	public Game() {
		settings = new Settings();
		board = new Board19();
		history = History.get();
	}
}
