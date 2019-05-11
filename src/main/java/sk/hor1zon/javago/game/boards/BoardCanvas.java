package sk.hor1zon.javago.game.boards;

import java.util.Collection;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sk.hor1zon.javago.game.Stone;

public class BoardCanvas extends Canvas {
	private final int SIZE;
	private final int SPACING_COUNT;
	public final int MIN_DIMENSION = 560;
	private final int MIN_SPACING;

	private int spacing;

	public BoardCanvas(int size) {
		SIZE = size;
		SPACING_COUNT = SIZE + 1;
		MIN_SPACING = MIN_DIMENSION / SPACING_COUNT;
		widthProperty().addListener(evt -> scaling());
		heightProperty().addListener(evt -> scaling());
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
			draw();
		}
	}

	private void draw() {
		int maxBoardCoord = spacing * SIZE;
		GraphicsContext gc = getGraphicsContext2D();

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

	public boolean placeStone(Stone stoneToPlace) {
		return false;
	}
	
	public boolean removeStone(Stone stoneToRemove) {
		return false;
	}
	
	public boolean removeStone(Collection<Stone> stonesToRemove) {
		return false;
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
