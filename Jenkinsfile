pipeline {
    agent any

    tools {
        maven 'Maven 3.8.1'
        jdk 'Java 17'
    }

    environment {
        SONAR_HOST_URL = credentials('sonar-host-url')
        SONAR_LOGIN = credentials('sonar-token')
    }

    stages {
        stage('Checkout') {
            steps {
                echo '━━━ Étape 1: Checkout ━━━'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo '━━━ Étape 2: Build Maven ━━━'
                sh 'mvn clean compile'
            }
        }

        stage('Tests') {
            steps {
                echo '━━━ Étape 3: Tests Unitaires ━━━'
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Coverage') {
            steps {
                echo '━━━ Étape 4: JaCoCo Report ━━━'
                sh 'mvn jacoco:report'
            }
        }

        stage('SonarQube') {
            steps {
                echo '━━━ Étape 5: SonarQube Scan ━━━'
                sh '''
                    mvn sonar:sonar \
                        -Dsonar.host.url=${SONAR_HOST_URL} \
                        -Dsonar.login=${SONAR_LOGIN} \
                        -Dsonar.projectKey=voting-system
                '''
            }
        }

        stage('Package') {
            steps {
                echo '━━━ Étape 6: Package ━━━'
                sh 'mvn package -DskipTests'
            }
            post {
                always {
                    archiveArtifacts artifacts: 'target/*.jar', allowEmptyArchive: true
                }
            }
        }
    }

    post {
        always {
            echo '✅ Pipeline finished'
        }
        failure {
            echo '❌ Pipeline failed'
        }
    }
}
