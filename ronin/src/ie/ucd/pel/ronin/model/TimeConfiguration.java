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

/**
 *
 * @author Come CACHARD
 *
 * Class used to contains the informations about time for the SUMO simulation
 * contained in sumocfg file.
 */
public class TimeConfiguration {

    /**
     * The time the simulation starts at in seconds. By default, it is set to 0.
     */
    private double beginTime;

    /**
     * The time the simulation ends at in seconds. By default, it is set to -1
     * and so is not considered.
     */
    private double endTime;

    /**
     * The step duration in seconds. By default, it is set to 1.
     */
    private double stepLength;

    /**
     * Constructs and initializes an object containing the time configuration
     * information for the simulation with default values.
     */
    public TimeConfiguration() {
        this.beginTime = 0;
        this.endTime = -1;
        this.stepLength = 1;
    }

    /**
     * Constructs and initializes an object containing the time configuration
     * information for the simulation. If we give negative value, we initialize
     * the object with default values.
     *
     * @param beginTime the time the simulation starts at in seconds.
     * @param endTime the time the simulation ends at in seconds. It must be
     * superior to beginTime.
     * @param stepLength the step duration in seconds. Must be superior to 0 or
     * we use the default value.
     */
    public TimeConfiguration(double beginTime, double endTime, double stepLength) {
        if (beginTime >= 0) {
            this.beginTime = beginTime;
        } else {
            this.beginTime = 0;
        }

        if (endTime >= 0) {
            if (endTime < beginTime) {
                throw new IllegalArgumentException("Error : the end time of a "
                        + "simulation must be superior to the begin time.");
            }
            this.endTime = endTime;
        } else {
            this.endTime = -1;
        }

        if (stepLength > 0) {
            this.stepLength = stepLength;
        } else {
            this.stepLength = 1;
        }

    }

    /**
     * Returns the begin time of the simulation in seconds.
     *
     * @return the begin time of the simulation in seconds
     */
    public double getBeginTime() {
        return beginTime;
    }

    /**
     * Returns the end time of the simulation in seconds.
     *
     * @return the end time of the simulation in seconds
     */
    public double getEndTime() {
        return endTime;
    }

    /**
     * Returns the step length in seconds.
     *
     * @return the step length in seconds
     */
    public double getStepLength() {
        return stepLength;
    }

    /**
     * Sets the value of the begin time of the simulation in seconds. If the
     * given argument is negative we use the default value 0.
     *
     * @param beginTime the new value for the begin time of the simulation in
     * seconds
     */
    public void setBeginTime(double beginTime) {
        if (beginTime >= 0) {
            this.beginTime = beginTime;
        } else {
            this.beginTime = 0;
        }
    }

    /**
     * Sets the new value of the end time of the simulation in seconds. It must
     * be positive else we use the default value -1 and this attribute will not
     * be considered for the simulation. It must be superior to the begin time
     * of the simulation.
     *
     * @param endTime the new value of the end time of the simulation in seconds
     */
    public void setEndTime(double endTime) {
        if (endTime >= 0) {
            if (endTime < beginTime) {
                throw new IllegalArgumentException("Error : the end time of a "
                        + "simulation must be superior to the begin time.");
            }
            this.endTime = endTime;
        } else {
            this.endTime = -1;
        }
    }

    /**
     * Sets the new value for the duration of step for this simulation in
     * seconds. The new value must be superior to 0 else we use the default
     * value 1.
     *
     * @param stepLength the new value for the duration of step for this
     * simulation in seconds
     */
    public void setStepLength(double stepLength) {
        if (stepLength > 0) {
            this.stepLength = stepLength;
        } else {
            this.stepLength = 1;
        }
    }

}
