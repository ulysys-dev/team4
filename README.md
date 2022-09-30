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


### EKS

```
export myclusterUserid=team4

eksctl create cluster --name ${myclusterUserid} --version 1.21 --spot --managed --nodegroup-name standard-workers --node-type t3.medium --nodes 3 --nodes-min 1 --nodes-max 3
```

### ECR

```
ECR_URI=936103362868.dkr.ecr.ap-northeast-3.amazonaws.com/team4

export REGION=ap-northeast-3
 $ aws --region $REGION ecr get-login-password 
eyJwYXlsb2FkIjoiNTY1dFZKbmVtdVlTZmpuMEtIdEhQclo4S29wbjRaWk1xNUNRelllc2t6enoxREtBTDhLc1VoVlVHL1QwcElMemgxMWRsN2E3QkQwbXlSc1M0bllsYjdpUG9KazEyOXEzOXZWa2IyMXhQMUVpeFNGZm1JaFRHdG56OW00UG1YeVc2cExRZEFUK0VSZTJFTGUvTlZDN0N5dDJaTTR2ODVNS1BRZkFvTEVtZDhKbU5LQU1PZDk4MThlMHF6SWtBUEhXazZGdjVDbWpXbTM0c25pS3ZmNFdCMGxxMk5aazdhYkFxRE91a3pib05PWk9WZUVueGZNVDArS1phek9aT3dXbzJKK1Nzcmx1QjhoS0FJVlEvaEgwY3B6bzlFSjdXbjVaWVloS2Y2TzNWbUVXWDFXRGM1VzZHcUQ4VmNhQzY0eHFQWTN0VC9BNjM0ekJZcGdtVzJLczd2OTAzdFBsN2sxU2tWMXArcUVLM2tickZMSmlHQmtESDBzTnZOTEFwSW00Vmw3QUNjeGVPNmhVbU1BNldkaFQ1bXBHU3dYWXdUeDR3Q0xLK1ZibGFBYzVmZ3JTN2VHKzhWUnhGcVF4U2F3RWZZdXYxYkdOeFVTaGp2R2c0cm84VzBCUUdlN2dMQjdRdklHT2lHUkRRRDRvdXhPekoyMWdrWUtZbmdteEFYY1JWcEV4L3VPdFh1TDBKb3Y0NXVPS0dmSjdjZ2xlWXltUHNLNXAxTms4cDdNYm1nUG1YbzFCU2lRMkVPN015MWtSMmNDbS9HVW52QjVXM2VCd1BsMyttU1YrSmNGQW84N2ZMT0x1VGtaNzhpSjFPc284U2owbU5YbWpmMXZCNkNBOE0rZGdNdGlDOWxhNGhidTM3K1lKanRuU0hwZGYxeXY2dTJGa1NVRjlDRWI1RDlxeTIrZUx6L3REa09EMGw3M3NzbXl4ZTA0ZlJzM2V6SDFSdlAwRXJKS0tFYVBROTFqU2kvbENGanIyRDA3Mng2eVF2T3hhS3VaQjFCNEFKTnU2V0kyV0FYMllwaEo2OHFsVk1OWFdTcFVUaU0yWUkxV0VZUGNqRWV6MHQzMkxHb3pheGpLZ1g1TFRFTTNKQ29QTTkxRkV4TXZYcXFFMnNBc3NqeGtaajhyMWJUOWcvMUU0SlQwUVVXcng4Z3ZpOFRaYi90bGwzOExnWFpZVXdnVFZiVzB1dG1Dd1ZoVFB1K2VrcE1nejdXWnBpcTY0eGZoK3dpVXFaWlRNYzRzdkZCeFZLMnE1N2dOUnBjSC9LWnJ5bkRwcnlyelExWW1pckpFMnVXekh0UEYyTzRiMy9QQldxR1kraytSdHFjeGdtRFlreHVlU1JvdUFNOUhhd0QrWERlS3BsT1dSaDYzWjBNRzB5THcwWERka3l1UVliM25LMnhoTWJqUnNBTlFDMWFERzVkdWltd0U9IiwiZGF0YWtleSI6IkFRSUJBSGlKOUdmM0xEa3YrVllQdUJ4emw1SUJxWFBDanJvbWM0NGJYY3RaVFBGcFVBSFExU2RRTXlOWmt2MENSWWg5aE1YdkFBQUFmakI4QmdrcWhraUc5dzBCQndhZ2J6QnRBZ0VBTUdnR0NTcUdTSWIzRFFFSEFUQWVCZ2xnaGtnQlpRTUVBUzR3RVFRTVBwbkFQQ0I1T0lVdDFQSDdBZ0VRZ0R0Zko3a1JBRTNuRHBoY1VWUUVJZGIxQWxONFBCeHJtQTNwSFdERndGR3h3ajBCSjkrblZGUzBQNTh4eWlyK1R0RC9ZbWs4cGFkUVVmTHkwQT09IiwidmVyc2lvbiI6IjIiLCJ0eXBlIjoiREFUQV9LRVkiLCJleHBpcmF0aW9uIjoxNjY0NTY2MzQwfQ==
```

## Docker Login to ECR
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