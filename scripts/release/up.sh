if [[ "`docker ps -f status=running -f name=pmo-8080 --format '{{.Names}}'`" == "pmo-8080" ]]
then
  green_port="8081"
else
  green_port="8080"
fi
TAG=$(cat tag.txt) PORT=${green_port} docker-compose up -d
echo "Green is up on port ${green_port}"