package model;

import java.util.ArrayList;

public class Geometries<T extends Polygon> {

  private final ArrayList<T> list;

  public Geometries() {
    list = new ArrayList<>();
  }

  public boolean add(T polygon) throws IllegalArgumentException {
    if(polygon == null)
      throw new IllegalArgumentException("polygon argument can't be null");
    for(Polygon current : list)
      if(current.equals(polygon))
        return false;

    list.add(polygon);
    return true;
  }

  public int size() {
    return list.size();
  }

  public void remove(int idx) throws IndexOutOfBoundsException {
    list.remove(idx);
  }

  public void printAreas() {
    for(Polygon current : list) {
      System.out.println("[" + current.getClass().getName() + "] -> Area: " + current.getArea());
    }
  }

  public int getNumElements() {
    return list.size();
  }
}
