package mush;

import java.util.ArrayList;
import java.util.Random;

public class Salle {

    //Nom de la salle
    private final String nom;
    //Stockage de la salle
    private final ArrayList<Objet> stockage = new ArrayList<>();
    //variable aléatoir pour la répartition aléatoire des objets dans les salles
    private Random ra = new Random();

    /**
     * Constructeur de Salle
     *
     * @param nom nom de la Salle
     */
    public Salle(String nom) {
        this.nom = nom;
    }

    private void initObjets() {

        String[] nomObjets = {"Armure",
            "Clé à molette",
            "Combinaisons",
            "Paire de gants de protection",
            "Savon",
            "Tablier intachable",
            "Trottinette",
            "Extincteurs",
            "Couteau",
            "Blasters",
            "Grenade",
            "Médikit",
            "Caméras",
            "Souche de test mush",
            "Débris métallique",
            "Chat de Shrödinger",
            "ration standard",
            "Extracteur de spores",
            "Sérum rétro-fongique"};

        int[] nbrObjets = {1, 1, 4,
            1, 1, 1, 1, 3, 1, 2, 1, 1,
            2, 1, 30, 1, 30, 1, 1};

        for (int i = 0; i < nomObjets.length; i++) {
            for (int j = 0; j < nbrObjets[i]; j++) {
                int randomNom = ra.nextInt((nomObjets.length - 1) - 1) + 1;
                Objet objet = new Objet(nomObjets[randomNom]);
                stockage.add(objet);
            }
        }

    }
 
}
