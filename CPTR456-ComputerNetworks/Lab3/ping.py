## Date from ping reports in class
Tokyo = {'Tokyo': -1, 'Mumbai': 136, 'Frankfurt': 227, 'Oregon': 101, 'Ireland': 212, 'London': 226, 'Sydney': 103, 'Ohio': 143, 'NCal': 113, 'Virginia': 141}
Mumbai = {'Tokyo': 136, 'Mumbai': -1, 'Frankfurt': 112, 'Oregon': 280, 'Ireland': 121, 'London': 113, 'Sydney': 238, 'Ohio': 193, 'NCal': 241, 'Virginia': 183}
Frankfurt = {'Tokyo': 227, 'Mumbai': 112, 'Frankfurt': -1, 'Oregon': 166, 'Ireland': 22, 'London': 17.3, 'Sydney': 332, 'Ohio': 98.4, 'NCal': 147, 'Virginia': 90}
Oregon = {'Tokyo': 101.0, 'Mumbai': 265.4, 'Frankfurt': 161.329, 'Oregon': -1, 'Ireland': 139.4, 'London': 156.8, 'Sydney': 162.0, 'Ohio': 72.0, 'NCal': 21.4, 'Virginia': 90.7}
Ireland = {'Tokyo': 212, 'Mumbai': 122, 'Frankfurt': 22.0, 'Oregon': 138, 'Ireland': -1, 'London': 11.4, 'Sydney': 301, 'Ohio': 91.5, 'NCal': 141, 'Virginia': 84.2}
London = {'Tokyo': 226.6, 'Mumbai': 113.49, 'Frankfurt': 17.4, 'Oregon': 157.4, 'Ireland': 11.5, 'London': -1, 'Sydney': 295.5, 'Ohio': 86.7, 'NCal': 139.8, 'Virginia': 76.7}
Sydney = {'Tokyo': 103, 'Mumbai': 237, 'Frankfurt': 323, 'Oregon': 162, 'Ireland': 301, 'London': 294, 'Sydney': -1, 'Ohio': 209, 'NCal': 159, 'Virginia': 219}
Ohio = {'Tokyo': 143, 'Mumbai': 149, 'Frankfurt': 98.4, 'Oregon': 72.0, 'Ireland': 91.5, 'London': 86.7, 'Sydney': 209, 'Ohio': -1, 'NCal': 52.6, 'Virginia': 12}
NCal = {'Tokyo': 113, 'Mumbai': 241, 'Frankfurt': 147, 'Oregon': 21.5, 'Ireland': 141, 'London': 139, 'Sydney': 159, 'Ohio': 52.6, 'NCal': -1, 'Virginia': 70.7}
Virginia = {'Tokyo': 141, 'Mumbai': 183, 'Frankfurt': 90, 'Oregon': 90, 'Ireland': 84, 'London': 76.6, 'Sydney': 219, 'Ohio': 12, 'NCal': 20.9, 'Virginia': -1}

aroundtheworld ={'NCal': NCal,'Oregon':Oregon,'Tokyo':Tokyo,'Sydney':Sydney,'Mumbai':Mumbai,'Frankfurt':Frankfurt,'Ireland':Ireland,'Virginia':Virginia,'Ohio':Ohio,'London':London}

listOfFastTimes = []
listOfFastPaths = []

listOfSlowTimes = []
listOfSlowPaths = []

def beginCircuit():
    print "\nBEGIN Fastest Route\n"
    for key, value in aroundtheworld.iteritems():
        time=0
        path=""
        getFastestRoute(key, aroundtheworld.copy(), key, [], time, path)

    print "\n\nBEGIN Slowest Route\n"
    for key, value in aroundtheworld.iteritems():
        time=0
        path=""
        getSlowestRoute(key, aroundtheworld.copy(), key, [], time, path)

    print "\nFinal Statistics\n"
    
    listOfFastTimes.sort()
    print "Fastest times:"
    for x in range(0,3):
        print str(listOfFastTimes[x])+" ms"

    listOfSlowTimes.sort()
    list_len = len(listOfSlowTimes)
    print "Slowest times:"
    for x in range(list_len-3,list_len):
        print str(listOfSlowTimes[x])+" ms"

    #print listOfFastTimes
    #print listOfFastPaths
    #print listOfSlowTimes
    #print listOfSlowPaths

def getFastestRoute(place, listOfPlacesToVisit, startingPoint, excludePlaces, time, path):
    route = listOfPlacesToVisit[place]
    listOfPlacesToVisit.pop(place)
    excludePlaces.append(place)
    location = ""
    path += place+" -> "
    for key, value in sorted(route.iteritems(), key=lambda (k,v): (v,k)):
        if key not in excludePlaces:
            if key != place:
                time += value
                location = key
                #print "%s: %s" % (key, value)
                break
    if len(listOfPlacesToVisit) == 0:
        time += route[startingPoint]
        path += startingPoint
        print "Path to get shortest time:",path
        print "Time: "+str(time)+" ms"
        listOfFastTimes.append(time)
        listOfFastPaths.append(path)
        return
    getFastestRoute(location, listOfPlacesToVisit, startingPoint, excludePlaces, time, path)

def getSlowestRoute(place, listOfPlacesToVisit, startingPoint, excludePlaces, time, path):
    route = listOfPlacesToVisit[place]
    listOfPlacesToVisit.pop(place)
    excludePlaces.append(place)
    location = ""
    path += place+" -> "
    for key, value in sorted(route.iteritems(), key=lambda (k,v): (v,k), reverse=True):
        if key not in excludePlaces:
            if key != place:
                time += value
                location = key
                #print "%s: %s" % (key, value)
                break
    if len(listOfPlacesToVisit) == 0:
        time += route[startingPoint]
        path += startingPoint
        print "Path to get longest time:",path
        print "Time: "+str(time)+" ms"
        listOfSlowTimes.append(time)
        listOfSlowPaths.append(path)
        return
    getSlowestRoute(location, listOfPlacesToVisit, startingPoint, excludePlaces, time, path)

beginCircuit()
