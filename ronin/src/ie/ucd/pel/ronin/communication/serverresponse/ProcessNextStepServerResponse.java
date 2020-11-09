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
package ie.ucd.pel.ronin.communication.serverresponse;

/**
 *
 * @author Come CACHARD
 *
 * Class describing the response of a Ronin server to the query of a client
 * asking to process the next step of a Ronin simulation.
 */
public class ProcessNextStepServerResponse extends ServerResponse {

    /**
     * If true, the simulation hosted by the Ronin server is finished; false
     * otherwise.
     */
    private final boolean simulationFinished;

    /**
     * Creates a Server response to a query asking to process the next step of a
     * Ronin Simulation.
     *
     * @param status the status of the execution of the query : if it has
     * succeeded or failed, or not implemented feature
     * @param description the description associated to the status of the
     * response
     * @param simulationFinished if true, the simulation hosted by the Ronin
     * server is finished; false otherwise
     */
    public ProcessNextStepServerResponse(StatusResponse status, String description, boolean simulationFinished) {
        super(status, description);
        this.simulationFinished = simulationFinished;
    }

    /**
     * Returns true if the simulation hosted by the Ronin server is finished;
     * false otherwise.
     *
     * @return true if the simulation hosted by the Ronin server is finished;
     * false otherwise
     */
    public final boolean isSimulationFinished() {
        return simulationFinished;
    }

}
