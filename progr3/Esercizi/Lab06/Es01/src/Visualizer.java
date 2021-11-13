import java.util.List;

public class Visualizer {
  public void visualize(List<Integer> list) {
    for(Integer i : list)
      System.out.print("[" + i + "] ");
    System.out.println();
  }
}
