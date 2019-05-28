package sk.hor1zon.javago;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import sk.hor1zon.javago.test.suites.*;

public class TestRunner {

	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(HistoryTestSuite.class, BoardCanvasTestSuite.class);

		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}

		System.out.println(result.wasSuccessful());
	}

}
