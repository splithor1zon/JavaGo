package sk.hor1zon.javago.game;

import java.io.File;
import java.util.ArrayList;

import sk.hor1zon.javago.views.GameView;

/**
 * Provides interface for View to send events to controller.
 * @author splithor1zon
 *
 */
public interface ControllerIntf {

	/**
	 * Provides functionality to check the rules when user wants to place the stone.
	 * Alerts user if he broke the rules.
	 * @param x Parsed X coordinate for placing the stone.
	 * @param y Parsed Y coordinate for placing the stone.
	 */
	void placeStone(int x, int y);
	
	/**
	 * Provides functionality for when user wants to pass.
	 * Checks if pass was also invoked in previous turn, if so, then it starts negotiation.
	 */
	void pass();
	
	/**
	 * Receives the results of negotiation and ends the game.
	 * @param conclusion If the players came into conclusion.
	 * @param player1score What was the Player 1 score decided.
	 * @param player2score What was the Player 2 score decided.
	 */
	void resultNegotiation(boolean conclusion, double player1score, double player2score);
	
	/**
	 * Notices the model of removed stones.
	 * @param removed List of Stones removed.
	 */
	void removedNotice(ArrayList<Stone> removed);
	
	/**
	 * Initializes the model and controller when the view is ready.
	 * @param gv Reference to GameView.
	 */
	void viewReady(GameView gv);
	
	/**
	 * Saves the current Game.
	 * @param file Output file of the game.
	 */
	void saveGame(File file);
}
