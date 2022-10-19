# Hazelcast Cluster on Kubernetes

## hazel cast with docker

Just use the docker-compose.yaml file in hazelcast directory. 

Props

* `HZ_NETWORK_PUBLICADDRESS` \

This should be the IP address of your computer. If not specified, clients will try to connect to ip address of the 
docker container, which are not reachable from outside of docker container/ network. So, you will see a lot of 
"unable to reach" exceptions in you application logs. This also should not be `127.0.0.1/ localhost` since it would 
be the localhost to the docker container, not to your computer. 

## hazelcast with kubernetes
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

delete all
```shell
kubectl delete -f hazelcast.yaml
kubectl delete -f hc_mc.yaml
kubectl delete -f https://repository.hazelcast.com/operator/bundle-5.4.yaml
```