import java.util.Observable;
import java.util.Observer;

public class Controller implements Observer {
  private final Counter counter;
  private final Filter filter;
  private final Visualizer visualizer;

  public Controller(Counter counter, Filter filter, Visualizer visualizer) {
    this.counter = counter;
    this.filter = filter;
    this.visualizer = visualizer;

    counter.addObserver(this);
    filter.addObserver(this);
  }

  public void start() {
    counter.start();
  }


  @Override
  public void update(Observable o, Object arg) {
    if(o instanceof Counter) {
      filter.add(((Counter) o).getVal());
    } else if(o instanceof Filter) {
      visualizer.visualize(filter.getList());
    }
  }
}
