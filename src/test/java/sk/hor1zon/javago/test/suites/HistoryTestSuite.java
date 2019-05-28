package sk.hor1zon.javago.test.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import sk.hor1zon.javago.test.history.*;

@RunWith(Suite.class)

@Suite.SuiteClasses({
   GetAllTest.class,
   GetLatestTest.class,
   GetPrisonerCountTest.class,
   GetActiveCountTest.class,
   GetStoneCountTest.class,
   MoveStoneToPrisonTest.class,
   MoveStonesToPrisonTest.class,
   GetLatestColorTest.class,
   IsPrisonerTest.class
})

public class HistoryTestSuite {

}
