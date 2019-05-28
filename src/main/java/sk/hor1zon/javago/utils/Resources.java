package sk.hor1zon.javago.utils;

import java.net.URL;

/**
 * Helper class, translates string paths to URL.
 * @author splithor1zon
 *
 */
public class Resources {
	/**
	 * Returns URL of provided path.
	 * @param resPath Provided string path.
	 * @return URL of provided path.
	 */
	public static URL getURLof(String resPath) {
		return Resources.class.getClassLoader().getResource(resPath);

	}
	
	/**
	 * Returns URL of provided filename of image.
	 * @param filename Filename of image.
	 * @return URL of provided filename of image.
	 */
	public static URL getURLofImage(String filename) {
		return getURLof("img/" + filename);
	}
}
