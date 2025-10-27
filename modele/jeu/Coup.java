package modele.jeu;

import modele.plateau.Case;
import modele.plateau.Plateau;

public class Coup {
    private Case source;
    private Case destination;
    private Piece pieceCapturee;
    private boolean priseEnPassant = false;
    private Case caseCapturee = null;
    
    private boolean estRoque = false;
    private Case sourceTourRoque = null;
    private Case destinationTourRoque = null;

    public Coup(Case source, Case destination) {
        this.source = source;
        this.destination = destination;
        this.pieceCapturee = destination.estOccupee() ? destination.getPiece() : null;
    }

    public void executer(Plateau plateau) {
        if (source == null || destination == null || !source.estOccupee()) return;

        Piece piece = source.getPiece();

        if (piece instanceof Pion && pieceCapturee == null) {
            int xDiff = Math.abs(destination.getX() - source.getX());
            int yDiff = Math.abs(destination.getY() - source.getY());

            if (xDiff == 1 && yDiff == 1) {
                int direction = piece.getCouleur().equals("blanc") ? 1 : -1;
                caseCapturee = plateau.getCase(destination.getX() + direction, destination.getY());
                if (caseCapturee != null && caseCapturee.estOccupee()
                        && caseCapturee.getPiece() instanceof Pion
                        && !caseCapturee.getPiece().getCouleur().equals(piece.getCouleur())) {
                    pieceCapturee = caseCapturee.getPiece();
                    priseEnPassant = true;
                    caseCapturee.setPiece(null);
                }
            }
        }

        if (piece instanceof Roi && Math.abs(destination.getY() - source.getY()) == 2) {
            int ligne = source.getX();

            if (destination.getY() == 6) {
                sourceTourRoque = plateau.getCase(ligne, 7);
                destinationTourRoque = plateau.getCase(ligne, 5);
            } else if (destination.getY() == 2) {
                sourceTourRoque = plateau.getCase(ligne, 0);
                destinationTourRoque = plateau.getCase(ligne, 3);
            }

            if (sourceTourRoque != null && sourceTourRoque.estOccupee()) {
                Piece tour = sourceTourRoque.getPiece();
                plateau.deplacerPiece(sourceTourRoque, destinationTourRoque);
                tour.setABouge(true);
                estRoque = true;
            }
        }

        plateau.deplacerPiece(source, destination);
        piece.setABouge(true);
    }

    public void annuler(Plateau plateau) {
        if (source == null || destination == null) return;

        Piece piece = destination.getPiece();

        if (piece != null) {
            source.setPiece(piece);
            piece.setPosition(source);
            piece.setABouge(false);
        } else {
            source.setPiece(null);
        }

        if (priseEnPassant && caseCapturee != null) {
            caseCapturee.setPiece(pieceCapturee);
            pieceCapturee.setPosition(caseCapturee);
            destination.setPiece(null);
        } else if (pieceCapturee != null) {
            destination.setPiece(pieceCapturee);
            pieceCapturee.setPosition(destination);
        } else {
            destination.setPiece(null);
        }

        if (estRoque && sourceTourRoque != null && destinationTourRoque != null) {
            Piece tour = destinationTourRoque.getPiece();
            if (tour != null) {
                sourceTourRoque.setPiece(tour);
                tour.setPosition(sourceTourRoque);
                tour.setABouge(false);
                destinationTourRoque.setPiece(null);
            }
        }
    }

    public Case getSource() {
        return source;
    }

    public Case getDestination() {
        return destination;
    }

    public Piece getPieceCapturee() {
        return pieceCapturee;
    }

    public boolean estPriseEnPassant() {
        return priseEnPassant;
    }

    public Case getCaseCapturee() {
        return caseCapturee;
    }

    public boolean estRoque() {
        return estRoque;
    }

    public Case getSourceTourRoque() {
        return sourceTourRoque;
    }

    public Case getDestinationTourRoque() {
        return destinationTourRoque;
    }

    // ✅ Pour affichage dans l’historique : ex. "e2 → e4"
    public String getNotation() {
        return source.getNomCase() + " → " + destination.getNomCase();
    }
}
