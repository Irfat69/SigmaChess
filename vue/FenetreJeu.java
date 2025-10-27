package vue;

import controleur.ControleurJeu;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import modele.jeu.Coup;
import modele.jeu.Piece;
import modele.plateau.Case;

public class FenetreJeu extends JFrame {
    public enum Theme {
        CLASSIQUE(new Color(234, 216, 176), new Color(181, 136, 99)),
        SOMBRE(new Color(60, 60, 60), new Color(30, 30, 30));

        public final Color claire;
        public final Color foncee;

        Theme(Color claire, Color foncee) {
            this.claire = claire;
            this.foncee = foncee;
        }
    }

    private static final int TAILLE_CASE = 100;
    private static final int NB_CASES = 8;
    private static final int TAILLE_FENETRE = TAILLE_CASE * NB_CASES;

    private Color CASE_CLAIRE;
    private Color CASE_FONCEE;
    private final Color SURBRILLANCE = new Color(0, 255, 0, 90);
    private final Color SELECTION = new Color(255, 215, 0, 140);

    private ControleurJeu controleur;
    private Case caseSelectionnee;
    private List<Case> casesPossibles;

    private float alphaEchec = 0;
    private Timer fadeTimer;

    private ImageIcon icoRoiBlanc, icoRoiNoir, icoReineBlanc, icoReineNoir;
    private ImageIcon icoFouBlanc, icoFouNoir, icoCavalierBlanc, icoCavalierNoir;
    private ImageIcon icoTourBlanc, icoTourNoir, icoPionBlanc, icoPionNoir;

    private Theme themeActuel;
    private JTextArea zoneHistorique;

    public FenetreJeu(ControleurJeu controleur, Theme theme) {
        this.controleur = controleur;
        this.themeActuel = theme;
        this.CASE_CLAIRE = theme.claire;
        this.CASE_FONCEE = theme.foncee;

        setTitle("Sigma Chess - Partie");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        chargerLesIcones();

        JPanel panelPlateau = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dessinerPlateau(g);
                dessinerDeplacementsPossibles(g);
                dessinerPieces(g);
                dessinerEchec(g);
            }
        };
        panelPlateau.setBackground(Color.DARK_GRAY);
        panelPlateau.setPreferredSize(new Dimension(TAILLE_FENETRE, TAILLE_FENETRE));
        panelPlateau.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getY() / TAILLE_CASE;
                int y = e.getX() / TAILLE_CASE;
                gererClic(x, y);
                repaint();
            }
        });

        // Historique
        zoneHistorique = new JTextArea();
        zoneHistorique.setEditable(false);
        zoneHistorique.setFont(new Font("Consolas", Font.PLAIN, 16));
        zoneHistorique.setBackground(new Color(245, 245, 245));
        zoneHistorique.setForeground(Color.BLACK);
        JScrollPane scroll = new JScrollPane(zoneHistorique);
        scroll.setPreferredSize(new Dimension(250, TAILLE_FENETRE));

        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelPlateau, scroll);
        splitPane.setDividerLocation(TAILLE_FENETRE);
        splitPane.setDividerSize(3);
        splitPane.setEnabled(false);

        setContentPane(splitPane);
        pack();
        setVisible(true);
    }

    private void mettreAJourHistorique() {
        List<Coup> coups = controleur.getJeu().getHistorique().getListeCoups();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < coups.size(); i++) {
            Coup c = coups.get(i);
            sb.append((i + 1)).append(". ").append(c.getNotation()).append("\n");
        }

        zoneHistorique.setText(sb.toString());
        zoneHistorique.setCaretPosition(zoneHistorique.getDocument().getLength());
    }

    private void dessinerPlateau(Graphics g) {
        for (int i = 0; i < NB_CASES; i++) {
            for (int j = 0; j < NB_CASES; j++) {
                boolean claire = (i + j) % 2 == 0;
                g.setColor(claire ? CASE_CLAIRE : CASE_FONCEE);
                g.fillRect(j * TAILLE_CASE, i * TAILLE_CASE, TAILLE_CASE, TAILLE_CASE);

                if (caseSelectionnee != null && caseSelectionnee.getX() == i && caseSelectionnee.getY() == j) {
                    g.setColor(SELECTION);
                    g.fillRect(j * TAILLE_CASE, i * TAILLE_CASE, TAILLE_CASE, TAILLE_CASE);
                }
            }
        }
    }

    private void dessinerDeplacementsPossibles(Graphics g) {
        if (casesPossibles != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(SURBRILLANCE);
            for (Case c : casesPossibles) {
                g2d.fillRect(c.getY() * TAILLE_CASE, c.getX() * TAILLE_CASE, TAILLE_CASE, TAILLE_CASE);
            }
        }
    }

    private void dessinerPieces(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i < NB_CASES; i++) {
            for (int j = 0; j < NB_CASES; j++) {
                Case c = controleur.getJeu().getPlateau().getCase(i, j);
                if (c.estOccupee()) {
                    Piece p = c.getPiece();
                    ImageIcon icon = getIcone(p);
                    if (icon != null) {
                        int px = j * TAILLE_CASE;
                        int py = i * TAILLE_CASE;

                        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
                        g2d.drawImage(icon.getImage(), px + 4, py + 4, TAILLE_CASE, TAILLE_CASE, this);
                        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                        g2d.drawImage(icon.getImage(), px, py, TAILLE_CASE, TAILLE_CASE, this);
                    }
                }
            }
        }
    }

    private void dessinerEchec(Graphics g) {
        if (alphaEchec > 0f) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaEchec));
            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 48));
            FontMetrics fm = g2d.getFontMetrics();
            String texte = "Échec !";
            int x = (TAILLE_FENETRE - fm.stringWidth(texte)) / 2;
            int y = TAILLE_FENETRE / 2;
            g2d.drawString(texte, x, y);
        }
    }

    private void lancerFadeOut() {
        alphaEchec = 1.0f;
        if (fadeTimer != null && fadeTimer.isRunning()) fadeTimer.stop();

        fadeTimer = new Timer(60, e -> {
            alphaEchec -= 0.05f;
            if (alphaEchec <= 0) {
                alphaEchec = 0;
                fadeTimer.stop();
            }
            repaint();
        });
        fadeTimer.start();
    }

    private void gererClic(int x, int y) {
        if (controleur.getJeu().estPartieTerminee()) return;

        Case cliquee = controleur.getJeu().getPlateau().getCase(x, y);

        if (caseSelectionnee == null) {
            if (cliquee != null && cliquee.estOccupee() && cliquee.getPiece().getCouleur().equals(controleur.getJoueurCourant())) {
                caseSelectionnee = cliquee;
                casesPossibles = cliquee.getPiece().getDeplacementsPossibles(controleur.getJeu().getPlateau());
            }
        } else {
            if (casesPossibles != null && casesPossibles.contains(cliquee)) {
                boolean coupValide = controleur.jouerTour(caseSelectionnee, cliquee);
                repaint();
                mettreAJourHistorique();

                if (!coupValide) {
                    lancerFadeOut();
                }

                if (controleur.getJeu().estPartieTerminee()) {
                    afficherFinDePartie();
                } else if (coupValide && controleur.estContreIA()) {
                    controleur.jouerIATour();
                    repaint();
                    mettreAJourHistorique();
                    if (controleur.getJeu().estPartieTerminee()) {
                        afficherFinDePartie();
                    }
                }
            }
            caseSelectionnee = null;
            casesPossibles = null;
        }
    }

    private void afficherFinDePartie() {
        String gagnant = controleur.getJeu().getGagnant();
        String message = (gagnant == null) ? "Partie terminée." :
                         (gagnant.contains("Blancs") || gagnant.contains("Noirs")) ? "Victoire des " + gagnant + " !" :
                         gagnant + " !";

        int choix = JOptionPane.showOptionDialog(this, message, "Fin de partie", JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Rejouer", "Menu principal"}, "Menu principal");

        if (choix == 0) {
            dispose();
            new FenetreJeu(new ControleurJeu(controleur.estContreIA()), themeActuel).setVisible(true);
        } else {
            dispose();
            new MenuPrincipal().setVisible(true);
        }
    }

    private void chargerLesIcones() {
        icoRoiBlanc = chargerIcone("Images/wK.png");
        icoRoiNoir = chargerIcone("Images/bK.png");
        icoReineBlanc = chargerIcone("Images/wQ.png");
        icoReineNoir = chargerIcone("Images/bQ.png");
        icoFouBlanc = chargerIcone("Images/wB.png");
        icoFouNoir = chargerIcone("Images/bB.png");
        icoCavalierBlanc = chargerIcone("Images/wN.png");
        icoCavalierNoir = chargerIcone("Images/bN.png");
        icoTourBlanc = chargerIcone("Images/wR.png");
        icoTourNoir = chargerIcone("Images/bR.png");
        icoPionBlanc = chargerIcone("Images/wP.png");
        icoPionNoir = chargerIcone("Images/bP.png");
    }

    private ImageIcon chargerIcone(String chemin) {
        return new ImageIcon(chemin);
    }

    private ImageIcon getIcone(Piece piece) {
        String nom = piece.getNom().toLowerCase();
        String couleur = piece.getCouleur();
        return switch (nom) {
            case "roi" -> couleur.equals("blanc") ? icoRoiBlanc : icoRoiNoir;
            case "reine" -> couleur.equals("blanc") ? icoReineBlanc : icoReineNoir;
            case "fou" -> couleur.equals("blanc") ? icoFouBlanc : icoFouNoir;
            case "cavalier" -> couleur.equals("blanc") ? icoCavalierBlanc : icoCavalierNoir;
            case "tour" -> couleur.equals("blanc") ? icoTourBlanc : icoTourNoir;
            case "pion" -> couleur.equals("blanc") ? icoPionBlanc : icoPionNoir;
            default -> null;
        };
    }
}
