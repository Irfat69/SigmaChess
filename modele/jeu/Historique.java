package modele.jeu;

import java.util.ArrayList;
import java.util.List;

public class Historique {
    private List<Coup> coups;

    public Historique() {
        this.coups = new ArrayList<>();
    }

    public void ajouterCoup(Coup coup) {
        this.coups.add(coup);
    }

    public Coup getDernierCoup() {
        if (coups.isEmpty()) return null;
        return coups.get(coups.size() - 1);
    }

    public List<Coup> getListeCoups() {
        return coups;
    }

    public boolean estVide() {
        return coups.isEmpty();
    }

    public void reinitialiser() {
        coups.clear();
    }
}
