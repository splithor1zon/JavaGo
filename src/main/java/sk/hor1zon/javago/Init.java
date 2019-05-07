package sk.hor1zon.javago;

import sk.hor1zon.javago.game.*;

public class Init {
	public static void main(String[] args) {
		new Thread() {
            @Override
            public void run() {
                javafx.application.Application.launch(Menu.class);
            }
        }.start();
	}

	public static void initModel(GameStatus status, Menu menu) {
		GameModel gm = new GameModel(status);
		gm.addObserver(menu);
		gm.setSettings(Settings.loadSettings());
	}
}
