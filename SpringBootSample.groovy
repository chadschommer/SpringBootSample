import groovy.json.JsonSlurper
import java.text.SimpleDateFormat

def repo = "git@github.com:schommer21/SpringBootSample.git"
def label = "worker-${UUID.randomUUID().toString()}"

def dockerbuild() {
    withDockerRegistry('https://docker.io', 'docker-registry-personal') {
        sh """
        docker build -t schommer21/springboot-sample:${env.BUILD_NUMBER} -f Dockerfile .
        docker push schommer21/springboot-sample:${env.BUILD_NUMBER}
        """
    }
}

podTemplate(label: label, containers: [
        containerTemplate(name: 'docker-build', image: 'docker', command: 'cat', ttyEnabled: true, resourceRequestCpu: '50m', resourceLimitCpu: '100m', resourceRequestMemory: '500Mi', resourceLimitMemory: '1000Mi')
],
        volumes: [hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock')]
) {
    node(label) {
        stage('Build Container') {
            container('docker-build') {
                git url: repo, credentialsId: 'personal-ssh-github', branch: 'master'
                withCredentials([sshUserPrivateKey(credentialsId: 'personal-ssh-github', keyFileVariable: 'GIT_KEY')]) {
                    withEnv(["GIT_SSH_COMMAND=ssh -i $GIT_KEY -o StrictHostKeyChecking=no"]) {
                        dockerbuild()
                        // builtImage = docker.build("springboot-sample:${env.BUILD_NUMBER}")
                    }
                }

                // docker.withRegistry('https://docker.io', 'docker-registry-personal') {
                //     builtImage.push()
                // }
            }
        }
    }
}
