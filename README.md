# Rock Paper Scissors REST API

A rock-paper-scissors game backend written on Java. 
Backend micro-service is configured for Docker containerisation and can be scaled with container management system.
Backend is using Redis NoSQL as a datastore.

Game engine is using Markov chain to predict player moves depending on previous moves and RSP society data for first round with new players.

Out of scope:
- Player authentication
- Threat detection handling
- Currently, micro-service is using http

Requirements:
- Current suggested load up to 250 ops/second per instance
- 1 instance is designed and tested for serving 10000 users in parallel

Tech stack: Java 8, spring-boot, Redis, Docker

Checkstyles is used for static code analysis.

## How to run the backend

### 1. With Docker (Recommended)
Prerequisites: installed docker, internet, system proxy is set up if needed, allowed outbound connections to redis-278a75ee-zamkovyi-f976.aivencloud.com:26890

```
docker run -p 8080:8080 -t invaa/rock-paper-scissors:latest
```

### 2. Maven + Spring-Boot
Prerequisites: installed jdk 1.8+, maven, git, internet, system proxy is set up if needed, allowed outbound connections to redis-278a75ee-zamkovyi-f976.aivencloud.com:26890


```
cd <your project dir> 
git clone https://github.com/invaa/rock-paper-scissors.git
cd rock-paper-scissors
mvn clean install
mvn spring-boot:run -Djasypt.encryptor.password=password
```

## Health info
There is an endpoint to check if service instance is running: 
```
http://localhost:8080/actuator/health
```

## Documentation
Swagger UI page:
```
http://localhost:8080/swagger-ui.html
```

Swagger json file:
```
http://localhost:8080/v2/api-docs
```

## Examples
Install postman an import collection from project path
```
./src/test/postman/rps.postman_collection.json
```
rps.postman_collection.json scripts should be run with environment variables from default.postman_environment.json 
