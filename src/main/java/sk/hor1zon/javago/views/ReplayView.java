package sk.hor1zon.javago.views;

import java.util.Observable;
import java.util.Observer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import sk.hor1zon.javago.Init;
import sk.hor1zon.javago.game.History;
import sk.hor1zon.javago.game.ReplayController;
import sk.hor1zon.javago.game.Stone;
import sk.hor1zon.javago.game.StoneColor;
import sk.hor1zon.javago.game.ViewAction;
import sk.hor1zon.javago.game.boards.BoardCanvas;
import sk.hor1zon.javago.utils.Resources;
import sk.hor1zon.javago.utils.Settings;

/**
 * Provides GUI capabilities for replaying saved finished games.
 * 
 * @author splithor1zon
 *
 */
public class ReplayView extends Application implements Observer {
	private int menuWidth = 25;
	private int dashboardWidth = 200;
	private BoardCanvas board;
	private ReplayController rc;
	private Stage ps;
	private Label player1label;
	private Label player2label;
	private Label player1time;
	private Label player2time;
	private Label cursorLabel;

	/**
	 * Creates ReplayView with provided reference to ReplayController.
	 * 
	 * @param rc ReplayController to communicate with.
	 */
	public ReplayView(ReplayController rc) {
		this.rc = rc;
		board = new BoardCanvas(Settings.currentRef.board);
	}

	@Override
	public void update(Observable o, Object arg) {
		ViewAction action = (ViewAction) arg;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				switch (action) {
				case PLACEBATCH:
					board.initGrid();
					Stone[] toPlace = (Stone[]) action.getContent();
					Stone last = toPlace[toPlace.length - 1];
					StoneColor player1color = Settings.currentRef.playerColor == "white" ? StoneColor.WHITE
							: StoneColor.BLACK;
					for (Stone stone : toPlace) {
						playStone(stone);
					}

					int onTurn = last.getColor() == player1color ? 2 : 1;
					setActivePlayer(onTurn);
					int[] tp = { onTurn == 1 ? 2 : 1, last.getPlaceTime() };
					updateTime(tp);
					updateCursor(toPlace.length - 1);
					break;
				case FINISH:
					showFinish();
					break;
				default:
					break;
				}
			}
		});

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.ps = primaryStage;
		initRV();
	}

	private void initRV() {
		ps.setTitle("JavaGo - Replay");
		VBox rootP = rootPane();
		HBox boardP = boardPane();
		VBox dashboardP = dashboardPane();
		VBox player1 = playerPane(Settings.currentRef.player1, Settings.currentRef.playerColor.equals("white"));
		VBox player2 = playerPane(Settings.currentRef.player2, Settings.currentRef.playerColor.equals("black"));
		player1label = (Label) player1.getChildren().get(0);
		player2label = (Label) player2.getChildren().get(0);
		player1time = (Label) player1.getChildren().get(2);
		player2time = (Label) player2.getChildren().get(2);
		GridPane controls = controls();
		controls.setAlignment(Pos.TOP_CENTER);
		dashboardP.getChildren().addAll(player1, player2, controls);
		VBox.setVgrow(boardP, Priority.ALWAYS);
		boardP.getChildren().add(1, dashboardP);
		rootP.getChildren().add(1, boardP);

		// +25 is to compensate for menu bar, + 200 is for dashboard
		Scene scene = new Scene(rootP, 560 + dashboardWidth, 560 + menuWidth);
		scene.getStylesheets().add(getClass().getResource("/css/game.css").toExternalForm());
		ps.setMinHeight(600 + menuWidth);
		ps.setMinWidth(580 + dashboardWidth);
		ps.setScene(scene);
		ps.setTitle("JavaGo");
		ps.show();
		rc.viewReady(this);
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
				ps.close();
				Platform.runLater(new Runnable() {
					public void run() {
						try {
							sk.hor1zon.javago.Menu menu = new sk.hor1zon.javago.Menu();
							menu.start(new Stage());
							Init.initModel(true, menu);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});

		MenuItem mainMenu = new MenuItem("Main Menu",
				new ImageView(new Image(Resources.getURLofImage("placeholder.png").toString())));
		mainMenu.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				ps.close();
				Platform.runLater(new Runnable() {
					public void run() {
						try {
							sk.hor1zon.javago.Menu menu = new sk.hor1zon.javago.Menu();
							menu.start(new Stage());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		MenuItem exit = new MenuItem("Exit",
				new ImageView(new Image(Resources.getURLofImage("placeholder.png").toString())));
		exit.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				ps.close();
			}
		});
		menuGame.getItems().addAll(newGame, mainMenu, exit);

		// --- Menu Options
		Menu menuOptions = new Menu("Options");

		MenuItem boardCust = new MenuItem("Board...",
				new ImageView(new Image(Resources.getURLofImage("placeholder.png").toString())));
		boardCust.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
			}
		});

		menuOptions.getItems().addAll(boardCust);

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

	private HBox boardPane() {
		HBox boardPane = new HBox();
		boardPane.setId("board");

		board.widthProperty().bind(boardPane.widthProperty().subtract(dashboardWidth));
		board.heightProperty().bind(boardPane.heightProperty());
		boardPane.setMinSize(board.MIN_DIMENSION, board.MIN_DIMENSION);
		boardPane.getChildren().add(0, board);
		return boardPane;
	}

	private VBox dashboardPane() {
		VBox dashboard = new VBox(30);
		dashboard.setId("dashboard");
		dashboard.setPadding(new Insets(10, 5, 0, 5));
		dashboard.setMaxWidth(dashboardWidth);
		dashboard.setPrefWidth(dashboardWidth);
		dashboard.setMinWidth(dashboardWidth);
		dashboard.setAlignment(Pos.TOP_CENTER);

		return dashboard;
	}

	private VBox playerPane(String name, boolean white) {
		VBox player = new VBox(5);
		player.setAlignment(Pos.CENTER);
		// player.setBorder(new Border(new BorderStroke(Color.BLACK,
		// BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
		ImageView stone = new ImageView(white ? Resources.getURLofImage("white_stone.png").toString()
				: Resources.getURLofImage("black_stone.png").toString());

		Label playerName = new Label(name);
		// playerName.setAlignment(Pos.CENTER);
		playerName.setGraphic(stone);

		Label timeLabel = new Label("Time:");
		// timeLabel.setAlignment(Pos.CENTER);
		timeLabel.setFont(Font.font("Calibri", FontWeight.NORMAL, 20));

		Label timer = new Label("00:00:00");
		// timer.setAlignment(Pos.CENTER);
		timer.setFont(Font.font("Calibri", FontWeight.NORMAL, 20));

		if (white) {
			playerName.setId("active");
		} else {
			playerName.setId("inactive");
		}
		player.getChildren().addAll(playerName, timeLabel, timer);

		return player;
	}

	private GridPane controls() {
		GridPane gp = new GridPane();
		gp.setHgap(10);
		gp.setVgap(5);

		Button homeBtn = new Button("<<");
		Button prevBtn = new Button("<");
		Button nextBtn = new Button(">");
		Button endBtn = new Button(">>");
		homeBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				rc.setCursor(0);
			}
		});
		prevBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				rc.previousStone();
			}
		});
		nextBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				rc.nextStone();
			}
		});
		endBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				rc.setCursor(rc.LAST_STONE_IDX);
			}
		});
		Label cursorLabel = new Label(Integer.toString(rc.getCursor()));
		this.cursorLabel = cursorLabel;
		gp.add(new Label("Controls:"), 0, 0, 5, 1);
		gp.add(homeBtn, 0, 1);
		gp.add(prevBtn, 1, 1);
		gp.add(cursorLabel, 2, 1);
		gp.add(nextBtn, 3, 1);
		gp.add(endBtn, 4, 1);

		return gp;
	}

	private void playStone(Stone toPlace) {
		board.placeStone(toPlace, true);
	}

	private void setActivePlayer(int player) {
		if (player == 1) {
			player1label.setId("active");
			player2label.setId("inactive");
		} else if (player == 2) {
			player1label.setId("inactive");
			player2label.setId("active");
		}
	}

	private void updateTime(int[] timePackage) {
		int hours = timePackage[1] / 3600;
		int minutes = (timePackage[1] - (60 * hours)) / 60;
		int seconds = (timePackage[1] - (3600 * hours) - (60 * minutes));
		String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
		if (timePackage[0] == 1) {
			player1time.setText(time);
		} else if (timePackage[0] == 2) {
			player2time.setText(time);
		}
	}

	private void updateCursor(int cursor) {
		cursorLabel.setText(Integer.toString(cursor));
	}

	private void showFinish() {
		Alert endDialog = new Alert(AlertType.INFORMATION);
		endDialog.setTitle("End of game");
		if (History.getRef().getWinner() == 0) {
			endDialog.setHeaderText("Draw!");
			endDialog
					.setContentText("The game ended in a draw!\n\n" + Settings.currentRef.player1 + "'s result: "
							+ Double.toString((Settings.currentRef.playerColor == "white"
									? History.getRef().getScore(StoneColor.WHITE)
									: History.getRef().getScore(StoneColor.BLACK)))
							+ "\n" + Settings.currentRef.player2 + "'s result: "
							+ Double.toString((Settings.currentRef.playerColor == "white"
									? History.getRef().getScore(StoneColor.BLACK)
									: History.getRef().getScore(StoneColor.WHITE))));
		} else {
			endDialog.setHeaderText(
					(History.getRef().getWinner() == 1 ? Settings.currentRef.player1 : Settings.currentRef.player2)
							+ " won the game!");
			endDialog
					.setContentText(Settings.currentRef.player1 + "'s result: "
							+ Double.toString((Settings.currentRef.playerColor == "white"
									? History.getRef().getScore(StoneColor.WHITE)
									: History.getRef().getScore(StoneColor.BLACK)))
							+ "\n" + Settings.currentRef.player2 + "'s result: "
							+ Double.toString((Settings.currentRef.playerColor == "white"
									? History.getRef().getScore(StoneColor.BLACK)
									: History.getRef().getScore(StoneColor.WHITE))));
		}
		endDialog.showAndWait();
	}

}
