pipeline {

	agent { 
		label 'master'
	}

    tools {
        maven 'Maven3'
        jdk 'jdk9'
    }

    stages {
        stage("mvn build") {
            steps {
                sh 'mvn clean install'
                junit allowEmptyResults: true, testResults: '/target/surefire-reports/**/*.xml'

            }
        }

        stage ("sonar analysis") {
            steps {
                withSonarQubeEnv('Sonar') {
                    sh "${tool 'SonarScanner'}/bin/sonar-scanner"
                }
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
                        docker.image('actoaps/web-dispatcher').push('1.0.' + env.BUILD_NUMBER).push('latest')
                    }
                }
            }
        }
    }
}