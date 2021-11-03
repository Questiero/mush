/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mush;

import java.util.ArrayList;

/**
 *
 * @author hp
 */
public class Salle {

    private final String nom;
    // declaration de l'inventaire des obejts dans la salle 
    private ArrayList<Objet> stockage = new ArrayList<>();

    public Salle(String nom) {
        this.nom = nom;
    }
}
