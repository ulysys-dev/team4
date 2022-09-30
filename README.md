

## Service Port

| microSerivce | portNum |
|:-------------|:--------|
| deliveries    | 8081   |
| orders        | 8082   |
| stores        | 8083   |
| payments      | 8084   |
| notify        | 8086   |


# start Shell

```bash
#!/bin/bash

WORKSPACE=/workspace/team4

cd $WORKSPACE
cd delivery
mvn sprint-boot:run

cd $WORKSPACE
cd Notify
mvn sprint-boot:run

cd $WORKSPACE
cd store
mvn sprint-boot:run

cd $WORKSPACE
cd payment
mvn sprint-boot:run

cd $WORKSPACE
cd order
mvn sprint-boot:run


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