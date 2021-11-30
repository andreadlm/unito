import java.util.ArrayList;

public class Main {
  public static void main(String[] args) {
    ArrayList<Philosopher> philosophers = new ArrayList<>();

    Chopstick first = new Chopstick();
    Chopstick oldLeft = new Chopstick();
    philosophers.add(new Philosopher(oldLeft, first));

    for(int i = 1; i < Integer.parseInt(args[0]) - 1; i++) {
      Chopstick left = new Chopstick();
      philosophers.add(new Philosopher(left, oldLeft));
      oldLeft = left;
    }

    philosophers.add(new Philosopher(oldLeft, first));

    for(Philosopher philosopher : philosophers) {
      Thread thread = new Thread(philosopher);
      thread.start();
    }
  }
}
