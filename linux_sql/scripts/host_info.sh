#!/bin/bash

# Script usage
# ./scripts/host_info.sh [psql_host] [psql_port] [db_name] [psql_user] [psql_password]

# Example
# ./scripts/host_info.sh "localhost" 5432 "host_agent" "postgres" "password"

# setup arguments
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

# validate arguments
if [ "$#" -ne 5 ]; then
    echo "Illegal number of parameters" >&2
    echo "Usage:"
    echo "./scripts/host_info.sh [psql_host] [psql_port] [db_name] [psql_user] [psql_password]"
    exit 1
fi

# save hostname as a variable
hostname=$(hostname -f)

# save the number of CPUs to a variable
lscpu_out=`lscpu`

# parse hardware info
hostname="$hostname"
cpu_number=$(echo "$lscpu_out"  | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out"  | egrep "^Architecture:" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out"  | egrep "^Model name:" |  awk -F': *' '{print $2}' | xargs)
cpu_mhz=$(echo "$cpu_model" | egrep -o '[0-9]+(\.[0-9]+)?' | awk '{printf "%.3f", $1 * 1000}' | xargs)
l2_cache=$(echo "$lscpu_out"  | egrep "^L2 cache:" | egrep -o '[0-9]+' | awk 'NR==2' | xargs)
total_mem=$(cat /proc/meminfo | egrep 'MemTotal' | egrep -o '[0-9]+' |xargs)
timestamp=$(date +"%Y-%m-%d %H:%M:%S") # current timestamp in `2019-11-26 14:40:19` format

# PSQL command: Inserts hardware into host_info table
insert_stmt="INSERT INTO host_info(id, hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, total_mem, \"timestamp\") \
VALUES(1, '$hostname', '$cpu_number', '$cpu_architecture', '$cpu_model', '$cpu_mhz', '$l2_cache', '$total_mem', '$timestamp') \
ON CONFLICT (id) DO UPDATE SET hostname = EXCLUDED.hostname, cpu_number = EXCLUDED.cpu_number, cpu_architecture = EXCLUDED.cpu_architecture, cpu_model = EXCLUDED.cpu_model, cpu_mhz = EXCLUDED.cpu_mhz, l2_cache = EXCLUDED.l2_cache, total_mem = EXCLUDED.total_mem, \"timestamp\" = EXCLUDED.\"timestamp\";"

# set up env var for pql cmd
export PGPASSWORD=$psql_password

# insert hardware info into database
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?