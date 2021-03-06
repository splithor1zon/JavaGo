package sk.hor1zon.javago.test.history;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sk.hor1zon.javago.game.History;
import sk.hor1zon.javago.game.Stone;
import sk.hor1zon.javago.game.StoneColor;

@RunWith(MockitoJUnitRunner.Silent.class)
public class MoveStoneToPrisonTest {
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
		when(s1.getColor()).thenReturn(StoneColor.WHITE);
		when(s2.getColor()).thenReturn(StoneColor.WHITE);
		when(s3.getColor()).thenReturn(StoneColor.WHITE);
		h.addStone(s1);
		h.addStone(s2);
		h.addStone(s3);
		long res = h.getPrisonerCount(StoneColor.WHITE);
		assertEquals(0, res);
	}

	@Test
	public void testAdd3prisonMixedColor() {
		when(s1.getColor()).thenReturn(StoneColor.WHITE);
		when(s2.getColor()).thenReturn(StoneColor.WHITE);
		when(s3.getColor()).thenReturn(StoneColor.BLACK);
		h.addStone(s1);
		h.addStone(s2);
		h.addStone(s3);
		h.moveStoneToPrison(s1);
		h.moveStoneToPrison(s2);
		h.moveStoneToPrison(s3);
		long res = h.getPrisonerCount(StoneColor.BLACK);
		assertEquals(1, res);
	}

	@Test
	public void testAdd1Stone2prison1null() {
		when(s1.getColor()).thenReturn(StoneColor.WHITE);
		when(s2.getColor()).thenReturn(StoneColor.WHITE);
		when(s3.getColor()).thenReturn(StoneColor.WHITE);
		h.addStone(s1);
		h.addStone(s2);
		h.addStone(s3);
		h.moveStoneToPrison(s1);
		h.moveStoneToPrison(s3);
		h.moveStoneToPrison(null);
		long res = h.getPrisonerCount(StoneColor.WHITE);
		assertEquals(2, res);
	}
}
