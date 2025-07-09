# 👻 Pac-Man en Java Swing 🟡

Bienvenue dans ce projet simple de jeu Pac-Man développé en Java avec la bibliothèque graphique Swing. Ce projet est une implémentation des mécaniques fondamentales du jeu classique, axé sur la clarté du code et la fonctionnalité.

## ✨ Fonctionnalités Implémentées

* **Personnage Principal (Pac-Man) :** Contrôle de Pac-Man avec les touches directionnelles (flèches).
* **Labyrinthe :** Génération et affichage d'un labyrinthe composé de murs, d'espaces vides, de petits points et de super points.
* **Collecte de Points :** Pac-Man collecte les points et les super points pour augmenter son score.
* **Fantômes (Ennemis) :** Des fantômes se déplacent dans le labyrinthe avec une IA basique qui les fait poursuivre Pac-Man.
* **Gestion des Collisions :** Si Pac-Man entre en contact avec un fantôme, il perd une vie.
* **Super Points (Power Pellets) :** Des points spéciaux qui, une fois collectés, rendent les fantômes temporairement vulnérables (ils deviennent bleus). Pac-Man peut alors les manger pour des points bonus.
* **Score en Jeu :** Affichage et mise à jour du score en temps réel pendant la partie.
* **Gestion des Vies :** Pac-Man dispose d'un nombre de vies, le jeu se termine lorsqu'elles sont toutes perdues.
* **Objectif du Jeu :** Collecter tous les points du labyrinthe pour remporter la partie.
* **États du Jeu :** Écrans dédiés pour le menu principal, la partie en cours, le "Game Over" et la victoire, offrant une interface utilisateur conviviale.

## 🚀 Comment Lancer le Jeu

Pour compiler et exécuter ce jeu, vous devez avoir un kit de développement Java (JDK) installé sur votre machine.

### Prérequis

* **Java Development Kit (JDK)** (version 8 ou plus récente recommandée).

### Étapes

1.  **Clonez le dépôt (ou téléchargez les fichiers) :**
    ```bash
    git clone https://github.com/Xulyraide124/pakcman_java.git
    cd pakcman_java
    ```

2.  **Compilez les fichiers source :**
    Assurez-vous que vous êtes dans le répertoire racine du projet (où se trouvent `FenetrePrincipale.java`, `PanneauJeu.java`, etc.).
    ```bash
    javac FenetrePrincipale.java PanneauJeu.java PacMan.java Fantome.java
    ```

3.  **Exécutez le jeu :**
    ```bash
    java FenetrePrincipale
    ```

## 🎮 Instructions de Jeu

* **Démarrer une partie :** Appuyez sur la touche `ESPACE` depuis l'écran de menu, l'écran "Game Over" ou l'écran de victoire.
* **Déplacement de Pac-Man :** Utilisez les touches fléchées de votre clavier (⬆️⬇️⬅️➡️).
* **Objectif :** Mangez tous les petits points jaunes et les super points (plus gros) dans le labyrinthe pour gagner la partie.
* **Fantômes :** Évitez les fantômes ! Une collision vous fera perdre une vie.
* **Super Points :** Lorsque Pac-Man mange un super point, les fantômes deviennent bleus et vulnérables pendant un court instant. Profitez-en pour les manger et gagner des points supplémentaires ! Les fantômes mangés retourneront à leur base.
* **Fin de Partie :** Le jeu se termine lorsque toutes les vies de Pac-Man sont épuisées (Game Over) ou lorsque tous les points du labyrinthe ont été collectés (Victoire).
* **Quitter le jeu :** Fermez simplement la fenêtre du jeu.

## 🛠️ Technologies Utilisées

* **Langage de Programmation :** Java
* **Interface Graphique :** Java Swing

## 📈 Améliorations Futures Possibles

Ce projet peut être étendu avec de nombreuses fonctionnalités pour le rendre plus riche et plus fidèle au jeu original :

* **Animations :** Animer Pac-Man (bouche qui s'ouvre et se ferme) et les fantômes pour des mouvements plus fluides.
* **Effets Sonores et Musique :** Ajouter des sons pour les actions (manger un point, mourir, manger un fantôme) et une musique de fond.
* **Niveaux Multiples :** Concevoir et implémenter plusieurs labyrinthes ou niveaux de difficulté.
* **IA de Fantômes Avancée :** Implémenter les comportements uniques de chaque fantôme (Blinky, Pinky, Inky, Clyde) pour des stratégies de poursuite plus complexes.
* **Système de Score Persistant :** Réintégrer un système de sauvegarde des meilleurs scores dans une base de données (comme SQLite) et un tableau des scores consultable.
* **Packaging de l'Application :** Créer un exécutable autonome (`.exe` pour Windows, `.dmg` pour macOS, etc.) pour une distribution plus facile aux utilisateurs finaux qui n'auraient pas Java installé.
* **Fruits Bonus :** Ajouter des fruits bonus qui apparaissent périodiquement pour des points supplémentaires.