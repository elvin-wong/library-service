# About

TBD

# Development

Requires JDK 21.

## Java setup

Use [SDKMAN](https://sdkman.io/install) to manage JDK.

### Install JDK 21 with SDKMAN
```shell
> sdk install java 21.0.3-librca
```

## Run Application from source code
```shell
> ./mvnw spring-boot:run
```
* Requires mysql to be running. (See docker compose file in `docker` directory)

Or, use the development time support.
```shell
> ./mvnw spring-boot:test-run
```
* In this mode, local mysql is used if available; otherwise, it starts a mysql container via Testcontainers (Docker required).

Change basic auth user/pass from command line
```shell
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments='-Dspring.security.user.name=foo,-Dspring.security.user.password=bar'
```

Using env variable to specify various values
```shell
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/library_service  \
SPRING_DATASOURCE_USERNAME=root  \
SPRING_DATASOURCE_PASSWORD=password  \
SPRING_SECURITY_USER_NAME=user  \
SPRING_SECURITY_USER_PASSWORD=pass  \
./mvnw spring-boot:run
```

Note:
`SPRING_SECURITY_USER_NAME` and `SPRING_SECURITY_USER_PASSWORD` are used for Basic auth user & password. 

## Run Application from artifact(executable jar)

To build an artifact(executable jar)
```shell
> ./mvnw package
```
The artifact is generated under `target` directory. (e.g. `target/library-service-0.0.1-SNAPSHOT.jar`)
To run it:
```shell
> java -jar target/library-service-0.0.1-SNAPSHOT.jar
```

## Run Application from container image with Docker

Specify environment properties with `-e` option.
```shell
> docker run \
   -p 8080:8080  \
   -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/library_service  \
   -e SPRING_DATASOURCE_USERNAME=root  \
   -e SPRING_DATASOURCE_PASSWORD=password  \
   docker.io/library/library-service:0.0.1-SNAPSHOT
```

NOTE:
- `-e` option needs to be specified before the container image name.
- Use `host.docker.internal` to talk to another service running in docker. (substitute to `localhost`)

## Run Tests
```shell
> ./mvnw test
```
* When mysql DB is not running locally, it automatically starts up mysql container during the test.

## Container Image

### Local container image creation
```shell
>  ./mvnw -DskipTests spring-boot:build-image
```

### Create and push to remote registry
```shell
> DOCKER_REGISTRY_USER=<user> DOCKER_REGISTRY_TOKEN=<token or password> ./mvnw -DskipTests spring-boot:build-image \
  -Dspring-boot.build-image.publish=true \
  -Dspring-boot.build-image.imageName="<image name>"
```

Sample:
```shell
DOCKER_REGISTRY_USER=ttddyy DOCKER_REGISTRY_TOKEN=$CR_PAT ./mvnw -DskipTests spring-boot:build-image \
  -Dspring-boot.build-image.publish=true \
  -Dspring-boot.build-image.imageName="ghcr.io/ttddyy/library-service:snapshot"
```

## Format

Use [Spring Java Format](https://github.com/spring-io/spring-javaformat).

```shell
> ./mvnw spring-javaformat:apply
```

## `@author` tag

* Add `@author` tag to `main` source code `.java` files except `package-info.java`.
* Add yourself as an `@author` that you modify substantially (more than cosmetic changes).

# Endpoints

For now, all endpoints is protected with Basic Auth.

Swagger API: http://localhost:8080/swagger-ui.html

# Development tips

Recreate mysql schema
```sql
drop schema library_service; create schema library_service;
drop schema library_service_test; create schema library_service_test;
```

Testcontainers trys to create container per test.  
Check the log message has the following:
```
Reuse was requested but the environment does not support the reuse of containers
To enable reuse of containers, you must set 'testcontainers.reuse.enable=true' in a file located at /Users/ttsuyukubo/.testcontainers.properties
```
If so, update `.testcontainers.properties` with `testcontainers.reuse.enable=true`.
