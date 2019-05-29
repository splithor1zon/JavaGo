package sk.hor1zon.javago.test.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import sk.hor1zon.javago.test.history.GetActiveCountTest;
import sk.hor1zon.javago.test.history.GetAllTest;
import sk.hor1zon.javago.test.history.GetLatestColorTest;
import sk.hor1zon.javago.test.history.GetLatestPrisonerTest;
import sk.hor1zon.javago.test.history.GetLatestTest;
import sk.hor1zon.javago.test.history.GetPrisonerCountTest;
import sk.hor1zon.javago.test.history.GetStoneCountTest;
import sk.hor1zon.javago.test.history.IsPrisonerTest;
import sk.hor1zon.javago.test.history.MoveStoneToPrisonTest;
import sk.hor1zon.javago.test.history.MoveStonesToPrisonTest;

@RunWith(Suite.class)

@Suite.SuiteClasses({ GetAllTest.class, GetLatestTest.class, GetPrisonerCountTest.class, GetActiveCountTest.class,
		GetStoneCountTest.class, MoveStoneToPrisonTest.class, MoveStonesToPrisonTest.class, GetLatestColorTest.class,
		GetLatestPrisonerTest.class, IsPrisonerTest.class })

public class HistoryTestSuite {

}
