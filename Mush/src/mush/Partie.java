package mush;

import java.util.ArrayList;

public class Partie {

    //Nombre des joueurs 
    private final int nbJoueurs = 16;

    private Vaisseau vaisseau;

    //Valeur contenant le jour actuel
    private int jour;
    //Valeur contenant le cycle actuel
    private int cycle;

    //Tableau contenant tout les joueurs de la partie
    private Joueur[] personnages = new Joueur[nbJoueurs];
    //Tableau dynamique contenant tout les joueurs contrôlés par des personnes
    private ArrayList<Joueur> joueurs = new ArrayList<>();
    //Tableau dynamique contenant tout les joueurs contrôlés par l'ordinateur
    private ArrayList<Joueur> ordinateurs = new ArrayList<>();

    /**
     * Constructeur de Partie
     */
    public Partie() {

        this.vaisseau = new Vaisseau("Daedalus");

        this.initPersonnages();

    }

    private void initPersonnages() {
        //TODO
        
        String[] nomPersonnages = {"Wang Chao",
            "Zhong Chun",
            "Eleesha Williams",
            "Finola Keegan",
            "Frieda Bergmann",
            "Gioele Rinaldao",
            "Jiang Hua",
            "Ian Soulton",
            "Janice Kent",
            "Kim Jin Su",
            "Lai Kuan-Ti",
            "Paola Rinaldo",
            "Raluca Tomescu",
            "Roland Zuccali",
            "Stephen Seagull",
            "Terrence Archer"};
        
        for (int i = 0; i < nbJoueurs; i++) {
            this.personnages[i] = new Joueur(nomPersonnages[i]);
        }
        
    }

    /**
     * Incrémentation du cycle en tenant compte de la limite de 8 cycles par
     * jour
     */
    private void nextCycle() {

        this.cycle++;

        if (this.cycle == 9) {

            this.cycle = 1;

            //TODO Code chaque cycle (ex: +1PA, +1PM pour tout les joueurs)
            nextDay();

        }

    }

    /**
     * Incrémentaiton du jour
     */
    private void nextDay() {

        this.jour++;

        //TODO Code chaque jour (ex: reset Infirmier)
    }

    /**
     * Lance la partie
     */
    public void start() {
        //TODO
    }

    /**
     * Arrête la partie
     */
    public void end() {
        //TODO
    }

}
