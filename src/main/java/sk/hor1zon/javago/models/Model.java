package sk.hor1zon.javago.models;

import java.util.ArrayList;
import sk.hor1zon.javago.game.GameAlert;
import sk.hor1zon.javago.game.Stone;

/**
 * Provides interface for Controller to Model communication.
 * @author splithor1zon
 *
 */
public interface Model {

	/**
	 * Show specified alert.
	 * @param gameAlert Type of alert to show.
	 */
	void alert(GameAlert gameAlert);
	
	/**
	 * Place single stone on the board.
	 * @param stone Stone to place.
	 */
	void placeStone(Stone stone);
	
	/**
	 * Place single stone on the board with regards to Ko rule.
	 * @param stone Stone to place.
	 */
	void placeStoneKo(Stone stone);
	
	/**
	 * Account for prisoners in history.
	 * @param removed Stones to be captured as prisoners.
	 */
	void updatePrisoners(ArrayList<Stone> removed);
	
	/**
	 * Player pass.
	 */
	void pass();
	
	/**
	 * Start of negotiation of final results.
	 */
	void negotiate();
	
	/**
	 * Finalize the history and show congratulations dialog.
	 * @param player1score Score of player 1.
	 * @param player2score Score of player 2.
	 */
	void finish(double player1score, double player2score);
	
	/**
	 * Set the time of specified player.
	 * @param newTime The time to set.
	 * @param player For which player.
	 */
	void setTime(int newTime, int player);
	
	/**
	 * Shows the time-up dialog and passes.
	 */
	void timeUp();
	
	/**
	 * Changes the active player to player specified.
	 * @param player Player to set active.
	 */
	void changePlayer(int player);
	
	/**
	 * Start the timer.
	 */
	void startTimer();
	
	/**
	 * Stop the timer and prepare for abandoning the model.
	 */
	static void shutdown() {}
	
	/**
	 * Set the stones of handicap.
	 */
	void setHandicap();
}
