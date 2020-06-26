package arg2;

public class ImmutablePoint {
    private double x = 0;
    private double y = 0;

    public ImmutablePoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }

    public double getY() { return y; }

    public ImmutablePoint setX(double x) { return new ImmutablePoint(x, y); }

    public ImmutablePoint setY(double y) { return new ImmutablePoint(x, y); }

    public ImmutablePoint traslate(double dx, double dy) {
        return new ImmutablePoint(x + dx, y + dy);
    }

    public ImmutablePoint rotate(double theta) {
        return new ImmutablePoint(
            x * Math.cos(theta) - y * Math.sin(theta),
            x * Math.sin(theta) + y * Math.cos(theta));
    }

    public double distance(ImmutablePoint p) {
        assert p != null;
        
        return Math.sqrt(Math.pow(p.getX() - x, 2) + Math.pow(p.getY() - y, 2));
    }
}