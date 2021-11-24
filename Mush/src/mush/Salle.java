package mush;

import java.util.ArrayList;
import java.util.Random;

public class Salle {

    //Nom de la salle
    private final String nom;
    //Stockage de la salle
    public final ArrayList<Objet> stockage = new ArrayList<>();

    //variable aléatoir pour la répartition aléatoire des objets dans les salles
    /**
     * Constructeur de Salle
     *
     * @param nom nom de la Salle
     */
    public Salle(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return this.nom;
    }

}
