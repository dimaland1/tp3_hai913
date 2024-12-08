# Manuel Complet - Application Java de Logging et Profilage

## 1. Présentation

### 1.1 Description
Application Java permettant de gérer des produits avec un système de logging avancé pour le profilage des utilisateurs. L'application utilise :
- Spring Boot pour le backend
- MongoDB pour la persistance
- Log4j2 pour le logging
- Spoon pour l'instrumentation automatique

### 1.2 Fonctionnalités principales
- Gestion des utilisateurs (création, connexion)
- Gestion des produits (CRUD)
- Logging automatique des actions
- Profilage des utilisateurs

## 2. Configuration de l'environnement

### 2.1 Prérequis
- Java JDK 11+
- IntelliJ IDEA
- Maven 3.6+
- MongoDB 4.4+

### 2.2 MongoDB
MongoDB est déjà configuré dans le projet via `MongoConfig.java` :
```java
@Configuration
@EnableMongoRepositories(basePackages = "com.example.demo.repository")
public class MongoConfig extends AbstractMongoClientConfiguration {
    @Override
    protected String getDatabaseName() {
        return "tp3_db";
    }

    @Override
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb://localhost:27017");
    }
}
```

## 3. Installation et configuration

### 3.1 Installation de MongoDB
1. Télécharger et installer MongoDB
2. Démarrer le service MongoDB
3. Vérifier l'accès sur le port 27017

### 3.2 Installation du projet
1. Ouvrir IntelliJ IDEA
2. `File > New > Project from Version Control`
3. Cloner le repository
4. Attendre le téléchargement des dépendances Maven

## 4. Build et déploiement

### 4.1 Instrumentation avec Spoon
1. Trouver `SpoonMain` dans le projet
2. Exécuter `SpoonMain.main()`
3. Copier le code généré depuis `spooned/`
4. Remplacer les fichiers sources correspondants

### 4.2 Build du projet
```bash
# Via Maven
mvn clean install

# Ou via IntelliJ
# Maven Window > clean > install
```

### 4.3 Démarrage
```bash

Lancer le fichier Main.java

```

## 5. Utilisation de l'application

### 5.1 Création d'un compte
```
=== Login Menu ===
1. Create new user
2. Login
3. Exit
Choose an option: 1

Enter ID: <votre_id>
Enter name: <votre_nom>
Enter age: <votre_age>
Enter email: <votre_email>
Enter password: <votre_mot_de_passe>
```

### 5.2 Connexion
```
Choose an option: 2
Enter user ID: <votre_id>
```

### 5.3 Menu principal
```
=== Product Management ===
1. Display all products
2. Find product by ID
3. Add new product
4. Update product
5. Delete product
6. Logout
```

### 5.4 Gestion des produits

#### Ajouter un produit
```
Choose option: 3
Enter product ID: <id>
Enter name: <nom>
Enter price: <prix>
Enter expiration date (DD-MM-YYYY): <date>
```

#### Modifier un produit
```
Choose option: 4
Enter product ID to update: <id>
```

#### Supprimer un produit
```
Choose option: 5
Enter product ID to delete: <id>
```

## 6. Comprendre les logs

### 6.1 Localisation des logs
- Logs application : `logs/application.log`
- Profils utilisateurs : `logs/user-profile.json`

### 6.2 Structure des logs
```json
{
    "timestamp": "2024-12-08 10:30:00",
    "level": "INFO",
    "user": "<user_id>",
    "action": "<action_type>",
    "details": { ... }
}
```

## Processus de build et d'exécution

### 1. Instrumentation avec Spoon
1. Localiser la classe `SpoonMain` dans le projet
2. Clic droit sur `SpoonMain` > `Run 'SpoonMain.main()'`
3. Spoon va générer le code instrumenté dans le dossier `spooned/`
4. Copier les fichiers générés depuis `spooned/` vers les sources du projet
   - Remplacer les fichiers existants par leurs versions instrumentées
   - **Important**: Vérifier que les packages correspondent

## 1. Configuration des tests

### Prérequis
- MongoDB en cours d'exécution
- Port MongoDB : 27017
- Base de test : `tp3_db` (créée automatiquement)

### Configuration Maven (pom.xml)
Les dépendances de test sont déjà incluses :
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## 2. Exécution des tests

### Via IntelliJ IDEA
1. Navigation vers les fichiers de test
   - `src/test/java/`
   - TestScenarios.java
   - UserProfileTest.java

2. Exécution des tests
   - Clic droit sur le dossier `test` > Run 'All Tests'
   - Ou clic droit sur un fichier de test spécifique > Run 'TestFile'
   - Ou utiliser le raccourci `Ctrl+Shift+F10`

### Types de profils générés
1. Profil lecteur (opérations READ dominantes)
2. Profil écrivain (opérations WRITE dominantes)
3. Profil premium (produits à prix élevé)
