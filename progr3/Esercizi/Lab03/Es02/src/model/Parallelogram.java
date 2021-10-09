package model;

public class Parallelogram extends NEqPolygon {

  public Parallelogram(double heightLength, double baseLength) throws IllegalArgumentException {
    super(4, heightLength, baseLength);
  }

  @Override
  public double getArea() {
    return baseLength * heightLength / 2;
  }
}
