package mush;

public class Objet {

    //Nom
    private final String nom;

    /**
     * Constructeur d'Objet
     *
     * @param nom nom de l'Objet
     */
    public Objet(String nom) {

        this.nom = nom;
    }
    
    public String toString(){
        return this.nom;
    }

    public String getNom() {
        return this.nom;
    }
    
}
