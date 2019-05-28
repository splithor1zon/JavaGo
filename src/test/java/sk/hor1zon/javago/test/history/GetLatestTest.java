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

@RunWith(MockitoJUnitRunner.class)
public class GetLatestTest {

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
		h.addStone(s1);
		h.addStone(s2);
		h.addStone(s3);
		Stone res = h.getLatest();
		assertEquals(s3, res);
	}
	@Test
	public void testAdd2Stones1prisoner() {
		h.addStone(s1);
		h.addStone(s2);
		h.addStone(s3);
		h.moveStoneToPrison(s2);
		Stone res = h.getLatest();
		assertEquals(s3, res);
	}
	@Test
	public void testAdd1Stone2null() {
		h.addPass();
		h.addStone(s3);
		h.addPass();
		Stone res = h.getLatest();
		assertEquals(s3, res);
	}
}
