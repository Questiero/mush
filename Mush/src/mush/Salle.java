package mush;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Salle {

    //Nom de la salle
    private final String nom;
    //Stockage de la salle
    private final ArrayList<Objet> stockage = new ArrayList<>();
    //Equipements de la salle
    private final HashSet<Equipement> equipements = new HashSet<>();
    //Historique des actions
    private final LinkedBlockingQueue<String> historique = new LinkedBlockingQueue<>(10);
    //Historique des déplacements
    private final LinkedBlockingQueue<String> deplacements = new LinkedBlockingQueue<>(10);

    private boolean incendie = false;

    /**
     * Constructeur de Salle
     *
     * @param nom nom de la Salle
     */
    public Salle(String nom) {

        this.nom = nom;

        if (this.nom.startsWith("Dortoir")) {
            this.addEquipement("Lit", 3);
            this.addEquipement("Douche", 1);
        } else if (this.nom.contains("Baie Alpha")) {
            this.addEquipement("Jet d'attaque", 3);
        } else if (this.nom.equals("Baie Beta")) {
            this.addEquipement("Jet d'attaque", 2);
        } else if (this.nom.equals("Baie Icarus")) {
            this.addEquipement("Jet d'exploration", 1);
        }

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

    public void toggleIncendie() {
        this.incendie = !this.incendie;
    }

    public LinkedBlockingQueue<String> getHistorique() {
        return this.historique;
    }

    public void addToHistorique(String msg) {
        if (this.historique.size() == 10) {
            this.historique.poll();
        }
        this.historique.offer(msg);
    }

    public LinkedBlockingQueue<String> getDeplacements() {
        return this.deplacements;
    }

    public void addToDeplacements(String msg) {
        if (this.deplacements.size() == 10) {
            this.deplacements.poll();
        }
        this.deplacements.offer(msg);

    }

    public void addObjet(Objet objet) {
        this.stockage.add(objet);
    }

    public boolean hasEquipement(String key) {

        for (Equipement equipement : this.equipements) {
            if (equipement.getNom().equals(key)) {
                return true;
            }
        }

        return false;
    }

    public HashSet<Equipement> getEquipements() {
        return this.equipements;
    }

    public Equipement getEquipement(String key) {

        for (Equipement equipement : this.equipements) {
            if (equipement.getNom().equals(key)) {
                return equipement;
            }
        }

        return null;

    }

    public void addEquipement(String key, int n) {

        Equipement equipement = new Equipement(key);
        equipement.setValue(n);
        this.equipements.add(equipement);

    }

}
