package sk.hor1zon.javago;

import java.io.File;
import java.util.Map;
import java.util.Observer;
import java.util.Optional;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import sk.hor1zon.javago.models.InitModel;
import sk.hor1zon.javago.utils.Settings;
import sk.hor1zon.javago.game.GameType;

public class Menu extends Application implements Observer {
	private Scene mainMenuScene;
	private Scene loadGameScene;
	private Stage ps;
	private Menu ref;

	public Menu() {
		ref = this;
	}

	@Override
	public void start(Stage primaryStage) {
		mainMenuScene = mainMenu();
		loadGameScene = loadGame();
		ps = primaryStage;
		ps.setTitle("JavaGo");
		ps.setScene(mainMenuScene);
		ps.show();

	}

	@Override
	public void update(java.util.Observable obs, Object arg) {
		if ((boolean) arg) {
			ps.setTitle("JavaGo - New");
			ps.setScene(gameSettings(((InitModel) obs).getSettings()));
		} else {
			ps.setTitle("JavaGo - Load");
			ps.setScene(loadGameScene);
		}
	}

	public void showMenu() {
		ps.setTitle("JavaGo");
		ps.setScene(mainMenuScene);
	}

	private Scene gameSettings(Settings settings) {
		Map<String, String> settingsMap = settings.toMap();
		GridPane form = new GridPane();
		form.setAlignment(Pos.TOP_LEFT);
		form.setHgap(10);
		form.setVgap(10);
		form.setPadding(new Insets(25, 25, 25, 25));

		//
		Text typeLabel = new Text("Game Type:");
		typeLabel.setFont(Font.font("Calibri", FontWeight.NORMAL, 16));
		final ToggleGroup type = new ToggleGroup();
		RadioButton typeL = new RadioButton("Local");
		typeL.setUserData("LOCAL");
		typeL.setToggleGroup(type);
		RadioButton typeS = new RadioButton("Server");
		typeS.setUserData("SERVER");
		typeS.setToggleGroup(type);
		RadioButton typeC = new RadioButton("Client");
		typeC.setUserData("CLIENT");
		typeC.setToggleGroup(type);
		switch (settingsMap.get("type")) {
		case "SERVER":
			type.selectToggle(typeS);
			break;
		case "CLIENT":
			type.selectToggle(typeC);
			break;
		default:
			type.selectToggle(typeL);
			break;
		}
		form.add(typeLabel, 0, 0);
		form.add(typeL, 1, 0);
		form.add(typeS, 2, 0);
		form.add(typeC, 3, 0);

		//
		Text handicapLabel = new Text("Handicap:");
		handicapLabel.setFont(Font.font("Calibri", FontWeight.NORMAL, 16));
		ChoiceBox<String> handicap_9_13 = new ChoiceBox<String>(
				FXCollections.observableArrayList("0", "2", "3", "4", "5"));
		ChoiceBox<String> handicap_19 = new ChoiceBox<String>(
				FXCollections.observableArrayList("0", "2", "3", "4", "5", "6", "7", "8", "9"));
		handicap_19.getSelectionModel()
				.select(settingsMap.get("handicap").equals("0") ? 0 : Integer.valueOf(settingsMap.get("handicap")) - 1);
		handicap_9_13.getSelectionModel()
				.select(settingsMap.get("handicap").equals("0") ? 0 : Integer.valueOf(settingsMap.get("handicap")) - 1);
		form.add(handicapLabel, 0, 2);
		form.add(handicap_19, 1, 2);
		form.add(handicap_9_13, 1, 2);

		//
		Text boardLabel = new Text("Board Size:");
		boardLabel.setFont(Font.font("Calibri", FontWeight.NORMAL, 16));
		final ToggleGroup board = new ToggleGroup();
		RadioButton board9 = new RadioButton("9x9");
		board9.setUserData("9");
		board9.setToggleGroup(board);
		RadioButton board13 = new RadioButton("13x13");
		board13.setUserData("13");
		board13.setToggleGroup(board);
		RadioButton board19 = new RadioButton("19x19");
		board19.setUserData("19");
		board19.setToggleGroup(board);
		board.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle oldT, Toggle newT) {
				if (newT.getUserData().equals("19")) {
					handicap_19.setVisible(true);
					handicap_9_13.setVisible(false);
				} else {
					handicap_19.setVisible(false);
					handicap_9_13.setVisible(true);
				}
			}
		});
		switch (settingsMap.get("board")) {
		case "9":
			board.selectToggle(board9);
			break;
		case "13":
			board.selectToggle(board13);
			break;
		default:
			board.selectToggle(board19);
			break;
		}
		form.add(boardLabel, 0, 1);
		form.add(board19, 1, 1);
		form.add(board13, 2, 1);
		form.add(board9, 3, 1);

		//
		Text byoyomiLabel = new Text("Byoyomi:");
		byoyomiLabel.setFont(Font.font("Calibri", FontWeight.NORMAL, 16));
		TextField byoyomi = new TextField();
		byoyomi.setText(settingsMap.get("byoyomi"));
		byoyomi.setMinWidth(20);
		byoyomi.setPrefWidth(20);
		form.add(byoyomiLabel, 2, 2);
		form.add(byoyomi, 3, 2);

		//
		Text komiLabel = new Text("Komi:");
		komiLabel.setFont(Font.font("Calibri", FontWeight.NORMAL, 16));
		/*
		 * Slider komi = new Slider(-9.5,9.5,Double.valueOf(settingsMap.get("komi")));
		 * komi.setShowTickLabels(true); komi.setShowTickMarks(true);
		 * komi.setMajorTickUnit(1); komi.setMinorTickCount(1);
		 * komi.setBlockIncrement(.5); komi.setSnapToTicks(true);
		 */
		ListView<String> komi = new ListView<String>();
		ObservableList<String> komiItems = FXCollections.observableArrayList("-9.5", "-9.0", "-8.5", "-8.0", "-7.5",
				"-7.0", "-6.5", "-6.0", "-5.5", "-5.0", "-4.5", "-4.0", "-3.5", "-3.0", "-2.5", "-2.0", "-1,5", "-1.0",
				"-0.5", "0.0", "0.5", "1.0", "1.5", "2.0", "2.5", "3.0", "3.5", "4.0", "4.5", "5.0", "5.5", "6.0",
				"6.5", "7.0", "7.5", "8.0", "8.5", "9.0", "9.5");
		komi.setItems(komiItems);
		komi.setPrefHeight(40);
		komi.setOrientation(Orientation.HORIZONTAL);
		komi.scrollTo(settingsMap.get("komi"));
		komi.getSelectionModel().select(settingsMap.get("komi"));
		form.add(komiLabel, 0, 3);
		form.add(komi, 1, 3, 3, 1);

		//
		Text colorLabel = new Text("Player1 color:");
		colorLabel.setFont(Font.font("Calibri", FontWeight.NORMAL, 16));
		final ToggleGroup color = new ToggleGroup();
		RadioButton colorB = new RadioButton("Black");
		colorB.setUserData("black");
		colorB.setToggleGroup(color);
		RadioButton colorW = new RadioButton("White");
		colorW.setUserData("white");
		colorW.setToggleGroup(color);
		RadioButton colorN = new RadioButton("Nigiri");
		colorN.setUserData("nigiri");
		colorN.setToggleGroup(color);
		switch (settingsMap.get("playerColor")) {
		case "white":
			color.selectToggle(colorW);
			break;
		case "nigiri":
			color.selectToggle(colorN);
			break;
		default:
			color.selectToggle(colorB);
			break;
		}
		form.add(colorLabel, 0, 4);
		form.add(colorB, 1, 4);
		form.add(colorW, 2, 4);
		form.add(colorN, 3, 4);

		//
		Text rulesLabel = new Text("Rules:");
		rulesLabel.setFont(Font.font("Calibri", FontWeight.NORMAL, 16));
		final ToggleGroup rules = new ToggleGroup();
		RadioButton rulesJ = new RadioButton("Japanese");
		rulesJ.setUserData("japanese");
		rulesJ.setToggleGroup(rules);
		RadioButton rulesC = new RadioButton("Chinese");
		rulesC.setUserData("chinese");
		rulesC.setToggleGroup(rules);
		if (settingsMap.get("rules").equals("chinese")) {
			rules.selectToggle(rulesC);
		} else {
			rules.selectToggle(rulesJ);
		}
		form.add(rulesLabel, 0, 5);
		form.add(rulesJ, 1, 5);
		form.add(rulesC, 2, 5);

		// Local Dialog.
		Dialog<Pair<String, String>> dialogL = new Dialog<>();
		dialogL.setTitle("Local Multiplayer");
		dialogL.setHeaderText("Choose your names");

		// Set the button types.
		ButtonType okType = new ButtonType("OK", ButtonData.OK_DONE);
		dialogL.getDialogPane().getButtonTypes().addAll(okType, ButtonType.CANCEL);

		GridPane dialogLGrid = new GridPane();
		dialogLGrid.setHgap(10);
		dialogLGrid.setVgap(10);
		dialogLGrid.setPadding(new Insets(20, 10, 10, 10));

		// Create the labels and fields.
		TextField player1L = new TextField();
		player1L.setPromptText("Player 1");
		player1L.setText(settingsMap.get("player1"));
		TextField player2L = new TextField();
		player2L.setPromptText("Player 2");
		player2L.setText(settingsMap.get("player2"));

		dialogLGrid.add(new Label("Player 1:"), 0, 0);
		dialogLGrid.add(player1L, 1, 0);
		dialogLGrid.add(new Label("Player 2:"), 0, 1);
		dialogLGrid.add(player2L, 1, 1);
		dialogLGrid.add(new ImageView(this.getClass().getResource("res/img/multi_local.png").toString()), 2, 0, 1, 2);

		dialogL.getDialogPane().setContent(dialogLGrid);

		// Convert the result to a username-password-pair when the login button is
		// clicked.
		dialogL.setResultConverter(dialogButton -> {
			if (dialogButton == okType) {
				return new Pair<>(player1L.getText(), player2L.getText());
			}
			return null;
		});

		// Server Dialog.
		Dialog<Pair<String, String>> dialogS = new Dialog<>();
		dialogS.setTitle("Server Multiplayer");
		dialogS.setHeaderText("Choose your name and port");

		// Set the button types.
		dialogS.getDialogPane().getButtonTypes().addAll(okType, ButtonType.CANCEL);

		GridPane dialogSGrid = new GridPane();
		dialogSGrid.setHgap(10);
		dialogSGrid.setVgap(10);
		dialogSGrid.setPadding(new Insets(20, 10, 10, 10));

		// Create the labels and fields.
		TextField player1S = new TextField();
		player1S.setPromptText("Player 1");
		player1S.setText(settingsMap.get("player1"));
		TextField portS = new TextField();
		portS.setPromptText("1-65535");
		portS.setText(settingsMap.get("port"));

		dialogSGrid.add(new Label("Player 1:"), 0, 0);
		dialogSGrid.add(player1S, 1, 0);
		dialogSGrid.add(new Label("Server Port:"), 0, 1);
		dialogSGrid.add(portS, 1, 1);
		dialogSGrid.add(new ImageView(this.getClass().getResource("res/img/multi_online.png").toString()), 2, 0, 1, 2);

		dialogS.getDialogPane().setContent(dialogSGrid);

		// Convert the result to a username-password-pair when the login button is
		// clicked.
		dialogS.setResultConverter(dialogButton -> {
			if (dialogButton == okType) {
				return new Pair<>(player1S.getText(), portS.getText());
			}
			return null;
		});

		// Client Dialog.
		Dialog<Pair<String, String>> dialogC = new Dialog<>();
		dialogC.setTitle("Client Multiplayer");
		dialogC.setHeaderText("Choose your name, ip and port");

		// Set the button types.
		dialogC.getDialogPane().getButtonTypes().addAll(okType, ButtonType.CANCEL);

		GridPane dialogCGrid = new GridPane();
		dialogCGrid.setHgap(10);
		dialogCGrid.setVgap(10);
		dialogCGrid.setPadding(new Insets(20, 10, 10, 10));

		// Create the labels and fields.
		TextField player2C = new TextField();
		player2C.setPromptText("Player 2");
		player2C.setText(settingsMap.get("player2"));
		TextField portC = new TextField();
		portC.setPromptText("1-65535");
		portC.setText(settingsMap.get("port"));
		TextField ip = new TextField();
		ip.setPromptText("IPv4 / IPv6");
		ip.setText(settingsMap.get("ip"));

		dialogCGrid.add(new Label("Player 2:"), 0, 0);
		dialogCGrid.add(player2C, 1, 0);
		dialogCGrid.add(new Label("Connect to:"), 0, 1);
		dialogCGrid.add(ip, 1, 1);
		dialogCGrid.add(new Label("Port:"), 0, 2);
		dialogCGrid.add(portC, 1, 2);
		dialogCGrid.add(new ImageView(this.getClass().getResource("res/img/multi_online.png").toString()), 2, 0, 1, 3);

		dialogC.getDialogPane().setContent(dialogCGrid);

		// Convert the result to a username-password-pair when the login button is
		// clicked.
		dialogC.setResultConverter(dialogButton -> {
			if (dialogButton == okType) {
				return new Pair<>(player2C.getText(), ip.getText() + ";" + portC.getText());
			}
			return null;
		});

		///
		Button multiBtn = new Button("Multiplayer Settings");
		multiBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// Request focus on the player1 field by default.
				Platform.runLater(() -> player1L.requestFocus());
				Platform.runLater(() -> player1S.requestFocus());
				Platform.runLater(() -> player2C.requestFocus());
				switch ((String) type.getSelectedToggle().getUserData()) {
				case "LOCAL":
					dialogL.showAndWait().ifPresent(players -> {
						settingsMap.replace("player1", players.getKey());
						settingsMap.replace("player2", players.getValue());
					});
					break;
				case "SERVER":
					dialogS.showAndWait().ifPresent(playerPort -> {
						settingsMap.replace("player1", playerPort.getKey());
						settingsMap.replace("port", playerPort.getValue());
					});
					break;
				case "CLIENT":
					dialogC.showAndWait().ifPresent(playerIPPort -> {
						System.out.println(playerIPPort.getValue());
						String[] ipPort = playerIPPort.getValue().split(";");
						settingsMap.replace("player2", playerIPPort.getKey());
						settingsMap.replace("ip", ipPort[0]);
						settingsMap.replace("port", ipPort[1]);
					});
					break;
				}

			}
		});
		form.add(multiBtn, 0, 6, 2, 1);

		//
		Button saveBtn = new Button();
		saveBtn.setText("Set as Default");
		saveBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO
				settingsMap.replace("type", (String) type.getSelectedToggle().getUserData());
				settingsMap.replace("board", (String) board.getSelectedToggle().getUserData());
				settingsMap.replace("handicap",
						handicap_19.isVisible() ? handicap_19.getValue() : handicap_9_13.getValue());
				settingsMap.replace("komi", komi.getSelectionModel().getSelectedItem());
				settingsMap.replace("byoyomi", byoyomi.getText());
				settingsMap.replace("playerColor", (String) color.getSelectedToggle().getUserData());
				settingsMap.replace("rules", (String) rules.getSelectedToggle().getUserData());
				if (settings.parseMap(settingsMap)) {
					if (settings.write()) {
						form.add(new ImageView(this.getClass().getResource("res/img/ok_tick.png").toString()), 1, 7);
					} else {
						form.add(new ImageView(this.getClass().getResource("res/img/ko_cross.png").toString()), 1, 7);
					}
				} else {
					form.add(new ImageView(this.getClass().getResource("res/img/ko_cross.png").toString()), 1, 7);

				}
			}
		});
		form.add(saveBtn, 0, 7);

		//
		Button backBtn = new Button();
		backBtn.setText("<< Back");
		backBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				showMenu();
			}
		});
		form.add(backBtn, 2, 7);

		//
		Button startBtn = new Button();
		startBtn.setText("Start!");
		startBtn.setId("startBtn");
		startBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Init.initGame(settings);
				ps.close();
			}
		});
		form.add(startBtn, 3, 7);

		//
		//
		Text header = new Text("Game setup");
		header.setFont(Font.font("Calibri", FontWeight.NORMAL, 32));
		BorderPane bpane = new BorderPane();
		bpane.setId("settings");
		BorderPane.setAlignment(header, Pos.CENTER);
		BorderPane.setMargin(header, new Insets(10, 0, 10, 0));
		bpane.setTop(header);
		BorderPane.setAlignment(form, Pos.TOP_LEFT);
		bpane.setCenter(form);

		Scene scene = new Scene(bpane, 384, 384);
		scene.getStylesheets().add(getClass().getResource("res/css/menu.css").toExternalForm());

		return scene;
	}

	private Scene loadGame() {
		Alert nafDialog = new Alert(AlertType.ERROR);
		nafDialog.setTitle("Error");
		nafDialog.setHeaderText("Not a File!");
		nafDialog.setContentText("The path you entered does not correspond to a valid file or is unreadable!");

		TextField filePath = new TextField();
		filePath.setPrefWidth(240);
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Game File");
		// fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JavaGo save", "*.jgo"));
		Button fileOpenBtn = new Button("Open...");

		fileOpenBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				File file = fileChooser.showOpenDialog(ps);
				if (file != null) {
					filePath.setText(file.getAbsolutePath());
				}
			}
		});

		Button loadBtn = new Button("Load!");
		loadBtn.setId("loadBtn");
		loadBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				File file = new File(filePath.getText());
				if (file != null && file.canRead() && file.getName().endsWith(".jgo")) {
					Init.initGame(file);
					ps.close();
				} else {
					nafDialog.showAndWait();
				}
			}
		});

		Button backBtn = new Button();
		backBtn.setText("<< Back");
		backBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				showMenu();
			}
		});

		HBox fileBox = new HBox(5);
		fileBox.getChildren().addAll(filePath, fileOpenBtn);
		fileBox.setAlignment(Pos.CENTER);

		HBox buttonBox = new HBox(5);
		buttonBox.getChildren().addAll(backBtn, loadBtn);
		buttonBox.setAlignment(Pos.CENTER_RIGHT);

		Text header = new Text("Load Game");
		header.setFont(Font.font("Calibri", FontWeight.NORMAL, 32));

		BorderPane bpane = new BorderPane();
		bpane.setId("loadBP");
		BorderPane.setAlignment(header, Pos.CENTER);
		BorderPane.setMargin(header, new Insets(10, 0, 10, 0));
		BorderPane.setMargin(buttonBox, new Insets(15, 20, 15, 20));
		bpane.setTop(header);
		bpane.setCenter(fileBox);
		bpane.setBottom(buttonBox);

		Scene scene = new Scene(bpane, 384, 384);
		scene.getStylesheets().add(getClass().getResource("res/css/menu.css").toExternalForm());

		return scene;
	}

	private Scene mainMenu() {
		Button newBtn = new Button();
		newBtn.setText("New Game");
		newBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Init.initModel(true, ref);
			}
		});
		Button loadBtn = new Button();
		loadBtn.setText("Load Game");
		loadBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Init.initModel(false, ref);
			}
		});
		Text header = new Text("JavaGo");
		header.setFont(Font.font("Calibri", FontWeight.NORMAL, 42));

		VBox buttons = new VBox(5);
		buttons.getChildren().addAll(newBtn, loadBtn);
		buttons.setAlignment(Pos.CENTER);

		BorderPane bpane = new BorderPane();
		bpane.setId("menu");
		BorderPane.setAlignment(header, Pos.CENTER);
		BorderPane.setMargin(header, new Insets(10, 0, 10, 0));
		bpane.setTop(header);
		bpane.setCenter(buttons);

		Scene scene = new Scene(bpane, 384, 384);
		scene.getStylesheets().add(getClass().getResource("res/css/menu.css").toExternalForm());

		return scene;
	}
}
