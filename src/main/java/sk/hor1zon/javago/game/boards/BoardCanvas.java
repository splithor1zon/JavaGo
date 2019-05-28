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

public class BoardCanvas extends Canvas {
	private static int SIZE;
	private final int SPACING_COUNT;
	public final int MIN_DIMENSION = 560;
	private final int MIN_SPACING;
	private GraphicsContext gc;
	public static BoardCanvas currRef;

	private static int spacing;
	private static Stone[][] stoneGrid;

	public static Stone[][] getStoneGrid() {
		if (stoneGrid == null || stoneGrid.length != Settings.currentRef.board) {
			stoneGrid = new Stone[Settings.currentRef.board][Settings.currentRef.board];
		}
		return stoneGrid;
	}

	public BoardCanvas(int size) {
		SIZE = size;
		SPACING_COUNT = SIZE + 1;
		MIN_SPACING = MIN_DIMENSION / SPACING_COUNT;
		widthProperty().addListener(evt -> scaling());
		heightProperty().addListener(evt -> scaling());
		stoneGrid = new Stone[SIZE][SIZE];
		currRef = this;
		//drawBoard();
	}
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

	public void drawStones() {
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

	public static ArrayList<Stone> koRemain(Stone virtualStone) {
		Stone tmp = stoneGrid[virtualStone.getX()][virtualStone.getY()];
		stoneGrid[virtualStone.getX()][virtualStone.getY()] = virtualStone;
		ArrayList<Stone> result = koRemain(virtualStone.getX(), virtualStone.getY(), new ArrayList<Stone>());
		stoneGrid[virtualStone.getX()][virtualStone.getY()] = tmp;
		return result;
	}

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

	public static boolean isOccupied(int x, int y) {
		if (stoneGrid[x][y] != null) {
			return true;
		} else {
			return false;
		}
	}

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

	public static boolean hasGroupLiberty(Stone virtualStone) {
		Stone tmp = stoneGrid[virtualStone.getX()][virtualStone.getY()];
		stoneGrid[virtualStone.getX()][virtualStone.getY()] = virtualStone;
		boolean result = hasGroupLiberty(virtualStone.getX(), virtualStone.getY(), new ArrayList<Stone>());
		stoneGrid[virtualStone.getX()][virtualStone.getY()] = tmp;
		return result;
	}

	public static boolean hasGroupLiberty(int x, int y, Stone virtualStone) {
		Stone tmp = stoneGrid[virtualStone.getX()][virtualStone.getY()];
		stoneGrid[virtualStone.getX()][virtualStone.getY()] = virtualStone;
		boolean result = hasGroupLiberty(x, y, new ArrayList<Stone>());
		stoneGrid[virtualStone.getX()][virtualStone.getY()] = tmp;
		return result;
	}

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

	public static int resultingGroupLiberty(Stone virtualStone) {
		Stone tmp = stoneGrid[virtualStone.getX()][virtualStone.getY()];
		stoneGrid[virtualStone.getX()][virtualStone.getY()] = virtualStone;
		int result = resultingGroupLiberty(virtualStone.getX(), virtualStone.getY(), new ArrayList<Stone>());
		stoneGrid[virtualStone.getX()][virtualStone.getY()] = tmp;
		return result;
	}

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

	public void removeStone(Stone stoneToRemove) {
		stoneGrid[stoneToRemove.getX()][stoneToRemove.getY()] = null;
	}

	public void removeStone(Collection<Stone> stonesToRemove) {
		for (Stone stone : stonesToRemove) {
			removeStone(stone);
		}
	}

	public static boolean koApplicable(Stone stone) {
		int count = 0;
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
	
	public void initGrid() {
		stoneGrid = new Stone[SIZE][SIZE];
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
