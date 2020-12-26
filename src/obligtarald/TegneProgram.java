package obligtarald;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import obligtarald.figurer.Linje;
import obligtarald.figurer.Mangekant;
import obligtarald.figurer.Rektangel;
import obligtarald.figurer.Sirkel;
import obligtarald.figurer.Tekst;

/**
 *
 * @author Tarald
 */
public class TegneProgram extends Application {

    private final BorderPane root = new BorderPane();
    private final StackPane centerPane = new StackPane();
    private final ComboBox figurDropDown = new ComboBox();
    private final ArrayList<TextField> inputList = new ArrayList<>();
    private final Text valgtFigur = new Text();
    private final Text areaEllerLengde = new Text();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        root.setTop(topPane());

        centerPane.setMinHeight(700);
        centerPane.setMinWidth(700);
        centerPane.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        root.setCenter(centerPane);

        FlowPane right = rightPane();
        right.setPadding(new Insets(5, 5, 5, 5));
        root.setRight(right);

        FlowPane left = leftPane();
        root.setLeft(left);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Tegne Program");
        stage.show();

    }

    //Høyre side av programmet
    private FlowPane rightPane() {
        FlowPane flow = new FlowPane(Orientation.VERTICAL, 0, 10);
        FlowPane fyllfarge = fargePane("Fyllfarge:");
        FlowPane linjefarge = fargePane("Linjefarge:");
        Button applyColorButton = new Button("Bruk fargen");

        //Knytter knappen til et event som endrer fargen til figuren som er valgt
        applyColorButton.setOnAction((event) -> {
            String selected = valgtFigur.getText();
            int selectedIndex = HjelpeFunksjoner.figurIndex(selected, centerPane);

            //Sjekker om figuren ble funnet (-1 betyr ikke funnet)
            if (selectedIndex >= 0) {
                ObservableList<Node> stackPaneChildren = centerPane.getChildren();
                Pane selectedNode = (Pane) stackPaneChildren.get(selectedIndex);
                Shape shape = (Shape) selectedNode.getChildren().get(0);
                HjelpeFunksjoner.colorShape(shape, inputList);
            }

        });
        flow.getChildren().addAll(
                new Text("Valgt figur:"),
                valgtFigur,
                new Text("Areal/Linjelengde/Tekst:"),
                areaEllerLengde,
                linjefarge,
                fyllfarge,
                applyColorButton
        );
        return flow;
    }

    //Returnerer et vertikalt flowpane som inneholder en Text og 3 flowpanes med
    //hver sin TextField og tekst fra spektrum arrayet
    private FlowPane fargePane(String tekst) {
        FlowPane flow = new FlowPane(Orientation.VERTICAL, 0, 10);
        flow.setMaxHeight(150);
        Text tempText = new Text(tekst);
        flow.getChildren().add(tempText);
        String[] spektrum = {"R", "G", "B"};
        for (String string : spektrum) {
            TextField fargeInput = new TextField("0");
            inputList.add(fargeInput);
            fargeInput.setMaxWidth(50);
            FlowPane tempPane = new FlowPane(new Text(string), fargeInput);
            tempPane.setMaxWidth(50);
            flow.getChildren().add(tempPane);
        }
        return flow;
    }

    private FlowPane leftPane() {
        FlowPane flow = new FlowPane(Orientation.VERTICAL, 0, 10);
        Text posText = new Text("Figur z posisjon: ");

        figurDropDown.setMinWidth(110);
        figurDropDown.setVisibleRowCount(15);
        figurDropDown.valueProperty().setValue("Velg figur");

        //Legger til event som fyrer når et nytt element blir valgt fra dropdown 
        figurDropDown.valueProperty().addListener((event) -> {
            String selected = (String) figurDropDown.getValue();
            valgtFigur.setText(selected);
            //Sjekker om den ikke er null får å unngå error når 
            //listen endrer seg mens den er valgt
            if (selected != null) {
                int selectedIndex = HjelpeFunksjoner.figurIndex(selected, centerPane);
                ObservableList<Node> stackPaneChildren = centerPane.getChildren();
                Pane selectedNode = (Pane) stackPaneChildren.get(selectedIndex);
                HjelpeFunksjoner.disableAllDragging(centerPane);
                HjelpeFunksjoner.resetMouseTransparent(centerPane);
                //Prøver å gjøre valgt figur "draggable" og 
                //henter areal/lengde/tekst til valgt figur
                try {
                    Shape shape = (Shape) selectedNode.getChildren().get(0);
                    HjelpeFunksjoner.setPanesBeforeToTransparent(selectedIndex, centerPane);
                    HjelpeFunksjoner.enableShapeDrag(shape);
                    areaEllerLengde.setText(HjelpeFunksjoner.getAreaOfShape(shape));
                } catch (IndexOutOfBoundsException e) {
                    areaEllerLengde.setText("0");
                }
                posText.setText("Figur z posisjon: " + selectedIndex);
            }
        });

        Button endreIndexPositiv = new Button("++");
        Button endreIndexNegativ = new Button("--");
        endreIndexPositiv.setMinWidth(55);
        endreIndexNegativ.setMinWidth(55);

        //Event som fyrer når "++ "knapp trykkes på
        //flytter valgt figur fremover og oppdaterer tekst hvis mulig
        endreIndexPositiv.setOnAction((event) -> {
            String nyPos = HjelpeFunksjoner.changeIndexOrder(centerPane, figurDropDown, 1);
            if (nyPos != null) {
                posText.setText(nyPos);
            }

        });

        //Event som fyrer når "--" knapp trykkes på
        //flytter valgt figur bakover og oppdaterer tekst hvis mulig
        endreIndexNegativ.setOnAction((event) -> {
            String nyPos = HjelpeFunksjoner.changeIndexOrder(centerPane, figurDropDown, -1);
            if (nyPos != null) {
                posText.setText(nyPos);
            }
        });

        flow.getChildren().add(figurDropDown);
        flow.getChildren().add(posText);
        FlowPane subPane = new FlowPane(endreIndexPositiv, endreIndexNegativ);
        subPane.setMaxWidth(110);
        flow.getChildren().add(subPane);

        return flow;
    }

    //Flowpane som inneholder knapper til de forskjellige figurene
    private FlowPane topPane() {
        FlowPane flow = new FlowPane();

        Button lineButton = new Button("Ny linje");
        //Knytter event til ny linja knapp
        lineButton.setOnAction((buttonEvent) -> {
            HjelpeFunksjoner.disableAllDragging(centerPane);

            Pane tempPane = new Pane();
            Linje tempLine = new Linje();
            tempPane.setUserData(tempLine.getName());
            centerPane.getChildren().add(tempPane);
            //Knytter event til når tempPane klikkes på med mus
            tempPane.setOnMouseClicked((paneEvent) -> {
                //Legger til en liten sirkel hvor musen klikket
                HjelpeFunksjoner.addDot(paneEvent, tempPane);

                //Legger til et punkt (x,y) til tempLine
                tempLine.addPoint(paneEvent.getX(), paneEvent.getY());

                //Sjekker om tempLine er ferdig (2 punkter)
                if (tempLine.isFinished()) {
                    valgtFigur.setText(tempLine.getName());
                    areaEllerLengde.setText(Double.toString(tempLine.getLength()));
                    HjelpeFunksjoner.updateDropdown(centerPane, figurDropDown);
                    HjelpeFunksjoner.colorShape(tempLine, inputList);
                    HjelpeFunksjoner.enableShapeDrag(tempLine);

                    //Fjerner små sirkler og legger tempLine til tempPane
                    HjelpeFunksjoner.drawShape(tempLine, tempPane);
                }
            });
        });

        //Knapp for rektangel, gjør det samme som linje knapp, men med annen figur
        Button rectButton = new Button("Ny rektangel");
        rectButton.setOnAction((buttonEvent) -> {
            HjelpeFunksjoner.disableAllDragging(centerPane);

            Pane tempPane = new Pane();
            Rektangel tempRect = new Rektangel();
            tempPane.setUserData(tempRect.getName());
            centerPane.getChildren().add(tempPane);
            tempPane.setOnMouseClicked((paneEvent) -> {
                HjelpeFunksjoner.addDot(paneEvent, tempPane);
                tempRect.addPoint(paneEvent.getX(), paneEvent.getY());
                if (tempRect.isFinished()) {
                    valgtFigur.setText(tempRect.getName());
                    areaEllerLengde.setText(Double.toString(tempRect.getArea()));
                    HjelpeFunksjoner.updateDropdown(centerPane, figurDropDown);
                    HjelpeFunksjoner.colorShape(tempRect, inputList);
                    HjelpeFunksjoner.enableShapeDrag(tempRect);
                    HjelpeFunksjoner.drawShape(tempRect, tempPane);
                }
            });
        });

        //Knapp for sirkel, gjør det samme som linje knapp, men med annen figur
        Button circleButton = new Button("Ny sirkel");
        circleButton.setOnAction((buttonEvent) -> {
            HjelpeFunksjoner.disableAllDragging(centerPane);

            Pane tempPane = new Pane();
            Sirkel tempCircle = new Sirkel();
            tempPane.setUserData(tempCircle.getName());
            centerPane.getChildren().add(tempPane);
            tempPane.setOnMouseClicked((paneEvent) -> {
                HjelpeFunksjoner.addDot(paneEvent, tempPane);
                tempCircle.addPoint(paneEvent.getX(), paneEvent.getY());
                if (tempCircle.isFinished()) {
                    valgtFigur.setText(tempCircle.getName());
                    areaEllerLengde.setText(Double.toString(tempCircle.getArea()));
                    HjelpeFunksjoner.updateDropdown(centerPane, figurDropDown);
                    HjelpeFunksjoner.colorShape(tempCircle, inputList);
                    HjelpeFunksjoner.enableShapeDrag(tempCircle);
                    HjelpeFunksjoner.drawShape(tempCircle, tempPane);
                }
            });
        });

        //Knapp for mangekant, gjør det samme som linje knapp, men med annen figur
        Button polyButton = new Button("Ny polygon");
        polyButton.setOnAction((buttonEvent) -> {
            HjelpeFunksjoner.disableAllDragging(centerPane);

            Pane tempPane = new Pane();
            Mangekant tempPolygon = new Mangekant();
            tempPane.setUserData(tempPolygon.getName());
            centerPane.getChildren().add(tempPane);
            tempPane.setOnMouseClicked((paneEvent) -> {
                HjelpeFunksjoner.addDot(paneEvent, tempPane);
                tempPolygon.addPoint(paneEvent.getX(), paneEvent.getY());
                if (tempPolygon.isFinished()) {
                    valgtFigur.setText(tempPolygon.getName());
                    areaEllerLengde.setText(Double.toString(tempPolygon.getArea()));
                    HjelpeFunksjoner.updateDropdown(centerPane, figurDropDown);
                    HjelpeFunksjoner.colorShape(tempPolygon, inputList);
                    HjelpeFunksjoner.enableShapeDrag(tempPolygon);
                    HjelpeFunksjoner.drawShape(tempPolygon, tempPane);
                }
            });
        });

        //Knapp som legger til tekst
        Button textButton = new Button("Tekst");
        textButton.setOnAction((buttonEvent) -> {
            HjelpeFunksjoner.disableAllDragging(centerPane);

            Pane tempPane = new Pane();
            centerPane.getChildren().add(tempPane);

            //Knytter event til mouseclick på tempPane, som lager en midlertidig textField
            //på samme (x,y) punkt.
            tempPane.setOnMouseClicked((paneEvent) -> {
                Tekst tempTekst = new Tekst(paneEvent.getX(), paneEvent.getY(), tempPane);
                valgtFigur.setText(tempTekst.getName());
                tempPane.setUserData(tempTekst.getName());
                tempPane.setOnMouseClicked(null);
                TextField brukerInput = new TextField();

                //Når textfield mister fokus fjernes textfield og en tekst
                //figur med samme innhold settes inn på samme (x,y) punkt
                brukerInput.focusedProperty().addListener((changeEvent) -> {
                    ReadOnlyBooleanProperty harFokusBoolean = (ReadOnlyBooleanProperty) changeEvent;
                    if (!harFokusBoolean.get()) {
                        tempTekst.finishTekst(brukerInput.getText());
                        HjelpeFunksjoner.colorShape(tempTekst, inputList);
                        HjelpeFunksjoner.enableShapeDrag(tempTekst);
                        HjelpeFunksjoner.updateDropdown(centerPane, figurDropDown);
                    }
                });

                //Hvis text input fanger opp en ENTER key utføres det samme som
                //skjedde når textfield mister fokus
                brukerInput.setOnKeyReleased((keyEvent) -> {
                    if (keyEvent.getCode() == KeyCode.ENTER) {
                        tempTekst.finishTekst(brukerInput.getText());
                        HjelpeFunksjoner.colorShape(tempTekst, inputList);
                        HjelpeFunksjoner.enableShapeDrag(tempTekst);
                        HjelpeFunksjoner.updateDropdown(centerPane, figurDropDown);
                    }
                });

                tempPane.getChildren().add(brukerInput);

                brukerInput.setTranslateX(paneEvent.getX());
                brukerInput.setTranslateY(paneEvent.getY());

            });
        });

        flow.getChildren().addAll(lineButton, rectButton, circleButton, polyButton, textButton);
        return flow;
    }

}
