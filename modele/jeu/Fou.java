package modele.jeu;

import java.util.List;
import modele.plateau.Case;
import modele.plateau.DecorateurCasesEnDiagonale;
import modele.plateau.Plateau;

public class Fou extends Piece {

    public Fou(String couleur, Case position) {
        super(couleur, position);
    }

    @Override
    public List<Case> getDeplacementsPossibles(Plateau plateau) {
        // On délègue les déplacements diagonaux au décorateur
        return new DecorateurCasesEnDiagonale(plateau).getCasesEnDiagonale(this);
    }

    @Override
    public String getNom() {
        return "Fou";
    }

    @Override
    public Piece copie() {
        Fou clone = new Fou(this.couleur, this.position);
        clone.setABouge(this.aBouge());
        return clone;
    }
}
