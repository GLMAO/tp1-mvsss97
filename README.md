# Rapport du TP - Service d'Horloge et Patrons de Conception

**Auteur :** CS(19)

---

## 1. Objectif du TP

L'objectif de ce projet était de développer un service de temps (`TimerService`) en utilisant le patron de conception **Observer**. Le projet est divisé en plusieurs modules, séparant l'interface du service, son implémentation, et les clients qui l'utilisent.

Les principales fonctionnalités à implémenter étaient :
- Une horloge console.
- Un compte à rebours avec désinscription automatique.
- La correction d'un bug de concurrence.
- La création d'une interface graphique pour l'horloge (bonus).

## 2. Déroulement et Implémentation

### Partie (c) : Création de l'Horloge Console

Pour afficher l'heure en console, la classe `Horloge` a été modifiée pour devenir un "Observer" du `TimerService`.

- **Implémentation de `TimerChangeListener`** : La classe `Horloge` implémente cette interface, ce qui l'oblige à définir une méthode `propertyChange`.
- **Abonnement au service** : Dans son constructeur, l'instance de `Horloge` reçoit une référence vers le `TimerService` et s'abonne à ses notifications en appelant `timerService.addTimeChangeListener(this)`.
- **Réaction aux changements** : Lorsque le `TimerService` notifie un changement de seconde, la méthode `propertyChange` de l'horloge est appelée, qui à son tour déclenche l'affichage de l'heure mise à jour.

### Partie (d) : Implémentation du Compte à Rebours

Un nouveau client, `CompteARebours`, a été créé sur le même modèle que l'horloge.

- **Logique de décrémentation** : Il prend un entier au constructeur et le décrémente à chaque notification de changement de seconde.
- **Désinscription automatique** : Une fois que le compteur atteint 0, l'instance de `CompteARebours` se désabonne du `TimerService` en appelant `timerService.removeTimeChangeListener(this)`. Cela permet de libérer les ressources et d'arrêter le traitement inutile.
- **Mise en évidence du bug de concurrence** : En instanciant plusieurs comptes à rebours, une exception `java.util.ConcurrentModificationException` est survenue. Ce bug se produit car un client (`CompteARebours`) tente de se supprimer de la liste des auditeurs pendant que le `TimerService` est en train de parcourir cette même liste pour envoyer les notifications.

### Partie (e) : Correction du Bug de Concurrence

Pour résoudre le problème de modification concurrente, la gestion des auditeurs a été déléguée à une classe spécialisée et thread-safe.

- **Héritage de `PropertyChangeListener`** : L'interface `TimerChangeListener` a été modifiée pour hériter de `java.beans.PropertyChangeListener`, une interface standard en Java pour le patron Observer.
- **Utilisation de `PropertyChangeSupport`** : Dans l'implémentation `DummyTimeServiceImpl`, la `LinkedList` manuelle a été remplacée par une instance de `java.beans.PropertyChangeSupport`. Cette classe est conçue pour gérer les ajouts, suppressions et notifications d'auditeurs de manière atomique et sécurisée, empêchant ainsi les modifications concurrentes.
- **Adaptation des clients** : Les classes `Horloge` et `CompteARebours` ont été mises à jour pour s'adapter à la nouvelle signature de la méthode `propertyChange(PropertyChangeEvent evt)`.

**Résultat :** Le bug a été résolu avec succès. L'application peut désormais gérer un grand nombre d'auditeurs qui se désinscrivent dynamiquement sans provoquer de crash.

### Partie (f) - Bonus : Horloge Graphique (GUI)

Pour la partie bonus, une interface graphique simple a été développée en utilisant la bibliothèque **Swing**.

- **Création de la classe `HorlogeGUI`** : Cette nouvelle classe crée une `JFrame` (fenêtre) contenant un `JLabel` (étiquette de texte).
- **Mise à jour de l'affichage** : `HorlogeGUI` implémente également `TimerChangeListener` et met à jour le texte du `JLabel` à chaque seconde pour afficher l'heure actuelle.
- **Gestion du Threading Swing** : L'appel à la mise à jour de l'interface est encapsulé dans `SwingUtilities.invokeLater()`. C'est une pratique cruciale en Swing pour garantir que toutes les modifications des composants graphiques s'exécutent sur le *Event Dispatch Thread (EDT)*, évitant ainsi les conflits entre le thread du `Timer` et le thread de l'interface graphique.

## 3. Comment Lancer l'Application

1.  Ouvrez le projet dans votre IDE (ex: IntelliJ IDEA, Eclipse).
2.  Naviguez vers le fichier `launcher/src/main/java/org/emp/gl/core/launcher/App.java`.
3.  Exécutez la méthode `main` de cette classe.

Une fenêtre affichant l'horloge graphique apparaîtra, et la console affichera simultanément les messages des horloges et des comptes à rebours.
