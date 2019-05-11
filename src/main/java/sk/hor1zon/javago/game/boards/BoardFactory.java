package sk.hor1zon.javago.game.boards;

public class BoardFactory {

	public static Board initBoard(int size) {
		switch (size) {
		case 13:
			return new Board13();
		case 9:
			return new Board9();
		default:
			return new Board19();
		}
	}
}
