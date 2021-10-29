import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Visualizer implements Observer {
  @Override
  public void update(Observable o, Object arg) {
    if(arg instanceof List) {
      List<Integer> list = (List<Integer>) arg;
      for(Integer e : list)
        System.out.print("[" + e + "] ");
    }
    System.out.print("\n");
  }
}
