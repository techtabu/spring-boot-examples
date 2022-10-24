# embedded hazelcast

This is to create a distributed cache service using hazelcast to deploy in kubernetes. 

This is based on the [tutorial](https://docs.hazelcast.com/tutorials/kubernetes-embedded) provided by Hazelcast 
itself on how to create a docker image that embed a hazel cast instance in a spring boot application. You can scale 
up or down this application as you need. 

Apart from the base spring boot dependencies, you need the following dependency. 
```xml
<dependency>
    <groupId>com.hazelcast</groupId>
    <artifactId>hazelcast</artifactId>
    <version>${hazelcast.version}</version>
</dependency>
```

> **hazelcast 5.x changes**: Since hazelcast 5.x release, the hazelcast jar itself include all necessary 
> dependencies. You don't have to use hazelcast-all or hazelcast-kubernetes jars.  

You will also need a hazelcast.yaml file in src/main/resources directory which can be used to configure hazelcast 
instance.
````yaml
hazelcast:
  cluster-name: sbe-cache
  network:
    join:
      multicast:
        enabled: false
      kubernetes:
        enabled: true
````

Alternatively, you can configure the server via Java. Refer to the `HazelcastConfig` class for equivalent 
configuration. Now, hazelcast.yaml file can be found in docker directory.

After that, you have to create the docker image by executing,
```shell
mvn clean install -Pdocker-build
```

If you are using minikube environment, make sure you execute the following command to make sure images are available 
in the minikube environment.

```shell
eval $(minikube -p minikube docker-env)
```

The above command will create an image with,
```shell
techtabu/sbe/hazelcast-kubernetes:latest
```

To test the application, you can use `hazelcast-kubernetes` application. Again execute,
```shell
cd ../hazelcast-kubernetes
mvn clean install -Pdocker-build
```

You may have execute following command to grant Hazelcast kubernetes discovery plugin specific ClusterRole to enable 
discovery by using Kubernetes API.
```shell
kubectl apply -f https://raw.githubusercontent.com/hazelcast/hazelcast-kubernetes/master/rbac.yaml
```
if you don't, you will get following error message,
```
Kubernetes API access is forbidden! Starting standalone. To use Hazelcast Kubernetes discovery, configure the required RBAC. 
For 'default' service account in 'default' namespace execute: 
`kubectl apply -f https://raw.githubusercontent.com/hazelcast/hazelcast/master/kubernetes-rbac.yaml`
```

Once both images are built, you can deploy applications in kubernetes by,
```shell
cd ../embedded-hazelcast/docker/kub
kubectl apply -f .
```

Above will deploy, three instances of embedded-hazelcast, once instance of hazelcast-kubernetes and hazelcast 
management center. Credentials for management center is `admin/hazelcast01`. Inside the management center, you will 
three members of hazelcast cluster and one client (hazelcast-kubernetes).

I have used minikube environment on my Mac. To access swagger url of the hazelcast-kubernetes application, execute 
the following command. 
```shell
minikube service hc-kub
```

It will open a browser tab with a url like `http://127.0.0.1:63596`. Add, `/swagger-ui/index.html` to look like 
`http://127.0.0.1:65448/swagger-ui/index.html`. You can make post request using swagger from url with book object, 
which will save the book object in embedded H2 database. Then when you make a get request, `hazelcast/name/<book-name>`, you will see 
an entry added to hazelcast map. Also, you will see the request make and sql entry to find the book. However, when 
you make subsequent request using the name, you will not see sql entry in the logs. The object will be returned from 
the hazelcast cache. 

Once all done and tested, you can clean up the deployment by,
```shell
cd embedded-hazelcast/docker/kub
kubectl delete -f .
```
This will remove all the deployments and services from kubernetes. 


# embedded hazelcast configuration
## Discovering members 

There are two ways you can tell your hazelcast to discover members. 
1. Use Kubernetes API
2. Use Kubernetes DNS lookup mode

If you use Kubernetes API, then you need to run additional configuration to add cluster role binding to grant 
permission to use Kubernetes API.

On the other hand, if you use DNS look up method, it is not required. To use DNS look up mode, instead of kubernetes 
API add following property to hazelcast.xml. 

```yaml
hazelcast:
  network:
      kubernetes:
        enabled: true
        service-dns: sbe-hazelcast.default.svc.cluster.local
```

In most of the case, the service dns is `SERVICE-NAME.NAMESPACE.svc.cluster.local`.

When you are using DNS lookup, make sure `clusterIP=None` in service definition
```yaml
spec:
  type: ClusterIP
  clusterIP: None
```

# hazelcast in separate namespace
A common use case is run hazelcast in separate namespace than the namespace in which your services are running. For 
that, first you need to create a namespace. create a file called, namespace.yaml.
```yaml
{
    "apiVersion": "v1",
    "kind": "Namespace",
    "metadata": {
      "name": "hazelcast",
      "labels": {
        "name": "hazelcast"
      }
    }
  }
```
then run, the command, `kubectl create -f namespace.yaml`.

Then modify the hazelcast.yaml, and hc_mc.yaml files to reflect this namespace (look inside docker/hc-namespace 
directory). The file at `https://raw.githubusercontent.com/hazelcast/hazelcast-kubernetes/master/rbac.yaml` is set 
to work with `default` namespace, you need to modify this file to update your name space. Look at 
`docker/hc-namespace/roles/kubernetes-rbac-hazelcast.yaml` file. In essence,  you have to add this 
section,
```yaml
subjects:
  - kind: ServiceAccount
    name: default
    namespace: hazelcast
```
Then you need to update your hosts of Network config file of your application to include the namespace. Like, 
`<service-name>.<namespace>`
```yaml
cache:
  client:
    hosts: sbe-hazelcast.hazelcast
```
