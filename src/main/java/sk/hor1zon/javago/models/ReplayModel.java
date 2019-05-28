package sk.hor1zon.javago.models;

import java.util.Observable;
import sk.hor1zon.javago.game.History;
import sk.hor1zon.javago.game.Stone;
import sk.hor1zon.javago.game.StoneColor;
import sk.hor1zon.javago.game.ViewAction;
import sk.hor1zon.javago.utils.Settings;

/**
 * Creates simple model used for replaying games.
 * @author splithor1zon
 *
 */
public class ReplayModel extends Observable {
	private History history;
	/**
	 * Current game time.
	 */
	public static int gameTime;
	/**
	 * Color of player 1.
	 */
	public static StoneColor player1color;
	/**
	 * Color of player 2.
	 */
	public static StoneColor player2color;
	/**
	 * Time of player 1.
	 */
	public static int player1time;
	/**
	 * Time of player 2.
	 */
	public static int player2time;
	
	/**
	 * Create and initialize the ReplayModel.
	 */
	public ReplayModel() {
		history = History.getRef();
		Stone latest = history.getLatest();
		gameTime = latest == null ? 0 : latest.getPlaceTime();
		player1color = Settings.currentRef.playerColor == "white" ? StoneColor.WHITE : StoneColor.BLACK;
		player2color = Settings.currentRef.playerColor == "white" ? StoneColor.BLACK : StoneColor.WHITE;
		Stone latest1 = history.getLatest(player1color);
		Stone latest2 = history.getLatest(player2color);
		player1time = latest1 == null ? 0 : latest.getPlaceTime();
		player2time = latest2 == null ? 0 : latest.getPlaceTime();
		history.getPrisonerCount(player1color);
		history.getPrisonerCount(player2color);

	}

	/**
	 * Place batch of Stones at once.
	 * @param toPlace Array of Stones to place.
	 */
	public void placeStones(Stone[] toPlace) {
		ViewAction place = ViewAction.PLACEBATCH;
		place.setContent(toPlace);
		setChanged();
		notifyObservers(place);
	}

	/**
	 * Show finish dialog.
	 */
	public void finish() {
		setChanged();
		notifyObservers(ViewAction.FINISH);
	}

	/**
	 * Set the time of specified player.
	 * @param newTime The time to set.
	 * @param player For which player.
	 */
	public void setTime(int newTime, int player) {
		int[] times = { player, newTime };
		ViewAction timeNotice = ViewAction.UPDATE_TIME;
		timeNotice.setContent(times);
		setChanged();
		notifyObservers(timeNotice);
	}
}
