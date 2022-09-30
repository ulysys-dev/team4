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

기본 코드에 아래 내용 추가
1) store >>> infra > PolicyHandler.java
  - getIsOffline() 값으로 분기 처리

2) store >>> domain > Store.java
  - onPostPersist() 주석처리 : 중복 포스팅 발생
  - ifOnlineOrder() paymentCompleted 받은 값 저장
  - ifOfflineOrder() paymentCompleted 받은 값 저장

```
## Order
http POST :8082/orders flowerId=1 qty=2 address="pusan" isOffline=false phoneNumber="01012345678" price="20000"
http POST :8082/orders flowerId=1 qty=1 address="seoul" isOffline=true phoneNumber="01012345678" price="10000"

## Correlation -> 500 에러
http POST :8082/orders flowerId=1 qty=20 address="pusan" isOffline=false phoneNumber="01012345678" price="20000"

## cacel Order
http PUT :8082/orders/1/cancelorder

## CQRS
http :8082/orderHistories

## CQRS
[CQRS 설명](https://www.youtube.com/watch?v=1c1J7dNh4u8)

### AWS Cloud
[AWS 설정](https://labs.msaez.io/#/courses/cna-full/d7337970-32f3-11ed-92da-1bf9f0340c92/#ops-aws-setting)


### git sync
```
git pull

git merge origin/template
```






## MariaDB
[mariaDB 설정](https://github.com/msa-school/ddd-petstore-level6-layered-spring-jpa/blob/main/README.md)




---

#### AWS install
```bash
cd /workspace/aws
sudo ./install

```


####  configure AWS Client 
```bash
aws configure
AWS Access Key ID [None]: AKIA5T5A3YE2OAGFX65X
AWS Secret Access Key [None]: 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
Default region name [None]: ap-northeast-3
Default output format [None]: json

```

### .bashrc

```bash
## K8s Settings
KUBECTL=/workspace/team/kubectl
export PATH=$KUBECTL:$PATH

source <(kubectl completion bash)
alias k=kubectl
complete -o default -F __start_kubectl k
```


### EKS
#### create EKS

```
export myclusterUserid=team4

eksctl create cluster --name ${myclusterUserid} --version 1.21 --spot --managed --nodegroup-name standard-workers --node-type t3.medium --nodes 3 --nodes-min 1 --nodes-max 3
```

#### config EKS Client
```bash
aws eks update-kubeconfig --name ${myclusterUserid}

 kubectl get nodes
NAME                                                STATUS   ROLES    AGE   VERSION
ip-192-168-31-153.ap-northeast-3.compute.internal   Ready    <none>   13m   v1.21.14-eks-ba74326
ip-192-168-45-158.ap-northeast-3.compute.internal   Ready    <none>   13m   v1.21.14-eks-ba74326
ip-192-168-76-44.ap-northeast-3.compute.internal    Ready    <none>   13m   v1.21.14-eks-ba74326
```

### ECR
#### get ECR Credential

```
ECR_URI=936103362868.dkr.ecr.ap-northeast-3.amazonaws.com/team4

export REGION=ap-northeast-3
 $ aws --region $REGION ecr get-login-password 
eyJwYXlsb2FkIjoiNTY1dFZKbmVtdVlTZmpuMEtIdEhQc
... 생략 ..
```

### Docker Login to ECR
```
export ECR_URI=936103362868.dkr.ecr.ap-northeast-3.amazonaws.com/team4
export USER_NM=team4
docker login --username AWS -p $(aws --region $REGION ecr get-login-password) $ECR_URI


WARNING! Using --password via the CLI is insecure. Use --password-stdin.
WARNING! Your password will be stored unencrypted in /home/gitpod/.docker/config.json.
Configure a credential helper to remove this warning. See
https://docs.docker.com/engine/reference/commandline/login/#credentials-store

Login Succeeded
```





### AWS Info
- AWS ID: team4
- REGION: ap-northeast-3
- EKS: 
    - arn:aws:eks:ap-northeast-3:936103362868:cluster/team4
- ECR:
  - 936103362868.dkr.ecr.ap-northeast-3.amazonaws.com/team4



#### install heml
[heml & kafka](https://labs.msaez.io/#/courses/cna-full/d7337970-32f3-11ed-92da-1bf9f0340c92/#ops-utility)
```bash
curl https://raw.githubusercontent.com/helm/helm/master/scripts/get-helm-3 > get_helm.sh
chmod 700 get_helm.sh
./get_helm.sh
```


#### install kafka without namespace
```bash
helm repo update
helm repo add bitnami https://charts.bitnami.com/bitnami
helm install my-kafka bitnami/kafka
```

#### check kafka topics without namespace
```bash
kubectl run my-kafka-client --restart='Never' --image docker.io/bitnami/kafka:2.8.0-debian-10-r0 --command -- sleep infinity

## in container
kubectl exec --tty -i my-kafka-client -- bash

    PRODUCER:
        kafka-console-producer.sh \
            --broker-list my-kafka-0.my-kafka-headless:9092 \
            --topic team

    CONSUMER:
        kafka-console-consumer.sh \
            --bootstrap-server my-kafka:9092 \
            --topic team \
            --from-beginning

        kafka-console-consumer.sh \
        --bootstrap-server my-kafka:9092 \
        --topic team
```

##### login dockerHub
```
 docker login
Login with your Docker ID to push and pull images from Docker Hub. If you don't have a Docker ID, head over to https://hub.docker.com to create one.
Username: ulysysdev
Password: 
WARNING! Your password will be stored unencrypted in /home/gitpod/.docker/config.json.
Configure a credential helper to remove this warning. See
https://docs.docker.com/engine/reference/commandline/login/#credentials-store

Login Succeeded
```

##### build package 
```
export MS_ID=payment

cd /workspace/team4/${MS_ID}

mvn package -B -DskipTests
```

##### build package 
```
export MS_ID=payment
export DOCKER_ID=ulysysdev
export DOCKER_TAG=v1

cd /workspace/team4/${MS_ID}

docker build -t ${DOCKER_ID}/${MS_ID}:${DOCKER_TAG} .
docker run ${DOCKER_ID}/${MS_ID}:${DOCKER_TAG}
```