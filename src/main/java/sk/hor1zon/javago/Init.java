package sk.hor1zon.javago;

import java.io.File;

import javafx.application.Platform;
import javafx.stage.Stage;
import sk.hor1zon.javago.game.ControllerIntf;
import sk.hor1zon.javago.game.Game;
import sk.hor1zon.javago.game.History;
import sk.hor1zon.javago.game.LocalController;
import sk.hor1zon.javago.game.NetworkController;
import sk.hor1zon.javago.game.ReplayController;
import sk.hor1zon.javago.game.Stone;
import sk.hor1zon.javago.models.GameModel;
import sk.hor1zon.javago.models.InitModel;
import sk.hor1zon.javago.models.ReplayModel;
import sk.hor1zon.javago.utils.Settings;
import sk.hor1zon.javago.views.GameView;
import sk.hor1zon.javago.views.ReplayView;

/**
 * This class is the main class of this Application, manages Menu and
 * initialization of the Game.
 * 
 * @author splithor1zon
 *
 */
public class Init {
	private static ControllerIntf c;
	private static GameView gv;
	private static ReplayModel rm;
	private static ReplayController rc;
	private static ReplayView rv;

	/**
	 * Main class, starts Menu GUI.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new Thread() {
			@Override
			public void run() {
				javafx.application.Application.launch(Menu.class);
			}
		}.start();
	}

	/**
	 * After selecting whether to load or create new game the InitModel is
	 * initialized.
	 * 
	 * @param isNew Whether the selected option is New Game.
	 * @param menu  Provide reference to running Menu class instance.
	 */
	public static void initModel(boolean isNew, Menu menu) {
		InitModel im = new InitModel(isNew);
		im.addObserver(menu);
		if (isNew)
			im.setSettings(Settings.load());
		im.publishChanges();
	}

	/**
	 * This method is called only when new game was selected. Creates the MVC
	 * structure of the game itself and starts it.
	 * 
	 * @param isLocal Whether if the game is intended to be local.
	 */
	public static void initGame(boolean isLocal) {
		History.resetHistory();
		Game game = new Game();
		switch (Settings.currentRef.type) {
		case LOCAL:
			c = new LocalController(new GameModel());
			gameParse(game);
			break;
		case SERVER:
			NetworkController.serverHandshake();
			gameParse(game);
			c = new NetworkController(new GameModel());
			break;
		case CLIENT:
			game = NetworkController.clientHandshake();
			gameParse(game);
			c = new NetworkController(new GameModel());
			break;
		default:
			return;
		}

		if (gv != null) {
			try {
				gv.stop();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if (rv != null) {
			try {
				rv.stop();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if (isLocal) {
			game.board = new Stone[Settings.currentRef.board][Settings.currentRef.board];
		}
		gv = new GameView(c, game.board);
		Platform.runLater(new Runnable() {
			public void run() {
				try {
					gv.start(new Stage());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * This method is called only when load game was selected. Loads game from file.
	 * Creates the MVC structure of the game itself and starts it.
	 * 
	 * @param file Saved game file.
	 */
	public static boolean initGame(File file) {
		History.resetHistory();
		Game game = Game.load(file);
		if (game == null) {
			return false;
		}
		gameParse(game);
		if (!game.isFinal) {
			switch (Settings.currentRef.type) {
			case LOCAL:
				c = new LocalController(new GameModel());
				gameParse(game);
				break;
			case SERVER:
				NetworkController.serverHandshake();
				gameParse(game);
				c = new NetworkController(new GameModel());
				break;
			case CLIENT:
				game = NetworkController.clientHandshake();
				gameParse(game);
				c = new NetworkController(new GameModel());
				break;
			default:
				return false;
			}

			if (gv != null) {
				try {
					gv.stop();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			if (rv != null) {
				try {
					rv.stop();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

			gv = new GameView(c, game.board);
			Platform.runLater(new Runnable() {
				public void run() {
					try {
						gv.start(new Stage());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} else {
			rm = new ReplayModel();
			rc = new ReplayController(rm);

			if (gv != null) {
				try {
					gv.stop();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			if (rv != null) {
				try {
					rv.stop();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

			rv = new ReplayView(rc);
			Platform.runLater(new Runnable() {
				public void run() {
					try {
						rv.start(new Stage());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}

		return true;
	}

	private static void gameParse(Game game) {
		if (game == null) {
			return;
		}
		Settings.setSettings(game.settings);
		History.setHistory(game.history);
	}
}
