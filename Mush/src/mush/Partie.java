package mush;

import java.util.ArrayList;

public class Partie {
    
    private Vaisseau vaisseau;
    
    //Tableau contenant tout les joueurs de la partie
    private Joueur[] personnages = new Joueur[16];
    //Tableau dynamique contenant tout les joueurs contrôlés par des personnes
    private ArrayList<Joueur> joueurs = new ArrayList<>();
    //Tableau dynamique contenant tout les joueurs contrôlés par l'ordinateur
    private ArrayList<Joueur> ordinateurs = new ArrayList<>();
    
    /**
     * Constructeur de Partie
     */
    public Partie() {
        
        this.vaisseau = new Vaisseau("Daedalus");
        
        initPersonnages();
        
    }
    
    private void initPersonnages() {
        
        
        
    }
    
}
