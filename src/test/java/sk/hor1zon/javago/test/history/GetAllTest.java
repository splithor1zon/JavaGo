package sk.hor1zon.javago.test.history;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.mockito.junit.MockitoJUnitRunner;

import javafx.beans.binding.When;
import sk.hor1zon.javago.game.History;
import sk.hor1zon.javago.game.Stone;
import sk.hor1zon.javago.game.boards.BoardCanvas;

@RunWith(MockitoJUnitRunner.class)
public class GetAllTest {
	@Mock
	Stone s1;
	
	@Mock
	Stone s2;
	
	@Mock
	Stone s3;
	
	History h = History.getRef();;
	@Before
	public void setUp() {
		History.resetHistory();
	}
	
	@Test
	public void testAdd3Stones() {
		when(s1.getId()).thenReturn(3);
		when(s2.getId()).thenReturn(1);
		when(s3.getId()).thenReturn(2);
		h.addStone(s1);
		h.addStone(s2);
		h.addStone(s3);
		ArrayList<Stone> res = h.getAllStones();
		assertEquals(s2, res.get(0));
		assertEquals(s3, res.get(1));
		assertEquals(s1, res.get(2));
		assertEquals(3, res.size());
	}
	@Test
	public void testAdd2Stones1null() {
		when(s1.getId()).thenReturn(3);
		when(s2.getId()).thenReturn(1);
		h.addStone(s1);
		h.addPass();
		h.addStone(s2);
		ArrayList<Stone> res = h.getAllStones();
		assertEquals(s2, res.get(0));
		assertEquals(s1, res.get(1));
		assertEquals(2, res.size());
	}
	@Test
	public void testAdd2null() {
		h.addPass();
		h.addPass();
		ArrayList<Stone> res = h.getAllStones();
		assertEquals(0, res.size());
	}
}
