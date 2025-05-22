package oop1.k24044;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

public class Triangle extends Shape {
    private int width;
    private int height;

    public Triangle(int x, int y, int width, int height, Color color) {
        super(x, y, color);
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        int[] xPoints = {x, x - width/2, x + width/2};
        int[] yPoints = {y - height/2, y + height/2, y + height/2};
        g.fillPolygon(xPoints, yPoints, 3);
    }

    @Override
    public boolean contains(int x, int y) {
        int[] xPoints = {this.x, this.x - width/2, this.x + width/2};
        int[] yPoints = {this.y - height/2, this.y + height/2, this.y + height/2};
        return new Polygon(xPoints, yPoints, 3).contains(x, y);
    }
} 