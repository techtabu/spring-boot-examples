# embedded hazelcast

This is to create a distributed cache service using hazelcast to deploy in kubernetes. 

This is based on the [tutorial](https://docs.hazelcast.com/tutorials/kubernetes-embedded) provided by Hazelcast 
itself on how to create a docker image that embed a hazel cast instance in a spring boot application. You can scale 
up or down this application as you need. 

Apart from the base spring boot dependencies, you need the following dependency. 
```xml
<dependency>
    <groupId>com.hazelcast</groupId>
    <artifactId>hazelcast-all</artifactId>
    <version>${hazelcast.version}</version>
</dependency>
```

You will also need a hazelcast.yaml file in src/main/resources directory to enable kubernetes discovery.
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



