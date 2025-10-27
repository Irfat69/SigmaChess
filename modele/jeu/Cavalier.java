package modele.jeu;

import java.util.ArrayList;
import java.util.List;
import modele.plateau.Case;
import modele.plateau.Plateau;

public class Cavalier extends Piece {

    public Cavalier(String couleur, Case position) {
        super(couleur, position);
    }

    @Override
    public List<Case> getDeplacementsPossibles(Plateau plateau) {
        List<Case> deplacements = new ArrayList<>();

        // Tous les déplacements possibles d'un cavalier (en L)
        int[][] mouvements = {
            {-2, -1}, {-2, 1},
            {-1, -2}, {-1, 2},
            {1, -2},  {1, 2},
            {2, -1},  {2, 1}
        };

        for (int[] move : mouvements) {
            int newX = position.getX() + move[0];
            int newY = position.getY() + move[1];
            Case cible = plateau.getCase(newX, newY);

            if (cible != null) {
                if (!cible.estOccupee() || !cible.getPiece().getCouleur().equals(this.couleur)) {
                    deplacements.add(cible);
                }
            }
        }

        return deplacements;
    }

    @Override
    public String getNom() {
        return "Cavalier";
    }

    @Override
    public Piece copie() {
        Cavalier clone = new Cavalier(this.couleur, this.position);
        clone.setABouge(this.aBouge());
        return clone;
    }
}
