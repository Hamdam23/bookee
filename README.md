# ![text-1663590297807](https://user-images.githubusercontent.com/90045606/191016582-cb24ed29-13d3-4841-a96e-2b979e55d0eb.png)

#### _bookee - Find books you'll love, and keep track of the ones you want to read._

#### By _**Hamdam23**_

<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->
[![Contributors][contributors-shield]][contributors-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

<!-- TABLE OF CONTENTS -->
## <span style="font-weight:bold">Table of Contents</span>

* [Built-With](#built-with)
* [Requirements](#requirements)
* [Database-Relationships](#database-relationships)
* [API docs](#api-docs)
* [Tech Stack](#tech-stack)
* [Usage](#usage)
* [Running](#running)
* [Contact](#contact)

### Built-With
* [Spring Boot](https://spring.io/projects/spring-boot)
* [PostgreSQL](https://www.postgresql.org/)

## Requirements

For building and running the application you need:

- [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven 3](https://maven.apache.org)

## Database-Relationships

![image](https://user-images.githubusercontent.com/90045606/206860056-0dfe4f92-1614-45ab-87ea-321a247194db.png)

## API docs

<p>Check out <a href="https://documenter.getpostman.com/view/23459056/2s8YzTT2Vd">You can get Postman collection</a>.</p>

## Tech stack

This REST API is developed using Java programming language and Spring Boot starter of Spring framework. The full tech stack:

  - [Spring Boot](https://spring.io/projects/spring-boot)
  - [Spring Security](https://spring.io/projects/spring-security) for authentication and authorization
  - [JWT](https://auth0.com/docs/secure/tokens/json-web-tokens) for tokens and share security information between two parties — a client and a server
  - [AWS S3](https://aws.amazon.com/s3/) for storing images and other files
  - [JUnit 5](https://junit.org/junit5/) for unit and integration tests
  - [TestContainer](https://www.testcontainers.org/) for using Docker images in integration tests
  - [Localstack](https://github.com/localstack/localstack) for mocking AWS services in integration tests
  - [Spring Data JPA](https://spring.io/projects/spring-data-jpa) for data access layers
  - [PostgreSQL](https://www.postgresql.org/) for Database
  - [FlywayDB](https://flywaydb.org/) for Database migrations
  - [Lombok](https://projectlombok.org/) for reducing boilerplate code
  - and etc.

## Usage

**Attention!** This project uses environment variables for some configuration properties. Define your environment variables in application.yml file. Otherwise default values will be used.

  - AWS_ACCESS_KEY
  - AWS_SECRET_KEY

**Hint**: Get AWS access and secret keys from your [Amazon Web Services console](https://docs.aws.amazon.com/powershell/latest/userguide/pstools-appendix-sign-up.html)

## Running

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `bookee.src.main.java.BookeeApplication` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

## Contact

<a href="https://www.linkedin.com/in/hamdam-xudayberganov-612634224">
  <img src="https://img.shields.io/badge/linkedin-%230077B5.svg?&style=for-the-badge&logo=linkedin&logoColor=white" />    
</a>&nbsp;
<a href="https://t.me/xhamdam">
  <img src="https://img.shields.io/badge/Telegram-1DA1F2?style=for-the-badge&logo=telegram&logoColor=white" />    
</a>&nbsp;
<a href="mailto://xudayberganovhamdam01@mail.com">
  <img src="https://img.shields.io/badge/gmail-D14836?style=for-the-badge&logo=gmail&logoColor=white" />
</a>&nbsp;

<br>Released under the Apache License 2.0. See the [LICENSE](https://github.com/Hamdam23/bookee/blob/master/LICENSE) file.

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/Hamdam23/bookee.svg?style=flat-square
[contributors-url]: https://github.com/Hamdam23/bookee/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/Hamdam23/bookee.svg?style=flat-square
[forks-url]: https://github.com/Hamdam23/bookee/network/members
[stars-shield]: https://img.shields.io/github/stars/Hamdam23/bookee.svg?style=flat-square
[stars-url]: https://github.com/Hamdam23/bookee/stargazers
[issues-shield]: https://img.shields.io/github/issues/Hamdam23/bookee.svg?style=flat-square
[issues-url]: https://github.com/Hamdam23/bookee/issues
[license-shield]: https://img.shields.io/github/license/Hamdam23/bookee.svg?style=flat-square
[product-screenshot]: images/screenshot.png
