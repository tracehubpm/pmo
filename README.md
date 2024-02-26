[![EO principles respected here](https://www.elegantobjects.org/badge.svg)](https://www.elegantobjects.org)
[![DevOps By Rultor.com](https://www.rultor.com/b/ac-californium/api)](https://www.rultor.com/p/ac-californium/api)
[![We recommend IntelliJ IDEA](https://www.elegantobjects.org/intellij-idea.svg)](https://www.jetbrains.com/idea/)

[![mvn](https://github.com/tracehubpm/pmo/actions/workflows/mvn.yml/badge.svg)](https://github.com/tracehubpm/pmo/actions/workflows/mvn.yml)
[![codecov](https://codecov.io/gh/tracehubpm/pmo/graph/badge.svg?token=rnRZ3e6s6e)](https://codecov.io/gh/tracehubpm/pmo)
[![PDD status](http://www.0pdd.com/svg?name=tracehubpm/pmo)](http://www.0pdd.com/p?name=tracehubpm/pmo)

[![Hits-of-Code](https://hitsofcode.com/github/tracehubpm/pmo)](https://hitsofcode.com/view/github/tracehubpm/pmo)

Project architect: [@hizmailovich](https://github.com/hizmailovich)

Project registry, facilities and its governance.

### How to use?

Project Management Office (PMO) is a RESTful JSON API with ability to
manipulate with projects. To check this RESTful API, all you need is Swagger Docs,
it can be found here: `/swagger-ui/index.html`.

**Functionality:**

* Allows to log in using login and password.
* Allows to log in using such social coding platforms as GitHub, GitLab, and Bitbucket.
* Allows to create a project.
* Creates and queries tickets. 
* Creates and manages [secrets](https://en.wikipedia.org/wiki/Environment_variable), represented as simple `key = value`
pair, where value will be encrypted using [jasypt](http://www.jasypt.org).

After project creation bot [@tracehubgit](https://github.com/tracehubgit) will be invited
to the repository and a `new` label for issues will be added. Moreover, a webhook for `push` events will be
created to notify PMO about changes in the repository.

### How to run?

Before you start the app locally, you need to run Keycloak and PostgreSQL using such command:

```bash
$ docker-compose up -d
```

Then you should update client secrets for identity providers in Keycloak using following steps:

1. Open Keycloak admin console.
2. Choose `pmo` realm.
3. Go to Identity Providers.
4. Choose appropriate identity provider and update client secret.
5. Save changes.

### How to contribute?

Fork repository, make changes, send us a [pull request](https://www.yegor256.com/2014/04/15/github-guidelines.html).
We will review your changes and apply them to the `master` branch shortly,
provided they don't violate our quality standards. To avoid frustration,
before sending us your pull request please run full Maven build:

```bash
$ mvn clean install -DskipITs
```

You will need Maven 3.8.7+ and Java 17+.

If you want to run an integration tests to check whole system, run:

```bash
$ mvn clean install -DGithubToken=...
```

You should provide GitHub [token](https://github.com/settings/tokens) as value for `GithubToken` variable.
Token must be granted with write permissions to `hizmailovich/draft`.

Ensure that you have a running [Docker](https://docs.docker.com/config/daemon/troubleshoot/) in your environment.
If you test it locally, you can use [Docker Desktop](https://www.docker.com/products/docker-desktop).

All the things above will be run by [Rultor.com](http://rultor.com/)
and CI [gate](https://github.com/tracehub/pmo/actions).
