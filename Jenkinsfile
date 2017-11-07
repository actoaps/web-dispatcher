pipeline {

	agent { 
		label 'master'
	}

    tools {
        maven 'Maven3'
    }

    stages {
        stage("mvn build") {
            steps {
                sh 'mvn clean install'
            }
        }

        stage ("sonar analysis") {
            steps {
                withSonarQubeEnv('Sonar') {
                    sh "${tool 'SonarScanner'}/bin/sonar-scanner"
                }
            }
        }

        stage ("archive jar") {
            steps {
                archive 'target/*.jar'
                archive 'target/lib/**/*'
            }
        }

        stage("docker build") {
            steps {
                script {
                    docker.build('actoaps/web-dispatcher')
                }
            }
        }

        stage("docker push") {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                        docker.image('actoaps/web-dispatcher').push('latest')
                    }
                }
            }
        }
    }
}