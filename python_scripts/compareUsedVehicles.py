import sys,os,re, getopt
from xml.dom.minidom import parse
import xml.dom.minidom
import time


# AUTHOR : Come CACHARD


def getVehidListFromVehRouteFile(filepath):
# return a list of vehicles ids from an xml file that contains "vehicle" elements

    # Open XML document using minidom parser
    DOMTree = xml.dom.minidom.parse(filepath)
    collection = DOMTree.documentElement

    vehiclesIds = []

    # Get all the vehicles in the collection
    vehicles = collection.getElementsByTagName("vehicle")

    # Print detail of each movie.
    for vehicle in vehicles:
       vehiclesIds.append(vehicle.getAttribute("id"))
    
    return vehiclesIds


def compareVehListFromFiles(files_path):
# compare if given xml files contains "vehicle" elements with the same id.
# return true if the files contain the same vehicles; false otherwise.

    begin = time.time()

    listOfIdLists = []

    for file in files_path:
        listOfIdLists.append(getVehidListFromVehRouteFile(file))

    length = len(listOfIdLists[0])

    #for idList1 in listOfIdLists:
    idList1 = listOfIdLists[0]
    for idList2 in listOfIdLists:
        areListsEquals = set(idList1) == set(idList2)
        if areListsEquals == False:
            return False

    end = time.time()

    print("comparaison lasts : "+str(end-begin) + " seconds.")
    return True


def usage():
    print("The script will compare if given files contain the same vehicles ids.")
    print("usage : compareUsedVehicles.py --files filepath1[,filepath2[,filepath3[...]]]")
    print("\t--files : the filepaths of xml files that contains \"vehicle\" elements.")


def main(argv):

    filepath = ""
    files_path = ""

    try:
       opts, args = getopt.getopt(argv,"h", ["help","files="])
    except getopt.GetoptError as err:
       print str(err)
       print ""
       usage()
       sys.exit(-1)

    opt_list = [opt for opt, arg in opts]

    if len(opt_list) < 1 and  '-h' not in opt_list and '--help' not in opt_list:
       print "Error : please give the good amount of required arguments."
       print ""
       usage()
       sys.exit(-1)

    for opt, arg in opts :
       if opt in ("-h", "--help"):
          usage()
          sys.exit()
       elif opt in ("--files"):
          files_path = arg

    files = [f for f in files_path.split(",") if os.path.isfile(f)]
    fileuse = files[0]
    if len(files) ==0:
         print "No file found"
         exit(-1)

    isOk = compareVehListFromFiles(files)
    
    if isOk == False:
       print("uh oh : Lists do not contain the same vehicles.")
    else :
       print("yes they contain the same vehicles.")



if __name__=='__main__':
    main(sys.argv[1:])