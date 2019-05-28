package sk.hor1zon.javago.game;

import java.util.ArrayList;

import sk.hor1zon.javago.models.ReplayModel;
import sk.hor1zon.javago.views.ReplayView;

/**
 * Lightweight Controller for supporting Replay functionality.
 * 
 * @author splithor1zon
 *
 */
public class ReplayController {
	private ReplayModel model;
	/**
	 * Index of last stone placed.
	 */
	public final int LAST_STONE_IDX;
	private int cursor;
	private ArrayList<Stone> allStones;

	/**
	 * Crates and initializes ReplayController.
	 * 
	 * @param rm Reference to ReplayModel.
	 */
	public ReplayController(ReplayModel rm) {
		model = rm;
		allStones = History.getRef().getAllStones();
		LAST_STONE_IDX = allStones.size() - 1;
		cursor = 0;
	}

	/**
	 * Receive notification of View readiness.
	 * 
	 * @param gv GameView sending the notification.
	 */
	public void viewReady(ReplayView gv) {
		model.addObserver(gv);
		setCursor(cursor);
	}

	/**
	 * View next stone.
	 */
	public void nextStone() {
		if (cursor < LAST_STONE_IDX) {
			setCursor(cursor + 1);
		} else {
			model.finish();
			;
		}

	}

	/**
	 * View previous stone.
	 */
	public void previousStone() {
		if (cursor > 0) {
			setCursor(cursor - 1);
		}
	}

	/**
	 * Set stone cursor to specified position.
	 * 
	 * @param set Cursor position.
	 */
	public void setCursor(int set) {
		this.cursor = set;
		model.placeStones(allStones.subList(0, set + 1).toArray(new Stone[0]));
	}

	/**
	 * Get current position of cursor.
	 * 
	 * @return Position of cursor.
	 */
	public int getCursor() {
		return cursor;
	}
}
