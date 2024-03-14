if [[ "`docker ps -f status=running -f name=pmo-8080 --format '{{.Names}}'`" == "pmo-8080" ]]
then
  green_port="8081"
  blue_port="8080"
else
  green_port="8080"
  blue_port="8081"
fi
TAG=$(cat tag.txt) PORT=${green_port} docker-compose up -d
echo "Green is running on port ${green_port}"
if [[ "`sudo docker inspect -f {{.State.Running}} pmo-${green_port}`" == "true" ]]
then
  echo "Switching..."
  sudo cp /root/deploy/default-${green_port}.conf /etc/nginx/conf.d/default.conf
  sudo nginx -t
  sudo systemctl reload nginx
  sudo docker stop pmo-${blue_port}
  sudo docker rm pmo-${blue_port}
  echo "Blue is stopped"
else
  echo "Green is not running"
fi