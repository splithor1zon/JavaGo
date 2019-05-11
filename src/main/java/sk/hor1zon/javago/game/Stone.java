package sk.hor1zon.javago.game;

public class Stone {
	private static int idx;
	public final int id;
	public final StoneColor color;
	public final int x;
	public final int y;
	public final int placeTime;
	private int prisonTime;

	public Stone(StoneColor color, int x, int y, int timestamp) {
		this.color = color;
		this.x = x;
		this.y = y;
		placeTime = timestamp;
		prisonTime = -1;
		this.id = idx++;
	}

	public boolean toPrison(int timestamp) {
		if (prisonTime == -1 && timestamp >= 0) {
			prisonTime = timestamp;
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isPrisoner() {
		return prisonTime != -1;
	}
	public int getPrisonTime() {
		return prisonTime;
	}

	public static void setIdxCounter(int idxSet) {
		idx = idxSet;
	}
	public static void resetIdxCounter() {
		idx = 0;
	}
}
