package graph;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * Runner for the unit tests.
 *
 * @author Andrea Delmastro.
 */
public class GraphTest_Runner {
  public static void main(String[] args) {
    Result result = JUnitCore.runClasses(GraphTest.class);
    System.out.println("== Graph class tests ==\n" +
                       "Run: " + result.getRunCount() + "\n" +
                       "Failures: " + result.getFailureCount() + "\n" +
                       "Successes: " + (result.getRunCount() - result.getFailureCount()));
    for (Failure failure : result.getFailures()) {
      System.out.println(failure.toString());
    }
  }
}
