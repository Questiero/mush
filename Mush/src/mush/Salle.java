package mush;

import java.util.ArrayList;
import java.util.Random;

public class Salle {

    //Nom de la salle
    private final String nom;
    //Stockage de la salle
    public final ArrayList<Objet> stockage = new ArrayList<>();
    //Historique des actions
    public final ArrayList<String> historique = new ArrayList<>();

    private boolean camera = false;

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
    
    public boolean isCameraInstalle(){
        return this.camera;
    }

    public boolean installerUnCamera() {
        return this.camera = true;
    }
}
