## kafka consumer 접속
```
WORKSPACE=/workspace/team4
cd $WORKSPACE/kafka

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
```
http POST :8082/orders flowerId=1 qty=2 address="pusan" isOffline=false phoneNumber="01012345678" price="20000"
http POST :8082/orders flowerId=1 qty=1 address="seoul" isOffline=true phoneNumber="01012345678" price="10000"
```

## Correlation -> 500 에러
```
http POST :8082/orders flowerId=1 qty=20 address="pusan" isOffline=false phoneNumber="01012345678" price="20000"
```

## cacel Order
```
http PUT :8082/orders/1/cancelorder
```

## CQRS
```
http :8082/orderHistories
```

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

# 가. Kubernetes Cluster 설치
## 1. AWS cli Client 설치
### 1.1 download and install AWS cli
```bash
## 다음은 init.sh 에 포함되어 있기 때문에 생략 가능합니다.
## awscliv2.zip을 현재 작업하는 pwd 의 상위 디렉토리에 download 받는 것에 주의 바랍니다.
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "../awscliv2.zip"
unzip ../awscliv2.zip -d ../
sudo .././aws/install
```


### 1.2 configure AWS Client
- MAS EZ에서 제공하는 AWS 계정을 사용하여 설정 합니다.
```bash
aws configure
AWS Access Key ID [None]: 
AWS Secret Access Key [None]: 
Default region name [None]: 
Default output format [None]: json
```

## 2. AWS EKS Cli 설치
### 2.1 download and install AWS EKS cli
- 설치하려는 linux 버전에 맞는 eks cli를 다운로드하고, /usr/local/bin으로 옮겨 전체 시스템에서 사용할 수 있는 PATH 로 옮겨 둡니다.
```bash
curl --silent --location "https://github.com/weaveworks/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp
sudo mv /tmp/eksctl /usr/local/bin
```

## 3. Kubectl 설치
- 현재 지원되는 안정적인 버전을 참조하여 kubectl을 다운로드하고 전체 시스템에서 사용할 수 있는 PATH로 옮겨 둡니다.
```bash
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl
```

## 4. EKS Cluster 구성
- CAPSTONE 평가의 기준에 따라서 설정을 요청한 명칭(team4) 로 클러스터를 설치 합니다.
  - 설치 작업은 약 30분 정도 예상되니, 시간 소요를 고려하여 스케쥴하는 편이 좋습니다.
```bash
export myClusterUserid=team4

eksctl create cluster --name ${myClusterUserid}  \
--version 1.21 \
--spot --managed --nodegroup-name standard-workers \
--node-type t3.medium --nodes 3 --nodes-min 1 --nodes-max 3
```
- EKS Cluster 가 설치 완료 된 후 정보를 조회하여 확인 합니다.
```bash
eksctl get cluster
```


## 5. EKS Cluster에 접근하기 위한 Kubectl Context를 등록 합니다.
```bash
export myClusterUserid=team4
aws eks update-kubeconfig --name ${myclusterUserid}
```
- K8s Cluster 의 Node 상태 확인
```bash
 kubectl get nodes
NAME                                                STATUS   ROLES    AGE   VERSION
ip-192-168-31-153.ap-northeast-3.compute.internal   Ready    <none>   13m   v1.21.14-eks-ba74326
ip-192-168-45-158.ap-northeast-3.compute.internal   Ready    <none>   13m   v1.21.14-eks-ba74326
ip-192-168-76-44.ap-northeast-3.compute.internal    Ready    <none>   13m   v1.21.14-eks-ba74326
```

## 6. kubectl 의 shortcut alias 등록
- .bashrc 에 alias 와 auto complete 를 수행할 command 및 kubectl 을 k로 단축하는 명령어를 추가 합니다.
```bash

cat << EOF | tee -a ~/.bashrc

## K8s Settings
source <(kubectl completion bash)
alias k=kubectl
complete -o default -F __start_kubectl k
EOF
```
```bash
source ~/.bashrc
```

## 7. Docker 의 Registry로 ECR을 등록 합니다.
- 이 등록작업은 만약 Public으로 Docker Hub를 사용하는 경우는 Skip 해도 좋습니다.
  - 실제 Product 환경에서는 보완을 고려하여 Private Docker Registry를 설정하는 ECR을 사용을 권고합니다.
- AWS Console을 통해서 ECR을 생성하고 이를 현재 사용중인 Terminal에 설정 하여, Docker Image의 Registery로 등록 합니다.
- ECR을 사용하기 위한 계정의 패스워드는 아래의 cli를 사용하여 확인 합니다.
```bash
ECR_URI=936103362868.dkr.ecr.ap-northeast-3.amazonaws.com/team4
export REGION=ap-northeast-3

aws --region $REGION ecr get-login-password 
eyJwYXlsb2FkIjoiNTY1dFZKbmVtdVlTZmpuMEtIdEhQc
... 생략 ..
```
- ECR을 현재 접속한 Docker Client에서 로그인합니다.
  - 로그인에 성공하면 현재의 Terminal에서 ECR에 Image를 up/download 할 수 있습니다.  
```bash
export ECR_URI=936103362868.dkr.ecr.ap-northeast-3.amazonaws.com/team4
export USER_NM=team4
docker login --username AWS -p $(aws --region $REGION ecr get-login-password) $ECR_URI


WARNING! Using --password via the CLI is insecure. Use --password-stdin.
WARNING! Your password will be stored unencrypted in /home/gitpod/.docker/config.json.
Configure a credential helper to remove this warning. See
https://docs.docker.com/engine/reference/commandline/login/#credentials-store

Login Succeeded
```


# 나. Kubernetes Support

## 1. install Helm
[heml & kafka](https://labs.msaez.io/#/courses/cna-full/d7337970-32f3-11ed-92da-1bf9f0340c92/#ops-utility)
- Helm Client 설치
```bash
curl https://raw.githubusercontent.com/helm/helm/master/scripts/get-helm-3 > get_helm.sh
chmod 700 get_helm.sh
./get_helm.sh
```

### 2. install HTTPie Pod
- http Request를 Cli 로 만들기 위해서 설치 합니다.
```bash
cat <<EOF | kubectl apply -f -
apiVersion: "v1"
kind: "Pod"
metadata: 
  name: httpie
  labels: 
    name: httpie
spec: 
  containers: 
    - 
      name: httpie
      image: clue/httpie
      command:
        - sleep
        - "36000"
EOF
```
- 생성한 pod 에 terminal 접속하기
```bash
kubectl exec -it httpie bin/bash
```

### 3. Seige Pod
- K8s의 Circuit Break 및 Horizonal Pod Autoscaler 설정 시, load를 만들기 위해서 설치 합니다.
```
kubectl apply -f - <<EOF
apiVersion: v1
kind: Pod
metadata:
  name: siege
spec:
  containers:
  - name: siege
    image: apexacme/siege-nginx
EOF
```
- siege pod 접속하기
```bash
kubectl exec -it siege bin/bash
```

### 4. login dockerHub
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


# 다. CAPSTONE 준비
### 1. install Metric server
```bash
kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/download/v0.3.7/components.yaml
kubectl get deployment metrics-server -n kube-system
```

### 2. kafka 설치
- kafka는 helm을 사용하여 설치 합니다.
- CAPSTONE의 Application는 kafka를 Default Namespace를 사용하는 것으로 기본 설정되어 있습니다.
- 그래서 여기서는 default Namespace에 kafka를 설치 합니다.
```bash
helm repo update
helm repo add bitnami https://charts.bitnami.com/bitnami
helm install my-kafka bitnami/kafka
```
- 만약 특정 Namespace를 사용하도록 설정하는 경우는
  - 아래의 예제는 Namespace를 kafka를 사용하는 경우 임
```bash
helm repo update
helm repo add bitnami https://charts.bitnami.com/bitnami
kubectl create ns kafka
helm install my-kafka bitnami/kafka --namespace kafka
```

#### 2.1 kafka Client
- kafka의 Topic에 대해서 수신되는 메시지를 보는 방법은 아래와 같습니다.
- kafka client(Pod)를 설치 후 해당 client를 통해서 kafka server에 접속해서 kafka topic의 메시지를 확인 합니다.
  - 여기서는 topic 명이 `team` 입니다.
  - 생성된 kafka의 Service 는 `my-kafka` 입니다.
  - 생성된 kafka zookeeper의 Service는 `my-kafka-zookeeper` 입니다.
 
- [Kafka Commands](https://labs.msaez.io/#/courses/cna-full/d7337970-32f3-11ed-92da-1bf9f0340c92/#kafka-base)
- [kafka Scaling](https://labs.msaez.io/#/courses/cna-full/d7337970-32f3-11ed-92da-1bf9f0340c92/#Kafka-Scaling)
- [kafka Dead Letter Queue](https://labs.msaez.io/#/courses/cna-full/d7337970-32f3-11ed-92da-1bf9f0340c92/#Kafka-Retry-DLQ)
```bash
kubectl run my-kafka-client --restart='Never' --image docker.io/bitnami/kafka:2.8.0-debian-10-r0 --command -- sleep infinity

## Pod 안으로 접속 하기
kubectl exec --tty -i my-kafka-client -- bash

###------------------------------------------------
### Producer
###------------------------------------------------
### PRODUCER 그룹 정보
kafka-console-producer.sh \
--broker-list my-kafka:9092 \
--topic team
--describe

### Topic team에 producer 연결 후 메시지 publish 하기
kafka-console-producer.sh --broker-list http://my-kafka:9092 \
--topic team

###------------------------------------------------
### Topic
###------------------------------------------------
### TOPIC:
kafka-topics.sh --zookeeper  my-kafka-zookeeper:2181 --list 

### Topic 생성
kafka-topics.sh --bootstrap-server http://my-kafka:9092 \
--topic team \
--create \
--partitions 1 \
--replication-factor 1

### Topic 리스트
kafka-topics.sh --bootstrap-server http://my-kafka:9092 --list

### Topic Partition 증가
kafka-topics.sh --zookeeper localhost:2181 --alter --topic kafkatest -partitions 2

###------------------------------------------------
### Consumer
###------------------------------------------------
### CONSUMER:
kafka-console-consumer.sh \
--bootstrap-server my-kafka:9092 \
--topic team \
--from-beginning

kafka-console-consumer.sh \
--bootstrap-server my-kafka:9092 \
--topic team

### Topic team에 consumer 연결 후 메시지 subscribe 하기
kafka-console-consumer.sh --bootstrap-server http://my-kafka:9092 --topic topic_example --from-beginning

### consumer group 목록
kafka-consumer-groups.sh --bootstrap-server http://my-kafka:9092 --list

### consumer group offset 확인 / Lagging 확인
kafka-consumer-groups.sh --bootstrap-server http://my-kafka:9092 --group <group_id> --describe

### consumer group offset 재 설정 하기
kafka-consumer-groups.sh --bootstrap-server http://my-kafka:9092 --group <group_id> --topic topic_example --reset-offsets --to-earliest --execute

##### 다른 reset 옵션
 --shift-by <positive_or_negative_integer>
 --to-current
 --to-latest
 --to-offset <offset_integer>
 --to-datetime <datetime_string>
 --by-duration <duration_string>
```


### 3. Istio 설치
#### 3.1 install Istio 
- download istio
```bash
curl -L https://istio.io/downloadIstio | ISTIO_VERSION=1.11.3 TARGET_ARCH=x86_64 sh -
```
- install istio
```bash
cd istio-1.11.3
export PATH=$PWD/bin:$PATH
istioctl install --set profile=demo -y --set hub=gcr.io/istio-release
```

#### 3.2 install addons
- install addon
```bash
kubectl apply -f samples/addons
```
- modify ClusterIP to LoadBalancer
```
## kail
kubectl edit svc kiali -n istio-system
:%s/ClusterIP/LoadBalancer/g
:wq!

## Jaeger
kubectl edit svc tracing -n istio-system
:%s/ClusterIP/LoadBalancer/g
:wq!
```

#### 3.3 enable istio-injection
[Istion Ingress](https://labs.msaez.io/#/courses/cna-full/d7337970-32f3-11ed-92da-1bf9f0340c92/#ops-service-mesh-istio)

- Envoy 를 생성해서 Pod에 자동적으로 주입하기 위한 설정
```bash
kubectl label namespace default istio-injection=enabled
```

### 4. Ingress 설치
#### 4.1Ingress Controller 설치
```bash
helm repo add stable https://charts.helm.sh/stable
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo update
kubectl create namespace ingress-basic
```
```bash
helm install nginx-ingress ingress-nginx/ingress-nginx --namespace=ingress-basic
```
- 설치 점검
```bash
kubectl get all --namespace=ingress-basic
```
- Istio Addition Ingress
```yaml
apiVersion: "extensions/v1beta1"
kind: "Ingress"
metadata: 
  name: "istio-ingress"
  namespace: "istio-system"
  annotations: 
    kubernetes.io/ingress.class: "nginx"
spec: 
  rules: 
    - host: "kiali.service.com"
      http: 
        paths: 
          - 
            path: /
            pathType: Prefix
            backend: 
              serviceName: kiali
              servicePort: 20001
    - host: "prom.service.com"
      http: 
        paths: 
          - 
            path: /
            pathType: Prefix
            backend: 
              serviceName: prometheus
              servicePort: 9090
    - host: "gra.service.com"
      http: 
        paths: 
          - 
            path: /
            pathType: Prefix
            backend: 
              serviceName: grafana
              servicePort: 3000

```
##### 4.2 /etc/hosts 등록
- C:\Windows\System32\drivers\etc 에 ip 와 CNAME 등록
  - IP는 AWS의 LoadBalancer 의 IP 입니다.
```bash
kubectl get services -n istio-system
kubectl get ingress  -n istio-system
```

# 라. CAPSTONE 평가

## 5번. Circuit Breaker
[Istio/DestinationRule](https://labs.msaez.io/#/courses/cna-full/773ec1d0-b3ca-11ec-a031-eb021d6c81d0/Istio-CircuitBreaker)

### 5번-1 enable istio injection
- Envoy 를 생성해서 Pod에 자동적으로 주입하기 위한 설정
```bash
kubectl label namespace default istio-injection=enabled
```

### 5번-2 deploy payment
- RS 3개를 설정하였음
```bash
cd payment/kubernetes
kubectl apply -f deployment.yaml
kubectl apply -f service.yaml
```
- deploy 된 payment 점검
  - envoy 가 정상적으로 runing 되는지 확인 할 것
```
kubectl get all
```

### 5번-4 install Circuit Breaker
- payment 서비스의 라우팅 대상 컨테이너 목록에서 1초단위로 체크하여 1번이라도 서버 오류가 발생 시, 3분동안 라우팅에서 제외하며, 모든 컨테이너가 제외될 수 있다.
- 모든 컨테이너가 제외된 경우, ‘no healthy upstream’ 오류를 리턴한다.
```bash
kubectl apply -f - << EOF
  apiVersion: networking.istio.io/v1alpha3
  kind: DestinationRule
  metadata:
    name: payment
    namespace: default
  spec:
    host: payment
    trafficPolicy:
      http:
        http1MaxPendingRequests: 1
        maxRequestsPerConnection: 1    
      # loadBalancer:
      #   simple: ROUND_ROBIN
      # outlierDetection:
      #   consecutive5xxErrors: 1
      #   interval: 1s
      #   baseEjectionTime: 3m
      #   maxEjectionPercent: 100
EOF
```
- CB 설정 확인
```bash
kubectl get DestinationRule -o yaml
```

### 5번-5 load 발생 및 캡쳐
```bash
kubectl exec -it pod/[SIEGE POD객체] -n tutorial -c siege-nginx  -- /bin/bash

## In POD
siege -c1 -t10S -v -y http://payment:8080/payments

siege -c2 -t10S -v -y http://payment:8080/payments
```




## 6번 - API Gateway
[참조](https://labs.msaez.io/#/courses/cna-full/773ec1d0-b3ca-11ec-a031-eb021d6c81d0/ops-ingress)
### 6번-1 Ingress 배포
```yaml
apiVersion: "extensions/v1beta1"
kind: "Ingress"
metadata: 
  name: "shop-ingress"
  annotations: 
    kubernetes.io/ingress.class: "nginx"
spec: 
  rules: 
    - 
      http: 
        paths: 
          - 
            path: /orders
            pathType: Prefix
            backend: 
              serviceName: order
              servicePort: 8080
          - 
            path: /deliveries
            pathType: Prefix
            backend: 
              serviceName: delivery
              servicePort: 8080
          - 
            path: /store
            pathType: Prefix
            backend: 
              serviceName: stores
              servicePort: 8080
          - 
            path: /deliveries
            pathType: Prefix
            backend: 
              serviceName: delivery
              servicePort: 8080
```
```
kubectl create -f shop-ingress.yaml
```

### 6번-2 Ingress Controller 설치
- 위에서 Ingress Controller 를 설치했다면 이 단계는 스킵합니다.
```bash
helm repo add stable https://charts.helm.sh/stable
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo update
kubectl create namespace ingress-basic
```
```bash
helm install nginx-ingress ingress-nginx/ingress-nginx --namespace=ingress-basic
```
- 설치 점검
```bash
kubectl get all --namespace=ingress-basic
```
- 설치 완료 후 Ingress Addr 점검
```bash
kubectl get ingress
```
### 6번-3 EXTERNAL(Ingress) 로 접속 확인
```bash
http {{ INGRESS_ADDRESS }}/payments
```
## 7번 위 6번 성공시 자동 성공


## 8번 K8s Deployment
## 


## 9번 Zero-downtime Deploy(Readiness probe)은 10번의 liveness와 함께 처리 됩니다.
[Readiness probe](https://labs.msaez.io/#/courses/cna-full/773ec1d0-b3ca-11ec-a031-eb021d6c81d0/#ops-readiness)

#### 9번-1 Application의 Spring Acuator 설정 추가 및 재 compile 및 패킹
※ liveness와 readiness 의 점검 path를 기본  Microservice로 할 것 인지 아니면, actuator/health 로 할지는 테스트 하면서 결정하도록 한다.
- spring actuator를 활성화하는 경우는 아래의 설정을 ${PAYMENT_HOME}/resources/application.yaml 에 추가하도록 합니다.
```bash
management:
  endpoints:
    web:
      exposure:
        include:
          - "info"
          - "health"
          - "httptrace"
```
- 다시 빌드 및 docker image 만들기
```bash
export MS_ID=payment

cd /workspace/team4/${MS_ID}

mvn package -B -DskipTests
```

##### build docker
```bash
export MS_ID=payment
export DOCKER_ID=ulysysdev
export DOCKER_TAG=v1

cd /workspace/team4/${MS_ID}

docker build -t ${DOCKER_ID}/${MS_ID}:${DOCKER_TAG} .
# docker run ${DOCKER_ID}/${MS_ID}:${DOCKER_TAG}
```

##### push docker 
```bash
export MS_ID=payment
export DOCKER_ID=ulysysdev
export DOCKER_TAG=v1

cd /workspace/team4/${MS_ID}

docker push ${DOCKER_ID}/${MS_ID}:${DOCKER_TAG}
```


## 11번 HPA
[HPA](https://labs.msaez.io/#/courses/cna-full/773ec1d0-b3ca-11ec-a031-eb021d6c81d0/#ops-autoscale)

#### 11번-1 Auto Scale-out 설정
- deployment manifest에서 rs 를 1로 수정 후 재배호 합니다.
```
kebuectl -f deployment.yaml
```
- hpa 를 설정 합니다.
```bash
kubectl autoscale deployment payment --cpu-percent=20 --min=1 --max=5
```
- hpa 를 확인 합니다.
```bash
kubectl get hpa
```

#### 11번-2 hpa 점검
- load 를 발생 시킵니다.
  - 50명, 60초 동안 부하를 발생 시킵니다.
```bash
kubectl exec -it siege -- /bin/bash
siege -c50 -t60S -v http://payment:8080/payments
exit
```
#### 11번-3 payment Pod 가 증가하는 것을 확인

```bash
kubectl get hpa
```
- Seige Summary
```bash

```


## 12번 PVC/ Secret Map
[PVC](https://labs.msaez.io/#/courses/cna-full/773ec1d0-b3ca-11ec-a031-eb021d6c81d0/ops-persistence-volume)
#### 12번-1 EFS 생성

#### 12번-2 EFS 계정(SA)에 대한 RBAC 설정
- SA 배포
```yaml
apiVersion: v1
kind: ServiceAccount
metadata:
  name: efs-provisioner
  namespace: default
```
```bash
kubectl apply -f efs-sa.yaml
kubectl get ServiceAccount 
```
- RBAC 배포
```bash
kubectl apply -f efs-rbac.yaml
```
- efs provisioner 배포
  - 반드시 env 의 설정 값을 현행화 해야 합니다. (efs-provisioner-deploy.yml 는 샘플 수준 임)
```
kubectl apply -f efs-provisioner-deploy.yml
```
#### 12번-3 StorageClass 등록
```bash
kubectl apply -f efs-storageclass.yml
kubectl get sc aws-efs
```
#### 12번-4 PVC 생성
```bash
kubectl apply -f volume-pvc.yml
kubectl get pvc aws-efs
```
#### 12번-5 POD 적용
- volume 에 대한 주석을 deployment.yaml 에서 해제하고 다시 배포
```
kubectl apply -f deployment.yaml
```
#### 12번-6 POD 에  mount 확인
```
kubectl get pods

kubectl exec -it pod/payment-oooooo payment -- /bin/sh
## in POD
cd /mnt/aws
touch HELLO-WORLD.txt
```
