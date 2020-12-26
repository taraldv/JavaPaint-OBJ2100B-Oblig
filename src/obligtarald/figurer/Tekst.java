/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package obligtarald.figurer;

import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 *
 * @author Tarald
 */
public class Tekst extends Text {

    private static int antall = 0;
    private final Pane parentPane;
    private double x;
    private double y;
    private boolean finished = false;

    public Tekst(double x, double y, Pane parent) {
        this.parentPane = parent;
        this.x = x;
        this.y = y;
        antall++;
    }

    public String getName() {
        return "Tekst" + antall;
    }

    public void finishTekst(String text) {
        if (!finished) {
            parentPane.getChildren().clear();
            setText(text);
            parentPane.getChildren().add(this);
            setX(x);
            setY(y);
            finished = true;
        }
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
