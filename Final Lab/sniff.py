#Packet sniffer in python
#Packet sniffer in python
#For Linux

import socket

HOST = socket.gethostbyname(socket.gethostname())
print HOST

#create an INET, raw socket
s = socket.socket(socket.AF_INET, socket.SOCK_RAW, socket.IPPROTO_TCP)
s.bind((HOST,0))

s.setsockopt(socket.IPPROTO_IP, socket.IP_HDRINCL,1)

while True:
    print s.recvfrom(65565)
