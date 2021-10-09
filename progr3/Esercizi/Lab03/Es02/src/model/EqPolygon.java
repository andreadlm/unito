package model;

public abstract class EqPolygon extends Polygon {

  double sizeLength;

  public EqPolygon(int numVertices, double sizeLength) {
    super(numVertices);

    if(numVertices < 0)
      throw new IllegalArgumentException("sizeLength argument must be positive");
    this.sizeLength = sizeLength;
  }

  public double getPerimeter() {
    return numVertices * sizeLength;
  }

  @Override
  public boolean equals(Object o) {
    if(o.getClass() != getClass()) return false;
    EqPolygon eqPolygon = (EqPolygon) o;
    return eqPolygon.sizeLength == sizeLength;
  }

  @Override
  public String[] describeAttributes() {
    return new String[]{"sizeLength"};
  }

  @Override
  public void setAttributes(double[] attributes) throws IllegalArgumentException {
    if(attributes.length != 1)
      throw new IllegalArgumentException("attributes must contain 1 field");
    sizeLength =  attributes[0];
  }
}
