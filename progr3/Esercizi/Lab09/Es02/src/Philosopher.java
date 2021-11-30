import static java.lang.Thread.sleep;

public class Philosopher implements Runnable {
  private static int counter = 0;
  private int number = counter++;

  private final Chopstick left;
  private final Chopstick right;

  public Philosopher(Chopstick left, Chopstick right) {
    this.left = left;
    this.right = right;
  }

  public void think() {
    try {
      print("Thinking...");
      sleep(2000L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void print(String s) {
    System.out.println("[Philosopher | " + number + "] -> " + s);
  }

  public void eat() {
    synchronized(left) {
      print("Got " + left);

      synchronized(right) {
        print("Got" + right);
        print("Eating ...");

        try {
          sleep(2000L);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

      }
      print("Released " + right);

    }
    print("Released " + left);
  }

  @Override
  public void run() {
    while(true) {
      think();
      eat();
    }
  }
}
