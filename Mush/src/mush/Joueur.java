package mush;

import java.util.HashMap;

public class Joueur {

    //Constantes
    private final int tailleInventaire = 3;
    
    //Nom du joueur
    private final String nom;

    //Caractéristiques maximales de joueur
    private final int maxPM = 12;
    private final int maxPA = 12;
    private final int maxPV = 14;
    private final int maxPMO = 14;
    //Caractéristiques de joueur
    private int pm = maxPM;
    private int pa = maxPA;
    private int pv = maxPV;
    private int pmo = maxPMO;

    //Type de joueur (humain, mush)
    boolean mush = false;
    
    //Compétences du joueur
    private HashMap<String, String> competences = new HashMap<>();

    //Inventaire du joueur
    private Objet[] inventaire = new Objet[tailleInventaire];

    /**
     * Constructeur de Joueur
     * @param nom nom du joueur
     */
    public Joueur(String nom) {
        
        this.nom = nom;

    }

    @Override
    public String toString() {
        return "a";
    }
    
}
