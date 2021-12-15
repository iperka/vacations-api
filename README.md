[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=iperka_vacations-api&metric=alert_status)](https://sonarcloud.io/dashboard?id=iperka_vacations-api) [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=iperka_vacations-api&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=iperka_vacations-api) [![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=iperka_vacations-api&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=iperka_vacations-api) [![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=iperka_vacations-api&metric=security_rating)](https://sonarcloud.io/dashboard?id=iperka_vacations-api)

[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=iperka_vacations-api&metric=ncloc)](https://sonarcloud.io/dashboard?id=iperka_vacations-api) [![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=iperka_vacations-api&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=iperka_vacations-api) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=iperka_vacations-api&metric=coverage)](https://sonarcloud.io/dashboard?id=iperka_vacations-api)

[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=iperka_vacations-api&metric=code_smells)](https://sonarcloud.io/dashboard?id=iperka_vacations-api) [![Bugs](https://sonarcloud.io/api/project_badges/measure?project=iperka_vacations-api&metric=bugs)](https://sonarcloud.io/dashboard?id=iperka_vacations-api) [![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=iperka_vacations-api&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=iperka_vacations-api)

[![Continuous Integration](https://github.com/iperka/vacations-api/actions/workflows/main.yml/badge.svg)](https://github.com/iperka/vacations-api/actions/workflows/main.yml)

# 1. Vacations API 📅

Access iperka vacations with the API. This API is built with Java Spring Boot and is built with scalability in mind.

## 1.1. Table of Contents 🧾

- [1. Vacations API 📅](#1-vacations-api-)
  - [1.1. Table of Contents 🧾](#11-table-of-contents-)
  - [1.2. Getting Started 🚀](#12-getting-started-)
  - [1.3. Installation 💽](#13-installation-)
  - [1.4. Deployment 🐳](#14-deployment-)
  - [1.5. Usage 🎉](#15-usage-)
  - [1.6. Built With 📦](#16-built-with-)
  - [1.7. Authors 👨‍💻](#17-authors-)
  - [1.8. License 📃](#18-license-)
  - [1.9. Contributing 🤝](#19-contributing-)
  - [1.10. Acknowledgments 🐛](#110-acknowledgments-)

## 1.2. Getting Started 🚀

Pull the official docker image and run the container to test the API.

```bash
$ docker run -p 8080:8080 ghcr.io/iperka/vacations-api:latest
```

The API is now running on port `8080` and should be accessible via `http://localhost:8080`. (The database is only in memory, if the container restarts all data is lost.)

- Swagger UI: http://localhost:8080/swagger-ui/index.html#/
- OpenAPI: http://localhost:8080/openapi/v3/
- Postman Collection: [JSON](./postman_collection.json)

## 1.3. Installation 💽

Its highly recommended to use the container image.

Package the Java application with maven.

```bash
# Windows
.\mvnw.cmd package

# Linux / MacOS
$ ./mvnw package
```

## 1.4. Deployment 🐳

It is recommended to use this API as docker container within a Kubernetes cluster.

## 1.5. Usage 🎉

Run docker container locally with "production" profile activated, for local testing. Keep in mind that a MySQL instance must be running. For production see [Deployment](#deployment--1).

To run a docker container with the image in production mode use the following command (should not be used in production).

```bash
$ docker run \
    -e "SPRING_PROFILES_ACTIVE=production" \
    -e "DATASOURCE_URL=jdbc:mysql://mysql-server:3306/vacations" \
    -e "DATASOURCE_DRIVER=com.mysql.jdbc.Driver" \
    -e "DATASOURCE_USERNAME=vacations" \
    -e "DATASOURCE_PASSWORD=secret" \
    -e "JPA_DDL_AUTO=update" \
    -e "JPA_HIBERNATE_DIALECT=org.hibernate.dialect.MySQL5InnoDBDialect" \
    -e "SPRING_PROFILES_ACTIVE=production" \
    -p 8080:8080 ghcr.io/iperka/vacations-api:latest
```

Now the API should be accessible via `http://localhost:8080`.

## 1.6. Built With 📦

- [Maven](https://maven.apache.org/) - Dependency Management
- [Spring Boot](https://spring.io/) - Java Framework
- [Auth0](https://auth0.com/) - Authentication Provider

## 1.7. Authors 👨‍💻

- **Michael Beutler** - _Initial work_ - [MichaelBeutler](https://github.com/MichaelBeutler)

## 1.8. License 📃

[MIT](https://choosealicense.com/licenses/mit/)

## 1.9. Contributing 🤝

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate and meet the quality gate requirements.

## 1.10. Acknowledgments 🐛

- Ingrate vacations into your calendar.
- Add new workflows to your company.
- Simplify vacations.
- etc...
