package sk.hor1zon.javago.test.boardcanvas;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import sk.hor1zon.javago.game.Stone;
import sk.hor1zon.javago.game.boards.BoardCanvas;

public class AdjacentTest {
	BoardCanvas bc;

	@Before
	public void setUp() {
		bc = new BoardCanvas(9);
	}
	
	@Test
	public void testFor2() {
		Stone[] result = bc.getAdjacent(0, 0);
		assertEquals(2, result.length);
	}
	@Test
	public void testFor3() {
		Stone[] result = bc.getAdjacent(5, 8);
		assertEquals(3, result.length);
	}
	@Test
	public void testFor4() {
		Stone[] result = bc.getAdjacent(4, 7);
		assertEquals(4, result.length);
	}
}
