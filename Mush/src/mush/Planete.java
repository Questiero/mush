package mush;

public class Planete {

    private boolean estDecouverte = false;

    public void decouvre() {
        this.estDecouverte = true;
    }

    public boolean estDecouverte() {
        return this.estDecouverte;
    }

}
