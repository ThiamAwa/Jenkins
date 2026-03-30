pipeline {
    agent none

    stages {

        stage('Unit Test') {
            agent {
                docker {
                    image 'maven:3.9.11-eclipse-temurin-17-alpine'
                    args '-u root -v $HOME/.m2:/root/.m2'
                }
            }
            steps {
                sh 'mvn test'
            }
        }

        stage('Build Maven') {
            agent {
                docker {
                    image 'maven:3.9.11-eclipse-temurin-17-alpine'
                    args '-u root -v $HOME/.m2:/root/.m2'
                }
            }
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            agent {
                docker {
                    image 'docker:25.0.3'
                    args '-u root -v /var/run/docker.sock:/var/run/docker.sock'
                }
            }
            steps {
                sh 'docker build -t thiamawa/gl1-jee-module8:v$BUILD_NUMBER .'
            }
        }

        stage('Push to Docker Hub') {
            agent {
                docker {
                    image 'docker:25.0.3'
                    args '-u root -v /var/run/docker.sock:/var/run/docker.sock'
                }
            }
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub_credentials',
                    usernameVariable: 'DOCKER_HUB_USERNAME',
                    passwordVariable: 'DOCKER_HUB_PASSWORD'
                )]) {
                    sh '''
                        echo "$DOCKER_HUB_PASSWORD" | docker login -u "$DOCKER_HUB_USERNAME" --password-stdin
                        docker push thiamawa/gl1-jee-module8:v$BUILD_NUMBER
                    '''
                }
            }
        }

        stage('Deploy on Remote Server') {
            agent any
            steps {
                script {
                    def userInput = input(
                        message: 'Voulez-vous déployer sur le serveur distant ?',
                        ok: 'Déployer'
                    )

                    if (userInput != null) {
                        sh """
                            ssh -i /idrsa -o StrictHostKeyChecking=no user@remote.server.com '
                            docker pull thiamawa/gl1-jee-module8:v$BUILD_NUMBER &&
                            docker stop gl1-jee-module8 || true &&
                            docker rm gl1-jee-module8 || true &&
                            docker run -d --name gl1-jee-module8 -p 8080:8080 thiamawa/gl1-jee-module8:v$BUILD_NUMBER
                            '
                        """
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline exécuté avec succès'
        }
        failure {
            echo ' Pipeline échoué'
        }
    }
}