import sys,os,re, getopt
from xml.dom.minidom import parse
import xml.dom.minidom
import xml.etree.ElementTree as ET
import time


# AUTHOR : Come CACHARD


def getVehidListFromVehRouteFile(filepath):
# get the list of vehicles ids contained in an xml file

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



def generate_sumorou_file(vehroute_file,sumorou_file,sumorou_output_name):
# Generate a sumorou file from an existing one by removing all the vehicles that do not appear in given vehroute file
    print("begin")
    begin = time.time()
    # Open XML document using minidom parser
    DOMTree_sumorou = ET.parse(sumorou_file)
    doc_sumorou = DOMTree_sumorou.getroot()

    list_idsVehicles_considered = getVehidListFromVehRouteFile(vehroute_file)
    print("vehroute loaded.")
    print("")

    # Get all the vehicles in the collection
    collection = doc_sumorou.findall("vehicle")
    nb_vehicles = len(collection)
    i = 0
    for vehicle in collection:
        if vehicle.get("id") not in list_idsVehicles_considered :
            doc_sumorou.remove(vehicle)
        print "%d/%d vehicles done.\r" % (i,nb_vehicles),
        sys.stdout.flush()
        i+=1

    DOMTree_sumorou.write(sumorou_output_name);
    end = time.time()

    print("sumorou file generated in : "+str(end-begin)+" seconds.")


def usage():
    print("The script will produce a sumo rou file which will contain only the vehicles that are used in a vehroute sumo output file.")
    print("This script is mainly used to produce sumorou file for different scale in order to make comparison with SUMO.")
    print("usage : createSumoRouFromVehroute.py --sumorou sumorouPath --vehroute vehroutePath [--output outputName]")
    print("\t--sumorou : the path to the sumorou path we want to create a new one from.")
    print("\t--vehroute : the path to the sumo vehroute output file that contains the vehicles that we want to keep in our new sumorou file.")


def main(argv):

    sumorou_src = ""
    vehroute_src = ""

    sumorou_output_name = ""

    try:
        opts, args = getopt.getopt(argv, "h", ["help","sumorou=","vehroute=","output="])
    except getopt.GetoptError as err:
        print str(err)
        print ""
        usage()
        sys.exit(-1)
 
    opt_list = [opt for opt, arg in opts]

    if len(opt_list) < 2 and  '-h' not in opt_list and '--help' not in opt_list:
        print "Error : please give the good amount of required arguments."
        print ""
        usage()
        sys.exit(-1)

    for opt, arg in opts :
        if opt in ("-h", "--help"):
            usage()
            sys.exit()
        elif opt in ("--sumorou"):
            if os.path.isfile(arg)==False:
                print("file "+arg+" not found")
                sys.exit()
            sumorou_src = arg
        elif opt in ("--vehroute"):
            if os.path.isfile(arg)==False:
                print("file "+arg+" not found")
                sys.exit()
            vehroute_src = arg
        elif opt in ("--output"):
            sumorou_output_name = arg
        else:
            print("option not recognized : "+str(opt))
            usage()
            sys.exit()

    if sumorou_output_name == "":
        sumorou_output_name = "sumorou_created.rou.xml"
        i = 0
        while os.path.isfile(sumorou_output_name)==True:
            sumorou_output_name = "sumorou_created_"+str(i)+".rou.xml"
            i+= 1
    if sumorou_output_name.endswith(".rou.xml")==False:
        sumorou_output_name += ".rou.xml"

    generate_sumorou_file(vehroute_src,sumorou_src,sumorou_output_name)



if __name__=='__main__':
    main(sys.argv[1:])