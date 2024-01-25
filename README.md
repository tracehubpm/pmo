[![EO principles respected here](https://www.elegantobjects.org/badge.svg)](https://www.elegantobjects.org)
[![DevOps By Rultor.com](https://www.rultor.com/b/ac-californium/api)](https://www.rultor.com/p/ac-californium/api)
[![We recommend IntelliJ IDEA](https://www.elegantobjects.org/intellij-idea.svg)](https://www.jetbrains.com/idea/)

[![mvn](https://github.com/tracehubpm/pmo/actions/workflows/mvn.yml/badge.svg)](https://github.com/tracehubpm/pmo/actions/workflows/mvn.yml)
[![codecov](https://codecov.io/gh/tracehubpm/pmo/graph/badge.svg?token=rnRZ3e6s6e)](https://codecov.io/gh/tracehubpm/pmo)
[![PDD status](http://www.0pdd.com/svg?name=tracehubpm/pmo)](http://www.0pdd.com/p?name=tracehubpm/pmo)

Project architect: [@hizmailovich](https://github.com/hizmailovich)

Project registry, facilities and its governance.

### How to use?

Project Management Office (PMO) is a RESTful JSON API with ability to
manipulate with projects. To check this RESTful API, all you need is Swagger Docs,
it can be found here: `/swagger-ui/index.html`.

**Functionality:**

* It allows to log in using login and password.
* It allows to log in using such social networks as Google and GitHub.
* It allows to create a project.

After project creation bot [@tracehubgit](https://github.com/tracehubgit) will be invited
to the repository and a `new` label for issues will be added. Moreover, a webhook for `push` events will be 
created to notify PMO about changes in the repository.

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
