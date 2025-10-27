package modele.jeu;

import java.util.List;
import modele.plateau.Case;
import modele.plateau.Plateau;

public abstract class Piece {
    protected String couleur; // "blanc" ou "noir"
    protected Case position;
    protected boolean aBouge;

    public Piece(String couleur, Case position) {
        this.couleur = couleur;
        this.position = position;
        this.aBouge = false;
    }

    public String getCouleur() {
        return couleur;
    }

    public Case getPosition() {
        return position;
    }

    public void setPosition(Case nouvellePosition) {
        if (nouvellePosition != null) {
            this.position = nouvellePosition;
        }
    }

    public boolean aBouge() {
        return aBouge;
    }

    public void setABouge(boolean aBouge) {
        this.aBouge = aBouge;
    }

    /**
     * Renvoie les déplacements possibles de cette pièce selon le plateau.
     */
    public abstract List<Case> getDeplacementsPossibles(Plateau plateau);

    /**
     * Nom de la pièce (utile pour debug ou affichage).
     */
    public abstract String getNom();

    /**
     * Clone propre (à surcharger dans chaque sous-classe).
     */
    public abstract Piece copie();
}
