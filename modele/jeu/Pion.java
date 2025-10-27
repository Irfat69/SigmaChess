package modele.jeu;

import java.util.ArrayList;
import java.util.List;
import modele.plateau.Case;
import modele.plateau.Plateau;

public class Pion extends Piece {

    public Pion(String couleur, Case position) {
        super(couleur, position);
    }

    @Override
    public List<Case> getDeplacementsPossibles(Plateau plateau) {
        List<Case> deplacements = new ArrayList<>();
        int direction = this.couleur.equals("blanc") ? -1 : 1;

        int x = position.getX();
        int y = position.getY();

        // Premier coup : deux cases en avant (si sur case de départ)
        boolean estSurCaseDepart = (this.couleur.equals("blanc") && x == 6) ||
                                   (this.couleur.equals("noir") && x == 1);

        if (estSurCaseDepart) {
            Case intermediaire = plateau.getCase(x + direction, y);
            Case deuxDevant = plateau.getCase(x + 2 * direction, y);
            if (intermediaire != null && deuxDevant != null &&
                !intermediaire.estOccupee() && !deuxDevant.estOccupee()) {
                deplacements.add(deuxDevant);
            }
        }

        // Une case en avant
        Case devant = plateau.getCase(x + direction, y);
        if (devant != null && !devant.estOccupee()) {
            deplacements.add(devant);
        }

        // Captures diagonales
        ajouterCaptureDiagonale(plateau, direction, x, y, deplacements);

        // Prise en passant
        ajouterPriseEnPassant(plateau, direction, x, y, deplacements);

        return deplacements;
    }

    private void ajouterCaptureDiagonale(Plateau plateau, int direction, int x, int y, List<Case> deplacements) {
        Case diagGauche = plateau.getCase(x + direction, y - 1);
        Case diagDroite = plateau.getCase(x + direction, y + 1);

        if (diagGauche != null && diagGauche.estOccupee() &&
            !diagGauche.getPiece().getCouleur().equals(this.couleur)) {
            deplacements.add(diagGauche);
        }

        if (diagDroite != null && diagDroite.estOccupee() &&
            !diagDroite.getPiece().getCouleur().equals(this.couleur)) {
            deplacements.add(diagDroite);
        }
    }

    private void ajouterPriseEnPassant(Plateau plateau, int direction, int x, int y, List<Case> deplacements) {
        Jeu jeu = plateau.getJeu();
        if (jeu == null || jeu.getHistorique() == null || jeu.getHistorique().estVide()) return;
    
        Coup dernierCoup = jeu.getHistorique().getDernierCoup();
        if (dernierCoup == null || dernierCoup.getSource() == null || dernierCoup.getDestination() == null) return;
    
        Piece piece = dernierCoup.getDestination().getPiece();
        if (!(piece instanceof Pion)) return;
    
        int startX = dernierCoup.getSource().getX();
        int endX = dernierCoup.getDestination().getX();
        int endY = dernierCoup.getDestination().getY();
    
        // Le pion adverse a avancé de 2 cases d’un coup
        if (Math.abs(startX - endX) == 2) {
            // Il est maintenant sur une colonne adjacente
            if (Math.abs(endY - y) == 1 && endX == x) {
                Case casePrise = plateau.getCase(x + direction, endY);
                if (casePrise != null && !casePrise.estOccupee()) {
                    deplacements.add(casePrise);
                }
            }
        }
    }
    

    @Override
    public String getNom() {
        return "Pion";
    }

    @Override
    public Piece copie() {
        Pion clone = new Pion(this.couleur, this.position);
        clone.setABouge(this.aBouge());
        return clone;
    }
}
