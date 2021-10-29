package Kernel;

import Module.SortableList;

public class SortableArray <T extends Comparable<T>> {
  private SortableList<T> module;

  public void setModule(SortableList<T> module) {
    this.module = module;
  }

  public boolean add(T elem) {
    return module.add(elem);
  }

  public boolean remove(T elem) {
    return module.remove(elem);
  }

  public void sort() {
    module.sort();
  }

  public void print() {
    module.print();
  }
}
