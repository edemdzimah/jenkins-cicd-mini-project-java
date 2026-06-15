// Java + Maven version of the CI/CD mini project.
//
// Compare this with the Python Jenkinsfile: the SHAPE is the same
// (checkout, build, test, package, image). Only the commands change,
// because Java is a COMPILED language and uses Maven as its build tool.
//
// Note the extra Package stage. Python had no separate package step
// because it is interpreted. Java must be compiled and packaged into
// a JAR before it can run, so that step is visible here.

pipeline {
    agent any

    environment {
        IMAGE_NAME = 'cicd-mini-project-java'
        IMAGE_TAG  = "${BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Stage 1: Getting the source code'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo 'Stage 2: Compiling the code with Maven'
                dir('app') {
                    sh 'mvn -B -ntp clean compile'
                }
            }
        }

        stage('Test') {
            steps {
                echo 'Stage 3: Running the unit tests with Maven'
                dir('app') {
                    sh 'mvn -B -ntp test'
                }
            }
            post {
                always {
                    // Maven writes test reports here; the JUnit plugin displays them.
                    junit 'app/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                echo 'Stage 4: Packaging the app into a runnable JAR'
                dir('app') {
                    sh 'mvn -B -ntp package -DskipTests'
                }
            }
        }

        stage('Docker Build') {
            steps {
                echo 'Stage 5: Building the Docker image from the JAR'
                dir('app') {
                    sh 'docker build -t ${IMAGE_NAME}:${IMAGE_TAG} -t ${IMAGE_NAME}:latest .'
                }
                sh 'docker images | grep ${IMAGE_NAME}'
            }
        }
    }

    post {
        success {
            echo "Pipeline succeeded. Built image ${IMAGE_NAME}:${IMAGE_TAG}"
        }
        failure {
            echo 'Pipeline failed. Scroll up and read the first red error in the log.'
        }
        always {
            echo 'Pipeline finished.'
        }
    }
}
