# fortune-web

A web app that prints a random epigram (like the `fortune` command-line tool, but on the web).

You can:
1. Manually refresh the epigram
2. Toggle auto refreshing
3. Add your own epigram

It also comes with a lot of preloaded epigrams from [fortune-mod](https://github.com/shlomif/fortune-mod/tree/master/fortune-mod/datfiles).

* Frontend: simple html + javascript
* Backend: Quarkus + postgres

## Requirements
* Java 25
* Docker

## Running it in dev mode

```shell script
./mvnw quarkus:dev
```

## Run with docker compose

Note that I only tested it with podman.

```shell script
./mvnw package -DskipTests
docker compose -f docker-compose.yaml up
```