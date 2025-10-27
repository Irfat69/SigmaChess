package modele.plateau;

import modele.jeu.*;

public class Plateau {
    private Case[][] cases;
    private Jeu jeu;

    public Plateau(Jeu jeu) {
        this.jeu = jeu;
        cases = new Case[8][8];
        initialiser();
    }

    public Case getCase(int x, int y) {
        if (x >= 0 && x < 8 && y >= 0 && y < 8) {
            return cases[x][y];
        }
        return null;
    }

    public Jeu getJeu() {
        return jeu;
    }

    private void initialiser() {
        // Initialiser toutes les cases vides
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                cases[i][j] = new Case(i, j);
            }
        }

        // Pièces noires
        cases[0][0].setPiece(new Tour("noir", cases[0][0]));
        cases[0][1].setPiece(new Cavalier("noir", cases[0][1]));
        cases[0][2].setPiece(new Fou("noir", cases[0][2]));
        cases[0][3].setPiece(new Reine("noir", cases[0][3]));
        cases[0][4].setPiece(new Roi("noir", cases[0][4]));
        cases[0][5].setPiece(new Fou("noir", cases[0][5]));
        cases[0][6].setPiece(new Cavalier("noir", cases[0][6]));
        cases[0][7].setPiece(new Tour("noir", cases[0][7]));

        for (int j = 0; j < 8; j++) {
            cases[1][j].setPiece(new Pion("noir", cases[1][j]));
        }

        // Pièces blanches
        cases[7][0].setPiece(new Tour("blanc", cases[7][0]));
        cases[7][1].setPiece(new Cavalier("blanc", cases[7][1]));
        cases[7][2].setPiece(new Fou("blanc", cases[7][2]));
        cases[7][3].setPiece(new Reine("blanc", cases[7][3]));
        cases[7][4].setPiece(new Roi("blanc", cases[7][4]));
        cases[7][5].setPiece(new Fou("blanc", cases[7][5]));
        cases[7][6].setPiece(new Cavalier("blanc", cases[7][6]));
        cases[7][7].setPiece(new Tour("blanc", cases[7][7]));

        for (int j = 0; j < 8; j++) {
            cases[6][j].setPiece(new Pion("blanc", cases[6][j]));
        }
    }

    public void deplacerPiece(Case source, Case destination) {
        if (source != null && destination != null && source.estOccupee()) {
            Piece piece = source.getPiece();
            destination.setPiece(piece);
            piece.setPosition(destination);
            source.setPiece(null);
            // Ne pas modifier aBouge ici : c’est géré dans Coup.executer()
        }
    }
}
