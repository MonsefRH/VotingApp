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
                sh 'mvn clean compile'
            }
        }

        stage('Tests') {
            steps {
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
                sh 'mvn jacoco:report'
            }
        }

        stage('SonarQube') {
            steps {
                sh """
                mvn sonar:sonar ^
                  -Dsonar.projectKey=voting-system ^
                  -Dsonar.host.url=http://sonarqube:9000 ^
                  -Dsonar.login=%SONAR_TOKEN%
                """
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
            }
            post {
                always {
                    archiveArtifacts artifacts: 'target/*.jar'
                }
            }
        }
    }
}
