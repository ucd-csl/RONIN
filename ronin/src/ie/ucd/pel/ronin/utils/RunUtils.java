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
package ie.ucd.pel.ronin.utils;

import ie.ucd.pel.ronin.main.RoninCommandLineConfigurationInfos;
import ie.ucd.pel.ronin.simulation.Simulation;
import java.io.File;
import static ie.ucd.pel.ronin.utils.FileUtils.getFileNameWithoutExtension;

/**
 *
 * @author Come CACHARD
 *
 * Class used by the Main class to do specific running of simulations.
 */
public class RunUtils {

    /**
     * The extension of a file sumocfg.
     */
    private static final String EXTENSION_SUMOCFG = ".sumocfg";

    /**
     * Method used to make an average of profiling time of a simulation with the
     * sumocfg specified in command line arguments.
     *
     * @param roninConf the command configuration of the simulation
     * @param nbSimulationToDo the number of simulations we want to do to get
     * the average profiling time
     */
    public static void getAverageProfilingTimeForSumocfg(RoninCommandLineConfigurationInfos roninConf, int nbSimulationToDo) {
        System.out.println("");
        System.out.println("**********************************************************************************************************************************************");
        System.out.println("*************************** File " + roninConf.getSumocfgFilePath() + " **************************");
        System.out.println("**********************************************************************************************************************************************");

        try {
            roninConf.setPrintProfilingTime(false);
            double simuTime = 0.;
            double timeComputationLoads = 0;
            double timePropagateOverloads = 0;
            double timeRepositioning = 0;
            double timeComputeStatistics = 0;
            double timeWriteCurrentStepOutputs = 0;
            double timeWriteEndSimulationOutputs = 0;
            double nbSteps = 1;

            for (int i = 0; i < nbSimulationToDo; i++) {
                Simulation sm = new Simulation(roninConf);
                sm.start();
                simuTime += sm.getProfilingTimeStats().getDuration();
                timeComputationLoads += sm.getProfilingTimeStats().getTimeComputationLoads();
                timePropagateOverloads += sm.getProfilingTimeStats().getTimePropagateOverloads();
                timeRepositioning += sm.getProfilingTimeStats().getTimeRepositioning();
                timeComputeStatistics += sm.getProfilingTimeStats().getTimeComputeStatistics();
                timeWriteCurrentStepOutputs += sm.getProfilingTimeStats().getTimeWriteCurrentStepOutputs();
                timeWriteEndSimulationOutputs += sm.getProfilingTimeStats().getTimeWriteEndSimulationOutputs();
                nbSteps = sm.getCurrentStep();
            }

            simuTime /= nbSimulationToDo;
            timeComputationLoads /= nbSimulationToDo;
            timePropagateOverloads /= nbSimulationToDo;
            timeRepositioning /= nbSimulationToDo;
            timeComputeStatistics /= nbSimulationToDo;
            timeWriteCurrentStepOutputs /= nbSimulationToDo;
            timeWriteEndSimulationOutputs /= nbSimulationToDo;

            System.out.println("----------------------------------------------------------------------");
            System.out.println("--------------------Global results of running time--------------------");
            System.out.println("----------------------------------------------------------------------");
            System.out.println("");
            System.out.println("AVG Duration of the simulation : " + simuTime + " seconds.");

            System.out.println("");
            System.out.println("AVG Loads Matrix time profiling :");
            System.out.println("\tAVG  step 1 computing loads : ");
            System.out.println("\t\tAVG total time (seconds) : " + timeComputationLoads);
            System.out.println("\t\tAVG average time (seconds) : " + timeComputationLoads / nbSteps);
            System.out.println("\tAVG  step 2 propagate overloads : ");
            System.out.println("\t\tAVG total time (seconds) : " + timePropagateOverloads);
            System.out.println("\t\tAVG average time (seconds) : " + timePropagateOverloads / nbSteps);
            System.out.println("\tAVG  step 3 repositioning : ");
            System.out.println("\t\tAVG total time (seconds) : " + timeRepositioning);
            System.out.println("\t\tAVG average time (seconds) : " + timeRepositioning / nbSteps);
            System.out.println("\tAVG  step 4 compute statistics : ");
            System.out.println("\t\tAVG total time (seconds) : " + timeComputeStatistics);
            System.out.println("\t\tAVG average time (seconds) : " + timeComputeStatistics / nbSteps);
            System.out.println("\tAVG  step 5 write current step outputs : ");
            System.out.println("\t\tAVG total time (seconds) : " + timeWriteCurrentStepOutputs);
            System.out.println("\t\tAVG average time (seconds) : " + timeWriteCurrentStepOutputs / nbSteps);
            System.out.println("\tAVG  step 6 write end simulation outputs : ");
            System.out.println("\t\tAVG total time (seconds) : " + timeWriteEndSimulationOutputs);
        } catch (Exception e) {
            System.out.println("ERROR for file : " + roninConf.getSumocfgFilePath());
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
    }

    /**
     * Method used to make an average of profiling time with sumocfg file.
     *
     * @param roninConf the command configuration of the simulation
     * @param sumocfgFilepath the path to the sumocfg file for the simulation
     * @param nbSimulationToDo the number of simulations we want to do to get
     * the average profiling time
     */
    public static void getAverageProfilingTimeForSumocfg(RoninCommandLineConfigurationInfos roninConf, String sumocfgFilepath, int nbSimulationToDo) {
        roninConf.setSumocfgFilePath(sumocfgFilepath);
        getAverageProfilingTimeForSumocfg(roninConf, nbSimulationToDo);
    }

    /**
     * Method used to make an average of profiling time with sumocfg file that
     * is named like [basename]scale.sumocfg.
     *
     * @param roninConf the command configuration of the simulation
     * @param folderpath the path to the folder that contains the sumocfg files
     * for different scales
     * @param basename the basename of the sumocfg file
     * @param scale the scale of the sumocfg file that is theend of the name of
     * the file
     * @param nbSimulationToDo the number of simulations we want to do to get
     * the average profiling time
     */
    public static void getAverageProfilingTimeForSumocfg(RoninCommandLineConfigurationInfos roninConf, String folderpath, String basename, int scale, int nbSimulationToDo) {
        System.out.println("");
        System.out.println("***********************************************************************");
        System.out.println("*************************** Scale " + scale + " **********************************");
        System.out.println("***********************************************************************");

        try {
            //"../../TAPASCologne-0.17.0/cologne_scale/cologne_"+scale+".sumocfg"
            String finalFolderpath = folderpath.endsWith(File.separator) ? folderpath : folderpath + File.separator;
            roninConf.setSumocfgFilePath(finalFolderpath + basename + scale + EXTENSION_SUMOCFG);
            roninConf.setPrintProfilingTime(false);
            double simuTime = 0.;
            double timeComputationLoads = 0;
            double timePropagateOverloads = 0;
            double timeRepositioning = 0;
            double timeComputeStatistics = 0;
            double timeWriteCurrentStepOutputs = 0;
            double timeWriteEndSimulationOutputs = 0;
            double nbSteps = 1;

            for (int i = 0; i < nbSimulationToDo; i++) {
                Simulation sm = new Simulation(roninConf);
                sm.start();
                simuTime += sm.getProfilingTimeStats().getDuration();
                timeComputationLoads += sm.getProfilingTimeStats().getTimeComputationLoads();
                timePropagateOverloads += sm.getProfilingTimeStats().getTimePropagateOverloads();
                timeRepositioning += sm.getProfilingTimeStats().getTimeRepositioning();
                timeComputeStatistics += sm.getProfilingTimeStats().getTimeComputeStatistics();
                timeWriteCurrentStepOutputs += sm.getProfilingTimeStats().getTimeWriteCurrentStepOutputs();
                timeWriteEndSimulationOutputs += sm.getProfilingTimeStats().getTimeWriteEndSimulationOutputs();
                nbSteps = sm.getCurrentStep();
            }

            simuTime /= nbSimulationToDo;
            timeComputationLoads /= nbSimulationToDo;
            timePropagateOverloads /= nbSimulationToDo;
            timeRepositioning /= nbSimulationToDo;
            timeComputeStatistics /= nbSimulationToDo;
            timeWriteCurrentStepOutputs /= nbSimulationToDo;
            timeWriteEndSimulationOutputs /= nbSimulationToDo;

            System.out.println("----------------------------------------------------------------------");
            System.out.println("--------------------Global results of running time--------------------");
            System.out.println("----------------------------------------------------------------------");
            System.out.println("");
            System.out.println("AVG Duration of the simulation : " + simuTime + " seconds.");

            System.out.println("");
            System.out.println("AVG Loads Matrix time profiling :");
            System.out.println("\tAVG  step 1 computing loads : ");
            System.out.println("\t\tAVG total time (seconds) : " + timeComputationLoads);
            System.out.println("\t\tAVG average time (seconds) : " + timeComputationLoads / nbSteps);
            System.out.println("\tAVG  step 2 propagate overloads : ");
            System.out.println("\t\tAVG total time (seconds) : " + timePropagateOverloads);
            System.out.println("\t\tAVG average time (seconds) : " + timePropagateOverloads / nbSteps);
            System.out.println("\tAVG  step 3 repositioning : ");
            System.out.println("\t\tAVG total time (seconds) : " + timeRepositioning);
            System.out.println("\t\tAVG average time (seconds) : " + timeRepositioning / nbSteps);
            System.out.println("\tAVG  step 4 compute statistics : ");
            System.out.println("\t\tAVG total time (seconds) : " + timeComputeStatistics);
            System.out.println("\t\tAVG average time (seconds) : " + timeComputeStatistics / nbSteps);
            System.out.println("\tAVG  step 5 write current step outputs : ");
            System.out.println("\t\tAVG total time (seconds) : " + timeWriteCurrentStepOutputs);
            System.out.println("\t\tAVG average time (seconds) : " + timeWriteCurrentStepOutputs / nbSteps);
            System.out.println("\tAVG  step 6 write end simulation outputs : ");
            System.out.println("\t\tAVG total time (seconds) : " + timeWriteEndSimulationOutputs);
        } catch (Exception e) {
            System.out.println("ERROR for scale : " + scale);
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
    }

    /**
     * Method used to make an average of profiling time with sumocfg file that
     * is named like [basename]scale.sumocfg for each sumocfg file in a given
     * folder.
     *
     * @param roninConf the command configuration of the simulation
     * @param folderpath the path to the folder that contains the sumocfg files
     * for different scales
     * @param basename the basename of the sumocfg files
     * @param nbRunsPerScale the number of simulations we want to do to get the
     * average profiling time for each scale
     */
    public static void getAverageProfilingTimeForEachScaleInFolder(RoninCommandLineConfigurationInfos roninConf, String folderpath, String basename, int nbRunsPerScale) {
        File folder = new File(folderpath);
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isFile() && file.getName().startsWith(basename) && file.getName().endsWith(EXTENSION_SUMOCFG)) {
                String[] parsedName = getFileNameWithoutExtension(file).split(basename);
                int scale = Integer.parseInt(parsedName[parsedName.length - 1]);
                getAverageProfilingTimeForSumocfg(roninConf, folderpath, basename, scale, nbRunsPerScale);
            }
        }
    }

}
