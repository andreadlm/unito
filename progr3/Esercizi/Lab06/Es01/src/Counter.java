import java.util.Observable;

public class Counter extends Observable {
  public void start() {
    for(int i = 0; i < 50; i++) {
      if(i % 5 == 0) {
        setChanged();
        notifyObservers(i);
      }
    }
  }
}
