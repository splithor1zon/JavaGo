package sk.hor1zon.javago.models;

import java.util.ArrayList;
import javafx.beans.Observable;
import sk.hor1zon.javago.game.GameAlert;
import sk.hor1zon.javago.game.Stone;

public interface Model {

	void alert(GameAlert gameAlert);
	
	void placeStone(Stone stone);
	
	void placeStoneKo(Stone stone);
	
	void updatePrisoners(ArrayList<Stone> removed);
	
	void pass();
	
	void negotiate();
	
	void finish(double player1score, double player2score);
	
	void setTime(int newTime, int player);
	
	void timeUp();
	
	void changePlayer(int player);
	
	void startTimer();
	
	static void shutdown() {}
	
	void setHandicap();
}
