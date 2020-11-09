/* 
 * Copyright (C) 2017 Come CACHARD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ie.ucd.pel.ronin.filesreader.sumofilesreader;

import ie.ucd.pel.ronin.model.TimeConfiguration;
import ie.ucd.pel.ronin.model.Network;

/**
 *
 * @author Come CACHARD
 *
 * Class used to parse SUMO files and generate the traffic network and the
 * vehicles.
 */
public class SumoFilesReader {

    /**
     * Object containing all the needed informations from SUMO simulator.
     */
    private final SumoConfigInformation sumoInfos;

    /**
     * Constructs and initializes a SUMOFilesReader with the specified SUMO
     * informations.
     *
     * @param sumoInfos object containing all the needed informations from SUMO
     * simulator
     */
    public SumoFilesReader(SumoConfigInformation sumoInfos) {
        this.sumoInfos = sumoInfos;
    }

    /**
     * Returns the time configuration of the simulation.
     *
     * @return the time configuration of the simulation
     */
    public TimeConfiguration readSimulationTimeConfiguration() {
        SumoConfigFileReader sumoConfigReader = new SumoConfigFileReader(sumoInfos.getSumocfgFilePath());
        return sumoConfigReader.readSimulationTimeConfiguration();
    }

    /**
     * Read the Network from SUMO files, constructing and initializing the
     * Edges, the Nodes, the VehicleTypes and the Vehicles.
     *
     * @return the network which will contain the Vehicles and the Traffic Graph
     */
    public Network readNetwork() {
        Network network = new Network();

        SumoNetFileReader sumoNetFileReader = new SumoNetFileReader(sumoInfos.getSumonetFilePath());
        sumoNetFileReader.readGraph(network.getGraph());

        if (!sumoInfos.getVehiclesTypesFilePath().isEmpty()) {
            SumoRouteFileReader sumoRouteFileReader = new SumoRouteFileReader(sumoInfos.getVehiclesTypesFilePath());
            sumoRouteFileReader.readVehicleTypes(network);
        }
        if (!sumoInfos.getSumorouFilePath().isEmpty()) {
            SumoRouteFileReader sumoRouteFileReader = new SumoRouteFileReader(sumoInfos.getSumorouFilePath());
            if (sumoInfos.getVehiclesTypesFilePath().isEmpty()) {
                sumoRouteFileReader.readVehicleTypes(network);
            }
            sumoRouteFileReader.readVehicles(network);
        }

        System.out.println("nodes : " + network.getNodes().size());
        System.out.println("edges : " + network.getEdges().size());
        System.out.println("vehicles : " + network.getAllVehiclesOfSimulation().size());

        return network;
    }

}
