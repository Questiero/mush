package mush;

import java.util.ArrayList;

public class Salle {

    //Nom de la salle
    private final String nom;
    
    //Stockage de la salle 
    private final ArrayList<Objet> stockage = new ArrayList<>();
    
    /**
     * Constructeur de Salle
     * 
     * @param nom nom de la Salle
     */
    public Salle(String nom) {
        this.nom = nom;
    }
    
}
