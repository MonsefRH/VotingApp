#  VotingApp - Système de Vote 
---

##  Vue d'ensemble

VotingApp est un système de vote en ligne moderne construit en Java, transformé d'un code hérité monolithique en une architecture modulaire appliquant 3 design patterns fondamentaux. Le projet inclut une suite de tests complète et un pipeline CI/CD automatisé avec Jenkins et SonarQube.

### Améliorations principales

- ✅ **Refactoring complet** : 16 classes modulaires
- ✅ **Couverture de tests** : 0% → 80%+ (49 tests)
- ✅ **3 Design Patterns** : Factory, Strategy, Observer
- ✅ **Pipeline CI/CD** : Jenkins + SonarQube + JaCoCo
- ✅ **Code Quality** : 0 bugs, 0 vulnérabilités, 0 code smells
- ✅ **Détection de fraude** : Votes en double détectés automatiquement

---

## 🚀 Quick Start

### Installation

```bash
# Cloner le repository
git clone https://github.com/MonsefRH/VotingApp.git
cd VotingApp

# Installer les dépendances et compiler
mvn clean install

# Lancer les tests
mvn test

# Exécuter l'application
mvn exec:java -Dexec.mainClass="org.example.project.VotingApp"
```

### Tests et Rapports

```bash
# Générer le rapport JaCoCo
mvn jacoco:report
# Ouvrir: target/site/jacoco/index.html

# Générer l'analyse SonarQube
mvn sonar:sonar \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=YOUR_SONAR_TOKEN
```

---

## 📊 Métriques Actuelles

### Tests

| Métrique | Valeur | Status |
|----------|--------|--------|
| Tests exécutés | 49 | ✅ 100% passed |
| Couverture | 80%+ | ✅ Excellent |
| Temps d'exécution | ~5s | ✅ Rapide |

### JaCoCo Coverage

| Type                    | Couvert | Manqué | Total | Pourcentage |
|-------------------------|---------|--------|-------|-------------|
| Instructions            | 1 187   | 284    | 1 471 | 80 %        |
| Branches                | 103     | 57     | 160   | 64 %        |
| Lignes (Lines)          | 291     | 59     | 350   | 83 %        |
| Complexité (Cxty)       | 104     | 64     | 168   | 62 %        |
| Méthodes                | 76      | 9      | 85    | 89 %        |
| Classes                 | 13      | 0      | 13    | 100 %       |


### SonarQube Metrics

| Métrique | Valeur | Seuil | Status |
|----------|--------|-------|--------|
| **Code Coverage** | 80%+ | > 60% | ✅ |
| **Bugs** | 0 | 0 | ✅ |
| **Vulnérabilités** | 0 | 0 | ✅ |
| **Code Smells** | 0 | 0 | ✅ |
| **Duplications** | 0.0% | < 3% | ✅ |
| **Technical Debt** | < 1% | < 2% | ✅ |

---

##  Architecture

### Structure du projet

```
src/main/java/org/example/project/
├── model/
│   ├── Candidate.java          (Candidat immuable)
│   ├── Voter.java              (Électeur immuable)
│   └── Vote.java               (Vote immuable)
├── repo/
│   ├── VoteRepository.java     (Interface DAO)
│   ├── InMemoryVoteRepository.java
│   ├── CandidateRepository.java
│   └── ... (Voter, etc.)
├── factory/
│   └── RepositoryFactory.java  (Pattern 1: Factory)
├── strategy/
│   ├── CountingStrategy.java   (Pattern 2: Strategy)
│   ├── PluralityCountingStrategy.java
│   └── RankedChoiceCountingStrategy.java
├── observer/
│   ├── VoteListener.java       (Pattern 3: Observer)
│   ├── LoggingVoteListener.java
│   └── AuditVoteListener.java
├── service/
│   └── VoteService.java        (Logique métier)
└── VotingApp.java              (Interface CLI)
```

### Patterns appliqués

1. **Factory Method** - Création flexible des repositories
2. **Strategy** - Algorithmes de comptage interchangeables  
3. **Observer** - Notifications découplées

---

##  Configuration et Dépendances

### Prérequis

- Java 21
- Maven 3.8+
- Docker (optionnel, pour Jenkins/SonarQube)

### Dépendances principales

- **JUnit 5.9.3** - Framework de test
- **JaCoCo 0.8.12** - Couverture de code
- **SonarQube Scanner 3.9.1** - Analyse de qualité

---

##  Pipeline CI/CD

### Architecture

```
Git Push
  ↓
Jenkins Checkout
  ↓
Maven Build
  ↓
Unit Tests (49/49)
  ↓
JaCoCo Coverage (80%+)
  ↓
SonarQube Analysis
  ↓
Package JAR
  ↓
✅ SUCCESS
```

### Jenkinsfile

Le projet inclut un `Jenkinsfile` configuré avec 6 stages :

1. **Checkout** - Récupération du code depuis Git
2. **Build** - Compilation Maven (clean compile)
3. **Tests** - Exécution des 49 tests unitaires
4. **Coverage** - Génération du rapport JaCoCo
5. **SonarQube** - Analyse de qualité
6. **Package** - Création du JAR exécutable

### Configuration requise

#### Jenkins

- **Outils** : Maven 3.9.9, JDK 21
- **Credentials** :
  - `sonar-host-url` : URL SonarQube
  - `sonar-token` : Token SonarQube

#### SonarQube

- **Project Key** : `voting-system`
- **Quality Gate** : ✅ Configurée et validée

---

##  Rapports et Résultats

### Jenkins Console Output

```
✅ Stage "Checkout": SUCCESS
✅ Stage "Build": SUCCESS
✅ Stage "Tests": SUCCESS (49/49 tests)
✅ Stage "Coverage": SUCCESS (80%+ coverage)
✅ Stage "SonarQube": SUCCESS
✅ Stage "Package": SUCCESS

BUILD SUCCESS
```

### JaCoCo Report

**Accès** : `target/site/jacoco/index.html`
! Vous pouvez voir l'exemple de rapport générée par Jacoco dans `src/main/ressources/jacoco-files-generated/jacoco/index.html`

Coverage par classe (sélection) :

| Classe | Coverage | Instructions |
|--------|----------|--------------|
| PluralityCountingStrategy | 100% | 30/30 |
| RankedChoiceCountingStrategy | 100% | 45/45 |
| LoggingVoteListener | 100% | 20/20 |
| VoteService | 83% | 246/295 |
| Candidate | 93% | 60/64 |
| Vote | 77% | 73/94 |

### SonarQube Dashboard

**Accès** : `http://localhost:9000/dashboard?id=voting-system`

**Modules analysés** :

- factory : 58.8% coverage
- model : 86.5% coverage  
- observer : 69.6% coverage
- repo : 75.0% coverage
- service : 76.7% coverage
- strategy : 100% coverage

---

##  Rapports et Dashboards

### Jenkins Pipeline Status
<img width="1901" height="866" alt="Screenshot 2025-12-17 203251" src="https://github.com/user-attachments/assets/539fe767-dae6-4e6b-a005-4cc0a2deda8c" />

<img width="1902" height="871" alt="Screenshot 2025-12-17 201847" src="https://github.com/user-attachments/assets/ce7b1434-d6e1-45ec-88a0-83151b745510" />

<img width="1902" height="864" alt="Screenshot 2025-12-17 201902" src="https://github.com/user-attachments/assets/1d7a02e3-5b14-45e6-93fc-9aabd57e6229" />

<img width="1900" height="870" alt="Screenshot 2025-12-17 202035" src="https://github.com/user-attachments/assets/fe9bb9af-e7cf-4dbf-a51f-1cd24ed51998" />

### SonarQube Dashboard

<img width="1899" height="874" alt="Screenshot 2025-12-17 185322" src="https://github.com/user-attachments/assets/ab83e0fa-84b5-4dd7-92f8-6201a68af9c4" />

<img width="1905" height="865" alt="Screenshot 2025-12-17 185349" src="https://github.com/user-attachments/assets/0684d2f7-ab19-47b0-be64-596d3578ee93" />

*Dashboard SonarQube montrant les métriques de qualité (Coverage 80%+, 0 bugs, 0 vulnerabilities)*

### JaCoCo Coverage Report

<img width="1333" height="369" alt="image" src="https://github.com/user-attachments/assets/53d2a24d-e254-4574-bf27-02909896b134" />

---

##  Exécution des Tests

### Tests unitaires

```bash
mvn test
```

**Résultat** :
```
Tests run: 49, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

### Classes de test

- **ModelTests** - 9 tests
- **VotingAppTest** - 4 tests
- **VoteServiceTest** - 22 tests
- **PatternTests** - 14 tests

### Cas couverts

- ✅ Création et validation des modèles
- ✅ Enregistrement et comptage des votes
- ✅ Gestion des candidats et électeurs
- ✅ Détection des votes en double (fraude)
- ✅ Multiple algorithmes de comptage
- ✅ Notifications via observateurs
- ✅ Factories et créations

---

##  Contrôle de Qualité

### Code Quality Checks

- ✅ **Compilation** : 0 warnings
- ✅ **Tests** : 49/49 passing
- ✅ **Coverage** : 80%+ minimum
- ✅ **JaCoCo Check** : All checks passed
- ✅ **SonarQube Analysis** : Quality Gate PASSED

### Validations

- ✅ Pas de bugs
- ✅ Pas de vulnérabilités
- ✅ Pas de code smells (critiques)
- ✅ Pas de duplications
- ✅ SOLID principles respectés

---

##  Déploiement

### Build

```bash
mvn clean package
```

**Résultat** : `target/VotingApp-1.0-SNAPSHOT.jar`

### Exécution

```bash
java -jar target/VotingApp-1.0-SNAPSHOT.jar
```

---

