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
package ie.ucd.pel.ronin.communication.query;

import ie.ucd.pel.ronin.communication.serverresponse.ProcessNextStepServerResponse;
import ie.ucd.pel.ronin.communication.serverresponse.ServerResponse;
import ie.ucd.pel.ronin.communication.socket.RoninServer;

/**
 *
 * @author Come CACHARD
 *
 * Class describing a Client query asking to proccess the next step of a Ronin
 * Simulation hosted by a Ronin server.
 */
public class ProcessNextStepQuery extends Query {

    /**
     * Executes the query of the client by processing the next step of the
     * simulation hosted by the Ronin server. It returns a
     * ProcessNextStepServerResponse to send to the Client, that indicates if
     * the simulation is finished.
     *
     * @param roninServer the Ronin server that hosts the simulation and
     * receives this query.
     * @return a ProcessNextStepServerResponse to send to the Client.
     */
    @Override
    public ProcessNextStepServerResponse execute(RoninServer roninServer) {
        boolean isSimulationFinished = roninServer.getSimulation().processNextStep();
        return new ProcessNextStepServerResponse(ServerResponse.StatusResponse.STATUS_SUCCESS,
                "The processing of the next step of the simulation has been done successfully.",
                isSimulationFinished);
    }

    @Override
    public ProcessNextStepServerResponse getResponse() {
        if (response == null) {
            return new ProcessNextStepServerResponse(ServerResponse.StatusResponse.STATUS_NO_RESPONSE,
                    "There is no response associated to this Query.", false);
        }
        return (ProcessNextStepServerResponse) response;
    }

}
