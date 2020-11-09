import sys,os,re, getopt
from xml.dom.minidom import parse
import xml.dom.minidom
import time

# AUTHOR : Come CACHARD

def getVLoadsByEdge(filepath):
# return a dict of loads of vehicles per edge from an xml file that contains "edge" elements

    # Open XML document using minidom parser
    DOMTree = xml.dom.minidom.parse(filepath)
    collection = DOMTree.documentElement

    loads = {}

    # Get all the vehicles in the collection
    edges = collection.getElementsByTagName("edge")

    # Print detail of each movie.
    for edge in edges:
       loads[edge.getAttribute("id")] = edge.getAttribute("load")
    
    return loads


def compareLoadsFromFiles(files_path):
# compare if given xml files contains "vehicle" elements with the same id.
# return true if the files contain the same vehicles; false otherwise.

    loadsList = []

    for file in files_path:
        loadsList.append(getVLoadsByEdge(file))

    length = len(loadsList[0])

    #for idList1 in loadsList:
    load1 = loadsList[0]
    for load2 in loadsList:
        areLoadsEqual = compareDict(load1,load2)
        if areLoadsEqual == False:
            return False

    return True

def compareDict(dico1,dico2):
#compare two dictionaries

    if len(dico1) != len(dico2):
        print ("dicos do not have the same length.")
        return False

    dico1_keys = dico1.keys()
    dico2_keys = dico2.keys()

    for key in dico1_keys:
        if key not in dico2_keys:
            print("key "+str(key)+" not in dico2")
            return False
        if dico1[key] != dico2[key]:
            print ("key "+str(key)+" have diffent values.")
            return False
    return True

def usage():
    print("The script will compare if given loads matrix folders contain the same loads per edge.")
    print("usage : compareLoadsMatrix.py --folders loadsMatrixFolderPath1[,loadsMatrixFolderPath2[,loadsMatrixFolderPath3[...]]]")
    print("\t--folders : the folder pahts to loads matrixes to compare. It can be light loads matrixes.")


def main(argv):

    filepath = ""
    files_path = ""

    try:
       opts, args = getopt.getopt(argv,"h", ["help","folders="])
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
       elif opt in ("--folders"):
          folders_path = arg

    nbStep = len(os.listdir(folders_path.split(",")[0]))

    folder_path_0 = folders_path.split(",")[0].split(os.sep)
    nameMatrix = folder_path_0[len(folder_path_0)-1] if folder_path_0[len(folder_path_0)-1] != "" else folder_path_0[len(folder_path_0)-2]
    extension = ".llm.xml" if nameMatrix.startswith("light") else ".lm.xml"

    for i in range(nbStep):
        files_to_compare = []
        for folder in folders_path.split(","):
            name = folder
            if folder.endswith(os.sep) == False:
                name += os.sep
            name += "step_"+str(i)+extension
            files_to_compare.append(name)
        isOk = compareLoadsFromFiles(files_to_compare)
        if isOk == False:
            print("uh oh : files do not contain the same loads per Edge.")
            return
        print "%d/%d steps compared.\r" % (i,nbStep),
        sys.stdout.flush()

    print("yes they contain the same loads per edge.")



if __name__=='__main__':
    main(sys.argv[1:])