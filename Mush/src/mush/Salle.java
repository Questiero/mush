package mush;

import java.util.ArrayList;

public class Salle {

    private final String nom;
    // declaration de l'inventaire des obejts dans la salle 
    private ArrayList<Objet> stockage = new ArrayList<>();

    public Salle(String nom) {
        this.nom = nom;
    }
}
