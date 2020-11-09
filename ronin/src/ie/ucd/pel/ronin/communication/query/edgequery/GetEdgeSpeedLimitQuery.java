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
import ie.ucd.pel.ronin.communication.serverresponse.edgeresponse.EdgeSpeedLimitServerResponse;
import ie.ucd.pel.ronin.communication.socket.RoninServer;
import ie.ucd.pel.ronin.model.Edge;

/**
 *
 * @author Come CACHARD
 *
 * Class describing a Client query asking to return the speed limit in m/s of a
 * specific edge of a Ronin Simulation hosted by a Ronin server.
 */
public class GetEdgeSpeedLimitQuery extends Query {

    /**
     * The id of the edge concerned by this query.
     */
    private final String edgeId;

    /**
     * Constructs and initializes a query asking to return the speed limit in
     * m/s of an edge of a Ronin Simulation hosted by a Ronin server.
     *
     * @param edgeId the id of the edge concerned by this query.
     */
    public GetEdgeSpeedLimitQuery(String edgeId) {
        super();
        this.edgeId = edgeId;
    }

    /**
     * Executes the query of the client by returning the speed limit in m/s of
     * an edge of a Ronin Simulation hosted by a Ronin server. It returns a
     * EdgeSpeedLimitServerResponse to send to the Client, that contains the
     * speed limit in m/s of an edge. If the Edge is not found, the request
     * fails.
     *
     * @param roninServer the Ronin server that hosts the simulation and
     * receives this query.
     * @return a EdgeSpeedLimitServerResponse to send to the Client.
     */
    @Override
    public EdgeSpeedLimitServerResponse execute(RoninServer roninServer) {
        Edge e = roninServer.getSimulation().getNetwork().getEdge(edgeId);
        if (e == null) {
            return new EdgeSpeedLimitServerResponse(ServerResponse.StatusResponse.STATUS_FAILED,
                    "The edge was not found for the requested id " + edgeId + ".", -1);
        }
        return new EdgeSpeedLimitServerResponse(ServerResponse.StatusResponse.STATUS_SUCCESS,
                "The request has been processed successfully.",
                e.getSpeedLimit());
    }

    @Override
    public boolean checkQueryParameters() {
        if (edgeId == null || edgeId.isEmpty()) {
            this.setResponse(new EdgeSpeedLimitServerResponse(ServerResponse.StatusResponse.STATUS_FAILED,
                    "The id of the Edge is not valid.", -1));
            return false;
        }
        return true;
    }

    /**
     * Returns a EdgeSpeedLimitServerResponse that contains the speed limit in
     * m/s of a specific edge.
     *
     * @return a EdgeSpeedLimitServerResponse that contains the speed limit in
     * m/s of a specific edge
     */
    @Override
    public EdgeSpeedLimitServerResponse getResponse() {
        if (response == null) {
            return new EdgeSpeedLimitServerResponse(ServerResponse.StatusResponse.STATUS_NO_RESPONSE,
                    "There is no response associated to this Query.", -1);
        }
        return (EdgeSpeedLimitServerResponse) response;
    }

}
