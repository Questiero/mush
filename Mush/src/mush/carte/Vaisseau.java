package mush.carte;

public class Vaisseau {
    
    private final String nom;
    
    private final int nbrSalles = 27;
    
    private final Salle[] salles = new Salle[nbrSalles];
    //Représentation des déplacements possibles entre les salles au sein du vaisseau sous forme de graph (représenté par sa matrice d'adjacence)
    private final boolean[][] graphSalles = {{, , , , , , , , , , , , , , , , , , , , , , , , , , },
                                             {, , , , , , , , , , , , , , , , , , , , , , , , , , },
                                             {, , , , , , , , , , , , , , , , , , , , , , , , , , },
                                             {, , , , , , , , , , , , , , , , , , , , , , , , , , },
                                             {, , , , , , , , , , , , , , , , , , , , , , , , , , },
                                             {, , , , , , , , , , , , , , , , , , , , , , , , , , },
                                             {, , , , , , , , , , , , , , , , , , , , , , , , , , },
                                             {, , , , , , , , , , , , , , , , , , , , , , , , , , },
                                             {, , , , , , , , , , , , , , , , , , , , , , , , , , },
                                             {, , , , , , , , , , , , , , , , , , , , , , , , , , },
                                             {, , , , , , , , , , , , , , , , , , , , , , , , , , },
                                             {, , , , , , , , , , , , , , , , , , , , , , , , , , },
                                             {, , , , , , , , , , , , , , , , , , , , , , , , , , },
                                             {, , , , , , , , , , , , , , , , , , , , , , , , , , },
                                             {, , , , , , , , , , , , , , , , , , , , , , , , , , },
                                             {, , , , , , , , , , , , , , , , , , , , , , , , , , },
                                             {, , , , , , , , , , , , , , , , , , , , , , , , , , },
                                             {, , , , , , , , , , , , , , , , , , , , , , , , , , },
                                             {, , , , , , , , , , , , , , , , , , , , , , , , , , },
                                             {, , , , , , , , , , , , , , , , , , , , , , , , , , },
                                             {, , , , , , , , , , , , , , , , , , , , , , , , , , },
                                             {, , , , , , , , , , , , , , , , , , , , , , , , , , },
                                             {, , , , , , , , , , , , , , , , , , , , , , , , , , },
                                             {, , , , , , , , , , , , , , , , , , , , , , , , , , },
                                             {, , , , , , , , , , , , , , , , , , , , , , , , , , },
                                             {, , , , , , , , , , , , , , , , , , , , , , , , , , },
                                             {, , , , , , , , , , , , , , , , , , , , , , , , , , }};
    
    public Vaisseau(String nom) {
        
        this.nom = nom;
        
        initSalles();
        
    }
    
    private void initSalles() {        
        
        String[] nomSalles = {"Pont",
                              "Tourelle Alpha avant",
                              "Tourelle Beta avant", 
                              "Couloir avant",
                              "Jardin Hydrophonique",
                              "Laboratoire",
                              "Stockage avant",
                              "Infirmerie",
                              "Tourelle Alpha centre",
                              "Tourelle Beta centre",
                              "Couloir central",
                              "Baie Alpha",
                              "Baie Beta",
                              "Stockage Alpha centre",
                              "Stockage Beta centre",
                              "Réfectoire",
                              "Dortoir Alpha",
                              "Dortoir Beta",
                              "Nexus",
                              "Couloir arrière",
                              "Baie Alpha 2",
                              "Baie Icarus",
                              "Stockage Alpha arrière",
                              "Stockage Beta arrière",
                              "Tourelle Alpha arrière",
                              "Tourelle Beta arrière",
                              "Salle des moteurs"};
        
        for (int i = 0; i < nbrSalles; i++) {
            
            this.salles[i] = new Salle(nomSalles[i]);
            
        }
                
    }
    
}
