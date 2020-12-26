/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package obligtarald.figurer;

import java.util.ArrayList;
import javafx.scene.shape.Line;

/**
 *
 * @author Tarald
 */
public class Linje extends Line {

    private final ArrayList<Double[]> pointList = new ArrayList<>();
    private boolean finished = false;
    private static int antall = 0;
    private double length;
    private double x;
    private double y;

    public Linje() {
        antall++;
    }

    public String getName() {
        return "Linje" + antall;
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

    //Sjekker når linjen er ferdig (2 punkter)
    private void checkSize() {
        if (pointList.size() == 2) {
            double x1 = pointList.get(0)[0];
            double y1 = pointList.get(0)[1];
            double x2 = pointList.get(1)[0];
            double y2 = pointList.get(1)[1];

            //Setter koordinatene til linjen
            setStartX(x1);
            setStartY(y1);
            setEndX(x2);
            setEndY(y2);

            //Setter lengen til linjen
            length = Geometri.avstandMellomToPunkt(x1, y1, x2, y2);
            finished = true;
        }
    }

    public double getLength() {
        return Math.round(length);
    }

    public void enableDrag() {

        setOnMousePressed((event) -> {
            x = event.getX();
            y = event.getY();
        });

        setOnMouseDragged((event) -> {
            double offsetX = event.getX() - x;
            double offsetY = event.getY() - y;

            setStartX(getStartX() + offsetX);
            setStartY(getStartY() + offsetY);
            setEndX(getEndX() + offsetX);
            setEndY(getEndY() + offsetY);

            x = event.getX();
            y = event.getY();
        });
    }

}
