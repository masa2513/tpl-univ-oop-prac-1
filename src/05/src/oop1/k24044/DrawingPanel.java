package oop1.k24044;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Stack;

import javax.swing.JPanel;

public class DrawingPanel extends JPanel {
    private Shape[] shapes;
    private String currentShapeType = "Circle";
    private Color currentColor = Color.BLUE;
    private Shape selectedShape = null;
    private int startX, startY;
    private boolean isDragging = false;
    private Stack<Shape[]> undoStack = new Stack<>();
    private Stack<Shape[]> redoStack = new Stack<>();

    public DrawingPanel() {
        shapes = new Shape[0];
        setBackground(Color.WHITE);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startX = e.getX();
                startY = e.getY();
                selectedShape = findShapeAt(startX, startY);
                isDragging = selectedShape != null;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDragging && selectedShape != null) {
                    int dx = e.getX() - startX;
                    int dy = e.getY() - startY;
                    selectedShape.setX(selectedShape.getX() + dx);
                    selectedShape.setY(selectedShape.getY() + dy);
                    startX = e.getX();
                    startY = e.getY();
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!isDragging) {
                    saveState();
                    Shape newShape = null;
                    int width = Math.abs(e.getX() - startX);
                    int height = Math.abs(e.getY() - startY);
                    int centerX = (startX + e.getX()) / 2;
                    int centerY = (startY + e.getY()) / 2;

                    if ("Circle".equals(currentShapeType)) {
                        newShape = new Circle(centerX, centerY, Math.max(width, height) / 2, currentColor);
                    } else if ("Rectangle".equals(currentShapeType)) {
                        newShape = new Rectangle(centerX, centerY, width, height, currentColor);
                    } else if ("Triangle".equals(currentShapeType)) {
                        newShape = new Triangle(centerX, centerY, width, height, currentColor);
                    }

                    if (newShape != null) {
                        addShape(newShape);
                        repaint();
                    }
                }
                isDragging = false;
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    private Shape findShapeAt(int x, int y) {
        for (int i = shapes.length - 1; i >= 0; i--) {
            if (shapes[i].contains(x, y)) {
                return shapes[i];
            }
        }
        return null;
    }

    private void saveState() {
        Shape[] state = new Shape[shapes.length];
        for (int i = 0; i < shapes.length; i++) {
            state[i] = shapes[i];
        }
        undoStack.push(state);
        redoStack.clear();
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            redoStack.push(shapes);
            shapes = undoStack.pop();
            repaint();
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            undoStack.push(shapes);
            shapes = redoStack.pop();
            repaint();
        }
    }

    public void deleteSelected() {
        if (selectedShape != null) {
            saveState();
            Shape[] newShapes = new Shape[shapes.length - 1];
            int j = 0;
            for (Shape shape : shapes) {
                if (shape != selectedShape) {
                    newShapes[j++] = shape;
                }
            }
            shapes = newShapes;
            selectedShape = null;
            repaint();
        }
    }

    public void addShape(Shape shape) {
        Shape[] newShapes = new Shape[this.shapes.length + 1];
        for (int i = 0; i < this.shapes.length; i++) {
            newShapes[i] = this.shapes[i];
        }
        newShapes[newShapes.length - 1] = shape;
        this.shapes = newShapes;
    }

    public void clearShapes() {
        saveState();
        this.shapes = new Shape[0];
        repaint();
    }

    public void setCurrentShapeType(String shapeType) {
        this.currentShapeType = shapeType;
    }

    public void setCurrentColor(Color color) {
        this.currentColor = color;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Shape shape : shapes) {
            shape.draw(g);
        }
        if (selectedShape != null) {
            g.setColor(Color.BLACK);
            g.drawRect(selectedShape.getX() - 5, selectedShape.getY() - 5, 10, 10);
        }
    }
} 