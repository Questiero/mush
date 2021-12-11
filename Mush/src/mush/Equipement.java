package mush;

public class Equipement {

    private boolean estCasse = false;

    private final String nom;

    private int value = 1;

    public Equipement(String nom) {

        this.nom = nom;
    }

    public String toString() {
        return this.nom;
    }

    public String getNom() {
        return this.nom;
    }

    public void toggleCasse() {
        this.estCasse = !this.estCasse;
    }

    public boolean estCasse() {
        return this.estCasse;
    }

    public void setValue(int n) {
        this.value = n;
    }

    public int getValue() {
        return this.value;
    }

    public void removeValue(int n) {
        this.value -= n;
        if (this.value < 0) {
            this.value = 0;
        }
    }

    public void addValue(int n) {
        this.value += n;
    }

}
