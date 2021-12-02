package mush;

import java.util.ArrayList;
import java.util.Random;

public class Vaisseau {

    //Nombre de salles dans le vaisseau
    private final int nbSalles = 27;

    //Nom du vaisseau
    private final String nom;

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
        this.initObjets();
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
     * Initialisation des objets aléatoirment dans les stockages des salles
     */
    private void initObjets() {

        //variable aléatoire
        Random ra = new Random();

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
            "Extracteur de spores",
            "Sérum rétro-fongique"};

        int[] nbrObjets = {1, 1, 4,
            1, 1, 1, 1, 3, 1, 2, 1, 1,
            2, 1, 30, 1, 1, 1};

        Salle Refectoire = new Salle("Réfectoire");
        Objet rationStandard = new Objet("Ration Standard");

        for (int i = 0; i < nbSalles; i++) {
            if (this.salles[i] == Refectoire) {
                for (int j = 0; j < 30; j++) {
                    this.salles[i].stockage.add(rationStandard);
                }
            }
        }

        for (int i = 0; i < nomObjets.length; i++) {
            for (int j = 0; j < nbrObjets[i]; j++) {

                Objet objet = new Objet(nomObjets[i]);

                this.salles[ra.nextInt(nbSalles - 1)].stockage.add(objet);
            }

        }

    }

    /**
     * @param key clé de la salle qu'on souhaite obtenir
     * @return la Salle voulue si elle existe, null sinon
     */
    public Salle getSalle(String key) {

        for (Salle salle : salles) {

            if (salle.getNom().equals(key)) {
                return salle;
            }

        }

        return null;

    }

    public Salle getSalleAvecCamera() {

        for (Salle salle : salles) {

            if (salle.isCameraInstalle()) {
                return salle;
            }

        }

        return null;

    }

    /**
     * Détermine les voisins d'une salle donnée par référence
     *
     * @param salle salle pour laquelle on souhaite obtenir les voisins
     * @return liste des voisins de salle si elle existe, null sinon
     */
    public ArrayList<Salle> getVoisins(Salle salle) {

        return getVoisinsByKey(salle.getNom());

    }

    /**
     * Détermine les voisins d'une salle donnée par un id
     *
     * @param salleKey clé de la salle pour laquelle on souhaite obtenir les
     * voisins
     * @return liste des voisins de salle si elle existe, null sinon
     */
    public ArrayList<Salle> getVoisinsByKey(String salleKey) {

        //Détermination de la salle 
        for (int i = 0; i < this.salles.length; i++) {

            Salle salle = this.salles[i];

            if (salle.getNom().equals(salleKey)) {
                return getVoisinsByIndex(i);
            }

        }

        return null;

    }

    /**
     * Détermine les voisins d'une salle donnée par un id
     *
     * @param salleIndex index de la salle pour laquelle on souhaite obtenir les
     * voisins
     * @return liste des voisins de salle si elle existe, null sinon
     */
    public ArrayList<Salle> getVoisinsByIndex(int salleIndex) {

        ArrayList<Salle> res = new ArrayList<>();

        for (int i = 0; i < this.graphVoisins.length; i++) {

            if (this.graphVoisins[salleIndex][i] == 1) {

                res.add(this.salles[i]);

            }

        }

        return res;

    }

    public int getArmure() {
        return this.armure;
    }

    public int getOxygene() {
        return this.oxygene;
    }

    public int getFuel() {
        return this.fuel;
    }
    
}
