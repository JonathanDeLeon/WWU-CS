# OS Lab Docker Setup

This project is a Docker Setup for the Operating Systems Lab. So instead of running a full VM, this runs Chickadee in a Docker container.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

What things you need to install the software and how to install them

```
Docker
An internet Connection
```

### Installing

All you need to do to run this project is to run the docker build SH script.

```
./docker-build.sh -b
```

* The `-b` flag rebuilds the image. This needs done only when the dockerfile is changed.

* To remove the current container and run a new one, run with the `-r` flag
	* WARNING: This will delete your current instance of the container. You will lose all changes you've made to the container.

* After the initial running of the command above, the most common use case will be with both the `-r` and `-b` flags.

```
./docker-build.sh -r -b
```
### Usage

To enter the container, run the command:

```
docker exec -it chickadee_container "/bin/bash"
```

Just type `exit` to exit the container.

## Built With

* [Docker](https://docs.docker.com/) - The software for the Container infrastructure
* [Ubuntu](https://hub.docker.com/_/ubuntu/) - The OS running in the Docker container to run the Chickadee emulator.

## Contributing

Feel free to fork this project.
Hit me up with a pull request to contribute.

## Authors

* **Ethan Beaver** - *Project Creator* - [Ethan Beaver](https://github.com/ethanbeaver)
