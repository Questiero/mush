package mush;

import java.util.ArrayList;
import java.util.Scanner;

public class Partie {

    private final Vaisseau vaisseau;

    //Valeur contenant le jour actuel
    private int jour = 1;
    //Valeur contenant le cycle actuel
    private int cycle = 0;

    //Tableau contenant tout les joueurs de la partie
    private final ArrayList<Joueur> personnages = new ArrayList<>();
    //Tableau dynamique contenant tout les joueurs contrôlés par des personnes
    private final ArrayList<Joueur> joueurs = new ArrayList<>();
    //Tableau dynamique contenant tout les joueurs contrôlés par l'ordinateur
    private final ArrayList<Joueur> ordinateurs = new ArrayList<>();

    //Nombre de mush actuellement dans la partie
    private int nbrMush = 0;
    //Nombre de joueurs encore en vie dans la partie
    private int nbrJoueurs = 0;

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

            this.nbrJoueurs++;

        }

    }

    //Méthode pour vérifer si l'arraylist Personnage contient le joueur
    private void getPersonnagesCaracs(Joueur joueur) {
        joueur.affichageEtatJoueur();
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

        //Clone de this.personnages pour faciliter la distribution
        ArrayList<Joueur> tempPersonnages = (ArrayList<Joueur>) this.personnages.clone();

        //Sélection des joueurs
        for (int i = 0; i < 3; i++) {

            int choixPersonnage = -1;
            boolean correctInput;

            //Affiche le menu de sélection tant que le nombre saisi ne correspond pas à un personnage disponible
            do {

                System.out.println("Joueur " + (i + 1) + ", choississez un personnage parmis:");

                for (int j = 0; j < tempPersonnages.size(); j++) {

                    System.out.println((j + 1) + ". " + tempPersonnages.get(j));

                }

                choixPersonnage = Main.scanner.nextInt();

                correctInput = ((choixPersonnage >= 1) && (choixPersonnage <= tempPersonnages.size()));

                if (!correctInput) {

                    System.out.println(Main.msgErreurEntree);

                }

            } while (!correctInput);

            this.joueurs.add(this.personnages.get(choixPersonnage - 1));

            //Si les deux Mush ne sont pas encore séléctionnés, on demande à l'utilisateur de choisir
            if (this.nbrMush < 2) {

                do {

                    System.out.println("\nJoueur " + (i + 1) + ", vous préférez:");
                    System.out.println("1. Etre un humain");
                    System.out.println("2. Etre un mush");
                    System.out.println("3. Laissez le hasard décider");

                    switch (Main.scanner.nextInt()) {

                        case 1:
                            correctInput = true;
                            break;
                        case 2:
                            correctInput = true;
                            this.joueurs.get(i).transform();
                            this.nbrMush++;
                            break;
                        case 3:
                            correctInput = true;
                            if ((Math.random() * tempPersonnages.size()) < (double) (2 - this.nbrMush)) {
                                this.joueurs.get(i).transform();
                                this.nbrMush++;
                            }
                            break;
                        default:
                            correctInput = false;
                            System.out.println(Main.msgErreurEntree);

                    }

                } while (!correctInput);

            }

            tempPersonnages.remove(choixPersonnage - 1);

        }

        //Sélection et transformation des personnages contrôlés par l'ordinateur
        for (int i = 0; i < 13; i++) {

            Joueur ordinateur = tempPersonnages.get(0);

            if ((Math.random() * tempPersonnages.size()) < (double) (2 - this.nbrMush)) {
                ordinateur.transform();
                this.nbrMush++;
            }

            this.ordinateurs.add(ordinateur);
            tempPersonnages.remove(0);

        }

        //TODO position aléatoire des joueurs (pour le moment, par défaut Nexus)
        this.gameProcess();

    }

    //Coeur de la partie
    private void gameProcess() {

        this.nextCycle();

        for (Joueur joueur : joueurs) {
        //Réalisation des actions pour un personnage contrôlé par un joueur
        //le menu 
        System.out.println("1.Affichage des caractéristiques des joueurs.");
        System.out.println("2.Déplacer les joueures.");
        System.out.println("3.Accéder à l'historique des actions.");
        System.out.println("4.Accéder au stockage des objets.");
        System.out.println("Entrez votre choix :");
        int choix = Main.scanner.nextInt();
        switch (choix) {
            case 1 -> {//Affichage des caractéristiques des joueurs.
                System.out.println("Quel joueur souhaitez-vous afficher?");
                System.out.println("Veuillez entrer le numero de joueur : ");
                int index = 1;
                for (Joueur jo : joueurs) {
                    System.out.println((index++) + ": " + jo);
                }
                int choixJoueur = Main.scanner.nextInt();
                switch (choixJoueur) {
                    case 1 ->
                        getPersonnagesCaracs(joueurs.get(0));
                    case 2 ->
                        getPersonnagesCaracs(joueurs.get(1));
                    case 3 ->
                        getPersonnagesCaracs(joueurs.get(2));
                }

            }

            case 2 -> {

            }
            case 3 -> {

            }
            case 4 -> {
                //on affiche le stockage des objets d'une salle à partir de 
                //la position du joueur

                System.out.println("Veuillez entrer le joueur pour afficher sa "
                        + "position");
                System.out.println("Veuillez entrer le numero de joueur : ");
                int index = 1;
                for (Joueur jo : joueurs) {
                    System.out.println((index++) + ": " + jo);
                }
                int choixJoueur = Main.scanner.nextInt();
                switch (choixJoueur) {
                    case 1 ->
                        System.out.println(vaisseau.getSalle(joueurs.get(0).getPosition()).stockage.toString());
                    case 2 ->
                        System.out.println(vaisseau.getSalle(joueurs.get(1).getPosition()).stockage.toString());
                    case 3 ->
                        System.out.println(vaisseau.getSalle(joueurs.get(2).getPosition()).stockage.toString());
                }

            }

        }

         }
        for (Joueur joueur : ordinateurs) {

            //Réalisation des actions pour un personnage contrôlé par l'ordinateur
            //TODO
            System.out.println("1.Affichage des caractéristiques des joueurs.");
            System.out.println("2.Déplacer les joueures.");
            System.out.println("3.Accéder à l'historique des actions.");
            System.out.println("4.Accéder au stockage des objets.");
            System.out.println("Entrez votre choix :");
            int choix = Main.scanner.nextInt();
            switch (choix) {
                case 1 -> {//Affichage des caractéristiques des joueurs.
                    System.out.println("Quel joueur souhaitez-vous afficher?");
                    System.out.println("Veuillez entrer le numero de joueur : ");
                    int index = 1;

                    for (Joueur jo : ordinateurs) {
                        System.out.println((index++) + ": " + jo);
                    }

                    int choixJoueur = Main.scanner.nextInt();
                    for (int i = 0; i < ordinateurs.size(); i++) {
                        if (choixJoueur == i + 1) {
                            getPersonnagesCaracs(ordinateurs.get(i));
                        }
                    }

                }

                case 2 -> {

                }
                case 3 -> {

                }
                case 4 -> {

                    System.out.println("Veuillez entrer le joueur pour afficher sa "
                            + "position");
                    System.out.println("Veuillez entrer le numero de joueur : ");
                    int index = 1;
                    for (Joueur jo : ordinateurs) {
                        System.out.println((index++) + ": " + jo);
                    }
                    int choixJoueur = Main.scanner.nextInt();
                    for (int i = 0; i < ordinateurs.size(); i++) {
                        if (choixJoueur == i + 1) {
                            System.out.println(vaisseau.getSalle(ordinateurs.get(i).getPosition()).stockage.toString());
                        }
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
