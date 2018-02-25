#!groovy

@Library('Roche_pipeline_library@development') _

node {

  stage('PreBuild') {
    pre_Build{}
  }
  stage('Build') {
    code_Build{}
  }
  stage('Sonar') {
    sonar_Test{}
  }
}