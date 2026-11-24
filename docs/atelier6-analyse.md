# Atelier 6 - Analyse et Refactoring

## Partie 1 : Analyse de AutoLoc v0

### 1.2 Étape 2 : Analyse du problème

#### Question 1 : Combien de responsabilités possède la classe AutoLocManager ?

**Réponse : La classe AutoLocManager possède 5 responsabilités distinctes :**

1. **Calcul du prix de location** (responsabilité métier)
   - Calcul du tarif selon le type de réservation (Standard, Weekend, LongueDuree)
   
2. **Notification par email** (responsabilité communication)
   - Envoi d'emails aux clients
   
3. **Notification par SMS** (responsabilité communication)
   - Envoi de SMS aux clients
   
4. **Persistance des contrats** (responsabilité stockage)
   - Sauvegarde des contrats en base de données
   
5. **Gestion des paiements** (responsabilité comptable)
   - Enregistrement des paiements

**Conclusion :** Cette classe viole clairement le principe **Single Responsibility Principle (SRP)** car elle a trop de raisons de changer. Si on veut modifier la façon d'envoyer un email, on doit toucher à cette classe. Si on veut changer le calcul de prix, encore cette classe. C'est une classe "God Object" (objet dieu) qui fait tout.

---

#### Question 2 : Quel principe SOLID n'est pas respecté ?

**Réponse : Plusieurs principes SOLID sont violés :**

1. **S - Single Responsibility Principle (SRP) ❌**
   - Raison : La classe a 5 responsabilités au lieu d'une seule
   - Impact : Code fragile, difficile à tester, difficile à maintenir

2. **O - Open/Closed Principle (OCP) ❌**
   - Raison : Pour ajouter un nouveau type de tarif (ex: "Premium"), on doit **modifier** la méthode `louerVehicule()` en ajoutant un nouveau `else if`
   - Impact : Chaque ajout de fonctionnalité nécessite de modifier le code existant, risque de régression

3. **D - Dependency Inversion Principle (DIP) ❌**
   - Raison : La classe ne dépend d'aucune abstraction, tout est en dur
   - Impact : Impossible de remplacer facilement les implémentations (ex: changer d'email à SMS)

**Principe le plus violé : SRP (Single Responsibility Principle)**

---

#### Question 3 : Que se passe-t-il si AutoLoc ajoute un nouveau type de réservation ? (Exemple : PleineSaison)

**Réponse : Il faudrait :**

1. **Modifier** la méthode `louerVehicule()` en ajoutant un nouveau `else if` :
```java
else if (typeReservation.equals("PleineSaison")) {
    prix = nombreJours * 200;
}
```

2. **Problèmes causés :**
   - ❌ **Viole OCP** : On doit modifier le code existant au lieu de l'étendre
   - ❌ **Risque de régression** : En touchant au code existant, on peut casser les tarifs qui fonctionnaient déjà
   - ❌ **Tests à refaire** : Il faut retester toute la méthode, pas seulement le nouveau tarif
   - ❌ **Code devient de plus en plus long** : Si on a 10 types de tarifs, on aura 10 `else if` illisibles

**Donc, violation du principe OCP (Open/Closed Principle)**

---

## Conclusion de l'analyse

La classe `AutoLocManager` est un exemple typique de **code legacy mal conçu** :
- Trop de responsabilités (viole SRP)
- Code rigide et fermé à l'extension (viole OCP)
- Difficile à tester (dépendances en dur)
- Difficile à maintenir (tout est mélangé)

**Solution : Refactoring avec les Design Patterns GOF**
- **Strategy Pattern** pour le calcul de prix (respect OCP)
- **Factory Method** pour créer les stratégies et observateurs
- **Observer Pattern** pour les notifications (respect SRP)


---

## Partie 2 : Refactoring avec Strategy Pattern

### Solution implémentée

**Pattern utilisé : STRATEGY + FACTORY METHOD**

#### 1. Interface ITarificationStrategy
- Définit le contrat : `calculerTarif(Vehicule, Reservation)` et `getCode()`
- Respecte **DIP** : les classes dépendent de l'abstraction, pas des implémentations

#### 2. Stratégies concrètes (3 classes)
- **TarifStandardStrategy** : 150€/jour (fallback par défaut)
- **TarifWeekendStrategy** : 100€/jour (≤ 3 jours incluant weekend)
- **TarifLongueDureeStrategy** : 80€/jour (≥ 7 jours)

Chaque stratégie :
- Respecte **SRP** : une seule responsabilité = calculer un type de tarif spécifique
- Annotée **@Component** : gérée par Spring

#### 3. TarificationStrategyFactory (Factory Method)
- **Responsabilité unique** : sélectionner la bonne stratégie selon la réservation
- **Injection automatique** : Spring injecte toutes les implémentations de `ITarificationStrategy`
- **Logique de sélection** :
  1. Si `durée >= 7 jours` → LONGUE_DUREE
  2. Si `durée <= 3 jours` ET `weekend` → WEEKEND
  3. Sinon → STANDARD

#### Avantages de cette architecture

✅ **Respect OCP** : Pour ajouter un nouveau tarif (ex: "PREMIUM"), il suffit de :
- Créer une nouvelle classe `TarifPremiumStrategy implements ITarificationStrategy`
- Ajouter `@Component`
- Spring l'injecte automatiquement dans la factory
- **Aucune modification du code existant** !

✅ **Respect SRP** : Chaque classe a une responsabilité unique
- Stratégies → calculer un tarif
- Factory → sélectionner la stratégie

✅ **Testabilité** : Chaque stratégie peut être testée indépendamment

✅ **Maintenabilité** : Le code est lisible, modulaire, et facile à faire évoluer

#### Exemple d'utilisation

```java
@Service
public class ReservationService {
    private final TarificationStrategyFactory factory;
    
    public BigDecimal calculerPrix(Reservation reservation) {
        ITarificationStrategy strategy = factory.resoudre(reservation);
        return strategy.calculerTarif(vehicule, reservation);
    }
}
```


---

## Partie 4 : Observer Pattern pour les Notifications

### Solution implémentée

**Pattern utilisé : OBSERVER (GoF Comportemental)**

#### Architecture Observer

```
ReservationService (Client)
    ↓ utilise
ReservationSubject (Sujet Observable)
    ↓ notifie
List<IReservationObserver> (Observateurs)
    ├─ EmailNotificationObserver
    ├─ SmsNotificationObserver
    └─ [Futurs observateurs extensibles...]
```

#### Composants créés

**1. IReservationObserver (Interface)**
- Contrat commun pour tous les observateurs
- Méthodes : `onReservationConfirmee()`, `onReservationAnnulee()`
- Respecte **ISP** (Interface Segregation Principle)

**2. ReservationSubject (Sujet Observable)**
- Maintient la liste des observateurs
- Injecte automatiquement tous les beans `IReservationObserver` via Spring
- Méthodes : `notifierConfirmation()`, `notifierAnnulation()`
- Utilise `forEach()` pour notifier tous les observateurs

**3. Observateurs concrets**
- **EmailNotificationObserver** : Simule envoi d'emails (logs)
- **SmsNotificationObserver** : Simule envoi de SMS (logs)
- Tous annotés `@Component` pour injection Spring
- Utilisent `@Slf4j` pour logger les notifications

**4. Intégration dans ReservationService**
- Injecte `ReservationSubject` via constructeur
- Méthode `creerReservation()` : crée + notifie confirmation
- Méthode `annulerReservation()` : annule + notifie annulation

#### Avantages de cette architecture

✅ **Découplage total** : 
- `ReservationService` ne connaît PAS les observateurs concrets
- Il utilise uniquement `ReservationSubject`
- Pour ajouter un nouveau type de notification, **aucune modification** du service

✅ **Respect SRP** :
- `ReservationService` → logique métier réservation
- `ReservationSubject` → gestion des observateurs
- `EmailObserver` → envoi emails
- `SmsObserver` → envoi SMS

✅ **Respect OCP** :
- Pour ajouter "PushNotificationObserver" :
  ```java
  @Component
  public class PushNotificationObserver implements IReservationObserver {
      public void onReservationConfirmee(Reservation r) { /* ... */ }
  }
  ```
  Spring l'injecte automatiquement, **aucun code à modifier** !

✅ **Testabilité** :
- Chaque observateur testé indépendamment
- Possibilité de mocker `ReservationSubject` dans les tests

#### Exemple d'exécution

Quand on appelle `reservationService.creerReservation(client)` :

1. La réservation est créée en base
2. `reservationSubject.notifierConfirmation(reservation)` est appelé
3. Le sujet parcourt **tous les observateurs** :
   - `emailObserver.onReservationConfirmee()` → Log "[EMAIL] Confirmation envoyée..."
   - `smsObserver.onReservationConfirmee()` → Log "[SMS] Confirmation envoyée..."
4. Si on ajoute `pushObserver`, il sera notifié automatiquement !

---

## Comparaison Avant/Après

### ❌ AVANT (AutoLocManager)
```java
public void louerVehicule(...) {
    // Calcul prix en dur avec if/else
    if (type.equals("Standard")) prix = ...
    else if (type.equals("Weekend")) prix = ...
    
    // Notifications mélangées
    envoyerEmail(email);
    envoyerSMS(sms);
    sauvegarderContrat();
}
```

**Problèmes** :
- 5 responsabilités mélangées (viole SRP)
- Code rigide (viole OCP)
- Impossible de tester séparément
- Difficile à maintenir

### ✅ APRÈS (Refactoring avec Patterns)

**Calcul de prix** :
```java
ITarificationStrategy strategy = factory.resoudre(reservation);
BigDecimal prix = strategy.calculerTarif(vehicule, reservation);
```

**Notifications** :
```java
reservationSubject.notifierConfirmation(reservation);
// Tous les observateurs sont notifiés automatiquement
```

**Avantages** :
- Chaque classe a UNE responsabilité (SRP)
- Extensible sans modifier le code (OCP)
- Testable indépendamment
- Maintenable et lisible

---

## Principes SOLID appliqués - Résumé

| Principe | Comment il est respecté |
|----------|------------------------|
| **S**RP | Chaque classe a une responsabilité unique (Strategy = calcul, Observer = notification, Factory = création) |
| **O**CP | Nouvelles stratégies/observateurs ajoutables sans modifier le code existant |
| **L**SP | Non directement utilisé dans ce TP |
| **I**SP | Interfaces focalisées (ITarificationStrategy, IReservationObserver) |
| **D**IP | Dépendance sur abstractions (interfaces), pas sur implémentations concrètes |

---

## Patterns GOF appliqués - Résumé

| Pattern | Objectif | Classes concernées |
|---------|----------|-------------------|
| **Strategy** | Sélectionner algorithme dynamiquement | ITarificationStrategy + 3 implémentations |
| **Factory Method** | Créer objets sans exposer logique création | TarificationStrategyFactory, NotificationFactory |
| **Observer** | Notifier automatiquement multiples parties | ReservationSubject + EmailObserver + SmsObserver |
