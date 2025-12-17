pipeline {
    agent any

    environment {
        MAVEN_OPTS = "-Xmx1024m"
        SONAR_HOST_URL = "http://sonarqube:9000"
        SONAR_TOKEN = credentials('sonar-token')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean compile'
            }
        }

        stage('Tests') {
            steps {
                bat 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Coverage') {
            steps {
                bat 'mvn jacoco:report'
            }
        }

        stage('SonarQube') {
            steps {
                bat """
                mvn sonar:sonar ^
                  -Dsonar.projectKey=voting-system ^
                  -Dsonar.host.url=http://sonarqube:9000 ^
                  -Dsonar.login=%SONAR_TOKEN%
                """
            }
        }

        stage('Package') {
            steps {
                bat 'mvn package -DskipTests'
            }
            post {
                always {
                    archiveArtifacts artifacts: 'target/*.jar'
                }
            }
        }
    }
}
