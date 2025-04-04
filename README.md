# Projet GL - Partie 2 : Amélioration du projet
> **Louis Becue**

## Notes

J'ai initialement commencé le projet sur GitLab. Cependant, en raison de problèmes de compilation, j'ai décidé de faire un fork d'un projet qui ne présentait pas ces problèmes.  

Pour consulter l'historique complet des commits du projet dans le temps, il faut aller sur le dépôt GitLab : [lien du projet](https://gitlab-etu.fil.univ-lille.fr/louis.becue.etu/gl-gson).

## Petites Modifications

### Renommage de la méthode `indexOf` en `findIndexOf`

- **Lien du commit** : [03d76b37edad0484f4e8e669654c5fdf6da327f4](https://github.com/google/gson/commit/03d76b37edad0484f4e8e669654c5fdf6da327f4)

- **Fichier concerné** :
  - **test-shrinker/src/main/java/com/example/TestExecutor.java**

- **Description** : Renommage de la méthode `indexOf` en `findIndexOf` pour améliorer sa clarté et rendre son objectif plus explicite.  

### Ajout d'une constante `UNREACHABLE_ERROR`

- **Lien du commit** : [1044696c162020ed96a8850cb0a1217860febc3d](https://github.com/google/gson/commit/1044696c162020ed96a8850cb0a1217860febc3d)

- **Description** : Introduction d'une constante `UNREACHABLE_ERROR` pour permettre une gestion centralisée du texte, évitant ainsi de modifier directement le code.  

### Renommage des classes contenant le symbole `$`

- **Lien du commit** : [266da71093b677185710abb7f39c40ffc5e80e2d](https://github.com/google/gson/commit/266da71093b677185710abb7f39c40ffc5e80e2d)

- **Fichiers concernés** :
  - **gson/src/main/java/com/google/gson/internal/$Gson$Types.java**
  - **gson/src/main/java/com/google/gson/internal/$Gson$Preconditions.java**

- **Description** : L'issue ([Issue #1744](https://github.com/google/gson/issues/1744)) traite ce probléme. Cela permet d'empêcher l'importation automatique dans l'IDE. Cependant, les IDE actuels permettent d'empêcher l'apparition de certains types ou packages dans les suggestions d'importation. Donc ce nomage n'est plus indispensable.

### Ajout de documentarion à la place de TODO

- **Lien du commit** : [2a0f2da010cf6d5e96bd2eda3bb73c94ee210573](https://github.com/google/gson/commit/2a0f2da010cf6d5e96bd2eda3bb73c94ee210573)

- **Fichier concerné** :
  - **extras/src/main/java/com/google/gson/graph/GraphAdapterBuilder.java**
  - **Description** : Ajout de documentation pour la classe `GraphAdapterBuilder`. Cette modification remplace un commentaire **TODO: proper documentation** par une documentation.

## Moyennes Modifications

### Ajout d'une méthode local dans le méthode `write()` de la classe `ReflectiveTypeAdapterFactory`

- **Lien du commit** : [3c6990ef1cf118242238cb212f1804647b52fdbf](https://github.com/google/gson/commit/3c6990ef1cf118242238cb212f1804647b52fdbf)

- **Fichier concerné** :
  - **gson/src/main/java/com/google/gson/internal/bind/ReflectiveTypeAdapterFactory.java**

- **Description** : Ajout d'une méthode pour extraire et vérifier l'accésibilté d'un objet et donc réduire la compléxité cyclomatique.

- **Résultat** : La compléxité cyclomatique est passé de `11` à `9`.

### Réduction la complexité cyclomatique de la méthode `write()` de la classe `MapTypeAdapterFactory`

- **Lien du commit** : [6994059dd30a5d93ee2a3307292c07fd0eb8c710](https://github.com/google/gson/commit/6994059dd30a5d93ee2a3307292c07fd0eb8c710)

- **Fichier concerné** :
  - **gson/src/main/java/com/google/gson/internal/bind/MapTypeAdapterFactory.java**

- **Description** : Une nouvelle version de la méthode `write()` en décomposant la logique en plusieurs méthodes privées, notamment `writeSimpleMap`, `writeComplexMap`, `extractKeysAndValues`, `writeAsArrays`, et `writeAsObject`. Ces méthodes permettent de mieux structurer le code et de le rendre plus lisible et maintenable.  

- **Résultat** : La compléxité cyclomatique est passé de `17` à `5`.

### Ajout de tests pour la méthode `write()` de la classe `MapTypeAdapterFactory`

- **Lien du commit** : [d70574e2514cc3545ad8acbe635c86390a6e10c1](https://github.com/google/gson/commit/d70574e2514cc3545ad8acbe635c86390a6e10c1)

- **Fichier concerné** :
  - **gson/src/test/java/com/google/gson/internal/bind/MapTypeAdapterFactoryTest.java**

- **Description** : Ajout de méthodes de test pour la fonction modifiée précédemment, afin de valider son comportement et garantir son bon fonctionnement.

- **Résultat** : Les tests passent.

## Grande Modification

### Ajout d'un dessign pattern strategy pour le traitement des dates

- **Description** : Ajout d'un pattern strategy car le code actuel utilise des conditions complexes pour le traitement des dates.

- **Fichier concerné** :
  - **gson/src/test/java/com/google/gson/internal/bind/MapTypeAdapterFactoryTest.java**

#### Les étapes

- **Ajout d'une interface `DateFormattingStrategy`**

  - **Lien du commit** : [84596a9461b48e50ed8c0fd02c6dcd6bea29c5a2](https://github.com/google/gson/commit/84596a9461b48e50ed8c0fd02c6dcd6bea29c5a2)

  - **Fichier concerné** :
    - **gson/src/main/java/com/google/gson/DateTypeAdapter.java**

  - **Description** : Création d'une interface `DateFormattingStrategy`
  pour les différentes stratégies de formatage de dates.

- **Ajout de la stratégie basée sur un pattern**

  - **Lien du commit** : [4b258ebfcaf0e51008de7c2e69705d3af4060c8c](https://github.com/google/gson/commit/4b258ebfcaf0e51008de7c2e69705d3af4060c8c)

  - **Fichier concerné** :
    - **gson/src/main/java/com/google/gson/PatternBasedDateStrategy.java**

  - **Description** : Implémentation d'une stratégie de formatage de dates basée sur des patterns (par exemple, `yyyy-MM-dd`). Cette stratégie utilise des objets `SimpleDateFormat` pour gérer les conversions entre chaînes de caractères et objets `Date`.

- **Ajout de la stratégie basée sur les types SQL**

  - **Lien du commit** : [a45be25fcfec1725f5bd6f0eae62bb2a21fe5bb0](https://github.com/google/gson/commit/a45be25fcfec1725f5bd6f0eae62bb2a21fe5bb0)

  - **Fichier concerné** :
    - **gson/src/main/java/com/google/gson/SqlDateStrategy.java**

  - **Description** : Création d'une stratégie spécifique pour le formatage des dates SQL (`java.sql.Date`). Cette stratégie permet de gérer les particularités des types SQL tout en respectant l'interface `DateFormattingStrategy`.

- **Création des `TypeAdapter` utilisant les stratégies**

  - **Lien du commit** : [4872f80b6d4ca897e87df55250fdd03a8d684645](https://github.com/google/gson/commit/4872f80b6d4ca897e87df55250fdd03a8d684645)

  - **Fichier concerné** :
    - **gson/src/main/java/com/google/gson/DateTypeAdapter.java**

  - **Description** : Mise à jour de la classe `DateTypeAdapter` pour intégrer les différentes stratégies de formatage de dates. Cela permet de choisir dynamiquement la stratégie appropriée.

- **Modification de `GsonBuilder` pour intégrer les stratégies**

  - **Lien du commit** : [6af6fd219ba00f458b495c4ddf5c85ca83f1e130](https://github.com/google/gson/commit/6af6fd219ba00f458b495c4ddf5c85ca83f1e130)

  - **Fichier concerné** :
    - **gson/src/main/java/com/google/gson/GsonBuilder.java**

  - **Description** : Ajout de nouvelles méthodes dans `GsonBuilder` pour ajouter le nouveaux pattern strategy de formatage de dates.

- **Résultat** : Malheureusement, cette modification ne compile pas. Les messages d'erreur ne sont pas suffisamment précis pour identifier la cause exacte du problème.

```bash
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.13.0:compile (default-compile) on project gson: Compilation failure
[ERROR] warnings found and -Werror specified
```
