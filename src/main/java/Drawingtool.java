import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Drawingtool extends Frame implements ActionListener, WindowListener, MouseListener, MouseMotionListener, ItemListener {

    Shape.Type selectedShape = Shape.Type.Square;
    String selectedColor = "Blue";
    Action action = Action.Draw;
    Shape actionObject = null;
    List<Shape> actionObjectsGroup = null;
    Shape clipboard = null;
    BufferedImage background = null;
    Polygon myPolygon = null;

    Stack<Shape> undoStack = new Stack<>();

    final static int WIDTH = 1700;
    final static int HEIGHT = 1700;

    int mX, mY;

    enum Action {
        Draw, Move, MoveGroup, Cut, Paste, Undo, Redo
    }

    int upLX, upLY, W, H, selX1, selY1, selX2, selY2;
    String[] extrasList = {"Draw", "Move Object", "Clear Canvas", "Move Group", "Cut", "Paste", "Save", "Load", "Undo", "Redo"};
    String[] colorList = {"Black", "Cyan", "Green", "Yellow", "Magenta", "Red", "Blue"};
    Map<String, Color> colorMap = Stream.of(new Object[][]{
            {"Black", Color.black},
            {"Cyan", Color.cyan},
            {"Green", Color.green},
            {"Yellow", Color.yellow},
            {"Magenta", Color.magenta},
            {"Red", Color.red},
            {"Blue", Color.blue}
    }).collect(Collectors.toMap(data -> (String) data[0], data -> (Color) data[1]));
    List<Shape.Type> shapeList = Arrays.asList(Shape.Type.Freehand, Shape.Type.Line, Shape.Type.Rectangle, Shape.Type.Square, Shape.Type.Circle, Shape.Type.Ellipse, Shape.Type.Polygon);

    List<Shape> shapes = new ArrayList<>();
    Shape currentShape = null;

    @Override //from WindowListener
    public void windowClosing(WindowEvent eve) {
        System.exit(0);
    }

    @Override //from WindowListener
    public void windowActivated(WindowEvent we) {
    }

    @Override //from WindowListener
    public void windowOpened(WindowEvent we) {
    }

    @Override //from WindowListener
    public void windowIconified(WindowEvent we) {
    }

    @Override //from WindowListener
    public void windowClosed(WindowEvent we) {
    }

    @Override //from WindowListener
    public void windowDeactivated(WindowEvent we) {
    }

    @Override //from WindowListener
    public void windowDeiconified(WindowEvent we) {
    }

    @Override //from MouseMotionListener
    public void mouseMoved(MouseEvent me) {
    }

    @Override //from MouseListener
    public void mouseClicked(MouseEvent me) {
    }

    @Override //from MouseListener
    public void mouseExited(MouseEvent me) {
    }

    @Override //from MouseListener
    public void mouseEntered(MouseEvent me) {
    }

    @Override //from ItemListener
    public void itemStateChanged(ItemEvent ie) {
    }

    public Drawingtool(String title) {
        super(title);
        addMouseMotionListener(this);
        addWindowListener(this);
        addMouseListener(this);
        setLayout(null);
        setMenuItems();
        setBackground(Color.white);
    }

    @Override //from ActionListener
    public void actionPerformed(ActionEvent ape) {
        Graphics ga = getGraphics();
        Object s = ape.getActionCommand();
        for (int i = 0; i != colorList.length; i++) {
            if (s.equals(colorList[i])) {
                selectedColor = colorList[i];
                return;
            }
        }
        for (int i = 0; i != shapeList.size(); i++) {
            if (s.equals(shapeList.get(i).toString())) {
                selectedShape = shapeList.get(i);
                return;
            }
        }
        if (s.equals("Clear Canvas")) {
            ga.clearRect(0, 0, WIDTH, HEIGHT);
            shapes = new ArrayList<>();
            myPolygon = null;
            return;
        } else if (s.equals("Move Object")) {
            action = Action.Move;
            actionObject = null;
        } else if (s.equals("Draw")) {
            action = Action.Draw;
        } else if (s.equals("Move Group")) {
            action = Action.MoveGroup;
            actionObjectsGroup = null;
        } else if (s.equals("Cut")) {
            action = Action.Cut;
            clipboard = null;
        } else if (s.equals("Paste")) {
            if (clipboard == null) {
                JOptionPane.showMessageDialog(null, "Nothing to paste");
            } else {
                action = Action.Paste;
            }
        } else if (s.equals("Save")) {
            BufferedImage bImg = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
            Graphics2D cg = bImg.createGraphics();
            cg.setBackground(Color.white);
            drawStack(cg);
            try {
                String fileName = JOptionPane.showInputDialog("Please enter filename:");
                if (ImageIO.write(bImg, "png", new File("./" + fileName + ".png"))) {
                    System.out.println("-- saved");
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error saving file, please look at stacktrace");
                e.printStackTrace();
            }
        } else if (s.equals("Load")) {
            String fileName = JOptionPane.showInputDialog("Please enter filename:");
            try {
                background = ImageIO.read(new FileInputStream("./" + fileName + ".png"));
                ga.clearRect(0, 0, WIDTH, HEIGHT);
                ga.drawImage(background, 0, 0, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (s.equals("Undo")) {
            if (!shapes.isEmpty()) {
                undoStack.push(shapes.get(shapes.size() - 1));
                shapes.remove(shapes.size() - 1);
            }
            drawStack(ga);
        } else if (s.equals("Redo")) {
            if (!undoStack.empty())
                shapes.add(undoStack.pop());
            drawStack(ga);
        }
    }

    void chooseColor(Graphics ga) {
        for (int i = 0; i != colorList.length; i++) {
            if (selectedColor.equals(colorList[i])) {
                switch (i) {
                    case 0:
                        ga.setColor(Color.black);
                        break;
                    case 1:
                        ga.setColor(Color.cyan);
                        break;
                    case 2:
                        ga.setColor(Color.green);
                        break;
                    case 3:
                        ga.setColor(Color.yellow);
                        break;
                    case 4:
                        ga.setColor(Color.magenta);
                        break;
                    case 5:
                        ga.setColor(Color.red);
                        break;
                    case 6:
                        ga.setColor(Color.blue);
                }
            }
        }
    }

    @Override //from MouseListener
    public void mouseReleased(MouseEvent me) {
        Graphics ga = getGraphics();
        if (action == Action.Move) {
            actionObject = null;
            return;
        } else if (action == Action.MoveGroup) {
            actionObjectsGroup = null;
            return;
        } else if (action == Action.Cut) {
            drawStack(ga);
            return;
        } else if (action == Action.Paste) {
            drawStack(ga);
            return;
        }
        chooseColor(ga);
        selX2 = me.getX();
        selY2 = me.getY();
        upLX = Math.min(selX1, selX2);
        upLY = Math.min(selY1, selY2);
        W = Math.abs(selX1 - selX2);
        H = Math.abs(selY1 - selY2);
        redraw(ga);
        if(!shapes.contains(currentShape))
            shapes.add(currentShape);
    }

    @Override //from MouseMotionListener
    public void mouseDragged(MouseEvent me) {
        Graphics ga = getGraphics();
        selX2 = me.getX();
        selY2 = me.getY();
        if (action == Action.Draw)
            redraw(ga);
        else if (action == Action.Move && actionObject != null) {
            moveObject(ga, actionObject, selX2, selY2, mX, mY);
            mX = selX2;
            mY = selY2;
        } else if (action == Action.MoveGroup && actionObjectsGroup != null) {
            moveObjects(ga, actionObjectsGroup, selX2, selY2, mX, mY);
            mX = selX2;
            mY = selY2;
        }
    }

    public void moveObject(Graphics ga, Shape shape, int x1, int y1, int x0, int y0) {
        shape.setX(x1, x0);
        shape.setY(y1, y0);
        drawStack(ga);
    }

    public void moveObjects(Graphics ga, List<Shape> shapeList, int x1, int y1, int x0, int y0) {
        shapeList.forEach(ao -> {
            ao.setX(x1, x0);
            ao.setY(y1, y0);
        });
        drawStack(ga);
    }

    public void redraw(Graphics ga) {
        drawStack(ga);
        ga.setColor(currentShape.getColor());
        currentShape.setEndXY(selX2, selY2);
        currentShape.draw(ga);
    }

    public void selectObjectToMove(int x, int y) {
        shapes.stream().filter(shape -> shape.doesIntersect(x, y)).findFirst().ifPresent(shape -> actionObject = shape);
    }

    public Shape getObjectToCut(int x, int y) {
        Optional<Shape> shape = shapes.stream().filter(s -> s.doesIntersect(x, y)).findFirst();
        return shape.orElse(null);
    }

    public void selectObjectsToMove(int x, int y) {
        actionObjectsGroup = shapes.stream().filter(shape -> shape.doesIntersect(x, y)).collect(Collectors.toList());
    }

    @Override //from MouseListener
    public void mousePressed(MouseEvent me) {
        upLX = 0;
        upLY = 0;
        W = 0;
        H = 0;
        selX1 = me.getX();
        selY1 = me.getY();
        if (action == Action.Move) {
            mX = selX1;
            mY = selY1;
            selectObjectToMove(selX1, selY1);
            return;
        } else if (action == Action.MoveGroup) {
            mX = selX1;
            mY = selY1;
            selectObjectsToMove(selX1, selY1);
            return;
        } else if (action == Action.Cut) {
            clipboard = getObjectToCut(selX1, selY1);
            shapes.remove(clipboard);
            return;
        } else if (action == Action.Paste) {
            moveObject(getGraphics(), clipboard, selX1, selY1, clipboard.getX(), clipboard.getY());
            shapes.add(clipboard);
            clipboard = null;
            return;
        }
        switch (selectedShape) {
            case Freehand:
                currentShape = new Freehand(selX1, selY1, W, W, colorMap.get(selectedColor));
                break;
            case Line:
                currentShape = new Line(selX1, selY1, W, W, colorMap.get(selectedColor));
                break;
            case Polygon:
                drawPolygon(selX1, selY1, selectedColor);
                break;
            case Square:
                currentShape = new Square(selX1, selY1, W, W, colorMap.get(selectedColor));
                break;
            case Rectangle:
                currentShape = new Rectangle(selX1, selY1, H, W, colorMap.get(selectedColor));
                break;
            case Ellipse:
                currentShape = new Ellipse(selX1, selY1, H, W, colorMap.get(selectedColor));
                break;
            case Circle:
                currentShape = new Circle(selX1, selY1, H, H, colorMap.get(selectedColor));
                break;
        }
    }

    public void drawPolygon(int x1, int y1, String color) {
        if(myPolygon != null && myPolygon.isNearby(x1, y1)) {
            myPolygon.addLine(x1, y1, colorMap.get(color));
        }
        else {
            myPolygon = new Polygon(x1, y1, 0, 0, colorMap.get(color));
            currentShape = myPolygon;
        }

    }

    void setMenuItems() {
        MenuBar mBar = new MenuBar();
        Menu menuShape = new Menu("Shape");
        for (int i = 0; i != shapeList.size(); i++) {
            menuShape.add(shapeList.get(i).toString());
        }
        mBar.add(menuShape);
        menuShape.addActionListener(this);
        Menu menuColor = new Menu("Colors");
        for (int i = 0; i != colorList.length; i++) {
            menuColor.add(colorList[i]);
        }
        mBar.add(menuColor);
        menuColor.addActionListener(this);
        Menu menuExtras = new Menu("Extras");
        for (int i = 0; i != extrasList.length; i++) {
            menuExtras.add(extrasList[i]);
        }
        mBar.add(menuExtras);
        menuExtras.addActionListener(this);
        setMenuBar(mBar);
    }

    public void drawStack(Graphics ga) {
        ga.clearRect(0, 0, WIDTH, HEIGHT);
        ga.drawImage(background, 0, 0, null);
        shapes.forEach(shape -> shape.draw(ga));
    }

    //main method menuExtrasecution starts here
    public static void main(String[] args) {
        Drawingtool sp = new Drawingtool("Sketchpad in Java");
        sp.setSize(WIDTH, HEIGHT);
        sp.setVisible(true);
    }
}