package sk.hor1zon.javago.models;

import java.util.Observable;
import java.util.Observer;

import sk.hor1zon.javago.Menu;
import sk.hor1zon.javago.utils.Settings;

public class InitModel extends Observable {
	private boolean isNew;
	private Settings settings;
	public InitModel(boolean isNew) {
		this.isNew = isNew;		
	}
	public Settings getSettings() {
		return settings;
	}
	public void setSettings(Settings settings) {
		this.settings = settings;
	}
	public void publishChanges() {
		setChanged();
		notifyObservers(isNew);
	}
}
