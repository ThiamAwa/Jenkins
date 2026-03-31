pipeline {
    agent none

    environment {
        IMAGE         = "thiamawa/gl1-jee-module8"
        REMOTE_USER   = "user"
        REMOTE_HOST   = "remote.server.com"
        CONTAINER_NAME = "gl1-jee-module8"
        APP_PORT      = "8081"
    }

    stages {

        // ============================================================
        // STAGE 1 — Tests unitaires
        // ============================================================
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

        // ============================================================
        // STAGE 2 — Build de l'image Docker (multi-stage Dockerfile)
        // Maven est exécuté DANS le Dockerfile
        // ============================================================
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

        // ============================================================
        // STAGE 3 — Push vers Docker Hub
        // ============================================================
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

        // ============================================================
        // STAGE 4 — Validation manuelle avant déploiement
        // ============================================================
        stage('Approval') {
            agent none
            steps {
                input message: 'Voulez-vous déployer sur le serveur distant ?',
                      ok: 'Déployer'
            }
        }

        // ============================================================
        // STAGE 5 — Déploiement sur le serveur distant via SSH
        // ============================================================
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

    // ============================================================
    // POST — Notifications de fin de pipeline
    // ============================================================
    post {
        success {
            echo "✅ Pipeline réussi — Image déployée : ${IMAGE}:v${BUILD_NUMBER}"
        }
        failure {
            echo '❌ Pipeline échoué — Vérifier les logs ci-dessus'
        }
        always {
            echo 'Pipeline terminé.'
        }
    }
}