
# CI Docker Stack

## Create Jenkins Docker Image

[jenkins-image](../jenkins-image/README.md)  

## Init

~~~
make
~~~

## Get Service Logs

> LDAP

~~~
make logs SERVICE=ldap
~~~

## List Services

~~~
make info
~~~

## Web UIs

|Service|URL|Username|Password|
|---|---|---|---|
|**Gerrit**|<http://localhost:8686/>|```gerrit_jenkins```|```password123```|
|**Jenkins**|<http://localhost:8080/>|```admin```|```admin```|
|**Weaveworks scope**|<http://localhost:4040>|||
|**phpldapadmin**|<http://localhost:8282/>|```cn=admin,dc=example,dc=org```|```admin```|
|**pgadmin4**|<http://localhost:8484/>|```postgres.admin@example.org```|```password123```|

## Jenkins Gerrit Trigger Configuration

- <https://wiki.jenkins.io/display/JENKINS/Gerrit+Trigger>

1. Add ```../jenkins-image/.ssh/id_rsa.pub``` key to Gerrit user ```gerrit_jenkins```.

2. Create new Group (```People > Create New Group```) ```Event Streaming Users``` and add user ```gerrit_jenkins``` to it, if needed.

3. Allow new group ```Event Streaming Users``` to ```Stream Events``` for ```All-Projects```.

    - ```Projects > All-Projects > Access > Edit```
    - ```Global Capabilities```
    - ```Stream Events``` **ALLOW** for ```Event Streaming Users```
