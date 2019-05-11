package sk.hor1zon.javago.game.boards;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Tip 1: A canvas resizing itself to the size of the parent pane.
 */
public class Tip1ResizableCanvas extends Application {

	class ResizableCanvas extends Canvas {
		private final int SIZE = 19;
		private final int SPACING_COUNT = SIZE + 1;
		private final int MIN_DIMENSION = 560;
		private final int MIN_SPACING = MIN_DIMENSION / SPACING_COUNT;
		
		private int spacing;
		
		
		public ResizableCanvas() {
			// Redraw canvas when size changes.
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
			gc.setLineWidth((double)spacing/(double)MIN_SPACING+0.2);

			for (int x = spacing; x <= maxBoardCoord; x += spacing) {
				gc.strokeLine(x, spacing, x, maxBoardCoord);
			}
			for (int y = spacing; y <= maxBoardCoord; y += spacing) {
				gc.strokeLine(spacing, y, maxBoardCoord, y);
			}
			
			int diamondDiameter = spacing/5;
			diamondDiameter = diamondDiameter + (diamondDiameter % 2);
			int diamondRadius = diamondDiameter/2;
			gc.setFill(Color.BLACK);
			switch (SIZE) {
			case 19:
				gc.fillOval(spacing*4-diamondRadius, spacing*4-diamondRadius, diamondDiameter, diamondDiameter);
				gc.fillOval(spacing*10-diamondRadius, spacing*4-diamondRadius, diamondDiameter, diamondDiameter);
				gc.fillOval(spacing*16-diamondRadius, spacing*4-diamondRadius, diamondDiameter, diamondDiameter);
				gc.fillOval(spacing*4-diamondRadius, spacing*10-diamondRadius, diamondDiameter, diamondDiameter);
				gc.fillOval(spacing*10-diamondRadius, spacing*10-diamondRadius, diamondDiameter, diamondDiameter);
				gc.fillOval(spacing*16-diamondRadius, spacing*10-diamondRadius, diamondDiameter, diamondDiameter);
				gc.fillOval(spacing*4-diamondRadius, spacing*16-diamondRadius, diamondDiameter, diamondDiameter);
				gc.fillOval(spacing*10-diamondRadius, spacing*16-diamondRadius, diamondDiameter, diamondDiameter);
				gc.fillOval(spacing*16-diamondRadius, spacing*16-diamondRadius, diamondDiameter, diamondDiameter);
				break;
			case 13:
				gc.fillOval(spacing*3-diamondRadius, spacing*3-diamondRadius, diamondDiameter, diamondDiameter);
				gc.fillOval(spacing*11-diamondRadius, spacing*3-diamondRadius, diamondDiameter, diamondDiameter);
				gc.fillOval(spacing*3-diamondRadius, spacing*11-diamondRadius, diamondDiameter, diamondDiameter);
				gc.fillOval(spacing*11-diamondRadius, spacing*11-diamondRadius, diamondDiameter, diamondDiameter);
				gc.fillOval(spacing*7-diamondRadius, spacing*7-diamondRadius, diamondDiameter, diamondDiameter);
				break;
			case 9:
				gc.fillOval(spacing*3-diamondRadius, spacing*3-diamondRadius, diamondDiameter, diamondDiameter);
				gc.fillOval(spacing*7-diamondRadius, spacing*3-diamondRadius, diamondDiameter, diamondDiameter);
				gc.fillOval(spacing*3-diamondRadius, spacing*7-diamondRadius, diamondDiameter, diamondDiameter);
				gc.fillOval(spacing*7-diamondRadius, spacing*7-diamondRadius, diamondDiameter, diamondDiameter);
				gc.fillOval(spacing*5-diamondRadius, spacing*5-diamondRadius, diamondDiameter, diamondDiameter);
				break;
			default:
				break;
			}
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

	@Override
	public void start(Stage stage) throws Exception {
		BoardCanvas canvas = new BoardCanvas(19);

		StackPane stackPane = new StackPane();
		stackPane.getChildren().add(canvas);

		// Bind canvas size to stack pane size.
		canvas.widthProperty().bind(stackPane.widthProperty());
		canvas.heightProperty().bind(stackPane.heightProperty());
		stage.setMinHeight(600);
		stage.setMinWidth(580);
		stage.setScene(new Scene(stackPane));
		stage.setTitle("Tip 1: Resizable Canvas");
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}