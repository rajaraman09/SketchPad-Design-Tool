
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Line extends Shape {

    protected int endX;

    protected int endY;

    List<Point> points = new ArrayList<>();

    public Line(int x, int y, int height, int width, Color color) {
        super(x, y, height, width, Type.Line, color);
    }

    public void initializePoints() {
        int x1 = this.x;
        int y1 = this.y;
        int x2 = endX;
        int y2 = endY;
        points.clear();
        points.add(new Point(x1, y1));
        points.add(new Point(x2, y2));
        createPoints(x1, y1, x2, y2);
    }

    public void createPoints(int x1, int y1, int x2, int y2) {
        if (Math.abs(x1 - x2) <= 1 && Math.abs(y1 - y2) <= 1) {
            return;
        } else {
            points.add(new Point((x1 + x2) / 2, (y1 + y2) / 2));
            createPoints(x1, y1, (x1 + x2) / 2, (y1 + y2) / 2);
            createPoints((x1 + x2) / 2, (y1 + y2) / 2, x2, y2);
        }
    }

    public int isNearby(int x, int y) {
        if (Math.abs(this.x - x) <= 5 && Math.abs(this.y - y) <= 5)
            return 1;
        else if (Math.abs(this.endX - x) <= 5 && Math.abs(this.endY - y) <= 5)
            return 2;
        else
            return 0;
    }

    @Override
    public void draw(Graphics ga) {
        Color defaultColor = ga.getColor();
        ga.setColor(color);
        ga.drawLine(x, y, endX, endY);
        ga.setColor(defaultColor);
    }

    public void setEndXY(int x, int y) {
        endX = x;
        endY = y;
        initializePoints();
    }

    @Override
    public boolean doesIntersect(int x, int y) {
        return points.stream().map(p -> p.x).collect(Collectors.toList()).stream().anyMatch(pointX -> (pointX > x - 2 && pointX < x + 2)) &&
                points.stream().map(p -> p.y).collect(Collectors.toList()).stream().anyMatch(pointY -> (pointY > y - 2 && pointY < y + 2));
    }

    public void setX(int x, int x0) {
        endX = endX + x - x0;
        this.x += x - x0;
        initializePoints();
    }

    public void setY(int y, int y0) {
        endY = endY + y - y0;
        this.y += y - y0;
        initializePoints();
    }
}
