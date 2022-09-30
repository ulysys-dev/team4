## kafka consumer 접속
```
WORKSPACE=/workspace/team4
cd $WORKSPACE

docker-compose exec -it kafka /bin/bash
cd /bin
./kafka-console-consumer --bootstrap-server localhost:9092 --topic team
```


## Service Port

| microSerivce | portNum |
|:-------------|:--------|
| deliveries    | 8081   |
| orders        | 8082   |
| stores        | 8083   |
| payments      | 8084   |
| notify        | 8085   |


# start Shell

```bash
#!/bin/bash

WORKSPACE=/workspace/team4

cd $WORKSPACE
cd delivery
mvn spring-boot:run

cd $WORKSPACE
cd Notify
mvn spring-boot:run

cd $WORKSPACE
cd store
mvn spring-boot:run

cd $WORKSPACE
cd payment
mvn spring-boot:run

cd $WORKSPACE
cd order
mvn spring-boot:run

cd $WORKSPACE
cd kafka
docker-compose up
```

# stop Shell

```bash
# kill delivery
fuser -k 8081/tcp

# kill order
fuser -k 8082/tcp

# kill store
fuser -k 8083/tcp

# kill payment
fuser -k 8084/tcp

# kill notify
fuser -k 8086/tcp
```


## Tests

```
## payments

http :8084/payments orderId=1 qty=3 status="PAYED"
http :8084/payments


## Store
http :8083/stores flowerId=1 flowerCnt=2 flowerPrice=500
http :8083/stores flowerId=2 flowerCnt=5 flowerPrice=1500
http :8083/stores flowerId=3 flowerCnt=8 flowerPrice=4000
http :8083/stores flowerId=4 flowerCnt=12 flowerPrice=6000
http :8083/stores flowerId=5 flowerCnt=1 flowerPrice=1000

```

## CQRS
[CQRS 설명](https://www.youtube.com/watch?v=1c1J7dNh4u8)

### AWS Cloud
[AWS 설정](https://labs.msaez.io/#/courses/cna-full/d7337970-32f3-11ed-92da-1bf9f0340c92/#ops-aws-setting)


```

eksctl create cluster --name team4 --version 1.21 --spot --managed --nodegroup-name standard-workers --node-type t3.medium --nodes 3 --nodes-min 1 --nodes-max 3
```


### git sync
```
git pull

git merge origin/template
```

## MariaDB
[mariaDB 설정](https://github.com/msa-school/ddd-petstore-level6-layered-spring-jpa/blob/main/README.md)