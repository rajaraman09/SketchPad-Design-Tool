import java.awt.*;

public abstract class Shape {

    protected int x;
    protected int y;
    protected int height;
    protected int width;

    protected Type type;
    protected Color color;

    enum Type {
        Freehand, Line, Rectangle, Square, Circle, Ellipse, Polygon
    }

    public Shape(int x, int y, int height, int width, Type type, Color color) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.type = type;
        this.color = color;
    }

    public abstract void draw(Graphics ga);

    public abstract boolean doesIntersect(int x, int y);

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Type getType() {
        return type;
    }

    public Color getColor() {
        return color;
    }

    public void setX(int x, int x0) {
        this.x += x - x0;
    }

    public void setY(int y, int y0) {
        this.y += y - y0;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setEndXY(int x, int y) {
        width = x - this.x;
        height = y - this.y;
    }

}
