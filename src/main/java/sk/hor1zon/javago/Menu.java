package sk.hor1zon.javago;

import java.io.File;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Menu extends Application {

	@Override
	public void start(Stage primaryStage) {
		Button newBtn = new Button();
		newBtn.setText("New Game");
		newBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("New");
			}
		});
		Button loadBtn = new Button();
		loadBtn.setText("Load Game");
		loadBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("Load");
			}
		});
		Button replayBtn = new Button();
		replayBtn.setText("Replay Game");
		replayBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("Replay");
			}
		});
		Text header = new Text("JavaGo");
		header.setFont(Font.font("Calibri", FontWeight.NORMAL, 42));
		
		VBox buttons = new VBox(5);
		buttons.getChildren().addAll(newBtn, loadBtn, replayBtn);
		buttons.setAlignment(Pos.CENTER);
		
		
		BorderPane bpane = new BorderPane();
		bpane.setId("root");
		BorderPane.setAlignment(header,Pos.CENTER);
		BorderPane.setMargin(header, new Insets(10,0,10,0));
		bpane.setTop(header);
		bpane.setCenter(buttons);
		
		Scene scene = new Scene(bpane, 300, 250);
		scene.getStylesheets().add(getClass().getResource("res/css/menu.css").toExternalForm());
		primaryStage.setTitle("JavaGo");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
