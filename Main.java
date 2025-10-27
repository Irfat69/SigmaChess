import javax.swing.*;
import vue.MenuPrincipal;

public class Main {
    public static void main(String[] args) {
        // Titre de lancement dans la console
        System.out.println("=== Lancement de Sigma Chess ===");

        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Impossible d'appliquer le LookAndFeel : " + e.getMessage());
        }

        // Lancer l'interface dans le thread Swing
        SwingUtilities.invokeLater(() -> {
            MenuPrincipal menu = new MenuPrincipal();
            menu.setVisible(true);
        });
    }
}
