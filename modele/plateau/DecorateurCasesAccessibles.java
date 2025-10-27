package modele.plateau;

import java.util.ArrayList;
import java.util.List;
import modele.jeu.Piece;

public class DecorateurCasesAccessibles {
    private Plateau plateau;

    public DecorateurCasesAccessibles(Plateau plateau) {
        this.plateau = plateau;
    }

    public List<Case> getCasesAccessibles(Piece piece) {
        if (piece == null) {
            return new ArrayList<>();
        }
        return piece.getDeplacementsPossibles(plateau);
    }
}
