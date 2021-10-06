import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Generic implementation of a queue.
 * @param <T> the type of the elements to be stored inside the queue.
 */
public class Queue<T> {
  private final List<T> list;

  /**
   * Creates a new queue.
   * @param list the list the queue is based on.
   * @throws IllegalArgumentException if the list parameter is null.
   */
  public Queue(List<T> list) throws IllegalArgumentException {
    if(list != null) this.list = list;
    else throw new IllegalArgumentException("list argument can't be null");
  }

  /**
   * Checks if the queue is empty.
   * @return true if the queue is empty, false otherwise.
   */
  public boolean empty() {
    return list.isEmpty();
  }

  /**
   * Adds a new element to the queue.
   * @param elem the element to be added.
   */
  public void enqueue(T elem) {
    list.add(elem);
  }

  /**
   * Removes an element from the queue.
   * @return the element removed, null if the queue is empty.
   */
  public T dequeue() {
    if(!empty()) {
      T elem = list.get(0);
      list.remove(0);
      return elem;
    } else {
      return null;
    }
  }

  public static void main(String[] args) {
    Queue<Double> qD = new Queue<Double>(new LinkedList<>());
    Queue<Integer> qI = new Queue<Integer>(new LinkedList<>());

    qD.enqueue(5.0);
    qD.enqueue(6.0);
    qD.enqueue(7.0);

    qI.enqueue(5);
    qI.enqueue(6);
    qI.enqueue(7);

    System.out.println("qD:");
    while(!qD.empty()) {
      System.out.println(qD.dequeue());
    }

    System.out.println("\nqI:");
    while(!qI.empty()) {
      System.out.println(qI.dequeue());
    }

  }
}
