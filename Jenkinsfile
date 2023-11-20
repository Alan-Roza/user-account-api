pipeline {
    agent any

    stages {
        stage("Verify tooling") {
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

        stage('Pull and start container') {
            steps {
                script {
                    // Pull the Docker image from Docker Hub
                    sh "docker pull rozaworks/user-account-api"

                    // Start the container using the pulled image
                    sh 'docker run -d --name account-api -p 9090:9090 rozaworks/user-account-api'
                }
            }
        }

        stage('Run tests against the container') {
            steps {
                script {
                    // Wait for the container to be ready (adjust as needed)
                    sleep time: 30, unit: 'SECONDS'

                    // Run tests against the running container
                    sh 'curl http://localhost:9090'
                }
            }
        }
    }
}
