package sk.hor1zon.javago.test.history;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import sk.hor1zon.javago.game.History;
import sk.hor1zon.javago.game.Stone;
import sk.hor1zon.javago.game.StoneColor;

@RunWith(MockitoJUnitRunner.class)
public class MoveStonesToPrisonTest {
	@Mock
	Stone s1;
	
	@Mock
	Stone s2;
	
	@Mock
	Stone s3;
	
	History h = History.getRef();
	ArrayList<Stone> ar;
	@Before
	public void setUp() {
		History.resetHistory();
		ar = new ArrayList<Stone>();
	}
	
	@Test
	public void testAdd3Stones() {
		when(s1.getColor()).thenReturn(StoneColor.WHITE);
		when(s2.getColor()).thenReturn(StoneColor.WHITE);
		when(s3.getColor()).thenReturn(StoneColor.WHITE);
		h.addStone(s1);
		h.addStone(s2);
		h.addStone(s3);
		h.moveStonesToPrison(ar);
		long res = h.getPrisonerCount(StoneColor.WHITE);
		assertEquals(0, res);
	}
	@Test
	public void testAdd3prisonMixedColor() {
		when(s1.getColor()).thenReturn(StoneColor.WHITE);
		when(s2.getColor()).thenReturn(StoneColor.BLACK);
		when(s3.getColor()).thenReturn(StoneColor.BLACK);
		h.addStone(s1);
		h.addStone(s2);
		h.addStone(s3);
		ar.add(s1);
		ar.add(s2);
		ar.add(s3);
		h.moveStonesToPrison(ar);
		long res = h.getPrisonerCount(StoneColor.BLACK);
		assertEquals(2, res);
	}
	@Test
	public void testAdd1Stone2prison1null() {
		when(s1.getColor()).thenReturn(StoneColor.WHITE);
		when(s2.getColor()).thenReturn(StoneColor.WHITE);
		when(s3.getColor()).thenReturn(StoneColor.WHITE);
		h.addStone(s1);
		h.addStone(s2);
		h.addStone(s3);
		ar.add(s3);
		ar.add(s1);
		ar.add(null);
		h.moveStonesToPrison(ar);
		long res = h.getPrisonerCount(StoneColor.WHITE);
		assertEquals(2, res);
	}
}
