package sk.hor1zon.javago.game;

import sk.hor1zon.javago.models.GameModel;

/**
 * Data structure of individual stones.
 * @author splithor1zon
 *
 */
public class Stone {
	private static int idx;
	private final int id;
	private final StoneColor color;
	private final int x;
	private final int y;
	private final int placeTime;
	private int prisonTime;

	public Stone(StoneColor color, int x, int y) {
		this.color = color;
		this.x = x;
		this.y = y;
		placeTime = GameModel.gameTime;
		prisonTime = -1;
		this.id = idx++;
	}

	/**
	 * When stone is moved to prison, this function updates its prison time.
	 * @return Returns false if already set.
	 */
	public boolean toPrison() {
		if (prisonTime == -1) {
			prisonTime = GameModel.gameTime;
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isPrisoner() {
		return prisonTime != -1;
	}
	
	public int getId() {
		return id;
	}

	public StoneColor getColor() {
		return color;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getPlaceTime() {
		return placeTime;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + id;
		result = prime * result + placeTime;
		result = prime * result + prisonTime;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Stone other = (Stone) obj;
		if (color != other.color)
			return false;
		if (id != other.id)
			return false;
		if (placeTime != other.placeTime)
			return false;
		if (prisonTime != other.prisonTime)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
	public boolean similar(Stone s) {
		if (this == s)
			return true;
		if (s == null)
			return false;
		if (color != s.color)
			return false;
		if (x != s.x)
			return false;
		if (y != s.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Stone [id=" + id + ", color=" + color + ", x=" + x + ", y=" + y + ", placeTime=" + placeTime
				+ ", prisonTime=" + prisonTime + "]";
	}
}
