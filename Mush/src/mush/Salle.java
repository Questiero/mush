package mush;

import java.util.ArrayList;
import java.util.Random;

public class Salle {

    //Nom de la salle
    private final String nom;
    //Stockage de la salle
    private final ArrayList<Objet>[] stockage = new ArrayList[27];
    //variable aléatoir pour la répartition aléatoire des objets dans les salles
    private Random ra = new Random();

    /**
     * Constructeur de Salle
     *
     * @param nom nom de la Salle
     */
    public Salle(String nom) {
        this.nom = nom;
    }

    private void initStockage() {
        //valeur entre 1 et 19 le nombre des objets
        int x = ra.nextInt(18) + 1;
        
        
        //la quantité des objets
        int armureCpt = 1;
        int cleCpt = 1;
        int combinaisonCpt = 4;
        int gantsCpt = 1;
        int savonCpt = 1;
        int tablierCpt = 1;
        int trottinetteCpt = 1;
        int extincteurCpt = 3;
        int couteauCpt = 1;
        int blasterCpt = 2;
        int grenadeCpt = 1;
        int medikitCpt = 1;
        int cameraCpt = 2;
        int soucheCpt = 1;
        int debrisCpt = 30;
        int chatCpt = 1;
        int rationStandardCpt = 30;
        int exctracteurSporeCpt = 1;
        int serumCpt = 1;

        //répartition aléatoire des objets dans les stockages 
        switch (x) {
            case 1:
                if (armureCpt > 0) {
                    Objet armure = null;
                    int y = ra.nextInt(26) + 1;
                    stockage[y].add(armure);
                    armureCpt--;
                    break;
                }
            case 2:
                if (cleCpt > 0) {
                    Objet CleAMolette = null;
                    int y = ra.nextInt(26) + 1;
                    stockage[y].add(CleAMolette);
                    cleCpt--;
                    break;
                }
            case 3:
                if (combinaisonCpt > 0) {
                    Objet Combinaison = null;
                    int y = ra.nextInt(26) + 1;
                    stockage[y].add(Combinaison);
                    combinaisonCpt--;
                    break;
                }
            case 4:
                if (gantsCpt > 0) {
                    Objet paireDeGantsDeProtection = null;
                    int y = ra.nextInt(26) + 1;
                    stockage[y].add(paireDeGantsDeProtection);
                    gantsCpt--;
                    break;
                }
            case 5:
                if (savonCpt > 0) {
                    Objet savon = null;
                    int y = ra.nextInt(26) + 1;
                    stockage[y].add(savon);
                    savonCpt--;
                    break;
                }
            case 6:
                if (tablierCpt > 0) {
                    Objet tablierIntachable  = null;
                    int y = ra.nextInt(26) + 1;
                    stockage[y].add(tablierIntachable);
                    tablierCpt--;
                    break;
                }
            case 7:
                if (trottinetteCpt > 0) {
                    Objet trottinette = null;
                    int y = ra.nextInt(26) + 1;
                    stockage[y].add(trottinette);
                    trottinetteCpt--;
                    break;
                }
            case 8:
                if (extincteurCpt > 0) {
                    Objet extincteurs = null;
                    int y = ra.nextInt(26) + 1;
                    stockage[y].add(extincteurs);
                    extincteurCpt--;
                    break;
                }
            case 9:
                if (couteauCpt > 0) {
                    Objet couteau = null;
                    int y = ra.nextInt(26) + 1;
                    stockage[y].add(couteau);
                    couteauCpt--;
                    break;
                }
            case 10:
                if (blasterCpt > 0) {
                    Objet blaster = null;
                    int y = ra.nextInt(26) + 1;
                    stockage[y].add(blaster);
                    blasterCpt--;
                    break;
                }
            case 11:
                if (grenadeCpt > 0) {
                    Objet grenade = null;
                    int y = ra.nextInt(26) + 1;
                    stockage[y].add(grenade);
                    grenadeCpt--;
                    break;
                }
            case 12:
                if (medikitCpt > 0) {
                    Objet medikit = null;
                    int y = ra.nextInt(26) + 1;
                    stockage[y].add(medikit);
                    medikitCpt--;
                    break;
                }
            case 13:
                if (cameraCpt > 0) {
                    Objet camera = null;
                    int y = ra.nextInt(26) + 1;
                    stockage[y].add(camera);
                    cameraCpt--;
                    break;
                }
            case 14:
                if (soucheCpt > 0) {
                    Objet soucheDeTestMush = null;
                    int y = ra.nextInt(26) + 1;
                    stockage[y].add(soucheDeTestMush);
                    soucheCpt--;
                    break;
                }
            case 15:
                if (debrisCpt > 0) {
                    Objet DebrisMetalliques  = null;
                    int y = ra.nextInt(26) + 1;
                    stockage[y].add(DebrisMetalliques);
                    debrisCpt--;
                    break;
                }
            case 16:
                if (chatCpt > 0) {
                    Objet chatDeSchrödinger = null;
                    int y = ra.nextInt(26) + 1;
                    stockage[y].add(chatDeSchrödinger);
                    chatCpt--;
                    break;
                }
            case 17:
                if (rationStandardCpt > 0) {
                    Objet rationsStandard = null;
                    int y = ra.nextInt(26) + 1;
                    stockage[y].add(rationsStandard);
                    rationStandardCpt--;
                    break;
                }
            case 18:
                if (exctracteurSporeCpt > 0) {
                    Objet extracteurDeSpores = null;
                    int y = ra.nextInt(26) + 1;
                    stockage[y].add(extracteurDeSpores);
                    exctracteurSporeCpt--;
                    break;
                }
            case 19:
                if (serumCpt > 0) {
                    Objet SerumRetroFongique = null;
                    int y = ra.nextInt(26) + 1;
                    stockage[y].add(SerumRetroFongique);
                    serumCpt--;
                    break;
                }

        }
    }

}
