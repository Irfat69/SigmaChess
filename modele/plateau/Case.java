package modele.plateau;

import modele.jeu.Piece;

public class Case {
    private int x;
    private int y;
    private Piece piece;

    public Case(int x, int y) {
        this.x = x;
        this.y = y;
        this.piece = null;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public boolean estOccupee() {
        return piece != null;
    }

    public boolean estOccupeeParCouleur(String couleur) {
        return piece != null && piece.getCouleur().equals(couleur);
    }

    public boolean estCaseBlanche() {
        return (x + y) % 2 == 0;
    }

    // ✅ Notation échiquéenne : ex. "e4"
    public String getNomCase() {
        char colonne = (char) ('a' + y);      // y = colonne
        int ligne = 8 - x;                    // x = ligne inversée
        return "" + colonne + ligne;
    }
}
