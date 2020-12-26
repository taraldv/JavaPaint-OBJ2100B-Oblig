/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package obligtarald.figurer;

import java.util.ArrayList;
import javafx.scene.shape.Rectangle;

public class Rektangel extends Rectangle {

    private static int antall = 0;
    private final ArrayList<Double[]> pointList = new ArrayList<>();
    private boolean finished = false;
    private double area;
    private double x;
    private double y;

    public Rektangel() {
        antall++;
    }

    public String getName() {
        return "Rektangel" + antall;
    }

    public void addPoint(double x, double y) {
        if (pointList.size() < 2) {
            pointList.add(new Double[]{x, y});
        }
        checkSize();
    }

    public boolean isFinished() {
        return finished;
    }

    private void checkSize() {
        if (pointList.size() == 2) {

            double x1 = pointList.get(0)[0];
            double y1 = pointList.get(0)[1];
            double x2 = pointList.get(1)[0];
            double y2 = pointList.get(1)[1];

            double maxX = Math.max(x1, x2);
            double minX = Math.min(x1, x2);

            double maxY = Math.max(y1, y2);
            double minY = Math.min(y1, y2);

            setX(minX);
            setY(minY);
            double width = maxX - minX;
            double height = maxY - minY;
            setWidth(width);
            setHeight(height);
            area = Geometri.arealAvRektangel(width, height);
            finished = true;
        }
    }

    public double getArea() {
        return Math.round(area);
    }

    public void enableDrag() {
        setOnMousePressed((event) -> {
            x = event.getX();
            y = event.getY();
        });

        setOnMouseDragged((event) -> {
            double offsetX = event.getX() - x;
            double offsetY = event.getY() - y;

            setX(getX() + offsetX);
            setY(getY() + offsetY);

            x = event.getX();
            y = event.getY();
        });
    }

}
