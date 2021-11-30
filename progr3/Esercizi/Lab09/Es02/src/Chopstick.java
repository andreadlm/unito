public class Chopstick {
  private static int counter = 0;
  private final int number = counter++;

  @Override
  public String toString() {
    return "[Chopstick | " + number + "]";
  }
}
