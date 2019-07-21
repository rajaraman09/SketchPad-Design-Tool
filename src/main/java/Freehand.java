import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Freehand extends Shape {

    protected List<Point> points = new ArrayList<>();

    public Freehand(int x, int y, int height, int width, Color color) {
        super(x, y, height, width, Type.Freehand, color);
        points.add(new Point(x, y));
    }

    @Override
    public void draw(Graphics ga) {
        Color defaultColor = ga.getColor();
        ga.setColor(color);
        ga.drawPolyline(points.stream().mapToInt(p -> p.x).toArray(), points.stream().mapToInt(p -> p.y).toArray(), points.size());
        ga.setColor(defaultColor);
    }

    @Override
    public boolean doesIntersect(int x, int y) {
        return points.stream().map(p -> p.x).collect(Collectors.toList()).stream().anyMatch(pointX -> (pointX > x - 3 && pointX < x + 3)) &&
                points.stream().map(p -> p.y).collect(Collectors.toList()).stream().anyMatch(pointY -> (pointY > y - 3 && pointY < y + 3));
    }

    public void setEndXY(int x, int y) {
        points.add(new Point(x, y));
    }

    public void setX(int x, int x0) {
        points.forEach(p -> p.x = p.x + x - x0);
        this.x += x - x0;
    }

    public void setY(int y, int y0) {
        points.forEach(p -> p.y = p.y + y - y0);
        this.y += y - y0;
    }
}
