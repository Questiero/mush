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
    private boolean mush = false;

    //Compétences du joueur
    private final HashMap<String, Integer> competences = new HashMap<>();

    //Inventaire du joueur
    private final Objet[] inventaire = new Objet[tailleInventaire];
    
    //Position actuelle du Joueur, initialisé au Nexus
    private String positionKey = "Nexus";
    
    /**
     * Constructeur de Joueur
     *
     * @param nom nom du joueur
     */
   

    Joueur() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //getters des variables 
    public String getnom(){
        return nom;  
    }
    public boolean getmush(){
        return mush;  
    }
    public String getPosition(){
        return this.positionKey;   
    }
     public String getCompetences(){
        return null;   
    }
    //fonction pour afficher les caractéristiques d'un joueur 
    // Nom et si il est mush ou humain  

    /**
     *
     */
    public static void affichage(){
        Joueur personnage = new Joueur();
        personnage .getnom();
        personnage.getmush();
        personnage.getPosition();
        
       
        
    }
    public void transform() {
        this.mush = true;
    }
    
    public boolean isMush() {
        return this.mush;
    }
    /**
     * @return nom du personnage
     */
    @Override
    public String toString() {
        return this.nom;
    }
    

    /**
     * Permet d'ajouter une compétence au joueur
     *
     * @param competenceKey nom de la compétence a ajouter
     */
    public void addCompetence(String competenceKey) {

        switch (competenceKey) {

            case "Infirmier":
            case "Physicien":
            case "Technicien":
                this.competences.put(competenceKey, 1);
                break;
            case "Informaticien":
            case "Tireur":
                this.competences.put(competenceKey, 2);
                break;
            case "Cuistot":
                this.competences.put(competenceKey, 4);
                break;
            case "Astrophysicien":
            case "Biologiste":
            case "Bourreur":
            case "Concepteur":
            case "Détaché":
            case "Leader":
            case "Logistique":
            case "Mycologiste":
            case "Observateur":
            case "Optimiste":
            case "Paranoïaque":
            case "Pilote":
            case "Psy":
            case "Robuste":
            case "Seul espoir":
            case "Sprinter":
            case "Traqueur":
                this.competences.put(competenceKey, 0);
                break;
            default:

        }

    }

    /**
     * Retourne l'Integer associé à la compétence competenceKey si le joueur la
     * possède, null sinon
     *
     * @param competenceKey clé de la compétence à récupérer
     * @return Integer associé à la compétence si le joueur la possède, null
     * sinon
     */
    public Integer getCompetence(String competenceKey) {
        return this.competences.get(competenceKey);
    }
}
