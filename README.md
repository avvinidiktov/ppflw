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

#### Usef tips:
 - For issue with connection to kafka container:
    * add "127.0.0.1 kafka" to hosts file "C:\Windows\System32\drivers\etc\hosts"
