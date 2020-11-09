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
import ie.ucd.pel.ronin.communication.serverresponse.edgeresponse.EdgeLengthServerResponse;
import ie.ucd.pel.ronin.communication.socket.RoninServer;
import ie.ucd.pel.ronin.model.Edge;

/**
 *
 * @author Come CACHARD
 *
 * Class describing a Client query asking to return the length in meter of an
 * edge of a Ronin Simulation hosted by a Ronin server.
 */
public class GetEdgeLengthQuery extends Query {

    /**
     * The id of the edge concerned by this query.
     */
    private final String edgeId;

    /**
     * Constructs and initializes a query asking to return the length in meter
     * of an edge of a Ronin Simulation hosted by a Ronin server.
     *
     * @param edgeId the id of the edge concerned by this query.
     */
    public GetEdgeLengthQuery(String edgeId) {
        super();
        this.edgeId = edgeId;
    }

    /**
     * Executes the query of the client by returning the length in m of an edge
     * of a Ronin Simulation hosted by a Ronin server. It returns a
     * EdgeLengthServerResponse to send to the Client, that contains the length
     * in m of an edge. If the Edge is not found, the request fails.
     *
     * @param roninServer the Ronin server that hosts the simulation and
     * receives this query.
     * @return a EdgeLengthServerResponse to send to the Client.
     */
    @Override
    public EdgeLengthServerResponse execute(RoninServer roninServer) {
        Edge e = roninServer.getSimulation().getNetwork().getEdge(edgeId);
        if (e == null) {
            return new EdgeLengthServerResponse(ServerResponse.StatusResponse.STATUS_FAILED,
                    "The edge was not found for the requested id " + edgeId + ".", -1);
        }
        return new EdgeLengthServerResponse(ServerResponse.StatusResponse.STATUS_SUCCESS,
                "The request has been processed successfully", e.getLength());
    }

    @Override
    public boolean checkQueryParameters() {
        if (edgeId == null || edgeId.isEmpty()) {
            this.setResponse(new EdgeLengthServerResponse(ServerResponse.StatusResponse.STATUS_FAILED,
                    "The id of the Edge is not valid.", -1));
            return false;
        }
        return true;
    }

    /**
     * Returns a EdgeLengthServerResponse that contains the length in meter of a
     * specific edge.
     *
     * @return a EdgeLengthServerResponse that contains the length in meter of a
     * specific edge
     */
    @Override
    public EdgeLengthServerResponse getResponse() {
        if (response == null) {
            return new EdgeLengthServerResponse(ServerResponse.StatusResponse.STATUS_NO_RESPONSE,
                    "There is no response associated to this Query.", -1);
        }
        return (EdgeLengthServerResponse) response;
    }

}
