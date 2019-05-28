package sk.hor1zon.javago.game;

import java.util.ArrayList;

import sk.hor1zon.javago.models.ReplayModel;
import sk.hor1zon.javago.views.ReplayView;

public class ReplayController {
	private ReplayModel model;
	public final int LAST_STONE_IDX;
	private int cursor;
	private ArrayList<Stone> allStones;
	
	public ReplayController(ReplayModel rm) {
		model = rm;
		allStones = History.getRef().getAllStones();
		LAST_STONE_IDX = allStones.size()-1;
		cursor = 0;
	}
	public void viewReady(ReplayView gv) {
		model.addObserver(gv);
		setCursor(cursor);
	}
	
	public void nextStone() {
		if (cursor < LAST_STONE_IDX) {
			setCursor(cursor+1);
		} else {
			model.finish();;
		}
		
	}
	public void previousStone() {
		if (cursor > 0) {
			setCursor(cursor-1);
		}
	}
	public void setCursor(int set) {
		this.cursor = set;
		model.placeStones(allStones.subList(0, set+1).toArray(new Stone[0]));
	}
	public int getCursor() {
		return cursor;
	}
	public void removedNotice(ArrayList<Stone> toRemove) {
		
	}
}
