if [[ $1 = "-r" || $2 = "-r" ]]; then
    docker stop chickadee_container
    docker rm chickadee_container
fi
if  [[ $1 = "-b" || $2 = "-b" ]]; then
    docker build . --tag chickadee
fi
docker run -P --name chickadee_container -p 42000:8000 -t -d chickadee

