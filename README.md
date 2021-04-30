[![CI](https://github.com/iperka/vacations.api/actions/workflows/main.yml/badge.svg?branch=main)](https://github.com/iperka/vacations.api/actions/workflows/main.yml)

# Vacations API 📅

Access iperka vacations with the API. This API is built with Java Spring Boot and is built with scalability in mind.

## Table of Contents 🧾

- [Vacations API 📅](#vacations-api-)
  - [Table of Contents 🧾](#table-of-contents-)
  - [Installation 💽](#installation-)
  - [Deployment 🐳](#deployment-)
  - [Usage 🚀](#usage-)
  - [Built With 📦](#built-with-)
  - [Authors 👨‍💻](#authors-)
  - [License 📃](#license-)
  - [Contributing 🤝](#contributing-)
  - [Acknowledgments 🐛](#acknowledgments-)

## Installation 💽

Build a docker image with given Dockerfile.

```bash
$ docker build -t iperka/vacations-api .
```

After docker has completed the build the image can be accessed via docker.

## Deployment 🐳

It is recommended to use this API as docker container within a cluster.

## Usage 🚀

Run docker container locally with "production" profile activated.

```bash
$ docker run -e "SPRING_PROFILES_ACTIVE=production" -p 8080:8080 -t iperka/vacations-api
```

Now the API should be accessible via `http://localhost:8080`.

## Built With 📦

* [Maven](https://maven.apache.org/) - Dependency Management
* [Spring Boot](https://spring.io/) - Java Framework

## Authors 👨‍💻

* **Michael Beutler** - *Initial work* - [MichaelBeutler](https://github.com/MichaelBeutler)

## License 📃

[MIT](https://choosealicense.com/licenses/mit/)

## Contributing 🤝

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## Acknowledgments 🐛

* Ingrate vacations into your calendar.
* Add new workflows to your company.
* Simplify vacations.
* etc...
