package arg4;

import arg2.Point;

public class MutableTriangle {
    private Point a = null;
    private Point b = null;
    private Point c = null;

    public MutableTriangle(Point a, Point b, Point c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public Point getA() { return a; }

    public Point getB() { return b; }

    public Point getC() { return c; }

    public void traslate(double dx, double dy) {
        a.traslate(dx, dy);
        b.traslate(dx, dy);
        c.traslate(dx, dy);
    }

    public void rotate(double theta) {
        a.rotate(theta);
        b.rotate(theta);
        c.rotate(theta);
    }

    public double perimeter() {
        return a.distance(b) + b.distance(c) + c.distance(a);
    }

    public double area() {
        return Math.abs((a.getX()*(b.getY() - c.getY()) + b.getX()*(c.getY() - a.getY()) + c.getX()*(a.getY() - b.getY()))/2);
    }
}