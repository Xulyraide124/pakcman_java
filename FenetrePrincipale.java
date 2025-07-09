// FenetrePrincipale.java
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class FenetrePrincipale extends JFrame {

    private static final long serialVersionUID = 1L;

    public FenetrePrincipale() {
        setTitle("Pac-Man en Java");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PanneauJeu panneauJeu = new PanneauJeu();
        add(panneauJeu);

        // Au lieu de setSize(), on utilise pack() pour ajuster la fenêtre à la taille préférée du PanneauJeu
        pack();
        
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FenetrePrincipale();
        });
    }
}