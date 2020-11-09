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
package ie.ucd.pel.ronin.model;

import ie.ucd.pel.ronin.utils.MapUtils;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author Come CACHARD
 *
 * Model class that describes a traffic network that is composed of the road
 * graph and of the vehicles.
 */
public class Network {

    /**
     * The map of all vehicles in the simulation (loaded,not departed, running,
     * and arrived). The vehicles are mapped on their ids. This map is filled
     * with the vehicles that are loaded, we assume that vehicles follow their
     * regular lifecycle and start to be added in loaded vehicles list. Else
     * they won't be added to this map.
     */
    private final Map<String, Vehicle> vehiclesInSimulation;

    /**
     * The map of Vehicles that are currently running in our simulation (or
     * stuck in jam). The list of running vehicles is supposed to be sorted by
     * departure time.
     */
    private final List<Vehicle> runningVehicles;

    /**
     * The list of Vehicles that are arrived in our network.
     */
    private final List<Vehicle> arrivedVehicles;

    /**
     * The list of loaded Vehicles during the current time step to add to the
     * list of not departed vehicles. All vehicles that are added to the
     * simulation are first added to this list. The vehicles are removed from
     * this list at the beginning of each step.
     */
    private final List<Vehicle> loadedVehicles;

    /**
     * The list of not departed Vehicles of the simulation. They are waiting to
     * be added to running vehicles. The list of not departed vehicles is
     * supposed to be sorted by departure time.
     */
    private List<Vehicle> notDepartedVehicles;

    /**
     * The list of loaded Vehicles that were loaded at the current time step. It
     * is not sorted by departure time. The vehicles are removed from this list
     * at the beginning of each step.
     */
    private final List<Vehicle> currentStepLoadedVehicles;

    /**
     * The list of Vehicles that start their trip at the current time step of
     * the simulation.
     */
    private final List<Vehicle> currentStepDepartedVehicles;

    /**
     * The list of ids of Vehicles to remove from the simulation. The vehicles
     * are removed from this simulation and their id removed from this list at
     * the beginning of each step.
     */
    private final List<String> vehiclesToRemoveFromSimulation;

    /**
     * The map of VehicleType that are considered in our simulation. The key is
     * the VehicleType's id.
     */
    private final Map<String, VehicleType> vehicleTypes;

    /**
     * The network graph of the roads and the intersections.
     */
    private final Graph graph;

    /**
     * Constructs and initializes a Network with a new Graph, an empty map of
     * Vehicles and an empty map of VehiclesTypes.
     */
    public Network() {
        runningVehicles = new LinkedList<>();
        arrivedVehicles = new LinkedList<>();
        loadedVehicles = new LinkedList<>();
        notDepartedVehicles = new LinkedList<>();
        currentStepDepartedVehicles = new LinkedList<>();
        vehiclesToRemoveFromSimulation = new LinkedList<>();
        currentStepLoadedVehicles = new LinkedList<>();
        vehiclesInSimulation = new HashMap<>();
        vehicleTypes = new HashMap<>();
        graph = new Graph();
    }

    /**
     * Returns the graph of this Network.
     *
     * @return the graph of this Network
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * Returns a non modifiable map of Nodes that composed this Graph.
     *
     * @return a non modifiable map of Nodes that composed this Graph
     */
    public Map<String, Node> getNodes() {
        return graph.getNodes();
    }

    /**
     * Returns a non modifiable map of Edges that composed this Graph.
     *
     * @return a non modifiable map of Edges that composed this Graph
     */
    public Map<String, Edge> getEdges() {
        return graph.getEdges();
    }

    /**
     * Returns a non modifiable map of all the Vehicles (loaded, not departed,
     * running and arrived).
     *
     * @return a non modifiable map of all the Vehicles (loaded, not departed,
     * departed and arrived).
     */
    public Map<String, Vehicle> getAllVehiclesOfSimulation() {
        return Collections.unmodifiableMap(vehiclesInSimulation);
    }

    /**
     * Returns the Vehicle with a specific id from the map of all Vehicles that
     * are considered during our simulation (loaded, not departed,running and
     * arrived). Returns null if not found.
     *
     * @param idVehicle the id of the Vehicle we want to return
     * @return the Vehicle with a specific id. Returns null if not found.
     */
    public Vehicle getVehicle(String idVehicle) {
        if (idVehicle == null || idVehicle.isEmpty()) {
            return null;
        }

        return vehiclesInSimulation.get(idVehicle);
    }

    /**
     * Returns the Vehicle with a specific id from the list of not arrived
     * Vehicles.
     *
     * @param idVehicle the id of the Vehicle we want to return
     * @return the Vehicle with a specific id. Returns null if not found.
     */
    public Vehicle getVehicleFromRunningVehicles(String idVehicle) {
        if (idVehicle == null || idVehicle.isEmpty()) {
            return null;
        }
        for (Vehicle v : runningVehicles) {
            if (v.getId().equals(idVehicle)) {
                return v;
            }
        }
        return null;
    }

    /**
     * Returns the Vehicle with a specific id from the list of arrived Vehicles.
     *
     * @param idVehicle the id of the Vehicle we want to return
     * @return the Vehicle with a specific id. Returns null if not found.
     */
    public Vehicle getVehicleFromArrivedVehicles(String idVehicle) {
        if (idVehicle == null || idVehicle.isEmpty()) {
            return null;
        }
        for (Vehicle v : arrivedVehicles) {
            if (v.getId().equals(idVehicle)) {
                return v;
            }
        }
        return null;
    }

    /**
     * Returns the Vehicle with a specific id from the list of not departed
     * Vehicles.
     *
     * @param idVehicle the id of the Vehicle we want to return
     * @return the Vehicle with a specific id. Returns null if not found.
     */
    public Vehicle getVehicleFromNotDepartedVehicles(String idVehicle) {
        if (idVehicle == null || idVehicle.isEmpty()) {
            return null;
        }
        for (Vehicle v : notDepartedVehicles) {
            if (v.getId().equals(idVehicle)) {
                return v;
            }
        }
        return null;
    }

    /**
     * Returns the Vehicle with a specific id from the list of loaded Vehicles.
     *
     * @param idVehicle the id of the Vehicle we want to return
     * @return the Vehicle with a specific id. Returns null if not found.
     */
    public Vehicle getVehicleFromLoadedVehicles(String idVehicle) {
        if (idVehicle == null || idVehicle.isEmpty()) {
            return null;
        }
        for (Vehicle v : loadedVehicles) {
            if (v.getId().equals(idVehicle)) {
                return v;
            }
        }
        return null;
    }

    /**
     * Returns a non modifiable view of the list of ids of vehicles to remove
     * from the simulation.
     *
     * @return a non modifiable view of the list of ids of vehicles to remove
     * from the simulation.
     */
    public List<String> getVehiclesToRemoveFromSimulation() {
        return Collections.unmodifiableList(vehiclesToRemoveFromSimulation);
    }

    /**
     * Returns a non modifiable view of the list of loaded vehicles.
     *
     * @return a non modifiable view of the list of loaded vehicles.
     */
    public List<Vehicle> getLoadedVehicles() {
        return Collections.unmodifiableList(loadedVehicles);
    }

    /**
     * Returns a non modifiable view of the list of the vehicles that starts
     * their trip at the current time step of the simulation.
     *
     * @return a non modifiable view of the list of the vehicles that starts
     * their trip at the current time step of the simulation
     */
    public List<Vehicle> getCurrentStepDepartedVehicles() {
        return Collections.unmodifiableList(currentStepDepartedVehicles);
    }

    /**
     * Returns a non modifiable view of the list of the vehicles that were
     * loaded at the current time step of the simulation.
     *
     * @return a non modifiable view of the list of the vehicles that were
     * loaded at the current time step of the simulation
     */
    public List<Vehicle> getCurrentStepLoadedVehicles() {
        return Collections.unmodifiableList(currentStepLoadedVehicles);
    }

    /**
     * Returns a non modifiable view of the list of vehicles to add to the
     * simulation.
     *
     * @return a non modifiable view of the list of vehicles to add to the
     * simulation.
     */
    public List<Vehicle> getNotDepartedVehicles() {
        return Collections.unmodifiableList(notDepartedVehicles);
    }

    /**
     * Returns a non modifiable view of the list of running vehicles. The list
     * of running vehicles is supposed to be sorted by departure time.
     *
     * @return a non modifiable view of the list of running vehicles that is
     * supposed to be sorted by departure time.
     */
    public List<Vehicle> getRunningVehicles() {
        return Collections.unmodifiableList(runningVehicles);
    }

    /**
     * Returns a non modifiable view of the list of arrived vehicles.
     *
     * @return a non modifiable view of the list of arrived vehicles
     */
    public List<Vehicle> getArrivedVehicles() {
        return Collections.unmodifiableList(arrivedVehicles);
    }

    /**
     * Returns a non modifiable map of VehicleTypes of this Network.
     *
     * @return a non modifiable map of VehicleTypes of this Network
     */
    public Map<String, VehicleType> getVehicleTypes() {
        return Collections.unmodifiableMap(vehicleTypes);
    }

    /**
     * Returns the VehicleType to which the specified key is mapped, or null if
     * this Network contains no mapping for the key.
     *
     * @param idVehicleType the id whose associated VehicleType is to be
     * returned
     * @return the Vehicle associated to the given id or null if there is no
     * matching VehicleType
     */
    public VehicleType getVehicleType(String idVehicleType) {
        return vehicleTypes.get(idVehicleType);
    }

    /**
     * Returns the Edge to which the specified key is mapped, or null if the
     * Graph of this Network contains no mapping for the key.
     *
     * @param idEdge the id whose associated Edge is to be returned
     * @return the Edge associated to the given id
     */
    public Edge getEdge(String idEdge) {
        return graph.getEdge(idEdge);
    }

    /**
     * Returns the Node to which the specified key is mapped, or null if the
     * Graph of this Network contains no mapping for the key.
     *
     * @param idNode the id whose associated Node is to be returned
     * @return the Node associated to the given id
     */
    public Node getNode(String idNode) {
        return graph.getNode(idNode);
    }

    /**
     * Returns the map configuration of the network.
     *
     * @return the map configuration of the network
     */
    public NetworkMapConfiguration getNetworkMapConfiguration() {
        return graph.getMapConfig();
    }

    /**
     * Adds a Vehicle to the list of arrived Vehicles of this Network. If the
     * Vehicle is already in, we do nothing.
     *
     * @param v the Vehicle we want to add
     * @return false if the Vehicle is null and not added to the map, true else
     */
    public boolean addVehicleToArrivedVehicles(Vehicle v) {
        if (v == null) {
            return false;
        }
        //we update the number of arrived vehicles of the final edge of the 
        //arrived vehicle
        if (!v.getRoute().isEmpty()) {
            Edge finalEdge = v.getEdgeOfRouteAtPosition(v.getRoute().size() - 1);
            finalEdge.increaseArrivedVehicles(1);
        }

        arrivedVehicles.add(v);
        return true;
    }

    /**
     * Adds a Vehicle to the list of running Vehicles of this Network. If the
     * Vehicle is already in, we do nothing.
     *
     * @param v the Vehicle we want to add
     * @return false if the Vehicle is null and not added to the map, true else
     */
    public boolean addVehicleToRunningVehicles(Vehicle v) {
        if (v == null) {
            return false;
        }
        runningVehicles.add(v);
        return true;
    }

    /**
     * Adds a Vehicle to the list of not departed Vehicles of this Network. If
     * the Vehicle is already in, we do nothing.
     *
     * @param v the Vehicle we want to add
     * @return false if the Vehicle is null and not added to the map, true else
     */
    public boolean addVehicleToNotDepartedVehicles(Vehicle v) {
        if (v == null) {
            return false;
        }
        notDepartedVehicles.add(v);
        return true;
    }

    /**
     * Adds a Vehicle to the list of loaded Vehicles. The Vehicle will be add to
     * the list of not arrived Vehicles at the beginning of next step. We add
     * the vehicle to the list of vehicles considered in the simulation at same
     * time. If the vehicle is already in the simulation we do not add it again
     * but we return true.
     *
     * @param v the Vehicle to add
     * @return false if the Vehicle is null and not added to the list, true else
     */
    public boolean addVehicleToLoadedVehicles(Vehicle v) {
        if (v == null || v.getRoute().isEmpty()) {
            return false;
        }
        if (!vehiclesInSimulation.containsKey(v.getId())) {
            loadedVehicles.add(v);
            vehiclesInSimulation.put(v.getId(), v);
        }
        return true;
    }

    /**
     * Adds a Vehicle id to the list of ids of Vehicles to remove from the
     * Simulation. The Vehicle will be add to the list of not arrived Vehicles
     * at the beginning of next step.
     *
     * @param idVehicle the id of the Vehicle to remove from the simulation
     * @return false if the id of Vehicle is empty or null and not added to the
     * list, true else
     */
    public boolean addVehicleToRemoveFromSimulationList(String idVehicle) {
        if (idVehicle == null || idVehicle.isEmpty()) {
            return false;
        }
        vehiclesToRemoveFromSimulation.add(idVehicle);

        return true;
    }

    /**
     * Adds a VehicleType to the map of VehicleTypes of this Network. If the
     * VehicleType is already in, we do nothing.
     *
     * @param vType the VehicleType we want to add
     * @return false if the VehicleType is null and not added to the map, true
     * else
     */
    public boolean addVehicleType(VehicleType vType) {
        if (vType == null) {
            return false;
        }
        vehicleTypes.putIfAbsent(vType.getId(), vType);
        return true;
    }

    /**
     * Adds a Node to the map of Nodes of this Network's Graph. If the Node is
     * already in the map, we do nothing
     *
     * @param n the Node we want to add
     * @return false if the node was null and not added to the Nodes map, true
     * else
     */
    public boolean addNode(Node n) {
        return graph.addNode(n);
    }

    /**
     * Adds an Edge to the map of Edges of this Network's Graph. If the Edge is
     * already in the map, we do nothing.
     *
     * @param e the Edge we want to add.
     * @return false if the edge was null and not added to the Edges map, true
     * else
     */
    public boolean addEdge(Edge e) {
        return graph.addEdge(e);
    }

    /**
     * Flushes the list of loaded Vehicles by adding them to the list of not
     * departed vehicles.
     */
    public void flushLoadedVehicles() {
        boolean areVehiclesFlushed = !loadedVehicles.isEmpty();
        currentStepLoadedVehicles.clear();

        Iterator<Vehicle> iter = loadedVehicles.iterator();
        while (iter.hasNext()) {
            Vehicle v = iter.next();
            notDepartedVehicles.add(v);
            currentStepLoadedVehicles.add(v);
            iter.remove();
        }

        // /!\ IMPORTANT !!! The list of not departed vehicles must always be sorted by departure time !
        // Otherwise, the algorithm won't work.
        if (areVehiclesFlushed) {
            sortNotDepartedVehiclesByDepartureTime();
        }

    }

    /**
     * Flushes the list of ids of Vehicles to remove from the simulation by
     * removing the vehicles from the network.
     */
    public void flushVehiclesToRemoveFromSimulationList() {
        Iterator<String> iter = vehiclesToRemoveFromSimulation.iterator();
        while (iter.hasNext()) {
            String idVehicle = iter.next();
            removeVehicle(idVehicle);
            iter.remove();
        }
    }

    /**
     * Looks over the not departed vehicles list in order to add the vehicles
     * that can depart to the list of departed vehicles (and remove them from
     * not separted vehicles) for a specific time step.
     *
     * @param timeSlot the time slot of the step to process in seconds.
     */
    public void updateDepartedVehiclesForCurrentTimeStep(Double timeSlot) {
        currentStepDepartedVehicles.clear();

        Iterator<Vehicle> iter = notDepartedVehicles.iterator();
        while (iter.hasNext()) {
            Vehicle v = iter.next();
            // WARNING !!! The list of not departed Vehicles is supposed to be sorted by departure time.
            if (v.getDepartureTime() > timeSlot) {
                return;
            }
            runningVehicles.add(v);
            currentStepDepartedVehicles.add(v);
            iter.remove();
        }
    }

    /**
     * Returns true if all vehicles are arrived; false otherwise.
     *
     * @return true if all vehicles are arrived; false otherwise
     */
    public boolean areAllVehiclesArrived() {
        return loadedVehicles.isEmpty() && notDepartedVehicles.isEmpty() && runningVehicles.isEmpty();
    }

    /**
     * Removes a Vehicle from a list of Vehicles and from the list of considered
     * vehicles in the simulation.
     *
     * @param idVehicle the id of the Vehicle to remove
     * @param collection the collection where we want to remove the vehicle
     * @return false if the id is null or if the vehicle was not removed; true
     * otherwise
     */
    private boolean removeVehicleFromList(String idVehicle, List<Vehicle> collection) {
        if (idVehicle == null) {
            return false;
        }
        if (idVehicle.isEmpty()) {
            return true;
        }
        vehiclesInSimulation.remove(idVehicle);

        Iterator<Vehicle> iter = collection.iterator();
        while (iter.hasNext()) {
            Vehicle v = iter.next();
            if (v.getId().equals(idVehicle)) {
                iter.remove();
                return true;
            }
        }

        return false;
    }

    /**
     * Removes a Vehicle from the Network (from loaded, not departed, running,
     * arrived Vehicles).
     *
     * @param idVehicle the id of the Vehicle to remove
     */
    public void removeVehicle(String idVehicle) {
        if (idVehicle == null || idVehicle.isEmpty()) {
            return;
        }

        if (removeVehicleFromList(idVehicle, arrivedVehicles)) {
        } else if (removeVehicleFromList(idVehicle, runningVehicles)) {
        } else if (removeVehicleFromList(idVehicle, notDepartedVehicles)) {
        } else if (removeVehicleFromList(idVehicle, loadedVehicles)) {
        }

    }

    /**
     * Sorts the list of running vehicles by their departure time in ASC order.
     */
    public void sortNotDepartedVehiclesByDepartureTime() {
        notDepartedVehicles = notDepartedVehicles.stream().sorted((v1, v2) -> {
            return v1.getDepartureTime().compareTo(v2.getDepartureTime());
        }).collect(Collectors.toList());
    }

    /**
     * Calculates and repositions the running vehicles according to the travel
     * times per edge.
     *
     * @param finalLoads the final map of loads of vehicles per edge for this
     * step with the accurate positions
     * @param timeSlot the time slot of the step to process in seconds.
     * @param timeStepLength the duration in seconds of a time slot
     * @param travelTimes the current map of travel time per edge for this step
     * @param isGenerateEdgeData if true, we want to compute statistics for edge
     * data output with filling positionsOfVehiclesForThisStep
     * @param positionsOfVehiclesForThisStep a map that contains all the
     * positions of each vehicle for this step. The key is the Edge, and value
     * is the list of vehicles that were on this Edge during this timestep.
     */
    public void repositionRunningVehicles(final Map< String, List<Vehicle>> finalLoads,
            final double timeSlot, final double timeStepLength, final Map< String, Double> travelTimes,
            final boolean isGenerateEdgeData, final Map<String, List<Vehicle>> positionsOfVehiclesForThisStep) {

        Iterator<Vehicle> runningIterator = runningVehicles.iterator();
        while (runningIterator.hasNext()) {
            Vehicle v = runningIterator.next();

            if (v.getDepartureTime() <= timeSlot) {
                int iCurrentPosition = v.getPosition();
                List<Edge> route = v.getRoute();

                if (v.getNbSlotsInSamePosition() == 0) {
                    double time = 0;
                    while (time < timeStepLength && iCurrentPosition < route.size() - 1) {
                        Edge currentEdge = route.get(iCurrentPosition);
                        time += travelTimes.get(currentEdge.getId());

                        //if we have enough time to go through this edge, we go to the next edge
                        if (time < timeStepLength) {
                            iCurrentPosition += 1;
                            if (isGenerateEdgeData) {
                                MapUtils.addVehicleToVehiclesListMap(positionsOfVehiclesForThisStep, v, currentEdge.getId());
                            }
                        }
                    }

                    //if the iCurrentPosition is the arrival
                    if (iCurrentPosition >= route.size() - 1) {
                        addVehicleToArrivedVehicles(v);
                        runningIterator.remove();

                        if (iCurrentPosition > route.size() - 1) {
                            iCurrentPosition = route.size() - 1;
                        }

                    } else if (iCurrentPosition == v.getPosition()) {
                        // if we are still on the same edge, how much time steps will we have to wait before reaching the next one?
                        Edge currentEdge = route.get(iCurrentPosition);
                        try {
                            int nbSlotsInSamePosition = (int) (travelTimes.get(currentEdge.getId()) / timeStepLength);
                            v.setNbSlotsInSamePosition(nbSlotsInSamePosition);
                        } catch (Exception e) {
                            System.out.println("null error on edge :" + currentEdge.getId());
                            System.out.println("traveltimes : " + travelTimes.get(currentEdge.getId()));
                            System.out.println("vehicle :" + v.getId());
                            System.out.println("iCurrentPosition :" + iCurrentPosition);
                            System.out.println("timeSlot :" + timeSlot);
                            System.out.println("");
                        }
                    }

                    v.setPosition(iCurrentPosition);

                } else {
                    v.decreaseNbSlotsInSamePosition(1);
                    if (v.getNbSlotsInSamePosition() == 0) {
                        v.increasePosition(1);
                        if (v.isArrived()) {
                            addVehicleToArrivedVehicles(v);
                            runningIterator.remove();
                        }
                    }
                    if (isGenerateEdgeData) {
                        MapUtils.addVehicleToVehiclesListMap(positionsOfVehiclesForThisStep, v, v.getCurrentEdge().getId());
                    }
                }

                v.increaseTravelTime(timeStepLength);
                MapUtils.addVehicleToVehiclesListMap(finalLoads, v, v.getCurrentEdge().getId());

            } else {
                //The list of running vehicles is supposed to be sorted by departure time
                //So if a vehicle is not arrived yet, the next are not arrived neither.
                break;
            }

        }

    }

}
