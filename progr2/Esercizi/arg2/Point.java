package arg2;

public class Point {
    private double x = 0;
    private double y = 0;

    public double getX() { return x; }

    public double getY() { return y; }

    public void setX(double x) { this.x = x; }

    public void setY(double y) { this.y = y; }

    public void traslate(double dx, double dy) {
        x += dx;
        y += dy;
    }

    public void rotate(double theta) {
        x = x * Math.cos(theta) - y * Math.sin(theta);
        y = x * Math.sin(theta) + y * Math.cos(theta);
    }

    public double distance(Point p) {
        assert p != null;
        
        return Math.sqrt(Math.pow(p.getX() - x, 2) + Math.pow(p.getY() - y, 2));
    }
}