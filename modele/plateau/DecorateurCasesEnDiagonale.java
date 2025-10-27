package modele.plateau;

import java.util.ArrayList;
import java.util.List;
import modele.jeu.Piece;

public class DecorateurCasesEnDiagonale {
    private Plateau plateau;

    public DecorateurCasesEnDiagonale(Plateau plateau) {
        this.plateau = plateau;
    }

    public List<Case> getCasesEnDiagonale(Piece piece) {
        List<Case> deplacements = new ArrayList<>();

        if (piece == null) {
            return deplacements;
        }

        Direction[] directions = {
            Direction.HAUT_GAUCHE, Direction.HAUT_DROITE,
            Direction.BAS_GAUCHE, Direction.BAS_DROITE
        };

        for (Direction dir : directions) {
            int dx = dir.getDeltaX();
            int dy = dir.getDeltaY();
            int x = piece.getPosition().getX() + dx;
            int y = piece.getPosition().getY() + dy;

            while (true) {
                Case cible = plateau.getCase(x, y);
                if (cible == null) {
                    break;
                }
                if (!cible.estOccupee()) {
                    deplacements.add(cible);
                } else {
                    if (!cible.getPiece().getCouleur().equals(piece.getCouleur())) {
                        deplacements.add(cible);
                    }
                    break;
                }
                x += dx;
                y += dy;
            }
        }

        return deplacements;
    }
}
