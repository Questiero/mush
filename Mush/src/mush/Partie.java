package mush;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

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
    //Nombre de joueurs encore en vie dans la partie
    private int nbrJoueurs = 0;

    private boolean estMort = false;

    private final Planete planete = new Planete();

    //Conversations
    private final LinkedBlockingQueue mainChat = new LinkedBlockingQueue<>(10);
    private final LinkedBlockingQueue mushChat = new LinkedBlockingQueue<>(10);

    /**
     * Constructeur de Partie
     */
    public Partie() {

        this.vaisseau = new Vaisseau("Daedalus");
        this.initPersonnages();
    }

    private void initPersonnages() {
        
        //TODO Objets

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
        
        //TODO Logs
        
        //Incendies
        for(Salle salle : this.vaisseau.getSallesNonIncendie()) {
            if(rand.nextInt(100)<5) {
                salle.toggleIncendie();
            }
        }
        this.vaisseau.removeArmure(5*this.vaisseau.getSallesIncendie().size());
        
        //TODO Equipements endomagés
        
        //Plafond qui tombe
        for(Salle salle : this.vaisseau.getSalles()) {
            if(rand.nextInt(100)<2) {
                
                for(Joueur joueur : this.personnages) {
                    
                    if(joueur.getPositionKey().equals(salle.getNom())) {
                        joueur.removePv(6);
                    }
                    
                }
                
            }
        }
        
        //TODO Aliens
        
        this.vaisseau.removeOxygene(this.nbrJoueurs);

        //Gestion des compétences et changement de caractéristiques à chaque fin de cycle
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

        //TODO boucle et arrêt quand il faut genre
        
        //Actions des joueurs
        for (Joueur joueur : joueurs) {

            boolean stop = false;

            while (!stop) {

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
                    if (joueur.getPm() == 0 && joueur.getPa() >= 1) {
                        System.out.println("conversion. Convertir 1 PA en 2 PM (1 PA)"); //TODO +2 PM si Sprinter et +2 PM si Trotinnette
                    }
                    if (joueur.getPm() >= 1) {
                        System.out.println("deplacer. Changer de salle (1 PM)");
                    }
                } else {
                    System.out.println("lever. Se lever du lit (gratuit)");
                }

                System.out.println("fin. Finir votre tour (gratuit)");

                switch (Main.scanner.next()) {

                    case "lever":
                        if (joueur.estCouche()) {

                            joueur.toogleEstCouche();
                            this.vaisseau.getSalle(joueur.getPositionKey()).addToHistorique(joueur + " se lève");
                            this.vaisseau.getSalle(joueur.getPositionKey()).getEquipement("Lit").addValue(1);

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

                            switch (Main.scanner.next()) {

                                case "cameras":
                                    //TODO
                                    break;
                                case "incendie":
                                    //TODO

                                    break;
                                case "casse":
                                    //TODO
                                    break;
                                case "etat":
                                    this.vaisseau.afficherEtatVaisseau();
                                    break;
                                case "objets":
                                    //TODO
                                    break;
                                case "aliens":
                                    //TODO
                                    break;
                                case "planete":
                                    //TODO

                                    break;
                                case "recherchers":
                                    //TODO
                                    break;
                                case "neron":
                                    //TODO
                                    break;
                                case "mushs":
                                    System.out.println("\n" + this.nbrMush + " mush sont actuellement à bord du vaisseau");
                                    break;
                                case "morts":
                                    System.out.println("\n" + (13-this.nbrJoueurs) + " joueurs sont morts");
                                    break;
                                case "retour":
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

                            switch (Main.scanner.next()) {

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
                                    this.addToMainChat(Main.scanner.next());

                                    break;
                                case "retour":
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
                            if (this.vaisseau.getSalle(joueur.getPositionKey()).estEnFeu() && joueur.hasObjet("Extincteur")) {
                                System.out.println("eteindre. Eteindre un incendie dans la salle (gratuit)");
                            }
                            if (joueur.getPositionKey().equals("Pont") && ((joueur.getPa() >= 2)) || (joueur.getPa() >= 1) && this.vaisseau.getSalle("Nexus").hasEquipement("Accélération de processeur")) {
                                System.out.println("detecter. Detecter une planète à proximité" + ((this.vaisseau.getSalle("Nexus").hasEquipement("Accélération du processeur")) ? "(1 PA)" : "(2 PA)"));
                            }
                            if (joueur.getPositionKey().equals("Pont") && ((joueur.getPa() >= 3)) || (joueur.getPa() >= 2) && joueur.hasCompetence("Astrophysicien")) {
                                System.out.println("scanner. Scanner la planète" + (joueur.hasCompetence("Astrophysicien") ? "(2 PA)" : "(3 PA)"));
                            }
                            if (joueur.getPositionKey().startsWith("Tourelle") || this.vaisseau.getSalle(joueur.getPositionKey()).hasEquipement("Jet d'attaque")) {
                                System.out.println("attaquer. Attaquer un vaisseau alien (1 PA)");
                            }
                            if (this.planete.estDecouverte() && joueur.hasCompetence("Pilote") && joueur.getPositionKey().equals("Baie Icarus")) {
                                System.out.println("expedition. Lancer une expédition (3 PA)");
                            }
                            if (joueur.getPositionKey().equals("Pont") && joueur.hasCompetence("Pilote")) {
                                System.out.println("deplacer. Déplacer le Daedalus (3 PA)");
                            }
                            if (joueur.getPositionKey().equals("Pont") && joueur.hasCompetence("Pilote") && this.vaisseau.getSalle("Salle des moteurs").getEquipement("Réacteur PILGRED").getValue() == 100) {
                                System.out.println("rentrer. Rentrer sur Terre (5 PA)");
                            }
                            if (this.vaisseau.getSalle(joueur.getPositionKey()).hasEquipement("Lit") && this.vaisseau.getSalle(joueur.getPositionKey()).getEquipement("Lit").getValue() > 0) {
                                System.out.println("coucher. Se coucher dans un lit (gratuit)");
                            }
                            if (joueur.getPositionKey().equals("Salle des moteurs") && this.vaisseau.getSalle("Salle des moteurs").hasEquipement("Réacteur PILGRED") && this.vaisseau.getSalle("Salle des moteurs").getEquipement("Réacteur PILGRED").getValue() < 100 && (joueur.competenceEquals("Physicien", 1) || joueur.getPa() >= 3)) {
                                System.out.println("pilgred. Réparer le réacteur PILGRED de 10%" + (joueur.competenceEquals("Physicien", 1) ? "(gratuit)" : "(3 PA)"));
                            }
                            if (this.vaisseau.getSalle(joueur.getPositionKey()).hasEquipement("Douche")) {
                                System.out.println("douche. Se doucher" + ((joueur.hasObjet("Savon")) ? "(1 PA)" : "(2 PA"));
                            }
                            if (this.vaisseau.getSalle(joueur.getPositionKey()).hasEquipement("Camera")) {
                                System.out.println("camera. Désinstaller une caméra (4 PA)");
                            } else {
                                System.out.println("camera. Installer une caméra (4 PA)");
                            }
                            if (joueur.hasCompetence("Cuistot") && !joueur.competenceEquals("Cuistot", 0)) {
                                System.out.println("cuisiner. Cuisiner une ration (gratuit)");
                            }
                            if (joueur.getPositionKey().equals("Laboratoire") && this.vaisseau.getSalle("Laboratoire").hasEquipement("Mycoscan")) {
                                System.out.println("mycoscan. Connaitre le nombre de spores sur vous-même (gratuit)");
                            }
                            if (joueur.getPositionKey().equals("Laboratoire")) {
                                System.out.println("recherches. Afficher les recherches disponibles (gratuit)");
                            }
                            if (joueur.getPositionKey().equals("Nexus")) {
                                System.out.println("projets. Afficher les projets NERON disponibles (gratuit)");
                            }
                            System.out.println("retour. Retourner au menu principal (gratuit)");

                            switch (Main.scanner.next()) {

                                case "historique":

                                    LinkedBlockingQueue<String> historiqueTemp = new LinkedBlockingQueue<>(this.vaisseau.getSalle(joueur.getPositionKey()).getHistorique());

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

                                        LinkedBlockingQueue<String> deplacementsTemp = new LinkedBlockingQueue<>(this.vaisseau.getSalle(joueur.getPositionKey()).getDeplacements());

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
                                    //TODO
                                    break;
                                case "stockage":
                                    //TODO

                                    break;
                                case "reparer":
                                    if (joueur.competenceEquals("Technicien", 1) || joueur.getPa() >= 1) {
                                        //TODO
                                    } else {
                                        System.out.println(Main.msgErreurEntree);
                                    }
                                    break;
                                case "eteindre":
                                    if (this.vaisseau.getSalle(joueur.getPositionKey()).estEnFeu() && joueur.hasObjet("Extincteur")) {
                                        //TODO
                                    } else {
                                        System.out.println(Main.msgErreurEntree);
                                    }
                                    break;
                                case "detecter":
                                    if (joueur.getPositionKey().equals("Pont") && ((joueur.getPa() >= 2)) || (joueur.getPa() >= 1) && this.vaisseau.getSalle("Nexus").hasEquipement("Accélération de processeur")) {
                                        //TODO
                                    } else {
                                        System.out.println(Main.msgErreurEntree);
                                    }
                                    break;
                                case "scanner":
                                    if (joueur.getPositionKey().equals("Pont") && ((joueur.getPa() >= 3)) || (joueur.getPa() >= 2) && joueur.hasCompetence("Astrophysicien")) {
                                        //TODO
                                    } else {
                                        System.out.println(Main.msgErreurEntree);
                                    }
                                    break;
                                case "attaquer":
                                    if (joueur.getPositionKey().startsWith("Tourelle") || this.vaisseau.getSalle(joueur.getPositionKey()).hasEquipement("Jet d'attaque")) {
                                        //TODO
                                    } else {
                                        System.out.println(Main.msgErreurEntree);
                                    }
                                    break;
                                case "expedition":
                                    if (this.planete.estDecouverte() && joueur.hasCompetence("Pilote") && joueur.getPositionKey().equals("Baie Icarus")) {
                                        //TODO
                                    } else {
                                        System.out.println(Main.msgErreurEntree);
                                    }
                                    break;
                                case "deplacer":
                                    if (joueur.getPositionKey().equals("Pont") && joueur.hasCompetence("Pilote")) {
                                        //TODO
                                    } else {
                                        System.out.println(Main.msgErreurEntree);
                                    }
                                    break;
                                case "rentrer":
                                    if (joueur.getPositionKey().equals("Pont") && joueur.hasCompetence("Pilote") && this.vaisseau.getSalle("Salle des moteurs").getEquipement("Réacteur PILGRED").getValue() == 100) {
                                        //TODO
                                    } else {
                                        System.out.println(Main.msgErreurEntree);
                                    }
                                    break;
                                case "coucher":
                                    if (this.vaisseau.getSalle(joueur.getPositionKey()).hasEquipement("Lit") && this.vaisseau.getSalle(joueur.getPositionKey()).getEquipement("Lit").getValue() > 0) {

                                        joueur.toogleEstCouche();
                                        this.vaisseau.getSalle(joueur.getPositionKey()).addToHistorique(joueur + " se couche");
                                        this.vaisseau.getSalle(joueur.getPositionKey()).getEquipement("Lit").removeValue(1);

                                    } else {
                                        System.out.println(Main.msgErreurEntree);
                                    }
                                    break;
                                case "pilgred":
                                    if (joueur.getPositionKey().equals("Salle des moteurs") && this.vaisseau.getSalle("Salle des moteurs").hasEquipement("Réacteur PILGRED") && this.vaisseau.getSalle("Salle des moteurs").getEquipement("Réacteur PILGRED").getValue() < 100 && (joueur.competenceEquals("Physicien", 1) || joueur.getPa() >= 3)) {
                                        //TODO
                                    } else {
                                        System.out.println(Main.msgErreurEntree);
                                    }
                                    break;
                                case "douche":
                                    if (this.vaisseau.getSalle(joueur.getPositionKey()).hasEquipement("Douche")) {
                                        //TODO
                                    } else {
                                        System.out.println(Main.msgErreurEntree);
                                    }
                                    break;
                                case "camera":
                                    if (this.vaisseau.getSalle(joueur.getPositionKey()).hasEquipement("Camera")) {
                                        //TODO
                                    } else {
                                        //TODO
                                    }
                                    break;
                                case "cuisiner":
                                    if (!joueur.hasCompetence("Cuistot") && !joueur.competenceEquals("Cuistot", 0)) {
                                        //TODO
                                    } else {
                                        System.out.println(Main.msgErreurEntree);
                                    }
                                    break;
                                case "mycoscan":
                                    if (joueur.getPositionKey().equals("Laboratoire") && this.vaisseau.getSalle("Laboratoire").hasEquipement("Mycoscan")) {
                                        //TODO
                                    } else {
                                        System.out.println(Main.msgErreurEntree);
                                    }
                                    break;
                                case "recherches":
                                    if (joueur.getPositionKey().equals("Laboratoire")) {
                                        //TODO
                                    } else {
                                        System.out.println(Main.msgErreurEntree);
                                    }
                                    break;
                                case "projets":
                                    if (joueur.getPositionKey().equals("Nexus")) {
                                        //TODO
                                    } else {
                                        System.out.println(Main.msgErreurEntree);
                                    }
                                    break;
                                case "retour":
                                    break;
                                default:
                                    System.out.println(Main.msgErreurEntree);
                                    break;
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
                            if (joueur.getPa() >= PaSpore) {
                                System.out.println("spore. Créer un spore (" + PaSpore + " PA)");

                            }
                            if (joueur.getPa() >= 2) {
                                System.out.println("poincon. Poinçonner un joueur ou le chat avec un spore (2 PA)");

                            }
                            if (joueur.getPa() >= 1) {
                                System.out.println("infecter. Infecter une ration de nourriture (1 PA)");

                            }
                            if (joueur.getPa() >= 1) {
                                System.out.println("manger. Manger un spore (1 PA)");

                            }
                            if (joueur.getPa() >= 3) {
                                System.out.println("saboter. Saboter un équipement (3 PA)");

                            }
                            System.out.println("consulter. Consuler le canal de communication mush (gratuit)");
                            System.out.println("envoyer. Ecrire dans le canal de communication mush (gratuit)");
                            System.out.println("retour. Retourner au menu principal (gratuit");

                            switch (Main.scanner.next()) {

                                case "spore":
                                    if (joueur.getPa() >= PaSpore) {
                                        //TODO
                                    } else {
                                        System.out.println(Main.msgErreurEntree);
                                    }
                                    break;
                                case "poincon":
                                    if (joueur.getPa() >= 2) {
                                        //TODO
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
                                    if (joueur.getPa() >= 1) {
                                        //TODO
                                    } else {
                                        System.out.println(Main.msgErreurEntree);
                                    }
                                    break;
                                case "saboter":
                                    if (joueur.getPa() >= 3) {
                                        //TODO
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
                                    this.addToMushChat(Main.scanner.next());

                                    break;
                                case "retour":
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
                            //TODO
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
                            //TODO
                        } else {
                            System.out.println(Main.msgErreurEntree);
                        }
                        break;
                    case "deplacer":
                        if (joueur.getPm() >= 1 && !joueur.estCouche()) {

                            ArrayList<Salle> voisins = this.vaisseau.getVoisinsByKey(joueur.getPositionKey());

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

                                this.vaisseau.getSalle(joueur.getPositionKey()).addToDeplacements(joueur + " quitte la salle");

                                joueur.setPositionKey(voisins.get(choixSalle - 1).getNom());

                                System.out.println("\nVous venez de vous déplacez dans " + joueur.getPositionKey());
                                this.vaisseau.getSalle(joueur.getPositionKey()).addToDeplacements(joueur + " entre dans la salle");

                                joueur.removePm(1);

                            } else {
                                System.out.println(Main.msgErreurEntree);
                            }

                        } else {
                            System.out.println(Main.msgErreurEntree);
                        }
                        break;
                    case "fin":
                        stop = true;
                        break;
                    default:
                        System.out.println(Main.msgErreurEntree);

                }

            }

        }

        //Action des ordinateurs
        for (Joueur joueur : ordinateurs) {

            //TODO actions aléatoires pour les joueurs contrôlés par les ordinateurs
        }

        this.nextCycle();

    }

    /**
     * Arrête la partie
     */
    public void end() {
        //TODO
    }

}
