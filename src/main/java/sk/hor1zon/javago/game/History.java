package sk.hor1zon.javago.game;

import java.util.ArrayList;
import java.util.List;

import sk.hor1zon.javago.utils.LazySingleton;

public class History {
	private static volatile History history = null;
	private List<Stone> active;
	private List<Stone> prison;
	private History() {
		active = new ArrayList<Stone>();
		prison = new ArrayList<Stone>();
	}
	public static History get() {
		if (history == null) {
			synchronized (History.class) {
				if (history == null) {
					history = new History();
				}
			}
		}
		return history;
	}
	public boolean moveStoneToPrison(Stone stone, int timestamp) {
		if(!prison.contains(stone) && active.contains(stone)) {
			try {
				active.remove(stone);
			} catch (Exception e) {
				return false;
			}
			stone.toPrison(timestamp);
			prison.add(stone);
			return true;
		} else {
			return false;
		}
	}
	public boolean addStone(Stone stone) {
		if(!active.contains(stone) && !prison.contains(stone)) {
			active.add(stone);
			return true;
		} else {
			return false;
		}
	}
	public void setHistory(ArrayList<Stone> active, ArrayList<Stone> prison) {
		this.active = active;
		this.prison = prison;
	}
	public void initHistory(int prefMaxActiveStones) {
		setHistory(new ArrayList<Stone>(prefMaxActiveStones), new ArrayList<Stone>());
	}
}
