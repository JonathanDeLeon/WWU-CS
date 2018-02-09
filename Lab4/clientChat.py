import socket
import select
import sys

chatServer = '52.221.30.210'
chatPort = 27800
name = 'JonathanD'

clientSocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM ) 
clientSocket.connect( (chatServer,chatPort) )

while 1:
    rlist, wlist, elist = select.select([clientSocket,sys.stdin],[],[])
    for sock in rlist:
        if sock == sys.stdin:
            msg = sys.stdin.readline().replace('\n','')     # Remove new lines
            if msg == "exit":
                print "Exiting server.."
                clientSocket.close()
                sys.exit()
                break
            else:
                clientSocket.send(msg)
        else:
            print sock.recv(1024)

clientSocket.close()
