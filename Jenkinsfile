pipeline {
    agent any

    stages {
        stage("verify tooling") {
            steps {
                script {
                    sh '''
                        docker version
                        docker info
                        docker-compose version
                        curl --version
                    '''
                }
            }
        }

        stage('Build Project') {
          steps {
            sh 'mvn clean install'
          }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Define the Docker image name and tag
                    def dockerImage = "rozaworks/user-account-api:latest"

                    // Build the Docker image
                    sh "docker build -t ${dockerImage} ."
                }
            }
        }

        stage('Push Docker Image to DockerHub') {
            steps {
                script {
                    // Define the Docker image name and tag
                    def dockerImage = "rozaworks/user-account-api:latest"

                    // Authenticate with Docker Hub
                    sh "docker login -u rozaworks -p n@lA122804"

                    // Push the Docker image to Docker Hub
                    sh "docker push ${dockerImage}"
                }
            }
        }

        stage('Prune Docker data') {
            steps {
                sh 'docker system prune -a --volumes -f'
            }
        }

        stage('Start container') {
            steps {
                sh 'docker-compose up -d --no-color --wait'
                sh 'docker-compose ps'
            }
        }

        stage('Run tests against the container') {
            steps {
                sh 'curl http://localhost:9090'
            }
        }
    }
}
