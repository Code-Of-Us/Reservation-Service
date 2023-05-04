# Reservation-Service

![build workflow](https://github.com/Code-Of-Us/Rent-a-Park/actions/workflows/build.yaml/badge.svg)
![codecov.io](https://codecov.io/github/Code-Of-Us/Rent-a-Park/coverage.svg)

![Bugs](https://sonarcloud.io/api/project_badges/measure?project=Code-Of-Us_Rent-a-Park&metric=bugs)
![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=Code-Of-Us_Rent-a-Park&metric=vulnerabilities)

### Technologies:
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)

![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)

![GitHub Actions](https://img.shields.io/badge/github%20actions-%232671E5.svg?style=for-the-badge&logo=githubactions&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

![Apache Kafka](https://img.shields.io/badge/Apache%20Kafka-000?style=for-the-badge&logo=apachekafka)

![CodeCov](https://img.shields.io/badge/codecov-%23ff0077.svg?style=for-the-badge&logo=codecov&logoColor=white)
![SonarQube](https://img.shields.io/badge/SonarQube-black?style=for-the-badge&logo=sonarqube&logoColor=4E9BCD)
## Content

### Technologies

* Java
* Spring Boot, Spring Cloud
* PostgreSQL
* Docker
* Kafka

### Features
* Create, update, delete and list reservations
* Find parking spots from [Parking Service](https://github.com/Code-Of-Us/Rent-A-Park)
* Used Test Containers in integration tests for setting up Postgres and Eureka containers

### CI/CD

* GitHub actions for Continuous Integration (CI)
    * Build with CodeCov
    * Code Analysis with SonarCloud
    * Build and push Docker image to GitHub Container Registry
    * Public GitHub package
    * Docker file
    * Docker compose file for setting up external services like Postgres and Eureka

### Microservice architecture
* Eureka server - Service Registry and Client-side service discovery using Eureka
* Feign Client
* Circuit Breaker and Retry mechanism

![Architecture](./src/main/resources/static/architecture-overview.png)