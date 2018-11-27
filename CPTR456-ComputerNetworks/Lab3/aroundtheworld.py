import time, socket, json, random, string
import scipy.stats as stats
import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt
import numpy as np


## Create a random string of length l
def RandomString(l):
  return ''.join(random.choice(string.ascii_uppercase + string.digits) for x in range(l))


## Make a CDF given a list of numbers
def MakeCDF(x,fn="foobar",range=None):
    a = np.array(x)
    print "Min:",np.min(a),"Max:",np.max(a),"Range:",np.max(a)-np.min(a),"Mean:",np.mean(a),"Std:",np.std(a)
    plt.hist(x,bins=100,cumulative=True,normed=True,range=range)
    plt.xlabel('Time (s)')
    plt.ylabel('Probability')
    plt.title(fn)
    x1,x2,y1,y2 = plt.axis()
    plt.axis((x1,x2,0.0,1.0))
    plt.savefig(fn+".pdf")    


Tokyo = '52.197.182.6'        ## BlakeS
Mumbai = '35.154.0.238'       ## CodyW
Frankfurt = '35.156.246.213'  ## DanielS
Oregon = '35.167.81.131'      ## DennisH
Ireland = '52.213.251.47'     ## EthanB
London = '52.56.97.0'         ## JonathanD
Sydney = '13.55.48.87'        ## KyleE
Ohio = '52.14.12.193'         ## MattF
California = '52.8.6.3'       ## RobertB
Virginia = '34.196.89.81'      ## RyanR


## Assumes starting point is NVirginia and goes west
aroundtheworld =[California,Oregon,Tokyo,Sydney,Mumbai,Frankfurt,Ireland,Virginia,Ohio,London]

  
## Add your function here
def MeasureRoute(listOfIPs, numberOfIterations, extrasize=0, extradelay=0):
    ## Opens socket and listens on port 27809
    clientSocket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM) 
    clientSocket.bind( ("",27809) )
    
    ## Create disctionary to send
    d = {}
    firstIP = listOfIPs.pop(0)  # Grabs firstIP to send to
    d['ips'] = listOfIPs
    d['packet'] = 0
    d['author'] = "JonathanD"
    if extrasize != 0:
        d['randomString'] = RandomString(extrasize) # Creates a random string of length x    

    delayList = []  
    ## Catch error if message is too large
    try:
        ## Sends a message 'x' times where x = numberOfIterations
        while numberOfIterations > 0:
            if extradelay != 0:
                time.sleep(extradelay)      # Causes program to sleep for x seconds before sending message
            d['t1'] = time.time()       # Returns time in seconds as a floating point number
            ## Convert this dictionary to a JSON object
            jo = json.dumps(d)
            # Send UDP message with JSON object as the message to the firstIP on port 27808
            clientSocket.sendto(jo, (firstIP, 27808))
            clientSocket.settimeout(20)         # Sets receive timeout to be 20 seconds
            ## Check for timeout exceptions
            try:
                msg, addr = clientSocket.recvfrom(129000)
                clientSocket.settimeout(None)   # Removes timeout
                nd = json.loads(msg)            # Reads the JSON object
                delay = time.time() - nd['t1']  # Computes the delay and adds it to the list
                delayList.append(delay)
                print delay,"second(s)"
            except Exception as e:          
                print "Unexpected error:", e
            numberOfIterations-=1           # Decrements iteration
    except Exception as e:
        print "Message is too large..."
        clientSocket.close()
        return

    # Print the delays
    print "List of delays:",delayList
         
    clientSocket.close()
    return delayList

