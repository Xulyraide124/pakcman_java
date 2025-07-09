// Fantome.java
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random; // Pour le mouvement aléatoire

public class Fantome {
    private int x, y; // Position en coordonnées de la grille (lignes/colonnes)
    private int dx, dy; // Direction de déplacement (-1, 0, 1)
    private int tailleCase; // Taille d'une case du labyrinthe en pixels
    private Color couleurOriginale; // Pour stocker la couleur normale du fantôme (rouge, rose, etc.)
    private Color couleurActuelle; // Couleur affichée (sera bleue si chassé, sinon couleurOriginale)

    private Random random = new Random(); // Générateur de nombres aléatoires pour le mouvement

    // Variables d'état spécifiques au fantôme
    private boolean estChasse; // Vrai si Pac-Man a mangé un super point et que le fantôme est vulnérable
    private int maisonX, maisonY; // Position de la "maison" du fantôme (où il retourne s'il est mangé)

    // Constructeur - mis à jour pour prendre la couleur originale et la position de la maison
    public Fantome(int startX, int startY, int tailleCase, Color couleur, int maisonX, int maisonY) {
        this.x = startX;
        this.y = startY;
        this.tailleCase = tailleCase;
        this.couleurOriginale = couleur;
        this.couleurActuelle = couleur; // Au début, la couleur actuelle est la couleur originale
        this.maisonX = maisonX;
        this.maisonY = maisonY;
        this.dx = 0; // Pas de mouvement initial
        this.dy = 0; // Pas de mouvement initial
        this.estChasse = false; // Par défaut, le fantôme n'est pas chassé
        choisirNouvelleDirection(); // Choisit une direction initiale aléatoire
    }

    // Méthode pour dessiner le fantôme
    public void dessiner(Graphics2D g2d) {
        int pixelX = x * tailleCase;
        int pixelY = y * tailleCase;

        // Dessine le corps du fantôme
        g2d.setColor(couleurActuelle); // Utilise la couleur actuelle (originale ou bleue)
        g2d.fillRoundRect(pixelX + 2, pixelY + 2, tailleCase - 4, tailleCase - 4, 10, 10);
        
        // Dessine la "jupe" du fantôme (parties inférieures)
        int[] xPoints = {pixelX + 2, pixelX + tailleCase / 3, pixelX + tailleCase / 2, pixelX + 2 * tailleCase / 3, pixelX + tailleCase - 2};
        int[] yPoints = {pixelY + tailleCase - 4, pixelY + tailleCase + 2, pixelY + tailleCase - 4, pixelY + tailleCase + 2, pixelY + tailleCase - 4};
        g2d.fillPolygon(xPoints, yPoints, 5);

        // Dessine les yeux des fantômes
        if (estChasse) {
            // Yeux blancs pour les fantômes chassés (bleus)
            g2d.setColor(Color.WHITE);
            g2d.fillOval(pixelX + (tailleCase / 4), pixelY + (tailleCase / 4), tailleCase / 4, tailleCase / 4); // Oeil gauche
            g2d.fillOval(pixelX + (3 * tailleCase / 8) + (tailleCase / 4), pixelY + (tailleCase / 4), tailleCase / 4, tailleCase / 4); // Oeil droit
        } else {
            // Yeux et pupilles normales pour les fantômes non chassés
            g2d.setColor(Color.WHITE);
            g2d.fillOval(pixelX + (tailleCase / 4), pixelY + (tailleCase / 4), tailleCase / 4, tailleCase / 4); // Oeil gauche
            g2d.fillOval(pixelX + (3 * tailleCase / 8) + (tailleCase / 4), pixelY + (tailleCase / 4), tailleCase / 4, tailleCase / 4); // Oeil droit
            
            g2d.setColor(Color.BLACK);
            // Correction ici : 'tailleCase / 8' au lieu de 'taille / 8'
            g2d.fillOval(pixelX + (tailleCase / 4) + (tailleCase / 16), pixelY + (tailleCase / 4) + (tailleCase / 16), tailleCase / 8, tailleCase / 8); // Pupille gauche
            g2d.fillOval(pixelX + (3 * tailleCase / 8) + (tailleCase / 4) + (tailleCase / 16), pixelY + (tailleCase / 4) + (tailleCase / 16), tailleCase / 8, tailleCase / 8); // Pupille droite
        }
    }

    // Méthode pour déplacer le fantôme, prend en compte l'état "chassé" et la position de Pac-Man
    public void deplacer(char[][] labyrinthe, int pacManX, int pacManY) {
        if (estChasse) {
            // Comportement du fantôme chassé: mouvement aléatoire pour l'instant (fuit Pac-Man idéalement)
            deplacerAleatoirement(labyrinthe);
        } else {
            // Comportement normal: tente de poursuivre Pac-Man
            deplacerAvecIA(labyrinthe, pacManX, pacManY);
        }
    }

    // Comportement de mouvement aléatoire (utilisé en mode chassé ou si l'IA est bloquée)
    private void deplacerAleatoirement(char[][] labyrinthe) {
        int nextX = x + dx;
        int nextY = y + dy;

        // Vérifie si la prochaine position est valide (pas un mur et dans les limites)
        if (nextX >= 0 && nextX < labyrinthe[0].length &&
            nextY >= 0 && nextY < labyrinthe.length &&
            labyrinthe[nextY][nextX] != 'W') {
            
            // Si valide, le fantôme se déplace
            x = nextX;
            y = nextY;
        } else {
            // Si bloqué par un mur, choisir une nouvelle direction aléatoire
            choisirNouvelleDirection();
        }
    }

    // Comportement de poursuite simple (IA basique)
    // Tente de se rapprocher de la cible (Pac-Man)
    private void deplacerAvecIA(char[][] labyrinthe, int targetX, int targetY) {
        // Logique simpliste : donne la priorité au déplacement sur l'axe où l'écart est le plus grand.
        // Cela ne gère pas les impasses complexes, mais est mieux que le mouvement purement aléatoire.

        int originalDx = dx; // Sauvegarde la direction actuelle au cas où
        int originalDy = dy;

        boolean moved = false; // Indicateur si une direction "intelligente" a été trouvée

        // Essai de déplacement horizontal si l'écart horizontal est plus grand
        if (Math.abs(targetX - x) > Math.abs(targetY - y)) { 
            if (targetX > x && peutAvancer(labyrinthe, 1, 0)) { // Tente droite
                dx = 1; dy = 0; moved = true;
            } else if (targetX < x && peutAvancer(labyrinthe, -1, 0)) { // Tente gauche
                dx = -1; dy = 0; moved = true;
            }
        } 
        
        // Si pas de mouvement ou si l'écart vertical est plus grand/prioritaire
        if (!moved) { 
            if (targetY > y && peutAvancer(labyrinthe, 0, 1)) { // Tente bas
                dx = 0; dy = 1; moved = true;
            } else if (targetY < y && peutAvancer(labyrinthe, 0, -1)) { // Tente haut
                dx = 0; dy = -1; moved = true;
            }
        }

        if (!moved) { 
            // Si aucune direction "intelligente" n'a pu être trouvée ou si elle mène à un mur,
            // ou si le fantôme est bloqué, revert au mouvement aléatoire.
            deplacerAleatoirement(labyrinthe);
            return; // Fin de la méthode, le déplacement aléatoire a été géré
        }

        // Si une direction "intelligente" a été choisie, on l'applique
        int nextX = x + dx;
        int nextY = y + dy;

        // Vérifie si la nouvelle position est valide
        if (nextX >= 0 && nextX < labyrinthe[0].length &&
            nextY >= 0 && nextY < labyrinthe.length &&
            labyrinthe[nextY][nextX] != 'W') {
            x = nextX;
            y = nextY;
        } else {
            // Si la direction intelligente mène à un mur, revert au mouvement aléatoire
            // pour ne pas rester bloqué.
            deplacerAleatoirement(labyrinthe);
        }
    }

    // Méthode utilitaire pour vérifier si le fantôme peut avancer dans une direction donnée
    private boolean peutAvancer(char[][] labyrinthe, int testDx, int testDy) {
        int nextX = x + testDx;
        int nextY = y + testDy;
        return nextX >= 0 && nextX < labyrinthe[0].length &&
               nextY >= 0 && nextY < labyrinthe.length &&
               labyrinthe[nextY][nextX] != 'W'; // Vérifie que ce n'est pas un mur
    }

    // Choisit une direction de déplacement aléatoire (utile pour l'initialisation et le mode chassé)
    // Rendue publique pour pouvoir la réinitialiser de l'extérieur si nécessaire (ex: après un respawn)
    public void choisirNouvelleDirection() {
        int direction = random.nextInt(4); // Génère un nombre entre 0 et 3
        dx = 0; // Réinitialise le mouvement horizontal
        dy = 0; // Réinitialise le mouvement vertical
        switch (direction) {
            case 0: dx = 1; break; // Droite
            case 1: dx = -1; break; // Gauche
            case 2: dy = -1; break; // Haut
            case 3: dy = 1; break; // Bas
        }
    }

    // Getters - Permettent aux autres classes de lire les propriétés privées
    public int getX() { return x; }
    public int getY() { return y; }
    public boolean estChasse() { return estChasse; } // Getter pour l'état "chassé"
    public int getMaisonX() { return maisonX; } // Getter pour la position X de la maison
    public int getMaisonY() { return maisonY; } // Getter pour la position Y de la maison

    // Setters - Permettent aux autres classes de modifier les propriétés privées de manière contrôlée
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }

    // Méthode pour changer l'état "chassé" du fantôme et sa couleur
    public void setEstChasse(boolean estChasse) {
        this.estChasse = estChasse;
        if (estChasse) {
            this.couleurActuelle = Color.BLUE; // Le fantôme devient bleu quand il est chassé
        } else {
            this.couleurActuelle = couleurOriginale; // Reviens à la couleur originale
        }
    }

    // Méthode pour renvoyer le fantôme à sa position de départ (maison)
    public void retournerMaison() {
        this.x = maisonX;
        this.y = maisonY;
        this.dx = 0; // Arrête tout mouvement
        this.dy = 0;
        this.estChasse = false; // N'est plus chassé
        this.couleurActuelle = couleurOriginale; // Reviens à la couleur normale
        choisirNouvelleDirection(); // Choisit une nouvelle direction pour ne pas rester bloqué en sortant de la maison
    }
}