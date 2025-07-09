// PanneauJeu.java - Modifiez votre fichier

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics; // Nouveau : Pour centrer le texte
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class PanneauJeu extends JPanel implements ActionListener, KeyListener {

    private static final long serialVersionUID = 1L;

    private final int TAILLE_CASE = 30;

    // Définition des états du jeu
    private enum EtatJeu {
        MENU,
        JEU_EN_COURS,
        GAME_OVER,
        VICTOIRE
    }
    private EtatJeu etatActuelDuJeu; // Variable pour stocker l'état actuel

    // Le labyrinthe (copiez-collez votre labyrinthe existant ici)
    private char[][] labyrinthe = {
        {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'},
        {'W', 'O', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', 'O', 'W'},
        {'W', '.', 'W', 'W', '.', 'W', 'W', 'W', '.', 'W', 'W', 'W', '.', 'W', 'W', 'W', '.', 'W', '.', 'W'},
        {'W', '.', 'W', 'W', '.', 'W', 'W', 'W', '.', 'W', 'W', 'W', '.', 'W', 'W', 'W', '.', 'W', '.', 'W'},
        {'W', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', 'W'},
        {'W', '.', 'W', 'W', '.', 'W', '.', 'W', 'W', 'W', 'W', 'W', 'W', 'W', '.', 'W', 'W', '.', 'W', 'W'},
        {'W', '.', 'W', 'W', '.', 'W', '.', 'W', 'W', 'W', 'W', 'W', 'W', 'W', '.', 'W', 'W', '.', 'W', 'W'},
        {'W', '.', '.', '.', '.', 'W', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', 'W'},
        {'W', 'W', 'W', 'W', '.', 'W', '.', 'W', 'W', 'W', 'W', 'W', 'W', 'W', '.', 'W', 'W', 'W', '.', 'W'},
        {'W', 'W', 'W', 'W', '.', 'W', '.', 'W', 'W', 'W', 'W', 'W', 'W', 'W', '.', 'W', 'W', 'W', '.', 'W'},
        {'W', 'O', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', 'O', 'W'},
        {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'}
    };

    private final int NB_COLONNES = labyrinthe[0].length;
    private final int NB_LIGNES = labyrinthe.length;

    private PacMan pacMan;
    private List<Fantome> fantomes;
    private Timer timer;
    private final int DELAI_JEU = 150;

    private int score;
    private int pointsRestants;
    private int vies;
    private final int VIES_INITIALES = 3;

    private final int DUREE_POWER_PELLET_TICKS = 5000 / DELAI_JEU; 
    private int powerPelletTimer;

    private final int PACMAN_START_X = 1;
    private final int PACMAN_START_Y = 1;
    private final int[][] FANTOME_START_POS = {
        {10, 5}, {10, 6}, {9, 5}, {9, 6} 
    };

    private char[][] labyrintheInitial; 

    public PanneauJeu() {
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        requestFocusInWindow();

        setPreferredSize(new java.awt.Dimension(NB_COLONNES * TAILLE_CASE, NB_LIGNES * TAILLE_CASE + 40));

        labyrintheInitial = new char[NB_LIGNES][NB_COLONNES];
        for (int i = 0; i < NB_LIGNES; i++) {
            System.arraycopy(labyrinthe[i], 0, labyrintheInitial[i], 0, NB_COLONNES);
        }

        // Initialise l'état du jeu au MENU
        etatActuelDuJeu = EtatJeu.MENU; 

        timer = new Timer(DELAI_JEU, this);
        timer.start(); // Le timer démarre immédiatement pour afficher le menu
    }

    // Méthode pour initialiser ou réinitialiser l'état du jeu pour une nouvelle partie
    private void initialiserJeu() {
        // Réinitialise le labyrinthe
        for (int i = 0; i < NB_LIGNES; i++) {
            System.arraycopy(labyrintheInitial[i], 0, labyrinthe[i], 0, NB_COLONNES);
        }

        pacMan = new PacMan(PACMAN_START_X, PACMAN_START_Y, TAILLE_CASE);

        fantomes = new ArrayList<>();
        fantomes.add(new Fantome(FANTOME_START_POS[0][0], FANTOME_START_POS[0][1], TAILLE_CASE, Color.RED, FANTOME_START_POS[0][0], FANTOME_START_POS[0][1]));
        fantomes.add(new Fantome(FANTOME_START_POS[1][0], FANTOME_START_POS[1][1], TAILLE_CASE, Color.CYAN, FANTOME_START_POS[1][0], FANTOME_START_POS[1][1]));
        fantomes.add(new Fantome(FANTOME_START_POS[2][0], FANTOME_START_POS[2][1], TAILLE_CASE, Color.PINK, FANTOME_START_POS[2][0], FANTOME_START_POS[2][1]));
        fantomes.add(new Fantome(FANTOME_START_POS[3][0], FANTOME_START_POS[3][1], TAILLE_CASE, Color.ORANGE, FANTOME_START_POS[3][0], FANTOME_START_POS[3][1]));

        score = 0;
        vies = VIES_INITIALES;
        compterPointsInitiaux();
        powerPelletTimer = 0;
        for (Fantome f : fantomes) {
            f.setEstChasse(false);
        }
        // Le timer est déjà démarré dans le constructeur, pas besoin de le redémarrer ici
    }

    private void compterPointsInitiaux() {
        pointsRestants = 0;
        for (int row = 0; row < NB_LIGNES; row++) {
            for (int col = 0; col < NB_COLONNES; col++) {
                if (labyrinthe[row][col] == '.' || labyrinthe[row][col] == 'O') {
                    pointsRestants++;
                }
            }
        }
    }

    // Nouvelle méthode pour centrer du texte
    private void dessinerTexteCentre(Graphics2D g2d, String text, int y) {
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        int x = (getWidth() - metrics.stringWidth(text)) / 2;
        g2d.drawString(text, x, y);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (etatActuelDuJeu == EtatJeu.JEU_EN_COURS) {
            // Dessine le labyrinthe
            for (int row = 0; row < NB_LIGNES; row++) {
                for (int col = 0; col < NB_COLONNES; col++) {
                    char element = labyrinthe[row][col];
                    int x = col * TAILLE_CASE;
                    int y = row * TAILLE_CASE;

                    if (element == 'W') {
                        g2d.setColor(Color.BLUE);
                        g2d.fillRect(x, y, TAILLE_CASE, TAILLE_CASE);
                    } else if (element == '.') {
                        g2d.setColor(Color.YELLOW);
                        int pointSize = TAILLE_CASE / 4;
                        int pointX = x + (TAILLE_CASE - pointSize) / 2;
                        int pointY = y + (TAILLE_CASE - pointSize) / 2;
                        g2d.fillOval(pointX, pointY, pointSize, pointSize);
                    } else if (element == 'O') {
                        g2d.setColor(Color.YELLOW);
                        int powerPointSize = TAILLE_CASE / 2;
                        int powerPointX = x + (TAILLE_CASE - powerPointSize) / 2;
                        int powerPointY = y + (TAILLE_CASE - powerPointSize) / 2;
                        g2d.fillOval(powerPointX, powerPointY, powerPointSize, powerPointSize);
                    }
                }
            }

            // Dessine Pac-Man
            pacMan.dessiner(g2d);

            // Dessine tous les fantômes
            for (Fantome fantome : fantomes) {
                fantome.dessiner(g2d);
            }

            // Affiche le score et les vies
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.drawString("Score: " + score, 10, NB_LIGNES * TAILLE_CASE + 30);
            g2d.drawString("Vies: " + vies, NB_COLONNES * TAILLE_CASE - 100, NB_LIGNES * TAILLE_CASE + 30); 

        } else if (etatActuelDuJeu == EtatJeu.MENU) {
            g2d.setColor(Color.YELLOW);
            g2d.setFont(new Font("Arial", Font.BOLD, 48));
            dessinerTexteCentre(g2d, "PAC-MAN", getHeight() / 2 - 50);

            g2d.setFont(new Font("Arial", Font.PLAIN, 24));
            dessinerTexteCentre(g2d, "Appuyez sur ESPACE pour commencer", getHeight() / 2 + 20);

        } else if (etatActuelDuJeu == EtatJeu.GAME_OVER) {
            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Arial", Font.BOLD, 48));
            dessinerTexteCentre(g2d, "GAME OVER", getHeight() / 2 - 50);

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.PLAIN, 24));
            dessinerTexteCentre(g2d, "Score final: " + score, getHeight() / 2 + 20);
            dessinerTexteCentre(g2d, "Appuyez sur ESPACE pour rejouer", getHeight() / 2 + 60);

        } else if (etatActuelDuJeu == EtatJeu.VICTOIRE) {
            g2d.setColor(Color.GREEN);
            g2d.setFont(new Font("Arial", Font.BOLD, 48));
            dessinerTexteCentre(g2d, "VICTOIRE !", getHeight() / 2 - 50);

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.PLAIN, 24));
            dessinerTexteCentre(g2d, "Score final: " + score, getHeight() / 2 + 20);
            dessinerTexteCentre(g2d, "Appuyez sur ESPACE pour rejouer", getHeight() / 2 + 60);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (etatActuelDuJeu == EtatJeu.JEU_EN_COURS) {
            pacMan.deplacer(labyrinthe);

            for (Fantome fantome : fantomes) {
                fantome.deplacer(labyrinthe, pacMan.getX(), pacMan.getY());
            }

            int pacManGridX = pacMan.getX();
            int pacManGridY = pacMan.getY();

            if (pacManGridY >= 0 && pacManGridY < NB_LIGNES &&
                pacManGridX >= 0 && pacManGridX < NB_COLONNES) {
                
                char elementSurCase = labyrinthe[pacManGridY][pacManGridX];

                if (elementSurCase == '.') {
                    score += 10;
                    labyrinthe[pacManGridY][pacManGridX] = ' ';
                    pointsRestants--;
                } else if (elementSurCase == 'O') {
                    score += 50;
                    labyrinthe[pacManGridY][pacManGridX] = ' ';
                    pointsRestants--;
                    
                    powerPelletTimer = DUREE_POWER_PELLET_TICKS;
                    for (Fantome f : fantomes) {
                        f.setEstChasse(true);
                    }
                }
            }

            if (powerPelletTimer > 0) {
                powerPelletTimer--;
                if (powerPelletTimer == 0) {
                    for (Fantome f : fantomes) {
                        f.setEstChasse(false);
                    }
                }
            }

            for (Fantome fantome : fantomes) {
                if (pacMan.getX() == fantome.getX() && pacMan.getY() == fantome.getY()) {
                    if (fantome.estChasse()) {
                        score += 200;
                        fantome.retournerMaison();
                    } else {
                        vies--;
                        System.out.println("Pac-Man a été touché ! Vies restantes : " + vies);
                        
                        if (vies <= 0) {
                            etatActuelDuJeu = EtatJeu.GAME_OVER; // Passe à l'état GAME_OVER
                            // Pas besoin d'arrêter le timer ici, il continue de déclencher repaint
                        } else {
                            // Réinitialiser la position de Pac-Man et des fantômes
                            pacMan = new PacMan(PACMAN_START_X, PACMAN_START_Y, TAILLE_CASE);
                            for(Fantome currentFantome : fantomes) {
                                currentFantome.setX(currentFantome.getMaisonX());
                                currentFantome.setY(currentFantome.getMaisonY());
                                currentFantome.choisirNouvelleDirection(); 
                                currentFantome.setEstChasse(false);
                            }
                            powerPelletTimer = 0;
                        }
                    }
                }
            }

            if (pointsRestants == 0) {
                etatActuelDuJeu = EtatJeu.VICTOIRE; // Passe à l'état VICTOIRE
                // Pas besoin d'arrêter le timer ici
            }
        }
        // Si le jeu n'est pas en cours (MENU, GAME_OVER, VICTOIRE), seul repaint est appelé
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (etatActuelDuJeu == EtatJeu.JEU_EN_COURS) {
            // Gère les déplacements de Pac-Man uniquement en mode jeu
            if (key == KeyEvent.VK_LEFT) {
                pacMan.setDirection(PacMan.GAUCHE);
            } else if (key == KeyEvent.VK_RIGHT) {
                pacMan.setDirection(PacMan.DROITE);
            } else if (key == KeyEvent.VK_UP) {
                pacMan.setDirection(PacMan.HAUT);
            } else if (key == KeyEvent.VK_DOWN) {
                pacMan.setDirection(PacMan.BAS);
            }
        } else if (key == KeyEvent.VK_SPACE) {
            // Si on est au menu, ou en game over, ou en victoire, SPACE lance une nouvelle partie
            if (etatActuelDuJeu == EtatJeu.MENU || etatActuelDuJeu == EtatJeu.GAME_OVER || etatActuelDuJeu == EtatJeu.VICTOIRE) {
                initialiserJeu(); // Réinitialise tous les paramètres du jeu
                etatActuelDuJeu = EtatJeu.JEU_EN_COURS; // Passe à l'état de jeu
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
}