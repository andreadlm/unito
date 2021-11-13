public class Main {
  public static void main(String[] args) {
    Counter counter = new Counter(0, 50);
    Filter filter = new Filter();
    Visualizer visualizer = new Visualizer();

    Controller controller = new Controller(counter, filter, visualizer);

    controller.start();
  }
}
