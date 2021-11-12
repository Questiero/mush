package mush;

import java.util.Random;

public class Vaisseau {

    //Nombre de salles dans le vaisseau
    private final int nbSalles = 27;

    //Nom du vaisseau
    private final String nom;

    //variable aléatoire
    Random ra = new Random();

    //Caractéristiques maximales du Vaisseau
    private final int maxArmure = 200;
    private final int maxOxygene = 500;
    private final int maxFuel = 50;
    //Caractéristiques du Vaisseau
    private int armure = maxArmure;
    private int oxygene = maxOxygene;
    private int fuel = maxFuel;

    //Tableau des salles
    private final Salle[] salles = new Salle[nbSalles];

    //Matrice du graph représentant les déplacement possibles entre les salles du vaisseau
    private final int[][] graphVoisins = {
        {0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0}};

    /**
     * Constructeur de Vaisseau
     *
     * @param nom nom du Vaisseau
     */
    public Vaisseau(String nom) {

        //Initialisation du nom à celui donné en paramètre
        this.nom = nom;

        this.initSalles();

    }

    /**
     * Initialisation des salles du vaisseau dans un ordre connu
     */
    private void initSalles() {

        String[] nomSalles = {"Pont",
            "Tourelle Alpha avant",
            "Tourelle Beta avant",
            "Couloir avant",
            "Jardin Hydrophonique",
            "Laboratoire",
            "Stockage avant",
            "Infirmerie",
            "Tourelle Alpha centre",
            "Tourelle Beta centre",
            "Couloir central",
            "Baie Alpha",
            "Baie Beta",
            "Stockage Alpha centre",
            "Stockage Beta centre",
            "Réfectoire",
            "Dortoir Alpha",
            "Dortoir Beta",
            "Nexus",
            "Couloir arrière",
            "Baie Alpha 2",
            "Baie Icarus",
            "Stockage Alpha arrière",
            "Stockage Beta arrière",
            "Tourelle Alpha arrière",
            "Tourelle Beta arrière",
            "Salle des moteurs"};

        for (int i = 0; i < nbSalles; i++) {

            this.salles[i] = new Salle(nomSalles[i]);

        }

    }
    
    /**
     * Initialisation des objets de stockages aléatoirment dans les salles
     */
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

                int randIndex = ra.nextInt(nbSalles - 1);

                Objet objet = new Objet(nomObjets[i]);

                this.salles[randIndex].stockage.add(objet);
            }

        }

    }

}
