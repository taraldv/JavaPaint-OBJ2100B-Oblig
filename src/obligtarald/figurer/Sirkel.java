/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package obligtarald.figurer;

import java.util.ArrayList;
import javafx.scene.shape.Circle;

/**
 *
 * @author Tarald
 */
public class Sirkel extends Circle {

    private final ArrayList<Double[]> pointList = new ArrayList<>();
    private boolean finished = false;
    private static int antall = 0;
    private double area;
    private double x;
    private double y;

    public Sirkel() {
        antall++;
    }

    public String getName() {
        return "Sirkel" + antall;
    }

    public void addPoint(double x, double y) {
        if (pointList.size() < 2) {
            pointList.add(new Double[]{x, y});
        }
        checkSize();
    }

    private void checkSize() {
        if (pointList.size() == 2) {
            double x1 = pointList.get(0)[0];
            double y1 = pointList.get(0)[1];
            double x2 = pointList.get(1)[0];
            double y2 = pointList.get(1)[1];
            setCenterX(x1);
            setCenterY(y1);
            double radius = Geometri.avstandMellomToPunkt(x1, y1, x2, y2);
            setRadius(radius);
            finished = true;
            area = Geometri.arealAvSirkel(radius);
        }
    }

    public double getArea() {
        return Math.round(area);
    }

    public boolean isFinished() {
        return finished;
    }

    public void enableDrag() {

        setOnMousePressed((event) -> {
            x = event.getX();
            y = event.getY();
        });

        setOnMouseDragged((event) -> {

            double offsetX = event.getX() - x;
            double offsetY = event.getY() - y;

            setCenterX(getCenterX() + offsetX);
            setCenterY(getCenterY() + offsetY);

            x = event.getX();
            y = event.getY();
        });
    }

}
