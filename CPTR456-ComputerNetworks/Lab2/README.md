# Egg Hunt Program
In lab, use thesimpleeggclient.py example as a starting point to write a Python script to find your hidden Computer Science quote.  
Here are some hints.
1.  It  is  at  one  of  the  following  IP  addresses:  35.154.50.227,  35.154.100.255,  52.76.6.50, 52.221.30.210, 52.221.34.84
2.  It is reachable via one of these ports:  27800, 27801, 27802, 27803, 27804
3.  You must send a UDP message that begins with an unsigned integer packed as 8-bit,16-bit or 32-bit.  After the packed unsigned integer, you must append a length 2 string with your initials in capital letters. 
Notice that there are 75 permutations.  Write sensible code that tries all these automatically for you.  Record the IP, port and response that you received for the written report.

> Note: The above IP addresses are no longer available

## Output Quote
> "Success is the greatest revenge"

## Background
1.  Python's struct for packing and unpacking binary data. See docs.python.org/library/struct.html for more details.
2.  Python's select for adding a timeout when waiting for data. See docs.python.org/library/select.html for more details.
