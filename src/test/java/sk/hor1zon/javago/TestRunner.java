package sk.hor1zon.javago;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import sk.hor1zon.javago.test.suites.BoardCanvasTestSuite;
import sk.hor1zon.javago.test.suites.HistoryTestSuite;

public class TestRunner {

	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(HistoryTestSuite.class, BoardCanvasTestSuite.class);

		for (Failure failure : result.getFailures()) {
			System.err.println(failure.toString());
		}

		if (result.wasSuccessful()) {
			System.out.println("\nAll tests passed.");
		} else {
			System.err.println("\nThere were some errors while running tests.");
		}
		
	}

}
