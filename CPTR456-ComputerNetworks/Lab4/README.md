# TCP Chat Client

## TCP Socket Programming
We have used UDP socket programming in past labs. As we have learned, UDP provides no
data transfer reliability or ordering guarantees. The connection-based Transmission Control
Protocol, TCP, does provide data transfer reliability and ordering. In this lab, we will
script a simple TCP-based chat client that is able to connect to the chat server described
at http://www.ibm.com/developerworks/linux/tutorials/l-pysocks/section4.html. This site
also includes a good tutorial on Python socket programming which I will introduce at the
beginning of lab.

* Chat server IP: 52.221.30.210
* Chat server port: 27800

> Note: Server no longer available

## Code Python Chat Client Script
Your Python script will duplicate and enhance the functionality of telnet to connect to the
chat server. It must incorporate the requirements listed below. Use the IBM tutorial site
for reference. Your script should be about 20 lines when done.

* Define variables at the beginning of your script that specify the chat server’s IP and
port as well as your name.
* Open a TCP socket and connect to the chat server
* Use select.select to asynchronously listen if your socket has data to read OR
sys.stdin has data.
* Receive and print the data if your socket is readable.
* If instead sys.stdin is readable, send the line read from sys.stdin.readline() with
your name prepended to the server if the line does not begin with exit.
* Close the socket and exit using sys.exit() if the string line from sys.stdin begins
with exit.

