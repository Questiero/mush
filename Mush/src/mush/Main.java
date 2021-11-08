package mush;

import java.util.Scanner;

public class Main {

    //Constantes statiques pour faciliter les entrées:
    public final static Scanner scanner = new Scanner(System.in);
    public final static String msgErreurEntree = "\nErreur, veuillez entrer une valeur correspondant au menu affiché\n";

    //Partie de jeu
    private final static Partie partie = new Partie();

    /**
     * Affiche le menu principal
     */
    private static void displayMainMenu() {

        System.out.println("\n");
        System.out.println("          ___  ___          _     ");
        System.out.println("          |  \\/  |         | |    ");
        System.out.println("          | .  . |_   _ ___| |__  ");
        System.out.println("          | |\\/| | | | / __| '_ \\ ");
        System.out.println("          | |  | | |_| \\__ \\ | | |");
        System.out.println("          \\_|  |_/\\__,_|___/_| |_|");
        System.out.println("\n");

        System.out.println("1. Nouvelle Partie");
        System.out.println("2. Quitter");

        switch (Main.scanner.nextInt()) {

            case 1:
                Main.partie.start();
                break;
            case 2:
                break;
            default:
                System.out.println(Main.msgErreurEntree);
                Main.displayMainMenu();
                break;

        }

    }

    public static void main(String[] args) {

        Main.displayMainMenu();

    }

}
