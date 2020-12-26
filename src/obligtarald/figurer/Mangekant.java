/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package obligtarald.figurer;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.shape.Polygon;

/**
 *
 * @author Tarald
 */
public class Mangekant extends Polygon {

    private final ArrayList<Double[]> pointList = new ArrayList<>();
    private boolean finished = false;
    private final double pixelMargin = 10;
    private static int antall = 0;
    private double area;
    private double x;
    private double y;

    public Mangekant() {
        antall++;
    }

    public String getName() {
        return "Polygon" + antall;
    }

    public void addPoint(double x, double y) {

        if (withinMargin(x, y)) {
            finished = true;
            finishPolygon();
            area = Geometri.arealAvMangekant(pointList);
        } else {
            pointList.add(new Double[]{x, y});
        }
    }

    public boolean isFinished() {
        return finished;
    }

    //Sjekker om (x,y) punktet er 
    //nær det alle første punktet til mangekanten
    private boolean withinMargin(double x, double y) {
        //denne mangekanten må ha minst 3 punkter før den kan ferdigjøres
        if (pointList.size() < 3) {
            return false;
        }
        Double[] startCoordinates = pointList.get(0);

        // sjekker om: førstePunkt - margin < (x,y) < førstePunkt + margin
        return ((x < startCoordinates[0] + pixelMargin
                && x > startCoordinates[0] - pixelMargin)
                && (y < startCoordinates[1] + pixelMargin
                && y > startCoordinates[1] - pixelMargin));
    }

    private void finishPolygon() {
        for (Double[] doubles : pointList) {
            getPoints().addAll(doubles);
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

            List<Double> points = getPoints();
            for (int i = 0; i < points.size(); i++) {
                if (i % 2 == 0) {
                    getPoints().set(i, points.get(i) + offsetX);
                } else {
                    getPoints().set(i, points.get(i) + offsetY);
                }
            }

            x = event.getX();
            y = event.getY();
        });
    }

}
