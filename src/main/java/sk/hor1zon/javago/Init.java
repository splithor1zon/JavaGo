package sk.hor1zon.javago;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Init {
	public static void main( String[] args ) {
		//Settings loader
		ObjectMapper objectMapper = new ObjectMapper();
		Settings settings = new Settings();
		try {
			settings = objectMapper.readValue(
					new File("settings.json"), Settings.class
					);
			System.out.println("Settings loaded!");
		} catch (Exception e) {
			System.err.println("Cannot read settings, using defaults.");
			settings.useDefaults();
		}
		new sk.hor1zon.javago.client.Controller();
	}
}
