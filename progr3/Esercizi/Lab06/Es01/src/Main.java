public class Main {
  public static void main(String[] args) {
    Counter c = new Counter();
    Filter f = new Filter();
    Visualizer v = new Visualizer();

    f.addObserver(v);
    c.addObserver(f);

    c.start();
  }
}
