package model;

public abstract class Polygon {

  final int numVertices;

  public Polygon(int numVertices) throws IllegalArgumentException {
    if(numVertices < 0)
      throw new IllegalArgumentException("numVertices argument must be positive");
    this.numVertices = numVertices;
  }

  public int getNumVertices() {
    return numVertices;
  }

  public abstract double getArea();

  public abstract String[] describeAttributes();

  public abstract void setAttributes(double[] attributes);
}
