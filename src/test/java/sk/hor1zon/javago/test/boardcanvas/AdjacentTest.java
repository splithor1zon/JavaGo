package sk.hor1zon.javago.test.boardcanvas;

import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import sk.hor1zon.javago.game.Stone;
import sk.hor1zon.javago.game.boards.BoardCanvas;

public class AdjacentTest {
	static BoardCanvas b;

	@BeforeClass
	public static void setUp() {
		b = new BoardCanvas(9);
	}

	@Test
	public void testFor2() {
		Stone[] result = BoardCanvas.getAdjacent(0, 0);
		assertEquals(2, result.length);
	}

	@Test
	public void testFor3() {
		Stone[] result = BoardCanvas.getAdjacent(5, 8);
		assertEquals(3, result.length);
	}

	@Test
	public void testFor4() {
		Stone[] result = BoardCanvas.getAdjacent(4, 7);
		assertEquals(4, result.length);
	}
}
