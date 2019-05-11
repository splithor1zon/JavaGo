package sk.hor1zon.javago.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import sk.hor1zon.javago.game.Game;
import sk.hor1zon.javago.game.Stone;

public class GameDeserializer implements JsonDeserializer<Game> {

	@Override
	public Game deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		List<Stone> active = new ArrayList<Stone>();
		JsonObject jobject = json.getAsJsonObject();
		JsonArray act = jobject.getAsJsonArray("active");
		JsonArray pri = jobject.getAsJsonArray("prison");
		for (JsonElement jsonElement : act) {
			JsonObject stone = jsonElement.getAsJsonObject();
			

		}
		return null;
	}

}
