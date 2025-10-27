package modele.jeu;

import java.util.ArrayList;
import java.util.List;
import modele.plateau.Case;
import modele.plateau.Direction;
import modele.plateau.Plateau;

public class Roi extends Piece {

    public Roi(String couleur, Case position) {
        super(couleur, position);
    }

    @Override
    public List<Case> getDeplacementsPossibles(Plateau plateau) {
        return getDeplacementsPossibles(plateau, true);
    }

    public List<Case> getDeplacementsPossibles(Plateau plateau, boolean inclureRoques) {
        List<Case> deplacements = new ArrayList<>();

        if (position == null) return deplacements;

        // Mouvements standards (1 case autour)
        Direction[] directions = {
            Direction.HAUT, Direction.BAS, Direction.GAUCHE, Direction.DROITE,
            Direction.HAUT_GAUCHE, Direction.HAUT_DROITE, Direction.BAS_GAUCHE, Direction.BAS_DROITE
        };

        for (Direction dir : directions) {
            int dx = dir.getDeltaX();
            int dy = dir.getDeltaY();
            Case cible = plateau.getCase(position.getX() + dx, position.getY() + dy);
            if (cible != null) {
                if (!cible.estOccupee() || !cible.getPiece().getCouleur().equals(this.couleur)) {
                    deplacements.add(cible);
                }
            }
        }

        if (inclureRoques) {
            ajouterRoques(plateau, deplacements);
        }

        return deplacements;
    }

    private void ajouterRoques(Plateau plateau, List<Case> deplacements) {
        if (this.aBouge) return;
        if (plateau.getJeu().estEnEchec(this.couleur)) return;

        int ligne = this.position.getX();

        if (peutPetitRoque(plateau)) {
            Case casePetitRoque = plateau.getCase(ligne, 6);
            if (casePetitRoque != null) {
                deplacements.add(casePetitRoque);
            }
        }

        if (peutGrandRoque(plateau)) {
            Case caseGrandRoque = plateau.getCase(ligne, 2);
            if (caseGrandRoque != null) {
                deplacements.add(caseGrandRoque);
            }
        }
    }

    private boolean peutPetitRoque(Plateau plateau) {
        int ligne = this.position.getX();
        Case tourCase = plateau.getCase(ligne, 7);
        if (tourCase == null || !tourCase.estOccupee()) return false;
        Piece piece = tourCase.getPiece();
        if (!(piece instanceof Tour) || !piece.getCouleur().equals(this.couleur) || piece.aBouge()) return false;

        for (int y = 5; y < 7; y++) {
            if (plateau.getCase(ligne, y).estOccupee()) return false;
        }

        for (int y = 4; y <= 6; y++) {
            Coup simulation = new Coup(this.position, plateau.getCase(ligne, y));
            simulation.executer(plateau);
            boolean enEchec = plateau.getJeu().estEnEchec(this.couleur);
            simulation.annuler(plateau);
            if (enEchec) return false;
        }

        return true;
    }

    private boolean peutGrandRoque(Plateau plateau) {
        int ligne = this.position.getX();
        Case tourCase = plateau.getCase(ligne, 0);
        if (tourCase == null || !tourCase.estOccupee()) return false;
        Piece piece = tourCase.getPiece();
        if (!(piece instanceof Tour) || !piece.getCouleur().equals(this.couleur) || piece.aBouge()) return false;

        for (int y = 1; y < 4; y++) {
            if (plateau.getCase(ligne, y).estOccupee()) return false;
        }

        
        for (int y = 2; y <= 3; y++) {
            Coup simulation = new Coup(this.position, plateau.getCase(ligne, y));
            simulation.executer(plateau);
            boolean enEchec = plateau.getJeu().estEnEchec(this.couleur);
            simulation.annuler(plateau);
            if (enEchec) return false;
        }

        return true;
    }

    @Override
    public String getNom() {
        return "Roi";
    }

    @Override
    public Piece copie() {
        Roi clone = new Roi(this.couleur, this.position);
        clone.setABouge(this.aBouge());
        return clone;
    }
}
