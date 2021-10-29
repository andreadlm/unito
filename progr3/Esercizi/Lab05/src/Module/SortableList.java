package Module;

public interface SortableList<T extends Comparable<T>> {
  public boolean add(T item);

  public boolean remove(T item);

  public void sort();

  public void print();
}
