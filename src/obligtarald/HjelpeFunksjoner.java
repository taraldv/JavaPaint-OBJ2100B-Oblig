/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package obligtarald;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import obligtarald.figurer.Linje;
import obligtarald.figurer.Mangekant;
import obligtarald.figurer.Rektangel;
import obligtarald.figurer.Sirkel;
import obligtarald.figurer.Tekst;

/**
 *
 * @author Tarald
 */
public class HjelpeFunksjoner {

    //Siden figurere mine ikke har en felles superclass jeg kan endre på,
    //må hver figur ha sin egen drag funksjon.
    public static void enableShapeDrag(Shape shape) {
        if (shape instanceof Linje) {
            Linje tempLinje = (Linje) shape;
            tempLinje.enableDrag();
        } else if (shape instanceof Sirkel) {
            Sirkel tempSirkel = (Sirkel) shape;
            tempSirkel.enableDrag();
        } else if (shape instanceof Mangekant) {
            Mangekant tempMangekant = (Mangekant) shape;
            tempMangekant.enableDrag();
        } else if (shape instanceof Rektangel) {
            Rektangel tempRektangel = (Rektangel) shape;
            tempRektangel.enableDrag();
        } else if (shape instanceof Tekst) {
            Tekst tempTekst = (Tekst) shape;
            tempTekst.enableDrag();
        }
    }

    //Samme problem som i enableShapeDrag
    public static String getAreaOfShape(Shape shape) {
        if (shape instanceof Linje) {
            Linje tempLinje = (Linje) shape;
            return Double.toString(tempLinje.getLength());
        } else if (shape instanceof Sirkel) {
            Sirkel tempSirkel = (Sirkel) shape;
            return Double.toString(tempSirkel.getArea());
        } else if (shape instanceof Mangekant) {
            Mangekant tempMangekant = (Mangekant) shape;
            return Double.toString(tempMangekant.getArea());
        } else if (shape instanceof Rektangel) {
            Rektangel tempRektangel = (Rektangel) shape;
            return Double.toString(tempRektangel.getArea());
        } else if (shape instanceof Tekst) {
            Tekst tempTekst = (Tekst) shape;
            return tempTekst.getText();
        } else {
            return "0";
        }
    }

    //Henter farge fra TextField lagret i en arrayliste og gjør de om til en Color
    public static Color getColorFromInputList(int index, ArrayList<TextField> list) {
        int red = Integer.parseInt(list.get(index).getText());
        int green = Integer.parseInt(list.get(index + 1).getText());
        int blue = Integer.parseInt(list.get(index + 2).getText());
        return Color.rgb(red, green, blue);

    }

    //Fjerner alle elementer fra pane, så legges shape til og mouseClick event fjernes fra pane
    public static void drawShape(Shape shape, Pane pane) {
        pane.getChildren().clear();
        pane.getChildren().add(shape);
        pane.setOnMouseClicked(null);
    }

    //Fargelegger både stroke og fill til figuren
    public static void colorShape(Shape shape, ArrayList<TextField> list) {
        shape.setFill(HjelpeFunksjoner.getColorFromInputList(0, list));
        shape.setStroke(HjelpeFunksjoner.getColorFromInputList(3, list));
    }

    //Legger til en liten sirkel hvor musen klikket, får å hjelpe til med tegning av figurene.
    public static void addDot(MouseEvent event, Pane pane) {
        double dotX = event.getX();
        double dotY = event.getY();
        Circle tempDot = new Circle(dotX, dotY, 1);
        pane.getChildren().add(tempDot);
    }

    //Returnerer hvilken index en valgt figur har i StackPane ved hjelp av figurnavnet
    //som blir lagret i "UserData" til figurens parent node
    public static int figurIndex(String selectedString, StackPane pane) {
        for (int i = 0; i < pane.getChildren().size(); i++) {
            Node tempNode = pane.getChildren().get(i);
            String nodeData = (String) tempNode.getUserData();
            if (selectedString.equals(nodeData)) {
                return i;
            }
        }
        return -1;
    }

    //Oppdaterer Combobox med figurene sitt navn basert på rekkefølgen i StackPane
    public static void updateDropdown(StackPane pane, ComboBox comboBox) {
        ObservableList<Node> stackPaneChildren = pane.getChildren();
        ObservableList<String> tempList = FXCollections.observableArrayList();
        for (Node node : stackPaneChildren) {
            tempList.add((String) node.getUserData());
        }
        comboBox.setItems(tempList);
    }

    //Går igjennom alle shapes i StackPane og setter mousePressed og dragged til null
    public static void disableAllDragging(StackPane pane) {
        ObservableList<Node> stackPaneChildren = pane.getChildren();
        for (Node node : stackPaneChildren) {
            Shape shape = (Shape) ((Pane) node).getChildren().get(0);
            shape.setOnMouseDragged(null);
            shape.setOnMousePressed(null);
        }
    }

    //Setter alle panes i StackPane til transparent (slik at mus klikk går igjennom)
    //frem til det pane man vil samhandle med
    public static void setPanesBeforeToTransparent(int indexOfClickablePane, StackPane pane) {
        ObservableList<Node> stackPaneChildren = pane.getChildren();
        Pane selectedNode = (Pane) stackPaneChildren.get(indexOfClickablePane);
        for (int i = stackPaneChildren.size() - 1; i >= 0; i--) {
            if (stackPaneChildren.get(i) == selectedNode) {
                break;
            } else {
                stackPaneChildren.get(i).setMouseTransparent(true);
            }
        }
    }

    //Setter alle pane i StackPane til default (slik at de fanger opp mus klikk)
    public static void resetMouseTransparent(StackPane pane) {
        ObservableList<Node> stackPaneChildren = pane.getChildren();
        for (Node node : stackPaneChildren) {
            node.setMouseTransparent(false);
        }
    }

    public static String changeIndexOrder(StackPane pane, ComboBox<String> dropDown, int direction) {
        ObservableList<Node> stackPaneChildren = pane.getChildren();

        int maxIndex = stackPaneChildren.size();
        String selected = (String) dropDown.getValue();
        int selectedIndex = HjelpeFunksjoner.figurIndex(selected, pane);
        Node selectedNode = stackPaneChildren.get(selectedIndex);
        if (selectedIndex + direction < maxIndex && selectedIndex + direction >= 0) {
            stackPaneChildren.remove(selectedNode);
            stackPaneChildren.add(selectedIndex + direction, selectedNode);
            HjelpeFunksjoner.updateDropdown(pane, dropDown);
            return "Figur z posisjon: " + (selectedIndex + direction);
        }
        return null;
    }

}
