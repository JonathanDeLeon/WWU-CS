# some imports
import socket, sys, binascii
from struct import *

#create a raw socket
try:
    # Open socket
    s= socket.socket(socket.AF_PACKET, socket.SOCK_RAW)
    s.bind(("eth0", 0))
except socket.error , msg:
    print 'Socket could not be created. Error Code : ' + str(msg[0]) + ' Message ' + msg[1]
    sys.exit()


# now start constructing the packet
packet = '';
mac_src = binascii.unhexlify('ffffffffffff')   #broadcast
mac_dest = binascii.unhexlify('e4ce8f3cf7da')    #macaddress for macbook
def ethernet():
    ethernet_type = socket.htons( 0x0800) 
    ethernet_type = 0x7a05 
    # 6s = 6 bytes of char[] 48 bits
    window = pack('!6s6sH', mac_dest, mac_src, ethernet_type)
    return window

window = ethernet()
# final full packet - syn packets dont have any data
packet = window + "HELLO EVERYONE"
# Send the packet finally - the port specified has no effect
s.send(packet)
s.close()
