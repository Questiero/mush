package mush;

public class Vaisseau {
    
    //Nombre de salles dans le vaisseau
    private final int nbrSalles = 27;
    
    //Nom du vaisseau
    private final String nom;
    
    //Caractéristiques maximales du Vaisseau
    private final int maxArmure = 200;
    private final int maxOxygene = 500;
    private final int maxFuel = 20;
    //Caractéristiques du Vaisseau
    private int armure;
    private int oxygene;
    private int fuel;
    
    /**
     * Constructeur de Vaisseau
     * @param nom nom du Vaisseau
     */
    public Vaisseau(String nom) {
        
        //Initialisation du nom à celui donné en paramètre
        this.nom = nom;
        
        //Initialisation des caractéristiques du Vaisseau aux valeurs par défaut
        this.armure = this.maxArmure;
        this.oxygene = this.maxOxygene;
        this.fuel = this.maxFuel;
         
    }
    
}
