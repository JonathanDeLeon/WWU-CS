import socket, struct, select

serverName = '52.76.6.50'
serverNames = ['35.154.50.227','35.154.100.255','52.76.6.50','52.221.30.210','52.221.34.84']
serverPort = 27801
serverPorts = [27800,27801,27802,27803,27804]
clientSocket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM) 

fmts = ['!B','!H','!I']
## Send message
for server in serverNames:
    for port in serverPorts:
        for f in fmts:
            number = struct.pack(f, 23)
            message = number + 'JD'
            clientSocket.sendto(message,(server, port))

            print "UDP Connection on server "+str(server)+":"+str(port)+" format: '"+str(f)+"'"

            ## Wait for reply with timeout
            inputready, outputready, exceptready =select.select([clientSocket],[],[],0.5)

            #print inputready, outputready, exceptready

            for s in inputready:
                print "Starting to unpack message from server "+str(server)+" ..."
                responseMessage, serverAddress = s.recvfrom(1024)

                ## Extract binary data portion
                rnumber, = struct.unpack_from(f,responseMessage)

                ## Extract string data portion
                response = responseMessage[struct.calcsize(f):]

                print rnumber,response

clientSocket.close()
