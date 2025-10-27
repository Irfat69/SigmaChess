package modele.jeu;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import modele.plateau.Case;
import modele.plateau.Plateau;

public class IAJoueur extends Joueur {
    private Random random;

    public IAJoueur(String couleur) {
        super(couleur);
        random = new Random();
    }

    @Override
    public void jouer(Jeu jeu, Plateau plateau) {
        List<CoupPossible> coupsDisponibles = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Case c = plateau.getCase(i, j);
                if (c.estOccupee()) {
                    Piece p = c.getPiece();
                    if (p.getCouleur().equals(couleur)) {
                        List<Case> mouvements = p.getDeplacementsPossibles(plateau);
                        for (Case destination : mouvements) {
                            // Avant d'accepter ce coup, on vérifie s'il nous met en échec
                            Coup coupTest = new Coup(c, destination);
                            coupTest.executer(plateau);
                            boolean enEchec = jeu.estEnEchec(couleur);
                            coupTest.annuler(plateau);

                            if (!enEchec) {
                                coupsDisponibles.add(new CoupPossible(p, destination));
                            }
                        }
                    }
                }
            }
        }

        if (coupsDisponibles.isEmpty()) {
            return;
        }

        // Priorité : essayer de capturer une pièce adverse
        List<CoupPossible> coupsAvecCapture = new ArrayList<>();
        for (CoupPossible coup : coupsDisponibles) {
            if (coup.destination.estOccupee() && !coup.destination.getPiece().getCouleur().equals(couleur)) {
                coupsAvecCapture.add(coup);
            }
        }

        CoupPossible choix;
        if (!coupsAvecCapture.isEmpty()) {
            choix = coupsAvecCapture.get(random.nextInt(coupsAvecCapture.size()));
        } else {
            choix = coupsDisponibles.get(random.nextInt(coupsDisponibles.size()));
        }

        jeu.jouerCoup(choix.source.getPosition(), choix.destination);
    }

    private static class CoupPossible {
        Piece source;
        Case destination;

        public CoupPossible(Piece source, Case destination) {
            this.source = source;
            this.destination = destination;
        }
    }
}
