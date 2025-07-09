// PacMan.java
import java.awt.Color;
import java.awt.Graphics2D;

public class PacMan {
    private int x, y; // Position en coordonnées de la grille (lignes/colonnes)
    private int dx, dy; // Direction de déplacement (-1, 0, 1)
    private int tailleCase; // Taille d'une case du labyrinthe en pixels

    // Constantes pour les directions
    public static final int DROITE = 0;
    public static final int GAUCHE = 1;
    public static final int HAUT = 2;
    public static final int BAS = 3;

    private int directionActuelle; // La direction dans laquelle Pac-Man est orienté
    private int directionDemandee; // La direction que le joueur tente d'emprunter

    // Constructeur
    public PacMan(int startX, int startY, int tailleCase) {
        this.x = startX;
        this.y = startY;
        this.tailleCase = tailleCase;
        this.dx = 0; // Pas de mouvement initial
        this.dy = 0; // Pas de mouvement initial
        this.directionActuelle = DROITE; // Par défaut, Pac-Man regarde à droite
        this.directionDemandee = DROITE; // Par défaut
    }

    // Méthode pour dessiner Pac-Man
    public void dessiner(Graphics2D g2d) {
        // Calcule les coordonnées pixel du centre de Pac-Man
        int pixelX = x * tailleCase;
        int pixelY = y * tailleCase;

        // Dessine Pac-Man en jaune
        g2d.setColor(Color.YELLOW);
        // On dessine un cercle. Les valeurs x, y sont pour le coin supérieur gauche du carré englobant
        // On réduit un peu la taille pour avoir un petit bord noir autour
        g2d.fillOval(pixelX + 2, pixelY + 2, tailleCase - 4, tailleCase - 4);
        
        // Optionnel: Dessiner la bouche de Pac-Man
        // L'angle de départ et l'étendue de l'arc dépendent de la direction
        int startAngle = 0;
        int arcAngle = 360; // Bouche fermée pour l'instant, on animera plus tard

        switch (directionActuelle) {
            case DROITE:
                startAngle = 45; arcAngle = 270; break;
            case GAUCHE:
                startAngle = 225; arcAngle = 270; break;
            case HAUT:
                startAngle = 135; arcAngle = 270; break;
            case BAS:
                startAngle = 315; arcAngle = 270; break;
        }
        g2d.setColor(Color.BLACK); // Couleur de la bouche
        // fillArc(x, y, width, height, startAngle, arcAngle)
        // x, y, width, height sont relatifs au Pac-Man entier
        g2d.fillArc(pixelX + 2, pixelY + 2, tailleCase - 4, tailleCase - 4, startAngle, arcAngle);
    }

    // Méthode pour définir la direction de déplacement demandée par le joueur
    public void setDirection(int nouvelleDirection) {
        this.directionDemandee = nouvelleDirection;
    }

    // Méthode pour mettre à jour la position de Pac-Man
    // Sera appelée régulièrement par le PanneauJeu
    public void deplacer(char[][] labyrinthe) {
        // Tentative de changer la direction si la nouvelle direction est possible
        if (peutAvancer(labyrinthe, directionDemandee)) {
            directionActuelle = directionDemandee;
            // Met à jour dx et dy en fonction de la direction actuelle
            mettreAJourVecteurDirection();
        } else if (!peutAvancer(labyrinthe, directionActuelle)) {
            // Si la direction actuelle n'est plus possible (ex: mur devant), Pac-Man s'arrête
            dx = 0;
            dy = 0;
        }
        // Si la direction actuelle est possible, Pac-Man continue dans cette direction
        // Si la direction demandée est possible, Pac-Man change pour cette direction
        // Si aucune n'est possible, il reste immobile.

        if (dx != 0 || dy != 0) { // S'il y a un mouvement
            int nouvelleX = x + dx;
            int nouvelleY = y + dy;

            // Vérifie si la nouvelle position est un mur
            if (nouvelleX >= 0 && nouvelleX < labyrinthe[0].length &&
                nouvelleY >= 0 && nouvelleY < labyrinthe.length &&
                labyrinthe[nouvelleY][nouvelleX] != 'W') {
                
                x = nouvelleX;
                y = nouvelleY;
            } else {
                // Si collision avec un mur, arrêter le mouvement
                dx = 0;
                dy = 0;
            }
        }
    }
    
    // Met à jour les vecteurs de direction dx et dy
    private void mettreAJourVecteurDirection() {
        dx = 0;
        dy = 0;
        switch (directionActuelle) {
            case DROITE: dx = 1; break;
            case GAUCHE: dx = -1; break;
            case HAUT: dy = -1; break;
            case BAS: dy = 1; break;
        }
    }

    // Vérifie si Pac-Man peut avancer dans une direction donnée
    private boolean peutAvancer(char[][] labyrinthe, int direction) {
        int testDx = 0;
        int testDy = 0;
        switch (direction) {
            case DROITE: testDx = 1; break;
            case GAUCHE: testDx = -1; break;
            case HAUT: testDy = -1; break;
            case BAS: testDy = 1; break;
        }

        int nextX = x + testDx;
        int nextY = y + testDy;

        // Vérifie les limites du labyrinthe et les murs
        if (nextX >= 0 && nextX < labyrinthe[0].length &&
            nextY >= 0 && nextY < labyrinthe.length &&
            labyrinthe[nextY][nextX] != 'W') {
            return true;
        }
        return false;
    }

    // Getters (utiles pour d'autres classes pour connaître la position de Pac-Man)
    public int getX() { return x; }
    public int getY() { return y; }
}