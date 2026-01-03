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

## Architecture
![Component Diagram](./assets/trading-arch.png)
### Controller Layer (REST APIs)
The controller layer is responsible for handling incoming HTTP requests from clients. It defines REST endpoints using annotations such as @GetMapping and@PostMapping. Controllers parse request parameters, path variables, and request bodies and delegate all business logic to the service layer. Controllers also translate exceptions into appropriate HTTP responses.
### Service Layer (Business Logic)
The service layer contains the core business logic of the application. It coordinates workflows across multiple repositories and external services while enforcing business rules (e.g., checking account balance before buying, validating trader state before deletion). Services are annotated with @Service and are managed by Spring's IoC container. They are responsible for composing data into views such as TraderAccountView and PortfolioView and ensure that controllers remain thin and that logic is testable.
### JPA Repository Layer (Data Access)
The JPA Repository layer abstracts database access using Spring Data JPA. Interfaces such as TraderJpaRepository, AccountJpaRepository, and SecurityOrderJpaRepository extend JpaRepository, which provides CRUD operations out of the box (e.g., save, findById, findAll, delete). Custom query methods are also defined using method naming conventions. This layer interacts directly with PostgreSQL via Hibernate and JDBC but hides SQL complexity from the service layer. For the Position View, the repository is typically read-only.
### Spring Boot: WebServlet / Tomcat and IoC
Spring Boot simplifies application setup and runtime configuration. It embeds Apache Tomcat as the WebServlet container, which handles HTTP request routing, threading, and lifecycle management. Spring?s Inversion of Control (IoC) container manages object creation, dependency injection, and lifecycle using annotations like @Controller, @Service, and @Autowired. This enables loose coupling between components and makes the application easier to test and extend.
### PostgreSQL (PSQL) and Finnhub
PostgreSQL is the persistence layer for the application. It stores traders, accounts, quotes, security orders, and the position view. PostgreSQL runs in a Docker container with a mounted volume to persist data across restarts. Finnhub is an external market data API used to fetch real-time stock information, such as quotes and market status. The application accesses Finnhub through a dedicated MarketDataDao using an HTTP client. Finnhub provides live market data. PostgreSQL stores validated, application-specific state.

## Database
The database schema is defined via an SQL script (sql/ddl.sql). For production or sharing, a custom Docker image (based on postgres:16-alpine) is built that copies the init script to /docker-entrypoint-initdb.d/. On first container startup (with an empty data dir), PostgreSQL runs the script so that the DB is pre-initialized.
### ER Diagram
![ER Diagram](./assets/jrvstrading.png)

## REST API Usage
### Swagger
Swagger is a set of tools and specifications for designing, building, and documenting RESTful APIs. It provides an interactive UI that lets developers explore and test API endpoints in a browser, showing available paths, parameters, request/response formats, and real-time responses, making API development and testing easier and more transparent.

### Quote Controller
QuoteController exposes REST endpoints for managing stock quote data in the trading application. The controller supports creating quotes using live market data from Finnhub, updating existing quotes, refreshing all stored quotes with the latest market prices, and retrieving the current daily list of quotes.
- POST `/quote/finnhub/ticker/{ticker}`: Fetches real-time market data for the given ticker from Finnhub, creates a Quote entity, and persists it in the database.
- PUT `/quote/`: Creates a new quote or updates an existing one using the provided request body.
- PUT `/quote/finnhubMarketData`: Refreshes market data for all stored quotes by calling Finnhub and updating the database.
- GET `/quote/dailyList`: Retrieves all stored quotes representing the current daily market snapshot.

### Trader Account Controller
TraderAccountController exposes REST endpoints for managing traders and their associated accounts in the trading application. It supports creating traders and initializing their accounts, depositing and withdrawing funds, and deleting traders when business constraints are satisfied.
- POST `/trader/firstname/{firstname}/lastname/{lastname}/dob/{dob}/country/{country}/email/{email}`: Creates a new trader and initializes an associated account with a zero balance. `dob` must follow `yyyy-MM-dd` format.
- POST `/trader/`: Creates a trader using a JSON request body.
- DELETE `/trader/traderId/{traderId}`: Deletes a trader and all related data only if account balance is zero and no open positions exist.
- PUT `/trader/deposit/traderId/{traderId}/amount/{amount}`: Deposits funds into a trader?s account.
- PUT `/trader/withdraw/traderId/{traderId}/amount/{amount}`: Withdraws funds from a trader?s account.

### Order Controller
OrderController exposes REST endpoints for submitting and executing market orders in the trading application. It accepts user-defined market orders, validates input, and delegates execution logic to OrderService.
- POST `/order/marketOrder`: Submits a market order to buy or sell a security at the current market price using the provided request body.

### Dashboard Controller
DashboardController exposes read-only REST endpoints that aggregate and present a trader?s account and portfolio information in a dashboard-friendly format.
- GET `/dashboard/profile/traderId/{traderId}`: Retrieves a consolidated view of a trader?s profile and account information.
- GET `/dashboard/portfolio/traderId/{traderId}`: Retrieves a trader?s portfolio, including all open positions.

# Test

# Deployment

# Improvements