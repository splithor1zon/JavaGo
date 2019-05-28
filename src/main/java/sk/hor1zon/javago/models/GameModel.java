package sk.hor1zon.javago.models;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;
import sk.hor1zon.javago.game.GameAlert;
import sk.hor1zon.javago.game.GameType;
import sk.hor1zon.javago.game.History;
import sk.hor1zon.javago.game.Stone;
import sk.hor1zon.javago.game.StoneColor;
import sk.hor1zon.javago.game.ViewAction;
import sk.hor1zon.javago.utils.Settings;

public class GameModel extends Observable implements Model {
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

	public GameModel() {
		history = History.getRef();
		gameTime = 0;
		player1color = Settings.currentRef.playerColor == "white" ? StoneColor.WHITE : StoneColor.BLACK;
		player2color = Settings.currentRef.playerColor == "white" ? StoneColor.BLACK : StoneColor.WHITE;
		player1time = 0;
		player2time = 0;
		byoyomiTime = Settings.currentRef.byoyomi;
		byoyomiOn = Settings.currentRef.byoyomi != 0;
		player1prisoners = history.getPrisonerCount(player2color);
		player2prisoners = history.getPrisonerCount(player1color);
		timer = null;
		player = Settings.currentRef.type != GameType.LOCAL
				? (Settings.currentRef.playerColor == "white" ? (Settings.currentRef.type == GameType.SERVER ? 1 : 2)
						: (Settings.currentRef.type == GameType.SERVER ? 2 : 1))
				: Settings.currentRef.playerColor == "white" ? 1 : 2;
	}

	public void alert(GameAlert gameAlert) {
		ViewAction alert = ViewAction.ALERT;
		alert.setContent(gameAlert);
		setChanged();
		notifyObservers(alert);
	}

	public void placeStone(Stone stone) {
		history.addStone(stone);
		ViewAction place = ViewAction.PLACE;
		place.setContent(stone);
		setChanged();
		notifyObservers(place);
	}

	public void placeStoneKo(Stone stone) {
		history.addStone(stone);
		ViewAction placeko = ViewAction.PLACEKO;
		placeko.setContent(stone);
		setChanged();
		notifyObservers(placeko);
	}

	public void updatePrisoners(ArrayList<Stone> removed) {
		history.moveStonesToPrison(removed);
		int[] prisoners;
		if (removed.size() > 0) {
			if (Settings.currentRef.playerColor
					.equals(removed.get(0).getColor() == StoneColor.BLACK ? "black" : "white")) {
				player2prisoners += removed.size();
				prisoners = new int[] { 2, player2prisoners };
			} else {
				player1prisoners += removed.size();
				prisoners = new int[] { 1, player1prisoners };
			}
			ViewAction toPrison = ViewAction.UPDATE_PRISONERS;
			toPrison.setContent(prisoners);
			setChanged();
			notifyObservers(toPrison);
		}
	}

	public void pass() {
		history.addPass();
	}

	public void negotiate() {
		setChanged();
		notifyObservers(ViewAction.NEGOTIATE);
	}

	public void finish(double player1score, double player2score) {
		if (player1color == StoneColor.WHITE) {
			history.finalize(player1score, player2score);
		} else {
			history.finalize(player2score, player1score);
		}
		setChanged();
		notifyObservers(ViewAction.FINISH);

	}

	private void timeStep(int player) {
		if (player == 1) {
			setTime(++player1time, player);
		} else if (player == 2) {
			setTime(++player2time, player);
		}
	}

	public void setTime(int newTime, int player) {
		int[] times = { player, newTime, byoyomiTime };
		ViewAction timeNotice = ViewAction.UPDATE_TIME;
		timeNotice.setContent(times);
		setChanged();
		notifyObservers(timeNotice);
	}

	public void timeUp() {
		ViewAction alert = ViewAction.ALERT;
		alert.setContent(GameAlert.TIMEUP);
		setChanged();
		notifyObservers(alert);
	}

	public void changePlayer(int player) {
		this.player = player;
		resetByoyomi();
		ViewAction sw = ViewAction.SWITCH_PLAYER;
		sw.setContent(player);
		setChanged();
		notifyObservers(sw);
	}

	private void resetByoyomi() {
		byoyomiTime = Settings.currentRef.byoyomi;
	}

	public void startTimer() {
		if (timer != null) {
			timer.cancel();
		}
		timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				if (byoyomiOn) {
					if (byoyomiTime > 0) {
						byoyomiTime--;
					} else {
						timeUp();
					}
				}
				gameTime++;
				timeStep(player);
			}
		};
		timer.schedule(task, 0, 1000);
	}

	public static void shutdown() {
		timer.cancel();
		timer.purge();
	}

	public void setHandicap() {
		int handicap = Settings.currentRef.handicap;
		int size = Settings.currentRef.board;
		Stone[] batch = null;
		if (handicap < 2 || handicap > 9) {
			return;
		}
		Stone A, B, C, D, E, F, G, H, I;
		switch (size) {
		case 19:
			A = new Stone(StoneColor.BLACK, 15, 3);
			B = new Stone(StoneColor.BLACK, 3, 15);
			C = new Stone(StoneColor.BLACK, 15, 15);
			D = new Stone(StoneColor.BLACK, 3, 3);
			E = new Stone(StoneColor.BLACK, 9, 9);
			F = new Stone(StoneColor.BLACK, 3, 9);
			G = new Stone(StoneColor.BLACK, 15, 9);
			H = new Stone(StoneColor.BLACK, 9, 3);
			I = new Stone(StoneColor.BLACK, 9, 15);
			switch (handicap) {
			case 9:
				batch = new Stone[] { A, B, C, D, E, F, G, H, I };
				break;
			case 8:
				batch = new Stone[] { A, B, C, D, F, G, H, I };
				break;
			case 7:
				batch = new Stone[] { A, B, C, D, E, F, G };
				break;
			case 6:
				batch = new Stone[] { A, B, C, D, F, G };
				break;
			case 5:
				batch = new Stone[] { A, B, C, D, E };
				break;
			case 4:
				batch = new Stone[] { A, B, C, D };
				break;
			case 3:
				batch = new Stone[] { A, B, C };
				break;
			case 2:
				batch = new Stone[] { A, B };
				break;
			}
			break;
		case 13:
			A = new Stone(StoneColor.BLACK, 9, 3);
			B = new Stone(StoneColor.BLACK, 3, 9);
			C = new Stone(StoneColor.BLACK, 9, 9);
			D = new Stone(StoneColor.BLACK, 3, 3);
			E = new Stone(StoneColor.BLACK, 6, 6);
			switch (handicap) {
			case 5:
				batch = new Stone[] { A, B, C, D, E };
				break;
			case 4:
				batch = new Stone[] { A, B, C, D };
				break;
			case 3:
				batch = new Stone[] { A, B, C };
				break;
			case 2:
				batch = new Stone[] { A, B };
				break;
			}
			break;
		case 9:
			A = new Stone(StoneColor.BLACK, 6, 2);
			B = new Stone(StoneColor.BLACK, 2, 6);
			C = new Stone(StoneColor.BLACK, 6, 6);
			D = new Stone(StoneColor.BLACK, 2, 2);
			E = new Stone(StoneColor.BLACK, 4, 4);
			switch (handicap) {
			case 5:
				batch = new Stone[] { A, B, C, D, E };
				break;
			case 4:
				batch = new Stone[] { A, B, C, D };
				break;
			case 3:
				batch = new Stone[] { A, B, C };
				break;
			case 2:
				batch = new Stone[] { A, B };
				break;
			}
			break;
		default:
			break;
		}
		if (batch == null) {
			return;
		}
		for (Stone stone : batch) {
			history.addStone(stone);
		}

		ViewAction placeb = ViewAction.PLACEBATCH;
		placeb.setContent(batch);
		setChanged();
		notifyObservers(placeb);
	}
}
