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

L'aplicació vol cobrir la necessitat de registrar dades d'aquestes tres àrees de la rehabilitació. Mitjançant una API de les màquines INDIBA i dels dispositius de PNI, la base de dades de l'aplicació es pot omplir amb aquesta informació.

Si no es pot accedir a les API o cal afegir informació sobre la rehabilitació esportiva, els fisioterapeutes poden introduir els detalls de cada sessió, com els paràmetres de tractament, notes o durada, a través d'un formulari al frontend. Aquestes dades s'enviaran al sistema backend per guardar-les.

El sistema també permet consultar l'historial complet d'un pacient, incloent les sessions, la PNI i la rehabilitació esportiva. Això facilita un seguiment detallat del procés i ajuda a prendre decisions clíniques basades en dades.

A més de la funcionalitat en salut, aquest TFG es basa en l'ús d'un conjunt d'arquitectures i bones pràctiques de desenvolupament:

- Arquitectura Hexagonal: Per aïllar la lògica de negoci dels detalls externs, assegurant millor escalabilitat i fàcil manteniment.
- DDD (Domain-Driven Design): Juntament amb l'anterior, per modelar l'aplicació al voltant del domini real de la fisioteràpia.
- TDD (Test-Driven Development): Per guiar el desenvolupament a través de proves abans de fer el codi, garantint la robustesa.
- Principis SOLID: Per garantir un disseny de codi net i flexible.
- CI/CD: Per establir un flux de treball automatitzat d'integració i desplegament.

## Project structure

```
DanielBarbancho_TFG/
├── backend/
│   └── Fisioterapia_TFG/
│       ├── adapter/                  # Adaptadors d'entrada (REST) i sortida (persistència)
│       │   └── src/main/java/com/tfg/adapter/
│       │       ├── in/
│       │       │   ├── auditaspect/  # Aspecte AOP per registrar auditoria automàticament
│       │       │   ├── rest/         # Controladors REST, un per domini
│       │       │   │   ├── auditlog/
│       │       │   │   ├── indiba/
│       │       │   │   ├── patient/
│       │       │   │   ├── physiotherapist/
│       │       │   │   ├── pni/
│       │       │   │   ├── polar/
│       │       │   │   ├── statistics/
│       │       │   │   └── trainingsession/
│       │       │   └── scheduler/polar/  # Sincronització periòdica amb l'API Polar
│       │       └── out/
│       │           ├── mail/         # Enviament de correus (reset de contrasenya)
│       │           ├── persistence/  # Repositoris JPA (PostgreSQL) i MongoDB
│       │           ├── polar/        # Client de l'API Polar
│       │           └── springsecurity/  # UserDetails i filtre JWT
│       ├── application/              # Casos d'ús, ports i serveis de domini
│       │   └── src/main/java/com/tfg/
│       │       ├── exceptions/       # Excepcions de domini
│       │       ├── pojos/            # Objectes de transferència (paginació, queries)
│       │       ├── port/
│       │       │   ├── in/           # Ports d'entrada (interfícies de cas d'ús)
│       │       │   └── out/          # Ports de sortida (repositoris, serveis externs)
│       │       └── service/          # Implementacions dels casos d'ús
│       ├── bootstrap/                # Punt d'entrada i configuració de l'aplicació
│       └── model/                    # Entitats i objectes de valor del domini pur
├── frontend/
│   └── src/
│       ├── api/                      # Client HTTP centralitzat (axios)
│       ├── components/               # Components reutilitzables organitzats per domini
│       │   ├── auth/                 # Botons, camps i targetes del flux d'autenticació
│       │   ├── layout/               # DashboardLayout, Header i Sidebar
│       │   └── patient/              # Tabs de detall (INDIBA, PNI, entrenament, estadístiques)
│       ├── contexts/                 # Context d'autenticació (AuthContext)
│       ├── pages/                    # Pàgines de l'aplicació, una per ruta
│       ├── services/                 # Capa de servei per domini (crida a l'API backend)
│       └── utils/                    # Utilitats transversals (JWT, exportació CSV)
└── .github/
    └── workflows/                    # Pipeline CI/CD (GitHub Actions)
```

| Mòdul / Carpeta | Descripció |
|-----------------|-----------|
| `adapter/in/rest` | Controladors REST. Un controlador per cas d'ús, sense lògica de negoci |
| `adapter/in/auditaspect` | Aspecte AOP que intercepta crides als serveis i persiteix logs d'auditoria a MongoDB |
| `adapter/in/scheduler` | Scheduler de Spring que sincronitza dades de l'API Polar periòdicament |
| `adapter/out/persistence` | Repositoris JPA per a PostgreSQL i repositoris MongoDB per als logs |
| `adapter/out/springsecurity` | Implementació de `UserDetails` i filtre d'autenticació JWT |
| `application/port/in` | Interfícies dels casos d'ús que el domini exposa cap enfora |
| `application/port/out` | Interfícies de repositoris i serveis externs que el domini necessita |
| `application/service` | Implementació de la lògica de negoci, sense dependències d'infraestructura |
| `model` | Entitats pures de domini sense anotacions de framework |
| `bootstrap` | Launcher principal, `application.properties` i fitxer `.env` |
| `frontend/pages` | Login, registre, llistat de pacients, detall de pacient, sessions, auditoria |
| `frontend/services` | Una capa de servei per domini: auth, patient, indiba, pni, training, statistics, auditLog |

## Prerequisites

- IntelliJ Idea Ultimate
- Java Development Kit 21 o superior
- Docker Desktop
- Git CLI o Github Desktop
- Maven 3.8.7 o superior
- Node.js LTS (v20 o superior)
- npm (inclòs amb Node.js)

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

<h3> Iniciar el frontend </h3>

Obre una nova terminal i executa:

```bash
cd frontend
npm install
npm run dev
```

El frontend estarà disponible a `http://localhost:5173`.

## Dades de prova (Seed Data)

El fitxer `backend/Fisioterapia_TFG/bootstrap/src/main/resources/data.sql` s'executa automàticament en arrencar l'aplicació amb el perfil `dev`. Insereix les següents entitats de prova:

### Fisioterapeutes (`psychiatrists`)

| ID | Nom | Cognoms | Email | Rols | Contrasenya |
|----|-----|---------|-------|------|-------------|
| 1 | Laura | Martínez | laura.martinez@fisio.com | ADMIN, USER | `Admin1234!` |
| 2 | Carlos | López | carlos.lopez@fisio.com | USER | `Admin1234!` |

### Pacients (`patients`)

| ID | Nom legal | Cognoms | DNI | Gènere | Email |
|----|-----------|---------|-----|--------|-------|
| 1 | Ana | García Ruiz | 12345678A | FEMALE | ana.garcia@email.com |
| 2 | Miquel | Puig Torres | 23456789B | MALE | miquel.puig@email.com |
| 3 | Alex | Fernández | 34567890C | NONBINARY | alex.fernandez@email.com |
| 4 | Sara | Gómez Molina | 45678901D | FEMALE | sara.gomez@email.com |
| 5 | David | Navarro Blanco | 56789012E | MALE | david.navarro@email.com |

### Sessions INDIBA (`indiba_sessions`)

| ID | Pacient | Data | Àrea | Mode | Intensitat | Fisioterapeuta |
|----|---------|------|------|------|-----------|----------------|
| 1 | Ana García | 2026-01-10 | Lumbar | CAPACITIVE | 3.5 | Laura |
| 2 | Ana García | 2026-01-17 | Lumbar | RESISTIVE | 4.0 | Laura |
| 3 | Miquel Puig | 2026-01-12 | Genoll dret | DUAL | 3.0 | Carlos |
| 4 | Alex Fernández | 2026-02-05 | Espatlla | CAPACITIVE | 2.5 | Laura |
| 5 | Sara Gómez | 2026-02-18 | Cervical | RESISTIVE | 3.0 | Carlos |

### Informes PNI (`pni_reports`)

| ID | Pacient | Data | Hores de son | HRV | Càrrega ANS | Puntuació son |
|----|---------|------|-------------|-----|------------|---------------|
| 1 | Ana García | 2026-01-09 | 7.5 | 58.0 | 72 | 80 |
| 2 | Ana García | 2026-01-16 | 8.0 | 62.0 | 65 | 85 |
| 3 | Miquel Puig | 2026-01-11 | 6.0 | 45.0 | 80 | 65 |
| 4 | Alex Fernández | 2026-02-04 | 7.0 | 55.0 | 70 | 75 |
| 5 | Sara Gómez | 2026-02-17 | 5.5 | 40.0 | 85 | 60 |
| 6 | David Navarro | 2026-03-01 | 8.5 | 70.0 | 55 | 90 |
| 7 | David Navarro | 2026-03-08 | 8.0 | 68.0 | 58 | 88 |

### Sessions d'entrenament (`training_sessions`) i exercicis

| Sessió ID | Pacient | Data | Exercicis |
|-----------|---------|------|-----------|
| 1 | Miquel Puig | 2026-01-14 | Sentadilla (3 sèries), Press banca (2 sèries) |
| 2 | Miquel Puig | 2026-01-21 | Peso muerto (2 sèries) |
| 3 | David Navarro | 2026-03-03 | Remo amb barra (2 sèries) |
| 4 | David Navarro | 2026-03-10 | Extensió de quàdriceps (2 sèries) |

## API Keys Configuration

Copia el fitxer d'exemple i omple les teves credencials:

```bash
cp backend/Fisioterapia_TFG/bootstrap/src/main/resources/.env.example \
   backend/Fisioterapia_TFG/bootstrap/src/main/resources/.env
```

| Variable | Requerida | Descripció |
|----------|-----------|-----------|
| `POSTGRES_USER` | ✅ | Usuari de la base de dades PostgreSQL |
| `POSTGRES_PASSWORD` | ✅ | Contrasenya de la base de dades PostgreSQL |
| `POSTGRES_DB` | ✅ | Nom de la base de dades PostgreSQL |
| `MONGO_INITDB_ROOT_USERNAME` | ✅ | Usuari root de MongoDB |
| `MONGO_INITDB_ROOT_PASSWORD` | ✅ | Contrasenya root de MongoDB |
| `APPLICATION_SECRET_KEY_JWT` | ✅ | Clau secreta per signar els tokens JWT |
| `POLAR_CLIENT_ID` | ✅ | Client ID de l'API Polar (dispositius de PNI) |
| `POLAR_CLIENT_SECRET` | ✅ | Client Secret de l'API Polar |
| `MAIL_USERNAME` | ✅ | Adreça de correu per enviar notificacions (reset de contrasenya) |
| `MAIL_PASSWORD` | ✅ | Contrasenya o token d'aplicació del compte de correu |

## Application Calls

La documentació interactiva completa està disponible a:

```
http://localhost:8080/swagger-ui/index.html
```

Els endpoints estan protegits amb JWT excepte els marcats com a públics. La capçalera requerida és `Authorization: Bearer <token>`.

### Autenticació (`/physiotherapist`)

| Mètode | Endpoint | Descripció | Auth |
|--------|----------|-----------|------|
| `POST` | `/physiotherapist/login` | Inici de sessió, retorna token JWT | Públic |
| `POST` | `/physiotherapist/register` | Registre d'un nou fisioterapeuta | 🔒 ADMIN |
| `GET` | `/physiotherapist/{physiotherapistId}` | Obté dades d'un fisioterapeuta | 🔒 ADMIN |

### Reset de contrasenya (`/password`)

| Mètode | Endpoint | Descripció | Auth |
|--------|----------|-----------|------|
| `POST` | `/password/forgot` | Envia correu de reset de contrasenya | Públic |
| `POST` | `/password/reset` | Aplica la nova contrasenya amb el token rebut | Públic |

### Pacients (`/patients`)

| Mètode | Endpoint | Descripció | Auth |
|--------|----------|-----------|------|
| `POST` | `/patients/create` | Crea un nou pacient | 🔒 USER |
| `GET` | `/patients/list` | Llista tots els pacients (paginat) | 🔒 USER |
| `GET` | `/patients/export` | Exporta tots els pacients (CSV) | 🔒 USER |
| `GET` | `/patients/{patientId}` | Obté les dades d'un pacient | 🔒 USER |
| `PUT` | `/patients/{patientId}` | Actualitza les dades d'un pacient | 🔒 USER |

### Sessions INDIBA (`/indiba`)

| Mètode | Endpoint | Descripció | Auth |
|--------|----------|-----------|------|
| `POST` | `/indiba/create` | Crea una nova sessió INDIBA | 🔒 USER |
| `GET` | `/indiba/export` | Exporta totes les sessions (CSV) | 🔒 USER |
| `GET` | `/indiba/session/{sessionId}` | Obté una sessió INDIBA per ID | 🔒 USER |
| `GET` | `/indiba/{patientId}` | Llista les sessions INDIBA d'un pacient | 🔒 USER |

### Informes PNI (`/pni`)

| Mètode | Endpoint | Descripció | Auth |
|--------|----------|-----------|------|
| `GET` | `/pni/export` | Exporta tots els informes PNI (CSV) | 🔒 USER |
| `GET` | `/pni/report/{reportId}` | Obté un informe PNI per ID | 🔒 USER |
| `GET` | `/pni/{patientId}` | Llista els informes PNI d'un pacient | 🔒 USER |

### Sessions d'entrenament (`/training-session`)

| Mètode | Endpoint | Descripció | Auth |
|--------|----------|-----------|------|
| `POST` | `/training-session/{patientId}/create` | Crea una sessió d'entrenament per a un pacient | Públic |
| `GET` | `/training-session/export` | Exporta totes les sessions (CSV) | 🔒 USER |
| `GET` | `/training-session/session/{sessionId}` | Obté una sessió d'entrenament per ID | 🔒 USER |
| `GET` | `/training-session/{patientId}` | Llista les sessions d'entrenament d'un pacient | 🔒 USER |

### Estadístiques (`/statistics`)

| Mètode | Endpoint | Descripció | Auth |
|--------|----------|-----------|------|
| `GET` | `/statistics/{patientId}/{exerciseName}/workload-progression` | Progressió de càrrega per exercici | 🔒 USER |
| `GET` | `/statistics/{patientId}/{year}/patient-transition-ratio` | Ràtio de transició de fases anual | 🔒 USER |

### Logs d'auditoria (`/auditlogs`)

| Mètode | Endpoint | Descripció | Auth |
|--------|----------|-----------|------|
| `GET` | `/auditlogs/list` | Llista tots els logs d'auditoria | 🔒 ADMIN |

### Integració Polar (`/api/auth/polar`)

| Mètode | Endpoint | Descripció | Auth |
|--------|----------|-----------|------|
| `GET` | `/api/auth/polar/authorize` | Inicia el flux OAuth2 amb Polar | Públic |
| `GET` | `/api/auth/polar/callback` | Callback OAuth2, rep el token de Polar | Públic |

## Arquitecture Overview

El projecte implementa una **arquitectura hexagonal (ports i adaptadors)** combinada amb **DDD**, estructurada en quatre mòduls Maven independents. La lògica de negoci (`model` i `application`) no conté cap dependència d'infraestructura: és el mòdul `adapter` qui connecta el món exterior (HTTP, bases de dades, APIs externes) amb el domini a través dels ports definits a `application`.

```
  Frontend (React)
       │ HTTP / REST
       ▼
┌─────────────────────────────────────────────────────┐
│  adapter                                            │
│  ┌──────────────────┐   ┌──────────────────────┐   │
│  │  REST Controllers│   │  Scheduler (Polar)   │   │
│  │  (entrada)       │   │  AOP Audit Aspect    │   │
│  └────────┬─────────┘   └──────────┬───────────┘   │
│           │ port.in                │               │
│  ┌────────▼────────────────────────▼───────────┐   │
│  │  application — Serveis i casos d'ús         │   │
│  │  (sense dependències d'infraestructura)      │   │
│  │  ┌──────────────────────────────────────┐   │   │
│  │  │  model — Entitats de domini pures    │   │   │
│  │  └──────────────────────────────────────┘   │   │
│  └────────────────────┬────────────────────────┘   │
│           port.out    │                            │
│  ┌────────────────────▼────────────────────────┐   │
│  │  Repositoris JPA · MongoDB · Mail · Polar   │   │
│  └─────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────┘
       │                    │               │
  PostgreSQL             MongoDB         Polar API
```

La seguretat es gestiona amb **JWT**: Spring Security intercepta totes les peticions (excepte les d'autenticació) i valida el token abans d'arribar als controladors. El pipeline **CI/CD** de GitHub Actions executa els tests, genera el badge de cobertura JaCoCo i publica les imatges Docker (backend i frontend) al GitHub Container Registry (GHCR).

## Domain Concepts

### Pacient (`Patient`)
Persona que rep tractament al centre de fisioteràpia. Conté dades personals i és l'entitat central al voltant de la qual s'organitzen totes les sessions i informes.

### Fisioterapeuta (`Physiotherapist`)
Professional que atorga i registra els tractaments. Té rol `USER` o `ADMIN`. Les credencials s'utilitzen per autenticar-se a l'aplicació via JWT.

### Sessió INDIBA (`IndibaSession`)
Tractament de radiofreqüència aplicat a un pacient. Registra la data, l'àrea corporal tractada, el mode (CAPACITIVE, RESISTIVE, DUAL) i la intensitat. Es pot crear manualment o importar des de l'API Polar.

### Informe PNI (`PniReport`)
Informe de psiconeuroimmunologia vinculat a un pacient. Recull indicadors de recuperació: hores de son, HRV (variabilitat de la freqüència cardíaca), càrrega del sistema nerviós autònom (ANS) i puntuació de son. Les dades provenen del dispositiu Polar o s'introdueixen manualment.

### Sessió d'entrenament (`TrainingSession`)
Sessió de rehabilitació esportiva amb un conjunt d'exercicis. Cada exercici té nom, sèries, repeticions i càrrega. Es registra manualment pel fisioterapeuta.

### Log d'auditoria (`AuditLog`)
Registre immutable de cada acció executada sobre el sistema (creació, modificació, eliminació). Es genera automàticament via AOP i es persisteix a MongoDB. Només visible per als usuaris amb rol `ADMIN`.

## Test Coverage
<img width="1534" height="755" alt="image" src="https://github.com/user-attachments/assets/0e5751d6-11bf-42f8-9d84-2cc80fd22a35" />
<img width="1363" height="473" alt="image" src="https://github.com/user-attachments/assets/6e91fdd5-986a-46d0-b59d-6dc44ed711f2" />
<img width="1364" height="337" alt="image" src="https://github.com/user-attachments/assets/301e5178-a63e-4e09-bed9-0cca74d9d7dd" />



