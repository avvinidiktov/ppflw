# Getting Started

##### To run tests:

> mvn test

#### To run app using docker:

> mvn package -DskipTests
>
>docker-compose build
>
>docker-compose up -d

#### API docs:

http://localhost:8080/api-docs

#### H2 console:

http://localhost:8081/h2

#### Usef tips:
 - For issue with connection to kafka container:
    * add "127.0.0.1 kafka" to hosts file "C:\Windows\System32\drivers\etc\hosts"
 - For issue with h2 console:
    * add "spring.h2.console.settings.web-allow-others=true" to application.properties file
