package mush;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Partie {

    private final Vaisseau vaisseau;

    //Valeur contenant le jour actuel
    private int jour = 1;
    //Valeur contenant le cycle actuel
    private int cycle = 1;

    //Tableau contenant tout les joueurs de la partie
    private final ArrayList<Joueur> personnages = new ArrayList<>();
    //Tableau dynamique contenant tout les joueurs contrôlés par des personnes
    private final ArrayList<Joueur> joueurs = new ArrayList<>();
    //Tableau dynamique contenant tout les joueurs contrôlés par l'ordinateur
    private final ArrayList<Joueur> ordinateurs = new ArrayList<>();

    //Nombre de mush actuellement dans la partie
    private int nbrMush = 0;
    private int sporesExtraits = 0;
    private int maxSpore = 4;
    //Nombre de joueurs encore en vie dans la partie
    private int nbrJoueurs = 0;

    private boolean estMort = false;

    private final Planete planete = new Planete();

    //Conversations
    private final LinkedBlockingQueue mainChat = new LinkedBlockingQueue<>(10);
    private final LinkedBlockingQueue mushChat = new LinkedBlockingQueue<>(10);

    //Recherchers, projets et pilgred
    private final HashMap<String, Integer> recherches = new HashMap<>();
    private final HashMap<String, Integer> projets = new HashMap<>();
    private int pilgred = 0;

    //Vaisseaux Aliens
    private int aliensActifs = 0, aliensInter = 0, aliensInactifs = 0;

    /**
     * Constructeur de Partie
     */
    public Partie() {

        this.vaisseau = new Vaisseau("Daedalus");

        this.initPersonnages();
        this.initRecherchesProjets();

    }

    private void initPersonnages() {

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

    private void initRecherchesProjets() {

        this.recherches.put("Mycoscan", 0);
        this.recherches.put("Gaz antispore", 0);
        this.recherches.put("Sérum de constipaspore", 0);
        this.recherches.put("Savon mushicide", 0);
        this.recherches.put("Sérum rétro-fongique", 0);
        this.recherches.put("Extracteur de spores", 0);

        this.projets.put("Accélération du processeur", 0);
        this.projets.put("Arrosseurs automatiques", 0);
        this.projets.put("Conduites oxygénées", 0);
        this.projets.put("Bouclier plasma", 0);
        this.projets.put("Réducteur de trainée", 0);

    }

    public void addToMainChat(String msg) {
        if (this.mainChat.size() == 10) {
            this.mainChat.poll();
        }
        this.mainChat.offer(msg);
    }

    public void addToMushChat(String msg) {
        if (this.mushChat.size() == 10) {
            this.mushChat.poll();
        }
        this.mushChat.offer(msg);
    }

    /**
     * Incrémentation du cycle en tenant compte de la limite de 8 cycles par
     * jour
     */
    private void nextCycle() {

        this.cycle++;

        //Evénèments aléatoires
        Random rand = new Random();

        this.vaisseau.removeArmure(5 * this.vaisseau.getSallesIncendie().size());

        for (Salle salle : this.vaisseau.getSalles()) {

            //Incendies
            if (rand.nextInt(100) < 5 && !salle.estEnFeu()) {
                salle.toggleIncendie();
                salle.addToHistorique("Un incendie vient de se déclarer !");
            }

            //Plafond qui tombe
            if (rand.nextInt(100) < 2) {

                for (Joueur joueur : this.personnages) {

                    if (joueur.estDans(salle.getNom())) {
                        joueur.removePv(6);
                        //Vérification que le joueur est encore en vie
                    }

                }

                salle.addToHistorique("Le plafond vient de tomber !");

            }

            //Equipements endommagés
            for (Equipement equipement : salle.getEquipements()) {
                if (rand.nextInt(100) < 3) {
                    equipement.toggleCasse();
                    salle.addToHistorique("L'équipement " + equipement.getNom() + " vient de se casser !");
                }
            }

        }

        //Aliens, l'apparition ne commence qu'à partir du 5ème cycle du premier jour
        if (this.cycle >= 5 || this.jour >= 2) {

            //Augmentation de l'activité des aliens
            this.aliensActifs += this.aliensInter;
            this.aliensInter += this.aliensInactifs;
            this.aliensInactifs = rand.nextInt(6);

            this.vaisseau.removeArmure(this.aliensActifs);

        }

        //Casse des portes
        int[][] graph = this.vaisseau.getPortes();

        for (int i = 0; i < graph.length; i++) {

            for (int j = i; j < graph.length; j++) {

                if (graph[i][j] == 1 && rand.nextInt(100) < 3) {
                    graph[i][j] = -1;
                    graph[j][i] = -1;
                }

            }

        }

        if (!(this.vaisseau.getSalle("Nexus").hasEquipement("Conduites oxygénées") && Math.random() * 100 < 20)) {
            this.vaisseau.removeOxygene(this.nbrJoueurs);
        }

        //Gestion des compétences et changement de caractéristiques des joueurs à chaque fin de cycle
        for (Joueur joueur : this.personnages) {

            //Incrémentation des attributs du joueur
            joueur.addPa(1);
            if (joueur.estCouche()) {
                joueur.addPa(1);
            }
            joueur.addPm(1);
            joueur.removePmo(1);

            //Activation de la compétence Logistique
            if (joueur.hasCompetence("Logistique")) {

                //Construction de la liste des joueurs dans la même salle que joueur
                LinkedList<Joueur> joueursSalle = new LinkedList<>();

                for (Joueur tempJoueur : this.personnages) {
                    if (tempJoueur.getPositionKey().equals(joueur.getPositionKey())) {
                        joueursSalle.add(tempJoueur);
                    }
                }

                //Incrémentation des PA d'un joueur aléatoire dans la même salle que le Logistique
                joueursSalle.get(rand.nextInt(joueursSalle.size())).addPa(1);

            }

            //Décrémentation de la valeur de sasiété du joueur
            joueur.waitSasiete();

            //Perte de Pmo si un joueur esrt mort au dernier cycle
            if (this.estMort && !joueur.hasCompetence("Détaché")) {
                joueur.removePmo(1);
                this.estMort = false;
            }

            //Décrémentation des PVs si le moral du joueur est nul
            if ((joueur.getPmo() == 0)) {
                joueur.removePv(1);
                //Vérification de la mort
            }

            //Arrosseurs automatiques
            if (this.vaisseau.getSalle("Nexus").hasEquipement("Arrosseurs automatiques")) {

                ArrayList<Salle> sallesIncendie = this.vaisseau.getSallesIncendie();

                sallesIncendie.get(rand.nextInt(sallesIncendie.size())).toggleIncendie();

            }

            if (this.vaisseau.getOxygene() == 0) {
                joueur.removePv(5);
            }

        }

        if (this.cycle == 9) {

            this.cycle = 1;

            nextDay();

        }

    }

    /**
     * Incrémentaiton du jour
     */
    private void nextDay() {

        this.jour++;

        //Reset du nombre de spores que les mushs peuvent produire chaque jour
        this.sporesExtraits = 0;

        //Gestion des compétences et changement de caractéristiques des joueurs à chaque fin de jour
        for (Joueur joueur : personnages) {

            //Reset du poinconnage des mushs
            if (joueur.isMush()) {
                joueur.setPeutPoinconner(true);
            }

            //Reset des compétences
            if (joueur.hasCompetence("Cuistot")) {
                joueur.setCompetence("Cuistot", 4);
            }
            if (joueur.hasCompetence("Tireur")) {
                joueur.setCompetence("Tireur", 2);
            }
            if (joueur.hasCompetence("Infirmier")) {
                joueur.setCompetence("Infirmier", 1);
            }
            if (joueur.hasCompetence("Technicien")) {
                joueur.setCompetence("Technicien", 1);
            }
            if (joueur.hasCompetence("Physicien")) {
                joueur.setCompetence("Physicien", 1);
            }

            if (joueur.hasCompetence("Concepteur")) {
                joueur.addPa(2);
            }
            if (joueur.hasCompetence("Concepteur")) {
                joueur.addPmo(1);
            }

            joueur.setPeutCaresser(true);

        }

    }

    /**
     * Lance la partie
     */
    public void start() {

        //Clone de this.personnages pour faciliter la distribution
        ArrayList<Joueur> tempPersonnages = (ArrayList<Joueur>) this.personnages.clone();

        //Sélection des joueurs
        for (int i = 0; i < 3; i++) {

            int choixPersonnage = -1;
            boolean correctInput;

            //Affiche le menu de sélection tant que le nombre saisi ne correspond pas à un personnage disponible
            do {

                System.out.println("\nJoueur " + (i + 1) + ", choississez un personnage parmis:");

                for (int j = 0; j < tempPersonnages.size(); j++) {

                    System.out.println((j + 1) + ". " + tempPersonnages.get(j));

                }

                choixPersonnage = Main.scanner.nextInt();

                correctInput = ((choixPersonnage >= 1) && (choixPersonnage <= tempPersonnages.size()));

                if (!correctInput) {

                    System.out.println(Main.msgErreurEntree);

                }

            } while (!correctInput);

            this.joueurs.add(tempPersonnages.get(choixPersonnage - 1));

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

        //Répartition aléatoire de la salle de départ des joueurs
        Random rand = new Random();

        for (Joueur joueur : joueurs) {
            joueur.setPositionKey(vaisseau.getSalles()[rand.nextInt(vaisseau.getSalles().length)].getNom());
        }

        this.gameProcess();

    }

    //Coeur de la partie
    private void gameProcess() {

        //Variable pour déterminer si la partie doit s'arrêter
        boolean continuer = true;

        do {

            System.out.println("\nJour " + this.jour + ", cycle " + this.cycle);

            //Actions des joueurs
            for (Joueur joueur : joueurs) {

                boolean stop = false;

                while (!stop && joueur.getPv() > 0) {

                    boolean retour = false;
                    Salle salleJoueur = this.vaisseau.getSalle(joueur.getPositionKey());

                    System.out.println("\n" + joueur + ", sélectionnez une action à effectuer parmis:");

                    if (!joueur.estCouche()) {
                        System.out.println("etat. Afficher votre état (gratuit)");
                        System.out.println("journal. Afficher le journal de bord (gratuit)");
                        System.out.println("canal. Afficher les actions relatives au canal de communication (gratuit)");
                        System.out.println("salle. Afficher les actions disponibles dans " + joueur.getPositionKey() + " (gratuit)");
                        if (joueur.isMush()) {
                            System.out.println("mush. Actions de Mush (gratuit)");
                        }
                        if (joueur.hasObjet("Extracteur de Spore") && joueur.getPa() >= 1) {
                            System.out.println("extraire. Extraire un spore (1 PA)");
                        }
                        if (joueur.hasObjet("Chat de Schrödinger") && joueur.getPa() >= 1) {
                            System.out.println("chat. Caresser le chat de Schrödinger (1 PA)");
                        }
                        int pmConversion = 2;
                        if (joueur.hasCompetence("Sprinter")) {
                            pmConversion += 2;
                        }
                        if (joueur.hasObjet("Trotinette")) {
                            pmConversion += 2;
                        }
                        if (joueur.getPm() == 0 && joueur.getPa() >= 1) {
                            System.out.println("conversion. Convertir 1 PA en " + pmConversion + " PM (1 PA)");
                        }
                        if (joueur.getPm() >= 1) {
                            System.out.println("deplacer. Changer de salle (1 PM)");
                        }
                    } else {
                        System.out.println("lever. Se lever du lit (gratuit)");
                    }

                    System.out.println("fin. Finir votre tour (gratuit)");

                    switch (Main.scanner.next().toLowerCase()) {

                        case "lever":
                            if (joueur.estCouche()) {

                                joueur.toogleEstCouche();
                                salleJoueur.addToHistorique(joueur + " se lève");
                                salleJoueur.getEquipement("Lit").addValue(1);

                            } else {
                                System.out.println(Main.msgErreurEntree);
                            }
                            break;
                        case "etat":
                            if (!joueur.estCouche()) {
                                joueur.affichageEtatJoueur();
                            } else {
                                System.out.println(Main.msgErreurEntree);
                            }
                            break;
                        case "journal":

                            if (!joueur.estCouche()) {
                                System.out.println("\n" + joueur + ", sélectionnez une action à effectuer parmis:");
                                System.out.println("cameras. Afficher la liste des joueurs présents dans les salles où il y a une caméra (gratuit)");
                                System.out.println("incendis. Afficher les incendies en cours (gratuit)");
                                System.out.println("casse. Afficher la liste des équipements endommagés (gratuit)");
                                System.out.println("etat. Afficher l'état du vaisseau (gratuit)");
                                System.out.println("objets. Afficher la position des objets dans les unités de stockage (gratuit)");
                                System.out.println("aliens. Afficher le nombre de vaisseaux alien à proximité (gratuit)");
                                System.out.println("planete. Afficher les informations disponibles sur la planète à proximité (gratuit)");
                                System.out.println("recherches. Afficher les recherches de laboratoire terminées (gratuit)");
                                System.out.println("neron. Afficher les projets NERON terminés (gratuit)");
                                System.out.println("mushs. Afficher le nombre de mushs à bord (gratuit)");
                                System.out.println("morts. Afficher le nombre de joueurs morts (gratuit)");
                                System.out.println("retour. Retourner au menu principal (gratuit)");

                                switch (Main.scanner.next().toLowerCase()) {

                                    case "cameras":

                                        Salle[] salles = this.vaisseau.getSalles().clone();
                                        LinkedList<Salle> sallesCameras = new LinkedList<>();

                                        int i = 1;

                                        System.out.println("\nChoississez une salle dans laquelle vous déplacer parmis:");
                                        for (Salle salle : salles) {
                                            if (salle.hasEquipement("Caméra")) {
                                                System.out.println(i + ". " + salle.getNom());
                                                sallesCameras.add(salle);
                                                i++;
                                            }
                                        }

                                        Salle salleTemp = sallesCameras.get(Main.scanner.nextInt() - 1);

                                        if (salleTemp.hasEquipement("Caméra")) {

                                            System.out.println("\nListe des joueurs dans " + salleTemp + ":");
                                            for (Joueur joueurTemp : personnages) {
                                                if (joueurTemp.getPositionKey().equals(salleTemp.getNom())) {
                                                    System.out.println(joueurTemp);
                                                }
                                            }

                                        } else {
                                            System.out.println(Main.msgErreurEntree);
                                        }

                                        break;
                                    case "incendie":

                                        System.out.println("\nListe des salles avec un incendie en cours:");

                                        for (Salle salle : this.vaisseau.getSallesIncendie()) {
                                            System.out.println(salle.getNom());
                                        }

                                        break;
                                    case "casse":

                                        System.out.println("\nListe des équipements endommagés par salle:");
                                        //Affichage des équipements endommagés
                                        for (Salle salle : this.vaisseau.getSalles()) {

                                            if (!salle.getEquipements().isEmpty()) {

                                                StringBuilder sb = new StringBuilder();

                                                sb.append(salle.getNom());
                                                sb.append(":  ");

                                                for (Equipement equipement : salle.getEquipements()) {
                                                    if (equipement.estCasse()) {
                                                        sb.append(equipement.getNom());
                                                        sb.append(", ");
                                                    }
                                                }

                                                sb.delete(sb.length() - 2, sb.length() - 1);

                                                System.out.println(sb);

                                            }

                                        }

                                        System.out.println("\nListe des portes endommagées:");
                                        //Affichage des portes endommagées
                                        int[][] graph = this.vaisseau.getPortes();

                                        for (int j = 0; j < graph.length; j++) {

                                            for (int k = j; k < graph.length; k++) {

                                                if (graph[j][k] == -1) {
                                                    System.out.println("Porte entre " + this.vaisseau.getSalles()[j] + " et " + this.vaisseau.getSalles()[k]);
                                                }

                                            }

                                        }

                                        break;
                                    case "etat":
                                        this.vaisseau.afficherEtatVaisseau();
                                        break;
                                    case "objets":

                                        System.out.println("\nListe des objets par salle:");

                                        for (Salle salle : this.vaisseau.getSalles()) {

                                            if (!salle.getStockage().isEmpty()) {

                                                StringBuilder sb = new StringBuilder();

                                                sb.append(salle.getNom());
                                                sb.append(":  ");

                                                for (Objet objet : salle.getStockage()) {
                                                    sb.append(objet.getNom());
                                                    sb.append(", ");
                                                }
                                                sb.delete(sb.length() - 2, sb.length() - 1);

                                                System.out.println(sb);

                                            }

                                        }

                                        break;
                                    case "aliens":
                                        System.out.println("\nIl y a actuellement " + (this.aliensActifs + this.aliensInter + this.aliensInactifs) + " vaisseaux aliens à proximité du vaisseau");
                                        break;
                                    case "planete":
                                        //TODO
                                        break;
                                    case "recherches":

                                        System.out.println("\nListe des recherches terminées:");
                                        for (Map.Entry valeur : this.recherches.entrySet()) {
                                            if (valeur.equals(100)) {
                                                System.out.println(valeur.getKey());
                                            }
                                        }

                                        break;
                                    case "neron":

                                        System.out.println("\nListe des projets NERON terminées:");
                                        for (Map.Entry valeur : this.projets.entrySet()) {
                                            if (valeur.equals(100)) {
                                                System.out.println(valeur.getKey());
                                            }
                                        }

                                        break;
                                    case "mushs":
                                        System.out.println("\n" + this.nbrMush + " mush sont actuellement à bord du vaisseau");
                                        break;
                                    case "morts":
                                        System.out.println("\n" + (16 - this.nbrJoueurs) + " joueurs sont morts");
                                        break;
                                    case "retour":
                                        retour = true;
                                        break;
                                    default:
                                        System.out.println(Main.msgErreurEntree);

                                }
                            } else {
                                System.out.println(Main.msgErreurEntree);
                            }

                            break;
                        case "canal":

                            if (!joueur.estCouche()) {
                                System.out.println("\n" + joueur + ", sélectionnez une action à effectuer parmis:");
                                System.out.println("consulter. Consulter le canal de communication (gratuit)");
                                System.out.println("envoyer. Envoyer un message dans le canal de communication (gratuit)");
                                System.out.println("retour. Retourner au menu principal (gratuit)");

                                switch (Main.scanner.next().toLowerCase()) {

                                    case "consulter":

                                        LinkedBlockingQueue<String> mainChatTemp = new LinkedBlockingQueue<>(this.mainChat);

                                        if (mainChatTemp.isEmpty()) {
                                            System.out.println("\nAucun message n'a encore été envoyé dans le canal de communication");
                                        } else {

                                            System.out.println("\nHistorique des messages envoyés dans le canal de communication:");

                                            int i = mainChatTemp.size();
                                            while (!mainChatTemp.isEmpty()) {
                                                System.out.println(i + ") " + mainChatTemp.poll());
                                                i--;
                                            }

                                        }

                                        break;
                                    case "envoyer":

                                        System.out.println("\nEntrez votre message à envoyer dans le canal de communication:");
                                        this.addToMainChat(Main.scanner.next().toLowerCase());

                                        break;
                                    case "retour":
                                        retour = true;
                                        break;
                                    default:
                                        System.out.println(Main.msgErreurEntree);

                                }

                            } else {
                                System.out.println(Main.msgErreurEntree);
                            }

                            break;
                        case "salle":

                            if (!joueur.estCouche()) {

                                System.out.println("\n" + joueur + ", sélectionnez une action à effectuer parmis:");
                                System.out.println("historique. Afficher l'historique des 10 dernières actions de la salle (gratuit)");
                                if (joueur.hasCompetence("Traqueur")) {
                                    System.out.println("deplacements. Afficher l'historique de déplacements (gratuit)");
                                }
                                System.out.println("joueurs. Afficher la liste des joueurs présents dans " + joueur.getPositionKey() + " (gratuit)");
                                System.out.println("stockage. Afficher les actions relatives à l'unité de stockage de " + joueur.getPositionKey() + " (gratuit)");
                                if (joueur.competenceEquals("Technicien", 1) || joueur.getPa() >= 1) {
                                    System.out.println("reparer. Reparer un équipement dans la salle " + (joueur.competenceEquals("Technicien", 1) ? "(gratuit)" : "(1PA)"));
                                }
                                if (salleJoueur.estEnFeu() && joueur.hasObjet("Extincteur")) {
                                    System.out.println("eteindre. Eteindre un incendie dans la salle (gratuit)");
                                }
                                if (joueur.estDans("Pont") && ((joueur.getPa() >= 2)) || (joueur.getPa() >= 1) && this.vaisseau.getSalle("Nexus").hasEquipement("Accélération de processeur")) {
                                    System.out.println("detecter. Detecter une planète à proximité" + ((this.vaisseau.getSalle("Nexus").hasEquipement("Accélération du processeur")) ? "(1 PA)" : "(2 PA)"));
                                }
                                if (joueur.estDans("Pont") && ((joueur.getPa() >= 3)) || (joueur.getPa() >= 2) && joueur.hasCompetence("Astrophysicien")) {
                                    System.out.println("scanner. Scanner la planète" + (joueur.hasCompetence("Astrophysicien") ? "(2 PA)" : "(3 PA)"));
                                }
                                if ((this.aliensActifs + this.aliensInter + this.aliensInactifs) > 0 && ((joueur.getPositionKey().startsWith("Tourelle") || salleJoueur.hasEquipement("Jet d'attaque")))) {
                                    System.out.println("attaquer. Attaquer un vaisseau alien (1 PA)");
                                }
                                if (this.planete.estDecouverte() && joueur.hasCompetence("Pilote") && joueur.estDans("Baie Icarus")) {
                                    System.out.println("expedition. Lancer une expédition (3 PA)");
                                }
                                if (joueur.estDans("Pont") && joueur.hasCompetence("Pilote")) {
                                    System.out.println("deplacer. Déplacer le Daedalus (3 PA)");
                                }
                                if (joueur.estDans("Pont") && joueur.hasCompetence("Pilote") && this.vaisseau.getSalle("Salle des moteurs").getEquipement("Réacteur PILGRED").getValue() == 100) {
                                    System.out.println("rentrer. Rentrer sur Terre (5 PA)");
                                }
                                if (salleJoueur.hasEquipement("Lit") && salleJoueur.getEquipement("Lit").getValue() > 0) {
                                    System.out.println("coucher. Se coucher dans un lit (gratuit)");
                                }
                                if (joueur.estDans("Salle des moteurs") && this.vaisseau.getSalle("Salle des moteurs").hasEquipement("Réacteur PILGRED") && this.vaisseau.getSalle("Salle des moteurs").getEquipement("Réacteur PILGRED").getValue() < 100 && (joueur.competenceEquals("Physicien", 1) || joueur.getPa() >= 3)) {
                                    System.out.println("pilgred. Réparer le réacteur PILGRED de 10%" + (joueur.competenceEquals("Physicien", 1) ? "(gratuit)" : "(3 PA)"));
                                }
                                if (salleJoueur.hasEquipement("Douche")) {
                                    System.out.println("douche. Se doucher" + ((joueur.hasObjet("Savon")) ? "(1 PA)" : "(2 PA"));
                                }
                                if (salleJoueur.hasEquipement("Caméra")) {
                                    System.out.println("camera. Désinstaller une caméra (4 PA)");
                                } else if (joueur.hasObjet("Caméra")) {
                                    System.out.println("camera. Installer une caméra (4 PA)");
                                }
                                if (joueur.hasCompetence("Cuistot") && !joueur.competenceEquals("Cuistot", 0) && joueur.hasObjet("Ration standard")) {
                                    System.out.println("cuisiner. Cuisiner une ration (gratuit)");
                                }
                                if ((joueur.hasObjet("Ration standard") || joueur.hasObjet("Ration cuisinée")) && !(joueur.getSasiete() == 3)) {
                                    System.out.println("manger. Manger une ration (gratuit)");
                                }
                                if (joueur.estDans("Laboratoire") && this.vaisseau.getSalle("Laboratoire").hasEquipement("Mycoscan")) {
                                    System.out.println("mycoscan. Connaitre le nombre de spores sur vous-même (gratuit)");
                                }
                                if (joueur.estDans("Laboratoire")) {
                                    System.out.println("recherches. Afficher les recherches disponibles (gratuit)");
                                }
                                if (joueur.estDans("Nexus")) {
                                    System.out.println("projets. Afficher les projets NERON disponibles (gratuit)");
                                }
                                System.out.println("retour. Retourner au menu principal (gratuit)");

                                switch (Main.scanner.next().toLowerCase()) {

                                    case "historique":

                                        LinkedBlockingQueue<String> historiqueTemp = new LinkedBlockingQueue<>(salleJoueur.getHistorique());

                                        if (historiqueTemp.isEmpty()) {
                                            System.out.println("\nAucune action n'a encore été effectuée dans " + joueur.getPositionKey());
                                        } else {

                                            System.out.println("\nHistorique des actions effectuées dans " + joueur.getPositionKey() + ":");

                                            int i = historiqueTemp.size();
                                            while (!historiqueTemp.isEmpty()) {
                                                System.out.println(i + ") " + historiqueTemp.poll());
                                                i--;
                                            }

                                        }

                                        break;
                                    case "deplacements":
                                        if (joueur.hasCompetence("Traqueur")) {

                                            LinkedBlockingQueue<String> deplacementsTemp = new LinkedBlockingQueue<>(salleJoueur.getDeplacements());

                                            if (deplacementsTemp.isEmpty()) {
                                                System.out.println("\nAucune action n'a encore été effectuée dans " + joueur.getPositionKey());
                                            } else {

                                                System.out.println("\nHistorique des actions effectuées dans " + joueur.getPositionKey() + ":");

                                                int i = deplacementsTemp.size();
                                                while (!deplacementsTemp.isEmpty()) {
                                                    System.out.println(i + ") " + deplacementsTemp.poll());
                                                    i--;
                                                }

                                            }

                                        } else {
                                            System.out.println(Main.msgErreurEntree);
                                        }
                                        break;
                                    case "joueurs":

                                        System.out.println("\n" + joueur + ", sélectionnez une action à effectuer parmis:");
                                        System.out.println("liste. Afficher la liste des joueurs présents dans" + salleJoueur + "(gratuit)");
                                        if (joueur.hasCompetence("Leader") && joueur.getPa() >= 2) {
                                            System.out.println("discours. Prononcer un discours (2 PA)");
                                        }
                                        if (joueur.competenceEquals("Infirmier", 1) || (joueur.hasObjet("Medkit") && joueur.getPa() >= 2)) {
                                            System.out.println("soigner. Soigner un joueur" + (joueur.competenceEquals("Infirmier", 1) ? "(gratuit)" : "(2 PA)"));
                                        }
                                        if (joueur.hasCompetence("Psy") && joueur.getPa() >= 1) {
                                            System.out.println("reconforter. Reconforter un joueur (1 PA)");
                                        }
                                        if (joueur.getPa() >= 2 || (joueur.getPa() >= 1 && (joueur.hasObjet("Blaster") || joueur.hasObjet("Couteau"))) || joueur.hasObjet("Grenade")) {
                                            System.out.println("attaquer. Voir les options pour attaquer un joueur (gratuit)");

                                        }
                                        if (joueur.hasObjet("Sérum retro-fongique") && joueur.getPa() >= 2) {
                                            System.out.println("serum. Utiliser le sérum rétro-fongique sur un joueur (2 PA)");
                                        }
                                        if (joueur.hasCompetence("Bourreau") && joueur.getPa() >= 2) {
                                            System.out.println("bourreau. Fait perdre 1 PV à un joueur et révèle autant de ses dernières actions qu'il lui manque de PV (1 PA)");
                                        }
                                        System.out.println("retour. Retourner au menu principal (gratuit)");

                                        switch (Main.scanner.next()) {

                                            case "liste":

                                                System.out.println("\nListe des joueurs dans " + salleJoueur);
                                                for (Joueur joueurTemp : this.personnages) {
                                                    if (joueurTemp.getPositionKey().equals(salleJoueur.getNom())) {
                                                        System.out.println(joueurTemp);
                                                    }
                                                }

                                                salleJoueur.addToHistorique(joueur + "observe les personnes autour de lui");

                                                break;
                                            case "discours":
                                                if (joueur.hasCompetence("Leader") && joueur.getPa() >= 2) {

                                                    for (Joueur joueurTemp : this.personnages) {
                                                        if (joueurTemp.getPositionKey().equals(salleJoueur.getNom())) {
                                                            joueur.addPmo(2);
                                                        }
                                                    }

                                                    joueur.removePa(2);

                                                    System.out.println("Vous vennez de faire un discours");
                                                    salleJoueur.addToHistorique(joueur + "vient de faire un discours");

                                                } else {
                                                    System.out.println(Main.msgErreurEntree);
                                                }
                                                break;
                                            case "soigner":
                                                if (joueur.competenceEquals("Infirmier", 1) || (joueur.hasObjet("Medkit") && joueur.getPa() >= 2)) {

                                                    ArrayList<Joueur> joueursSalle = new ArrayList<>();

                                                    //Construction de l'ArrayList des joueurs dans la même salle que joueur
                                                    for (Joueur joueurTemp : this.personnages) {
                                                        if (joueurTemp.getPositionKey().equals(salleJoueur.getNom())) {
                                                            joueursSalle.add(joueurTemp);
                                                        }
                                                    }

                                                    int choixJoueur = -1;
                                                    boolean correctInput;

                                                    do {

                                                        int i;

                                                        System.out.println("\nChoississez un joueur à soigner:");
                                                        for (i = 0; i < joueursSalle.size(); i++) {
                                                            System.out.println((i + 1) + ". " + joueursSalle.get(i) + (joueur.competenceEquals("Infirimier", 1) ? " (gratuit)" : " (1 PA)"));
                                                        }
                                                        System.out.println((i + 1) + ". Personne (gratuit)");

                                                        choixJoueur = Main.scanner.nextInt();

                                                        correctInput = ((choixJoueur >= 1) && (choixJoueur <= joueursSalle.size() + 1));

                                                    } while (!correctInput);

                                                    if (choixJoueur <= joueursSalle.size()) {

                                                        System.out.println("\nVous venez de soigner " + joueursSalle.get(choixJoueur - 1));
                                                        salleJoueur.addToHistorique(joueur + " vient de soigner " + joueursSalle.get(choixJoueur - 1));

                                                        joueursSalle.get(choixJoueur - 1).addPv(4);

                                                        if (joueur.competenceEquals("Infirimier", 1)) {
                                                            joueur.setCompetence("Infirmier", 0);
                                                        } else {
                                                            joueur.removePa(1);
                                                        }

                                                    } else if (choixJoueur != joueursSalle.size() + 1) {
                                                        System.out.println(Main.msgErreurEntree);
                                                    }

                                                } else {
                                                    System.out.println(Main.msgErreurEntree);
                                                }
                                                break;
                                            case "reconforter":
                                                if (joueur.hasCompetence("Psy") && joueur.getPa() >= 1) {

                                                    ArrayList<Joueur> joueursSalle = new ArrayList<>();

                                                    //Construction de l'ArrayList des joueurs dans la même salle que joueur
                                                    for (Joueur joueurTemp : this.personnages) {
                                                        if (joueurTemp.getPositionKey().equals(salleJoueur.getNom())) {
                                                            joueursSalle.add(joueurTemp);
                                                        }
                                                    }

                                                    int choixJoueur = -1;
                                                    boolean correctInput;

                                                    do {

                                                        int i;

                                                        System.out.println("\nChoississez un joueur à réconforter:");
                                                        for (i = 0; i < joueursSalle.size(); i++) {
                                                            System.out.println((i + 1) + ". " + joueursSalle.get(i) + " (1 PA)");
                                                        }
                                                        System.out.println((i + 1) + ". Personne (gratuit)");

                                                        choixJoueur = Main.scanner.nextInt();

                                                        correctInput = ((choixJoueur >= 1) && (choixJoueur <= joueursSalle.size() + 1));

                                                    } while (!correctInput);

                                                    if (choixJoueur <= joueursSalle.size()) {

                                                        System.out.println("\nVous venez de réconforter " + joueursSalle.get(choixJoueur - 1));
                                                        salleJoueur.addToHistorique(joueur + " vient de réconforter " + joueursSalle.get(choixJoueur - 1));

                                                        joueursSalle.get(choixJoueur - 1).addPmo(2);
                                                        joueur.removePa(1);

                                                    } else if (choixJoueur != joueursSalle.size() + 1) {
                                                        System.out.println(Main.msgErreurEntree);
                                                    }

                                                } else {
                                                    System.out.println(Main.msgErreurEntree);
                                                }
                                                break;
                                            case "attaquer":
                                                if (joueur.getPa() >= 2 || (joueur.getPa() >= 1 && (joueur.hasObjet("Blaster") || joueur.hasObjet("Couteau"))) || joueur.hasObjet("Grenade")) {

                                                    System.out.println("\n" + joueur + ", sélectionnez une action à effectuer parmis:");
                                                    if (joueur.getPa() >= 2) {
                                                        System.out.println("main. Attaquer un joueur à main nue (2 PA)");
                                                    }
                                                    if (joueur.getPa() >= 1 && joueur.hasObjet("Couteau")) {
                                                        System.out.println("couteau. Attaquer un joueur avec un couteau (1 PA)");
                                                    }
                                                    if (joueur.getPa() >= 1 && joueur.hasObjet("Blaster")) {
                                                        System.out.println("blaster. Attaquer un joueur avec un blaster (1 PA)");
                                                    }
                                                    if (joueur.hasObjet("Grenade")) {
                                                        System.out.println("grenade. Lancer une grenade (gratuit)");
                                                    }
                                                    System.out.println("retour. Retourner au menu principal (gratuit)");

                                                    switch (Main.scanner.next()) {

                                                        case "main":
                                                            if (joueur.getPa() >= 2) {

                                                                ArrayList<Joueur> joueursSalle = new ArrayList<>();

                                                                //Construction de l'ArrayList des joueurs dans la même salle que joueur
                                                                for (Joueur joueurTemp : this.personnages) {
                                                                    if (joueurTemp.getPositionKey().equals(salleJoueur.getNom())) {
                                                                        joueursSalle.add(joueurTemp);
                                                                    }
                                                                }

                                                                int choixJoueur = -1;
                                                                boolean correctInput;

                                                                do {

                                                                    int i;

                                                                    System.out.println("\nChoississez un joueur à attaquer à main nues:");
                                                                    for (i = 0; i < joueursSalle.size(); i++) {
                                                                        System.out.println((i + 1) + ". " + joueursSalle.get(i) + " (2 PA)");
                                                                    }
                                                                    System.out.println((i + 1) + ". Personne (gratuit)");

                                                                    choixJoueur = Main.scanner.nextInt();

                                                                    correctInput = ((choixJoueur >= 1) && (choixJoueur <= joueursSalle.size() + 1));

                                                                } while (!correctInput);

                                                                if (choixJoueur <= joueursSalle.size()) {

                                                                    System.out.println("\nVous venez d'attaquer " + joueursSalle.get(choixJoueur - 1) + " à main nues");
                                                                    salleJoueur.addToHistorique(joueur + " vient d'attaquer " + joueursSalle.get(choixJoueur - 1) + " à main nues");

                                                                    joueursSalle.get(choixJoueur - 1).removePv(1);
                                                                    joueur.removePa(2);

                                                                } else if (choixJoueur != joueursSalle.size() + 1) {
                                                                    System.out.println(Main.msgErreurEntree);
                                                                }

                                                            } else {
                                                                System.out.println(Main.msgErreurEntree);
                                                            }
                                                            break;
                                                        case "couteau":
                                                            if (joueur.getPa() >= 1 && joueur.hasObjet("Couteau")) {

                                                                ArrayList<Joueur> joueursSalle = new ArrayList<>();

                                                                //Construction de l'ArrayList des joueurs dans la même salle que joueur
                                                                for (Joueur joueurTemp : this.personnages) {
                                                                    if (joueurTemp.getPositionKey().equals(salleJoueur.getNom())) {
                                                                        joueursSalle.add(joueurTemp);
                                                                    }
                                                                }

                                                                int choixJoueur = -1;
                                                                boolean correctInput;

                                                                do {

                                                                    int i;

                                                                    System.out.println("\nChoississez un joueur à attaquer au Couteau:");
                                                                    for (i = 0; i < joueursSalle.size(); i++) {
                                                                        System.out.println((i + 1) + ". " + joueursSalle.get(i) + " (1 PA)");
                                                                    }
                                                                    System.out.println((i + 1) + ". Personne (gratuit)");

                                                                    choixJoueur = Main.scanner.nextInt();

                                                                    correctInput = ((choixJoueur >= 1) && (choixJoueur <= joueursSalle.size() + 1));

                                                                } while (!correctInput);

                                                                if (choixJoueur <= joueursSalle.size()) {

                                                                    System.out.println("\nVous venez d'attaquer " + joueursSalle.get(choixJoueur - 1) + " au Couteau");
                                                                    salleJoueur.addToHistorique(joueur + " vient d'attaquer " + joueursSalle.get(choixJoueur - 1) + " au Coutea");

                                                                    joueursSalle.get(choixJoueur - 1).removePv(1);
                                                                    joueur.removePa(1);

                                                                } else if (choixJoueur != joueursSalle.size() + 1) {
                                                                    System.out.println(Main.msgErreurEntree);
                                                                }

                                                            } else {
                                                                System.out.println(Main.msgErreurEntree);
                                                            }
                                                            break;
                                                        case "blaster":
                                                            if (joueur.getPa() >= 1 && joueur.hasObjet("Blaster")) {

                                                                ArrayList<Joueur> joueursSalle = new ArrayList<>();

                                                                //Construction de l'ArrayList des joueurs dans la même salle que joueur
                                                                for (Joueur joueurTemp : this.personnages) {
                                                                    if (joueurTemp.getPositionKey().equals(salleJoueur.getNom())) {
                                                                        joueursSalle.add(joueurTemp);
                                                                    }
                                                                }

                                                                int choixJoueur = -1;
                                                                boolean correctInput;

                                                                do {

                                                                    int i;

                                                                    System.out.println("\nChoississez un joueur à attaquer au Blaster:");
                                                                    for (i = 0; i < joueursSalle.size(); i++) {
                                                                        System.out.println((i + 1) + ". " + joueursSalle.get(i) + " (1 PA)");
                                                                    }
                                                                    System.out.println((i + 1) + ". Personne (gratuit)");

                                                                    choixJoueur = Main.scanner.nextInt();

                                                                    correctInput = ((choixJoueur >= 1) && (choixJoueur <= joueursSalle.size() + 1));

                                                                } while (!correctInput);

                                                                if (choixJoueur <= joueursSalle.size()) {

                                                                    System.out.println("\nVous venez d'attaquer " + joueursSalle.get(choixJoueur - 1) + " au Blaster");
                                                                    salleJoueur.addToHistorique(joueur + " vient d'attaquer " + joueursSalle.get(choixJoueur - 1) + " au Blaster");

                                                                    joueursSalle.get(choixJoueur - 1).removePv(1);
                                                                    joueur.removePa(1);

                                                                } else if (choixJoueur != joueursSalle.size() + 1) {
                                                                    System.out.println(Main.msgErreurEntree);
                                                                }

                                                            } else {
                                                                System.out.println(Main.msgErreurEntree);
                                                            }
                                                            break;
                                                        case "grenade":
                                                            if (joueur.hasObjet("Grenade")) {

                                                                for (Joueur joueurTemp : this.personnages) {
                                                                    if (joueurTemp.getPositionKey().equals(salleJoueur.getNom())) {
                                                                        joueurTemp.removePv(6);
                                                                    }
                                                                }

                                                                System.out.println("\nVous vennez de lancer une grenade dans " + salleJoueur);
                                                                salleJoueur.addToHistorique(joueur + "vient de lancer une grande");

                                                            } else {
                                                                System.out.println(Main.msgErreurEntree);
                                                            }
                                                            break;
                                                        case "retour":
                                                            retour = true;
                                                        default:
                                                            System.out.println(Main.msgErreurEntree);

                                                    }

                                                } else {
                                                    System.out.println(Main.msgErreurEntree);
                                                }
                                                break;
                                            case "serum":
                                                if (joueur.hasObjet("Sérum retro-fongique") && joueur.getPa() >= 2) {

                                                    ArrayList<Joueur> joueursSalle = new ArrayList<>();

                                                    //Construction de l'ArrayList des joueurs dans la même salle que joueur
                                                    for (Joueur joueurTemp : this.personnages) {
                                                        if (joueurTemp.getPositionKey().equals(salleJoueur.getNom())) {
                                                            joueursSalle.add(joueurTemp);
                                                        }
                                                    }

                                                    int choixJoueur = -1;
                                                    boolean correctInput;

                                                    do {

                                                        int i;

                                                        System.out.println("\nChoississez un joueur sur qui utiliser le Sérum retro-fongique:");
                                                        for (i = 0; i < joueursSalle.size(); i++) {
                                                            System.out.println((i + 1) + ". " + joueursSalle.get(i) + " (2 PA)");
                                                        }
                                                        System.out.println((i + 1) + ". Personne (gratuit)");

                                                        choixJoueur = Main.scanner.nextInt();

                                                        correctInput = ((choixJoueur >= 1) && (choixJoueur <= joueursSalle.size() + 1));

                                                    } while (!correctInput);

                                                    if (choixJoueur <= joueursSalle.size()) {

                                                        System.out.println("\nVous venez d'utiliser le Sérum rétro-fongique sur " + joueursSalle.get(choixJoueur - 1));
                                                        salleJoueur.addToHistorique(joueur + " vient d'utiliser le Sérum rétro-fongique sur " + joueursSalle.get(choixJoueur - 1));

                                                        if (joueursSalle.get(choixJoueur - 1).isMush()) {
                                                            joueursSalle.get(choixJoueur - 1).transform();
                                                        }
                                                        joueur.removePa(2);

                                                    } else if (choixJoueur != joueursSalle.size() + 1) {
                                                        System.out.println(Main.msgErreurEntree);
                                                    }

                                                } else {
                                                    System.out.println(Main.msgErreurEntree);
                                                }
                                                break;
                                            case "bourreau":
                                                if (joueur.hasCompetence("Bourreau") && joueur.getPa() >= 2) {
                                                    //TODO
                                                } else {
                                                    System.out.println(Main.msgErreurEntree);
                                                }
                                                break;
                                            case "retour":
                                                retour = true;
                                                break;
                                            default:
                                                System.out.println(Main.msgErreurEntree);

                                        }

                                        break;
                                    case "stockage":

                                        System.out.println("\n" + joueur + ", sélectionnez une action à effectuer parmis:");
                                        System.out.println("regarder. Regarder les objets non cachés (gratuit)");
                                        System.out.println("deposer. Deposer un objet (gratuit)");
                                        if (!joueur.inventairePlein()) {
                                            System.out.println("prendre. Prendre un objet (gratuit)");
                                        }
                                        if (joueur.getPa() >= 1) {
                                            System.out.println("cacher. Cacher un objet (1 PA)");
                                        }
                                        if (joueur.getPa() >= 2 || ((joueur.hasCompetence("Observateur") && joueur.getPa() >= 1))) {
                                            System.out.println("fouiller. Fouiller l'unité de stockage" + (joueur.hasCompetence("Observateur") ? "(1 PA)" : "(2 PA)"));
                                        }
                                        System.out.println("retour. Retourner au menu principal (gratuit)");

                                        switch (Main.scanner.next()) {

                                            case "regarder":

                                                System.out.println("\nStockage de " + salleJoueur + ": " + salleJoueur.getStockage());
                                                salleJoueur.addToHistorique(joueur + "vient de regarder le stockage de la salle");

                                                break;
                                            case "deposer":

                                                System.out.println("\nSélectionner l'objet que vous voulez déposer:");
                                                int iDep = 1;
                                                for (Objet objet : joueur.getInventaire()) {
                                                    System.out.println(iDep + ". " + objet);
                                                    iDep++;
                                                }
                                                System.out.println(iDep + ". Ne rien deposer");

                                                int choixDep = Main.scanner.nextInt() - 1;

                                                if (choixDep < 0 || choixDep >= 3 || joueur.getInventaire()[choixDep].getNom().equals("Rien")) {
                                                    System.out.println(Main.msgErreurEntree);
                                                } else if (choixDep != iDep - 1) {

                                                    System.out.println("\nVous vennez de déposer " + joueur.getInventaire()[choixDep] + " dans le stockage de " + salleJoueur);
                                                    salleJoueur.addToHistorique(joueur + "vient de déposer " + joueur.getInventaire()[choixDep] + " dans le stockage");
                                                    salleJoueur.addObjet(joueur.getInventaire()[choixDep]);
                                                    joueur.getInventaire()[choixDep] = new Objet("Rien");

                                                }

                                                break;
                                            case "prendre":
                                                if (!joueur.inventairePlein()) {

                                                    System.out.println("\nSélectionner l'objet que vous voulez prendre:");

                                                    int iPren = 1;
                                                    for (Objet objet : salleJoueur.getStockage()) {
                                                        System.out.println(iPren + ". " + objet);
                                                        iPren++;
                                                    }
                                                    System.out.println(iPren + ". Ne rien prendre");

                                                    int choixPren = Main.scanner.nextInt() - 1;

                                                    if (choixPren < 0 || choixPren > iPren - 1) {
                                                        System.out.println(Main.msgErreurEntree);
                                                    } else if (choixPren != iPren - 1) {

                                                        System.out.println("\nVous vennez de prendre " + salleJoueur.getStockage().get(choixPren));
                                                        salleJoueur.addToHistorique(joueur + "vient de prendre " + salleJoueur.getStockage().get(choixPren) + " dans le stockage");
                                                        joueur.addObjet(salleJoueur.getStockage().get(choixPren));
                                                        salleJoueur.removeObjet(salleJoueur.getStockage().get(choixPren));

                                                    }
                                                } else {
                                                    System.out.println(Main.msgErreurEntree);
                                                }
                                                break;
                                            case "cacher":
                                                if (joueur.getPa() >= 1) {

                                                    System.out.println("\nSélectionner l'objet que vous voulez cacher:");
                                                    int iCach = 1;
                                                    for (Objet objet : salleJoueur.getStockage()) {
                                                        System.out.println(iCach + ". " + objet);
                                                        iCach++;
                                                    }
                                                    System.out.println(iCach + ". Ne rien cacher");

                                                    int choixCach = Main.scanner.nextInt() - 1;

                                                    if (choixCach < 0 || choixCach > iCach - 1) {
                                                        System.out.println(Main.msgErreurEntree);
                                                    } else if (choixCach != iCach - 1) {

                                                        System.out.println("\nVous vennez de cacher " + salleJoueur.getStockage().get(choixCach) + " dans le stockage de " + salleJoueur);
                                                        salleJoueur.addToHistorique(joueur + "vient de cacher " + salleJoueur.getStockage().get(choixCach) + " dans le stockage");
                                                        salleJoueur.cacherObjet(salleJoueur.getStockage().get(choixCach));
                                                        joueur.getInventaire()[choixCach] = new Objet("Rien");

                                                        joueur.removePa(1);

                                                    }

                                                } else {
                                                    System.out.println(Main.msgErreurEntree);
                                                }
                                                break;
                                            case "fouiller":
                                                if (joueur.getPa() >= 2 || ((joueur.hasCompetence("Observateur") && joueur.getPa() >= 1))) {

                                                    salleJoueur.fouiller();
                                                    System.out.println("\nVous fouillez " + salleJoueur);
                                                    salleJoueur.addToHistorique(joueur + "viens de fouiller la salle");

                                                    joueur.removePa(1);
                                                    if (!joueur.hasCompetence("Observateur")) {
                                                        joueur.removePa(1);
                                                    }

                                                } else {
                                                    System.out.println(Main.msgErreurEntree);
                                                }
                                                break;
                                            case "retour":
                                                retour = true;
                                                break;
                                            default:
                                                System.out.println(Main.msgErreurEntree);

                                        }

                                        break;
                                    case "reparer":
                                        if (joueur.competenceEquals("Technicien", 1) || joueur.getPa() >= 1) {

                                            System.out.println("\nSouhaitez-vous réparer:");
                                            System.out.println("equipement. Un équipement");
                                            System.out.println("porte. Une porte");
                                            System.out.println("retour. Retourner au menu principal");

                                            switch (Main.scanner.next()) {

                                                case "equipement":

                                                    ArrayList<Equipement> equipementsCasses = new ArrayList<>();

                                                    for (Equipement equipement : salleJoueur.getEquipements()) {
                                                        if (equipement.estCasse()) {
                                                            equipementsCasses.add(equipement);
                                                        }
                                                    }

                                                    int choixEquipement = -1;
                                                    boolean correctInputEquipement;

                                                    do {

                                                        int i;

                                                        System.out.println("\nChoississez un équipement à réparer parmis:");
                                                        for (i = 0; i < equipementsCasses.size(); i++) {
                                                            System.out.println((i + 1) + ". " + equipementsCasses.get(i) + (joueur.competenceEquals("Technicien", 1) ? " (gratuit)" : " (1 PA)"));
                                                        }
                                                        System.out.println((i + 1) + ". Ne rien faire (gratuit)");

                                                        choixEquipement = Main.scanner.nextInt();

                                                        correctInputEquipement = ((choixEquipement >= 1) && (choixEquipement <= equipementsCasses.size() + 1));

                                                    } while (!correctInputEquipement);

                                                    if (choixEquipement <= equipementsCasses.size()) {

                                                        equipementsCasses.get(choixEquipement - 1).toggleCasse();

                                                        System.out.println("\nVous vennez de réparer " + equipementsCasses.get(choixEquipement - 1));
                                                        salleJoueur.addToHistorique(joueur + " vient de réparer " + equipementsCasses.get(choixEquipement - 1));

                                                        if (!joueur.competenceEquals("Technicien", 1)) {
                                                            joueur.removePa(1);
                                                        } else {
                                                            joueur.setCompetence("Technicien", 0);
                                                        }

                                                    } else if (choixEquipement != salleJoueur.getEquipements().size() + 1) {
                                                        System.out.println(Main.msgErreurEntree);
                                                    }

                                                    break;
                                                case "porte":

                                                    ArrayList<Integer> portesCasses = new ArrayList<>();
                                                    int salleId = this.vaisseau.getSalleId(salleJoueur);

                                                    for (int i = 0; i < this.vaisseau.getPortes()[salleId].length; i++) {
                                                        if (this.vaisseau.getPortes()[salleId][i] == -1) {
                                                            portesCasses.add(i);
                                                        }
                                                    }

                                                    int choixPorte = -1;
                                                    boolean correctInputPorte;

                                                    do {

                                                        int i;

                                                        System.out.println("\nChoississez une porte à réparer parmis:");
                                                        for (i = 0; i < portesCasses.size(); i++) {
                                                            System.out.println((i + 1) + ". " + this.vaisseau.getSalles()[portesCasses.get(i)] + (joueur.competenceEquals("Technicien", 1) ? " (gratuit)" : " (1 PA)"));
                                                        }
                                                        System.out.println((i + 1) + ". Ne rien faire (gratuit)");

                                                        choixPorte = Main.scanner.nextInt();

                                                        correctInputPorte = ((choixPorte >= 1) && (choixPorte <= portesCasses.size() + 1));

                                                    } while (!correctInputPorte);

                                                    if (choixPorte <= portesCasses.size()) {

                                                        this.vaisseau.getPortes()[salleId][portesCasses.get(choixPorte - 1)] = 1;

                                                        System.out.println("\nVous vennez de réparer la porte vers " + this.vaisseau.getSalles()[portesCasses.get(choixPorte - 1)]);
                                                        salleJoueur.addToHistorique(joueur + " vient de réparer la porte vers " + this.vaisseau.getSalles()[portesCasses.get(choixPorte - 1)]);

                                                        if (!joueur.competenceEquals("Technicien", 1)) {
                                                            joueur.removePa(1);
                                                        }

                                                    } else if (choixPorte != salleJoueur.getEquipements().size() + 1) {
                                                        System.out.println(Main.msgErreurEntree);
                                                    }

                                                    break;
                                                case "retour":
                                                    retour = true;
                                                    break;
                                                default:
                                                    System.out.println(Main.msgErreurEntree);

                                            }

                                        } else {
                                            System.out.println(Main.msgErreurEntree);
                                        }
                                        break;
                                    case "eteindre":
                                        if (salleJoueur.estEnFeu() && joueur.hasObjet("Extincteur")) {

                                            System.out.println("\nVous vennez d'éteindre l'incendie dans " + joueur.getPositionKey());
                                            salleJoueur.addToHistorique(joueur + " vient d'éteindre l'incendies");

                                        } else {
                                            System.out.println(Main.msgErreurEntree);
                                        }
                                        break;
                                    case "detecter":
                                        if (joueur.estDans("Pont") && ((joueur.getPa() >= 2)) || (joueur.getPa() >= 1) && this.vaisseau.getSalle("Nexus").hasEquipement("Accélération de processeur")) {
                                            //TODO
                                        } else {
                                            System.out.println(Main.msgErreurEntree);
                                        }
                                        break;
                                    case "scanner":
                                        if (joueur.estDans("Pont") && ((joueur.getPa() >= 3)) || (joueur.getPa() >= 2) && joueur.hasCompetence("Astrophysicien")) {
                                            //TODO
                                        } else {
                                            System.out.println(Main.msgErreurEntree);
                                        }
                                        break;
                                    case "attaquer":
                                        if ((this.aliensActifs + this.aliensInter + this.aliensInactifs) > 0 && (joueur.getPositionKey().startsWith("Tourelle") || salleJoueur.hasEquipement("Jet d'attaque"))) {

                                            if (!joueur.hasObjet("Combinaison") && salleJoueur.hasEquipement("Jet d'attaque")) {
                                                System.out.println("\nLa prochaine fois, évitez de sortir dans l'espace sans combinaison...");
                                                joueur.removePv(14);
                                            } else {
                                                if (this.aliensActifs > 0) {
                                                    this.aliensActifs--;
                                                } else if (this.aliensInter > 0) {
                                                    this.aliensInter--;
                                                } else if (this.aliensInactifs > 0) {
                                                    this.aliensInactifs--;
                                                }
                                                joueur.removePa(1);
                                                System.out.println("\nVous vennez d'attaquer un vaisseau alien");
                                                salleJoueur.addToHistorique(joueur + " vient d'attaquer un vaisseau alien");
                                            }

                                        } else {
                                            System.out.println(Main.msgErreurEntree);
                                        }
                                        break;
                                    case "expedition":
                                        if (this.planete.estDecouverte() && joueur.hasCompetence("Pilote") && joueur.estDans("Baie Icarus")) {
                                            //TODO
                                        } else {
                                            System.out.println(Main.msgErreurEntree);
                                        }
                                        break;
                                    case "deplacer":
                                        if (joueur.estDans("Pont") && joueur.hasCompetence("Pilote")) {
                                            //TODO
                                        } else {
                                            System.out.println(Main.msgErreurEntree);
                                        }
                                        break;
                                    case "rentrer":
                                        if (joueur.estDans("Pont") && joueur.hasCompetence("Pilote") && this.vaisseau.getSalle("Salle des moteurs").getEquipement("Réacteur PILGRED").getValue() == 100) {

                                            System.out.println("\nLe vaisseau rentre sur Terre !");
                                            continuer = false;
                                            if (this.nbrMush == 0) {
                                                System.out.println("Aucun mush n'est à bord du vaisseau, l'équipage gagne la partie");
                                            } else {
                                                System.out.println("Il restait des mushs dans le vaisseau, ils gagnent alors la partie");
                                            }

                                        } else {
                                            System.out.println(Main.msgErreurEntree);
                                        }
                                        break;
                                    case "coucher":
                                        if (salleJoueur.hasEquipement("Lit") && salleJoueur.getEquipement("Lit").getValue() > 0) {

                                            joueur.toogleEstCouche();
                                            salleJoueur.addToHistorique(joueur + " se couche");
                                            salleJoueur.getEquipement("Lit").removeValue(1);

                                        } else {
                                            System.out.println(Main.msgErreurEntree);
                                        }
                                        break;
                                    case "pilgred":
                                        if (joueur.estDans("Salle des moteurs") && this.vaisseau.getSalle("Salle des moteurs").hasEquipement("Réacteur PILGRED") && this.vaisseau.getSalle("Salle des moteurs").getEquipement("Réacteur PILGRED").getValue() < 100 && (joueur.competenceEquals("Physicien", 1) || joueur.getPa() >= 3)) {

                                            this.pilgred += 10;
                                            if (this.pilgred >= 100) {
                                                this.pilgred = 100;
                                            }

                                            System.out.println("Vous vennez de réparer 10% du réacteur PILGRED");
                                            salleJoueur.addToHistorique(joueur + " viens de faire avancer la réparation du réacteur PILGRED");

                                            if (!joueur.competenceEquals("Physicien", 1)) {
                                                joueur.removePa(3);
                                            }

                                            if (this.pilgred == 100) {
                                                salleJoueur.addToHistorique("La réparation du réacteur PILGRED est terminée !");
                                                salleJoueur.addEquipement("Réacteur PILGRED", 1);
                                            }

                                        } else {
                                            System.out.println(Main.msgErreurEntree);
                                        }
                                        break;
                                    case "douche":
                                        if (salleJoueur.hasEquipement("Douche")) {

                                            joueur.douche();
                                            System.out.println("\nVous vennez de vous doucher");
                                            joueur.removePa(2);
                                            if (joueur.hasObjet("Savon")) {
                                                joueur.addPa(1);
                                            } else if (joueur.hasObjet("Savon mushicide")) {
                                                joueur.addPa(1);
                                                joueur.removeSpore(1);
                                            }
                                            if (joueur.isMush()) {
                                                joueur.removePv(3);
                                            }

                                            salleJoueur.addToHistorique(joueur + " vient de se doucher");

                                        } else {
                                            System.out.println(Main.msgErreurEntree);
                                        }
                                        break;
                                    case "camera":
                                        if (salleJoueur.hasEquipement("Caméra")) {

                                            System.out.println("\nVous vennez de désinstaller une caméra dans " + salleJoueur);
                                            salleJoueur.addToHistorique(joueur + " viens de désinstaller une caméra");

                                            if (joueur.inventairePlein()) {
                                                salleJoueur.addObjet(new Objet("Caméra"));
                                            } else {
                                                joueur.addObjet(new Objet("Caméra"));
                                            }
                                            salleJoueur.removeCamera();

                                            joueur.removePa(4);

                                        } else if (joueur.hasObjet("Caméra")) {

                                            System.out.println("\nVous vennez d'installer une caméra dans " + salleJoueur);
                                            salleJoueur.addToHistorique(joueur + " viens d'installer une caméra");

                                            joueur.removeObjet("Caméra");
                                            salleJoueur.addEquipement("Caméra", 1);

                                            joueur.removePa(4);

                                        }
                                        break;
                                    case "cuisiner":
                                        if (joueur.hasCompetence("Cuistot") && !joueur.competenceEquals("Cuistot", 0) && joueur.hasObjet("Ration standard")) {

                                            System.out.println("\nVous vennez de cuisiner une ration");
                                            salleJoueur.addToHistorique(joueur + " viens de cuisiner une ration");

                                            joueur.removeObjet("Ration standard");
                                            joueur.addObjet(new Objet("Ration cuisinee"));

                                        } else {
                                            System.out.println(Main.msgErreurEntree);
                                        }
                                        break;
                                    case "manger":
                                        if ((joueur.hasObjet("Ration standard") || joueur.hasObjet("Ration cuisinée")) && !(joueur.getSasiete() == 3)) {

                                            System.out.println("\nQuelle ration voulez-vous manger ?");
                                            if (joueur.hasObjet("Ration standard")) {
                                                System.out.println("standard. Ration standard");
                                            }
                                            if (joueur.hasObjet("Ration cuisinée")) {
                                                System.out.println("cuisinee. Ration cuisinée");
                                            }
                                            System.out.println("retour. Retourner au menu principal");

                                            switch (Main.scanner.next()) {

                                                case "standard":
                                                    if (joueur.hasObjet("Ration standard")) {

                                                        joueur.removeObjet("Ration Standard");

                                                        joueur.mange();

                                                        joueur.addPa(2);
                                                        joueur.removePmo(1);

                                                        System.out.println("Vous vennez de manger une ration standard");
                                                        salleJoueur.addToHistorique(joueur + "vient de manger une ration standard");

                                                    } else {
                                                        System.out.println(Main.msgErreurEntree);
                                                    }
                                                    break;
                                                case "cuisinee":
                                                    if (joueur.hasObjet("Ration cuisinée")) {

                                                        joueur.removeObjet("Ration cuisinée");

                                                        joueur.mange();

                                                        joueur.addPa(4);
                                                        joueur.addPmo(3);

                                                        System.out.println("Vous vennez de manger une Ration cuisinée");
                                                        salleJoueur.addToHistorique(joueur + "vient de manger une Ration cuisinée");

                                                    } else {
                                                        System.out.println(Main.msgErreurEntree);
                                                    }
                                                    break;
                                                case "retour":
                                                    retour = true;
                                                    break;
                                                default:
                                                    System.out.println(Main.msgErreurEntree);

                                            }

                                        } else {
                                            System.out.println(Main.msgErreurEntree);
                                        }
                                        break;
                                    case "mycoscan":
                                        if (joueur.estDans("Laboratoire") && this.vaisseau.getSalle("Laboratoire").hasEquipement("Mycoscan")) {
                                            System.out.println("\nVous possèdez actuellement " + joueur.getSpores() + "spores sur vous");
                                        } else {
                                            System.out.println(Main.msgErreurEntree);
                                        }
                                        break;
                                    case "recherches":
                                        if (joueur.estDans("Laboratoire")) {

                                            System.out.println("\n" + joueur + ", sélectionnez une action à effectuer parmis:");
                                            if (joueur.getPa() >= 2 && !this.recherches.get("Mycoscan").equals(100)) {
                                                System.out.println("mycoscan. Faire progresser la recherche sur le Mycoscan de " + ((joueur.hasCompetence("Biologiste")) ? "13-20%" : "3-10%") + " (2 PA)");
                                            }
                                            if (joueur.getPa() >= 2 && !this.recherches.get("Gaz antispore").equals(100)) {
                                                System.out.println("gaz. Faire progresser la recherche sur le Gaz antispore de " + ((joueur.hasCompetence("Biologiste")) ? "16-19%" : "6-9%") + " (2 PA)");
                                            }
                                            if (joueur.getPa() >= 2 && !this.recherches.get("Sérum de constipaspore").equals(100)) {
                                                System.out.println("constipaspore. Faire progresser la recherche sur le Sérum de constipaspore de " + ((joueur.hasCompetence("Biologiste")) ? "20-25%" : "10-15%") + " (2 PA)");
                                            }
                                            if (joueur.getPa() >= 2 && !this.recherches.get("Savon mushicide").equals(100)) {
                                                System.out.println("savon. Faire progresser la recherche sur le Savon mushicide de " + ((joueur.hasCompetence("Biologiste")) ? "13-14%" : "3-4%") + " (2 PA)");
                                            }
                                            if (joueur.getPa() >= 2 && !this.recherches.get("Sérum rétro-fongique").equals(100) && salleJoueur.hasObject("Souche de test mush") && this.getJoueur("Zhong Chun").getPositionKey().equals("Laboratoire")) {
                                                System.out.println("retro. Faire progresser la recherche sur le Sérum rétro-fongique de " + ((joueur.hasCompetence("Biologiste")) ? "11-14%" : "1-4%") + " (2 PA)");
                                            }
                                            if (joueur.getPa() >= 2 && !this.recherches.get("Extracteur de spores").equals(100)) {
                                                System.out.println("extracteur. Faire progresser la recherche sur l'Extracteur de spores de " + ((joueur.hasCompetence("Biologiste")) ? "13-17%" : "3-7%") + " (2 PA)");
                                            }

                                            System.out.println("retour. Retourner au menu principal");

                                            switch (Main.scanner.next()) {

                                                case "mycoscan":
                                                    if (joueur.getPa() >= 2 && !this.recherches.get("Mycoscan").equals(100)) {

                                                        Random rand = new Random();
                                                        int n = rand.nextInt(11) + 3;
                                                        if (joueur.hasCompetence("Biologiste")) {
                                                            n += 10;
                                                        }

                                                        this.avancerRecherche("Mycoscan", n);

                                                        System.out.println("Vous vennez de faire avancer la recherche sur le Mycoscan de " + n + "%");
                                                        salleJoueur.addToHistorique(joueur + " viens de faire avancer la recherche sur le Mycoscan");

                                                        joueur.removePa(2);

                                                        if (!joueur.hasObjet("Tablier intachable")) {
                                                            joueur.sali();
                                                        }

                                                        if (this.recherches.get("Mycoscan").equals(100)) {
                                                            salleJoueur.addToHistorique("La recherche sur le Mycoscan est terminée !");
                                                            salleJoueur.addEquipement("Mycoscan", 1);
                                                        }

                                                    } else {
                                                        System.out.println(Main.msgErreurEntree);
                                                    }
                                                    break;
                                                case "gaz":
                                                    if (joueur.getPa() >= 2 && !this.recherches.get("Gaz antispore").equals(100)) {

                                                        Random rand = new Random();
                                                        int n = rand.nextInt(10) + 6;
                                                        if (joueur.hasCompetence("Biologiste")) {
                                                            n += 10;
                                                        }

                                                        this.avancerRecherche("Gaz antispore", n);

                                                        System.out.println("Vous vennez de faire avancer la recherche sur le Gaz antispore de " + n + "%");
                                                        salleJoueur.addToHistorique(joueur + " viens de faire avancer la recherche sur le Gaz antispore");

                                                        joueur.removePa(2);

                                                        if (!joueur.hasObjet("Tablier intachable")) {
                                                            joueur.sali();
                                                        }

                                                        if (this.recherches.get("Gaz antispore").equals(100)) {
                                                            salleJoueur.addToHistorique("La recherche sur le Gaz antispore est terminée !");
                                                            salleJoueur.addEquipement("Gaz antispore", 1);
                                                        }

                                                    } else {
                                                        System.out.println(Main.msgErreurEntree);
                                                    }
                                                    break;
                                                case "constipaspore":
                                                    if (joueur.getPa() >= 2 && !this.recherches.get("Sérum de constipaspore").equals(100)) {

                                                        Random rand = new Random();
                                                        int n = rand.nextInt(16) + 10;
                                                        if (joueur.hasCompetence("Biologiste")) {
                                                            n += 10;
                                                        }

                                                        this.avancerRecherche("Sérum de constipaspore", n);

                                                        System.out.println("Vous vennez de faire avancer la recherche sur le Sérum de constipaspore de " + n + "%");
                                                        salleJoueur.addToHistorique(joueur + " viens de faire avancer la recherche sur le Sérum de constipaspore");

                                                        joueur.removePa(2);

                                                        if (!joueur.hasObjet("Tablier intachable")) {
                                                            joueur.sali();
                                                        }

                                                        if (this.recherches.get("Sérum de constipaspore").equals(100)) {
                                                            salleJoueur.addToHistorique("La recherche sur le Sérum de constipaspore est terminée !");
                                                            salleJoueur.addEquipement("Sérum de constipaspore", 1);
                                                        }

                                                    } else {
                                                        System.out.println(Main.msgErreurEntree);
                                                    }
                                                    break;
                                                case "savon":
                                                    if (joueur.getPa() >= 2 && !this.recherches.get("Savon mushicide").equals(100)) {

                                                        Random rand = new Random();
                                                        int n = rand.nextInt(5) + 3;
                                                        if (joueur.hasCompetence("Savon mushicide")) {
                                                            n += 10;
                                                        }

                                                        this.avancerRecherche("Savon mushicide", n);

                                                        System.out.println("Vous vennez de faire avancer la recherche sur le Savon mushicide de " + n + "%");
                                                        salleJoueur.addToHistorique(joueur + " viens de faire avancer la recherche sur le Savon mushicide");

                                                        joueur.removePa(2);

                                                        if (!joueur.hasObjet("Tablier intachable")) {
                                                            joueur.sali();
                                                        }

                                                        if (this.recherches.get("Savon mushicide").equals(100)) {
                                                            salleJoueur.addToHistorique("La recherche sur le Savon mushicide est terminée !");
                                                            salleJoueur.addObjet(new Objet("Savon mushicide"));
                                                        }

                                                    } else {
                                                        System.out.println(Main.msgErreurEntree);
                                                    }
                                                    break;
                                                case "retro":
                                                    if (joueur.getPa() >= 2 && !this.recherches.get("Sérum rétro-fongique").equals(100) && salleJoueur.hasObject("Souche de test mush") && this.getJoueur("Zhong Chun").getPositionKey().equals("Laboratoire")) {

                                                        Random rand = new Random();
                                                        int n = rand.nextInt(5) + 1;
                                                        if (joueur.hasCompetence("Biologiste")) {
                                                            n += 10;
                                                        }

                                                        this.avancerRecherche("Sérum rétro-fongique", n);

                                                        System.out.println("Vous vennez de faire avancer la recherche sur le Sérum rétro-fongique de " + n + "%");
                                                        salleJoueur.addToHistorique(joueur + " viens de faire avancer la recherche sur le Sérum rétro-fongique");

                                                        joueur.removePa(2);

                                                        if (!joueur.hasObjet("Tablier intachable")) {
                                                            joueur.sali();
                                                        }

                                                        if (this.recherches.get("Sérum rétro-fongique").equals(100)) {
                                                            salleJoueur.addToHistorique("La recherche sur le Sérum rétro-fongique est terminée !");
                                                            salleJoueur.addObjet(new Objet("Sérum rétro-fongique"));
                                                        }

                                                    } else {
                                                        System.out.println(Main.msgErreurEntree);
                                                    }
                                                    break;
                                                case "extracteur":
                                                    if (joueur.getPa() >= 2 && !this.recherches.get("Extracteur de spores").equals(100)) {

                                                        Random rand = new Random();
                                                        int n = rand.nextInt(8) + 3;
                                                        if (joueur.hasCompetence("Biologiste")) {
                                                            n += 10;
                                                        }

                                                        this.avancerRecherche("Extracteur de spores", n);

                                                        System.out.println("Vous vennez de faire avancer la recherche sur le Extracteur de spores de " + n + "%");
                                                        salleJoueur.addToHistorique(joueur + " viens de faire avancer la recherche sur le Extracteur de spores");

                                                        joueur.removePa(2);

                                                        if (!joueur.hasObjet("Tablier intachable")) {
                                                            joueur.sali();
                                                        }

                                                        if (this.recherches.get("Extracteur de spores").equals(100)) {
                                                            salleJoueur.addToHistorique("La recherche sur le Extracteur de spores est terminée !");
                                                            salleJoueur.addObjet(new Objet("Extracteur de spores"));
                                                        }

                                                    } else {
                                                        System.out.println(Main.msgErreurEntree);
                                                    }
                                                    break;
                                                case "retour":
                                                    retour = true;
                                                    break;
                                                default:
                                                    System.out.println(Main.msgErreurEntree);

                                            }

                                        } else {
                                            System.out.println(Main.msgErreurEntree);
                                        }
                                        break;
                                    case "projets":
                                        if (joueur.estDans("Nexus")) {

                                            System.out.println("\n" + joueur + ", sélectionnez une action à effectuer parmis:");
                                            if ((joueur.getPa() >= 2 || (joueur.hasCompetence("Informatition") && !joueur.competenceEquals("Informatitien", 0))) && !this.projets.get("Accélération du processeur").equals(100)) {
                                                System.out.println("processeur. Faire progresser le projet d'accélération du processeur de 6-9% " + ((joueur.hasCompetence("Informatition") && !joueur.competenceEquals("Informatitien", 0)) ? "(gratuit)" : "(2 PA)"));
                                            }
                                            if ((joueur.getPa() >= 2 || (joueur.hasCompetence("Informatition") && !joueur.competenceEquals("Informatitien", 0))) && !this.projets.get("Arrosseurs automatiques").equals(100)) {
                                                System.out.println("arroseurs. Faire progresser le projet d'arroseurs automatiques de 10-12% " + ((joueur.hasCompetence("Informatition") && !joueur.competenceEquals("Informatitien", 0)) ? "(gratuit)" : "(2 PA)"));
                                            }
                                            if ((joueur.getPa() >= 2 || (joueur.hasCompetence("Informatition") && !joueur.competenceEquals("Informatitien", 0))) && !this.projets.get("Conduites oxygénées").equals(100)) {
                                                System.out.println("conduites. Faire progresser le projet de conduites oxygénées de 10-15% " + ((joueur.hasCompetence("Informatition") && !joueur.competenceEquals("Informatitien", 0)) ? "(gratuit)" : "(2 PA)"));
                                            }
                                            if ((joueur.getPa() >= 2 || (joueur.hasCompetence("Informatition") && !joueur.competenceEquals("Informatitien", 0))) && !this.projets.get("Bouclier plasma").equals(100)) {
                                                System.out.println("bouclier. Faire progresser le projet de bouclier plasma de 20-30% " + ((joueur.hasCompetence("Informatition") && !joueur.competenceEquals("Informatitien", 0)) ? "(gratuit)" : "(2 PA)"));
                                            }
                                            if ((joueur.getPa() >= 2 || (joueur.hasCompetence("Informatition") && !joueur.competenceEquals("Informatitien", 0))) && !this.projets.get("Réducteur de trainée").equals(100)) {
                                                System.out.println("reducteur. Faire progresser le projet de réducteur de trainée de 2-3% " + ((joueur.hasCompetence("Informatition") && !joueur.competenceEquals("Informatitien", 0)) ? "(gratuit)" : "(2 PA)"));
                                            }
                                            System.out.println("retour. Retourner au menu principal");

                                            switch (Main.scanner.next()) {

                                                case "processer":
                                                    if ((joueur.getPa() >= 2 || (joueur.hasCompetence("Informatition") && !joueur.competenceEquals("Informatitien", 0))) && !this.projets.get("Accélération du processeur").equals(100)) {

                                                        Random rand = new Random();
                                                        int n = rand.nextInt(10) + 9;

                                                        this.avancerProjet("Accélération du processeur", n);

                                                        System.out.println("Vous vennez de faire avancer le projet d'Accélération du processeur de " + n + "%");
                                                        salleJoueur.addToHistorique(joueur + " viens de faire avancer le projet d'Accélération du processeur");

                                                        if (!(joueur.hasCompetence("Informatition") && !joueur.competenceEquals("Informatitien", 0))) {
                                                            joueur.removePa(2);
                                                        }

                                                        if (this.projets.get("Accélération du processeur").equals(100)) {
                                                            salleJoueur.addToHistorique("e projet d'Accélération du processeur est terminée !");
                                                            salleJoueur.addEquipement("Accélération du processeur", 1);
                                                        }

                                                    } else {
                                                        System.out.println(Main.msgErreurEntree);
                                                    }
                                                    break;
                                                case "arrosseurs":
                                                    if ((joueur.getPa() >= 2 || (joueur.hasCompetence("Informatition") && !joueur.competenceEquals("Informatitien", 0))) && !this.projets.get("Arrosseurs automatiques").equals(100)) {

                                                        Random rand = new Random();
                                                        int n = rand.nextInt(10) + 9;

                                                        this.avancerProjet("Arrosseurs automatiques", n);

                                                        System.out.println("Vous vennez de faire avancer le projet d'Arrosseurs automatiques de " + n + "%");
                                                        salleJoueur.addToHistorique(joueur + " viens de faire avancer le projet d'Arrosseurs automatiques");

                                                        if (!(joueur.hasCompetence("Informatition") && !joueur.competenceEquals("Informatitien", 0))) {
                                                            joueur.removePa(2);
                                                        }

                                                        if (this.projets.get("Arrosseurs automatiques").equals(100)) {
                                                            salleJoueur.addToHistorique("e projet d'Arrosseurs automatiques est terminée !");
                                                            salleJoueur.addEquipement("Arrosseurs automatiques", 1);
                                                        }

                                                    } else {
                                                        System.out.println(Main.msgErreurEntree);
                                                    }
                                                    break;
                                                case "conduites":
                                                    if ((joueur.getPa() >= 2 || (joueur.hasCompetence("Informatition") && !joueur.competenceEquals("Informatitien", 0))) && !this.projets.get("Conduites oxygénées").equals(100)) {

                                                        Random rand = new Random();
                                                        int n = rand.nextInt(10) + 9;

                                                        this.avancerProjet("Conduites oxygénées", n);

                                                        System.out.println("Vous vennez de faire avancer le projet de Conduites oxygénées de " + n + "%");
                                                        salleJoueur.addToHistorique(joueur + " viens de faire avancer le projet de Conduites oxygénées");

                                                        if (!(joueur.hasCompetence("Informatition") && !joueur.competenceEquals("Informatitien", 0))) {
                                                            joueur.removePa(2);
                                                        }

                                                        if (this.projets.get("Conduites oxygénées").equals(100)) {
                                                            salleJoueur.addToHistorique("e projet de Conduites oxygénées est terminée !");
                                                            salleJoueur.addEquipement("Conduites oxygénées", 1);
                                                        }

                                                    } else {
                                                        System.out.println(Main.msgErreurEntree);
                                                    }
                                                    break;
                                                case "bouclier":
                                                    if ((joueur.getPa() >= 2 || (joueur.hasCompetence("Informatition") && !joueur.competenceEquals("Informatitien", 0))) && !this.projets.get("Bouclier plasma").equals(100)) {

                                                        Random rand = new Random();
                                                        int n = rand.nextInt(10) + 9;

                                                        this.avancerProjet("Bouclier plasma", n);

                                                        System.out.println("Vous vennez de faire avancer le projet de Bouclier plasma de " + n + "%");
                                                        salleJoueur.addToHistorique(joueur + " viens de faire avancer le projet de Bouclier plasma");

                                                        if (!(joueur.hasCompetence("Informatition") && !joueur.competenceEquals("Informatitien", 0))) {
                                                            joueur.removePa(2);
                                                        }

                                                        if (this.projets.get("Bouclier plasma").equals(100)) {
                                                            salleJoueur.addToHistorique("e projet de Bouclier plasma est terminée !");
                                                            salleJoueur.addEquipement("Bouclier plasma", 1);
                                                            this.vaisseau.addArmure(200);
                                                        }

                                                    } else {
                                                        System.out.println(Main.msgErreurEntree);
                                                    }
                                                    break;
                                                case "reducteur":
                                                    if ((joueur.getPa() >= 2 || (joueur.hasCompetence("Informatition") && !joueur.competenceEquals("Informatitien", 0))) && !this.projets.get("Réducteur de trainée").equals(100)) {

                                                        Random rand = new Random();
                                                        int n = rand.nextInt(10) + 9;

                                                        this.avancerProjet("Réducteur de trainée", n);

                                                        System.out.println("Vous vennez de faire avancer le projet de Réducteur de trainée de " + n + "%");
                                                        salleJoueur.addToHistorique(joueur + " viens de faire avancer le projet de Réducteur de trainée");

                                                        if (!(joueur.hasCompetence("Informatition") && !joueur.competenceEquals("Informatitien", 0))) {
                                                            joueur.removePa(2);
                                                        }

                                                        if (this.projets.get("Réducteur de trainée").equals(100)) {
                                                            salleJoueur.addToHistorique("e projet de Réducteur de trainée est terminée !");
                                                            salleJoueur.addEquipement("Réducteur de trainée", 1);
                                                        }

                                                    } else {
                                                        System.out.println(Main.msgErreurEntree);
                                                    }
                                                    break;
                                                case "retour":
                                                    retour = true;
                                                    break;
                                                default:
                                                    System.out.println(Main.msgErreurEntree);

                                            }

                                        } else {
                                            System.out.println(Main.msgErreurEntree);
                                        }
                                        break;
                                    case "retour":
                                        retour = true;
                                        break;
                                    default:
                                        System.out.println(Main.msgErreurEntree);
                                }

                            } else {
                                System.out.println(Main.msgErreurEntree);
                            }

                            break;
                        case "mush":
                            if (joueur.isMush() && !joueur.estCouche()) {

                                System.out.println("\n" + joueur + ", sélectionnez une action à effectuer parmis:");
                                int PaSpore = 2;
                                if (this.vaisseau.getSalle("Laboratoire").hasEquipement("Sérum de constipaspore")) {
                                    PaSpore += 2;
                                }

                                boolean estSeul = true;
                                for (Joueur joueurTemp : personnages) {
                                    if (joueur.getPositionKey().equals(joueurTemp.getPositionKey())) {
                                        estSeul = false;
                                    }
                                }

                                if (this.vaisseau.getSalle("Laboratoire").hasEquipement("Gaz antispore")) {
                                    this.maxSpore = 2;
                                } else {
                                    this.maxSpore = 4;
                                }

                                if (joueur.getPa() >= PaSpore && this.sporesExtraits < this.maxSpore && joueur.getSpores() < 2 && estSeul) {
                                    System.out.println("spore. Créer un spore (" + PaSpore + " PA)");

                                }
                                if (joueur.getPa() >= 2) {
                                    System.out.println("poincon. Poinçonner un joueur ou le chat avec un spore (2 PA)");

                                }
                                if (joueur.getPa() >= 1) {
                                    System.out.println("infecter. Infecter une ration de nourriture (1 PA)");

                                }
                                if ((joueur.getPa() >= 1) && (joueur.getSpores() >= 1)) {
                                    System.out.println("manger. Manger un spore (1 PA)");

                                }
                                if (joueur.getPa() >= 3) {
                                    System.out.println("saboter. Saboter un équipement (3 PA)");

                                }
                                System.out.println("consulter. Consuler le canal de communication mush (gratuit)");
                                System.out.println("envoyer. Ecrire dans le canal de communication mush (gratuit)");
                                System.out.println("retour. Retourner au menu principal (gratuit");

                                switch (Main.scanner.next().toLowerCase()) {

                                    case "spore":
                                        if (joueur.getPa() >= PaSpore && this.sporesExtraits < this.maxSpore && joueur.getSpores() < 2 && estSeul) {
                                            this.sporesExtraits++;
                                            joueur.addSpore(1);
                                            joueur.sali();
                                            if (salleJoueur.hasEquipement("Caméra")) {
                                                salleJoueur.addToHistorique(joueur + " vient de créer un spore");
                                            }
                                        } else {
                                            System.out.println(Main.msgErreurEntree);
                                        }
                                        break;
                                    case "poincon":
                                        if (joueur.getPa() >= 2) {

                                            ArrayList<Joueur> joueursSalle = new ArrayList<>();

                                            //Construction de l'ArrayList des joueurs dans la même salle que joueur
                                            for (Joueur joueurTemp : this.personnages) {
                                                if (joueurTemp.getPositionKey().equals(salleJoueur.getNom()) && !joueurTemp.isMush()) {
                                                    joueursSalle.add(joueurTemp);
                                                }
                                            }

                                            int choixJoueur = -1;
                                            boolean correctInput;

                                            do {

                                                int i;

                                                System.out.println("\nChoississez un joueur à poinconner:");
                                                for (i = 0; i < joueursSalle.size(); i++) {
                                                    System.out.println((i + 1) + ". " + joueursSalle.get(i) + " (2 PA)");
                                                }
                                                System.out.println((i + 1) + ". Personne (gratuit)");

                                                choixJoueur = Main.scanner.nextInt();

                                                correctInput = ((choixJoueur >= 1) && (choixJoueur <= joueursSalle.size() + 1));

                                            } while (!correctInput);

                                            if (choixJoueur <= joueursSalle.size()) {

                                                System.out.println("\nVous venez de poinconner " + joueursSalle.get(choixJoueur - 1));
                                                if (salleJoueur.hasEquipement("Caméra")) {
                                                    salleJoueur.addToHistorique(joueur + " vient de poinconner " + joueursSalle.get(choixJoueur - 1));
                                                }
                                                joueursSalle.get(choixJoueur - 1).addSpore(1);

                                                joueur.removePa(2);

                                            } else if (choixJoueur != joueursSalle.size() + 1) {
                                                System.out.println(Main.msgErreurEntree);
                                            }

                                        } else {
                                            System.out.println(Main.msgErreurEntree);
                                        }
                                        break;
                                    case "infecter":
                                        if (joueur.getPa() >= 1) {
                                            //TODO
                                        } else {
                                            System.out.println(Main.msgErreurEntree);
                                        }
                                        break;
                                    case "manger":
                                        if ((joueur.getPa() >= 1) && (joueur.getSpores() >= 1)) {

                                            joueur.removeSpore(1);
                                            joueur.addPa(3);
                                            joueur.addPm(2);
                                            joueur.sali();

                                            System.out.println("\nVous vennez de manger un spore");
                                            if (salleJoueur.hasEquipement("Caméra")) {
                                                salleJoueur.addToHistorique(joueur + " vient de manger un spore");
                                            }

                                        } else {
                                            System.out.println(Main.msgErreurEntree);
                                        }
                                        break;
                                    case "saboter":
                                        if (joueur.getPa() >= 3) {

                                            ArrayList<Equipement> equipementsNonCasses = new ArrayList<>();

                                            for (Equipement equipement : salleJoueur.getEquipements()) {
                                                if (!equipement.estCasse()) {
                                                    equipementsNonCasses.add(equipement);
                                                }
                                            }

                                            int choixEquipement = -1;
                                            boolean correctInputEquipement;

                                            do {

                                                int i;

                                                System.out.println("\nChoississez un équipement à saboter parmis:");
                                                for (i = 0; i < equipementsNonCasses.size(); i++) {
                                                    System.out.println((i + 1) + ". " + equipementsNonCasses.get(i) + " (3 PA)");
                                                }
                                                System.out.println((i + 1) + ". Ne rien faire (gratuit)");

                                                choixEquipement = Main.scanner.nextInt();

                                                correctInputEquipement = ((choixEquipement >= 1) && (choixEquipement <= equipementsNonCasses.size() + 1));

                                            } while (!correctInputEquipement);

                                            if (choixEquipement <= equipementsNonCasses.size()) {

                                                equipementsNonCasses.get(choixEquipement - 1).toggleCasse();

                                                System.out.println("\nVous vennez de saboter " + equipementsNonCasses.get(choixEquipement - 1));
                                                if (salleJoueur.hasEquipement("Caméra")) {
                                                    salleJoueur.addToHistorique(joueur + " vient de saboter " + equipementsNonCasses.get(choixEquipement - 1));

                                                }

                                                joueur.removePa(3);

                                            } else if (choixEquipement != salleJoueur.getEquipements().size() + 1) {
                                                System.out.println(Main.msgErreurEntree);
                                            }

                                        } else {
                                            System.out.println(Main.msgErreurEntree);
                                        }
                                        break;
                                    case "consulter":

                                        LinkedBlockingQueue<String> mushChatTemp = new LinkedBlockingQueue<>(this.mushChat);

                                        if (mushChatTemp.isEmpty()) {
                                            System.out.println("\nAucun message n'a encore été envoyé dans le canal de communication mush");
                                        } else {

                                            System.out.println("\nHistorique des messages envoyés dans le canal de communication mush:");

                                            int i = mushChatTemp.size();
                                            while (!mushChatTemp.isEmpty()) {
                                                System.out.println(i + ") " + mushChatTemp.poll());
                                                i--;
                                            }

                                        }

                                        break;
                                    case "envoyer":

                                        System.out.println("\nEntrez votre message à envoyer dans le canal de communication mush:");
                                        this.addToMushChat(Main.scanner.next().toLowerCase());

                                        break;
                                    case "retour":
                                        retour = true;
                                        break;
                                    default:
                                        System.out.println(Main.msgErreurEntree);

                                }

                            } else {
                                System.out.println(Main.msgErreurEntree);
                            }
                            break;
                        case "extraire":
                            if (joueur.hasObjet("Extracteur de Spore") && joueur.getPa() >= 1 && !joueur.estCouche()) {
                                joueur.removeSpore(1);
                                salleJoueur.addToHistorique(joueur + " vient de s'extraire un spore");
                                System.out.println("\nVous venez de vous extraire un spore");

                            } else {
                                System.out.println(Main.msgErreurEntree);
                            }
                            break;
                        case "chat":
                            if (joueur.hasObjet("Chat de Schrödinger") && joueur.getPa() >= 1 && !joueur.estCouche()) {
                                //TODO
                            } else {
                                System.out.println(Main.msgErreurEntree);
                            }
                            break;
                        case "conversion":
                            if (joueur.getPm() == 0 && joueur.getPa() >= 1 && !joueur.estCouche()) {

                                int pmConversion = 2;
                                if (joueur.hasCompetence("Sprinter")) {
                                    pmConversion += 2;
                                }
                                if (joueur.hasObjet("Trotinette")) {
                                    pmConversion += 2;
                                }

                                joueur.removePa(1);
                                joueur.addPm(pmConversion);

                                System.out.println("\nVous vennez de convertir 1 PA en " + pmConversion + " PM");
                                salleJoueur.addToHistorique(joueur + " vient de convertir des PA en PM");

                            } else {
                                System.out.println(Main.msgErreurEntree);
                            }
                            break;
                        case "deplacer":
                            if (joueur.getPm() >= 1 && !joueur.estCouche()) {

                                ArrayList<Salle> voisins = this.vaisseau.getVoisins(joueur.getPositionKey());

                                int choixSalle = -1;
                                boolean correctInput;

                                do {

                                    int i;

                                    System.out.println("\nChoississez une salle dans laquelle vous déplacer parmis:");
                                    for (i = 0; i < voisins.size(); i++) {
                                        System.out.println((i + 1) + ". " + voisins.get(i) + " (1 PM)");
                                    }
                                    System.out.println((i + 1) + ". Restez dans cette salle (gratuit)");

                                    choixSalle = Main.scanner.nextInt();

                                    correctInput = ((choixSalle >= 1) && (choixSalle <= voisins.size() + 1));

                                } while (!correctInput);

                                if (choixSalle <= voisins.size()) {

                                    salleJoueur.addToDeplacements(joueur + " quitte la salle");

                                    joueur.setPositionKey(voisins.get(choixSalle - 1).getNom());

                                    System.out.println("\nVous venez de vous déplacez dans " + joueur.getPositionKey());
                                    salleJoueur.addToDeplacements(joueur + " entre dans la salle");

                                    joueur.removePm(1);

                                } else if (choixSalle != voisins.size() + 1) {
                                    System.out.println(Main.msgErreurEntree);
                                }

                            } else {
                                System.out.println(Main.msgErreurEntree);
                            }
                            break;
                        case "fin":
                            stop = true;
                            retour = true;
                            break;
                        default:
                            System.out.println(Main.msgErreurEntree);

                    }

                    if (!retour) {
                        System.out.println("\nAppuyez sur Entrée pour continuer");
                        try {
                            System.in.read();
                        } catch (IOException ex) {
                            Logger.getLogger(Partie.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                }

            }

            //Action des ordinateurs
            for (Joueur joueur : ordinateurs) {

                //TODO actions aléatoires pour les joueurs contrôlés par les ordinateurs
            }

            this.nextCycle();

            //Clean des joueurs morts
            for (Iterator<Joueur> iterator = this.personnages.iterator(); iterator.hasNext();) {

                Joueur joueur = iterator.next();

                if (!joueur.estEnVie()) {
                    iterator.remove();
                    this.joueurs.remove(joueur);
                    this.ordinateurs.remove(joueur);

                    this.nbrJoueurs--;

                    if (joueur.isMush()) {
                        this.nbrMush--;
                    }
                }

            }

            //Vérification des conditions de victoires
            if (this.nbrJoueurs == 0) {
                System.out.println("\nTout les joueurs sont morts, égalité");
                continuer = false;
            } else if (this.nbrMush == this.nbrJoueurs) {
                System.out.println("\nTout l'équipage est mush, ils gagnent donc la partie");
                continuer = false;
            }

        } while (continuer);

    }

    private Joueur getJoueur(String key) {

        for (Joueur joueur : this.personnages) {
            if (joueur.getNom().equals(key)) {
                return joueur;
            }
        }

        return null;

    }

    private void avancerRecherche(String key, int n) {

        int m = this.recherches.get(key) + n;
        if (m >= 100) {
            m = 100;
        }

        this.recherches.put(key, m);

    }

    private void avancerProjet(String key, int n) {

        int m = this.projets.get(key) + n;
        if (m >= 100) {
            m = 100;
        }

        this.projets.put(key, m);

    }

}
