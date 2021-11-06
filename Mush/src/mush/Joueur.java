package mush;

public class Joueur {
    //Nombre des joueurs 
    private final int nbJoueurs = 16;
    
    //Nom du joueur
    private final String nom;
    
    //Type de joueur ( humain , mush )
    boolean mush;
    
    //Caractéristiques maximales de joueur
    private final int maxPM = 12;
    private final int maxPA = 12;
    private final int maxPV = 14;
    private final int maxPMO = 14;
    
    //Caractéristiques de joueur
    private int pm;
    private int pa;
    private int pv;
    private int pmo;
    
    //Compétences des joueurs
    private Competence competence;
    
    //Contenu de l'inventaire
    private Objet[] inventaire;

    private Joueur(String nom) {
        this.nom = nom;
        this.mush = false;
        this.pa = maxPM;
        this.pm = maxPA;
        this.pv = maxPV;
        this.pmo = maxPMO;
        this.competence;
        this.inventaire;
        
    }
}
