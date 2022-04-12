[![Uptime](https://badgen.net/uptime-robot/status/m790176317-818ced6f0430585b9e70c6ab)](https://status.iperka.com)[![Uptime](https://badgen.net/uptime-robot/day/m790176317-818ced6f0430585b9e70c6ab)](https://status.iperka.com)[![Uptime](https://badgen.net/uptime-robot/week/m790176317-818ced6f0430585b9e70c6ab)](https://status.iperka.com)[![Uptime](https://badgen.net/uptime-robot/month/m790176317-818ced6f0430585b9e70c6ab)](https://status.iperka.com)

# 1. Vacations API 📅

Access iperka vacations with the API. This API is built with Java Spring Boot and is built with scalability in mind.

## 1.1. Table of Contents 🧾

- [1. Vacations API 📅](#1-vacations-api-)
  - [1.1. Table of Contents 🧾](#11-table-of-contents-)
  - [1.2. Getting Started 🚀](#12-getting-started-)
  - [1.3. Installation 💽](#13-installation-)
  - [1.4. Local Deployment 🐳](#14-local-deployment-)
  - [1.5. Deployment ⛴](#15-deployment-)
  - [1.6. Built With 📦](#16-built-with-)
  - [1.7. Authors 👨‍💻](#17-authors-)
  - [1.8. License 📃](#18-license-)
  - [1.9. Contributing 🤝](#19-contributing-)
  - [1.10. Acknowledgments 🐛](#110-acknowledgments-)
  - [1.11. Environment variables 💻](#111-environment-variables-)

## 1.2. Getting Started 🚀

Pull the official docker image and run the container to test the API.

```bash
$ docker run -p 8080:8080 ghcr.io/iperka/vacations-api:latest
```

The API is now running on port `8080` and should be accessible via `http://localhost:8080`. (The database is only in memory, if the container restarts all data is lost.) Keep in mind that you should also add the permissions to your Auth0 domain and assign them to users. By default the iperka Auth0 account will be used.

- Swagger UI: http://localhost:8080/swagger-ui/index.html#/
- OpenAPI: http://localhost:8080/openapi/v3/

If you want to test the API contact us to get client credentials.

## 1.3. Installation 💽

Its highly recommended to use the container image see [Local Deployment](#14-local-deployment-).

Package the Java application with maven.

```bash
$ ./mvnw package
```

## 1.4. Local Deployment 🐳

Run docker container locally with `production` profile activated, for local testing. For production see [Deployment](#15-deployment-).

To run a docker container with the image in production mode use the following command (should not be used in production).

```bash
# Adjust values to your needs
$ docker run \
    -e "SPRING_PROFILES_ACTIVE=production" \
    -e "DATASOURCE_URL=jdbc:h2:mem:vacations-development" \
    -e "DATASOURCE_DRIVER=org.h2.Driver" \
    -e "DATASOURCE_USERNAME=sa" \
    -e "DATASOURCE_PASSWORD=secret" \
    -e "JPA_DDL_AUTO=update" \
    -e "AUTH0_DOMAIN=https://my-domain.eu.auth0.com/" \
    -e "AUTH0_AUDIENCE=https://api.example.com/vacations/" \
    -e "API_SERVER_URL=http://localhost:8080/" \
    --restart unless-stopped -d \
    -p 8080:8080 ghcr.io/iperka/vacations-api:latest
```

Now the API should be accessible via `http://localhost:8080`.

## 1.5. Deployment ⛴

It is recommended to use this API as docker container within a Kubernetes cluster.

[Helm](https://helm.sh) must be installed to use the charts. Please refer to
Helm's [documentation](https://helm.sh/docs) to get started.

Once Helm has been set up correctly, add the repo as follows:

    helm repo add iperka https://iperka.github.io/helm-charts

If you had already added this repo earlier, run `helm repo update` to retrieve
the latest versions of the packages. You can then run `helm search repo iperka` to see the charts.

To install the vacations-api chart:

    helm install my-vacations-api iperka/vacations-api

To uninstall the chart:

    helm delete my-vacations-api

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

There is already a Google Script to integrate the Vacations API into your Google Calendar.
See https://github.com/iperka/vacations-google-script

- Ingrate vacations into your calendar.
- Add new workflows to your company.
- Simplify vacations.
- etc...

## 1.11. Environment variables 💻

Please note that some default values are also provided by configuration profiles. For example if the profile is set to `development` the datasource will be an in memory database ready to test but with non persistent data.

| Name                             | Description                                                            | Type                        | Default                                |
| -------------------------------- | ---------------------------------------------------------------------- | --------------------------- | -------------------------------------- |
| `SPRING_PROFILES_ACTIVE`         | Application configuration profile.                                     | `development \| production` | `development`                          |
| `REST_MAX_PAGE_SIZE`             | Defines the maximum page size for `Pagable` parameters.                | `int`                       | `100`                                  |
| `AUTH0_AUDIENCE`                 | Auth0 Audience configured in your Auth0 API. (Must end with `/`.)      | `string`                    | `https://api.vacations.iperka.com/`    |
| `AUTH0_DOMAIN`                   | Auth0 Domain provided by Auth0. (Must end with `/`.)                   | `string`                    | `https://iperka.eu.auth0.com/`         |
| `AUTH0_CLIENT_ID`                | Auth0 Client ID for Management API.                                    | `string`                    | `MY_CLIENT_ID`                         |
| `AUTH0_CLIENT_SECRET`            | Auth0 Client Secret for Management API.                                | `string`                    | `MY_CLIENT_SECRET`                     |
| `API_DOCS_PATH`                  | Relative path to provide OpenAPI v3 JSON.                              | `string`                    | `/openapi/v3`                          |
| `API_DOCS_SHOW_ACTUATOR`         | When set to `true` the actuator routes will also be documented.        | `boolean`                   | `false`                                |
| `API_DOCS_ENABLED`               | API Docs endpoint is enabled.                                          | `boolean`                   | `false`                                |
| `SWAGGER_UI_ENABLED`             | Swagger UI endpoint is enabled. (Requires API Docs to be enabled too.) | `boolean`                   | `false`                                |
| `SWAGGER_API_DOCS_PATH`          | Relative path for OpenAPI docs.                                        | `string`                    | `/openapi/v3`                          |
| `MANAGEMENT_SERVER_PORT`         | Management Server port. (Will be used to expose actuator endpoints.)   | `int`                       | `8081`                                 |
| `DATASOURCE_URL`                 | Spring Boot Datasource connection URL.                                 | `string`                    | -                                      |
| `DATASOURCE_DRIVER`              | Spring Boot Datasource driver class name.                              | `string`                    | -                                      |
| `DATASOURCE_USERNAME`            | Spring Boot Datasource username.                                       | `string`                    | -                                      |
| `DATASOURCE_PASSWORD`            | Spring Boot Datasource password.                                       | `string`                    | -                                      |
| `JPA_DDL_AUTO`                   | JPA DDL automation setting.                                            | `string`                    | `update`                               |
| `JPA_HIBERNATE_DIALECT`          | JPA Hibernate dialect.                                                 | `string`                    | -                                      |
| `SWAGGER_UI_OAUTH2_REDIRECT_URL` | Swagger UI OAuth2 redirect URL.                                        | `string`                    | `/oauth2-redirect.html`                |
| `SERVER_SERVLET_CONTEXT_PATH`    | Servelt context path.                                                  | `string`                    | `/`                                    |
| `API_SERVER_URL`                 | API Server url for OpenAPI requests.                                   | `string`                    | `https://api.vacations.iperka.com/v1/` |
| `MAIL_HOST`                      | Mail Server host.                                                      | `string`                    | `localhost`                            |
| `MAIL_PORT`                      | Mail Server port.                                                      | `number`                    | `25`                                   |
| `MAIL_USERNAME`                  | Mail Server username.                                                  | `string`                    | `myUser`                               |
| `MAIL_PASSWORD`                  | Mail Server password.                                                  | `string`                    | `myPassword`                           |
| `MAIL_SMTP_AUTH`                 | Mail Server requires authentication.                                   | `boolean`                   | `true`                                 |
| `MAIL_SMTP_STARTTLS_ENABLED`     | Mail Server allows `STARTTLS` connections.                             | `boolean`                   | `true`                                 |
| `MAIL_FROM_ADDRESS`              | Will send Mails from this address.                                     | `email`                     | `no-reply@iperka.com`                  |
| `MAIL_FROM_NAME`                 | Will send Mails from this name.                                        | `string`                    | `iperka`                               |
| `GOOGLE_RECAPTCHA_SECRET`        | Google Recaptcha secret for validating requests.                       | `string`                    | `reCAPTCHA_site_secret`                |
| `ONE_SIGNAL_ENABLED`             | If set to `true` the app will send push notifications.                 | `boolean`                   | `false`                                |
| `ONE_SIGNAL_APP_ID`              | App Id provided by one signal.                                         | `string`                    | `MY_APP_ID`                            |
| `ONE_SIGNAL_API_KEY`             | Api KEY provided by one signal.                                        | `string`                    | `MY_API_KEY`                           |
