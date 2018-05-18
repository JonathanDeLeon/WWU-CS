# Start with a Python image.
FROM ubuntu:latest

# Install some necessary things.
RUN apt-get update
RUN apt-get install -y git make vim build-essential net-tools openssh-server qemu gcc-7 g++-7 gdb

# Copy all our files into the image.
RUN mkdir /chickadee
WORKDIR /chickadee
COPY ./chickadee /chickadee/

EXPOSE 8000

# Specify the command to run when the image is run.
CMD bash 

