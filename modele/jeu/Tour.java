package modele.jeu;

import java.util.List;
import modele.plateau.Case;
import modele.plateau.DecorateurCasesEnLigne;
import modele.plateau.Plateau;

public class Tour extends Piece {

    public Tour(String couleur, Case position) {
        super(couleur, position);
    }

    @Override
    public List<Case> getDeplacementsPossibles(Plateau plateau) {
        // Utilise le décorateur pour récupérer les cases en ligne droite
        return new DecorateurCasesEnLigne(plateau).getCasesEnLigne(this);
    }

    @Override
    public String getNom() {
        return "Tour";
    }

    @Override
    public Piece copie() {
        Tour clone = new Tour(this.couleur, this.position);
        clone.setABouge(this.aBouge());
        return clone;
    }

    //  Optionnel : peut être supprimé si non utilisé ailleurs
    public boolean peutFaireRoqueAvec(Roi roi, Plateau plateau) {
        if (this.position.getX() != roi.getPosition().getX()) return false;
        if (this.aBouge) return false;

        int minY = Math.min(this.position.getY(), roi.getPosition().getY());
        int maxY = Math.max(this.position.getY(), roi.getPosition().getY());
        for (int y = minY + 1; y < maxY; y++) {
            if (plateau.getCase(this.position.getX(), y).estOccupee()) {
                return false;
            }
        }

        return true;
    }
}
