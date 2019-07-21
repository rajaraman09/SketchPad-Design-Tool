import java.awt.*;

public class Rectangle extends Shape {

    public Rectangle(int x, int y, int height, int width, Color color) {
        super(x, y, height, width, Type.Rectangle, color);
    }

    public void draw(Graphics ga) {
        Color defaultColor = ga.getColor();
        ga.setColor(color);
        ga.fillRect(x, y, width, height);
        ga.setColor(defaultColor);
    }

    @Override
    public boolean doesIntersect(int x, int y) {
        return x > this.x && x < this.x + width && y > this.y && y < this.y + height;
    }

}
