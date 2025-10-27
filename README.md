# SigmaChess

SigmaChess est un petit jeu d'échecs écrit en Java, organisé selon le modèle MVC (Modèle / Vue / Contrôleur).

## Description

Ce projet fournit une implémentation basique d'un jeu d'échecs avec :
- gestion des pièces (Roi, Reine, Tour, Fou, Cavalier, Pion)
- représentation du plateau et des cases
- historique des coups
- contrôle de jeu via un contrôleur et une interface graphique basique

Le code source est structuré dans les dossiers `modele`, `vue` et `controleur`.

## Arborescence principale

- `Main.java` : point d'entrée de l'application.
- `controleur/ControleurJeu.java` : logique du contrôleur.
- `modele/jeu/` : classes du modèle (pièces, joueur, jeu, historique, IA, etc.).
- `modele/plateau/` : classes liées au plateau et aux cases.
- `vue/` : classes d'affichage (fenêtre de jeu, menus).
- `Images/` : ressources graphiques (si utilisées).

## Prérequis

- Java JDK 11+ installé (ou version compatible avec le projet).

## Compilation et exécution (depuis la racine du projet)

Méthode simple (utilisation de `javac` et `java`):

```bash
# compiler tous les fichiers .java et placer les .class dans le dossier out
mkdir -p out
javac -d out $(find . -name "*.java")

# lancer l'application (Main doit contenir la méthode main)
java -cp out Main
```

Si vous utilisez un IDE (IntelliJ IDEA, Eclipse, VS Code Java), importez simplement le dossier comme projet Java et exécutez `Main`.

## Tests

Aucun framework de test n'est inclus pour l'instant. Ajouter des tests unitaires (JUnit) est une amélioration souhaitable.

## Contribution

- Forkez le dépôt
- Créez une branche pour votre fonctionnalité/fix : `git checkout -b feature/ma-fonctionnalite`
- Ouvrez une pull request décrivant vos changements

Avant d'envoyer une PR, merci de vérifier que les compilations fonctionnent localement.

## Améliorations possibles

- Ajouter des tests unitaires (JUnit)
- Ajout d'une AI plus avancée
- Sauvegarde/chargement de parties
- Améliorer l'interface graphique


---

Fichier généré automatiquement : README.md
