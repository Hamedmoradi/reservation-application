# <ins>azki Reservation service<ins/>

## Deploy

#### Requirements

you need these installed on your machine:

- [Maven](https://maven.apache.org/)
- [Docker](https://www.docker.com/)

to deploy locally on your machine:

```shell
$ bash ./deploy/deploy-local.sh
```

## Swagger UI

Swagger ui is available at [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)



## Application Structure
Application developed based on Flyway for manage DB scripts and data.

The application build and start on 8080 port.

For build docker images :

```shell
mvn clean package jib:dockerBuild
```
