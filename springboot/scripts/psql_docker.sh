#!/bin/sh

# script usage
# ./scripts/psql_docker.sh start|stop|create [db_username][db_password]

# Capture CLI arguments
cmd=$1
db_username=$2
db_password=$3

# start docker if docker server is not running
sudo systemctl status docker || systemctl start docker

# Check container status (try the following cmds on terminal)
docker container inspect jrvs-pgspring
container_status=$?

# User switch case to handle create|stop|start opetions
case $cmd in
  create)

  # Check if the container is already created
  if [ $container_status -eq 0 ]; then
		echo 'Container already exists'
		exit 1
	fi

  # Check # of CLI arguments
  if [ $# -ne 3 ]; then
    echo 'Create requires username and password'
    exit 1
  fi

  # Create container
	docker volume create pgjdbcdata
  # Start the container
	docker run --name jrvs-pgspring -e POSTGRES_PASSWORD=$db_password  \
	-e POSTGRES_USER=$db_username -d -v pgspringdata:/var/lib/postgresql/data \
	-p 5432:5432 postgres:16-alpine
  # `$?` is the exit code of the previous command
	exit $?
	;;

  start|stop)
  # Check instance status; exit 1 if container has not been created
  if [ $container_status -eq 1 ]; then
   echo 'Container does not exist'
   exit 1
  fi

  # Start or stop the container
	docker container $cmd jrvs-pgspring
	exit $?
	;;

  *)
	echo 'Illegal command'
	echo 'Commands: start|stop|create'
	exit 1
	;;
esac