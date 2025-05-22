package oop1.k24044;

import java.awt.Color;
import java.awt.Graphics;

public class Rectangle extends Shape {
    private int width;
    private int height;

    public Rectangle(int x, int y, int width, int height, Color color) {
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
        g.fillRect(x - width/2, y - height/2, width, height);
    }

    @Override
    public boolean contains(int x, int y) {
        return x >= this.x - width/2 && x <= this.x + width/2 &&
               y >= this.y - height/2 && y <= this.y + height/2;
    }
} 