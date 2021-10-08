package model;

public class Square extends EqPolygon {

  public Square(double sizeLength) throws IllegalArgumentException {
    super(4, sizeLength);
  }

  @Override
  public double getArea() {
    return sizeLength * sizeLength;
  }
}
