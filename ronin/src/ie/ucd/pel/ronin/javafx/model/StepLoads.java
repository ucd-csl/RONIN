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
package ie.ucd.pel.ronin.javafx.model;

import java.util.Map;

/**
 *
 * @author Come CACHARD
 *
 * Object describing the loads per Edge of a timeStep of a simulation of Ronin.
 */
public class StepLoads {

    /**
     * The timeslot corresponding to the step of this StepLoads in seconds.
     */
    private final double timeslot;

    /**
     * The map of loads per Edge.
     */
    private final Map<String, Double> loadsPerEdge;

    /**
     * Constructs and initializes a StepLoads object.
     *
     * @param timeslot the timeslot corresponding to the step of this StepLoads
     * in seconds
     * @param loadsPerEdge the map of loads per Edge
     */
    public StepLoads(double timeslot, Map<String, Double> loadsPerEdge) {
        this.timeslot = timeslot;
        this.loadsPerEdge = loadsPerEdge;
    }

    /**
     * Returns the timeslot corresponding to the step of this StepLoads in
     * seconds.
     *
     * @return the timeslot corresponding to the step of this StepLoads in
     * seconds
     */
    public double getTimeslot() {
        return timeslot;
    }

    /**
     * Returns the map of loads per Edge.
     *
     * @return the map of loads per Edge
     */
    public Map<String, Double> getLoadsPerEdge() {
        return loadsPerEdge;
    }

}
