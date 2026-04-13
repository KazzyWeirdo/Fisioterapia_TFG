# Bones pràctiques en enginyeria de software: DDD, TDD, Clean/Hexagonal, CI/CD

## Table of contents

- [Context](context)
- [Project Structure](project_structure)
- [Prerequisites](prerequisites)
- [Getting Started](getting_started)
- [API Keys Configuration](api_keys_configuration)
- [Application Calls](application_calls)
- [Arquitecture Overview](arquitecture_overview)
- [Domain Concepts](domain_concepts)
- [Test Coverage](test_coverage)

## Context

Aquest Treball de Fi de Grau (TFG) se centra en crear un sistema backend perquè els professionals de la fisioteràpia puguin registrar de manera digital i centralitzada les rehabilitacions de llarga durada, com la tendinopatia rotuliana crònica. El sistema inclou tant les sessions de radiofreqüència amb la màquina INDIBA com aspectes de psiconeuroimmunologia (PNI) i rehabilitació esportiva.

L’aplicació vol cobrir la necessitat de registrar dades d’aquestes tres àrees de la rehabilitació. Mitjançant una API de les màquines INDIBA i dels dispositius de PNI, la base de dades de l’aplicació es pot omplir amb aquesta informació.

Si no es pot accedir a les API o cal afegir informació sobre la rehabilitació esportiva, els fisioterapeutes poden introduir els detalls de cada sessió, com els paràmetres de tractament, notes o durada, a través d’un formulari al frontend. Aquestes dades s’enviaran al sistema backend per guardar-les.

El sistema també permet consultar l’historial complet d’un pacient, incloent les sessions, la PNI i la rehabilitació esportiva. Això facilita un seguiment detallat del procés i ajuda a prendre decisions clíniques basades en dades.

A més de la funcionalitat en salut, aquest TFG es basa en l’ús d’un conjunt d’arquitectures i bones pràctiques de desenvolupament:

- Arquitectura Hexagonal: Per aïllar la lògica de negoci dels detalls externs, assegurant millor escalabilitat i fàcil manteniment.
- DDD (Domain-Driven Design): Juntament amb l'anterior, per modelar l’aplicació al voltant del domini real de la fisioteràpia.
- TDD (Test-Driven Development): Per guiar el desenvolupament a través de proves abans de fer el codi, garantint la robustesa.
- Principis SOLID: Per garantir un disseny de codi net i flexible.
- CI/CD: Per establir un flux de treball automatitzat d'integració i desplegament.

## Project structure

```
├── backend
│   └── Fisioterapia_TFG
│       ├── adapter
│       │   └── src
│       │       ├── main
│       │       │   └── java
│       │       │       └── com
│       │       │           └── tfg
│       │       │               └── adapter
│       │       │                   ├── in
│       │       │                   │   └── rest
│       │       │                   │       ├── common
│       │       │                   │       ├── indiba
│       │       │                   │       └── patient
│       │       │                   └── out
│       │       │                       └── persistence
│       │       │                           ├── indiba
│       │       │                           └── patient
│       │       └── test
│       │           └── java
│       │               └── com
│       │                   └── tfg
│       │                       └── adapter
│       │                           ├── in
│       │                           │   └── rest
│       │                           │       ├── indiba
│       │                           │       └── patient
│       │                           └── out
│       │                               └── persistence
│       │                                   ├── indiba
│       │                                   └── patient
│       ├── application
│       │   └── src
│       │       ├── main
│       │       │   └── java
│       │       │       └── com
│       │       │           └── tfg
│       │       │               ├── exceptions
│       │       │               ├── port
│       │       │               │   ├── in
│       │       │               │   │   ├── indiba
│       │       │               │   │   └── patient
│       │       │               │   └── out
│       │       │               │       └── persistence
│       │       │               └── service
│       │       │                   ├── indiba
│       │       │                   └── patient
│       │       └── test
│       │           └── java
│       │               └── application
│       │                   ├── indiba
│       │                   └── patient
│       ├── bootstrap
│       │   └── src
│       │       ├── main
│       │       │   ├── java
│       │       │   │   └── com
│       │       │   │       └── tfg
│       │       │   │           └── bootstrap
│       │       │   └── resources
│       │       └── test
│       │           └── resources
│       └── model
│           └── src
│               ├── main
│               │   └── java
│               │       └── com
│               │           └── tfg
│               │               ├── indiba
│               │               └── patient
│               └── test
│                   └── java
│                       └── com
│                           └── tfg
│                               └── model
│                                   ├── indiba
│                                   └── patient
└── frontend

```

## Prerequisites

- IntelliJ Idea Ultimate
- Java Development Kit 21 o superior
- Docker Desktop
- Git CLI o Github Desktop
- Maven 3.8.7 o superior

## Getting started

<h3> Clonar el repositori </h3>

```
git clone https://github.com/KazzyWeirdo/Fisioterapia_TFG.git
```

<h3> Obrir el projecte en IntelliJ IDEA </h3>

1. Obre IntelliJ IDEA.
2. Selecciona "Open" i navega fins a la carpeta on has clonat el repositori.
3. Selecciona el arxiu `pom.xml` del projecte i fes clic a "Open as Project".
4. IntelliJ carregarà el projecte i baixarà totes les dependències necessàries.

<h3> Configurar les variables d'entorn </h3>

Posa aquesta commanda en una terminal Linux dintre de l'entorn IntelliJ.

```
cp ./backend/Fisioterapia_TFG/bootstrap/src/main/resources/.env.example ./backend/Fisioterapia_TFG/bootstrap/src/main/resources/.env
```
o
```
cp ./bootstrap/src/main/resources/.env.example ./bootstrap/src/main/resources/.env
```
<h3> Configurar la base de dades </h3>

En cas de que estiguis en una altre carpeta, entra dintre de la carpeta del backend del projecte en una terminal Linux de IntelliJ.

```
cd ./backend/Fisioterapia_TFG
```

Si vols obrir els ports de les bases de dades, pots fer un arxiu `docker-compose.override.yml` amb el següent contingut:

```
services:
  postgres-db:
    ports:
      - "5432:5432"

  mongo-db:
    ports:
      - "27017:27017"
```

Pots iniciar els contenidors de la base de dades a docker amb la següent comanda:

```
docker-compose -f ./backend/Fisioterapia_TFG/docker-compose.yml up -d
```

Si ho fas amb l'arxiu `docker-compose.override.yml` també, la comanda seria:

``` 
docker-compose -f ./backend/Fisioterapia_TFG/docker-compose.yml -f ./backend/Fisioterapia_TFG/docker-compose.override.yml up -d
```

<h3> Executar els tests </h3>

Pots executar els tests utilitzant Maven amb la següent comanda:

``` 
mvn clean test
```

## API Keys Configuration

## Application Calls

Un cop has executat els contenidors de Docker, pots accedir als endpoints de l'aplicació mitjançant la següent URL:

``` 
http://localhost:8080/swagger-ui/index.html
```

## Arquitecture Calls
.
## Domain Concepts
.
## Test Coverage
<img width="1270" height="317" alt="image" src="https://github.com/user-attachments/assets/e5e29125-8e1b-4356-b501-a892b335cdda" />
<img width="1392" height="470" alt="image" src="https://github.com/user-attachments/assets/6263c910-2736-4ca9-afda-dcab939a78e1" />
<img width="1445" height="671" alt="image" src="https://github.com/user-attachments/assets/190f6b36-3258-418d-a40b-7bf82540ec90" />
