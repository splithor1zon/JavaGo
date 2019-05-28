package sk.hor1zon.javago.game;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import com.google.gson.Gson;
import sk.hor1zon.javago.game.boards.BoardCanvas;
import sk.hor1zon.javago.models.GameModel;
import sk.hor1zon.javago.models.Model;
import sk.hor1zon.javago.utils.Settings;
import sk.hor1zon.javago.views.GameView;

public class NetworkController implements ControllerIntf {
	private Model model;
	private boolean previousPass;
	private final int LOCAL_PLAYER;
	private final int REMOTE_PLAYER;
	private final StoneColor LOCAL_PLAYER_COLOR;
	private static BufferedReader remoteReader;
	private static PrintWriter remoteWriter;

	public NetworkController(Model m) {
		model = m;
		previousPass = false;
		REMOTE_PLAYER = Settings.currentRef.type == GameType.SERVER ? 2 : 1;
		LOCAL_PLAYER = Settings.currentRef.type == GameType.SERVER ? 1 : 2;
		LOCAL_PLAYER_COLOR = (LOCAL_PLAYER == 1
				? (Settings.currentRef.playerColor == "white" ? StoneColor.WHITE : StoneColor.BLACK)
				: (Settings.currentRef.playerColor == "white" ? StoneColor.BLACK : StoneColor.WHITE));
	}

	@Override
	public void placeStone(int x, int y) {
		if (BoardCanvas.isOccupied(x, y)) {
			model.alert(GameAlert.OCCUPIED);
		} else {
			Stone newStone = new Stone(LOCAL_PLAYER_COLOR, x, y);
			if (!BoardCanvas.hasGroupLiberty(newStone)) {
				if (BoardCanvas.koApplicable(newStone)) {
					if (BoardCanvas.ko(newStone)) {
						model.alert(GameAlert.KO);
					} else {
						model.placeStoneKo(newStone);
						previousPass = false;
						changePlayer(newStone);
					}
				} else if (BoardCanvas.resultingGroupLiberty(newStone) > 0) {
					model.placeStoneKo(newStone);
					changePlayer(newStone);
				} else {
					model.alert(GameAlert.SUICIDE);
				}
			} else {
				model.placeStone(newStone);
				previousPass = false;
				changePlayer(newStone);
			}
		}
	}

	public void placeRemoteStone(Stone rStone) {
		if (!BoardCanvas.hasGroupLiberty(rStone)) {
			if (BoardCanvas.koApplicable(rStone)) {
				if (!BoardCanvas.ko(rStone)) {
					model.placeStoneKo(rStone);
				}
			}
		} else {
			model.placeStone(rStone);
		}
	}

	@Override
	public void pass() {
		if (previousPass) {
			GameModel.shutdown();
			model.negotiate(); // rework
			return;
		} else {
			previousPass = true;
		}
		model.pass();
		changePlayer(null);
	}

	@Override
	public void resultNegotiation(boolean conclusion, double player1score, double player2score) {
		if (Settings.currentRef.playerColor == "white") {
			player1score += Settings.currentRef.komi;
		} else {
			player2score += Settings.currentRef.komi;
		}
		if (conclusion) {
			model.finish(player1score, player2score);
		} else {
			model.startTimer();
			model.pass();
			changePlayer(null);
		}
	}

	@Override
	public void removedNotice(ArrayList<Stone> removed) {
		model.updatePrisoners(removed);
	}

	@Override
	public void viewReady(GameView gv) {
		((GameModel) model).addObserver(gv);
		model.startTimer();
		model.setHandicap();
	}

	@Override
	public void saveGame(File file) {
		Game game = new Game();
		game.write(file);
	}

	private void changePlayer(Stone placedStone) {
		model.changePlayer(REMOTE_PLAYER);
		GameStep step = new GameStep(placedStone);
		Gson gson = new Gson();
		remoteWriter.println(gson.toJson(step));
		System.out.println("asd");
		String result;
		try {
			result = remoteReader.readLine();
		} catch (IOException e) {
			result = null;
			e.printStackTrace();
		}
		GameStep resStep = gson.fromJson(result, GameStep.class);
		if (resStep.isOffer) {
			//TODO
		} else {
			placeRemoteStone(placedStone);
		}
		model.changePlayer(LOCAL_PLAYER);
	}

	public static Game clientHandshake() {
		// init
		Socket client;
		InputStream is;
		OutputStream os;
		try {
			client = new Socket(Settings.currentRef.ip, Settings.currentRef.port);
			is = client.getInputStream();
			os = client.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		remoteReader = new BufferedReader(new InputStreamReader(is));
		remoteWriter = new PrintWriter(os, true);
		// get
		String json;
		try {
			json = remoteReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		Game game = new Gson().fromJson(json, Game.class);
		game.settings.ip = Settings.currentRef.ip;
		game.settings.player2 = Settings.currentRef.player2;
		game.settings.playerColor = game.settings.playerColor == "white" ? "black" : "white";
		remoteWriter.println("ACK");
		return game;
	}

	public static void serverHandshake() {
		// init
		ServerSocket server;
		Socket client;
		InputStream is;
		OutputStream os;
		try {
			server = new ServerSocket(Settings.currentRef.port);
			client = server.accept();
			is = client.getInputStream();
			os = client.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		remoteReader = new BufferedReader(new InputStreamReader(is));
		remoteWriter = new PrintWriter(os, true);
		// send-ack
		Game game = new Game();
		game.settings.type = GameType.CLIENT;
		remoteWriter.println(new Gson().toJson(game));
		game.settings.type = GameType.SERVER;
		try {
			String a = remoteReader.readLine();
			if (a == "ACK") {
				System.out.println("Connection established!");
			} else {
				System.err.println("Connection error!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
