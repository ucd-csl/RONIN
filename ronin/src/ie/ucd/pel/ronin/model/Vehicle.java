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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Come CACHARD
 *
 * Model class that describes a vehicle that travels through Edges. A Vehicle
 * has a lifcycle that is the following : * loaded (the vehicle is just brand
 * new created and added to the simulation) * not departed (the vehicle is
 * waiting to start its trip) * running (the vehicle has started its trip and is
 * not arrived) * arrived (the vehicle has reached its destination)
 */
public class Vehicle implements Serializable {

    /**
     * The id of this Vehicle.
     */
    private final String id;

    /**
     * The id of the type of this Vehicle.
     */
    private final String vType;

    /**
     * The time of the trip of this Vehicle from its origin to its destination
     * in seconds.
     */
    private Double travelTime;

    /**
     * The timestamp in seconds when the Vehicle starts its trip.
     */
    private final Double departureTime;

    /**
     * The maximum speed this Vehicle can drive in m/s.
     */
    private final Double maxSpeed;

    /**
     * The length of this Vehicle in meter.
     */
    private final Double length;

    /**
     * The list of Edges this Vehicle has to go through to join its destination
     * from its origin.
     */
    private final List<Edge> route;

    /**
     * The position of this Vehicle on its route.
     */
    private int position;

    /**
     * The total number of timeSlots this Vehicle has to stay on the current
     * edge before going to next Edge.
     */
    private int nbTotSlotsInSamePositionForCurrentEdge;

    /**
     * The number of timeSlots this Vehicle has to stay on its current position
     * before going to next Edge. While the nbSlotsInSamePosition is not zero,
     * the vehicle will not be allow to move to the next position.
     */
    private int nbSlotsInSamePosition;

    /**
     * The number total of steps where this vehicle has to stay in the same
     * position during its trip.
     */
    private int nbTotSlotsInSamePosition;

    /**
     * The length of the route in meter.
     */
    private double routeLength;

    /**
     * Constructs and initializes a Vehicle with the specified properties.
     *
     * @param id the id of the newly constructed Vehicle
     * @param departureTime the time the car starts its trip in seconds
     * @param maxSpeed the maximum speed in m/s of the newly constructed Vehicle
     * @param length the length in meter of the newly constructed Vehicle
     */
    public Vehicle(String id, Double departureTime, Double maxSpeed, Double length) {
        this.id = id;
        this.travelTime = 0.0;
        this.departureTime = departureTime;
        this.maxSpeed = maxSpeed;
        this.length = length;
        this.position = 0;
        this.nbSlotsInSamePosition = 0;
        this.nbTotSlotsInSamePositionForCurrentEdge = 0;
        this.nbTotSlotsInSamePosition = 0;
        this.vType = "";
        this.route = new ArrayList<>();
        this.routeLength = 0;
    }

    /**
     * Constructs and initializes a Vehicle with the specified properties
     * including with VehicleType properties.
     *
     * @param id the id of the newly constructed Vehicle
     * @param departureTime the time the car starts its trip in seconds
     * @param vType the type of this Vehicle
     */
    public Vehicle(String id, Double departureTime, VehicleType vType) {
        this.id = id;
        this.travelTime = 0.0;
        this.departureTime = departureTime;
        this.maxSpeed = vType.getMaxSpeed();
        this.length = vType.getLength();
        this.position = 0;
        this.nbSlotsInSamePosition = 0;
        this.nbTotSlotsInSamePositionForCurrentEdge = 0;
        this.nbTotSlotsInSamePosition = 0;
        this.vType = vType.getId();
        this.route = new ArrayList<>();
        this.routeLength = 0;
    }

    /**
     * Returns the id of this Vehicle.
     *
     * @return the id of this Vehicle
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the traveling time in seconds of this Vehicle.
     *
     * @param timeUnit the duration in seconds of a step of the simulation
     * @return the traveling time in seconds of this Vehicle
     */
    public Double getTravelTime(double timeUnit) {
        return travelTime;
    }

    /**
     * Returns the departure time in seconds of this Vehicle.
     *
     * @return the departure time in seconds of this Vehicle
     */
    public Double getDepartureTime() {
        return departureTime;
    }

    /**
     * Returns the arrival time of this Vehicle in seconds; -1 if it is not
     * arrived yet.
     *
     * @param timeUnit the duration in seconds of a step of the simulation
     * @return the arrival time of this Vehicle in seconds; -1 if it is not
     * arrived yet
     */
    public Double getArrivalTime(double timeUnit) {
        if (!isArrived()) {
            return -1.;
        }
        return departureTime + getTravelTime(timeUnit);
    }

    /**
     * Returns the maximum speed in m/s this Vehicle can drive.
     *
     * @return the maximum speed in m/s this Vehicle can drive
     */
    public Double getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * Returns the speed in m/s of this Vehicle for the current step.
     *
     * @param timeStepLength the duration in second of a time step
     * @return the speed in m/s of this Vehicle for the current step
     */
    public double getCurrentStepSpeed(double timeStepLength) {
        if (nbSlotsInSamePosition == 0) {
            return getCurrentEdge().getSpeedLimit();
        }
        double timeToGoToNextEdge = (nbTotSlotsInSamePositionForCurrentEdge) * timeStepLength;
        double speed = getCurrentEdge().getLength() / timeToGoToNextEdge;
        return speed;
    }

    /**
     * Returns the current position of this Vehicle in meter on the current Edge
     *
     * @param timeStepLength the duration in second of a time step
     * @return the current position of this Vehicle in meter on the current Edge
     */
    public double getPositionOnCurrentEdge(double timeStepLength) {
        if (nbSlotsInSamePosition == 0) {
            return 0;
        } else {
            double timeToGoToNextEdge = (nbTotSlotsInSamePositionForCurrentEdge) * timeStepLength;
            double factor = ((nbTotSlotsInSamePositionForCurrentEdge - nbSlotsInSamePosition) / nbTotSlotsInSamePositionForCurrentEdge);
            double positionOnCurrentEdge = getCurrentEdge().getLength() * factor;
            return positionOnCurrentEdge;
        }
    }

    /**
     * Returns the length of this Vehicle in meter.
     *
     * @return the length of this Vehicle in meter
     */
    public Double getLength() {
        return length;
    }

    /**
     * Returns the length of the route of this Vehicle in meter.
     *
     * @return the length of the route of this Vehicle in meter
     */
    public Double getRouteLength() {
        return routeLength;
    }

    /**
     * Returns the vehicle type's id of this Vehicle.
     *
     * @return the vehicle type's id of this Vehicle
     */
    public String getvType() {
        return vType;
    }

    /**
     * Returns the non modifiable route of this Vehicle that is a list of Edges.
     *
     * @return the non modifiable route of this Vehicle that is a list of Edges
     */
    public List<Edge> getRoute() {
        return Collections.unmodifiableList(route);
    }

    /**
     * Returns the Edge at the index position of the route of this Vehicle.
     *
     * @param position the index of the wanted edge in the route of this Vehicle
     * @return the Edge at the index position of the route of this Vehicle
     */
    public Edge getEdgeOfRouteAtPosition(int position) {
        return route.get(position);
    }

    /**
     * Returns the current edge where this Vehicle is located.
     *
     * @return the current edge where this Vehicle is located
     */
    public Edge getCurrentEdge() {
        if (position < 0) {
            System.out.println("position : " + position);
            System.out.println("idVeh : " + id);
        }
        if (position >= this.route.size()) {
            System.out.println("position : " + position);
            System.out.println("idVeh : " + id);
            return route.get(route.size() - 1);
        }
        return route.get(position);
    }

    /**
     * Returns the position of this Vehicle in its route.
     *
     * @return the position of this Vehicle in its route
     */
    public int getPosition() {
        return position;
    }

    /**
     * Returns the number total of steps where this Vehicle has to stay on the
     * same position during its trip.
     *
     * @return the number total of steps where this Vehicle has to stay on the
     * same position during its trip
     */
    public int getNbTotSlotsInSamePosition() {
        return nbTotSlotsInSamePosition;
    }

    /**
     * Returns the number of time slots this Vehicle has to stay on the same
     * position.
     *
     * @return the number of time slots this Vehicle has to stay on the same
     * position.
     */
    public int getNbSlotsInSamePosition() {
        return nbSlotsInSamePosition;
    }

    /**
     * Sets the new position of this Vehicle. A vehicle cannot move back on
     * previous positions so the new position must be greater than the current
     * position of this Vehicle.
     *
     * @param position the new position of this Vehicle. It must be greater than
     * the current position of this Vehicle.
     */
    public void setPosition(int position) {
        if (position < this.position) {
            if (this.position >= this.route.size()) {
                this.position = this.route.size() - 1;
            } else {
                throw new IllegalArgumentException("Error : a vehicle cannot move to a previous position. In the worst case, it stays at the same position.");
            }
        }
        this.position = position;

        if (this.route.isEmpty()) {
            this.position = 0;
        } else if (this.position >= this.route.size()) {
            this.position = this.route.size() - 1;
        }
    }

    /**
     * Sets a new value for the number of time slots this Vehicle has to stay in
     * its current position. The new value cannot be negative.
     *
     * @param nbSlotsInSamePosition the number of time slots this Vehicle has to
     * stay in its current position. The new value cannot be negative.
     */
    public void setNbSlotsInSamePosition(int nbSlotsInSamePosition) {
        if (nbSlotsInSamePosition < 0) {
            throw new IllegalArgumentException("The new value for the jam penality of a Vehicle cannot be negative.");
        }

        // Normally, we use this method to set the number of slots we have to 
        // stay on this position, not to increase or decrease this number.
        this.nbTotSlotsInSamePositionForCurrentEdge = nbSlotsInSamePosition;

        //if we increase the number of slots in the same position, we increase the number tot of slots in the same position.
        if (nbSlotsInSamePosition > this.nbSlotsInSamePosition) {
            nbTotSlotsInSamePosition += nbSlotsInSamePosition - this.nbSlotsInSamePosition;
        }

        this.nbSlotsInSamePosition = nbSlotsInSamePosition;
    }

    /**
     * Adds an Edge at the end of the Route of this Vehicle.
     *
     * @param e the Edge we want to add
     * @return false if the edge is null and not added to the Route, else true
     */
    public boolean addEdgeToRoute(Edge e) {
        if (e == null) {
            return false;
        }
        route.add(e);
        routeLength += e.getLength();
        return true;
    }

    /**
     * Increases the position of this Vehicle in its route.
     *
     * @param number the number of edges to go through by this Vehicle. It must
     * be positive.
     */
    public void increasePosition(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("Error : we can only increase the position of a vehicle by a positive number.");
        }
        this.position += number;

        if (this.route.isEmpty()) {
            this.position = 0;
        } else if (this.position >= this.route.size()) {
            this.position = this.route.size() - 1;
        }
    }

    /**
     * Increases the number of time slots this Vehicle has to stay in its
     * current position by a number of time slots.
     *
     * @param number the number of time slots to increase the number of time
     * slots this Vehicle has to stay in its current position. It must be
     * positive.
     */
    public void increaseNbSlotsInSamePosition(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("The number of time slots to increase the jam penality of a Vehicle cannot be negative.");
        }
        this.nbSlotsInSamePosition += number;
        this.nbTotSlotsInSamePosition += number;
    }

    /**
     * Increases the travel time of this Vehicle by the time given in seconds.
     *
     * @param timeToAddInSeconds the time we want to increase by the travel time
     * of this Vehicle in seconds
     */
    public void increaseTravelTime(double timeToAddInSeconds) {
        if (timeToAddInSeconds < 0) {
            throw new IllegalArgumentException("We cannot add a negative time to the traveling time : " + timeToAddInSeconds);
        }
        this.travelTime += timeToAddInSeconds;
    }

    /**
     * Decreases the number of time slots this Vehicle has to stay in its
     * current position by a number of time slots.
     *
     * @param number the number of time slots to decrease the number of time
     * slots this Vehicle has to stay in its current position. It must be
     * positive.
     */
    public void decreaseNbSlotsInSamePosition(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("The number of time slots to increase the jam penality of a Vehicle cannot be negative.");
        }
        this.nbSlotsInSamePosition -= number;

        if (this.nbSlotsInSamePosition < 0) {
            this.nbSlotsInSamePosition = 0;
        }
    }

    /**
     * Returns if this Vehicle is arrived or not. A Vehicle is arrived if its
     * position is equal to the index of the last Edge of its route.
     *
     * @return true if this Vehicle is arrived, otherwise false.
     */
    public boolean isArrived() {
        if (route.isEmpty()) {
            return true;
        }
        return position == route.size() - 1;
    }

    /**
     * Returns the hashcode for this instance of Vehicle.
     *
     * @return the hascode for this Vehicle.
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.id);
        return hash;
    }

    /**
     * Determines wether or not two vehicles are equal. Two instances of Vehicle
     * are equal if they have the same id.
     *
     * @param obj an object to be compared with this Vehicle.
     * @return true if the object to be compared is an instance of Vehicle and
     * has the same id; false otherwise.
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
        final Vehicle other = (Vehicle) obj;
        return Objects.equals(this.id, other.id);
    }

}
