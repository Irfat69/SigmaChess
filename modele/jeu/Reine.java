package modele.jeu;

import java.util.ArrayList;
import java.util.List;
import modele.plateau.Case;
import modele.plateau.DecorateurCasesEnDiagonale;
import modele.plateau.DecorateurCasesEnLigne;
import modele.plateau.Plateau;

public class Reine extends Piece {

    public Reine(String couleur, Case position) {
        super(couleur, position);
    }

    @Override
    public List<Case> getDeplacementsPossibles(Plateau plateau) {
        List<Case> deplacements = new ArrayList<>();
        deplacements.addAll(new DecorateurCasesEnLigne(plateau).getCasesEnLigne(this));
        deplacements.addAll(new DecorateurCasesEnDiagonale(plateau).getCasesEnDiagonale(this));
        return deplacements;
    }

    @Override
    public String getNom() {
        return "Reine";
    }

    @Override
    public Piece copie() {
        Reine clone = new Reine(this.couleur, this.position);
        clone.setABouge(this.aBouge());
        return clone;
    }
}
