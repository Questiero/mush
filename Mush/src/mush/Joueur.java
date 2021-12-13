package mush;

import java.util.Arrays;
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
    private int pmo = 7;

    private boolean enVie = true;

    //Type de joueur (humain, mush)
    private boolean mush = false;
    private boolean peutPoinconner = false;
    private int nbrSpores = 0;

    //Compétences du joueur
    private final HashMap<String, Integer> competences = new HashMap<>();

    //Inventaire du joueur
    private final Objet[] inventaire = new Objet[tailleInventaire];

    //Position actuelle du Joueur, initialisé au Nexus
    private String positionKey = "Laboratoire";

    private boolean peutCaresser = true;
    private boolean estCouche = false;
    private boolean estSale = false;
    private int sasiete = 0;

    /**
     * Constructeur de Joueur
     *
     * @param nom nom du joueur
     */
    public Joueur(String nom) {

        this.nom = nom;

        //Initialisation de l'inventaire à des objets vides
        for (int i = 0; i < this.tailleInventaire; i++) {
            this.inventaire[i] = new Objet("Rien");
        }

    }

    Joueur() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //getters des variables 
    public String getNom() {
        return nom;
    }

    public boolean getMush() {
        return mush;
    }

    public int getPv() {
        return this.pv;
    }

    public int getPa() {
        return this.pa;
    }

    public int getPm() {
        return this.pm;
    }

    public int getPmo() {
        return this.pmo;
    }

    public void removePv(int n) {
        this.pv -= n;
        if (this.pv <= 0) {
            this.pv = 0;
            this.meurt();
        }
    }

    public void removePa(int n) {
        this.pa -= n;
        if (this.pa < 0) {
            this.pa = 0;
        }
    }

    public void removePm(int n) {
        this.pm -= n;
        if (this.pm < 0) {
            this.pm = 0;
        }
    }

    public void removePmo(int n) {
        this.pmo -= n;
        if (this.pmo < 0) {
            this.pmo = 0;
        }
    }

    public void addPv(int n) {
        this.pv += n;
        if (this.pv > this.maxPV) {
            this.pv = this.maxPV;
        }
    }

    public void addPa(int n) {
        this.pa += n;
        if (this.pa > this.maxPA) {
            this.pa = this.maxPA;
        }
    }

    public void addPm(int n) {
        this.pm += n;
        if (this.pm > this.maxPM) {
            this.pm = this.maxPM;
        }
    }

    public void addPmo(int n) {
        this.pmo += n;
        if (this.pmo > this.maxPMO) {
            this.pmo = this.maxPMO;
        }
    }

    public void meurt() {
        this.enVie = false;
    }

    public boolean estEnVie() {
        return this.enVie;
    }

    public Objet[] getInventaire() {
        return inventaire;
    }

    public boolean inventairePlein() {
        for (Objet objet : inventaire) {
            if (objet.getNom().equals("Rien")) {
                return false;
            }
        }
        return true;
    }

    public void addObjet(Objet objet) {

        if (this.inventaire[0].getNom().equals("Rien")) {
            this.inventaire[0] = new Objet(objet.getNom());
        } else if (this.inventaire[1].getNom().equals("Rien")) {
            this.inventaire[1] = new Objet(objet.getNom());
        } else if (this.inventaire[2].getNom().equals("Rien")) {
            this.inventaire[2] = new Objet(objet.getNom());
        }

    }

    public void removeObjet(Objet objet) {
        if (this.inventaire[0].getNom().equals(objet.getNom())) {
            this.inventaire[0] = new Objet("Rien");
        } else if (this.inventaire[1].getNom().equals(objet.getNom())) {
            this.inventaire[1] = new Objet("Rien");
        } else if (this.inventaire[2].getNom().equals(objet.getNom())) {
            this.inventaire[2] = new Objet("Rien");
        }

    }

    public HashMap<String, Integer> getCompetences() {
        return this.competences;
    }

    public void toogleEstCouche() {
        this.estCouche = !this.estCouche;
    }

    public boolean estCouche() {
        return this.estCouche;
    }

    public void sali() {
        this.estSale = false;
    }

    public void douche() {
        this.estSale = true;
    }

    public boolean estSale() {
        return this.estSale;
    }

    public void mange() {
        this.sasiete = 3;
    }

    public int getSasiete() {
        return this.sasiete;
    }

    public void waitSasiete() {
        if (this.sasiete != 0) {
            this.sasiete--;
        }
    }

    public boolean getPeutCaresser() {
        return this.peutCaresser;
    }

    public void setPeutCaresser(boolean b) {
        this.peutCaresser = b;
    }

    public void affichageEtatJoueur() {

        //TODO clean un peu
        System.out.println("\nEtat actuel de " + this.getNom() + ":");
        System.out.println("PV: " + this.pv + "/" + this.maxPV);
        System.out.println("PA: " + this.pa + "/" + this.maxPA);
        System.out.println("PM: " + this.pm + "/" + this.maxPM);
        System.out.println("PMO: " + this.pmo + "/" + this.maxPMO);
        System.out.println("MUSH ? " + this.getMush());
        System.out.println("Inventaire: " + Arrays.toString(this.getInventaire()));
        System.out.println("Competences: " + this.getCompetences());
        System.out.println("Position: " + this.positionKey);
    }
    //méthode  pour afficher les caractéristiques d'un joueur 
    // Nom et si il est mush ou humain  
    // PV, PA, PM, PMO, contenu de l'inventaire et les competences

    /**
     *
     */
    public void transform() {
        this.mush = true;
        this.peutPoinconner = true;
    }

    public boolean isMush() {
        return this.mush;
    }

    public boolean getPeutPoinconner() {
        return this.peutPoinconner;
    }

    public void setPeutPoinconner(boolean b) {
        this.peutPoinconner = b;
    }

    public int getSpores() {
        return this.nbrSpores;
    }

    public void removeSpore(int n) {
        this.nbrSpores -= n;
        if (this.nbrSpores < 0) {
            this.nbrSpores = 0;
        }
    }

    public void addSpore(int n) {
        this.nbrSpores += n;

        if (this.mush) {
            if (this.nbrSpores > 2) {
                this.nbrSpores = 2;
            }
        } else if (this.nbrSpores > 3) {
            this.nbrSpores = 0;
            this.transform();
        }
    }

    public String getPositionKey() {
        return this.positionKey;
    }

    public void setPositionKey(String key) {
        this.positionKey = key;
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

    public boolean hasCompetence(String competenceKey) {
        return this.competences.containsKey(competenceKey);
    }

    public boolean competenceEquals(String key, int n) {
        return Integer.valueOf(n).equals(this.competences.get(key));
    }

    public void setCompetence(String competenceKey, int n) {
        this.competences.replace(competenceKey, n);
    }

    public boolean hasObjet(String nom) {

        for (Objet objet : this.inventaire) {
            if (objet.getNom().equals(nom)) {
                return true;
            }
        }

        return false;

    }

    public boolean estDans(String key) {
        return this.positionKey.equals(key);
    }

}
