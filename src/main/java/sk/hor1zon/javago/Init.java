package sk.hor1zon.javago;

import java.io.File;
import sk.hor1zon.javago.models.InitModel;
import sk.hor1zon.javago.utils.Settings;

public class Init {
	public static void main(String[] args) {
		new Thread() {
			@Override
			public void run() {
				javafx.application.Application.launch(Menu.class);
			}
		}.start();
	}

	public static void initModel(boolean isNew, Menu menu) {
		InitModel im = new InitModel(isNew);
		im.addObserver(menu);
		if (isNew)
			im.setSettings(Settings.load());
		im.publishChanges();
	}

	public static void initGame(Settings settings) {
		
	}

	public static void initGame(File game) {

	}
}
