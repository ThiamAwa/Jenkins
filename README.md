#  Gestion Établissement — Module 8 CI/CD

<p align="center">
  <img src="https://img.shields.io/badge/work-completed-brightgreen.svg" alt="Work Status"/>
  <img src="https://img.shields.io/badge/Groupeisi-Java-green" alt="Groupe ISI"/>
  <img src="https://img.shields.io/badge/Gestion--Etablissement-SpringBoot-yellowgreen" alt="Spring Boot"/>
  <img src="https://img.shields.io/badge/Java-17-blue" alt="Java 17"/>
  <img src="https://img.shields.io/badge/Maven-3.9.6-blueviolet" alt="Maven"/>
  <img src="https://img.shields.io/badge/Swagger-UI-orange" alt="Swagger"/>
  <img src="https://img.shields.io/badge/PostgreSQL-16-blue" alt="PostgreSQL"/>
  <img src="https://img.shields.io/badge/CI%2FCD-Jenkins-D24939?logo=jenkins&logoColor=white" alt="Jenkins"/>
  <img src="https://img.shields.io/badge/Image-Docker%20Hub-2496ED?logo=docker&logoColor=white" alt="Docker Hub"/>
  <img src="https://img.shields.io/badge/Versioning-GitHub-181717?logo=github&logoColor=white" alt="GitHub"/>
  <img src="https://img.shields.io/badge/Deploy-SSH%20Remote-6C757D?logo=linux&logoColor=white" alt="SSH Deploy"/>
  <img src="https://img.shields.io/badge/Docker%20Image-thiamawa%2Fgl1--jee--module8-2496ED?logo=docker&logoColor=white" alt="Docker Image"/>
</p>

---

> **Auteure :** Awa Thiam  
> **Formation :** GL1 – JEE / DevOps  
> **Module :** 8 – Intégration Continue & Déploiement Continu (CI/CD)  
> **Docker Hub :** `thiamawa/gl1-jee-module8`  
> **Port applicatif :** `8081`

---

##  Description du projet

**Gestion Établissement** est une application web **Spring Boot** permettant de gérer les entités d'un établissement scolaire (étudiants, inscriptions, filières, paiements, etc.).
Le projet intègre un pipeline CI/CD complet avec **Jenkins**, utilisant des agents Docker pour isoler chaque étape, et déploie automatiquement l'application sur un **serveur distant via SSH** après validation manuelle.


##  Structure du projet

![10](10.png)


---

## Fichier `Jenkinsfile`

Le pipeline est **déclaratif**, sans agent global (`agent none`), chaque stage déclare son propre agent Docker pour une isolation maximale.

```groovy
pipeline {
    agent none

    environment {
        IMAGE          = "thiamawa/gl1-jee-module8"
        REMOTE_USER    = "user"
        REMOTE_HOST    = "remote.server.com"
        CONTAINER_NAME = "gl1-jee-module8"
        APP_PORT       = "8081"
    }

    stages {

     
        // STAGE 1 — Tests unitaires
       
        stage('Unit Test') {
            agent {
                docker {
                    image 'maven:3.9.11-eclipse-temurin-17-alpine'
                    args  '-u root -v $HOME/.m2:/root/.m2'
                }
            }
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        
        // STAGE 2 — Build de l'image Docker (multi-stage Dockerfile)
        // Maven est exécuté DANS le Dockerfile
        
        stage('Build Docker Image') {
            agent {
                docker {
                    image 'docker:25.0.3'
                    args  '-u root -v /var/run/docker.sock:/var/run/docker.sock'
                }
            }
            steps {
                sh "docker build -t ${IMAGE}:v${BUILD_NUMBER} ."
            }
        }

       
        // STAGE 3 — Push vers Docker Hub
     
        stage('Push to Docker Hub') {
            agent {
                docker {
                    image 'docker:25.0.3'
                    args  '-u root -v /var/run/docker.sock:/var/run/docker.sock'
                }
            }
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub_credentials',
                    usernameVariable: 'DOCKER_HUB_USERNAME',
                    passwordVariable: 'DOCKER_HUB_PASSWORD'
                )]) {
                    sh """
                        echo "\$DOCKER_HUB_PASSWORD" | docker login -u "\$DOCKER_HUB_USERNAME" --password-stdin
                        docker push ${IMAGE}:v${BUILD_NUMBER}
                        docker logout
                    """
                }
            }
        }

        
        // STAGE 4 — Validation manuelle avant déploiement
     
        stage('Approval') {
            agent none
            steps {
                input message: 'Voulez-vous déployer sur le serveur distant ?',
                      ok: 'Déployer'
            }
        }

      
        // STAGE 5 — Déploiement sur le serveur distant via SSH
      
        stage('Deploy on Remote Server') {
            agent any
            steps {
                sshagent(credentials: ['ssh-remote-server']) {
                    sh """
                        ssh -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_HOST} '
                            docker pull ${IMAGE}:v${BUILD_NUMBER} &&
                            docker stop  ${CONTAINER_NAME} || true &&
                            docker rm    ${CONTAINER_NAME} || true &&
                            docker run -d \\
                                --name ${CONTAINER_NAME} \\
                                -p ${APP_PORT}:${APP_PORT} \\
                                ${IMAGE}:v${BUILD_NUMBER}
                        '
                    """
                }
            }
        }
    }

    // POST — Notifications de fin de pipeline
   
    post {
        success {
            echo "Pipeline réussi — Image déployée : ${IMAGE}:v${BUILD_NUMBER}"
        }
        failure {
            echo 'Pipeline échoué — Vérifier les logs ci-dessus'
        }
        always {
            echo 'Pipeline terminé.'
        }
    }
}


```

## Description des 5 stages

| # | Stage | Agent Docker | Action principale | Particularité |
|---|-------|-------------|-------------------|---------------|
| 1 | **Unit Test** | `maven:3.9.11-eclipse-temurin-17-alpine` | `mvn test` + rapport JUnit | Cache `.m2` monté en volume |
| 2 | **Build Docker Image** | `docker:25.0.3` | `docker build -t IMAGE:vN .` | Socket Docker partagé |
| 3 | **Push to Docker Hub** | `docker:25.0.3` | `docker push IMAGE:vN` | Credentials Jenkins `dockerhub_credentials` |
| 4 | **Approval** | `none` | Gate de validation manuelle | Bloque le pipeline jusqu'à approbation humaine |
| 5 | ** Deploy on Remote Server** | `any` | `ssh` → pull + stop + run | Credentials SSH `ssh-remote-server` |

---

##  Dockerfile multi-stage

Le Dockerfile suit un pattern **multi-stage** pour produire une image de production légère et sécurisée.

```dockerfile
# ─────────────────────────────────────────────
# STAGE 1 : Compilation Maven
# ─────────────────────────────────────────────
FROM maven:3.9.11-eclipse-temurin-17-alpine AS builder

WORKDIR /build

# Cache des dépendances Maven
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Compilation
COPY src ./src
RUN mvn clean package -DskipTests -q

# ─────────────────────────────────────────────
# STAGE 2 : Image de production (légère)
# ─────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine AS production

WORKDIR /app

# Utilisateur non-root pour la sécurité
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copie uniquement le WAR final
COPY --from=builder /build/target/*.war app.war

RUN chown appuser:appgroup app.war

USER appuser

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.war"]
```





##  Mise en route locale

### Prérequis

- Java 17+ installé
- Maven 3.9,6 installé
- PostgreSQL 16 installé et démarré
- Docker installé
- Jenkins LTS avec les plugins : `Docker Pipeline`, `SSH Agent`, `JUnit`, `Blue Ocean`

### 1. Cloner le projet

```bash
git clone https://github.com/thiamawa/gestionEtablissement.git
cd gestionEtablissement
```

### 2. Configurer la base de données

```properties
# src/main/resources/application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/gestion_etablissement_db
spring.datasource.username=postgres
spring.datasource.password=votre_mot_de_passe
spring.jpa.hibernate.ddl-auto=update
server.port=8081
```

### 3. Lancer en local

```bash
mvn spring-boot:run
```

Application : `http://localhost:8081/inscrire`  


---

### 4. Construire et lancer avec Docker

**Étape 1 — Build Maven**

```bash
mvn clean package -DskipTests
```

![1](1.png)

---

**Étape 2 — Build de l'image Docker**

```bash
docker build -t thiamawa/gl1-jee-module8:local .
```

![2(2.png)

---

**Étape 3 — Création du réseau et lancement des conteneurs**

``` bash

docker network create test-network

docker run -d --name postgres \
  --network test-network \
  -e POSTGRES_PASSWORD=passer \
  -e POSTGRES_DB=gestionEtablissement \
  postgres

docker run -d --name gl1-jee-app \
  --network test-network \
  -p 8080:8080 \
  thiamawa/gl1-jee-module8:local
```
---
![3(3.png)
![4(4.png)
![5(5.png)


**Étape 4 — Vérification des logs**

```bash
docker logs -f gl1-jee-app
```

![App Started](docs/screenshots/app-started.png)

>  L'application est correctement démarrée si tu vois la ligne :
![7(7.png)

---

### 5. Lancer depuis Docker Hub

```bash
docker pull thiamawa/gl1-jee-module8:v<BUILD_NUMBER>
docker run -d --name gl1-jee-module8 -p 8081:8081 thiamawa/gl1-jee-module8:v<BUILD_NUMBER>
```

---

## 🔧 Configuration Jenkins — Pas à pas

1. **Nouveau Item** → choisir **Pipeline** → nommer `GestionEtablissement`
2. **Pipeline** → `Pipeline script from SCM`
3. **SCM** → `Git` → renseigner l'URL du dépôt
4. **Script Path** → `Jenkinsfile`
5. Ajouter les credentials `dockerhub_credentials` et `ssh-remote-server`
6. Sauvegarder → **Build Now**

---

##  Bonnes pratiques appliquées

- `agent none` global → chaque stage déclare son propre agent Docker
- Cache Maven `.m2` monté en volume pour accélérer les builds
- Credentials Jenkins pour Docker Hub et SSH (aucun mot de passe en clair)
- Gate de validation manuelle (`input`) avant déploiement en production
- Dockerfile multi-stage pour une image légère et sécurisée
- Exécution en utilisateur non-root dans le conteneur (`appuser`)
- Image versionnée avec `BUILD_NUMBER` pour la traçabilité
- Stop/remove de l'ancien conteneur avant redéploiement

---

## 👩‍💻 Auteure

**Awa Thiam**  
Étudiante en Génie Logiciel – M2GL  
Formation DevOps · Sprint Boot · Module 8  
Groupe ISI

---

## 📄 Licence

Ce projet est réalisé dans un cadre pédagogique.  
© 2024 – Awa Thiam · M2GL Sprint DevOps · Groupe ISI
