package sk.hor1zon.javago.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Timer;

import sk.hor1zon.javago.game.GameAlert;
import sk.hor1zon.javago.game.History;
import sk.hor1zon.javago.game.Stone;
import sk.hor1zon.javago.game.StoneColor;
import sk.hor1zon.javago.game.ViewAction;
import sk.hor1zon.javago.utils.Settings;

public class ReplayModel extends Observable {
	private History history;
	public static int gameTime;
	public static StoneColor player1color;
	public static StoneColor player2color;
	public static int player1time;
	public static int player2time;
	private int byoyomiTime;
	private boolean byoyomiOn;
	private int player1prisoners;
	private int player2prisoners;
	private static Timer timer;
	private int player;

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
		byoyomiTime = Settings.currentRef.byoyomi;
		byoyomiOn = Settings.currentRef.byoyomi != 0;
		player1prisoners = history.getPrisonerCount(player1color);
		player2prisoners = history.getPrisonerCount(player2color);
		timer = null;
		player = Settings.currentRef.playerColor == "white" ? 1 : 2;

	}

	public void placeStones(Stone[] toPlace) {
		ViewAction place = ViewAction.PLACEBATCH;
		place.setContent(toPlace);
		setChanged();
		notifyObservers(place);
	}

	public void finish() {
		setChanged();
		notifyObservers(ViewAction.FINISH);
	}

	public void setTime(int newTime, int player) {
		int[] times = { player, newTime };
		ViewAction timeNotice = ViewAction.UPDATE_TIME;
		timeNotice.setContent(times);
		setChanged();
		notifyObservers(timeNotice);
	}
}
