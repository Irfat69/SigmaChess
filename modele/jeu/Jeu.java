package modele.jeu;

import java.util.ArrayList;
import java.util.List;
import modele.plateau.Case;
import modele.plateau.Plateau;

public class Jeu {
    private Plateau plateau;
    private String joueurCourant; // "blanc" 
    private boolean partieTerminee;
    private Historique historique;

    public Jeu() {
        this.plateau = new Plateau(this);
        this.joueurCourant = "blanc";
        this.partieTerminee = false;
        this.historique = new Historique();
    }

    public Plateau getPlateau() {
        return plateau;
    }

    public String getJoueurCourant() {
        return joueurCourant;
    }

    public boolean estPartieTerminee() {
        return partieTerminee;
    }

    public Historique getHistorique() {
        return historique;
    }

    public boolean estEnEchecDuJoueurCourant() {
        return estEnEchec(joueurCourant);
    }
    

    public boolean jouerCoup(Case source, Case destination) {
        if (partieTerminee || source == null || destination == null || !source.estOccupee()) {
            return false;
        }

        Piece piece = source.getPiece();
        if (!piece.getCouleur().equals(joueurCourant)) {
            return false;
        }

        List<Case> deplacementsPossibles = piece.getDeplacementsPossibles(plateau);
        if (!deplacementsPossibles.contains(destination)) {
            return false;
        }

        Coup coup = new Coup(source, destination);
        coup.executer(plateau);

        // Vérifie si le roi est en échec après le coup
        if (estEnEchec(joueurCourant)) {
            coup.annuler(plateau);
            return false;
        }

        // Ajout à l’historique
        historique.ajouterCoup(coup);

        // Gère la promotion immédiatement après un coup valide
        gererPromotion(destination);

        changerTour();
        verifierFinDePartie();

        return true;
    }

    private void changerTour() {
        joueurCourant = joueurCourant.equals("blanc") ? "noir" : "blanc";
    }

    private void verifierFinDePartie() {
        if (!peutJouer(joueurCourant)) {
            partieTerminee = true;
        } else if (estMaterielInsuffisant()) {
            partieTerminee = true;
        }
    }

    public String getGagnant() {
        if (!partieTerminee) return null;

        if (estMaterielInsuffisant()) {
            return "Match nul par matériel insuffisant";
        }

        if (!peutJouer(joueurCourant)) {
            if (estEnEchec(joueurCourant)) {
                return joueurCourant.equals("blanc") ? "Noirs" : "Blancs";
            } else {
                return "Match nul par pat";
            }
        }

        return null;
    }

    private void gererPromotion(Case destination) {
        Piece piece = destination.getPiece();
        if (piece instanceof Pion) {
            int x = destination.getX();
            if ((piece.getCouleur().equals("blanc") && x == 0) ||
                (piece.getCouleur().equals("noir") && x == 7)) {
                destination.setPiece(new Reine(piece.getCouleur(), destination));
            }
        }
    }

    public boolean estEnEchec(String couleur) {
        Case roi = trouverRoi(couleur);
        if (roi == null) return false;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Case c = plateau.getCase(i, j);
                if (c.estOccupee() && !c.getPiece().getCouleur().equals(couleur)) {
                    List<Case> deplacements;
                    Piece piece = c.getPiece();

                    if (piece instanceof Roi) {
                        deplacements = ((Roi) piece).getDeplacementsPossibles(plateau, false);
                    } else {
                        deplacements = piece.getDeplacementsPossibles(plateau);
                    }

                    if (deplacements.contains(roi)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Case trouverRoi(String couleur) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Case c = plateau.getCase(i, j);
                if (c.estOccupee() && c.getPiece() instanceof Roi &&
                    c.getPiece().getCouleur().equals(couleur)) {
                    return c;
                }
            }
        }
        return null;
    }

    private boolean peutJouer(String couleur) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Case c = plateau.getCase(i, j);
                if (c.estOccupee() && c.getPiece().getCouleur().equals(couleur)) {
                    List<Case> moves;
                    Piece piece = c.getPiece();

                    if (piece instanceof Roi) {
                        moves = ((Roi) piece).getDeplacementsPossibles(plateau, false);
                    } else {
                        moves = piece.getDeplacementsPossibles(plateau);
                    }

                    for (Case dest : moves) {
                        Coup test = new Coup(c, dest);
                        test.executer(plateau);
                        boolean enEchec = estEnEchec(couleur);
                        test.annuler(plateau);
                        if (!enEchec) return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean estMaterielInsuffisant() {
        List<Piece> pieces = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Case c = plateau.getCase(i, j);
                if (c.estOccupee()) {
                    pieces.add(c.getPiece());
                }
            }
        }

        if (pieces.size() <= 2) return true;

        if (pieces.size() == 3) {
            for (Piece p : pieces) {
                if (!(p instanceof Roi)) {
                    if (!(p instanceof Fou) && !(p instanceof Cavalier)) {
                        return false;
                    }
                }
            }
            return true;
        }

        if (pieces.size() == 4) {
            List<Fou> fous = new ArrayList<>();
            for (Piece p : pieces) {
                if (p instanceof Fou) {
                    fous.add((Fou) p);
                } else if (!(p instanceof Roi)) {
                    return false;
                }
            }

            if (fous.size() == 2) {
                return fous.get(0).getPosition().estCaseBlanche() == fous.get(1).getPosition().estCaseBlanche();
            }
        }

        return false;
    }
}
