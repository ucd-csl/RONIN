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
package ie.ucd.pel.ronin.simulation;

import ie.ucd.pel.ronin.main.RoninCommandLineConfigurationInfos;
import ie.ucd.pel.ronin.filesreader.sumofilesreader.SumoFilesReader;
import ie.ucd.pel.ronin.model.Network;
import ie.ucd.pel.ronin.filesreader.sumofilesreader.SumoConfigInformation;
import ie.ucd.pel.ronin.model.Edge;
import ie.ucd.pel.ronin.model.TimeConfiguration;
import ie.ucd.pel.ronin.model.Vehicle;
import ie.ucd.pel.ronin.outputswriter.EdgesWriter;
import ie.ucd.pel.ronin.outputswriter.LightLoadsWriter;
import ie.ucd.pel.ronin.outputswriter.LoadsWriter;
import ie.ucd.pel.ronin.outputswriter.TripInfosWriter;
import ie.ucd.pel.ronin.statistics.SimulationProfilingTimeStatistics;
import ie.ucd.pel.ronin.utils.FileUtils;
import ie.ucd.pel.ronin.utils.MapUtils;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Come CACHARD
 *
 * Core class that runs a simulation.
 */
public class Simulation {

    /**
     * The path to the directory where we want to create the global outputs
     * folder.
     */
    private final String DIRECTORY_PARENT = ".";

    /**
     * The base name of the output directory for all simulations outputs.
     */
    private final String OUTPUT_DIRECTORY_NAME = "output";

    /**
     * The directory path of the outputs of this simulation.
     */
    private final String globalOutputsDirectoryPath;

    /**
     * The network for the simulation.
     */
    private final Network network;

    /**
     * the final map of loads of vehicles per edge for this step with the
     * accurate positions of the running vehicles.
     */
    private final Map<String, List<Vehicle>> currentStepFinalLoads;

    /**
     * The time configurations for the simulation including the begin time, the
     * end time and the step length of the simulation.
     */
    private final TimeConfiguration timeConfig;

    /**
     * The configuration of the simulation set by command line.
     */
    private final RoninCommandLineConfigurationInfos roninCmdConfig;

    /**
     * The current step of the simulation
     */
    private int currentStep;

    /**
     * Object containing the profiling time of the simulation.
     */
    private final SimulationProfilingTimeStatistics profilingTimeStats;

    /**
     * Constructs and initializes a Simulation Manager with the network to
     * simulate. This simulation can be monitored by a Ronin Server. The time
     * unit will be set to 1 second by default.
     *
     * @param roninCmdConfig object containing the configuration of Ronin with
     * the values of command line.
     */
    public Simulation(RoninCommandLineConfigurationInfos roninCmdConfig) {
        this.currentStepFinalLoads = new HashMap<>();

        this.profilingTimeStats = new SimulationProfilingTimeStatistics();

        this.roninCmdConfig = roninCmdConfig;
        SumoConfigInformation sumoInfos = (roninCmdConfig.getVehiclesTypesFilePath().isEmpty()) ? new SumoConfigInformation(roninCmdConfig.getSumocfgFilePath()) : new SumoConfigInformation(roninCmdConfig.getSumocfgFilePath(), roninCmdConfig.getVehiclesTypesFilePath());
        SumoFilesReader sumoReader = new SumoFilesReader(sumoInfos);

        this.network = sumoReader.readNetwork();
        this.timeConfig = sumoReader.readSimulationTimeConfiguration();
        this.currentStep = 0;

        globalOutputsDirectoryPath = getGlobalOutputsDirectoryPath();
    }

    /**
     * Constructs and initializes a Simulation Manager with the network to
     * simulate. We specify the step length in seconds that will be used. This
     * simulation won't be monitored by a server.
     *
     * @param roninCmdConfig object containing the configuration of Ronin with
     * the values of command line.
     * @param stepLength the duration of a time slot in seconds. If it was
     * already defined in Sumo file, the value is overriden by this one.
     */
    public Simulation(RoninCommandLineConfigurationInfos roninCmdConfig, double stepLength) {
        this.currentStepFinalLoads = new HashMap<>();

        this.profilingTimeStats = new SimulationProfilingTimeStatistics();

        this.roninCmdConfig = roninCmdConfig;

        SumoConfigInformation sumoInfos = new SumoConfigInformation(roninCmdConfig.getSumocfgFilePath());
        SumoFilesReader sumoReader = new SumoFilesReader(sumoInfos);

        this.network = sumoReader.readNetwork();
        this.timeConfig = sumoReader.readSimulationTimeConfiguration();
        this.timeConfig.setStepLength(stepLength);
        this.currentStep = 0;

        globalOutputsDirectoryPath = getGlobalOutputsDirectoryPath();
    }

    /**
     * Returns the current step number of the simulation.
     *
     * @return the current step number of the simulation
     */
    public int getCurrentStep() {
        return currentStep;
    }

    /**
     * Returns the current time slot of the simulation in seconds.
     *
     * @return the current time slot of the simulation in seconds
     */
    public double getCurrentTimeSlot() {
        return currentStep * timeConfig.getStepLength();
    }

    /**
     * Returns the network of this Simulation.
     *
     * @return the network of this Simulation
     */
    public Network getNetwork() {
        return network;
    }

    /**
     * Returns the time configuration of the simulation.
     *
     * @return the time configuration of the simulation
     */
    public TimeConfiguration getTimeConfig() {
        return timeConfig;
    }

    /**
     * Returns the profiling time statistics of the simulation.
     *
     * @return the profiling time statistics of the simulation
     */
    public SimulationProfilingTimeStatistics getProfilingTimeStats() {
        return profilingTimeStats;
    }

    /**
     * Returns the output directory path for the directory where we will write
     * our outputs for this simulation. If the directory does not exist we
     * creates it. If the directory already exists, we creates a new one.
     *
     * @return the output directory path for the directory where we will write
     * our outputs for this simulation.
     */
    private String getGlobalOutputsDirectoryPath() {
        Path path = Paths.get(DIRECTORY_PARENT, OUTPUT_DIRECTORY_NAME, roninCmdConfig.getSimulationName());

        if (Files.exists(path) && roninCmdConfig.isOverwriteOutputs()) {
            FileUtils.deleteFolder(path.toFile());
        }

        int i = 0;
        while (Files.exists(path) && roninCmdConfig.isOverwriteOutputs() == false) {
            String simulationDirectory = roninCmdConfig.getSimulationName() + "(" + i + ")";
            path = Paths.get(DIRECTORY_PARENT, OUTPUT_DIRECTORY_NAME, simulationDirectory);
            i++;
        }

        File resultDirectory = new File(path.toString());

        if (!resultDirectory.exists()) {
            resultDirectory.mkdirs();
        }

        return path.toString();
    }

    /**
     * Returns the list of running vehicles that are on a specific Edge at the
     * current time step of this Simulation.
     *
     * @param idEdge the id of the Edge that we want the list of vehicles
     * @return the list of running vehicles that are on a specific Edge at the
     * current time step of this Simulation
     */
    public List<Vehicle> getCurrentStepFinalLoadForEdge(String idEdge) {
        return currentStepFinalLoads.getOrDefault(idEdge, new LinkedList<>());
    }

    /**
     * Increases the current step number by one.
     */
    private void increaseCurrentStep() {
        currentStep++;
    }

    /**
     * Returns true if the simulation must end, false otherwise.
     *
     * @return true if the simulation must end, false otherwise.
     */
    public boolean isFinished() {
        //We do the simulation while there is stil a not arrived vehicle or until we reach the end time of the simulation
        boolean doWeConsiderEndTime = timeConfig.getEndTime() >= 0;
        return !(!network.areAllVehiclesArrived() && ((timeConfig.getBeginTime() + currentStep * timeConfig.getStepLength() <= timeConfig.getEndTime() && doWeConsiderEndTime) || doWeConsiderEndTime == false));
    }

    /**
     * Launches the simulation. If this simulation is monitored by a Ronin
     * server, this method is not used because all steps and actions will be
     * triggered by client's queries.
     */
    public void start() {
        System.out.println("Let's go !");
        System.out.println("");

        profilingTimeStats.setLaunchTime(System.currentTimeMillis() / 1000);
        work();
        profilingTimeStats.setEndTime(System.currentTimeMillis() / 1000);

        printEndSimulationInfos();
    }

    /**
     * Displays information about the simulation.
     */
    private void printEndSimulationInfos() {
        System.out.println("Duration of the simulation : " + profilingTimeStats.getDuration() + " seconds.");
        System.out.println("First timeSlot : " + timeConfig.getBeginTime());
        System.out.println("Final timeSlot : " + (timeConfig.getBeginTime() + timeConfig.getStepLength() * currentStep));
        System.out.println("Number of steps : " + currentStep);

        System.out.println("");

        if (roninCmdConfig.isPrintProfilingTime()) {
            profilingTimeStats.printProfilingTime(currentStep);
        }

    }

    /**
     * Computes the loads and the travel times for every step of the simulation.
     * If this simulation is monitored by a Ronin server, this method is not
     * used because all steps and actions will be triggered by client's queries.
     */
    public void work() {
        //while the simulation is not finished we process the next step.
        while (!processNextStep()) {
        }
        endSimulationWork();
    }

    /**
     * Does the work of end of simulation including writting the outputs of end
     * of simulation.
     */
    public void endSimulationWork() {
        double bTimeWriteOutputs = System.currentTimeMillis();
        writeEndSimulationOutputs();
        double eTimeWriteOutputs = System.currentTimeMillis();
        profilingTimeStats.setTimeWriteEndSimulationOutputs((eTimeWriteOutputs - bTimeWriteOutputs) / 1000);
    }

    /**
     * Process one step of the simulation if the simulation is not finished yet.
     * Returns true if the simulation is finished; false otherwise.
     *
     * @return true if the simulation is finished; false otherwise
     */
    public boolean processNextStep() {

        if (!isFinished()) {
            double timeSlot = timeConfig.getBeginTime() + currentStep * timeConfig.getStepLength();

            // a map of loads of vehicles per edge. Vehicles may be on several edges, we are making estimation.
            Map<String, List<Vehicle>> estimatedLoads = new HashMap<>();
            // a map that contains all the positions of each vehicle for this step. The key is the Edge, and value is the list of vehicles that were on this Edge during this timestep. Used for statistics for edges.
            Map<String, List<Vehicle>> positionsOfVehiclesForThisStep = null;
            if (roninCmdConfig.isGenerateEdgeData()) {
                positionsOfVehiclesForThisStep = new HashMap<>();
            }
            // a map of travel time per edge
            Map<String, Double> travelTimes = new HashMap<>();
            // the edges that are used during this step
            Set<Edge> edgesToConsider = new HashSet<>();
            // the edges that are overload during this step
            Map<String, Edge> overloadEdges = new HashMap<>();
            // the final map of loads of vehicles per edge for this step with the accurate positions is cleared for the new step
            currentStepFinalLoads.clear();

            // We remove and/or add vehicles to simulation. (used mainly if monitored by a Ronin Server).
            network.flushLoadedVehicles();
            network.flushVehiclesToRemoveFromSimulationList();
            network.updateDepartedVehiclesForCurrentTimeStep(timeSlot);

            // We do the algo
            double bTimeCompute = System.currentTimeMillis();
            computeLoads(estimatedLoads, overloadEdges, edgesToConsider);
            double eTimeCompute = System.currentTimeMillis();
            profilingTimeStats.increaseTimeComputationLoads((eTimeCompute - bTimeCompute) / 1000);

            double bTimePropagate = System.currentTimeMillis();
            propagateOverloads(estimatedLoads, travelTimes, overloadEdges, edgesToConsider);
            double eTimePropagate = System.currentTimeMillis();
            profilingTimeStats.increaseTimePropagateOverloads((eTimePropagate - bTimePropagate) / 1000);

            double bTimeRepositioning = System.currentTimeMillis();
            network.repositionRunningVehicles(currentStepFinalLoads, timeSlot, timeConfig.getStepLength(), travelTimes, roninCmdConfig.isGenerateEdgeData(), positionsOfVehiclesForThisStep);
            double eTimeRepositioning = System.currentTimeMillis();
            profilingTimeStats.increasesTimeRepositioning((eTimeRepositioning - bTimeRepositioning) / 1000);

            if (roninCmdConfig.isGenerateEdgeData()) {
                double bTimeStatistics = System.currentTimeMillis();
                computeStatistics(positionsOfVehiclesForThisStep, travelTimes, edgesToConsider);
                double eTimeStatistics = System.currentTimeMillis();
                profilingTimeStats.increaseTimeComputeStatistics((eTimeStatistics - bTimeStatistics) / 1000);
            }

            double bTimeWritingOutputs = System.currentTimeMillis();
            writeCurrentStepOutputs(currentStepFinalLoads, timeSlot);
            double eTimeWritingOutputs = System.currentTimeMillis();
            profilingTimeStats.increaseTimeWriteCurrentStepOutputs((eTimeWritingOutputs - bTimeWritingOutputs) / 1000);

            increaseCurrentStep();
            return false;
        }

        return true;
    }

    /**
     * Computes the loads of the edges. The vehicles are not moved but we
     * calculate their possible positions for this step.
     *
     * @param estimatedLoads a map of loads of vehicles per edge. Vehicles may
     * be on several edges, we are making estimation
     * @param overloadEdges the map of overload edges of this step
     * @param edgesToConsider the list of edges that are used during this step
     * of simulation
     */
    private void computeLoads(
            final Map<String, List<Vehicle>> estimatedLoads, final Map<String, Edge> overloadEdges, final Set<Edge> edgesToConsider) {

        Double timeSlot = timeConfig.getBeginTime() + currentStep * timeConfig.getStepLength();

        Iterator<Vehicle> runningIterator = network.getRunningVehicles().iterator();
        while (runningIterator.hasNext()) {
            Vehicle v = runningIterator.next();
            if (v.getDepartureTime() <= timeSlot) {
                double time = 0.0;
                int iCurrentPosition = v.getPosition();
                List<Edge> route = v.getRoute();

                //while we have enough time to move 
                //and while we are not arrived, we can move.
                while (time < timeConfig.getStepLength() && iCurrentPosition < route.size() - 1) {
                    Edge currentEdge = route.get(iCurrentPosition);
                    edgesToConsider.add(currentEdge);
                    MapUtils.addVehicleToVehiclesListMap(estimatedLoads, v, currentEdge.getId());
                    //we add the min travel time because we are making 
                    //probabilities about where the vehicle can be.
                    time += currentEdge.getMinTravelTime();

                    if (currentEdge.isOverloaded(estimatedLoads.get(currentEdge.getId()).size())) {
                        overloadEdges.put(currentEdge.getId(), currentEdge);
                    }

                    //if we have enough time to go through this edge, we go to the next edge
                    if (time < timeConfig.getStepLength()) {
                        iCurrentPosition += 1;
                    }
                }
            } else {
                //The list of running vehicles is supposed to be sorted by departure time
                //So if a vehicle is not arrived yet, the next are not arrived neither.
                break;
            }

        }

    }

    /**
     * Calculates the travel time for every Edge, and propagate the congestion
     * of overloaded edges to their predecessors.
     *
     * @param estimatedLoads a map of loads of vehicles per edge. Vehicles may
     * be on several edges, we are making estimation.
     * @param travelTimes the current map of travel time per edge for this step
     * @param overloadEdges the map of overload edges of this step
     * @param edgesToConsider the list of edges that are used during this step
     * of simulation
     */
    private void propagateOverloads(final Map< String, List<Vehicle>> estimatedLoads,
            final Map< String, Double> travelTimes, final Map<String, Edge> overloadEdges, Set<Edge> edgesToConsider) {

        //first we calculate the travel time for all the edges.
        edgesToConsider.stream().forEach((normalEdge) -> {
            double load = estimatedLoads.get(normalEdge.getId()).size();
            double travelTime = normalEdge.getTravelTime(load);
            travelTimes.put(normalEdge.getId(), travelTime);
        });

        //for all the edges that can lead to an overload Edge, 
        //their travel time is set to the maximum.
        overloadEdges.values().stream().forEach((e) -> {
            Map<String, Edge> ingoingEdges = e.getStartNode().getIngoingEdges();

            ingoingEdges.values().stream().forEach((impactedEdge) -> {
                travelTimes.put(impactedEdge.getId(), impactedEdge.getMaxTravelTime());
            });

        });

    }

    /**
     * Increases the values of attributes of objects like edges for statistical
     * outputs.
     *
     * @param positionsOfVehiclesForThisStep a map that contains all the
     * positions of each vehicle for this step. The key is the Edge, and value
     * is the list of vehicles that were on this Edge during this timestep.
     * @param travelTimes the current map of travel time per edge for this step
     * @param edgesToConsider the list of edges that are used during this step
     * of simulation
     */
    private void computeStatistics(final Map< String, List<Vehicle>> positionsOfVehiclesForThisStep,
            final Map< String, Double> travelTimes, Set<Edge> edgesToConsider) {

        edgesToConsider.parallelStream().forEach((edge) -> {
            int nbVehiclesForCurrentStepForEdge = 0;
            if (positionsOfVehiclesForThisStep.containsKey(edge.getId())) {
                nbVehiclesForCurrentStepForEdge = positionsOfVehiclesForThisStep.get(edge.getId()).size();
            }
            double travelTimeForCurrentStepForEdge = travelTimes.getOrDefault(edge.getId(), edge.getMinTravelTime());
            edge.increaseNbTotVehicles(nbVehiclesForCurrentStepForEdge);
            edge.increaseTravelTimeTotal(travelTimeForCurrentStepForEdge);
        });

    }

    /**
     * Writes the outputs of the finished current step.
     *
     * @param finalLoads the final map of loads of vehicles per edge for this
     * stpe with the accurate positions
     * @param timeSlot the timeslot of the step
     */
    public void writeCurrentStepOutputs(final Map< String, List<Vehicle>> finalLoads, double timeSlot) {
        if (roninCmdConfig.isGenerateLightLM()) {
            LightLoadsWriter llw = new LightLoadsWriter(globalOutputsDirectoryPath, "", finalLoads, timeSlot, currentStep);
            llw.writeOutputFile();
        }

        if (roninCmdConfig.isGenerateLM()) {
            LoadsWriter lw = new LoadsWriter(globalOutputsDirectoryPath, "", finalLoads, timeSlot, currentStep);
            lw.writeOutputFile();
        }

    }

    /**
     * Write the outputs of end of simulation.
     */
    private void writeEndSimulationOutputs() {
        if (roninCmdConfig.isGenerateEdgeData()) {
            double finalTimeSlot = timeConfig.getBeginTime() + timeConfig.getStepLength() * currentStep;
            EdgesWriter ew = new EdgesWriter(globalOutputsDirectoryPath, "", network.getEdges().values(), timeConfig.getBeginTime(), finalTimeSlot, currentStep);
            ew.writeOutputFile();
        }

        if (roninCmdConfig.isGenerateTripInfos()) {
            TripInfosWriter tw = new TripInfosWriter(globalOutputsDirectoryPath, "", timeConfig.getStepLength(), network.getArrivedVehicles());
            tw.writeOutputFile();
        }
    }

}
