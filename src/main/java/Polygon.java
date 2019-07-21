import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Polygon extends Shape {

    List<Line> lines = new ArrayList<>();
    Line currentLine = null;

    public Polygon(int x, int y, int height, int width, Color color) {
        super(x, y, height, width, Type.Polygon, color);
        currentLine = new Line(x, y, height, width, color);
        lines.add(currentLine);
    }

    @Override
    public void draw(Graphics ga) {
        Color defaultColor = ga.getColor();
        ga.setColor(color);
        if (isOpen()) {
            lines.forEach(line -> ga.drawLine(line.x, line.y, line.endX, line.endY));
        } else
            ga.fillPolygon(lines.stream().mapToInt(line -> line.endX).toArray(), lines.stream().mapToInt(line -> line.endY).toArray(), lines.size());
        ga.setColor(defaultColor);
    }

    @Override
    public boolean doesIntersect(int x, int y) {
        return lines.stream().anyMatch(line -> line.doesIntersect(x, y));
    }

    public boolean isOpen() {
        if (lines.size() > 2) {
            List<Line> auxLines = new ArrayList<>(lines);
            Line firstLine = lines.get(0);
            auxLines.remove(firstLine);
            Line nearbyLine = getNearbyLine(firstLine.endX, firstLine.endY, auxLines);
            auxLines.add(0, firstLine);
            while (nearbyLine != firstLine && nearbyLine != null) {
                auxLines.remove(nearbyLine);
                nearbyLine = getNearbyLine(nearbyLine.endX, nearbyLine.endY, auxLines);
            }
            return firstLine != nearbyLine;
        } else
            return true;
    }

    public boolean isNearby(int x, int y) {
        return lines.stream().anyMatch(line -> line.isNearby(x, y) == 1 || line.isNearby(x, y) == 2);
    }

    public Point getNearbyPoint(int x, int y) {
        Line line = lines.stream().filter(l -> l.isNearby(x, y) == 1 || l.isNearby(x, y) == 2).findFirst().orElse(null);
        if (line != null) {
            return line.isNearby(x, y) == 1 ? new Point(line.x, line.y) : new Point(line.endX, line.endY);
        } else {
            return null;
        }
    }

    public Line getNearbyLine(int x, int y, List<Line> lines) {
        return lines.stream().filter(l -> l.isNearby(x, y) == 1 || l.isNearby(x, y) == 2).findFirst().orElse(null);
    }

    public void setEndXY(int x, int y) {
        Point nearbyPoint = getNearbyPoint(x, y);
        if (nearbyPoint == null) {
            currentLine.setEndXY(x, y);
        } else {
            currentLine.setEndXY(nearbyPoint.x, nearbyPoint.y);
        }
    }

    public void addLine(int x, int y, Color color) {
        Point nearbyPoint = getNearbyPoint(x, y);
        currentLine = new Line(nearbyPoint.x, nearbyPoint.y, 0, 0, color);
        lines.add(currentLine);
    }

    public void setX(int x, int x0) {
        lines.forEach(line -> line.setX(x, x0));
        this.x += x - x0;
    }

    public void setY(int y, int y0) {
        lines.forEach(line -> line.setY(y, y0));
        this.y += y - y0;
    }
}
