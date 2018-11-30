
# Jenkins Docker Image

## References

- <https://technologyconversations.com/2017/06/16/automating-jenkins-docker-setup/>
- <https://github.com/jenkinsci/docker>
- <https://github.com/openfrontier/docker-jenkins>
- <https://github.com/vfarcic/docker-flow-stacks/blob/master/jenkins/Dockerfile>
- <https://github.com/khezen/compose-postgres>

## Generate SSH keys, if needed

~~~
mkdir .ssh
cd .ssh

ssh-keygen -t rsa -b 4096 -m PEM -C "gerrit_jenkins@example.org" -f id_rsa
~~~

> ```ssh-keygen``` argument ```-m PEM``` is needed by new versions to create older ```-----BEGIN RSA PRIVATE KEY-----``` formatted keys.  
>
> *i.e* <https://github.com/sshnet/SSH.NET/issues/465>
>
> Without this argument Gerrit Trigger plugin 2.27.7 throws the following error.
>
> Connection error : com.jcraft.jsch.JSchException: invalid privatekey

## Build Image

~~~
docker image build -t jpnewman/jenkins:lts .
~~~

## Test Image

### Start Gerrit Docker Stack

[docker-stack](../docker-stack/README.md)  

-- or --

~~~
docker run -p 8080:8080 -p 50000:50000 --name=jenkins-master -e GERRIT_HOST_NAME=localhost -e GERRIT_FRONT_END_URL=http://localhost -e GERRIT_USERNAME=gerrit_jenkins -d jpnewman/jenkins:lts
~~~

### Get List of plugins

~~~
curl -sSL "http://admin:admin@localhost:8080/pluginManager/api/xml?depth=1&xpath=/*/*/shortName|/*/*/version&wrapper=plugins" | perl -pe 's/.*?<shortName>([\w-]+).*?<version>([^<]+)()(<\/\w+>)+/\1 \2\n/g'|sed 's/ /:/' > _plugins.txt
~~~

### Docker Exec

~~~
docker exec -it $(docker ps -f 'name=jenkins' -q --no-trunc | head -n1) /bin/bash
~~~

### Web

~~~
open http://localhost:8080
~~~

|Service|URL|Username|Password|
|---|---|---|---|
|**Jenkins**|<http://localhost:8080>|admin|admin|

### Stop and Remove Docker Container

~~~
docker rm -f $(docker ps -f 'name=jenkins' -q --no-trunc | head -n1)
~~~

### Delete Docker Image

~~~
docker rmi jpnewman/jenkins:lts
~~~
