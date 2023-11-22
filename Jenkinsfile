pipeline {
  agent any
  stages {
    stage("verify tooling") {
      steps {
        bat '''
          docker version
          docker info
          docker compose version
          curl --version
          '''
      }
    }
    stage('Prune Docker data') {
      steps {
        bat 'docker system prune -a --volumes -f'
      }
    }
    stage('Start container') {
      steps {
        bat 'docker compose up -d --no-color --wait'
        bat 'docker compose ps'
      }
    }
    stage('Run tests against the container') {
      steps {
        bat 'ping -n 30 127.0.0.1 > nul'
        bat 'curl http://localhost:9090'
      }
    }
  }

}