package mush;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class Salle {

    //Nom de la salle
    private final String nom;
    //Stockage de la salle
    private final ArrayList<Objet> stockage = new ArrayList<>();
    //Equipements de la salle
    private final HashSet<Equipement> equipements = new HashSet<>();
    //Historique des actions
    private final Queue<String> historique = new LinkedList<>();

    private boolean incendie = false;
    
    /**
     * Constructeur de Salle
     *
     * @param nom nom de la Salle
     */
    public Salle(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return this.nom;
    }

    public String toString() {
        return this.nom;
    }

    public void action(String[] actions) {
        ArrayList<String> action = new ArrayList<>();
        action.add("Consulter le journal de bord (gratuit)");
        action.add("Consulter le canal de communication pour voir les messages"
                + " échangés entre les\n" + "joueurs (gratuit)");
        action.add("Ecrire un message "
                + "dans le canal de communication (gratuit)");
    }
    
    public boolean estEnFeu() {
        return this.incendie;
    }

    public Queue<String> getHistorique() {
        return this.historique;
    }

    public void addToHistorique(String msg) {

        if (this.historique.size() == 10) {
            this.historique.remove();
        }

        this.historique.add(msg);

    }
    
    public void addObjet(Objet objet) {
        this.stockage.add(objet);
    }

    public boolean hasEquipement(String key) {
        
        for(Equipement equipement : this.equipements) {
            if(equipement.getNom().equals(key)) {
                return true;
            }
        }
        
        return false;
    }
    
    public int getEquipement(String key) {
        
        for(Equipement equipement : this.equipements) {
            if(equipement.getNom().equals(key)) {
                return equipement.getValue();
            }
        }
        
        return 0;
        
    }
    
    public void addEquipement(String key) {
        this.equipements.add(new Equipement(key));
    }
    
}
