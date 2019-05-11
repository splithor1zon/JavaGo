package sk.hor1zon.javago.views;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import sk.hor1zon.javago.Init;
import sk.hor1zon.javago.game.boards.BoardCanvas;
import sk.hor1zon.javago.utils.Resources;

public class GameView extends Application {
	private int menuWidth = 25;
	private int dashboardWidth = 300;
	private BoardCanvas board;

	private boolean atariAllowed = true;
	private boolean placementConfirmation = false;

	@Override
	public void start(Stage primaryStage) {
		VBox rootP = rootPane();
		HBox boardP = boardPane(19);
		VBox dashboardP = dashboardPane();
		VBox player1 = playerPane("Numero Uno", true);
		VBox player2 = playerPane("Numero Due", false);
		dashboardP.getChildren().addAll(player1,player2);
		VBox.setVgrow(boardP, Priority.ALWAYS);

		dashboardP.setStyle("-fx-background-color: linear-gradient(blue, azure)");
		boardP.setStyle("-fx-background-color: linear-gradient(red, pink)");
		rootP.setStyle("-fx-background-color: linear-gradient(greenyellow, limegreen)");

		boardP.getChildren().add(1, dashboardP);
		rootP.getChildren().add(1, boardP);
		// +25 is to compensate for menu bar, + 200 is for dashboard
		Scene scene = new Scene(rootP, 560 + dashboardWidth, 560 + menuWidth);

		// scene.widthProperty().addListener(evt -> scaling());
		// scene.heightProperty().addListener(evt -> scaling());

		primaryStage.setMinHeight(600 + menuWidth);
		primaryStage.setMinWidth(580 + dashboardWidth);
		primaryStage.setScene(scene);
		primaryStage.setTitle("JavaGo");
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	private VBox rootPane() {
		VBox rootPane = new VBox();
		MenuBar menuBar = new MenuBar();

		// --- Menu Game
		Menu menuGame = new Menu("Game");
		MenuItem newGame = new MenuItem("New Game",
				new ImageView(new Image(Resources.getURLofImage("placeholder.png").toString())));
		newGame.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
			}
		});
		MenuItem save = new MenuItem("Save...",
				new ImageView(new Image(Resources.getURLofImage("placeholder.png").toString())));
		save.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
			}
		});
		MenuItem pause = new MenuItem("Pause",
				new ImageView(new Image(Resources.getURLofImage("placeholder.png").toString())));
		pause.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
			}
		});
		MenuItem mainMenu = new MenuItem("Main Menu",
				new ImageView(new Image(Resources.getURLofImage("placeholder.png").toString())));
		mainMenu.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
			}
		});
		MenuItem exit = new MenuItem("Exit",
				new ImageView(new Image(Resources.getURLofImage("placeholder.png").toString())));
		exit.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
			}
		});
		menuGame.getItems().addAll(newGame, save, pause, mainMenu, exit);

		// --- Menu Options
		Menu menuOptions = new Menu("Options");

		CheckMenuItem showAtari = new CheckMenuItem("Show Atari");
		showAtari.setSelected(true);
		atariAllowed = true;
		showAtari.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				atariAllowed = !atariAllowed;
			}
		});

		CheckMenuItem confirmPlacement = new CheckMenuItem("Confirm Placement");
		confirmPlacement.setSelected(false);
		placementConfirmation = false;
		confirmPlacement.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				placementConfirmation = !placementConfirmation;
			}
		});

		MenuItem boardCust = new MenuItem("Board...",
				new ImageView(new Image(Resources.getURLofImage("placeholder.png").toString())));
		boardCust.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
			}
		});

		menuOptions.getItems().addAll(showAtari, confirmPlacement, boardCust);

		// --- Menu Help
		Menu menuHelp = new Menu("Help");

		MenuItem help = new MenuItem("Help...",
				new ImageView(new Image(Resources.getURLofImage("placeholder.png").toString())));
		help.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
			}
		});

		MenuItem about = new MenuItem("About JavaGo",
				new ImageView(new Image(Resources.getURLofImage("placeholder.png").toString())));
		about.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
			}
		});

		menuHelp.getItems().addAll(help, about);

		menuBar.getMenus().addAll(menuGame, menuOptions, menuHelp);
		rootPane.getChildren().add(0, menuBar);
		return rootPane;

	}

	private HBox boardPane(int boardSize) {
		HBox boardPane = new HBox();
		BoardCanvas board = new BoardCanvas(boardSize);
		this.board = board;
		board.widthProperty().bind(boardPane.widthProperty().subtract(dashboardWidth));
		board.heightProperty().bind(boardPane.heightProperty());
		boardPane.setMinSize(board.MIN_DIMENSION, board.MIN_DIMENSION);
		boardPane.getChildren().add(0, board);
		return boardPane;
	}

	private VBox dashboardPane() {
		VBox dashboard = new VBox();
		dashboard.setMaxWidth(dashboardWidth);
		dashboard.setPrefWidth(dashboardWidth);
		dashboard.setMinWidth(300);

		return dashboard;
	}

	private VBox playerPane(String name, boolean black) {
		VBox player = new VBox(5);
		player.setAlignment(Pos.CENTER);

		ImageView stone = new ImageView(black ? Resources.getURLofImage("black_stone.png").toString()
				: Resources.getURLofImage("white_stone.png").toString());

		Label playerName = new Label(name);
		// playerName.setAlignment(Pos.CENTER);
		playerName.setGraphic(stone);
		playerName.setFont(Font.font("Arial", FontWeight.NORMAL, 16));

		Label timeLabel = new Label("Time (Byoyomi):");
		// timeLabel.setAlignment(Pos.CENTER);
		timeLabel.setFont(Font.font("Calibri", FontWeight.NORMAL, 12));

		Label timer = new Label("00:00:00 (0)");
		// timer.setAlignment(Pos.CENTER);
		timer.setFont(Font.font("Calibri", FontWeight.NORMAL, 12));

		Label prisoners = new Label("Prisoners: 0");
		// prisoners.setAlignment(Pos.CENTER);
		prisoners.setFont(Font.font("Calibri", FontWeight.NORMAL, 12));

		Label atari = new Label("Atari!");
		// atari.setAlignment(Pos.CENTER);
		atari.setTextFill(Color.DARKRED);
		atari.setFont(Font.font("Calibri", FontWeight.BOLD, 12));

		Button passBtn = new Button("Pass");
		passBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

			}
		});

		player.getChildren().addAll(playerName, timeLabel, timer, passBtn, prisoners, atari);

		return player;
	}

}
