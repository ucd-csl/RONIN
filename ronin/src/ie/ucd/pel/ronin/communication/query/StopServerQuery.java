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

import ie.ucd.pel.ronin.communication.serverresponse.ServerResponse;
import ie.ucd.pel.ronin.communication.socket.RoninServer;

/**
 *
 * @author Come CACHARD
 *
 * Describes a Client's query that stops the simulation hosted by a Ronin server
 * and stops the Ronin server.
 */
public class StopServerQuery extends Query {

    /**
     * Executes the query of the client by stopping the simulation hosted by the
     * Ronin server and stopping the server itself.
     * We produce at the same time the outputs of end of simulation.
     *
     * @param roninServer the Ronin server that hosts the simulation and
     * receives this query.
     * @return a ServerResponse to send to the Client.
     */
    @Override
    public ServerResponse execute(RoninServer roninServer) {
        roninServer.getSimulation().endSimulationWork();
        roninServer.setStopServer(true);
        return new ServerResponse(ServerResponse.StatusResponse.STATUS_SUCCESS,
                "The simulation and the Ronin server have been stopped successfully.");
    }

}
