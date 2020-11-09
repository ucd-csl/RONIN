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

import ie.ucd.pel.ronin.utils.FileUtils;
import java.io.File;

/**
 *
 * @author Come CACHARD
 *
 * Class used to read the Network from Sumo files. It contains all the Sumo
 * informations we need.
 */
public class SumoConfigInformation {

    /**
     * The path to the file where to read the vehicles types if we do not read
     * them from the sumorou file got from the sumocfg file.
     */
    private final String vehiclesTypesFilePath;

    /**
     * The path to the SUMO rou file that contains the vehicles,their routes and
     * the vehicle types.
     */
    private final String sumorouFilePath;

    /**
     * The path to the SUMO net file that contains the nodes and the edges.
     */
    private final String sumonetFilePath;

    /**
     * The path to the SUMO sumocfg file that contains global informations for
     * the simulation.
     */
    private final String sumocfgFilePath;

    /**
     * Constructs and initializes a SumoConfigInfos with the given properties.
     *
     * @param sumorouFilePath the path to the SUMO rou file that contains the
     * vehicles and their routes
     * @param sumonetFilePath the path to the SUMO net file that contains the
     * nodes, the edges, and the vehicle types
     * @param sumocfgFilePath the path to the SUMO sumocfg file that contains
     * global informations for the simulation
     * @param vehiclesTypesFilePath the path to the SUMO rou file that contains
     * the vehicles types to read from
     */
    public SumoConfigInformation(String sumorouFilePath, String sumonetFilePath, String sumocfgFilePath, String vehiclesTypesFilePath) {
        this.sumorouFilePath = sumorouFilePath;
        this.sumonetFilePath = sumonetFilePath;
        this.sumocfgFilePath = sumocfgFilePath;
        this.vehiclesTypesFilePath = vehiclesTypesFilePath;
    }

    /**
     * Constructs and initializes a SumoConfigInfos with the sumocfg file; the
     * path to the sumorou and sumonet files are described in the sumocfg file.
     *
     * @param sumocfgFilePath the path to the SUMO sumocfg file that contains
     * global informations for the simulation
     */
    public SumoConfigInformation(String sumocfgFilePath) {
        this.sumocfgFilePath = sumocfgFilePath;

        SumoConfigFileReader scfr = new SumoConfigFileReader(sumocfgFilePath);
        String[] sumoFilePaths = scfr.readSumoFilesInputsConfiguration();
        if (sumoFilePaths.length >= 1 && !sumoFilePaths[0].isEmpty()) {
            this.sumonetFilePath = FileUtils.getFileParentFolderPath(sumocfgFilePath) + File.separator + sumoFilePaths[0];
        } else {
            this.sumonetFilePath = "";
        }
        if (sumoFilePaths.length >= 2 && !sumoFilePaths[1].isEmpty()) {
            this.sumorouFilePath = FileUtils.getFileParentFolderPath(sumocfgFilePath) + File.separator + sumoFilePaths[1];
        } else {
            this.sumorouFilePath = "";
        }
        this.vehiclesTypesFilePath = "";
    }

    /**
     * Constructs and initializes a SumoConfigInfos with the sumocfg file; the
     * path to the sumorou and sumonet files are described in the sumocfg file.
     * And we read the vehicles types from the file described by the path
     * vehiclesTypesFilePath.
     *
     * @param sumocfgFilePath the path to the SUMO sumocfg file that contains
     * global informations for the simulation
     * @param vehiclesTypesFilePath the path to the SUMO rou file that contains
     * the vehicles types to read from
     */
    public SumoConfigInformation(String sumocfgFilePath, String vehiclesTypesFilePath) {
        this.sumocfgFilePath = sumocfgFilePath;

        SumoConfigFileReader scfr = new SumoConfigFileReader(sumocfgFilePath);
        String[] sumoFilePaths = scfr.readSumoFilesInputsConfiguration();
        if (sumoFilePaths.length >= 1 && !sumoFilePaths[0].isEmpty()) {
            this.sumonetFilePath = FileUtils.getFileParentFolderPath(sumocfgFilePath) + File.separator + sumoFilePaths[0];
        } else {
            this.sumonetFilePath = "";
        }
        if (sumoFilePaths.length >= 2 && !sumoFilePaths[1].isEmpty()) {
            this.sumorouFilePath = FileUtils.getFileParentFolderPath(sumocfgFilePath) + File.separator + sumoFilePaths[1];
        } else {
            this.sumorouFilePath = "";
        }
        this.vehiclesTypesFilePath = vehiclesTypesFilePath;
    }

    /**
     * Returns the path to the SUMO rou file that contains the vehicles and
     * their routes
     *
     * @return the path to the SUMO rou file that contains the vehicles and
     * their routes
     */
    public String getSumorouFilePath() {
        return sumorouFilePath;
    }

    /**
     * Returns the path to the SUMO net file that contains the nodes, the edges,
     * and the vehicle types.
     *
     * @return the path to the SUMO net file that contains the nodes, the edges,
     * and the vehicle types
     */
    public String getSumonetFilePath() {
        return sumonetFilePath;
    }

    /**
     * Returns the path to the SUMO sumocfg file that contains global
     * informations for the simulation
     *
     * @return the path to the SUMO sumocfg file that contains global
     * informations for the simulation
     */
    public String getSumocfgFilePath() {
        return sumocfgFilePath;
    }

    /**
     * Returns the path to the file where to read the vehicles types if we do
     * not read them from the sumorou file got from the sumocfg file.
     *
     * @return the path to the file where to read the vehicles types if we do
     * not read them from the sumorou file got from the sumocfg file
     */
    public String getVehiclesTypesFilePath() {
        return vehiclesTypesFilePath;
    }

}
