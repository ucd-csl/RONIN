# -*- coding: utf-8 -*-
import sys,os,re, getopt, glob
from xml.dom.minidom import parse
import xml.dom.minidom
import csv
import xlsxwriter
from collections import defaultdict
import xml.etree.ElementTree as etree
import xml.dom.pulldom as pulldom
import json
import time
import numpy as np
from scipy.integrate import simps
from numpy import trapz


# AUTHOR : Come CACHARD


INPUTS_PATH = "."+os.sep+"inputs"
OUTPUT_PATH = "."+os.sep+"output"

RONIN_PATH = INPUTS_PATH+os.sep+"ronin"
SUMO_PATH = INPUTS_PATH+os.sep+"sumo"

CREATED_INPUTS_PATH = INPUTS_PATH+os.sep+"created_inputs"
RONIN_CREATED_INPUTS_PATH = CREATED_INPUTS_PATH+os.sep+"ronin"
SUMO_CREATED_INPUTS_PATH = CREATED_INPUTS_PATH+os.sep+"sumo"

###################################################################################################################################################
##############################                        Duration trips                    ###########################################################
###################################################################################################################################################

def read_duration_trip_ronin(ronin_tripinfos_filepath):
# Reads the tripinfos output file generated by ronin and returns a dict with in key the id of vehicle and in value its trip duration in seconds.

    json_dico_path = RONIN_CREATED_INPUTS_PATH + os.sep + get_file_basename(ronin_tripinfos_filepath) +"_tripinfos_ronin_dico.json"

    json_dico = load_dict_from_jsonfile(json_dico_path)
    if len(json_dico) != 0 :
        print "load Ronin tripinfos from json."
        return json_dico

    trip_duration_by_veh = {}

    # Open XML document using minidom parser
    DOMTree = xml.dom.minidom.parse(ronin_tripinfos_filepath)
    collection = DOMTree.documentElement

    # Get all the tripinfos in the collection
    tripinfos = collection.getElementsByTagName("tripinfo")

    i = 0
    nb_tripinfos = len(tripinfos)
    for tripinfo in tripinfos:
        trip_duration_by_veh[tripinfo.getAttribute("id")] = tripinfo.getAttribute("duration")
        i+=1
        print "%d/%d ronin tripinfos read.\r" % (i,nb_tripinfos),
        sys.stdout.flush()


    save_dict_to_jsonfile(json_dico_path,trip_duration_by_veh)

    return trip_duration_by_veh



def read_duration_trip_sumo(sumo_tripinfos_filepath):
# Reads the tripinfos output file generated by sumo and returns a dict with in key the id of vehicle and in value its trip duration in seconds.

    json_dico_path = SUMO_CREATED_INPUTS_PATH + os.sep +get_file_basename(sumo_tripinfos_filepath)+ "_tripinfos_sumo_dico.json"

    json_dico = load_dict_from_jsonfile(json_dico_path)
    if len(json_dico) != 0 :
        print "load SUMO tripinfos from json."
        return json_dico

    trip_duration_by_veh = {}

    # Open XML document using minidom parser
    DOMTree = xml.dom.minidom.parse(sumo_tripinfos_filepath)
    collection = DOMTree.documentElement

    # Get all the tripinfos in the collection
    tripinfos = collection.getElementsByTagName("tripinfo")

    i=0
    nb_tripinfos = len(tripinfos)
    for tripinfo in tripinfos:
        trip_duration_by_veh[tripinfo.getAttribute("id")] = tripinfo.getAttribute("duration")
        i+=1
        print "%d/%d sumo tripinfos read.\r" % (i,nb_tripinfos),
        sys.stdout.flush()

    save_dict_to_jsonfile(json_dico_path,trip_duration_by_veh)

    return trip_duration_by_veh



def compare_duration_trip(sumo_tripinfos_filepath,ronin_tripinfos_filepath):
# Compare the trip duration of vehicles got from ronin and sumo. 
# Writes an excel file a dict containing for each vehicle its normalized difference of duration trip from ronin by the duration trip from sumo.
    tripinfos_sumo = read_duration_trip_sumo(sumo_tripinfos_filepath)
    tripinfos_ronin = read_duration_trip_ronin(ronin_tripinfos_filepath)

    compare_dic(tripinfos_sumo,tripinfos_ronin,"duration_trip_comparison",["idVehicle","(duration trip Ronin - duration trip SUMO) / duration trip SUMO"],normalize=True)


###################################################################################################################################################
##############################          Edges' density and traffic volume               ###########################################################
###################################################################################################################################################


def read_edges_infos_sumo(sumo_edgedata_filepath):
# Reads the edgedata output file generated by sumo.
# Returns a tuple containing a dict with in key the id of edge and in value its average density + a a dict with in key the id of edge and in value its average traffic volume.

    density_json_dico_path = SUMO_CREATED_INPUTS_PATH + os.sep + get_file_basename(sumo_edgedata_filepath) +"_density_sumo_dico.json"
    traffic_volume_json_dico_path = SUMO_CREATED_INPUTS_PATH + os.sep + get_file_basename(sumo_edgedata_filepath)+"_trafficVolume_sumo_dico.json"

    density_json_dico = load_dict_from_jsonfile(density_json_dico_path)
    traffic_volume_json_dico = load_dict_from_jsonfile(traffic_volume_json_dico_path)
    if len(traffic_volume_json_dico) != 0 and len(density_json_dico) != 0:
        print "load SUMO density and traffic volume from json."
        return [density_json_dico,traffic_volume_json_dico]


    average_density = {}
    average_traffic_volume = {}

    # Open XML document using minidom parser
    DOMTree = xml.dom.minidom.parse(sumo_edgedata_filepath)
    collection = DOMTree.documentElement

    edges = collection.getElementsByTagName("edge")

    i=0
    nb_edges = len(edges)
    for edge in edges:
        speed_attr = edge.getAttribute("speed")
        speed = float(speed_attr) if speed_attr!="" else 0.0

        density_attr = edge.getAttribute("density")
        density = float(density_attr) if density_attr!="" else 0.0

        idEdge = edge.getAttribute("id")

        traffic_volume = speed*density*3.6

        average_density[idEdge]=density
        average_traffic_volume[idEdge]=traffic_volume

        i+=1
        print "%d/%d sumo edges read.\r" % (i,nb_edges),
        sys.stdout.flush()

    save_dict_to_jsonfile(density_json_dico_path,average_density)
    save_dict_to_jsonfile(traffic_volume_json_dico_path,average_traffic_volume)


    return [average_density,average_traffic_volume]


def read_edges_infos_ronin(ronin_edgedata_filepath):
# Reads the edgedata output file generated by ronin.
# Returns a tuple containing a dict with in key the id of edge and in value its average density + a a dict with in key the id of edge and in value its average traffic volume.

    density_json_dico_path = RONIN_CREATED_INPUTS_PATH + os.sep+get_file_basename(ronin_edgedata_filepath) + "_density_ronin_dico.json"
    traffic_volume_json_dico_path = RONIN_CREATED_INPUTS_PATH + os.sep + get_file_basename(ronin_edgedata_filepath) +"_trafficVolume_ronin_dico.json"

    density_json_dico = load_dict_from_jsonfile(density_json_dico_path)
    traffic_volume_json_dico = load_dict_from_jsonfile(traffic_volume_json_dico_path)
    if len(traffic_volume_json_dico) != 0 and len(density_json_dico) != 0:
        print "load Ronin density and traffic volume from json."
        return [density_json_dico,traffic_volume_json_dico]

    average_density = {}
    average_traffic_volume = {}

    # Open XML document using minidom parser
    DOMTree = xml.dom.minidom.parse(ronin_edgedata_filepath)
    collection = DOMTree.documentElement

    edges = collection.getElementsByTagName("edge")

    i=0
    nb_edges = len(edges)
    for edge in edges:
        traffic_volume_attr = edge.getAttribute("trafficVolume")
        traffic_volume = float(traffic_volume_attr) if traffic_volume_attr != "" else 0.0

        density_attr = edge.getAttribute("density")
        density = float(density_attr) if density_attr!="" else 0.0

        idEdge = edge.getAttribute("id")

        average_density[idEdge]=density
        average_traffic_volume[idEdge]=traffic_volume

        i+=1
        print "%d/%d ronin edges read.\r" % (i,nb_edges),
        sys.stdout.flush()

    save_dict_to_jsonfile(density_json_dico_path,average_density)
    save_dict_to_jsonfile(traffic_volume_json_dico_path,average_traffic_volume)

    return [average_density,average_traffic_volume]


def compare_edges_infos(sumo_edgedata_filepath,ronin_edgedata_filepath,take_0_in_account=True):
# Compare the average density and average traffic volume of edges got from ronin and sumo. 
# Writes an excel file containing for each edge its difference of density from ronin by the density from sumo.
# + Writes an excel file containing for each edge its difference of traffic volume from ronin by the traffic volume from sumo.

    edges_infos_sumo = read_edges_infos_sumo(sumo_edgedata_filepath)
    edges_infos_ronin = read_edges_infos_ronin(ronin_edgedata_filepath)
    
    if take_0_in_account == False:
        compare_dic(edges_infos_sumo[0],edges_infos_ronin[0],"edge_density_comparison",["idEdge","(average density Ronin - average density SUMO) / average density SUMO"],normalize=True,minimal_value=5)
        compare_dic(edges_infos_sumo[1],edges_infos_ronin[1],"edge_traffic_volume_comparison",["idEdge","(average traffic volume Ronin - average traffic volume SUMO) / traffic volume SUMO"],normalize=True,minimal_value=100)
    else:
        compare_dic(edges_infos_sumo[0],edges_infos_ronin[0],"edge_density_comparison",["idEdge","(average density Ronin - average density SUMO) / average density SUMO"],normalize=True)
        compare_dic(edges_infos_sumo[1],edges_infos_ronin[1],"edge_traffic_volume_comparison",["idEdge","(average traffic volume Ronin - average traffic volume SUMO) / traffic volume SUMO"],normalize=True)


###################################################################################################################################################
##############################                   Vehicles' positions                    ###########################################################
###################################################################################################################################################


def read_vehicles_trip_edges(sumo_vehroute_filepath):
# Reads the vehroute output file generated by sumo and returns a dict with in key the id of vehicle and in value a dict 
# with in key the id of an edge of its trip and in value the position of this edge on the trip of the vehicle.

    json_dico_path = SUMO_CREATED_INPUTS_PATH + os.sep + get_file_basename(sumo_vehroute_filepath)+"_tripedges_sumo_dico.json"

    json_dico = load_dict_from_jsonfile(json_dico_path)
    if len(json_dico) != 0 :
        print "load trip edges from json."
        return json_dico

    trips_by_vehicle = {}

    # Open XML document using minidom parser
    DOMTree = xml.dom.minidom.parse(sumo_vehroute_filepath)
    collection = DOMTree.documentElement

    # Get all the tripinfos in the collection
    vehicles = collection.getElementsByTagName("vehicle")

    i=0
    nb_vehicles = len(vehicles)
    for vehicle in vehicles:
        id_vehicle = vehicle.getAttribute("id")
        route = vehicle.getElementsByTagName("route")[0]
        edges = route.getAttribute("edges").split()
        dic_edges = {k: v for v, k in enumerate(edges)}

        trips_by_vehicle[id_vehicle] = dic_edges

        i+=1
        print "%d/%d vehicle trips read.\r" % (i,nb_vehicles),
        sys.stdout.flush()

    save_dict_to_jsonfile(json_dico_path,trips_by_vehicle)

    return trips_by_vehicle



def read_position_vehicles_sumo(sumo_netstate_filepath,trips_by_vehicle):
# Reads the netstate output file generated by sumo and a dict containing the description of trips by vehicle
# and returns a dict with in key the id of vehicle and in value a dict with in key the timestep and in value the position of the vehicle on its trip

    json_dico_path = SUMO_CREATED_INPUTS_PATH + os.sep + get_file_basename(sumo_netstate_filepath) +"_positions_sumo_dico.json"

    json_dico = load_dict_from_jsonfile(json_dico_path)
    if len(json_dico) != 0 :
        print "load sumo vehicles positions from json."
        return json_dico

    vehicles_positions = defaultdict(lambda:{})

    # Open XML document using minidom parser
    DOMTree = xml.dom.minidom.parse(sumo_netstate_filepath)
    netstate = DOMTree.documentElement

    timesteps = netstate.getElementsByTagName("timestep")

    i = 0
    nb_steps = len(timesteps)
    for step in timesteps:

        time = step.getAttribute("time")
        edges = step.getElementsByTagName("edge")

        for edge in edges:
            id_edge = edge.getAttribute("id")
            vehicles = edge.getElementsByTagName("vehicle")

            for vehicle in vehicles:
                id_vehicle = vehicle.getAttribute("id")
                dico_positions[id_vehicle][time] = trips_by_vehicle[id_vehicle][id_edge]

        i+=1
        print "%d/%d sumo steps read.\r" % (i,nb_steps),
        sys.stdout.flush()

    save_dict_to_jsonfile(json_dico_path,vehicles_positions)

    return vehicles_positions



def read_position_vehicles_sumo_big_file(sumo_netstate_filepath,trips_by_vehicle):
# Reads the netstate output file generated by sumo and a dict containing the description of trips by vehicle
# and returns a dict with in key the id of vehicle and in value a dict with in key the timestep and in value the position of the vehicle on its trip
# This method use SAX and is slow, but can parse huge files.

    json_dico_path = SUMO_CREATED_INPUTS_PATH + os.sep + get_file_basename(sumo_netstate_filepath) +"_positions_sumo_dico.json"

    json_dico = load_dict_from_jsonfile(json_dico_path)
    if len(json_dico) != 0 :
        print "load sumo vehicles positions from json."
        return json_dico

    vehicles_positions = defaultdict(lambda:{})

    doc = pulldom.parse(sumo_netstate_filepath)
    i = 0
    for event, node in doc:
        if event == 'START_ELEMENT' and node.tagName=='timestep':
            doc.expandNode(node) # node now contains a dom fragment
            time = node.getAttribute("time")
            edges = node.getElementsByTagName("edge")

            for edge in edges:
                id_edge = edge.getAttribute("id")
                vehicles = edge.getElementsByTagName("vehicle")

                for vehicle in vehicles:
                    id_vehicle = vehicle.getAttribute("id")
                    vehicles_positions[id_vehicle][time] = trips_by_vehicle[id_vehicle][id_edge]

            i+=1
            print "step %d done.\r" % i,
            sys.stdout.flush()

    save_dict_to_jsonfile(json_dico_path,vehicles_positions)

    return vehicles_positions



def read_position_vehicles_for_step_file_ronin(step_file,dico_positions,trips_by_vehicle):
# Reads a stepfile of the loads matrix output generated by ronin and a dict containing the description of trips by vehicle
# and returns a dict with in key the id of vehicle and in value a dict with in key the timestep and in value the position of the vehicle on its trip
    
    # Open XML document using minidom parser
    DOMTree = xml.dom.minidom.parse(step_file)
    step = DOMTree.documentElement

    #step = collection.getElementsByTagName("step")[0]
    time = step.getAttribute("timeSlot")

    edges = step.getElementsByTagName("edge")

    for edge in edges:
        id_edge = edge.getAttribute("id")
        vehicles = edge.getElementsByTagName("vehicle")

        for vehicle in vehicles:
            id_vehicle = vehicle.getAttribute("id")
            dico_positions[id_vehicle][time] = trips_by_vehicle[id_vehicle][id_edge]



def read_position_vehicles_ronin(ronin_loadsmatrix_folderpath,trips_by_vehicle):
# Reads the loads matrix output generated by ronin and a dict containing the description of trips by vehicle
# and returns a dict with in key the id of vehicle and in value a dict with in key the timestep and in value the position of the vehicle on its trip

    json_dico_path = RONIN_CREATED_INPUTS_PATH + os.sep + get_file_basename(ronin_loadsmatrix_folderpath) +"_positions_ronin_dico.json"

    json_dico = load_dict_from_jsonfile(json_dico_path)
    if len(json_dico) != 0 :
        print "load Ronin vehicles positions from json."
        return json_dico

    vehicles_positions = defaultdict(lambda:{})

    nb_files = len(os.listdir(ronin_loadsmatrix_folderpath))
    i=1
    for file in os.listdir(ronin_loadsmatrix_folderpath):
        read_position_vehicles_for_step_file_ronin(ronin_loadsmatrix_folderpath+os.sep+file,vehicles_positions,trips_by_vehicle)
        i+=1
        print "%d/%d ronin positions files read.\r" % (i,nb_files),
        sys.stdout.flush()

    save_dict_to_jsonfile(json_dico_path,vehicles_positions)

    return vehicles_positions



def compare_vehicles_positions(sumo_vehroute_filepath,sumo_netstate_filepath,ronin_loadsmatrix_folderpath):
# Compare the positions of vehicles got from ronin and sumo. 
# Writes for each vehicle an excel file containing for each timestep its difference of positions from ronin by the positions from sumo.
# + Writes an excel file containing for each vehicle its normalized difference of spacetime area from ronin by the spacetime from sumo.

    output_postions_folder = OUTPUT_PATH+os.sep+"positions"
    output_areas_positions_file = OUTPUT_PATH+os.sep+"areas_positions"+".xlsx"

    check_and_create_if_not_exist_path(output_postions_folder)

    trips_by_vehicle = read_vehicles_trip_edges(sumo_vehroute_filepath)
    print("trips edges loaded.")
    vehicles_positions_ronin = read_position_vehicles_ronin(ronin_loadsmatrix_folderpath,trips_by_vehicle)
    print("positions from Ronin loaded.")
    vehicles_positions_sumo = read_position_vehicles_sumo_big_file(sumo_netstate_filepath,trips_by_vehicle)
    print("positions from sumo loaded.")

    dict_result_areas = {}

    nb_vehicles = len(vehicles_positions_ronin.keys())
    i=0
    for key in vehicles_positions_ronin.keys():
        if key not in vehicles_positions_sumo.keys():
            print "vehicle key of ronin not found in sumo : "+str(key)

        # for each vehicle, we create an excel file that will contains the comparison of their positions got from ronin to the positions got from SUMO timestep by timestep, 
        # + we compute in the same time a dict_result_areas that will contains in key the id of vehicle and in value the area difference of positions of Ronin by sumo.
        compare_positions_dic(dict_result_areas,key,vehicles_positions_sumo[key],vehicles_positions_ronin[key],"veh_"+key,["time","position Ronin - position SUMO"],normalize=True)
        i+=1
        print "%d/%d vehicles positions comparisons done.\r" % (i,nb_vehicles),
        sys.stdout.flush()

    # we make a file that contains the difference of areas of sumo and ronin for each vehicle
    write_dict_to_excel(output_areas_positions_file,["idVehicle","(area Ronin - area SUMO) / area SUMO"],dict_result_areas)



###################################################################################################################################################
##############################                 Utils  - Comparing                          ########################################################
###################################################################################################################################################

def compare_dic(dic_sumo,dic_ronin,output_file_name,columns_headers,normalize=False,excel_output=True,minimal_value=0):
# Writes a csv or excel file that contains the difference of value of the values from ronin by the values from sumo

    dic_result = {}

    if len(dic_sumo) != len(dic_ronin):
        print ("Error : the sumo and ronin outputs do not have the same length.")
        return

    for key in dic_ronin.keys():
        difference = float(dic_ronin[key]) - float(dic_sumo[key])
        if normalize == True:
            if difference != 0:
                if float(dic_sumo[key]) != 0:
                    difference = difference / float(dic_sumo[key])
                else:
                    difference = float("inf")
        if(abs(difference)>=minimal_value):
            dic_result[key] = difference


    output_filepath = OUTPUT_PATH + os.sep + output_file_name

    if excel_output == True : 
        output_filepath += ".xlsx"
        write_dict_to_excel(output_filepath,columns_headers,dic_result)
    else :
        output_filepath += ".csv"
        write_dict_to_csv(output_filepath,columns_headers,dic_result)



def compare_positions_dic(dict_result_areas,key_vehicle,dic_sumo,dic_ronin,output_file_name,columns_headers,normalize=False,excel_output=True,minimal_value=0):
# Writes a csv or excel file that contains the difference of value of the positions from ronin by the positions from sumo.
# + we add the difference area of each vehicle in dict_result_areas

    dic_result = {}

    all_keys = sorted(set().union(dic_ronin.keys(),dic_sumo.keys()))

    ronin_positions = []
    sumo_positions = []
    sumo_value = -1
    ronin_value = -1

    for key in all_keys:
        if key in dic_ronin.keys():
            ronin_value = dic_ronin[key]
        if key in dic_sumo.keys():
            sumo_value = dic_sumo[key]

        difference = float(ronin_value) - float(sumo_value)
        
        if(abs(difference)>=minimal_value):
            dic_result[key] = difference

        ronin_positions.append(float(ronin_value))
        sumo_positions.append(float(sumo_value))


    # put the statistics of area of this vehicle in the result dict
    area_ronin = get_area_from_ordinates(ronin_positions)
    area_sumo = get_area_from_ordinates(sumo_positions)
    difference_area = area_ronin - area_sumo
    if normalize == True:
        if difference_area != 0:
            if area_sumo != 0:
                difference_area = difference_area / area_sumo
            else :
                difference_area = float("inf")
    dict_result_areas[key_vehicle] = difference_area

    # write the comparison of positions for this vehicle in a file
    output_filepath = OUTPUT_PATH+os.sep+"positions" + os.sep + output_file_name

    if excel_output == True : 
        output_filepath += ".xlsx"
        write_dict_to_excel(output_filepath,columns_headers,dic_result,True)
    else :
        output_filepath += ".csv"
        write_dict_to_csv(output_filepath,columns_headers,dic_result)



###################################################################################################################################################
##############################                 Utils  - writing                  ##################################################################
###################################################################################################################################################


def write_dict_to_excel(excel_file,columns_headers,dict_data,is_key_number=False):
# Write a dict into an excel file

    workbook = xlsxwriter.Workbook(excel_file)
    worksheet = workbook.add_worksheet()

    # Start from the first cell. Rows and columns are zero indexed.
    row = 0
    col = 0

    #write header
    for i in range(len(columns_headers)):
        worksheet.write_string(row, i,columns_headers[i])
    
    row += 1

    # Iterate over the data and write it out row by row.
    for key, value in dict_data.items():
        if is_key_number == False:
            worksheet.write_string(row, col,key)
        else:
            worksheet.write_number(row, col,float(key))
        try :
            worksheet.write_number(row, col + 1, value)
        except :
            print "error printing dict to excel : \n\tkey :"+str(key)+"\n\tvalue : "+str(value)
        row += 1

    workbook.close()



def write_dict_to_csv(csv_filepath,columns_headers,dict_data):
# Write a dict into a csv file

    with open(csv_filepath, 'wb') as csv_file:
        writer = csv.writer(csv_file)
        
        #write header
        writer.writerow(columns_headers)

        for key, value in dict_data.items():
            writer.writerow([key, value])   


###################################################################################################################################################
##############################                       Utils - json                       ###########################################################
###################################################################################################################################################

def load_dict_from_jsonfile(filepath):
# Return a dict loaded from a json file

    try:
        # load from file:
        with open(filepath, 'r') as f:
            try:
                return json.load(f)
            # if the file is empty the ValueError will be thrown
            except ValueError:
                return {}
    except:
        return {}


def save_dict_to_jsonfile(filepath,dico):
# Dumps a dict into a json file

    # save to file:
    with open(filepath, 'w') as f:
        json.dump(dico, f)



###################################################################################################################################################
##############################                       Utils - files                       ##########################################################
###################################################################################################################################################

def check_if_file_exist_and_exit_if_not_found(filepath):
# Checks if a file exists and if it is not found we print an error and exit.
    if os.path.isfile(filepath)==False:
        print("file "+filepath+" not found")
        sys.exit()

def check_if_folder_exist_and_exit_if_not_found(folderpath):
# Checks if a folder exists and if it is not found we print an error and exit.
    if not os.path.exists(folderpath):
        print("folder "+folderpath+" not found")
        sys.exit()

def check_and_create_if_not_exist_path(a_path):
# Checks if a path exists and creates the path if not exist yet.
    if not os.path.exists(a_path):
        os.makedirs(a_path)


def get_file_basename(filepath,no_extension_at_all = True):
# Return the basename of a file.
# If no_extension_at_all == True, returns the basename composed of name without any extensions; 
# otherwise returns the basename composed of name+extensions minus the last extension

    array_split_sep = filepath.split(os.sep)
    filename = array_split_sep[len(array_split_sep)-1]
    array_split_dot = filename.split(".")
    basename = ""

    if no_extension_at_all == True :
        #return the basename composed of name without any extensions
        return array_split_dot[0]

    for i in range(len(array_split_dot)-1):
        if i !=0 :
            basename += "."
        basename +=array_split_dot[i]

    #return the basename composed of name+extensions minus the last extension
    return basename


###################################################################################################################################################
##############################                       Utils - maths                      ###########################################################
###################################################################################################################################################


def get_area_from_ordinates(list_of_ordinates,step_length=1):
# return the area describes by a ordered list of ordinates, separated between them by a dx = step_length
    y = np.array(list_of_ordinates)
    # Compute the area using the composite trapezoidal rule.
    return trapz(y, dx=step_length)


def get_score_from_ordinates(list_of_ordinates):
# Returns the sum of ordinates from a list of ordinates
    score = 0
    for ordinate in list_of_ordinates:
        score += float(ordinate)
    return score


###################################################################################################################################################
##############################                           Main                           ###########################################################
###################################################################################################################################################


def process_comparisons(ronin_tripinfos_filepath,ronin_edgedata_filepath,ronin_loadsmatrix_folderpath,sumo_tripinfos_filepath,sumo_edgedata_filepath,sumo_vehroute_filepath,sumo_netstate_filepath):
# Make all the comparisons wanted

    print(" ")

    begin_trip = time.time()
    print "comparison duration trip : begin."
    compare_duration_trip(sumo_tripinfos_filepath,ronin_tripinfos_filepath)
    print "comparison duration trip : done."
    end_trip = time.time()
    
    print(" ")
    
    begin_edges = time.time()
    print "comparison edges infos : begin."
    compare_edges_infos(sumo_edgedata_filepath,ronin_edgedata_filepath,True)
    print "comparison edges infos : done."
    end_edges = time.time()

    print(" ")

    begin_positions = time.time()
    print "comparison positions vehicles : begin."
    compare_vehicles_positions(sumo_vehroute_filepath,sumo_netstate_filepath,ronin_loadsmatrix_folderpath)
    print "comparison positions vehicles : done."
    end_positions = time.time()

    print(" ")
    print("Time trips comparison : "+str(end_trip - begin_trip))
    print("Time edges comparison : "+str(end_edges - begin_edges))
    print("Time positions comparison : "+str(end_positions - begin_positions))
    print(" ")
    print("Total time comparisons : "+str(end_positions - begin_trip))


def create_script_folders():
# checks that the folders used by this script exist.
    check_and_create_if_not_exist_path(OUTPUT_PATH)
    check_and_create_if_not_exist_path(RONIN_CREATED_INPUTS_PATH)
    check_and_create_if_not_exist_path(SUMO_CREATED_INPUTS_PATH)


def usage():
    #python compareStats.py --ronin_tripinfos inputs/ronin/tripinfos.ti.xml --ronin_edgedata inputs/ronin/edgeData.edd.xml --ronin_loadsmatrix inputs/ronin/LoadsMatrix --sumo_tripinfos inputs/sumo/tripinfo-output.xml --sumo_edgedata inputs/sumo/edgeData.xml --sumo_vehroute inputs/sumo/vehroute-output.xml --sumo_netstate inputs/sumo/netstate-dump.xml
    print("This script compares the outputs of Ronin and SUMO, and generates the results in an 'output' folder that is generated by this script.")
    print("usage : compareStats.py --ronin_tripinfos filepath1 --ronin_edgedata filepath2 --ronin_loadsmatrix filepath3 --sumo_tripinfos filepath4 --sumo_edgedata filepath5 --sumo_vehroute filepath6 --sumo_netstate filepath7")
    print("\t--ronin_tripinfos : the path to the tripinfos output file generated by ronin.")
    print("\t--ronin_edgedata : the path to the edgedata output file generated by ronin.")
    print("\t--ronin_loadsmatrix : the path to the loads matrix output folder generated by ronin.")
    print("\t--sumo_tripinfos : the path to the tripinfos output file generated by SUMO.")
    print("\t--sumo_edgedata : the path to the edgedata output file generated by SUMO.")
    print("\t--sumo_vehroute : the path to the vehroute output file generated by SUMO.")
    print("\t--sumo_netstate : the path to the netstate output file generated by SUMO.")



def main(argv):

    create_script_folders()

    ronin_tripinfos_filepath = ""
    ronin_edgedata_filepath = ""
    ronin_loadsmatrix_folderpath = ""

    sumo_tripinfos_filepath = ""
    sumo_edgedata_filepath = ""
    sumo_vehroute_filepath = ""
    sumo_netstate_filepath = ""


    try:
        opts, args = getopt.getopt(argv, "h", ["help","ronin_tripinfos=","ronin_edgedata=","ronin_loadsmatrix=","sumo_tripinfos=","sumo_edgedata=","sumo_vehroute=","sumo_netstate="])
    except getopt.GetoptError as err:
        print str(err)
        print ""
        usage()
        sys.exit(-1)
 
    opt_list = [opt for opt, arg in opts]

    if len(opt_list) < 7 and  '-h' not in opt_list and '--help' not in opt_list:
        print "Error : please give the good amount of required arguments."
        print ""
        usage()
        sys.exit(-1)

    for opt, arg in opts :
        if opt in ("-h", "--help"):
            usage()
            sys.exit()
        elif opt in ("--ronin_tripinfos"):
            check_if_file_exist_and_exit_if_not_found(arg)
            ronin_tripinfos_filepath = arg
        elif opt in ("--ronin_edgedata"):
            check_if_file_exist_and_exit_if_not_found(arg)
            ronin_edgedata_filepath = arg
        elif opt in ("--ronin_loadsmatrix"):
            check_if_folder_exist_and_exit_if_not_found(arg)
            ronin_loadsmatrix_folderpath = arg
        elif opt in ("--sumo_tripinfos"):
            check_if_file_exist_and_exit_if_not_found(arg)
            sumo_tripinfos_filepath = arg
        elif opt in ("--sumo_edgedata"):
            check_if_file_exist_and_exit_if_not_found(arg)
            sumo_edgedata_filepath = arg
        elif opt in ("--sumo_vehroute"):
            check_if_file_exist_and_exit_if_not_found(arg)
            sumo_vehroute_filepath = arg
        elif opt in ("--sumo_netstate"):
            check_if_file_exist_and_exit_if_not_found(arg)
            sumo_netstate_filepath = arg
        else:
            print("option not recognized : "+str(opt))
            usage()
            sys.exit()

    process_comparisons(ronin_tripinfos_filepath,ronin_edgedata_filepath,ronin_loadsmatrix_folderpath,sumo_tripinfos_filepath,sumo_edgedata_filepath,sumo_vehroute_filepath,sumo_netstate_filepath)



if __name__=='__main__':
    main(sys.argv[1:])