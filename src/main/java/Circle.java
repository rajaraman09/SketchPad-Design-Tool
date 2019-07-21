import java.awt.*;

public class Circle extends Shape {
    public Circle(int x, int y, int height, int width, Color color) {
        super(x, y, width, width, Type.Circle, color);
    }

    @Override
    
    public void draw(Graphics ga) {
        Color defaultColor = ga.getColor();
        ga.setColor(color);
        ga.fillOval(x, y, width, width);
        ga.setColor(defaultColor);
    }

    @Override
    //chk pos of mouse is prsent in circle or not
    public boolean doesIntersect(int x, int y) {
        return x > this.x && x < this.x + width && y > this.y && y < this.y + height;
    }

    public void setHeight(int height) {
        this.height = height;
        this.width = height;
    }

    public void setWidth(int width) {
        this.height = width;
        this.width = width;
    }

    public void setEndXY(int x, int y) {
        width = x - this.x;
        height = x - this.x;
    }
}
