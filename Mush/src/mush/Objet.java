package mush;

public class Objet {

    //Nom
    private final String nom;

    //Nombre d'objets en tout
    private final int nbObjets = 19;

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

}
