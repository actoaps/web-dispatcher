pipeline {

	agent { 
		label 'master'
	}

    tools {
        jdk 'jdk11'
    }

    stages {
        stage("gradle build") {
            steps {
                sh './gradlew build -Dsonar.host.url=$SONAR_HOST -Dsonar.login=$SONAR_LOGIN'
                junit allowEmptyResults: true, testResults: '/target/surefire-reports/**/*.xml'
            }
        }

        stage("docker ") {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                        def temp = docker.build('actoaps/web-dispatcher')
                        temp.push('1.0.${BUILD_NUMBER}')
                        temp.push('latest')
                    }
                }
            }
        }
    }
}