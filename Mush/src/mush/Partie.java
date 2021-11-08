package mush;

import java.util.ArrayList;

public class Partie {

    private final Vaisseau vaisseau;

    //Valeur contenant le jour actuel
    private int jour;
    //Valeur contenant le cycle actuel
    private int cycle;

    //Tableau contenant tout les joueurs de la partie
    private final ArrayList<Joueur> personnages = new ArrayList<>();
    //Tableau dynamique contenant tout les joueurs contrôlés par des personnes
    private final ArrayList<Joueur> joueurs = new ArrayList<>();
    //Tableau dynamique contenant tout les joueurs contrôlés par l'ordinateur
    private final ArrayList<Joueur> ordinateurs = new ArrayList<>();

    /**
     * Constructeur de Partie
     */
    public Partie() {

        this.vaisseau = new Vaisseau("Daedalus");

        this.initPersonnages();

    }

    private void initPersonnages() {
        //TODO

        String[][] caracteristiquesPersonnages = {{"Wang Chao", "Tireur", "Bourreau"},
        {"Zhong Chun", "Seul espoir", "Infirmier"},
        {"Eleesha Williams", "Traqueur", "Observateur"},
        {"Finola Keegan", "Biologiste", "Infirmier"},
        {"Frieda Bergmann", "Astrophysicien", "Pilote"},
        {"Gioele Rinaldao", "Robuste", "Paranoïaque"},
        {"Jiang Hua", "Pilote", "Technicien"},
        {"Ian Soulton", "Biologiste", "Mycologiste"},
        {"Janice Kent", "Psy", "Informaticien"},
        {"Kim Jin Su", "Leader", "Pilote"},
        {"Lai Kuan-Ti", "Concepteur", "Optimiste"},
        {"Paola Rinaldo", "Logistique", "Tireur"},
        {"Raluca Tomescu", "Physicien", "Détaché"},
        {"Roland Zuccali", "Sprinter", "Pilote"},
        {"Stephen Seagull", "Cuistot", "Robuste"},
        {"Terrence Archer", "Technicien", "Tireur"}};

        for (String[] caracteristiquesPersonnage : caracteristiquesPersonnages) {
            
            Joueur personnage = new Joueur(caracteristiquesPersonnage[0]);
            
            personnage.addCompetence(caracteristiquesPersonnage[1]);
            personnage.addCompetence(caracteristiquesPersonnage[2]);
            
            this.personnages.add(personnage);
            
        }

    }

    /**
     * Incrémentation du cycle en tenant compte de la limite de 8 cycles par
     * jour
     */
    private void nextCycle() {

        this.cycle++;

        if (this.cycle == 9) {

            this.cycle = 1;

            //TODO Code chaque cycle (ex: +1PA, +1PM pour tout les joueurs)
            nextDay();

        }

    }

    /**
     * Incrémentaiton du jour
     */
    private void nextDay() {

        this.jour++;

        //TODO Code chaque jour (ex: reset Infirmier)
    }

    /**
     * Lance la partie
     */
    public void start() {
        //TODO 

        //Sélection des joueurs
        for (int i = 0; i < 2; i++) {

            int choix = -1;
            boolean correctInput;

            //Affiche le menu de sélection tant que le nombre saisi ne correspond pas à un personnage disponible
            do {

                System.out.println("Joueur " + (i + 1) + ", choississez un personnage parmis:");

                for (int j = 0; j < this.personnages.size(); j++) {

                    System.out.println((j + 1) + ". " + this.personnages.get(j));

                }

                choix = Main.scanner.nextInt(); //TODO .next() ?

                correctInput = ((choix >= 1) && (choix <= this.personnages.size()));

                if (!correctInput) {

                    System.out.println("\n" + Main.msgErreurEntree + "\n");
                    
                }

            } while (!correctInput);

            this.joueurs.add(this.personnages.get(choix - 1));
            this.personnages.remove(choix - 1);

            //Si le joueur choississant actuellement nest le joueur 3 et que les deux joueurs précédant ont choisis Mush, il n'a pas le choix et doit être humain
            if (!((i == 2) && (this.joueurs.get(i).isMush()) && this.joueurs.get(i).isMush())) {

                System.out.println("\nJoueur " + (i + 1) + ", vous préférez:");
                System.out.println("1. Etre un humain");
                System.out.println("2. Etre un mush");
                System.out.println("3. Laissez le hasard décider");

                switch (Main.scanner.nextInt()) { //TODO Only correct inputs with .next()

                    case 2:
                        this.joueurs.get(i).transform();
                        break;
                    case 3: //TODO correct chances to get transformed taking into account current number of mush and remaining number of players
                        if (Math.random() <= 0.5d) {
                            this.joueurs.get(i).transform();
                        }

                }

            }

        }

    }

    /**
     * Arrête la partie
     */
    public void end() {
        //TODO
    }

}
