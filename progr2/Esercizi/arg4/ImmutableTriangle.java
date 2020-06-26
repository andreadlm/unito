package arg4;

import arg2.ImmutablePoint;

public class ImmutableTriangle {
    private ImmutablePoint a = null;
    private ImmutablePoint b = null;
    private ImmutablePoint c = null;

    public ImmutableTriangle(ImmutablePoint a, ImmutablePoint b, ImmutablePoint c) {
        this.a = a;
        this.b = b;
        this.c = c; 
    }

    public ImmutablePoint getA() { return a; }

    public ImmutablePoint getB() { return b; }

    public ImmutablePoint getC() { return c; }

    public ImmutableTriangle traslate(double dx, double dy) {
        return new ImmutableTriangle(
            a.traslate(dx, dy),
            b.traslate(dx, dy),
            c.traslate(dx, dy));
    }

    public ImmutableTriangle rotate(double theta) {
        return new ImmutableTriangle(
            a.rotate(theta),
            b.rotate(theta),
            c.rotate(theta));
    }

    public double perimeter() {
        return a.distance(b) + b.distance(c) + c.distance(a);
    }

    public double area() {
        return Math.abs((a.getX()*(b.getY() - c.getY()) + b.getX()*(c.getY() - a.getY()) + c.getX()*(a.getY() - b.getY()))/2);
    }
}