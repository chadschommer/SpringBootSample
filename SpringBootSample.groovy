import groovy.json.JsonSlurper
import java.text.SimpleDateFormat

def branch = "master"
def repo = "https://github.com/schommer21/SpringBootSample.git"
def label = "worker-${UUID.randomUUID().toString()}"

def dockerbuild() {
    sh """
    docker build --no-cache --network=host -t schommer21/springboot-sample:dev -f Dockerfile .
    docker push schommer21/springboot-sample:dev
    """
}

podTemplate(label: label, containers: [
        containerTemplate(name: 'docker-build', image: '816004290214.dkr.ecr.us-east-1.amazonaws.com/docker-build', command: 'cat', ttyEnabled: true, resourceRequestCpu: '50m', resourceLimitCpu: '100m', resourceRequestMemory: '500Mi', resourceLimitMemory: '1000Mi')
],
        volumes: [hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock')]
) {
    node(label) {
        stage('Build Container') {
            container('docker-build') {
                git url: repo, credentialsId: 'personal-github', branch: branch {
                    withEnv(["GIT_SSH_COMMAND=ssh -i $GIT_KEY -o StrictHostKeyChecking=no"]) {
                        dockerbuild()
                    }
                }
            }
        }
    }
}
