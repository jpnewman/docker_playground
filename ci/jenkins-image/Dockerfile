FROM jenkins/jenkins:lts-alpine

ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false"

USER root
RUN apk update && apk upgrade

USER jenkins

COPY init.groovy.d/ /usr/share/jenkins/ref/init.groovy.d/
COPY --chown=jenkins:jenkins .ssh/ /var/jenkins_home/.ssh/

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt
