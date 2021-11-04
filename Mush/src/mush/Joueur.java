package mush;

public class Joueur {

    String prénom;
    String nom;
    boolean mush;

    private Joueur(String prénom, String nom) {
        this.prénom = prénom;
        this.nom = nom;
        this.mush = false;
    }
}
