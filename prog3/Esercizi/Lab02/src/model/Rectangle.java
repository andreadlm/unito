package model;

public class Rectangle extends NEqPolygon {

  public Rectangle(double heightLength, double baseLength) throws IllegalArgumentException {
    super(4, heightLength, baseLength);
  }

  @Override
  public double getArea() {
    return baseLength * heightLength / 2;
  }

  public double getPerimeter() {
    return (baseLength + heightLength) * 2;
  }
}
