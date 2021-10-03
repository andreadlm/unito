package graph;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * Runner for the unit tests.
 *
 * @author Andrea Delmastro.
 */
public class KruskalTest_Runner {
  public static void main(String[] args) {
    Result result = JUnitCore.runClasses(KruskalTest.class);
    System.out.println("== Kruskal class tests ==\n" +
        "Run: " + result.getRunCount() + "\n" +
        "Failures: " + result.getFailureCount() + "\n" +
        "Successes: " + (result.getRunCount() - result.getFailureCount()));
    for (Failure failure : result.getFailures()) {
      System.out.println(failure.toString());
    }
  }
}