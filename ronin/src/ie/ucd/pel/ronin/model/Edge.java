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

import java.io.Serializable;
import java.util.Objects;
import javafx.scene.paint.Color;

/**
 *
 * @author Come CACHARD
 *
 * Model class that describes an unidirectional arc between two nodes.
 */
public class Edge implements Serializable {

    /**
     * The id of this Edge.
     */
    private final String id;

    /**
     * The number of Vehicles that can be on this Edge at the same time.
     */
    private final int capacity;

    /**
     * The length of this Edge in meter.
     */
    private final Double length;

    /**
     * The maximum speed that is allowed for Vehicles in m/s.
     */
    private Double speedLimit;

    /**
     * The priority of the road represented by this Edge. The highest is the
     * priority, the more important is the Edge.
     */
    private final int priority;

    /**
     * The origin node from where starts this Edge.
     */
    private transient final Node startNode;

    /**
     * The destination node to where ends this edge.
     */
    private transient final Node endNode;

    /**
     * The free-flow travel-time in seconds of this Edge as defined by the
     * Bureau of Public Roads (BPR).
     */
    private final double fftv;

    /**
     * Constant parameter used to evaluate the travel time of an edge according
     * to the formula given by the Bureau of Public Roads (BPR).
     */
    private final double alpha;

    /**
     * Constant parameter used to evaluate the travel time of an edge according
     * to the formula given by the Bureau of Public Roads (BPR).
     */
    private final double beta;

    /**
     * The sum of all travel times in seconds per step for this Edge for all the
     * simulation. It is used to do some statistics measures. To get the real
     * travel time total, use the getter because this attribute does not
     * consider the travel times of steps where this Edge was not used.
     */
    private double tempTravelTimeTotal;

    /**
     * A counter that counts how many times we increase the travel time total,
     * so we can calculate the real travel time total by adding for each step
     * where we didn't incrrease the travel time the min travel time because
     * there was no vehicle at that times.
     */
    private int counterIncreaseTravelTimeTotal;

    /**
     * The sum of all the number of vehicles per step that were on this Edge for
     * all the simulation. It is used to do some statistics.
     */
    private double nbTotVehicles;

    /**
     * The number of vehicles that end their trip at this Edge.
     */
    private double arrivedVehicles;

    /**
     * Constructs and initializes an Edge with the specified properties.
     *
     * @param id the id of the newly constructed Edge
     * @param capacity the car capacity of the newly constructed Edge
     * @param length the length in meter of the newly constructed Edge
     * @param speedLimit the speed limit in m/s of the newly constructed Edge
     * @param priority the priority of the road represented by this Edge
     * @param startNode the origin Node of the newly constructed Edge
     * @param endNode the destination Node of the newly constructed Edge
     */
    public Edge(String id, int capacity, Double length, Double speedLimit, int priority, Node startNode, Node endNode) {
        this.id = id;
        this.capacity = capacity;
        this.length = length;
        this.speedLimit = speedLimit;
        this.priority = priority;
        this.startNode = startNode;
        this.endNode = endNode;
        this.alpha = 0.15;
        this.beta = 4.0;
        this.fftv = length / speedLimit;
        this.tempTravelTimeTotal = 0.0;
        this.nbTotVehicles = 0.0;
        this.arrivedVehicles = 0.0;
    }

    /**
     * Returns the id of this Edge.
     *
     * @return the id of this Edge
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the car capacity of this Edge.
     *
     * @return the car capacity of this Edge
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Returns the length in meter of this Edge.
     *
     * @return the length in meter of this Edge
     */
    public Double getLength() {
        return length;
    }

    /**
     * Returns the speed limit in m/s of this Edge.
     *
     * @return the speed limit in m/s of this Edge.
     */
    public Double getSpeedLimit() {
        return speedLimit;
    }

    /**
     * Returns the road priority of this Edge.
     *
     * @return the road priority of this Edge
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Returns the origin node of this Edge.
     *
     * @return the origin node of this Edge
     */
    public Node getStartNode() {
        return startNode;
    }

    /**
     * Sets the destination Node of this Edge.
     *
     * @return the destination Node of this Edge
     */
    public Node getEndNode() {
        return endNode;
    }

    /**
     * Returns the parameter alpha of this Edge.
     *
     * @return the parameter alpha of this Edge
     */
    public double getAlpha() {
        return alpha;
    }

    /**
     * Returns the parameter beta of this Edge.
     *
     * @return the parameter beta of this Edge
     */
    public double getBeta() {
        return beta;
    }

    /**
     * Returns the sum of all travel times per step for this Edge for all the
     * simulation in seconds.
     *
     * @param nbSteps the number of steps of the simulation
     * @return the sum of all travel times per step for this Edge for all the
     * simulation in seconds.
     */
    public double getTravelTimeTotal(double nbSteps) {
        //the total travel time is the sum of travel times when there were vehicles on this edge
        // plus min travel time for each step where there was no vehicles on this edge.
        return tempTravelTimeTotal + (nbSteps - counterIncreaseTravelTimeTotal) * getMinTravelTime();
    }

    /**
     * Returns the sum of all the number of vehicles per step that were on this
     * Edge for all the simulation.
     *
     * @return the sum of all the number of vehicles per step that were on this
     * Edge for all the simulation
     */
    public double getNbTotVehicles() {
        return nbTotVehicles;
    }

    /**
     * Returns the number of vehicles that end their trip at this Edge.
     *
     * @return the number of vehicles that end their trip at this Edge
     */
    public double getArrivedVehicles() {
        return arrivedVehicles;
    }

    /**
     * Returns the travel time in seconds of this Edge. It is the time needed by
     * a Vehicle to go through this Edge. It is calculated with the formula
     * given by the Bureau of Public Roads (BPR) and the load on this Edge.
     *
     * @param load the number of Vehicles to be on this Edge. It must be
     * positive.
     * @return the travel time in seconds of this Edge.
     */
    public double getTravelTime(double load) {
        if (load < 0) {
            throw new IllegalArgumentException("The load used to evaluate if an Edge is overloaded must be positive.");
        }

        if (load >= this.capacity) {
            return getMaxTravelTime();
        } else {
            return this.fftv * (1 + this.alpha * Math.pow(load / this.capacity, this.beta));
        }
    }

    /**
     * Returns the minimum travel time in seconds of this Edge that is the
     * free-flow travel-time of this Edge. It is the shortest time needed by a
     * Vehicle to go through this Edge. It is calculated with the formula given
     * by the Bureau of Public Roads (BPR).
     *
     * @return the minimum travel time in seconds of this Edge that is the
     * free-flow travel-time of this Edge.
     */
    public double getMinTravelTime() {
        if (this.capacity == 0) {
            //So let's go through this Edge because it means that it is a very short edge
            return 0.0;
        }
        return this.fftv;
    }

    /**
     * Returns the maximum travel time in seconds of this Edge. It is the
     * maximum time needed by a Vehicle to go through this Edge. It is
     * calculated with the formula given by the Bureau of Public Roads (BPR).
     *
     * @return the maximum travel time in seconds of this Edge.
     */
    public double getMaxTravelTime() {
        if (this.capacity == 0) {
            //So let's go through this Edge because it means that it is a very short edge
            return 0.0;
        }
        return this.fftv * (1 + this.alpha);
    }

    /**
     * Returns the mean density of this Edge for this simulation in
     * nbVehicle/Km.
     *
     * @param nbSteps the nunber of steps of the simulation.
     * @return the mean density of this Edge for this simulation in nbVehicle/Km
     */
    public double getMeanDensity(double nbSteps) {
        if (this.capacity == 0) {
            return 0.0;
        }
        if (nbSteps == 0 || length == 0) {
            return 0.0;
        }
        // the average number of vehicles per step divided by the length of this Edge in Km.
        return (nbTotVehicles / nbSteps) / (length / 1000);
    }

    /**
     * Returns the mean of all travel times per step for this Edge for all the
     * simulation in seconds.
     *
     * @param nbSteps the number of steps of the simulation.
     * @return the mean of all travel times per step for this Edge for all the
     * simulation in seconds.
     */
    public double getMeanTravelTime(double nbSteps) {
        if (nbSteps == 0) {
            return 0.0;
        }
        return getTravelTimeTotal(nbSteps) / nbSteps;
    }

    /**
     * Returns the mean speed of vehicles on this Edge for all the simulation in
     * m/s.
     *
     * @param nbSteps the nunber of steps of the simulation.
     * @return the mean speed of vehicles on this Edge for all the simulation in
     * m/s
     */
    public double getMeanSpeed(double nbSteps) {
        double meanTravelTime = getMeanTravelTime(nbSteps);
        if (meanTravelTime == 0) {
            return 0.0;
        }
        return length / meanTravelTime;
    }

    /**
     * Returns the average traffic volume in nbVehicle/hour of this Edge for the
     * simulation.
     *
     * @param nbSteps the number of steps of the simulation.
     * @return the average traffic volume in nbVehicle/hour of this Edge for the
     * simulation
     */
    public double getAverageTrafficVolume(double nbSteps) {
        // mean density in nbVeh/Km 
        // mean speed in m/s
        return getMeanDensity(nbSteps) * 3.6 * getMeanSpeed(nbSteps);
    }

    /**
     * Returns a color between green and red according to the number of vehicles
     * on this Edge regarding its capacity. Red is jam and green is very fluent.
     *
     * @param loads the number of vehicles on this Edge.
     * @return a color between green and red according to the number of vehicles
     * on this Edge regarding its capacity.
     */
    public Color getColor(int loads) {
        if (capacity == 0 || loads == 0) {
            return Color.LIGHTGRAY;
        }
        if (loads < capacity * 0.2) {
            return Color.GREEN;
        }
        if (loads >= capacity * 0.2 && loads < capacity * 0.4) {
            return Color.YELLOW;
        }
        if (loads >= capacity * 0.4 && loads < capacity * 0.6) {
            return Color.ORANGE;
        }
        if (loads >= capacity * 0.6 && loads < capacity * 0.8) {
            return Color.ORANGERED;
        }
        return Color.RED.interpolate(Color.DARKRED, (double) (loads) / (double) (capacity));
    }

    /**
     * Sets a new value for the speed limit in m/s of this Edge.
     *
     * @param speedLimit the new speed limit in m/s of this Edge
     */
    public void setSpeedLimit(double speedLimit) {
        if (speedLimit < 0) {
            this.speedLimit = 0.;
        } else {
            this.speedLimit = speedLimit;
        }
    }

    /**
     * Increases the number total of vehicles that were on this Edge during the
     * simulation by the number of vehicles on this Edge during a step.
     *
     * @param number the number of vehicles that were on this Edge during a step
     * of the simulation
     */
    public void increaseNbTotVehicles(double number) {
        if (number < 0) {
            throw new IllegalArgumentException("The number of vehicles to add to an Edge must be positive.");
        }
        nbTotVehicles += number;
    }

    /**
     * Increases the sum of all travel times of this Edge by a travel time in
     * seconds. By the same time we increase the counter of how much time we
     * increase the travel time total in order to complete the calcul of travel
     * time total.
     *
     * @param travelTime the travel time in seconds of this Edge during a step
     * of the simulation
     */
    public void increaseTravelTimeTotal(double travelTime) {
        if (travelTime < 0) {
            throw new IllegalArgumentException("The travel time of an Edge must be positive.");
        }
        if (Double.isNaN(travelTime)) {
            throw new IllegalArgumentException("The given travel time for Edge " + this.getId() + " is NaN.");
        }

        tempTravelTimeTotal += travelTime;
        counterIncreaseTravelTimeTotal++;
    }

    /**
     * Increases the number of vehicles that end their trip on this Edge.
     *
     * @param number a number of vehicles that end their trip on this Edge
     */
    public void increaseArrivedVehicles(double number) {
        if (number < 0) {
            throw new IllegalArgumentException("The number of arrived vehicles to add to an Edge must be positive.");
        }
        arrivedVehicles += number;
    }

    /**
     * Evaluates if this Edge will be overloaded with the given load.
     *
     * @param load the number of Vehicles to be on this Edge. It must be
     * positive.
     * @return true if the load is superior to the car capacity of this Edge;
     * false otherwise.
     */
    public boolean isOverloaded(int load) {
        if (load < 0) {
            throw new IllegalArgumentException("The load used to evaluate if an Edge is overloaded must be positive.");
        }
        return load > this.capacity;
    }

    /**
     * Returns the hashcode for this instance of Edge.
     *
     * @return the hascode for this Edge.
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.id);
        return hash;
    }

    /**
     * Determines wether or not two Edges are equal. Two instances of Edge are
     * equal if they have the same id.
     *
     * @param obj an object to be compared with this Edge.
     * @return true if the object to be compared is an instance of Edge and has
     * the same id; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Edge other = (Edge) obj;
        return Objects.equals(this.id, other.id);
    }

}
