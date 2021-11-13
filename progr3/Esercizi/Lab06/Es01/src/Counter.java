import java.util.Observable;

public class Counter extends Observable {
  private int val;
  private int start;
  private int end;

  public Counter(int start, int end) {
    this.start = start;
    this.end = end;
  }

  public int getVal() {
    return val;
  }

  public void start() {
    for(val = start; val < end; val++) {
      setChanged();
      notifyObservers();
    }
  }
}
