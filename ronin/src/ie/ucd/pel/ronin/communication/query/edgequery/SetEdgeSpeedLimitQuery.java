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
package ie.ucd.pel.ronin.communication.query.edgequery;

import ie.ucd.pel.ronin.communication.query.Query;
import ie.ucd.pel.ronin.communication.serverresponse.ServerResponse;
import ie.ucd.pel.ronin.communication.socket.RoninServer;
import ie.ucd.pel.ronin.model.Edge;

/**
 *
 * @author Come CACHARD
 *
 * Class describing a Client query asking to set the speed limit in m/s of an
 * edge of a Ronin Simulation hosted by a Ronin server.
 */
public class SetEdgeSpeedLimitQuery extends Query {

    /**
     * The id of the edge concerned by this query.
     */
    private final String edgeId;

    /**
     * The new value of the speed limit of the edge in m/s.
     */
    private final double newSpeedLimit;

    /**
     * Constructs and initializes a query asking to set the speed limit in m/s
     * of an edge of a Ronin Simulation hosted by a Ronin server.
     *
     * @param edgeId the id of th edge to set a new value of speed limit
     * @param newSpeedLimit the new speed limit of the Edge in m/s
     */
    public SetEdgeSpeedLimitQuery(String edgeId, double newSpeedLimit) {
        super();
        this.edgeId = edgeId;
        this.newSpeedLimit = newSpeedLimit;
    }

    /**
     * Executes the query of the client by setting the speedlimit in m/s of an
     * edge of a Ronin Simulation hosted by a Ronin server. If the Edge is not
     * found, the request fails. It returns a ServerResponse to send to the
     * Client.
     *
     * @param roninServer the Ronin server that hosts the simulation and
     * receives this query.
     * @return a ServerResponse to send to the Client.
     */
    @Override
    public ServerResponse execute(RoninServer roninServer) {
        Edge e = roninServer.getSimulation().getNetwork().getEdge(edgeId);
        if (e == null) {
            return new ServerResponse(ServerResponse.StatusResponse.STATUS_FAILED,
                    "The edge was not found for the requested id " + edgeId + ".");
        }
        e.setSpeedLimit(newSpeedLimit);
        return new ServerResponse(ServerResponse.StatusResponse.STATUS_SUCCESS,
                "The request has been processed successfully.");
    }

    @Override
    public boolean checkQueryParameters() {
        if (edgeId == null || edgeId.isEmpty()) {
            this.setResponse(new ServerResponse(ServerResponse.StatusResponse.STATUS_FAILED,
                    "The id of the Edge is not valid."));
            return false;
        }
        return true;
    }

}
