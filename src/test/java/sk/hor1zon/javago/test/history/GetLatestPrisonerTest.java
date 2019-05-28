package sk.hor1zon.javago.test.history;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import sk.hor1zon.javago.game.History;
import sk.hor1zon.javago.game.Stone;
import sk.hor1zon.javago.game.StoneColor;

@RunWith(MockitoJUnitRunner.class)
public class GetLatestPrisonerTest {

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
		when(s3.getColor()).thenReturn(StoneColor.BLACK);
		h.addStone(s1);
		h.addStone(s2);
		h.addStone(s3);
		Stone res = h.getLatestPrisoner();
		assertNull(res);
	}
	@Test
	public void testAdd2Stones1prisoner() {
		when(s1.getColor()).thenReturn(StoneColor.BLACK);
		when(s2.getColor()).thenReturn(StoneColor.WHITE);
		when(s3.getColor()).thenReturn(StoneColor.BLACK);
		h.addStone(s1);
		h.addStone(s2);
		h.addStone(s3);
		h.moveStoneToPrison(s3);
		h.moveStoneToPrison(s2);
		Stone res = h.getLatestPrisoner();
		assertEquals(s2, res);
	}
	@Test
	public void testAdd1Stone2null() {
		when(s1.getColor()).thenReturn(StoneColor.BLACK);
		when(s2.getColor()).thenReturn(StoneColor.BLACK);
		when(s3.getColor()).thenReturn(StoneColor.WHITE);
		h.addPass();
		h.addStone(s3);
		h.addStone(s2);
		h.moveStoneToPrison(null);
		h.moveStoneToPrison(s3);
		h.addPass();
		Stone res = h.getLatestPrisoner();
		assertEquals(s3, res);
	}
}
