package Module;

import java.util.ArrayList;

public class SortableListImpl<T extends Comparable<T>> implements SortableList<T> {
  private final ArrayList<T> list;

  public SortableListImpl() {
    list = new ArrayList<>();
  }

  @Override
  public boolean add(T item) {
    return list.add(item);
  }

  @Override
  public boolean remove(T item) {
    return list.remove(item);
  }

  @Override
  public void sort() {
    list.sort((T e1, T e2) -> e1.compareTo(e2));
  }

  @Override
  public void print() {
    for(T e : list) {
      System.out.print("[" + e + "]");
    }
    System.out.print("\n");
  }
}
