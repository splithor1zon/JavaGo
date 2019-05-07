package sk.hor1zon.javago.game;

import java.util.Observable;
import java.util.Observer;

import sk.hor1zon.javago.GameStatus;
import sk.hor1zon.javago.Menu;

public class GameModel extends Observable {
	private GameStatus status;
	private Settings settings;
	public GameModel(GameStatus status) {
		this.status = status;		
	}
	public Settings getSettings() {
		return settings;
	}
	public void setSettings(Settings settings) {
		this.settings = settings;
		setChanged();
		notifyObservers(status);
	}
}
