package sk.hor1zon.javago.game;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import sk.hor1zon.javago.game.boards.BoardCanvas;
import sk.hor1zon.javago.models.GameModel;
import sk.hor1zon.javago.models.Model;
import sk.hor1zon.javago.utils.Settings;
import sk.hor1zon.javago.views.GameView;

/**
 * Provides the functionality for local game.
 * @author splithor1zon
 *
 */
public class LocalController implements ControllerIntf{

	private Model model;
	private int playerTurn = Settings.currentRef.playerColor == "white" ? 1 : 2;
	private boolean whiteTurn = true;
	private boolean player1previousPass = false;
	private boolean player2previousPass = false;
	private static Logger l = Logger.getLogger(LocalController.class.getName());

	public LocalController(Model m) {
		model = m;
	}

	@Override
	public void placeStone(int x, int y) {
		if (BoardCanvas.isOccupied(x, y)) {
			model.alert(GameAlert.OCCUPIED);
		} else {
			Stone newStone = new Stone(whiteTurn ? StoneColor.WHITE : StoneColor.BLACK, x, y);
			if (!BoardCanvas.hasGroupLiberty(newStone)) {
				if (BoardCanvas.koApplicable(newStone)) {
					if (BoardCanvas.ko(newStone)) {
						model.alert(GameAlert.KO);
					} else {
						model.placeStoneKo(newStone);
						if (playerTurn == 1) {
							player1previousPass = false;
						} else {
							player2previousPass = false;
						}
						l.log(Level.INFO, "Stone " + newStone.toString() + " was placed with Ko method.");
						changePlayer();
					}
				} else if (BoardCanvas.resultingGroupLiberty(newStone) > 0) {
					model.placeStoneKo(newStone);
					l.log(Level.INFO, "Stone " + newStone.toString() + " was placed with Ko method.");
					changePlayer();
				} else {
					model.alert(GameAlert.SUICIDE);
				}
			} else {
				model.placeStone(newStone);
				if (playerTurn == 1) {
					player1previousPass = false;
				} else {
					player2previousPass = false;
				}
				l.log(Level.INFO, "Stone " + newStone.toString() + " was placed.");
				changePlayer();
			}
		}
	}

	@Override
	public void pass() {
		if ((player1previousPass && playerTurn == 1) || (player2previousPass && playerTurn == 2)) {
			GameModel.shutdown();
			model.negotiate();
			l.log(Level.INFO, "Double pass, negotiation started.");
			return;
		} else {
			if (playerTurn == 1) {
				player1previousPass = true;
			} else {
				player2previousPass = true;
			}
		}
		model.pass();
		l.log(Level.INFO, "Player " + playerTurn + " passed.");
		changePlayer();
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
			l.log(Level.INFO, "Game finished.");
		} else {
			model.startTimer();
			model.pass();
			changePlayer();
		}
	}
	
	private void changePlayer() {
		playerTurn = playerTurn == 1 ? 2 : 1;
		whiteTurn = !whiteTurn;
		model.changePlayer(playerTurn);
	}

	@Override
	public void removedNotice(ArrayList<Stone> removed) {
		model.updatePrisoners(removed);
	}

	@Override
	public void viewReady(GameView gv) {
		((GameModel)model).addObserver(gv);
		model.startTimer();
		model.setHandicap();
	}
	
	@Override
	public void saveGame(File file) {
		Game game = new Game();
		game.write(file);
	}



}
