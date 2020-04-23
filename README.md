JGroups LockService for Wildfly
==============================

Build
==============================

```shell script
mvn clean package
```                           

Test
==============================

1. Run in one terminal window

```shell script
mvn wildfly:run -Dwildfly.build.directory=target/server0 -Dwildfly.serverArgs=-Djboss.socket.binding.port-offset=0 -Dwildfly.port=9990
```    

2. In second one terminal window run

```shell script
mvn wildfly:run -Dwildfly.build.directory=target/server1 -Dwildfly.serverArgs=-Djboss.socket.binding.port-offset=1 -Dwildfly.port=9991
```      

3. In third one terminal window run

```shell script
curl http://localhost:8080/lock
curl http://localhost:8081/lock
```                                                                  

