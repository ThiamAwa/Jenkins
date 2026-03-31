# 🚀 GL1_JEE – Module 8 : CI/CD avec Jenkins & Spring Boot

> **Auteure :** Awa Thiam  
> **Formation :** GL1 – JEE / DevOps  
> **Module :** 8 – Intégration Continue & Déploiement Continu (CI/CD)  
> **Outils :** Jenkins · Spring Boot · GitHub · Maven · Docker *(optionnel)*

---

## 📋 Objectifs du TP

Ce projet a pour objectif de mettre en place un pipeline CI/CD complet en utilisant **Jenkins** et un projet **Spring Boot**, hébergé sur un dépôt Git distant (GitHub / GitLab / Bitbucket).

Les compétences visées sont :

- Créer et configurer un dépôt distant (GitHub, GitLab ou Bitbucket)
- Pusher un projet Spring Boot existant sur ce dépôt
- Rédiger un fichier `Jenkinsfile` déclaratif pour automatiser le build, les tests et le déploiement
- Comprendre le fonctionnement d'un pipeline Jenkins (stages, steps, agents)

---

## 🛠️ Stack technique

| Composant        | Technologie               |
|-----------------|---------------------------|
| Langage          | Java 17+                  |
| Framework        | Spring Boot 3.x           |
| Build tool       | Maven                     |
| CI/CD            | Jenkins (Pipeline déclaratif) |
| Versioning       | Git – GitHub / GitLab / Bitbucket |
| Conteneurisation | Docker *(optionnel)*      |
| Tests            | JUnit 5 · Mockito         |

---

## 📁 Structure du projet

```
GL1_JEE__AWA_THIAM_MODULE8/
│
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   └── DemoApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/example/demo/
│           └── DemoApplicationTests.java
│
├── Jenkinsfile          ← Pipeline CI/CD Jenkins
├── pom.xml              ← Configuration Maven
├── Dockerfile           ← (optionnel) Image Docker
└── README.md            ← Ce fichier
```

---

## ⚙️ Fichier `Jenkinsfile`

Le pipeline Jenkins est défini de manière **déclarative** et comprend les étapes suivantes :

```groovy
pipeline {
    agent any

    tools {
        maven 'Maven 3.9'
        jdk   'JDK 17'
    }

    environment {
        APP_NAME    = 'gl1-jee-awa-thiam'
        DEPLOY_ENV  = 'staging'
    }

    stages {

        stage('📥 Checkout') {
            steps {
                checkout scm
                echo "✅ Code récupéré depuis le dépôt Git"
            }
        }

        stage('🔨 Build') {
            steps {
                sh 'mvn clean package -DskipTests'
                echo "✅ Build Maven terminé"
            }
        }

        stage('🧪 Tests unitaires') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('📊 Analyse qualité (SonarQube)') {
            when {
                branch 'main'
            }
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar'
                }
                echo "✅ Analyse SonarQube terminée"
            }
        }

        stage('📦 Packaging') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                echo "✅ Artefact archivé"
            }
        }

        stage('🐳 Build Docker Image') {
            when {
                branch 'main'
            }
            steps {
                sh "docker build -t ${APP_NAME}:${BUILD_NUMBER} ."
                echo "✅ Image Docker construite"
            }
        }

        stage('🚀 Déploiement') {
            when {
                branch 'main'
            }
            steps {
                echo "🚀 Déploiement sur l'environnement : ${DEPLOY_ENV}"
                sh "java -jar target/*.jar &"
            }
        }
    }

    post {
        success {
            echo "🎉 Pipeline terminé avec succès !"
        }
        failure {
            echo "❌ Le pipeline a échoué. Vérifiez les logs."
            // mail to: 'awa.thiam@example.com', subject: "Échec pipeline ${JOB_NAME}", body: "Build #${BUILD_NUMBER} a échoué."
        }
        always {
            cleanWs()
        }
    }
}
```

---

## 🚦 Étapes du pipeline

```
Checkout → Build → Tests → Qualité → Packaging → Docker → Déploiement
```

| Stage | Description |
|-------|-------------|
| **Checkout** | Récupération du code source depuis le dépôt Git |
| **Build** | Compilation du projet avec `mvn clean package` |
| **Tests** | Exécution des tests unitaires JUnit |
| **Qualité** | Analyse statique du code avec SonarQube *(branche main)* |
| **Packaging** | Archivage du fichier `.jar` produit |
| **Docker** | Construction de l'image Docker *(branche main)* |
| **Déploiement** | Démarrage de l'application sur l'environnement cible |

---

## 🚀 Mise en route

### Prérequis

- Java 17+ installé
- Maven 3.9+ installé
- Jenkins configuré avec les plugins : `Git`, `Pipeline`, `Maven Integration`, `JUnit`
- Un compte GitHub / GitLab / Bitbucket

### 1. Cloner le projet

```bash
git clone https://github.com/<votre-username>/GL1_JEE__AWA_THIAM_MODULE8.git
cd GL1_JEE__AWA_THIAM_MODULE8
```

### 2. Lancer l'application en local

```bash
mvn spring-boot:run
```

L'application sera disponible sur : `http://localhost:8080`

### 3. Exécuter les tests

```bash
mvn test
```

### 4. Construire le JAR

```bash
mvn clean package
java -jar target/*.jar
```

---

## 🔧 Configuration Jenkins

1. Depuis le tableau de bord Jenkins, cliquer sur **"Nouveau Item"**
2. Choisir **"Pipeline"** et nommer le projet
3. Dans la section **Pipeline**, sélectionner `Pipeline script from SCM`
4. Renseigner l'URL du dépôt Git
5. Définir le **Script Path** : `Jenkinsfile`
6. Sauvegarder et lancer le build avec **"Build Now"**

---

## 📌 Bonnes pratiques appliquées

- ✅ Pipeline **déclaratif** Jenkins (lisible et maintenable)
- ✅ Utilisation de la directive `when` pour conditionner certains stages
- ✅ Publication automatique des rapports de tests JUnit
- ✅ Nettoyage du workspace après chaque exécution (`cleanWs()`)
- ✅ Variables d'environnement centralisées dans le bloc `environment`
- ✅ Notifications de succès/échec dans le bloc `post`

---

## 👩‍💻 Auteure

**Awa Thiam**  
Étudiante en Génie Logiciel – GL1  
Formation DevOps · JEE · Module 8

---

## 📄 Licence

Ce projet est réalisé dans un cadre pédagogique.  
© 2024 – Awa Thiam · GL1 JEE DevOps
