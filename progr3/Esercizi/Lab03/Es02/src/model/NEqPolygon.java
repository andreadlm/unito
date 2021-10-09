package model;

public abstract class NEqPolygon extends Polygon {

  double heightLength;
  double baseLength;

  public NEqPolygon(int numVertices, double heightLength, double baseLength) throws IllegalArgumentException {
    super(numVertices);

    if(heightLength < 0)
      throw new IllegalArgumentException("heightLength argument must be positive");
    this.heightLength = heightLength;

    if(baseLength < 0)
      throw new IllegalArgumentException("baseLength argument must be positive");
    this.baseLength = baseLength;
  }

  @Override
  public boolean equals(Object o) {
    if(o.getClass() != getClass()) return false;
    NEqPolygon nEqPolygon = (NEqPolygon) o;
    return nEqPolygon.baseLength == baseLength &&
           nEqPolygon.heightLength == heightLength;
  }

  @Override
  public String[] describeAttributes() {
    return new String[]{"heightLength", "baseLength"};
  }

  @Override
  public void setAttributes(double[] attributes) throws IllegalArgumentException {
    if(attributes.length != 2)
      throw new IllegalArgumentException("attributes must contain 2 field");
    heightLength =  attributes[0];
    baseLength = attributes[1];
  }
}
