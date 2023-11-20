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



        stage('Build Docker Image') {
            steps {
                script {
                    dockerImage = docker.build("rozaworks/user-account-api:${env.BUILD_ID}")
                }
            }
        }

        stage('Push Docker Image to DockerHub') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'DockerHub') {
                        dockerImage.push('lastest')
                        dockerImage.push("${env.BUILD_ID}")
                    }
                }
            }
        }
    }
}
