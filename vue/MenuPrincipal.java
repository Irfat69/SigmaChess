package vue;

import controleur.ControleurJeu;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MenuPrincipal extends JFrame {
    public boolean isSombre = false;
    public MenuPrincipal() {
        setTitle("Sigma Chess");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        GradientPanel panel = new GradientPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel titre = new JLabel("Sigma Chess");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 60));
        titre.setForeground(new Color(236, 240, 241)); // Blanc cassé
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);
        titre.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        // Ombre douce pour le titre
        titre.setUI(new javax.swing.plaf.basic.BasicLabelUI() {
            @Override
            protected void paintSafely(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 100));
                g2.drawString(titre.getText(), titre.getX() + 2, titre.getY() + 2 + titre.getFontMetrics(titre.getFont()).getAscent());
                g2.dispose();
                super.paintSafely(g);
            }
        });

        JButton boutonVsIA = createStylishButton("Jouer contre IA");
        JButton boutonLocal = createStylishButton("Jouer en local");
        JButton boutonTheme = createStylishButton("Sombre" );
        JButton boutonQuitter = createStylishButton("Quitter");

        boutonVsIA.addActionListener(e -> {
            dispose();
            if (isSombre) {
                new FenetreJeu(new ControleurJeu(true), FenetreJeu.Theme.SOMBRE).setVisible(true);
            } else {
                new FenetreJeu(new ControleurJeu(true), FenetreJeu.Theme.CLASSIQUE).setVisible(true);
            }
        });

        boutonLocal.addActionListener(e -> {
            dispose();
            if (isSombre) {
                new FenetreJeu(new ControleurJeu(false), FenetreJeu.Theme.SOMBRE).setVisible(true);
            } else {
                new FenetreJeu(new ControleurJeu(false), FenetreJeu.Theme.CLASSIQUE).setVisible(true);
            }
        });

        boutonTheme.addActionListener(e -> {
            isSombre = !isSombre;
            boutonTheme.setText("Sombre".equals(boutonTheme.getText())? "Clair": "Sombre");
            if (isSombre) {
                panel.setColors(new Color(20, 20, 20), new Color(60, 60, 60)); // Sombre
            } else {
                panel.setColors(new Color(44, 62, 80), new Color(189, 195, 199)); // Clair
            }
        });

        boutonQuitter.addActionListener(e -> {
            dispose();
            System.exit(0);
        });

        panel.add(Box.createVerticalGlue());
        panel.add(titre);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(boutonVsIA);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(boutonLocal);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(boutonTheme);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(boutonQuitter);
        panel.add(Box.createVerticalGlue());

        setContentPane(panel);
    }

    private JButton createStylishButton(String texte) {
        JButton bouton = new JButton(texte);
        bouton.setAlignmentX(Component.CENTER_ALIGNMENT);
        bouton.setFont(new Font("Segoe UI", Font.BOLD, 26));
        bouton.setBackground(new Color(52, 73, 94));  // Bleu foncé mat
        bouton.setForeground(Color.WHITE);           // Texte bien lisible
        bouton.setFocusPainted(false);
        bouton.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));
        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bouton.setMaximumSize(new Dimension(320, 60));
    
        bouton.setContentAreaFilled(true);
        bouton.setOpaque(true);
        bouton.setBorderPainted(false); // ✅ Supprime le contour bleu moche par défaut
    
        bouton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                bouton.setBackground(new Color(41, 128, 185)); // Survol plus lumineux
            }
    
            @Override
            public void mouseExited(MouseEvent e) {
                bouton.setBackground(new Color(52, 73, 94));
            }
    
            @Override
            public void mousePressed(MouseEvent e) {
                bouton.setBackground(new Color(26, 82, 118)); // Appui
            }
    
            @Override
            public void mouseReleased(MouseEvent e) {
                bouton.setBackground(new Color(41, 128, 185));
            }
        });
    
        return bouton;
    }
    

    static class GradientPanel extends JPanel {
        private Color colorTop = new Color(44, 62, 80);     // Bleu nuit
        private Color colorBottom = new Color(189, 195, 199); // Gris clair moderne

        public void setColors(Color top, Color bottom) {
            this.colorTop = top;
            this.colorBottom = bottom;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            GradientPaint gp = new GradientPaint(0, 0, colorTop, 0, getHeight(), colorBottom);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
