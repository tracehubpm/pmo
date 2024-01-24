[![EO principles respected here](https://www.elegantobjects.org/badge.svg)](https://www.elegantobjects.org)
[![DevOps By Rultor.com](https://www.rultor.com/b/ac-californium/api)](https://www.rultor.com/p/ac-californium/api)
[![We recommend IntelliJ IDEA](https://www.elegantobjects.org/intellij-idea.svg)](https://www.jetbrains.com/idea/)

[![mvn](https://github.com/tracehubpm/pmo/actions/workflows/maven.yml/badge.svg)](https://github.com/tracehubpm/pmo/actions/workflows/maven.yml)
[![codecov](https://codecov.io/gh/tracehubpm/pmo/graph/badge.svg?token=QLRSWBDRBS)](https://codecov.io/gh/tracehubpm/pmo)
[![PDD status](http://www.0pdd.com/svg?name=tracehubpm/pmo)](http://www.0pdd.com/p?name=tracehubpm/pmo)

Project architect: [@hizmailovich](https://github.com/hizmailovich)

Project registry, facilities and its governance.

### How to run?

Before you start the app locally, you need to run Keycloak and PostgreSQL using such command:

```bash
$ docker-compose up -d
```

### How to contribute?

Fork repository, make changes, send us a [pull request](https://www.yegor256.com/2014/04/15/github-guidelines.html).
We will review your changes and apply them to the `master` branch shortly,
provided they don't violate our quality standards. To avoid frustration,
before sending us your pull request please run full Maven build:

```bash
$ mvn clean install
```

You will need Maven 3.8.7+ and Java 17+.

All the things above will be run by [Rultor.com](http://rultor.com/)
and CI [gate](https://github.com/tracehub/pmo/actions).
