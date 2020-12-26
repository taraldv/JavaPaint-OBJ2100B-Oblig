/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package obligtarald.figurer;

import java.util.ArrayList;

/**
 *
 * @author Tarald
 */
public class Geometri {

    
    //Regner ut avstand mellom to punkt
    public static double avstandMellomToPunkt(double x1, double y1, double x2, double y2) {
        return Math.hypot(Math.max(x1, x2) - Math.min(x1, x2), Math.max(y1, y2) - Math.min(y1, y2));
    }
    
    //Regner ut areal at et rektangel
    public static double arealAvRektangel(double x, double y) {
        return x * y;
    }

    //Regner ut areal av en sirkel
    public static double arealAvSirkel(double radius) {
        return radius * radius * Math.PI;
    }

    //Regner ut areal til en mangekant
    //Hentet fra https://www.mathopenref.com/coordpolygonarea.html 03.11.2018
    public static double arealAvMangekant(ArrayList<Double[]> list) {
        double sum = 0;
        for (int i = 0; i < list.size(); i++) {
            double xMultipliserMedNeste, yMultipliserMedNeste;
            double x = list.get(i)[0];
            double y = list.get(i)[1];
            
            //Bruker IndexOutOfBoundsException til å multiplisere siste element
            //med første element i listen
            try {
                xMultipliserMedNeste = x * list.get(i + 1)[1];
                yMultipliserMedNeste = y * list.get(i + 1)[0];
            } catch (IndexOutOfBoundsException e) {
                xMultipliserMedNeste = x * list.get(0)[1];
                yMultipliserMedNeste = y * list.get(0)[0];
            }

            sum += xMultipliserMedNeste - yMultipliserMedNeste;
        }
        return (Math.abs(sum / 2));
    }
}
