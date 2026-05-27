# StatePulse – LAB 18

Application Android développée en Java permettant de comprendre la gestion de l’état d’une interface avec **ViewModel**, **LiveData** et **SavedStateHandle**.

Ce laboratoire met en évidence la différence entre une gestion classique avec une variable dans l’Activity et une architecture moderne basée sur les composants Jetpack.

## Objectif:

Le but de ce laboratoire est de :

- Comprendre pourquoi une variable classique peut être perdue lors d’une rotation d’écran
- Tester une première version sans ViewModel ni LiveData
- Comprendre les limites de `onSaveInstanceState()`
- Utiliser un `ViewModel` pour conserver l’état après un changement de configuration
- Utiliser `LiveData` pour mettre à jour automatiquement l’interface
- Comprendre le rôle de `Observer`, `LifecycleOwner` et `ViewModelProvider`
- Différencier `MutableLiveData` et `LiveData`
- Utiliser `setValue()` depuis le thread principal
- Utiliser `postValue()` depuis un thread secondaire
- Tester `SavedStateHandle` pour restaurer un état léger après une destruction du processus
- Créer une interface personnalisée, moderne et visuellement agréable

## Description de l’application:

L’application **StatePulse** est une application de compteur interactif.

Elle permet de tester trois scénarios différents :

- Une version classique **sans ViewModel**
- Une version moderne **avec ViewModel et LiveData**
- Une version avancée **avec SavedStateHandle**

L’utilisateur peut :

- Augmenter la valeur du compteur
- Diminuer la valeur du compteur
- Réinitialiser la valeur
- Déclencher une incrémentation depuis un thread secondaire
- Tourner l’écran pour vérifier si la valeur est conservée
- Simuler une destruction du processus pour tester la restauration d’état

## Fonctionnalités:

- Compteur dynamique affiché au centre de l’écran
- Bouton d’incrémentation
- Bouton de décrémentation
- Bouton de réinitialisation
- Bouton bonus utilisant un thread secondaire
- Observation automatique des changements avec `LiveData`
- Conservation de l’état après rotation avec `ViewModel`
- Sauvegarde légère de l’état avec `SavedStateHandle`
- Interface moderne avec :
  - Fond en dégradé
  - Carte principale arrondie
  - Cercle coloré pour le compteur
  - Boutons personnalisés
  - Design responsive avec `ScrollView`

## Technologies utilisées:

- Android Studio
- Java
- XML
- Android Jetpack
- ViewModel
- LiveData
- SavedStateHandle
- AppCompat
- Minimum SDK : API 24

## Dépendances utilisées:

Les dépendances principales ajoutées dans le fichier `build.gradle.kts` sont :

```kotlin
val lifecycleVersion = "2.10.0"

implementation("androidx.lifecycle:lifecycle-viewmodel:$lifecycleVersion")
implementation("androidx.lifecycle:lifecycle-livedata:$lifecycleVersion")
implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycleVersion")
```
Ces dépendances permettent d’utiliser les composants Jetpack nécessaires à la gestion moderne du cycle de vie Android.

## Aperçu de l’application:

▶️ Trois démonstrations vidéo sont disponibles dans le dossier **Demo** du repository :

- Démonstration sans ViewModel
- Démonstration avec ViewModel et LiveData
- Démonstration avec SavedStateHandle

⚠️ En cas de problème de lecture depuis le repository :

👉 [▶️ Voir le dossier de démonstration sur Google Drive](https://)

## Structure du projet:

### Layouts (`res/layout`):

#### `activity_main.xml`

Contient l’interface principale de l’application.

Il inclut :

- Le titre de l’application
- Un sous-titre explicatif
- Une carte centrale
- Un compteur circulaire
- Les boutons d’action
- Un bouton bonus pour tester `postValue()`

L’interface utilise un `ScrollView` afin de rester lisible sur différents formats d’écran.

### Design (`res/drawable`):

#### `bg_statepulse_screen.xml`

Définit le fond principal de l’application avec un dégradé sombre et moderne.

#### `bg_statepulse_card.xml`

Définit l’arrière-plan de la carte principale avec :

- Coins arrondis
- Dégradé clair
- Bordure douce
- Padding interne

#### `bg_counter_bubble.xml`

Définit le cercle du compteur avec :

- Forme ovale
- Dégradé violet/bleu
- Bordure claire

#### `bg_button_plus.xml`

Définit le style du bouton d’incrémentation avec un dégradé vert.

#### `bg_button_minus.xml`

Définit le style du bouton de décrémentation avec un dégradé orange/rouge.

#### `bg_button_reset.xml`

Définit le style du bouton de réinitialisation avec un dégradé violet.

#### `bg_button_background.xml`

Définit le style du bouton bonus utilisé pour le test du thread secondaire.

## Classes Java:

### `LegacyPulseActivity.java`

Cette classe représente la version classique de l’application.

Elle utilise une simple variable locale :

```java
private int localPulse = 0;
```

Cette version permet de comprendre le problème de perte d’état lors d’une rotation d’écran.

Sans sauvegarde manuelle, la valeur du compteur peut revenir à zéro après la recréation de l’Activity.

Cette classe montre aussi la limite de `onSaveInstanceState()`, qui peut sauvegarder des valeurs simples mais devient insuffisant pour des états plus complexes.

### `PulseCounterViewModel.java`

Cette classe contient la logique principale du compteur.

Elle hérite de `ViewModel` :

```java
public class PulseCounterViewModel extends ViewModel
```

Elle stocke la valeur du compteur dans un `MutableLiveData`.

```java
private final MutableLiveData<Integer> pulseState;
```

Les méthodes principales sont :

- `boostPulse()` : augmente le compteur
- `lowerPulse()` : diminue le compteur
- `resetPulse()` : remet le compteur à zéro
- `boostFromWorkerThread()` : augmente le compteur depuis un thread secondaire
- `updatePulse()` : met à jour la valeur et la sauvegarde

Le `ViewModel` permet de conserver l’état lors des changements de configuration, comme la rotation de l’écran.

### `MainActivity.java`

Cette classe représente la version moderne de l’application.

Elle récupère le ViewModel avec :

```java
pulseViewModel = new ViewModelProvider(this).get(PulseCounterViewModel.class);
```

Puis elle observe les changements du compteur avec :

```java
pulseViewModel.getPulseState().observe(this, updatedPulse -> {
    tvPulseValue.setText(String.valueOf(updatedPulse));
});
```

L’Activity ne contient donc plus la logique du compteur. Elle se contente d’afficher les données observées.

Cette séparation rend le code plus propre, plus maintenable et plus conforme à l’architecture MVVM.

## Concepts importants:

### ViewModel

Le `ViewModel` permet de stocker et gérer les données liées à l’interface.

Il survit aux changements de configuration comme :

- Rotation d’écran
- Changement de thème
- Recréation temporaire de l’Activity

Dans ce lab, il garde la valeur du compteur même après rotation.

### LiveData

`LiveData` est une donnée observable qui respecte le cycle de vie de l’Activity.

L’interface est mise à jour uniquement lorsque l’Activity est active.

Cela évite :

- Les crashs liés au cycle de vie
- Les mises à jour inutiles
- Les risques de fuite mémoire

### MutableLiveData

`MutableLiveData` est une version modifiable de `LiveData`.

Dans ce projet, elle est utilisée uniquement dans le `ViewModel`.

L’Activity reçoit seulement une version en lecture seule avec :

```java
public LiveData<Integer> getPulseState()
```

Cette approche protège les données et respecte les bonnes pratiques.

### Observer

L’`Observer` permet d’écouter les changements d’un `LiveData`.

À chaque changement de valeur, l’interface est automatiquement mise à jour.

### `setValue()`

`setValue()` permet de modifier un `LiveData` depuis le thread principal.

Exemple :

```java
pulseState.setValue(newValue);
```

### `postValue()`

`postValue()` permet de modifier un `LiveData` depuis un thread secondaire.

Exemple :

```java
pulseState.postValue(nextValue);
```

Dans ce lab, cette méthode est testée avec le bouton bonus.

### SavedStateHandle

`SavedStateHandle` permet de sauvegarder un petit état de l’écran.

Dans ce projet, il est utilisé pour conserver la valeur du compteur avec une clé :

```java
private static final String PULSE_KEY = "saved_pulse_value";
```

À chaque changement, la valeur est sauvegardée :

```java
stateHandle.set(PULSE_KEY, newValue);
```

Cela permet de restaurer la valeur dans certains scénarios de destruction du processus.

## Tests réalisés:

| Test | Action réalisée | Résultat attendu |
|---|---|---|
| Version sans ViewModel | Incrémenter puis tourner l’écran | La valeur peut revenir à zéro |
| Version avec ViewModel | Incrémenter puis tourner l’écran | La valeur reste conservée |
| LiveData | Cliquer sur les boutons | L’interface se met à jour automatiquement |
| Thread secondaire | Cliquer sur le bouton bonus | La valeur augmente sans crash |
| SavedStateHandle | Mettre l’app en arrière-plan puis tuer le processus | La valeur peut être restaurée |
| Reset | Cliquer sur réinitialiser | Le compteur revient à zéro |

## Commandes utilisées pour le test SavedStateHandle:

Pour mettre l’application en arrière-plan :

```powershell
.\adb.exe shell input keyevent KEYCODE_HOME
```

Pour tuer le processus sans utiliser `force-stop` :

```powershell
.\adb.exe shell am kill com.example.viewmodellivedatademoenrichi
```

Pour relancer l’application depuis le launcher :

```powershell
.\adb.exe shell monkey -p com.example.viewmodellivedatademoenrichi -c android.intent.category.LAUNCHER 1
```

Le package peut varier selon le nom du projet. Il faut vérifier la valeur de `namespace` dans le fichier `build.gradle.kts`.

## Comparaison entre les versions:

| Critère | Version sans ViewModel | Version avec ViewModel + LiveData |
|---|---|---|
| Stockage de l’état | Variable dans l’Activity | ViewModel |
| Survie à la rotation | Non sans sauvegarde manuelle | Oui |
| Mise à jour de l’interface | Manuelle | Automatique avec LiveData |
| Respect du cycle de vie | Faible | Oui |
| Séparation logique / interface | Mélangée | Claire |
| Gestion des threads | Plus risquée | Possible avec postValue() |
| Code maintenable | Moyen | Meilleur |
| Architecture moderne | Non | Oui |
| Process death | Non géré directement | Amélioré avec SavedStateHandle |

## Résultat obtenu:

À la fin du laboratoire, l’application permet de visualiser clairement la différence entre une gestion classique de l’état et une gestion moderne avec Jetpack.

La version sans ViewModel montre le problème de perte d’état après recréation de l’Activity.

La version avec ViewModel et LiveData conserve correctement la valeur du compteur après rotation.

La version avec SavedStateHandle ajoute une couche supplémentaire pour sauvegarder un état léger et améliorer la restauration après destruction du processus.

## Conclusion:

Ce laboratoire permet de comprendre un concept fondamental du développement Android moderne : la gestion correcte de l’état d’une interface.

Grâce à `ViewModel`, les données ne sont plus stockées directement dans l’Activity, ce qui permet de mieux gérer les changements de configuration.

Grâce à `LiveData`, l’interface est mise à jour automatiquement tout en respectant le cycle de vie de l’Activity.

Grâce à `SavedStateHandle`, l’application peut restaurer un état léger dans des scénarios plus avancés.

Ce lab constitue une base importante pour comprendre l’architecture MVVM et les bonnes pratiques utilisées dans les applications Android professionnelles.
