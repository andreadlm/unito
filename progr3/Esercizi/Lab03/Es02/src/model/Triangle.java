package model;

public class Triangle extends NEqPolygon {

  public Triangle(double heightLength, double baseLength) throws IllegalArgumentException {
    super(3, heightLength, baseLength);
  }

  @Override
  public double getArea() {
    return baseLength * heightLength / 2;
  }
}
