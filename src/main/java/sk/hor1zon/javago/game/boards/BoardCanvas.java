package sk.hor1zon.javago.game.boards;

import java.util.ArrayList;
import java.util.Collection;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sk.hor1zon.javago.game.History;
import sk.hor1zon.javago.game.Stone;
import sk.hor1zon.javago.game.StoneColor;
import sk.hor1zon.javago.utils.Settings;

/**
 * Used for drawing the board and statically provide methods for checking
 * various rules of Go.
 * 
 * @author splithor1zon
 *
 */
public class BoardCanvas extends Canvas {
	private static int SIZE;
	private final int SPACING_COUNT;

	/**
	 * Minimal dimension of Go board in pixels.
	 */
	public final int MIN_DIMENSION = 560;
	private final int MIN_SPACING;
	private GraphicsContext gc;

	/**
	 * Reference to currently used BoardCanvas.
	 */
	public static BoardCanvas currRef;

	private static int spacing;
	private static Stone[][] stoneGrid;

	/**
	 * Create new instance of BoardCanvas with specified board size.
	 * 
	 * @param size Board size to create.
	 */
	public BoardCanvas(int size) {
		SIZE = size;
		SPACING_COUNT = SIZE + 1;
		MIN_SPACING = MIN_DIMENSION / SPACING_COUNT;
		widthProperty().addListener(evt -> scaling());
		heightProperty().addListener(evt -> scaling());
		stoneGrid = new Stone[SIZE][SIZE];
		currRef = this;
	}

	/**
	 * Create new instance of BoardCanvas with stone placement predefined.
	 * 
	 * @param size  Board size to create.
	 * @param sGrid 2D array of Stones to apply.
	 */
	public BoardCanvas(int size, Stone[][] sGrid) {
		SIZE = size;
		SPACING_COUNT = SIZE + 1;
		MIN_SPACING = MIN_DIMENSION / SPACING_COUNT;
		widthProperty().addListener(evt -> scaling());
		heightProperty().addListener(evt -> scaling());
		stoneGrid = sGrid;
		currRef = this;
	}

	private void scaling() {
		double width = getWidth();
		double height = getHeight();

		if (width > height)
			width = height;
		else
			height = width;

		// Zachovanie párnosti pixelov medzi čiarami
		int tmpspacing = (int) width / SPACING_COUNT;
		tmpspacing = tmpspacing - (tmpspacing % 2);
		tmpspacing = tmpspacing >= MIN_SPACING ? tmpspacing : MIN_SPACING;

		if (tmpspacing != spacing || tmpspacing == MIN_SPACING) {
			spacing = tmpspacing;
			drawBoard();
			drawStones();
		}
	}

	private void drawBoard() {
		int maxBoardCoord = spacing * SIZE;
		gc = getGraphicsContext2D();

		gc.clearRect(0, 0, getWidth(), getHeight());

		gc.setStroke(Color.BLACK);
		gc.setLineWidth((double) spacing / (double) MIN_SPACING + 0.2);

		for (int x = spacing; x <= maxBoardCoord; x += spacing) {
			gc.strokeLine(x, spacing, x, maxBoardCoord);
		}
		for (int y = spacing; y <= maxBoardCoord; y += spacing) {
			gc.strokeLine(spacing, y, maxBoardCoord, y);
		}

		int diamondDiameter = spacing / 5;
		diamondDiameter = diamondDiameter + (diamondDiameter % 2);
		int diamondRadius = diamondDiameter / 2;
		gc.setFill(Color.BLACK);
		switch (SIZE) {
		case 19:
			gc.fillOval(spacing * 4 - diamondRadius, spacing * 4 - diamondRadius, diamondDiameter, diamondDiameter);
			gc.fillOval(spacing * 10 - diamondRadius, spacing * 4 - diamondRadius, diamondDiameter, diamondDiameter);
			gc.fillOval(spacing * 16 - diamondRadius, spacing * 4 - diamondRadius, diamondDiameter, diamondDiameter);
			gc.fillOval(spacing * 4 - diamondRadius, spacing * 10 - diamondRadius, diamondDiameter, diamondDiameter);
			gc.fillOval(spacing * 10 - diamondRadius, spacing * 10 - diamondRadius, diamondDiameter, diamondDiameter);
			gc.fillOval(spacing * 16 - diamondRadius, spacing * 10 - diamondRadius, diamondDiameter, diamondDiameter);
			gc.fillOval(spacing * 4 - diamondRadius, spacing * 16 - diamondRadius, diamondDiameter, diamondDiameter);
			gc.fillOval(spacing * 10 - diamondRadius, spacing * 16 - diamondRadius, diamondDiameter, diamondDiameter);
			gc.fillOval(spacing * 16 - diamondRadius, spacing * 16 - diamondRadius, diamondDiameter, diamondDiameter);
			break;
		case 13:
			gc.fillOval(spacing * 3 - diamondRadius, spacing * 3 - diamondRadius, diamondDiameter, diamondDiameter);
			gc.fillOval(spacing * 11 - diamondRadius, spacing * 3 - diamondRadius, diamondDiameter, diamondDiameter);
			gc.fillOval(spacing * 3 - diamondRadius, spacing * 11 - diamondRadius, diamondDiameter, diamondDiameter);
			gc.fillOval(spacing * 11 - diamondRadius, spacing * 11 - diamondRadius, diamondDiameter, diamondDiameter);
			gc.fillOval(spacing * 7 - diamondRadius, spacing * 7 - diamondRadius, diamondDiameter, diamondDiameter);
			break;
		case 9:
			gc.fillOval(spacing * 3 - diamondRadius, spacing * 3 - diamondRadius, diamondDiameter, diamondDiameter);
			gc.fillOval(spacing * 7 - diamondRadius, spacing * 3 - diamondRadius, diamondDiameter, diamondDiameter);
			gc.fillOval(spacing * 3 - diamondRadius, spacing * 7 - diamondRadius, diamondDiameter, diamondDiameter);
			gc.fillOval(spacing * 7 - diamondRadius, spacing * 7 - diamondRadius, diamondDiameter, diamondDiameter);
			gc.fillOval(spacing * 5 - diamondRadius, spacing * 5 - diamondRadius, diamondDiameter, diamondDiameter);
			break;
		default:
			break;
		}
	}

	private void drawStones() {
		int stoneDiameter = spacing - 2;
		int stoneRadius = stoneDiameter / 2;
		for (Stone[] x : stoneGrid) {
			for (Stone stone : x) {
				if (stone != null) {
					gc.setFill(stone.getColor().value());
					gc.fillOval(spacing * (stone.getX() + 1) - stoneRadius, spacing * (stone.getY() + 1) - stoneRadius,
							stoneDiameter, stoneDiameter);
				}
			}
		}
	}

	/**
	 * Place Stone on the board by inserting it in the 2D array, also checks for
	 * which stones are to be removed.
	 * 
	 * @param stoneToPlace The stone that is valid to place on board.
	 * @param ko           If there is Ko situation.
	 * @return Returns list of stones, that are being removed.
	 */
	public ArrayList<Stone> placeStone(Stone stoneToPlace, boolean ko) {
		ArrayList<Stone> toRemove = new ArrayList<Stone>();
		stoneGrid[stoneToPlace.getX()][stoneToPlace.getY()] = stoneToPlace;
		for (Stone[] xS : stoneGrid) {
			for (Stone stone : xS) {
				if (stone != null) {
					if (!hasGroupLiberty(stone)) {
						toRemove.add(stone);
					}
				}
			}
		}
		if (ko) {
			toRemove.remove(stoneToPlace);
			toRemove.removeAll(koRemain(stoneToPlace));
		}
		removeStone(toRemove);
		drawBoard();
		drawStones();
		return toRemove;
	}

	/**
	 * Checks for whether there are any other stones to remain on the board after
	 * marking them to remove. Used in Ko situations.
	 * 
	 * @param virtualStone Stone to be placed on board, but after finishing to be
	 *                     replaced with original content.
	 * @return Returns List of stones that should remain on board.
	 */
	public static ArrayList<Stone> koRemain(Stone virtualStone) {
		Stone tmp = stoneGrid[virtualStone.getX()][virtualStone.getY()];
		stoneGrid[virtualStone.getX()][virtualStone.getY()] = virtualStone;
		ArrayList<Stone> result = koRemain(virtualStone.getX(), virtualStone.getY(), new ArrayList<Stone>());
		stoneGrid[virtualStone.getX()][virtualStone.getY()] = tmp;
		return result;
	}

	/**
	 * Recursive algorithm. Checks for whether there are any other stones to remain
	 * on the board after marking them to remove. Used in Ko situations.
	 * 
	 * @param x          X coordinate for desired stone to check.
	 * @param y          Y coordinate for desired stone to check.
	 * @param restricted List of stones that are not to be checked, used in
	 *                   recursion.
	 * @return Returns List of stones that should remain on board.
	 */
	public static ArrayList<Stone> koRemain(int x, int y, ArrayList<Stone> restricted) {
		StoneColor color = stoneGrid[x][y].getColor();
		Stone[] neighbors = getAdjacent(x, y);
		ArrayList<Stone> result = new ArrayList<Stone>();
		for (Stone stone : neighbors) {
			if (stone != null) {
				if (stone.getColor() == color && !restricted.contains(stone)) {
					restricted.add(stone);
					result.add(stone);
					result.addAll(koRemain(stone.getX(), stone.getY(), restricted));
				}
			}
		}
		return result;
	}

	/**
	 * Checks whether the intersection is occupied by another Stone.
	 * 
	 * @param x X coordinate of desired intersection check.
	 * @param y Y coordinate of desired intersection check.
	 * @return true if the intersection is already occupied.
	 */
	public static boolean isOccupied(int x, int y) {
		if (stoneGrid[x][y] != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks whether the stone on provided coordinates has any liberty.
	 * 
	 * @param x X coordinate of Stone to check.
	 * @param y Y coordinate of Stone to check.
	 * @return true - if has at least one liberty.
	 */
	public static boolean hasLiberty(int x, int y) {
		int max = SIZE - 1;
		if (x == max) {
			if (y == max) {
				return stoneGrid[x - 1][y] == null || stoneGrid[x][y - 1] == null;
			} else if (y == 0) {
				return stoneGrid[x - 1][y] == null || stoneGrid[x][y + 1] == null;
			} else {
				return stoneGrid[x - 1][y] == null || stoneGrid[x][y + 1] == null || stoneGrid[x][y - 1] == null;
			}
		} else if (x == 0) {
			if (y == max) {
				return stoneGrid[x + 1][y] == null || stoneGrid[x][y - 1] == null;
			} else if (y == 0) {
				return stoneGrid[x + 1][y] == null || stoneGrid[x][y + 1] == null;
			} else {
				return stoneGrid[x + 1][y] == null || stoneGrid[x][y + 1] == null || stoneGrid[x][y - 1] == null;
			}
		} else if (y == max) {
			return stoneGrid[x + 1][y] == null || stoneGrid[x - 1][y] == null || stoneGrid[x][y - 1] == null;
		} else if (y == 0) {
			return stoneGrid[x + 1][y] == null || stoneGrid[x - 1][y] == null || stoneGrid[x][y + 1] == null;
		} else {
			return stoneGrid[x + 1][y] == null || stoneGrid[x][y + 1] == null || stoneGrid[x - 1][y] == null
					|| stoneGrid[x][y - 1] == null;
		}

	}

	/**
	 * Counts how many intersection there are neighboring provided coordinates.
	 * 
	 * @param x X coordinate to check.
	 * @param y Y coordinate to check.
	 * @return Count of how many intersections is neighboring provided coordinates.
	 */
	public static int neighborCount(int x, int y) {
		int max = SIZE - 1;
		if (x == max || x == 0) {
			if (y == max || y == 0) {
				return 2;
			} else {
				return 3;
			}
		} else if (y == max || y == 0) {
			return 3;
		} else {
			return 4;
		}
	}

	/**
	 * Get array of every stone that is adjacent to provided coordinates.
	 * 
	 * @param x X coordinate to check.
	 * @param y Y coordinate to check.
	 * @return Array of stones adjacent to provided coordinates.
	 */
	public static Stone[] getAdjacent(int x, int y) {
		Stone[] neighbors;
		int max = SIZE - 1;
		if (x == max) {
			if (y == max) {
				neighbors = new Stone[2];
				neighbors[0] = stoneGrid[x][y - 1];
				neighbors[1] = stoneGrid[x - 1][y];
			} else if (y == 0) {
				neighbors = new Stone[2];
				neighbors[0] = stoneGrid[x][y + 1];
				neighbors[1] = stoneGrid[x - 1][y];
			} else {
				neighbors = new Stone[3];
				neighbors[0] = stoneGrid[x][y - 1];
				neighbors[1] = stoneGrid[x][y + 1];
				neighbors[2] = stoneGrid[x - 1][y];
			}
		} else if (x == 0) {
			if (y == max) {
				neighbors = new Stone[2];
				neighbors[0] = stoneGrid[x][y - 1];
				neighbors[1] = stoneGrid[x + 1][y];
			} else if (y == 0) {
				neighbors = new Stone[2];
				neighbors[0] = stoneGrid[x + 1][y];
				neighbors[1] = stoneGrid[x][y + 1];
			} else {
				neighbors = new Stone[3];
				neighbors[0] = stoneGrid[x][y - 1];
				neighbors[1] = stoneGrid[x + 1][y];
				neighbors[2] = stoneGrid[x][y + 1];
			}
		} else if (y == max) {
			neighbors = new Stone[3];
			neighbors[0] = stoneGrid[x][y - 1];
			neighbors[1] = stoneGrid[x + 1][y];
			neighbors[2] = stoneGrid[x - 1][y];
		} else if (y == 0) {
			neighbors = new Stone[3];
			neighbors[0] = stoneGrid[x + 1][y];
			neighbors[1] = stoneGrid[x][y + 1];
			neighbors[2] = stoneGrid[x - 1][y];
		} else {
			neighbors = new Stone[4];
			neighbors[0] = stoneGrid[x][y - 1];
			neighbors[1] = stoneGrid[x + 1][y];
			neighbors[2] = stoneGrid[x][y + 1];
			neighbors[3] = stoneGrid[x - 1][y];
		}
		return neighbors;
	}

	/**
	 * Checks whether group of same-colored Stones has any liberty after placement
	 * of provided stone. Provided stone is placed only temporarily.
	 * 
	 * @param virtualStone Stone to be placed on board, but after finishing to be
	 *                     replaced with original content.
	 * @return true if the group has at least one liberty.
	 */
	public static boolean hasGroupLiberty(Stone virtualStone) {
		Stone tmp = stoneGrid[virtualStone.getX()][virtualStone.getY()];
		stoneGrid[virtualStone.getX()][virtualStone.getY()] = virtualStone;
		boolean result = hasGroupLiberty(virtualStone.getX(), virtualStone.getY(), new ArrayList<Stone>());
		stoneGrid[virtualStone.getX()][virtualStone.getY()] = tmp;
		return result;
	}

	/**
	 * Checks whether group, including Stone on provided coordinates, of
	 * same-colored Stones has any liberty after placement of provided stone.
	 * 
	 * @param x            X coordinate of Stone in group to check.
	 * @param y            Y coordinate of Stone in group to check.
	 * @param virtualStone Stone to be placed on board, but after finishing to be
	 *                     replaced with original content.
	 * @return true if the group has at least one liberty.
	 */
	public static boolean hasGroupLiberty(int x, int y, Stone virtualStone) {
		Stone tmp = stoneGrid[virtualStone.getX()][virtualStone.getY()];
		stoneGrid[virtualStone.getX()][virtualStone.getY()] = virtualStone;
		boolean result = hasGroupLiberty(x, y, new ArrayList<Stone>());
		stoneGrid[virtualStone.getX()][virtualStone.getY()] = tmp;
		return result;
	}

	/**
	 * Recursive algorithm. Checks whether group, including Stone on provided
	 * coordinates, of same-colored Stones has any liberty, with stone check
	 * restriction List for preventing loops.
	 * 
	 * @param x          X coordinate of Stone in group to check.
	 * @param y          Y coordinate of Stone in group to check.
	 * @param restricted Stones that must not be checked (already checked).
	 * @return true if the group has at least one liberty.
	 */
	public static boolean hasGroupLiberty(int x, int y, ArrayList<Stone> restricted) {
		StoneColor color = stoneGrid[x][y].getColor();
		Stone[] neighbors = getAdjacent(x, y);
		for (Stone stone : neighbors) {
			if (stone != null) {
				if (stone.getColor() == color && !restricted.contains(stone)) {
					restricted.add(stone);
					if (hasGroupLiberty(stone.getX(), stone.getY(), restricted)) {
						return true;
					}
				}
			} else {
				return true;
			}
		}
		return false;
	}

	/**
	 * Count how many liberties has the group in which provided Stone will belong
	 * to.
	 * 
	 * @param virtualStone Stone to be placed on board, but after finishing to be
	 *                     replaced with original content.
	 * @return Count of liberties the group has.
	 */
	public static int resultingGroupLiberty(Stone virtualStone) {
		Stone tmp = stoneGrid[virtualStone.getX()][virtualStone.getY()];
		stoneGrid[virtualStone.getX()][virtualStone.getY()] = virtualStone;
		int result = resultingGroupLiberty(virtualStone.getX(), virtualStone.getY(), new ArrayList<Stone>());
		stoneGrid[virtualStone.getX()][virtualStone.getY()] = tmp;
		return result;
	}

	/**
	 * Count how many liberties has the group in which Stone on provided coordinates
	 * belongs to.
	 * 
	 * @param x          X coordinate of Stone in group to check.
	 * @param y          Y coordinate of Stone in group to check.
	 * @param restricted Stones that must not be checked (already checked).
	 * @return Count of liberties the group has.
	 */
	public static int resultingGroupLiberty(int x, int y, ArrayList<Stone> restricted) {
		StoneColor color = stoneGrid[x][y].getColor();
		Stone[] neighbors = getAdjacent(x, y);
		int result = 0;
		for (Stone stone : neighbors) {
			if (stone != null) {
				if (stone.getColor() != color && !restricted.contains(stone)) {
					restricted.add(stone);
					if (!hasGroupLiberty(stone.getX(), stone.getY(), new ArrayList<Stone>())) {
						result++;
					}
				} else if (stone.getColor() == color && !restricted.contains(stone)) {
					restricted.add(stone);
					int recres = resultingGroupLiberty(stone.getX(), stone.getY(), restricted);
					result += recres;
				}
			}
		}
		return result;
	}

	/**
	 * Translate provided real coordinate of mouse click into board coordinate.
	 * Translates in regard to real board size (in px).
	 * 
	 * @param coord Real coordinate.
	 * @return Translated board coordinate.
	 */
	public static int parseCoord(double coord) {
		int c = (int) coord;
		int gridCoord = spacing / 2;
		int position = 0;
		while (c - gridCoord >= spacing) {
			position++;
			gridCoord += spacing;
		}
		if (position > SIZE - 1 || c < gridCoord) {
			return -1;
		} else {
			return position;
		}
	}

	/**
	 * Removes provided Stone from the board.
	 * 
	 * @param stoneToRemove Stone to remove from board.
	 */
	public void removeStone(Stone stoneToRemove) {
		stoneGrid[stoneToRemove.getX()][stoneToRemove.getY()] = null;
	}

	/**
	 * Removes provided collection of Stones from the board.
	 * 
	 * @param stonesToRemove Collection of Stones to remove from board.
	 */
	public void removeStone(Collection<Stone> stonesToRemove) {
		for (Stone stone : stonesToRemove) {
			removeStone(stone);
		}
	}

	/**
	 * Checks whether the Ko rule is applicable for Stone to be placed.
	 * 
	 * @param stone Stone to be placed.
	 * @return true - if the Ko rule is applicable.
	 */
	public static boolean koApplicable(Stone stone) {
		Stone[] adj = getAdjacent(stone.getX(), stone.getY());
		for (Stone s : adj) {
			if (s == null || s.getColor() == stone.getColor()) {
				return false;
			}
		}
		for (Stone s : adj) {
			if (!hasGroupLiberty(s.getX(), s.getY(), stone)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks whether the Ko situation will happen after placing provided stone.
	 * 
	 * @param stone Stone to be placed.
	 * @return true - if the Ko situation will arise.
	 */
	public static boolean ko(Stone stone) {
		Stone[] adj = getAdjacent(stone.getX(), stone.getY());
		History history = History.getRef();
		Stone latest = history.getLatest();
		boolean copLastPlace = false;
		for (Stone s : adj) {
			if (latest.similar(s)) {
				copLastPlace = true;
				break;
			}
		}

		if (history.isPrisoner(stone) && copLastPlace && resultingGroupLiberty(stone) == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Sets blank 2D array of Stones.
	 */
	public void initGrid() {
		stoneGrid = new Stone[SIZE][SIZE];
	}

	/**
	 * Get 2D array representation of placed stones.
	 * 
	 * @return 2D array of Stones
	 */
	public static Stone[][] getStoneGrid() {
		if (stoneGrid == null || stoneGrid.length != Settings.currentRef.board) {
			stoneGrid = new Stone[Settings.currentRef.board][Settings.currentRef.board];
		}
		return stoneGrid;
	}

	@Override
	public boolean isResizable() {
		return true;
	}

	@Override
	public double minWidth(double height) {
		return MIN_DIMENSION;
	}

	@Override
	public double minHeight(double height) {
		return MIN_DIMENSION;
	}

}
