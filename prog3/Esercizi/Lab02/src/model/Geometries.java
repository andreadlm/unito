package model;

import java.util.ArrayList;

public class Geometries {

  private ArrayList<Polygon> list;

  public Geometries() {
    list = new ArrayList<>();
  }

  public boolean add(Polygon polygon) throws IllegalArgumentException {
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

  public void printDescription() {
    for(Polygon current : list) {
      System.out.println("[" + current.getClass().getName() + "] -> Area: " + current.getArea() + " NumVertices: " +
                         current.getNumVertices());
    }
  }
}
