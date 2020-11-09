# Ronin: a SUMO Interoperable Mesoscopic Urban Traffic Simulator
Ronin is an opensource macroscopic simulator developed in Java at UCD, Ireland. It is "SUMO-friendly" as it takes in inputs files used by SUMO such as SUMO net file and SUMO route file. Its algorithm is based on the idea of computing a Loads Matrix for each step of the simulation from the project ROThAr.


## Contributors:
* Takfarinas Saber: Lero – the Irish Software Research Centre, School of Computer Science, University College Dublin, Ireland
* Côme Cachard: Institut National des Sciences Appliquées, Lyon, France
* Anthony Ventresque: Lero – the Irish Software Research Centre, School of Computer Science, University College Dublin, Ireland

## Citation:

	@inproceedings{saber2020ronin,
	  title={RONIN: a SUMO Interoperable Mesoscopic Urban Traffic Simulator},
	  author={Saber, Takfarinas and Cachard, Côme and Ventresque, Anthony},
	  booktitle={SmartCity},
	  pages={},
	  year={2020},
	  organization={IEEE}
	}


## How to use Ronin

In this project, you can find the sources of the macroscopic simulator developed in java, and python scripts that are used to compare the outputs of Ronin with outputs of SUMO.

### Use the macroscopic simulator

#### Launch the simulator

Ronin takes as mandatory input the path to a SUMO cfg file that contains global information about the simulation such as :
* the input files to use composed of : 
  * the path to the SUMO net file that contains the description of the road network.
  * the path to the SUMO route file that contains the description of the vehicles considered during the simulation.
* the time configuration composed of : 
  * the duration of a time step
  * the beginning time of the simulation
  * the end time of the simulation
  
Then we can give several options to Ronin in order to produce specific outputs described in next section. By default, no output is produced.

Below the usage of Ronin :

```
Usage : Ronin --sumocfg arg_0 [ --help ] [ --profilingTime ] [ --avgProfilingTime arg_0 ] [ --lightLoadsMatrix ] [ --name arg_0 ] [ --loadsMatrix ] [ --monitoredByServer ] [ --vehiclesTypesFile arg_0 ] [ --tripInfos ] [ --overwrite ] [ --roninPort arg_0 ] [ --edgeData ]
	Required options :
		-cfg --sumocfg           :    the path to the SUMO .sumocfg file.

	Optional options :
		-h --help                :    print the usage of this application.
		-pt --profilingTime      :    if used, we print information about the profiling time of the simulation.
		-avgpt --avgProfilingTime:    if used, we execute the specified number of simulations to give an average of the profiling time of the simulation.
		-llm --lightLoadsMatrix  :    if used, the light loads matrix will be generated. By default, if no output has been chosen, we generate the ligth loads matrix.
		-n --name                :    the name of the simulation for outputs naming.
		-lm --loadsMatrix        :    if used, we generate the complete Loads Matix.
		-m --monitoredByServer   :    if used, the simulation is monitored by a Ronin server configured according to sumocfg file. The simulation will then be managed by queries of a Ronin Client.
		-vtf --vehiclesTypesFile :    if specified, we read the different types of vehicles from this file instead of the sumorou file described in the sumocfg file.
		-ti --tripInfos          :    if used, we generate the output containing the trip informations.
		-ow --overwrite          :    if used, we overwrite the outputs if name is already taken.
		-p --roninPort           :    if the simulation is monitored by a ronin server, this option describes the port to use. Else the port will be read from the sumocfg file.
		-ed --edgeData           :    if used, we generate the output containing the edges data.
```

So one example of launching Ronin:
```
java -cp ronin.jar ie.ucd.pel.ronin.main.Main -llm  --sumocfg ../../TAPASCologne-0.17.0/cologne.sumocfg
```
In the above example, Ronin will read the SUMO cfg file cologne.sumocfg and will produce a light loads matrix under a default output folder for the simulation called "simulation". If the default folder already exists, we create a new one.

#### Launch the simulator with a server

RONIN offers the possibility to interact with the simulation during runtime. We just have to use the option --monitoredByServer in command line to create a server that will host the simulation. So the Ronin Client will starts the server, the server initializes the simulation and will be then connect to the Client that will send to the server queries to process about the simulation. The server will be configured according to the parameters described in sumocfg file in traci-serverType section, especially for the remote port where the server will be listening to new Clients unless the port is described in parameters. A simulation monitored by a server does not process by itself, it waits explicitly for queries from Client. Only one Client at a time can be connected to a Ronin server.

An object RoninClient is available to communicate with the server and send queries. The queries that are available are :
* add a vehicle to the simulation
* remove a vehicle from the simulation
* process the next step of the simulation
* close the Ronin server and end the simulation
* a lot more queries...

So one example of launching a Ronin server:
```
java -cp ronin.jar ie.ucd.pel.ronin.communication.socket.TCPClient --sumocfg ../../TAPASCologne-0.17.0/cologne_scale/cologne.sumocfg -llm -ow --vehiclesTypesFile  ../../TAPASCologne-0.17.0/cologne_scale/cologne_scale_10.rou.xml
```
In the above example, we use a class TCPClient that use a Ronin Client (it can be any java project that uses a Ronin Client). The Ronin Client will start a server and read the SUMO cfg file cologne.sumocfg and will produce a light loads matrix under a default output folder for the simulation called "simulation". If the default folder already exists, we create a new one. The server will wait for queries from a Client. Moreover, we choose to read the vehicles types from file cologne_scale_10.rou.xml instead of the .rou file described in cologne.sumocfg. 

### Do the profiling time of Ronin

We have the possibility to make the profiling time of Ronin to see its efficiency. The application prints information about :
* the duration of the simulation in seconds
* the duration of the computation of loads in seconds
* the duration of the propagation of overloads in seconds
* the duration of the repositioning of vehicles in seconds
* the duration of computing statistics on objects such as Edges for outputs in seconds
* the duration of writing the outputs of the lightloadsMatrix and the loadsMatrix in seconds
* the duration of writing the other outputs in seconds

### About the outputs of Ronin

Ronin creates an output folder where it will create for each time we run the application a new folder. We can specify the name of this output folder in the parameters in command line. If a name is already taken, Ronin generate a new folder with the same name by appending a number, so no simulation output folder can be overwritten. 
Ronin can produce several kinds of output:
* the light loads matrix : Ronin creates a folder called lightLoadsMatrix, and for each step of the simulation, we produce a step file .llm.xml that will contain for each edge of the road network :
  * its id
  * the number of vehicles that were on the edge at the considered step, i.e the load of the edge
* the loads matrix : Ronin creates a folder called lightLoadsMatrix, and for each step of the simulation, we produce a step file .lm.xml that will contain for each edge of the road network :
  * its id
  * the number of vehicles that were on the edge at the considered step, i.e the load of the edge
  * the list of the ids of the vehicles that were on the edge at the considered step
* the edgeData : Ronin generates a file .edd.xml that will contain for each edge of the road network :
  * its id
  * the number of arrived vehicles on this Edge
  * its mean density in nbVehicles / kilometer
  * its mean traffic volume in nbVehicles / hour
  * its mean travel time in seconds
  * the mean speed of vehicles on this Edge in m/s
* the trips information : Ronin generates a file .ti.xml that will contain for each vehicle of the simulation :
  * its id
  * its departure time in seconds
  * the id of its departure Edge
  * its arrival time in seconds
  * the id of its arrival Edge
  * the length of its total route in meter
  * the duration in seconds of its trip
  * the number of steps this Vehicle had to wait
  * the id of the vehicle type of this Vehicle

### Comparing Ronin and SUMO outputs

We created Python scripts in order to compare Ronin with SUMO. Below you can find which Ronin's output is compared with which SUMO's output :

| Ronin output  | SUMO output   | Comparison measures      |
| ------------- | ------------- | ------------------------ |
| Trips information  | tripinfo-output (http://sumo.dlr.de/wiki/Simulation/Output/TripInfo)  | duration trip |
| Edge data  | Edge-Based Network States (http://sumo.dlr.de/wiki/Simulation/Output/Lane-_or_Edge-based_Traffic_Measures) | average density, average traffic volume|
| Loads matrix  | netstate-dump (http://sumo.dlr.de/wiki/Simulation/Output/RawDump), vehroute-output (http://sumo.dlr.de/wiki/Simulation/Output/VehRoutes) | positions of vehicles through time|

#### compareUsedVehicles.py

This script takes in arguments the path to files that contains vehicle xml elements, and will compare the ids in order to see if the files contains the same list of ids of vehicles. Below its usage :

```
usage : compareUsedVehicles.py --files filepath1[,filepath2[,filepath3[...]]]
    --files : the filepaths of xml files that contains "vehicle" elements.
```

So one example of usage :

```
python compareUsedVehicles.py --files ronin_sumorou.rou.xml,sumo_sumorou.rou.xml
```

#### createSumoRouFromVehroute.py

The script will produce a sumo rou file which will contain only the vehicles that are used in a vehroute sumo output file.
This script is mainly used to produce sumorou file for different scale of simulation for Ronin in order to be able to make comparison with SUMO. Below its usage :

```
usage : createSumoRouFromVehroute.py --sumorou sumorouPath --vehroute vehroutePath [--output outputName]
    --sumorou : the path to the sumorou path we want to create a new one from.
    --vehroute : the path to the sumo vehroute output file that contains the vehicles that we want to keep in our new sumorou file.
```

So one example of usage :

```
python createSumoRouFromVehroute.py --sumorou cologne_scale_100.rou.xml --vehroute sumo_vehroute_scale_30.xml --output cologne_scale_30
```


#### compareLoadsMatrix.py

The script will compare if given loads matrix folders contain the same loads per edge. This script is mainly used to be sure that we got the same results (or not).

```
usage : compareLoadsMatrix.py --folders loadsMatrixFolderPath1[,loadsMatrixFolderPath2[,loadsMatrixFolderPath3[...]]]
  --folders : the folder pahts to loads matrixes to compare. It can be light loads matrixes.
```


So one example of usage :

```
python compareLoadsMatrix.py --folders simulation1/loadsMatrix,simulation2/loadsMatrix
```

#### compareStats.py

This script compares the outputs of Ronin and SUMO according to the table above, and generates the results in an 'input' folder that is generated by this script. For each comparison, we produce an excel file that contains the results.
We make the following comparisons :
* comparison of the trip duration : (duration trip Ronin - duration trip SUMO) / duration trip SUMO
* comparison of the edges density : (average density Ronin - average density SUMO) / average density SUMO
* comparison of the edges traffic volume : (average traffic volume Ronin - average traffic volume SUMO) / average density SUMO
* comparison for each vehicle of their positions through time (we create a specific folder 'position' that will contain one output file by vehicle) : position Ronin - position SUMO
* comparison of the positions of vehicles by calculating the difference of space-time area : (area Ronin - area SUMO) / area SUMO

**/!\ One important point :** as in order to make these comparisons we have to read xml files that can be huge and long to parse, we save the parsing of these files in json dictionaries that are loaded first if they exist. These json dictionaries are generated in a created folder called "inputs/created_inputs". If you want to change the files to compare, be sure to delete these created inputs.

Below the usage of this script :
```
usage : compareStats.py --ronin_tripinfos filepath1 --ronin_edgedata filepath2 --ronin_loadsmatrix filepath3 --sumo_tripinfos filepath4 --sumo_edgedata filepath5 --sumo_vehroute filepath6 --sumo_netstate filepath7
  --ronin_tripinfos : the path to the tripinfos output file generated by ronin.
  --ronin_edgedata : the path to the edgedata output file generated by ronin.
  --ronin_loadsmatrix : the path to the loads matrix output folder generated by ronin.
  --sumo_tripinfos : the path to the tripinfos output file generated by SUMO.
  --sumo_edgedata : the path to the edgedata output file generated by SUMO.
  --sumo_vehroute : the path to the vehroute output file generated by SUMO.
  --sumo_netstate : the path to the netstate output file generated by SUMO.
```


So one example of usage :

```
python compareStats.py --ronin_tripinfos inputs/ronin/tripinfos.ti.xml --ronin_edgedata inputs/ronin/edgeData.edd.xml --ronin_loadsmatrix inputs/ronin/LoadsMatrix --sumo_tripinfos inputs/sumo/tripinfo-output.xml --sumo_edgedata inputs/sumo/edgeData.xml --sumo_vehroute inputs/sumo/vehroute-output.xml --sumo_netstate inputs/sumo/netstate-dump.xml
```

### Use the GUI

Ronin has a GUI that allows the user to see the result of a simulation (not launching and see the result in real time, yet).
So the user must open first a sumo network file, then he can open a matrix of loads folder that will display the result of the corresponding simulation. The window of the application will then display a tableview with the list of the edges with their loads for each step of the simulation, and a list view with all the step of the simulation. The map of the concerned city will be updated automatically. Below the usage of the GUI :

```
java -cp ronin.jar ie.ucd.pel.ronin.javafx.MainFx
```

## Prerequisites

The simulator and the python scripts are totally independant. One produces outputs, the other makes statistics on these outputs. You do not have to install all dependencies if you do not want to use a part of the project.

### Ronin Dependencies

* Java 1.8
* JUnit 4.12

### Python scripts dependencies

* numpy==1.11.2
* XlsxWriter==0.9.6
* pyexcel-io==0.3.3
* pyexcel==0.4.5
* scipy==0.18.1


## License

This project is licensed under the ? License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments
This work was supported with the financial support of the Science Foundation Ireland grant 13/RC/2094 and co-funded under the European Regional Development Fund through the Southern & Eastern Regional Operational Programme to Lero - the Irish Software Research Centre (www.lero.ie).

## References
* T. Saber, A. Ventresque and J. Murphy, "ROThAr: Real-Time On-Line Traffic Assignment with Load Estimation," 2013 IEEE/ACM 17th International Symposium on Distributed Simulation and Real Time Applications, Delft, 2013, pp. 79-86.
doi: 10.1109/DS-RT.2013.17,URL: http://ieeexplore.ieee.org/stamp/stamp.jsp?tp=&arnumber=6690497&isnumber=6690476
* SUMO official website : http://www.dlr.de/ts/en/desktopdefault.aspx/tabid-9883/16931_read-41000/
