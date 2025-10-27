package modele.jeu;

import modele.plateau.Plateau;

public abstract class Joueur {
    protected String couleur; // "blanc" ou "noir"

    public Joueur(String couleur) {
        this.couleur = couleur;
    }

    public String getCouleur() {
        return couleur;
    }

    public abstract void jouer(Jeu jeu, Plateau plateau);
}
