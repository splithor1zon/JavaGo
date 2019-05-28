package sk.hor1zon.javago.game;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import sk.hor1zon.javago.utils.Settings;

/**
 * Provides data structure for stone placements. Game can be recreated from it.
 * 
 * @author splithor1zon
 *
 */
public class History {
	private transient static History ref = null;
	private List<Stone> active;
	private List<Stone> prison;
	private double whitePlayerScore = 0.0;
	private double blackPlayerScore = 0.0;
	private int winner;
	private boolean finalFlag;

	private History() {
		active = new ArrayList<Stone>();
		prison = new ArrayList<Stone>();
	}

	/**
	 * Get reference to curreently used history. If there is none, then creates one.
	 * 
	 * @return Current history reference.
	 */
	public static History getRef() {
		if (ref == null) {
			synchronized (History.class) {
				if (ref == null) {
					ref = new History();
				}
			}
		}
		return ref;
	}

	/**
	 * Get all stones (active, prisoners) in one list sorted by id.
	 * 
	 * @return Sorted list.
	 */
	public ArrayList<Stone> getAllStones() {
		ArrayList<Stone> result = new ArrayList<Stone>();
		for (Stone stone : active) {
			if (stone != null) {
				result.add(stone);
			}
		}
		for (Stone stone : prison) {
			if (stone != null) {
				result.add(stone);
			}
		}
		result.sort(new Comparator<Stone>() {
			@Override
			public int compare(Stone o1, Stone o2) {
				int ret = 0;
				if (o1.getId() > o2.getId()) {
					ret = 1;
				} else if (o1.getId() < o2.getId()) {
					ret = -1;
				}
				return ret;
			}
		});
		return result;

	}

	/**
	 * Counts all placed stones.
	 * 
	 * @return Count of stones.
	 */
	public int getStoneCount() {
		int result = 0;
		for (Stone stone : active) {
			if (stone != null) {
				result++;
			}
		}
		for (Stone stone : prison) {
			if (stone != null) {
				result++;
			}
		}
		return result;
	}

	/**
	 * Counts active stones only of specified color.
	 * 
	 * @param color Color of stones to count.
	 * @return Count of active stones with specified color.
	 */
	public int getActiveStoneCount(StoneColor color) {
		int result = 0;
		for (Stone stone : active) {
			if (stone != null && stone.getColor() == color) {
				result++;
			}
		}
		return result;
	}

	/**
	 * Counts prisoner stones of specified color.
	 * 
	 * @param color Color of stones to count.
	 * @return Count of prisoner stones with specified color.
	 */
	public int getPrisonerCount(StoneColor color) {
		int result = 0;
		for (Stone stone : prison) {
			if (stone != null && stone.getColor() == color) {
				result++;
			}
		}
		return result;
	}

	/**
	 * Move specified stone to prison if on active list.
	 * 
	 * @param stone Stone to remove.
	 */
	public void moveStoneToPrison(Stone stone) {
		try {
			active.remove(stone);
		} catch (Exception e) {
			return;
		}
		if (stone == null) {
			return;
		}
		stone.toPrison();
		prison.add(stone);
	}

	/**
	 * Move list of stones to prison.
	 * 
	 * @param remove List of stones to remove.
	 */
	public void moveStonesToPrison(ArrayList<Stone> remove) {
		for (Stone stone : remove) {
			moveStoneToPrison(stone);
		}
	}

	/**
	 * Add stone to active list.
	 * 
	 * @param stone Stone to add.
	 * @return true if successfully added.
	 */
	public boolean addStone(Stone stone) {
		if (!active.contains(stone) && !prison.contains(stone)) {
			active.add(stone);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Get latest active stone.
	 * 
	 * @return Latest active stone.
	 */
	public Stone getLatest() {
		int size = active.size();
		if (size == 0) {
			return null;
		}
		Stone latest = null;
		for (int i = size - 1; i >= 0; i--) {
			latest = active.get(i);
			if (latest != null) {
				break;
			}
		}
		return latest;
	}

	/**
	 * Return latest stone of specified color.
	 * 
	 * @param color Latest stone of which color to return.
	 * @return Latest color stone.
	 */
	public Stone getLatest(StoneColor color) {
		int size = active.size();
		if (size == 0) {
			return null;
		}
		Stone latest = null;
		for (int i = size - 1; i >= 0; i--) {
			latest = active.get(i);
			if (latest != null && latest.getColor() == color) {
				break;
			}
		}
		return latest;
	}

	/**
	 * Get latest prisoner stone.
	 * 
	 * @return Latest prisoner stone.
	 */
	public Stone getLatestPrisoner() {
		int size = prison.size();
		if (size == 0) {
			return null;
		}
		return prison.get(size - 1);
	}

	/**
	 * Checks if the stone is in prison.
	 * 
	 * @param stone Stone to check.
	 * @return true if is in prison.
	 */
	public boolean isPrisoner(Stone stone) {
		for (Stone s : prison) {
			if (stone.similar(s)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Finalizes the history.
	 * 
	 * @param whitePlayerScore Achieved score of white player.
	 * @param blackPlayerScore Achieved score of black player.
	 */
	public void finalize(double whitePlayerScore, double blackPlayerScore) {
		this.whitePlayerScore = whitePlayerScore;
		this.blackPlayerScore = blackPlayerScore;
		this.finalFlag = true;
		if (whitePlayerScore > blackPlayerScore) {
			this.winner = Settings.currentRef.playerColor == "white" ? 1 : 2;
		} else if (whitePlayerScore < blackPlayerScore) {
			this.winner = Settings.currentRef.playerColor == "white" ? 2 : 1;
		} else {
			this.winner = 0;
		}
	}

	public int getWinner() {
		return winner;
	}

	public boolean isFinal() {
		return finalFlag;
	}

	/**
	 * Get the score for specified stone color.
	 * 
	 * @param color Color of stones to get the score to.
	 * @return Score.
	 */
	public double getScore(StoneColor color) {
		if (color == StoneColor.BLACK) {
			return blackPlayerScore;
		} else {
			return whitePlayerScore;
		}
	}

	/**
	 * Add pass move.
	 */
	public void addPass() {
		active.add(null);
	}

	/**
	 * Set history reference to provided one.
	 * 
	 * @param history History to set default reference to.
	 */
	public static void setHistory(History history) {
		ref = history;
	}

	/**
	 * Set reference of history to null.
	 */
	public static void resetHistory() {
		ref = null;
	}
}
