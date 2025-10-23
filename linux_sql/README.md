# Linux Cluster Monitoring Agent
This project implements a Linux cluster monitoring agent that collects and stores 
hardware and resource usage data. It helps system 
administrators track CPU, memory, and disk performance. The intention is that this can be 
scaled across multiple servers in a distributed 
environment. The system consists of two Bash scripts: one that records hardware 
information and another that periodically logs server usage metrics using crontab. 
Data is stored in a PostgreSQL database running inside a Docker container, allowing easy 
deployment from a shell script. Git is used for version control. The design can be 
scaled by enabling multiple nodes to 
report to a single database instance, providing real-time visibility into cluster 
health and performance through simple SQL queries.

## Setup
Follow the steps below to set up and run the Linux Cluster Monitoring Agent.
### 1.1 If necessary, create the Docker container for the PostgreSQL instance

```bash
./scripts/psql_docker.sh create [db_username] [db_password]
```


### 1.2 Start a PostgreSQL instance using Docker
```bash
./scripts/psql_docker.sh start
```

### 2. Create the database tables
```bash
psql -h localhost -U [db_username] -d host_agent -f sql/ddl.sql
```

### 3. Insert hardware specifications data
```bash
./scripts/host_info.sh localhost 5432 host_agent [db_username] [db_password]
```
### 4. To automate periodic data collection (every minute):
```bash
crontab -e
```
Then add the following line to the `crontab` file:
```crontab
* * * * * bash /full/path/to/scripts/host_usage.sh localhost 5432 host_agent [db_username] [db_password] > /tmp/host_usage.log
```
## Implementation

### Scalable Architecture
![Linux Cluster Monitoring Diagram](./assets/cluster.jpg)

### Scripts
#### 1. psql_docker.sh
Manages the PostgreSQL database container. It can create, start, or stop the Docker 
container used for storing monitoring data.
```bash
# Create and start a PostgreSQL container
./scripts/psql_docker.sh create [db_username] [db_password]

# Start an existing PostgreSQL container
./scripts/psql_docker.sh start

# Stop the PostgreSQL container
./scripts/psql_docker.sh stop
```
#### 2. host_info.sh
Collects hardware specifications (CPU, memory, etc.) of the host machine and inserts
them into the host_info table.
```bash
./scripts/host_info.sh [psql_host] [psql_port] [db_name] [psql_user] [psql_password]
```
Example:
```bash
./scripts/host_info.sh "localhost" 5432 "host_agent" "postgres" "password"
```
#### 3. host_usage.sh
Collects real-time resource usage data (CPU, memory, disk) and inserts it into the 
host_usage table.
```bash
./scripts/host_usage.sh [psql_host] [psql_port] [db_name] [psql_user] [psql_password]
```
Example:
```bash
./scripts/host_usage.sh "localhost" 5432 "host_agent" "postgres" "password"
```
#### 4. Crontab
Used to automate periodic execution of host_usage.sh for continuous monitoring.

Edit the crontab file:
```bash
crontab -e
```
Run `host_usage.sh` every minute
```crontab
* * * * * bash /full/path/to/scripts/host_usage.sh localhost 5432 host_agent [db_username] [db_password] > /tmp/host_usage.log
```
#### 5. ddl.sql
SQL file that defines the database schema, including the host_info and host_usage tables.
```bash
psql -h localhost -U [db_username] -d host_agent -f sql/ddl.sql
```

### Database Schema
#### 1. host_info
   		
| Column Name | Data Type | Description                                      |
|----------|----------|--------------------------------------------------|
| id | SERIAL   | Unique identifier for each host (primary key).   |
| hostname | VARCHAR  | Name of the host machine.                        |
| cpu_number | INT2     | Number of CPU cores on the host.                 |
| cpu_architecture | VARCHAR  | CPU architecture type (e.g., x86_64).            |
| cpu_model | VARCHAR  | Model name of the CPU.                           |
| cpu_mhz | FLOAT8   | CPU clock speed in MHz.                          |
| l2_cache | INT4     | Size of the CPU L2 cache (in KB).                |
| total_mem | INT4     | Total available memory on the host (in KB).      |
| timestamp | TIMESTAMP | Time when the hardware information was recorded. |

#### 2. host_usage

| Column Name | Data Type | Description                                      |
|----------|-----------|--------------------------------------------------|
| timestamp | TIMESTAMP | Time when the usage data was collected. |
| host_id | SERIAL    | Foreign key referencing host_info.id. |
| memory_free | INT4      | Amount of free memory (in MB). |
| cpu_idle | INT2      | Percentage of CPU time spent idle. |
| cpu_kernel | INT2      | Percentage of CPU time spent in kernel mode. |
| disk_io | INT4      | Number of disk I/O operations. |
| disk_available | INT4      | Available disk space (in MB). |

## Testing
To verify the functionality of the Bash scripts and the ddl.sql script:
#### DDL Testing:
1. I used DROP TABLE commands to remove existing tables (host_usage and host_info) and then ran ddl.sql to recreate them.

2. I confirmed that the tables were created correctly using SELECT * FROM [table].

#### Data Insertion Testing:
1. I inserted test data into the tables using the Bash scripts (host_usage.sh and host_info.sh).

2. After each insertion, I ran SELECT * FROM [table] to verify that the data appeared correctly and matched the expected format.

**Result:** All scripts worked as intended, creating tables and inserting data reliably, allowing the system to track host information and usage accurately.

## Deployment

## Improvements
#### 1. Scale the setup to run on multiple host machines to gather centralized data for a distributed system.
#### 2. Automate the PostgreSQL Docker container to start when the host starts/reboots
#### 3. Add alerting or logging for resource failures.
#### 4. Add automated backups for the PostgreSQL database.