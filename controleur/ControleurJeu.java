package controleur;

import modele.jeu.IAJoueur;
import modele.jeu.Jeu;
import modele.jeu.Joueur;
import modele.plateau.Case;


public class ControleurJeu {
    private Jeu jeu;
    private Joueur joueurBlanc;
    private Joueur joueurNoir;
    private boolean contreIA;

    public ControleurJeu(boolean contreIA) {
        this.contreIA = contreIA;
        this.jeu = new Jeu();

        if (contreIA) {
            joueurBlanc = null; // Joueur humain en blanc
            joueurNoir = new IAJoueur("noir");
        } else {
            joueurBlanc = null; // 2 joueurs humains (local)
            joueurNoir = null;
        }
    }

    public Jeu getJeu() {
        return jeu;
    }

    
    public String getJoueurCourant() {
        return jeu.getJoueurCourant();
    }

    public boolean estContreIA() {
        return contreIA;
    }

    public boolean jouerTour(Case source, Case destination) {
        boolean coupValide = jeu.jouerCoup(source, destination);

        if (coupValide && contreIA && !jeu.estPartieTerminee()) {
            jouerIATour();
        }

        return coupValide;
    }

    public void jouerIATour() {
        if (joueurNoir != null && jeu.getJoueurCourant().equals("noir") && !jeu.estPartieTerminee()) {
            joueurNoir.jouer(jeu, jeu.getPlateau());
        }
    }

}
