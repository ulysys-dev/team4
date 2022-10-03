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
```
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl
```

## 4. EKS Cluster 구성
- CAPSTONE 평가의 기준에 따라서 설정을 요청한 명칭(team4) 로 클러스터를 설치 합니다.
  - 설치 작업은 약 30분 정도 예상되니, 시간 소요를 고려하여 스케쥴하는 편이 좋습니다.
```
export myClusterUserid=team4

eksctl create cluster --name ${myClusterUserid}  \
--version 1.21 \
--spot --managed --nodegroup-name standard-workers \
--node-type t3.medium --nodes 3 --nodes-min 1 --nodes-max 3
```
- EKS Cluster 가 설치 완료 된 후 정보를 조회하여 확인 합니다.
```
eksctl get cluster
```


## 5. EKS Cluster에 접근하기 위한 Kubectl Context를 등록 합니다.
```
export myClusterUserid=team4
aws eks update-kubeconfig --name ${myclusterUserid}
```
- K8s Cluster 의 Node 상태 확인
```
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
```
source ~/.bashrc
```

## 7. Docker 의 Registry로 ECR을 등록 합니다.
- 이 등록작업은 만약 Public으로 Docker Hub를 사용하는 경우는 Skip 해도 좋습니다.
  - 실제 Product 환경에서는 보완을 고려하여 Private Docker Registry를 설정하는 ECR을 사용을 권고합니다.
- AWS Console을 통해서 ECR을 생성하고 이를 현재 사용중인 Terminal에 설정 하여, Docker Image의 Registery로 등록 합니다.
- ECR을 사용하기 위한 계정의 패스워드는 아래의 cli를 사용하여 확인 합니다.
```
ECR_URI=936103362868.dkr.ecr.ap-northeast-3.amazonaws.com/team4
export REGION=ap-northeast-3

aws --region $REGION ecr get-login-password 
eyJwYXlsb2FkIjoiNTY1dFZKbmVtdVlTZmpuMEtIdEhQc
... 생략 ..
```
- ECR을 현재 접속한 Docker Client에서 로그인합니다.
  - 로그인에 성공하면 현재의 Terminal에서 ECR에 Image를 up/download 할 수 있습니다.  
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

    CHECK
       kafka-topics.sh --list --zookeeper  my-kafka-zookeeper:2181
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

##### build docker
```
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

##### update k8s deployment manifest

```


```

#####  install Ingress Controller
```
helm repo add stable https://charts.helm.sh/stable
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo update
kubectl create namespace ingress-basic
helm install nginx-ingress ingress-nginx/ingress-nginx --namespace=ingress-basic
```

#####
```yaml
apiVersion: "extensions/v1beta1"
kind: "Ingress"
metadata: 
  name: "ingress"
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
            path: /payments
            pathType: Prefix
            backend: 
              serviceName: payment
              servicePort: 8080
          - 
            path: /stores
            pathType: Prefix
            backend: 
              serviceName: store
              servicePort: 8080
          - 
            path: /deliveries
            pathType: Prefix
            backend: 
              serviceName: delivery
              servicePort: 8080
```


```bash
kubectl create -f ingress.yaml
```

##### check k8s process
```bash
gitpod /workspace/team4/gateway/kubernetes (main) $ k get all -A
NAMESPACE       NAME                                                          READY   STATUS    RESTARTS   AGE
default         pod/my-kafka-0                                                1/1     Running   1          82m
default         pod/my-kafka-client                                           1/1     Running   0          78m
default         pod/my-kafka-zookeeper-0                                      1/1     Running   0          82m
default         pod/payment-76469bc4c5-slg4j                                  1/1     Running   0          12m
ingress-basic   pod/nginx-ingress-ingress-nginx-controller-5d88d44856-69srv   1/1     Running   0          2m21s
kube-system     pod/aws-node-n8pq9                                            1/1     Running   0          3h7m
kube-system     pod/aws-node-rmdxv                                            1/1     Running   0          3h7m
kube-system     pod/aws-node-tjmc2                                            1/1     Running   0          3h7m
kube-system     pod/coredns-79cc4658db-bv55k                                  1/1     Running   0          3h15m
kube-system     pod/coredns-79cc4658db-s9qqr                                  1/1     Running   0          3h15m
kube-system     pod/kube-proxy-k2k4l                                          1/1     Running   0          3h7m
kube-system     pod/kube-proxy-qwgv8                                          1/1     Running   0          3h7m
kube-system     pod/kube-proxy-w6xvp                                          1/1     Running   0          3h7m

NAMESPACE       NAME                                                       TYPE           CLUSTER-IP       EXTERNAL-IP                                                                    PORT(S)                      AGE
default         service/kubernetes                                         ClusterIP      10.100.0.1       <none>                                                                         443/TCP                      3h15m
default         service/my-kafka                                           ClusterIP      10.100.198.113   <none>                                                                         9092/TCP                     82m
default         service/my-kafka-headless                                  ClusterIP      None             <none>                                                                         9092/TCP,9093/TCP            82m
default         service/my-kafka-zookeeper                                 ClusterIP      10.100.243.96    <none>                                                                         2181/TCP,2888/TCP,3888/TCP   82m
default         service/my-kafka-zookeeper-headless                        ClusterIP      None             <none>                                                                         2181/TCP,2888/TCP,3888/TCP   82m
default         service/payment                                            ClusterIP      10.100.64.194    <none>                                                                         8080/TCP                     12m
ingress-basic   service/nginx-ingress-ingress-nginx-controller             LoadBalancer   10.100.241.105   a275654bef1e544208389d4833f9dd7d-1462542699.ap-northeast-3.elb.amazonaws.com   80:32276/TCP,443:32586/TCP   2m22s
ingress-basic   service/nginx-ingress-ingress-nginx-controller-admission   ClusterIP      10.100.192.243   <none>                                                                         443/TCP                      2m22s
kube-system     service/kube-dns                                           ClusterIP      10.100.0.10      <none>                                                                         53/UDP,53/TCP                3h15m

NAMESPACE     NAME                        DESIRED   CURRENT   READY   UP-TO-DATE   AVAILABLE   NODE SELECTOR   AGE
kube-system   daemonset.apps/aws-node     3         3         3       3            3           <none>          3h15m
kube-system   daemonset.apps/kube-proxy   3         3         3       3            3           <none>          3h15m

NAMESPACE       NAME                                                     READY   UP-TO-DATE   AVAILABLE   AGE
default         deployment.apps/payment                                  1/1     1            1           12m
ingress-basic   deployment.apps/nginx-ingress-ingress-nginx-controller   1/1     1            1           2m22s
kube-system     deployment.apps/coredns                                  2/2     2            2           3h15m

NAMESPACE       NAME                                                                DESIRED   CURRENT   READY   AGE
default         replicaset.apps/payment-76469bc4c5                                  1         1         1       12m
ingress-basic   replicaset.apps/nginx-ingress-ingress-nginx-controller-5d88d44856   1         1         1       2m22s
kube-system     replicaset.apps/coredns-79cc4658db                                  2         2         2       3h15m

NAMESPACE   NAME                                  READY   AGE
default     statefulset.apps/my-kafka             1/1     82m
default     statefulset.apps/my-kafka-zookeeper   1/1     82m
```

##### install HTTPie Pod
```
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
```
kubectl exec -it httpie bin/bash
```

###### install Seige Pod
```bash
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
```bash
kubectl exec -it siege bin/bash
```


###### install Metric server
```
kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/download/v0.3.7/components.yaml
kubectl get deployment metrics-server -n kube-system
```


##### install Istio
```bash
curl -L https://istio.io/downloadIstio | ISTIO_VERSION=1.11.3 TARGET_ARCH=x86_64 sh -
cd istio-1.11.3
export PATH=$PWD/bin:$PATH


istioctl install --set profile=demo --set hub=gcr.io/istio-release
```

- install Isto samples/addons
```bash
kubectl apply -f samples/addons
```

- enable istio-system
```
kubectl apply -f <(istioctl kube-inject -f Deployment.yml) -n istio-test-ns
kubectl label namespace default istio-injection=enabled
```

---

