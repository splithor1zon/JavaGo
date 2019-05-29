package sk.hor1zon.javago.test.history;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sk.hor1zon.javago.game.History;
import sk.hor1zon.javago.game.Stone;

@RunWith(MockitoJUnitRunner.class)
public class GetStoneCountTest {

	@Mock
	Stone s1;

	@Mock
	Stone s2;

	@Mock
	Stone s3;

	History h = History.getRef();

	@Before
	public void setUp() {
		History.resetHistory();
	}

	@Test
	public void testAdd3Stones() {
		h.addStone(s1);
		h.addStone(s2);
		h.addStone(s3);
		long res = h.getStoneCount();
		assertEquals(3, res);
	}

	@Test
	public void testAdd2Stones1prison() {
		h.addStone(s1);
		h.addStone(s2);
		h.addStone(s3);
		h.moveStoneToPrison(s2);
		long res = h.getStoneCount();
		assertEquals(3, res);
	}

	@Test
	public void testAdd1Stone1prison1null() {
		h.addPass();
		h.addStone(s1);
		h.addStone(s2);
		h.moveStoneToPrison(s1);
		long res = h.getStoneCount();
		assertEquals(2, res);
	}
}
