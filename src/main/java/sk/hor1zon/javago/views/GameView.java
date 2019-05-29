package sk.hor1zon.javago.views;

import java.io.File;
import java.util.ArrayList;
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
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;
import sk.hor1zon.javago.Init;
import sk.hor1zon.javago.game.ControllerIntf;
import sk.hor1zon.javago.game.Game;
import sk.hor1zon.javago.game.GameAlert;
import sk.hor1zon.javago.game.GameType;
import sk.hor1zon.javago.game.History;
import sk.hor1zon.javago.game.Stone;
import sk.hor1zon.javago.game.StoneColor;
import sk.hor1zon.javago.game.ViewAction;
import sk.hor1zon.javago.game.boards.BoardCanvas;
import sk.hor1zon.javago.models.GameModel;
import sk.hor1zon.javago.utils.Resources;
import sk.hor1zon.javago.utils.Settings;

/**
 * The View component of MVC structure, initializes game GUI and reacts to
 * events with calling controller functions.
 * 
 * @author splithor1zon
 *
 */
public class GameView extends Application implements Observer {
	private int menuWidth = 25;
	private int dashboardWidth = 200;
	private BoardCanvas board;
	private ControllerIntf c;
	private Stage ps;
	private Label player1label;
	private Label player2label;
	private Label player1time;
	private Label player2time;
	private Label player1prisoners;
	private Label player2prisoners;
	private Button player1pass;
	private Button player2pass;
	private boolean remoteGame = Settings.currentRef.type != GameType.LOCAL;
	private boolean allowedPlacing = Settings.currentRef.playerColor.equals("white");

	/**
	 * Creates an instance of GameView with blank board.
	 * 
	 * @param c Reference to Controller to send commands to.
	 */
	public GameView(ControllerIntf c) {
		this.c = c;
		board = new BoardCanvas(Settings.currentRef.board);
	}

	/**
	 * Creates an instance of GameView with provided Stone placement.
	 * 
	 * @param stoneGrid 2D array of Stones placed on board
	 * @param c         Reference to Controller to send commands to.
	 */
	public GameView(ControllerIntf c, Stone[][] stoneGrid) {
		this.c = c;
		board = new BoardCanvas(Settings.currentRef.board, stoneGrid);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.ps = primaryStage;
		initV();
	}

	@Override
	public void update(Observable ov, Object objAction) {
		ViewAction action = (ViewAction) objAction;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				switch (action) {
				case UPDATE_TIME:
					updateTime((int[]) action.getContent());
					break;
				case PLACE:
					playStone((Stone) action.getContent(), false);
					break;
				case PLACEBATCH:
					for (Stone stone : (Stone[]) action.getContent()) {
						playStone(stone, false);
					}
					break;
				case PLACEKO:
					playStone((Stone) action.getContent(), true);
					break;
				case ALERT:
					showAlert((GameAlert) action.getContent());
					break;
				case SWITCH_PLAYER:
					setActivePlayer((int) action.getContent());
					break;
				case UPDATE_PRISONERS:
					updatePrisoners((int[]) action.getContent());
					break;
				case NEGOTIATE:
					showNegotiation();
					break;
				case FINISH:
					showFinish();
					break;
				}
			}
		});

	}

	private void initV() {
		VBox rootP = rootPane();
		HBox boardP = boardPane();
		VBox dashboardP = dashboardPane();
		VBox player1;
		VBox player2;
		if (remoteGame) {
			boolean white = Settings.currentRef.playerColor.equals("white");
			player1 = playerPane(Settings.currentRef.player1,
					Settings.currentRef.type == GameType.SERVER ? white : !white,
					Settings.currentRef.type == GameType.SERVER);
			player2 = playerPane(Settings.currentRef.player2,
					Settings.currentRef.type == GameType.CLIENT ? white : !white,
					Settings.currentRef.type == GameType.CLIENT);
			if (Settings.currentRef.type == GameType.SERVER) {
				player1prisoners = (Label) player1.getChildren().get(4);
				player2prisoners = (Label) player2.getChildren().get(3);
				player1pass = (Button) player1.getChildren().get(3);
				player2pass = null;
			} else {
				player1prisoners = (Label) player1.getChildren().get(3);
				player2prisoners = (Label) player2.getChildren().get(4);
				player1pass = null;
				player2pass = (Button) player2.getChildren().get(3);
			}

		} else {
			player1 = playerPane(Settings.currentRef.player1, Settings.currentRef.playerColor.equals("white"), true);
			player2 = playerPane(Settings.currentRef.player2, Settings.currentRef.playerColor.equals("black"), true);
			player1prisoners = (Label) player1.getChildren().get(4);
			player2prisoners = (Label) player2.getChildren().get(4);
			player1pass = (Button) player1.getChildren().get(3);
			player2pass = (Button) player2.getChildren().get(3);
		}
		player1label = (Label) player1.getChildren().get(0);
		player2label = (Label) player2.getChildren().get(0);
		player1time = (Label) player1.getChildren().get(2);
		player2time = (Label) player2.getChildren().get(2);
		dashboardP.getChildren().addAll(player1, player2);
		VBox.setVgrow(boardP, Priority.ALWAYS);
		boardP.getChildren().add(1, dashboardP);
		rootP.getChildren().add(1, boardP);
		// +25 is to compensate for menu bar, + 200 is for dashboard
		Scene scene = new Scene(rootP, 560 + dashboardWidth, 560 + menuWidth);
		scene.getStylesheets().add(getClass().getResource("/css/game.css").toExternalForm());
		ps.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				GameModel.shutdown();
			}
		});
		ps.setMinHeight(600 + menuWidth);
		ps.setMinWidth(580 + dashboardWidth);
		ps.setScene(scene);
		ps.setTitle("JavaGo");
		ps.show();
		c.viewReady(this);
	}

	private VBox rootPane() {
		VBox rootPane = new VBox();
		MenuBar menuBar = new MenuBar();

		Alert nafDialog = new Alert(AlertType.ERROR);
		nafDialog.setTitle("Error");
		nafDialog.setHeaderText("Not a File!");
		nafDialog.setContentText("The path you provided does not correspond to a valid file or is unreadable!");

		Alert readEDialog = new Alert(AlertType.ERROR);
		readEDialog.setTitle("Error");
		readEDialog.setHeaderText("Could not load this file!");
		readEDialog.setContentText("The path you provided corresponds to either invalid file or corrupted file!");

		Dialog<File> dialogSave = new Dialog<>();
		ButtonType okType = new ButtonType("Save!", ButtonData.OK_DONE);
		dialogSave.setTitle("Save game");
		dialogSave.setHeaderText("Where do you want to save your game?");
		dialogSave.getDialogPane().getButtonTypes().addAll(okType, ButtonType.CANCEL);
		GridPane dialogSaveGrid = new GridPane();
		dialogSaveGrid.setHgap(10);
		dialogSaveGrid.setVgap(10);
		dialogSaveGrid.setPadding(new Insets(20, 10, 10, 10));

		TextField filePath = new TextField();
		filePath.setPrefWidth(200);
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Game File");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JavaGo save", "*.jgo"));
		Button fileSaveBtn = new Button("Save...");
		fileSaveBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				File file = fileChooser.showSaveDialog(ps);
				if (file != null) {
					filePath.setText(file.getAbsolutePath());
				}
			}
		});
		dialogSaveGrid.add(filePath, 0, 0);
		dialogSaveGrid.add(fileSaveBtn, 1, 0);
		dialogSaveGrid.setAlignment(Pos.CENTER);
		dialogSave.getDialogPane().setContent(dialogSaveGrid);
		dialogSave.setResultConverter(dialogButton -> {
			if (dialogButton == okType) {
				File file = new File(filePath.getText());
				if (file != null && file.getName().endsWith(".jgo")) {
					return file;
				} else {
					nafDialog.showAndWait();
					return null;
				}
			}
			return null;
		});

		// --- Menu Game
		Menu menuGame = new Menu("Game");
		MenuItem newGame = new MenuItem("New Game");
		newGame.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				ps.close();
				GameModel.shutdown();
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
		MenuItem save = new MenuItem("Save...");
		save.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				File file;
				try {
					file = dialogSave.showAndWait().get();
				} catch (Exception e) {
					return;
				}
				if (file != null) {
					Game game = new Game();
					game.write(file);
				}

			}
		});
		MenuItem mainMenu = new MenuItem("Main Menu");
		mainMenu.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				ps.close();
				GameModel.shutdown();
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
		MenuItem exit = new MenuItem("Exit");
		exit.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				ps.close();
				GameModel.shutdown();
			}
		});
		menuGame.getItems().addAll(newGame, save, mainMenu, exit);

		// --- Menu Options
		Menu menuOptions = new Menu("Options");

		MenuItem boardCust = new MenuItem("Board...");
		boardCust.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
			}
		});

		menuOptions.getItems().addAll(boardCust);

		// --- Menu Help
		Menu menuHelp = new Menu("Help");

		MenuItem help = new MenuItem("Help...");
		help.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
			}
		});

		MenuItem about = new MenuItem("About JavaGo");
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

		board.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (remoteGame) {
					if (!allowedPlacing) {
						return;
					}
				}
				int x = BoardCanvas.parseCoord(mouseEvent.getX());
				int y = BoardCanvas.parseCoord(mouseEvent.getY());
				if (mouseEvent.getButton() == MouseButton.PRIMARY && x != -1 && y != -1) {
					c.placeStone(x, y);
					allowedPlacing = false;
				}
			}
		});
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

	private VBox playerPane(String name, boolean white, boolean passEnabled) {
		VBox player = new VBox(5);
		player.setAlignment(Pos.CENTER);
		// player.setBorder(new Border(new BorderStroke(Color.BLACK,
		// BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
		ImageView stone = new ImageView(white ? Resources.getURLofImage("white_stone.png").toString()
				: Resources.getURLofImage("black_stone.png").toString());

		Label playerName = new Label(name);
		// playerName.setAlignment(Pos.CENTER);
		playerName.setGraphic(stone);

		Label timeLabel = new Label("Time (Byoyomi):");
		// timeLabel.setAlignment(Pos.CENTER);
		timeLabel.setFont(Font.font("Calibri", FontWeight.NORMAL, 20));

		Label timer = new Label("00:00:00 (00)");
		// timer.setAlignment(Pos.CENTER);
		timer.setFont(Font.font("Calibri", FontWeight.NORMAL, 20));

		Label prisoners = new Label(
				"Prisoners: " + History.getRef().getPrisonerCount(white ? StoneColor.WHITE : StoneColor.BLACK));
		// prisoners.setAlignment(Pos.CENTER);
		prisoners.setFont(Font.font("Calibri", FontWeight.NORMAL, 20));

		Button passBtn = new Button("Pass");
		passBtn.setDisable(!white);
		passBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				c.pass();
			}
		});

		if (white) {
			playerName.setId("active");
		} else {
			playerName.setId("inactive");
		}
		if (passEnabled) {
			player.getChildren().addAll(playerName, timeLabel, timer, passBtn, prisoners);
		} else {
			player.getChildren().addAll(playerName, timeLabel, timer, prisoners);
		}

		return player;
	}

	private void playStone(Stone toPlace, boolean ko) {
		ArrayList<Stone> toRemove = board.placeStone(toPlace, ko);
		if (toRemove.size() > 0) {
			c.removedNotice(toRemove);
		}
	}

	private void setActivePlayer(int player) {
		if (player == 1) {
			player1label.setId("active");
			player2label.setId("inactive");
			if (player1pass != null) {
				player1pass.setDisable(false);
			}
			if (player2pass != null) {
				player2pass.setDisable(true);
			}
			if (Settings.currentRef.type == GameType.SERVER) {
				allowedPlacing = true;
			}
		} else if (player == 2) {
			player1label.setId("inactive");
			player2label.setId("active");
			if (player1pass != null) {
				player1pass.setDisable(true);
			}
			if (player2pass != null) {
				player2pass.setDisable(false);
			}
			if (Settings.currentRef.type == GameType.CLIENT) {
				allowedPlacing = true;
			}
		}
	}

	private void updateTime(int[] timePackage) {
		int hours = timePackage[1] / 3600;
		int minutes = (timePackage[1] - (60 * hours)) / 60;
		int seconds = (timePackage[1] - (3600 * hours) - (60 * minutes));
		String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
		String byoyomi = String.format(" (%02d)", timePackage[2]);
		if (timePackage[0] == 1) {
			player1time.setText(time + byoyomi);
		} else if (timePackage[0] == 2) {
			player2time.setText(time + byoyomi);
		}
	}

	private void updatePrisoners(int[] prisonerPackage) {
		if (prisonerPackage[0] == 1) {
			player1prisoners.setText("Prisoners: " + prisonerPackage[1]);
		} else if (prisonerPackage[0] == 2) {
			player2prisoners.setText("Prisoners: " + prisonerPackage[1]);
		}
	}

	private void showAlert(GameAlert gameAlert) {
		Alert alert;
		switch (gameAlert) {
		case OCCUPIED:
			alert = new Alert(AlertType.ERROR);
			alert.setTitle("-_-");
			alert.setHeaderText("Selected intersection is occupied!");
			alert.setContentText(
					"The intersection you selected is already occupied by another stone! Please select an empty intersection.");
			allowedPlacing = true;
			break;
		case ILLEGAL:
			alert = new Alert(AlertType.ERROR);
			alert.setTitle("!!!");
			alert.setHeaderText("Illegal placement detected!");
			alert.setContentText(
					"You shouldn't do illegal things, that also applies for Go!\nPlease read the rules properly.");
			allowedPlacing = true;
			break;
		case SUICIDE:
			alert = new Alert(AlertType.ERROR);
			alert.setTitle("???");
			alert.setHeaderText("Are you trying to kill yourself?");
			alert.setContentText("I would let you, but the rules say no...\nPlease read the rules properly.");
			allowedPlacing = true;
			break;
		case TIMEUP:
			c.pass();
			alert = new Alert(AlertType.WARNING);
			alert.setTitle("The time flies...");
			alert.setHeaderText("Your time is up!");
			alert.setContentText("You exceeded the byoyomi time.\nYour turn is registered as pass.");
			break;
		case KO:
			alert = new Alert(AlertType.ERROR);
			alert.setTitle("KO!");
			alert.setHeaderText("KO!");
			alert.setContentText("KO!");
			allowedPlacing = true;
			break;
		case INVALID_SCORE:
			alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid score.");
			alert.setHeaderText("Invalid score entered!");
			alert.setContentText(
					"The score values you entered are invalid! Please enter only positive (including 0) integer values.");
			alert.showAndWait();
			return;
		default:
			return;
		}
		alert.show();
	}

	private void showNegotiation() {
		ButtonType okType = new ButtonType("OK", ButtonData.OK_DONE);
		// Negotiation Dialog.
		Dialog<Pair<Boolean, Integer[]>> dialogNeg = new Dialog<>();
		dialogNeg.setTitle("Result negotiaton");
		dialogNeg.setHeaderText("Please negotiate with your opponent scoring of each player.\nAccepting only integer!"
				+ "\nKomi of " + Settings.currentRef.komi + " will be added to white player ("
				+ (Settings.currentRef.playerColor.equals("white") ? Settings.currentRef.player1
						: Settings.currentRef.player2)
				+ ").\nThe number of placed black stones is: " + History.getRef().getActiveStoneCount(StoneColor.BLACK)
				+ ".\nThe number of placed white stones is: " + History.getRef().getActiveStoneCount(StoneColor.WHITE)
				+ ".");

		// Set the button types.
		dialogNeg.getDialogPane().getButtonTypes().addAll(okType, ButtonType.CANCEL);

		GridPane dialogNegGrid = new GridPane();
		dialogNegGrid.setHgap(10);
		dialogNegGrid.setVgap(10);
		dialogNegGrid.setPadding(new Insets(20, 10, 10, 10));

		// Create the labels and fields.
		TextField player1res = new TextField();
		player1res.setPromptText("Player 1 score");
		TextField player2res = new TextField();
		player2res.setPromptText("Player 2 score");

		dialogNegGrid.add(new Label(Settings.currentRef.player1 + "'s result:"), 0, 0);
		dialogNegGrid.add(player1res, 1, 0);
		dialogNegGrid.add(new Label(Settings.currentRef.player2 + "'s result:"), 0, 1);
		dialogNegGrid.add(player2res, 1, 1);

		dialogNeg.getDialogPane().setContent(dialogNegGrid);

		dialogNeg.setResultConverter(dialogButton -> {
			Integer player1score = 0;
			Integer player2score = 0;
			Integer[] res = new Integer[2];
			if (dialogButton == okType) {
				try {
					player1score = Integer.valueOf(player1res.getText());
					player2score = Integer.valueOf(player2res.getText());
				} catch (Exception e) {
					player1score = -1;
				}
				res[0] = player1score;
				res[1] = player2score;
				Pair<Boolean, Integer[]> pair = new Pair<Boolean, Integer[]>(true, res);
				return pair;
			} else {
				res[0] = player1score;
				res[1] = player2score;
				Pair<Boolean, Integer[]> pair = new Pair<Boolean, Integer[]>(false, res);
				return pair;
			}
		});

		Pair<Boolean, Integer[]> result;
		try {
			result = dialogNeg.showAndWait().get();
		} catch (Exception e) {
			return;
		}
		if (result == null || result.getValue()[0] < 0 || result.getValue()[1] < 0) {
			showAlert(GameAlert.INVALID_SCORE);
			showNegotiation();
			return;
		}
		c.resultNegotiation(result.getKey(), result.getValue()[0], result.getValue()[1]);

	}

	private void showFinish() {
		Alert nafDialog = new Alert(AlertType.ERROR);
		nafDialog.setTitle("Error");
		nafDialog.setHeaderText("Not a File!");
		nafDialog.setContentText("The path you entered does not correspond to a valid file or is unreadable!");

		ButtonType okType = new ButtonType("OK", ButtonData.OK_DONE);
		ButtonType saveType = new ButtonType("Save", ButtonData.APPLY);

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Game for Replay");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JavaGo save", "*.jgo"));

		// Negotiation Dialog.
		Dialog<Pair<Boolean, File>> dialogFin = new Dialog<>();
		dialogFin.setTitle("Result notice");

		Text infoText;
		if (History.getRef().getWinner() == 0) {
			dialogFin.setHeaderText("Draw!");
			infoText = new Text("Neither of you is winner, but you had a good game, haven't you? ;)");

		} else {
			dialogFin.setHeaderText("The winner is: "
					+ (History.getRef().getWinner() == 1 ? Settings.currentRef.player1 : Settings.currentRef.player2));
			infoText = new Text("Congratulations to winner!");
		}

		// Set the button types.
		dialogFin.getDialogPane().getButtonTypes().addAll(okType, saveType);

		GridPane dialogFinGrid = new GridPane();
		dialogFinGrid.setHgap(10);
		dialogFinGrid.setVgap(10);
		dialogFinGrid.setPadding(new Insets(20, 10, 10, 10));

		// Create the labels and fields.

		dialogFinGrid.add(infoText, 0, 0, 2, 1);
		dialogFinGrid.add(new Label(Settings.currentRef.player1 + "'s result:"), 0, 1);
		dialogFinGrid.add(new Text(Double
				.toString((Settings.currentRef.playerColor.equals("white") ? History.getRef().getScore(StoneColor.WHITE)
						: History.getRef().getScore(StoneColor.BLACK)))),
				1, 1);
		dialogFinGrid.add(new Label(Settings.currentRef.player2 + "'s result:"), 0, 2);
		dialogFinGrid.add(new Text(Double
				.toString((Settings.currentRef.playerColor.equals("white") ? History.getRef().getScore(StoneColor.BLACK)
						: History.getRef().getScore(StoneColor.WHITE)))),
				1, 2);

		dialogFin.getDialogPane().setContent(dialogFinGrid);

		dialogFin.setResultConverter(dialogButton -> {
			Pair<Boolean, File> pair;
			File file = null;
			if (dialogButton == saveType) {
				file = fileChooser.showSaveDialog(ps);
				if (file != null) {
					pair = new Pair<>(true, file);
				} else {
					pair = new Pair<>(false, file);
				}

			} else {
				pair = new Pair<>(false, file);
			}
			return pair;
		});

		Pair<Boolean, File> result = new Pair<>(false, null);
		try {
			result = dialogFin.showAndWait().get();
		} catch (Exception e) {
			return;
		}
		if (result.getKey()) {
			if (result.getValue() == null) {
				nafDialog.showAndWait();
			} else {
				c.saveGame(result.getValue());
			}
			showFinish();
		} else {
			ps.close();
			GameModel.shutdown();
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
	}
}
