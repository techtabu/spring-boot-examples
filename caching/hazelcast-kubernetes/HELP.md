# Hazelcast Cluster on Kubernetes

## standing up containers

Just use the docker-compose.yaml file in hazelcast directory. 

Props

* `HZ_NETWORK_PUBLICADDRESS` \

This should be the IP address of your computer. If not specified, clients will try to connect to ip address of the 
docker container, which are not reachable from outside of docker container/ network. So, you will see a lot of 
"unable to reach" exceptions in you application logs. This also should not be `127.0.0.1/ localhost` since it would 
be the localhost to the docker container, not to your computer. 

