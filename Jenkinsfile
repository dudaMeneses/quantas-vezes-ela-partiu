pipeline {
    agent any

    tools {
        jdk "Java-11"
        maven "Maven-3.5.3"
    }

    stages {
        stage('Clone Project') {
            git url: "https://github.com/dudaMeneses/quantas-vezes-ela-partiu"
        }

        stage('SonarQube Analysis'){
            steps {
                withSonarQubeEnv('SonarQube6.3'){
                    bat 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.3.0.603:sonar'
                }
            }
        }

        stage('Artifactory Upload') {
            steps {
                script {
                    rtMaven.tool = 'Maven-3.5.3'
                    rtMaven.deployer releaseRepo 'libs-release-local', snapshotRepo 'lobs-snapshot-local', server: server
                    rtMaven.resolver releaseRepo: 'libs-release', snapshotRepo: 'libs-snapshot', server: server
                    rtMaven.deployer.artifactDeploymentPatterns.addExclude("pom.xml")
                    buildInfo = Artifactory.newBuildInfo()
                    buildInfo.retention maxBuilds: 10, maxDays: 7, deleteBuildArtifacts: true
                    buildInfo.env.capture = true
                }
            }
        }

        stage('Execute Maven') {
            steps {
                script {
                    rtMaven.run pom: 'pom.xml', goals: 'clean install', buildInfo: buildInfo
                }
            }
        }

        stage('Publish Build Info') {
            steps {
                script {
                    server.publishBuildInfo buildInfo
                }
            }
        }
    }
}