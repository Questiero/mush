package mush;

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
            "Tourelle Alpha arrière",
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

}
