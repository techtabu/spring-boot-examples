# Hazelcast Cluster on Kubernetes

# running hazelcast
## with docker

Just use the docker-compose.yaml file in hazelcast directory. 

First change the following properties,
* `HZ_NETWORK_PUBLICADDRESS` to the IP address of your computer.
* `MC_DEFAULT_CLUSTER_MEMBERS`: IP of your computer.

This should be the IP address of your computer. If not specified, clients will try to connect to ip address of the
docker container, which are not reachable from outside of docker container/ network. i.e. you application will not 
be able to reach hazelcast. So, you will see a lot of "unable to reach" exceptions in you application logs. This 
also should not be `127.0.0.1/ localhost` since it would be the localhost to the docker container, not to your computer.

Then execute,

```shell
cd docker/hazelcast
docker-compouse up -d
```
The above will stand up 3 instance of hazelcast and single instance of management center. 

Then you can run the application with `mvn spring-boot:run`. 

Alternatively, you can run all applications in docker. In this case, you don't have to change 
`HZ_NETWORK_PUBLICADDRESS`.
```shell
# build the docker images
cd hazelcast-kubernetes
mvn clean install -Pdocker-build

# stand up all in docker
cd hazelcast-kubernetes/docker/all-doc
docker-compose up -d
```


## hazelcast in kubernetes

### standing up hazelcast manually,
First, Hazelcast uses kubernetes API for auto-discovery, So, you need grant certain roles to your service account. 
You can do so, by executing the following command,
```shell
kubectl apply -f https://raw.githubusercontent.com/hazelcast/hazelcast/master/kubernetes-rbac.yaml
```

And then you can run the following commands to stand up three members in Hazelcast cluster in the `defacult` 
namespace using `default` service account. 
```shell
kubectl run hazelcast-1 --image=hazelcast/hazelcast:5.1.4
kubectl run hazelcast-2 --image=hazelcast/hazelcast:5.1.4
kubectl run hazelcast-3 --image=hazelcast/hazelcast:5.1.4
```


### deploy via platform
[official docs](https://docs.hazelcast.com/operator/latest/get-started)

start the platform operator
```shell
kubectl apply -f https://repository.hazelcast.com/operator/bundle-5.4.yaml

# check logs
kubectl logs deployment.apps/hazelcast-platform-controller-manager
```

start hazelcast cluster
```shell
cd ./docker/hc-platform
kubectl -f hazelcast.yaml apply

# logs
kubectl logs pod/hazelcast-sample-0
```

start the management center
```shell
kubectl -f hc_mc.yaml apply

# logs
kubectl logs pod/managementcenter-sample-0
```

start the application
```shell
kubectl apply -f hc_kub.yaml
```

delete all
```shell
kubectl delete -f hazelcast.yaml
kubectl delete -f hc_mc.yaml
kubectl delete -f https://repository.hazelcast.com/operator/bundle-5.4.yaml
```

# configuring client
Client configuration are set up via, `com.hazelcast.client.config.ClientConfig` class of Hazelcast. Several version 
of hosts configuration,

* if you run hazelcast in docker, and application elsewhere 
  * IP address of your computer
* If you are running hazelcast instances and application in docker,
  * service-name or container_name or hostname of the container
* If you are running everything in kubernetes,
  * service-name of the hazelcast instance (if both are in same namespace)
  * <service-name>.<namespace> if hazelcast and application run in different namespace.

Even if you are running everything in kubernetes, you don't have to enable kubernetes in Client's Network 
Configuration. I am not sure, if doing so provides any benefit, but as long as you set the hosts to proper service 
name of the hazelcast instance, you don't have to provide kubernetes specific configuration in the client. 