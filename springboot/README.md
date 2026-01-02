# Introduction

## Option 1:
Download the `docker-compose.yml`
```bash
# From the directory containing docker-compose.yml:
docker compose up -d
```
This will:

- spin up the database and app containers,

- configure networking between them,

- initialize the database schema (on first run),

- start the server on localhost:8080

You can then access the Swagger-UI interface at `localhost:8080/swagger-ui.html` from a web broswer.


## Option 2: Run the containers separately (manual network)
```bash
# Create a Docker network
docker network create trading-net

# Start DB container in the background

docker run --rm -d --name jrvs-pgspring \
--network trading-net \
-p 5432:5432 \
-v pgspringdata:/var/lib/postgresql/data \
fraserraney/trading-db

# Run the application container
 docker run --rm --network trading-net \
 -p 8080:8080 \
 fraserraney/trading-app
```

## Option 3:
```bash
docker compose -f oci://fraserraney/trading-compose:latest up -d
```

# Implementaiton
## Database
The database schema is defined via an SQL script (sql/ddl.sql). For production or sharing, a custom Docker image (based on postgres:16-alpine) is built that copies the init script to /docker-entrypoint-initdb.d/. On first container startup (with an empty data dir), PostgreSQL runs the script so that the DB is pre-initialized.
### ER Diagram
![ER Diagram](./assets/jrvstrading.png)

## Architecture

## REST API Usage
### Swagger

### Quote Controller

### Trader Account Controller

### Order Controller

### Dashboard Controller

# Test

# Deployment

# Improvements