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
    private HashMap<String, Integer> competences = new HashMap<>();

    //Inventaire du joueur
    private Objet[] inventaire = new Objet[tailleInventaire];

    /**
     * Constructeur de Joueur
     *
     * @param nom nom du joueur
     */
    public Joueur(String nom) {

        this.nom = nom;

    }

    /**
     * Retourne le nom du Personnage
     *
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
                this.competences.put(competenceKey, -1);
                break;
            default:

        }

    }

    /**
     * Retourne l'entier associé à la compétence competenceKey si le joueur la
     * possède, null sinon
     *
     * @param competenceKey clé de la compétence à récupérer
     * @return int associé à la compétence si le joueur la possède, null sinon
     */
    public int getCompetence(String competenceKey) {
        return this.competences.get(competenceKey).intValue();
    }

}
