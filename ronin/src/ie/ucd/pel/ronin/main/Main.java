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
package ie.ucd.pel.ronin.main;

import ie.ucd.pel.ronin.communication.socket.RoninServer;
import ie.ucd.pel.ronin.simulation.Simulation;
import ie.ucd.pel.ronin.utils.MainUtils;
import ie.ucd.pel.ronin.utils.RunUtils;

/**
 *
 * @author Come CACHARD
 */
public class Main {

    /**
     * The entry point of Ronin.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RoninCommandLineConfigurationInfos roninConf = MainUtils.getRoninCmdConfiguration(args);

        //if we don't want to make an average profiling time of Ronin
        if (roninConf.getNumberOfSimulationsForAverageProfilingTime() <= 0) {
            if (roninConf.isSimulationMonitoredByServer()) {
                RoninServer roninServer = new RoninServer(roninConf);
                roninServer.start();
            } else {
                Simulation sm = new Simulation(roninConf);
                sm.start();
            }
        } else {
            RunUtils.getAverageProfilingTimeForSumocfg(roninConf, roninConf.getNumberOfSimulationsForAverageProfilingTime());
        }
        //RunUtils.getAverageProfilingTimeForEachScaleInFolder(roninConf,"../../TAPASCologne-0.17.0/cologne_scale/","cologne_",10);
    }

}
