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
package ie.ucd.pel.ronin.statistics;

/**
 *
 * @author Come CACHARD
 *
 * Class used to store time statistics about the running of a simulation.
 */
public class SimulationProfilingTimeStatistics {

    /**
     * The duration of the simulation in seconds.
     */
    private double duration;

    /**
     * The timestamp in seconds at which the simulation is started to run.
     */
    private double launchTime;

    /**
     * The timestamp in seconds at which the simulation is finished to run.
     */
    private double endTime;

    /**
     * Time needed in seconds to process the computation of loads (step 1 of
     * computing the matrix of loads).
     */
    private double timeComputationLoads;

    /**
     * Time needed in seconds to process the propagation of overloads (step 2 of
     * computing the matrix of loads).
     */
    private double timePropagateOverloads;

    /**
     * Time needed in seconds to process the repositioning of vehicles (step 3
     * of computing the matrix of loads).
     */
    private double timeRepositioning;

    /**
     * Time needed in seconds to compute the statistics about objects like
     * edges.
     */
    private double timeComputeStatistics;

    /**
     * Time needed in seconds to write the outputs of each step.
     */
    private double timeWriteCurrentStepOutputs;

    /**
     * Time needed in seconds to write the outputs of end of simulation.
     */
    private double timeWriteEndSimulationOutputs;

    /**
     * Constructs and initializes an object that contains statistics about the
     * profiling time of a simulation.
     */
    public SimulationProfilingTimeStatistics() {
        this.duration = 0.;
        this.launchTime = 0.;
        this.endTime = 0.;
        this.timeComputationLoads = 0.;
        this.timePropagateOverloads = 0.;
        this.timeRepositioning = 0.;
        this.timeComputeStatistics = 0.;
        this.timeWriteCurrentStepOutputs = 0.;
        this.timeWriteEndSimulationOutputs = 0.;
    }

    /**
     * Returns the duration of the simulation in seconds.
     *
     * @return the duration of the simulation in seconds
     */
    public double getDuration() {
        return duration;
    }

    /**
     * Returns the launch time of the running of the simulation in seconds.
     *
     * @return the launch time of the running of the simulation in seconds
     */
    public double getLaunchTime() {
        return launchTime;
    }

    /**
     * Returns the end time of the running of the simulation in seconds.
     *
     * @return the end time of the running of the simulation in seconds
     */
    public double getEndTime() {
        return endTime;
    }

    /**
     * Returns the time needed in seconds to process the computation of loads
     * (step 1 of computing the matrix of loads).
     *
     * @return the time needed in seconds to process the computation of loads
     * (step 1 of computing the matrix of loads)
     */
    public double getTimeComputationLoads() {
        return timeComputationLoads;
    }

    /**
     * Returns the average time needed per step in seconds to process the
     * computation of loads (step 1 of computing the matrix of loads).
     *
     * @param nbSteps the number of steps of the simulation
     * @return the average time needed per step in seconds to process the
     * computation of loads (step 1 of computing the matrix of loads)
     */
    public double getAverageTimeComputationLoads(double nbSteps) {
        return timeComputationLoads / nbSteps;
    }

    /**
     * Returns the time needed in seconds to process the propagation of
     * overloads (step 2 of computing the matrix of loads).
     *
     * @return the time needed in seconds to process the propagation of
     * overloads (step 2 of computing the matrix of loads)
     */
    public double getTimePropagateOverloads() {
        return timePropagateOverloads;
    }

    /**
     * Returns the average time needed per step in seconds to process the
     * propagation of overloads (step 2 of computing the matrix of loads).
     *
     * @param nbSteps the number of steps of the simulation
     * @return the average time needed per step in seconds to process the
     * propagation of overloads (step 2 of computing the matrix of loads)
     */
    public double getAverageTimePropagateOverloads(double nbSteps) {
        return timePropagateOverloads / nbSteps;
    }

    /**
     * Returns the time needed in seconds to process the repositioning of
     * vehicles (step 3 of computing the matrix of loads).
     *
     * @return the time needed in seconds to process the repositioning of
     * vehicles (step 3 of computing the matrix of loads)
     */
    public double getTimeRepositioning() {
        return timeRepositioning;
    }

    /**
     * Returns the average time needed per step in seconds to process the
     * repositioning of vehicles (step 3 of computing the matrix of loads).
     *
     * @param nbSteps the number of steps of the simulation
     * @return the average time needed per step in seconds to process the
     * repositioning of vehicles (step 3 of computing the matrix of loads)
     */
    public double getAverageTimeRepositioning(double nbSteps) {
        return timeRepositioning / nbSteps;
    }

    /**
     * Returns the time needed in seconds to compute the statistics about
     * objects like edges.
     *
     * @return the time needed in seconds to compute the statistics about
     * objects like edges
     */
    public double getTimeComputeStatistics() {
        return timeComputeStatistics;
    }

    /**
     * Returns the average time needed per step in seconds to compute the
     * statistics about objects like edges.
     *
     * @param nbSteps the number of steps of the simulation
     * @return the average time needed per step in seconds to compute the
     * statistics about objects like edges
     */
    public double getAverageTimeComputeStatistics(double nbSteps) {
        return timeComputeStatistics / nbSteps;
    }

    /**
     * Returns the time needed in seconds to write the outputs of the steps of
     * simulation.
     *
     * @return the time needed in seconds to write the outputs of the steps of
     * simulation
     */
    public double getTimeWriteCurrentStepOutputs() {
        return timeWriteCurrentStepOutputs;
    }

    /**
     * Returns the average time needed per step in seconds to write the outputs
     * of the steps of simulation.
     *
     * @param nbSteps the number of steps of the simulation
     * @return the average time needed per step in seconds to write the outputs
     * of the steps of simulation
     */
    public double getAverageTimeWriteCurrentStepOutputs(double nbSteps) {
        return timeWriteCurrentStepOutputs / nbSteps;
    }

    /**
     * Returns the time needed in seconds to write the outputs of end of
     * simulation.
     *
     * @return the time needed in seconds to write the outputs of end of
     * simulation
     */
    public double getTimeWriteEndSimulationOutputs() {
        return timeWriteEndSimulationOutputs;
    }

    /**
     * Sets the new value of the duration of the simulation in seconds.
     *
     * @param duration the duration of the simulation in seconds. It must be
     * positive.
     */
    public void setDuration(double duration) {
        if (duration < 0) {
            throw new IllegalArgumentException("Error : the duration of a simulation must be positive.");
        }
        this.duration = duration;
    }

    /**
     * Sets the new value of the launch time of this Simulation in seconds.
     *
     * @param launchTime the new value of the launch time of this Simulation in
     * seconds. It must be positive.
     */
    public void setLaunchTime(double launchTime) {
        if (launchTime < 0) {
            throw new IllegalArgumentException("Error : the launch time of a simulation must be positive.");
        }
        this.launchTime = launchTime;
    }

    /**
     * Sets the new value of the end time of this Simulation in seconds and so
     * the duration of the simulation.
     *
     * @param endTime the new value of the end time of this Simulation in
     * seconds. It must be positive.
     */
    public void setEndTime(double endTime) {
        if (endTime < 0) {
            throw new IllegalArgumentException("Error : the end time of a simulation must be positive.");
        }
        this.endTime = endTime;
        setDuration(this.endTime - this.launchTime);
    }

    /**
     * Sets the new value of the time needed to compute the loads in seconds.
     *
     * @param timeComputationLoads the new value of the time needed to compute
     * the loads in seconds. It must be positive.
     */
    public void setTimeComputationLoads(double timeComputationLoads) {
        if (timeComputationLoads < 0) {
            throw new IllegalArgumentException("Error : the time needed to compute the loads of a simulation must be positive.");
        }
        this.timeComputationLoads = timeComputationLoads;
    }

    /**
     * Sets the new value of the time needed to propagate the overloads in
     * seconds.
     *
     * @param timePropagateOverloads the new value of the time needed to
     * propagate the overloads in seconds. It must be positive.
     */
    public void setTimePropagateOverloads(double timePropagateOverloads) {
        if (timePropagateOverloads < 0) {
            throw new IllegalArgumentException("Error : the time needed to propagate the overloads of a simulation must be positive.");
        }
        this.timePropagateOverloads = timePropagateOverloads;
    }

    /**
     * Sets the new value of the time needed to reposition vehicles in seconds.
     *
     * @param timeRepositioning the new value of the time needed to reposition
     * vehicles in seconds. It must be positive.
     */
    public void setTimeRepositioning(double timeRepositioning) {
        if (timeRepositioning < 0) {
            throw new IllegalArgumentException("Error : the time needed to reposition vehicles of a simulation must be positive.");
        }
        this.timeRepositioning = timeRepositioning;
    }

    /**
     * Sets the new value of the time needed to compute statistics for objects
     * like edges in seconds.
     *
     * @param timeComputeStatistics the new value of the time needed to compute
     * statistics for objects like edges in seconds. It must be positive.
     */
    public void setTimeComputeStatistics(double timeComputeStatistics) {
        if (timeComputeStatistics < 0) {
            throw new IllegalArgumentException("Error : the time needed to compute statistics for objects like edges of a simulation must be positive.");
        }
        this.timeComputeStatistics = timeComputeStatistics;
    }

    /**
     * Sets the new value of the time needed to write the outputs of all steps
     * of the simulation in seconds.
     *
     * @param timeWriteCurrentStepOutputs the new value of the time needed to
     * write the outputs of all steps of the simulation in seconds. It must be
     * positive.
     */
    public void setTimeWriteCurrentStepOutputs(double timeWriteCurrentStepOutputs) {
        if (timeWriteCurrentStepOutputs < 0) {
            throw new IllegalArgumentException("Error : the time needed to write the outputs of all steps of of a simulation must be positive.");
        }
        this.timeWriteCurrentStepOutputs = timeWriteCurrentStepOutputs;
    }

    /**
     * Sets the new value of the time needed to write the outputs of end of
     * simulation in seconds.
     *
     * @param timeWriteEndSimulationOutputs the new value of the time needed to
     * write the outputs of end of simulation in seconds. It must be positive.
     */
    public void setTimeWriteEndSimulationOutputs(double timeWriteEndSimulationOutputs) {
        if (timeWriteEndSimulationOutputs < 0) {
            throw new IllegalArgumentException("Error : the time needed to write the outputs of end of a simulation must be positive.");
        }
        this.timeWriteEndSimulationOutputs = timeWriteEndSimulationOutputs;
    }

    /**
     * Increases the time needed to compute the loads in seconds.
     *
     * @param timeComputationLoads the time needed to compute the loads in
     * seconds of a step. It must be positive.
     */
    public void increaseTimeComputationLoads(double timeComputationLoads) {
        if (timeComputationLoads < 0) {
            throw new IllegalArgumentException("Error : the time needed to compute the loads of a simulation must be positive.");
        }
        this.timeComputationLoads += timeComputationLoads;
    }

    /**
     * Increases the time needed to propagate the overloads in seconds.
     *
     * @param timePropagateOverloads the time needed to propagate the overloads
     * in seconds of a step. It must be positive.
     */
    public void increaseTimePropagateOverloads(double timePropagateOverloads) {
        if (timePropagateOverloads < 0) {
            throw new IllegalArgumentException("Error : the time needed to propagate the overloads of a simulation must be positive.");
        }
        this.timePropagateOverloads += timePropagateOverloads;
    }

    /**
     * Increases the time needed to reposition vehicles in seconds.
     *
     * @param timeRepositioning the time needed to reposition vehicles in
     * seconds of a step. It must be positive.
     */
    public void increasesTimeRepositioning(double timeRepositioning) {
        if (timeRepositioning < 0) {
            throw new IllegalArgumentException("Error : the time needed to reposition vehicles of a simulation must be positive.");
        }
        this.timeRepositioning += timeRepositioning;
    }

    /**
     * Increases the time needed to compute statistics for objects like edges in
     * seconds.
     *
     * @param timeComputeStatistics the time needed to compute statistics for
     * objects like edges of a step in seconds. It must be positive.
     */
    public void increaseTimeComputeStatistics(double timeComputeStatistics) {
        if (timeComputeStatistics < 0) {
            throw new IllegalArgumentException("Error : the time needed to compute statistics for objects like edges of a simulation must be positive.");
        }
        this.timeComputeStatistics += timeComputeStatistics;
    }

    /**
     * Increases the time needed to write the outputs of all steps of the
     * simulation in seconds.
     *
     * @param timeWriteCurrentStepOutputs the time needed to write the outputs
     * of all steps of the simulation in seconds of a step. It must be positive.
     */
    public void increaseTimeWriteCurrentStepOutputs(double timeWriteCurrentStepOutputs) {
        if (timeWriteCurrentStepOutputs < 0) {
            throw new IllegalArgumentException("Error : the time needed to write the outputs of all steps of of a simulation must be positive.");
        }
        this.timeWriteCurrentStepOutputs += timeWriteCurrentStepOutputs;
    }

    /**
     * Prints the profiling time of the simulation.
     *
     * @param nbSteps the number of steps of the simulation
     */
    public void printProfilingTime(double nbSteps) {
        System.out.println("Duration of the simulation : " + duration + " seconds.");
        System.out.println("");
        System.out.println("Loads Matrix time profiling :");
        System.out.println("\t step 1 computing loads : ");
        System.out.println("\t\ttotal time (seconds) : " + timeComputationLoads);
        System.out.println("\t\taverage time (seconds) : " + getAverageTimeComputationLoads(nbSteps));
        System.out.println("\t step 2 propagate overloads : ");
        System.out.println("\t\ttotal time (seconds) : " + timePropagateOverloads);
        System.out.println("\t\taverage time (seconds) : " + getAverageTimePropagateOverloads(nbSteps));
        System.out.println("\t step 3 repositioning : ");
        System.out.println("\t\ttotal time (seconds) : " + timeRepositioning);
        System.out.println("\t\taverage time (seconds) : " + getAverageTimeRepositioning(nbSteps));
        System.out.println("\t step 4 compute statistics : ");
        System.out.println("\t\ttotal time (seconds) : " + timeComputeStatistics);
        System.out.println("\t\taverage time (seconds) : " + getAverageTimeComputeStatistics(nbSteps));
        System.out.println("\t step 5 write current step outputs : ");
        System.out.println("\t\ttotal time (seconds) : " + timeWriteCurrentStepOutputs);
        System.out.println("\t\taverage time (seconds) : " + getAverageTimeWriteCurrentStepOutputs(nbSteps));
        System.out.println("\t step 6 write end simulation outputs : ");
        System.out.println("\t\ttotal time (seconds) : " + timeWriteEndSimulationOutputs);
    }

}
