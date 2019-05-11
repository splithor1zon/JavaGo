package sk.hor1zon.javago.utils;

import java.net.URL;

public class Resources {
	public static URL getURLof(String resPath) {
		return Resources.class.getClassLoader().getResource(resPath);

	}
	public static URL getURLofImage(String filename) {
		return getURLof("img/" + filename);
	}
}
