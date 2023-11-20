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
