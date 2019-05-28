package sk.hor1zon.javago.models;

import java.util.Observable;
import sk.hor1zon.javago.utils.Settings;

/**
 * Provides backbone for Settings menu or loading game from file.
 * 
 * @author splithor1zon
 *
 */
public class InitModel extends Observable {
	private boolean isNew;
	private Settings settings;

	/**
	 * Create new InitModel.
	 * 
	 * @param isNew Is new game desired or load game.
	 */
	public InitModel(boolean isNew) {
		this.isNew = isNew;
	}

	/**
	 * Get currently used settings.
	 * 
	 * @return Currently used settings.
	 */
	public Settings getSettings() {
		return settings;
	}

	/**
	 * Sets settings to provided settings.
	 * 
	 * @param settings Settings to set to.
	 */
	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	/**
	 * Notify Menu of changes.
	 */
	public void publishChanges() {
		setChanged();
		notifyObservers(isNew);
	}
}
