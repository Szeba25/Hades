package hu.szeba.hades.main.io;

import java.awt.*;
import java.util.Random;

public class ViewNode {

    private String id;
    private Color color;
    private Point location;
    private int radius;

    public ViewNode(String[] data) {
        id = data[0];
        color = new Color(Integer.parseInt(data[1]), Integer.parseInt(data[2]), Integer.parseInt(data[3]));
        location = new Point(Integer.parseInt(data[4]), Integer.parseInt(data[5]));
        radius = 15;
    }

    public ViewNode(String id, Point location) {
        this.id = id;
        Random random = new Random();
        this.color = new Color(random.nextInt(160), random.nextInt(160), random.nextInt(160));
        this.location = location;
        radius = 15;
    }

    public String[] getDataArray() {
        return new String[] {id,
                String.valueOf(color.getRed()),
                String.valueOf(color.getGreen()),
                String.valueOf(color.getBlue()),
                String.valueOf(location.x),
                String.valueOf(location.y)};
    }

    public String getId() {
        return id;
    }

    public Color getColor() {
        return color;
    }

    public Point getLocation() {
        return location;
    }

    public int getRadius() {
        return radius;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setLocation(int x, int y) {
        location.setLocation(x, y);
    }

    public boolean isInside(int x, int y) {
        return Math.abs(x - location.x) < radius * 2 && Math.abs(y - location.y) < radius * 2;
    }

}
