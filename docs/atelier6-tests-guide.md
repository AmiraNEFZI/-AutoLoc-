# 🧪 Guide des Tests - Atelier 6

## 📋 Vue d'ensemble

Tous les tests pour les Design Patterns GOF ont été créés dans :
```
src/test/java/tn/esprit/autoloc/DesignPatternsTest.java
```

Ce fichier contient **12 tests** organisés en 3 catégories :

---

## 🏭 Tests Factory Method Pattern (4 tests)

### Test 1 : Créer observateur Email
```java
testNotificationFactoryCreerEmail()
```
- **Objectif** : Vérifie que `notificationFactory.creerObservateur("email")` retourne bien un `EmailNotificationObserver`
- **Assertion** : L'objet retourné est une instance de `EmailNotificationObserver`

### Test 2 : Créer observateur SMS
```java
testNotificationFactoryCreerSms()
```
- **Objectif** : Vérifie que `notificationFactory.creerObservateur("sms")` retourne bien un `SmsNotificationObserver`
- **Assertion** : L'objet retourné est une instance de `SmsNotificationObserver`

### Test 3 : Canal inconnu lance exception
```java
testNotificationFactoryCanalInconnu()
```
- **Objectif** : Vérifie que la factory rejette un canal non supporté (ex: "whatsapp")
- **Assertion** : `IllegalArgumentException` est levée avec message approprié

### Test 4 : Canal null lance exception
```java
testNotificationFactoryCanalNull()
```
- **Objectif** : Vérifie la gestion du cas `null`
- **Assertion** : `IllegalArgumentException` est levée

---

## 🎯 Tests Strategy Pattern (4 tests)

### Test 5 : Tarif Standard (4 jours en semaine)
```java
testTarifStandard()
```
- **Scénario** : Réservation de 4 jours commençant un lundi
- **Logique attendue** : Durée < 7j ET pas weekend → Stratégie STANDARD
- **Tarif attendu** : 4 jours × 150€ = **600€**
- **Assertions** :
  - `strategy.getCode() == "STANDARD"`
  - Prix calculé = 600€

### Test 6 : Tarif Weekend (3 jours commençant vendredi)
```java
testTarifWeekend()
```
- **Scénario** : Réservation de 3 jours commençant un vendredi
- **Logique attendue** : Durée ≤ 3j ET weekend → Stratégie WEEKEND
- **Tarif attendu** : 3 jours × 100€ = **300€**
- **Assertions** :
  - `strategy.getCode() == "WEEKEND"`
  - Prix calculé = 300€

### Test 7 : Tarif Longue Durée (10 jours)
```java
testTarifLongueDuree()
```
- **Scénario** : Réservation de 10 jours
- **Logique attendue** : Durée ≥ 7j → Stratégie LONGUE_DUREE
- **Tarif attendu** : 10 jours × 80€ = **800€**
- **Assertions** :
  - `strategy.getCode() == "LONGUE_DUREE"`
  - Prix calculé = 800€

### Test 8 : Cas limite 7 jours
```java
testTarifLongueDuree7Jours()
```
- **Scénario** : Réservation de **exactement 7 jours**
- **Logique attendue** : Durée = 7j (seuil ≥ 7) → Stratégie LONGUE_DUREE
- **Assertion** : `strategy.getCode() == "LONGUE_DUREE"`

---

## 👁️ Tests Observer Pattern (4 tests)

### Test 9 : Notification confirmation
```java
testObserverConfirmation()
```
- **Scénario** : Appeler `reservationSubject.notifierConfirmation()`
- **Comportement attendu** : Tous les observateurs (Email + SMS) sont notifiés
- **Vérification** : Aucune exception levée, logs contiennent `[EMAIL]` et `[SMS]`

### Test 10 : Notification annulation
```java
testObserverAnnulation()
```
- **Scénario** : Appeler `reservationSubject.notifierAnnulation()`
- **Comportement attendu** : Tous les observateurs sont notifiés de l'annulation
- **Vérification** : Aucune exception levée, logs contiennent messages annulation

### Test 11 : Intégration création réservation
```java
testObserverIntegrationCreerReservation()
```
- **Scénario** : Appeler `reservationService.creerReservation(client)`
- **Comportement attendu** :
  1. Réservation créée en base avec statut `CONFIRMEE`
  2. Observers automatiquement notifiés via `ReservationSubject`
- **Assertions** :
  - Réservation possède un ID
  - Statut = `CONFIRMEE`
  - Logs montrent notifications envoyées

### Test 12 : Intégration annulation réservation
```java
testObserverIntegrationAnnulerReservation()
```
- **Scénario** : Appeler `reservationService.annulerReservation(reservation)`
- **Comportement attendu** :
  1. Statut changé à `ANNULEE`
  2. Observers automatiquement notifiés
- **Assertions** :
  - Statut = `ANNULEE`
  - Logs montrent notifications d'annulation

---

## 🚀 Comment exécuter les tests

### Option 1 : Via IntelliJ IDEA / Eclipse
1. Ouvrir `DesignPatternsTest.java`
2. Clic droit sur la classe → **Run 'DesignPatternsTest'**
3. Voir les résultats dans la console

### Option 2 : Via Maven en ligne de commande
```bash
# Exécuter uniquement les tests de patterns GOF
mvn test -Dtest=DesignPatternsTest

# Exécuter TOUS les tests du projet
mvn test

# Exécuter un test spécifique
mvn test -Dtest=DesignPatternsTest#testTarifStandard
```

### Option 3 : Via PowerShell
```powershell
# Si mvnw fonctionne
./mvnw test -Dtest=DesignPatternsTest

# Sinon avec Maven installé
mvn test -Dtest=DesignPatternsTest
```

---

## ✅ Résultats attendus

Quand tous les tests passent, vous devriez voir :

```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running tn.esprit.autoloc.DesignPatternsTest

========== TEST 1: NotificationFactory - Créer observateur Email ==========
✓ Factory a créé correctement un EmailNotificationObserver

========== TEST 2: NotificationFactory - Créer observateur SMS ==========
✓ Factory a créé correctement un SmsNotificationObserver

========== TEST 3: NotificationFactory - Canal inconnu ==========
✓ Factory rejette correctement un canal inconnu

========== TEST 4: NotificationFactory - Canal null ==========
✓ Factory rejette correctement un canal null

========== TEST 5: Strategy Pattern - Tarif Standard ==========
✓ Stratégie STANDARD appliquée : 4 jours → 600.00€

========== TEST 6: Strategy Pattern - Tarif Weekend ==========
✓ Stratégie WEEKEND appliquée : 3 jours → 300.00€

========== TEST 7: Strategy Pattern - Tarif Longue Durée ==========
✓ Stratégie LONGUE_DUREE appliquée : 10 jours → 800.00€

========== TEST 8: Strategy Pattern - Cas limite 7 jours ==========
✓ Cas limite 7 jours → Stratégie LONGUE_DUREE

========== TEST 9: Observer Pattern - Confirmation réservation ==========
[EMAIL] Confirmation envoyée au client jean@email.com
[SMS] Confirmation envoyée au 0612345678
✓ Notification de confirmation envoyée à tous les observateurs

========== TEST 10: Observer Pattern - Annulation réservation ==========
[EMAIL] Annulation notifiée au client marie@email.com
[SMS] Annulation notifiée au 0698765432
✓ Notification d'annulation envoyée à tous les observateurs

========== TEST 11: Observer - Intégration avec ReservationService ==========
[EMAIL] Confirmation envoyée au client alice@email.com
[SMS] Confirmation envoyée au 0645678901
✓ Réservation créée avec ID=XX et observers notifiés automatiquement

========== TEST 12: Observer - Intégration annulation ==========
[EMAIL] Annulation notifiée au client bob@email.com
[SMS] Annulation notifiée au 0656789012
✓ Réservation annulée et observers notifiés automatiquement

[INFO] Tests run: 12, Failures: 0, Errors: 0, Skipped: 0
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

---

## 📊 Couverture des tests

| Pattern | Composants testés | Nombre de tests |
|---------|-------------------|-----------------|
| **Factory Method** | NotificationFactory | 4 tests |
| **Strategy** | TarificationStrategyFactory + 3 stratégies | 4 tests |
| **Observer** | ReservationSubject + 2 observateurs + intégration | 4 tests |
| **TOTAL** | | **12 tests** |

---

## 🎯 Objectifs pédagogiques atteints

✅ **Factory Method** :
- Création d'objets sans exposer logique de création
- Gestion des erreurs (canal inconnu, null)
- Respect DIP (retour interface, pas classe concrète)

✅ **Strategy** :
- Sélection dynamique d'algorithme (calcul tarif)
- Vérification logique métier (durée, weekend)
- Tests des cas limites (7 jours)

✅ **Observer** :
- Notification automatique multiples parties
- Découplage total (service ne connaît pas les observers)
- Intégration complète dans service métier

✅ **SOLID** :
- SRP : Chaque classe a une responsabilité unique
- OCP : Extension sans modification
- DIP : Dépendance sur abstractions

---

## 🐛 Dépannage

### Erreur : "Cannot resolve symbol 'Builder'"
→ Vérifier que Lombok est configuré dans votre IDE
→ Activer "Enable annotation processing"

### Erreur : "No bean named 'emailNotificationObserver'"
→ Vérifier que toutes les classes ont `@Component`
→ Scanner Spring doit inclure le package `tn.esprit.autoloc`

### Tests échouent avec NullPointerException
→ Vérifier que Spring Boot démarre correctement (`@SpringBootTest`)
→ Vérifier l'injection de dépendances (`@Autowired`)

---

## 📝 Notes finales

Ces tests **démontrent le bon fonctionnement** des 3 patterns GOF implémentés.

Pour tester **manuellement** dans Postman, vous pouvez créer des endpoints REST qui appellent ces services, mais les tests unitaires suffisent pour valider l'atelier 6.

**Prochaine étape** : Commit Git avec message :
```bash
git add .
git commit -m "Atelier 6 : Design Patterns GOF et SOLID - Strategy, Factory, Observer + Tests complets"
```
