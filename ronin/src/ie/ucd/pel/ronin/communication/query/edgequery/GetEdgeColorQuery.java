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
import ie.ucd.pel.ronin.communication.serverresponse.edgeresponse.EdgeColorServerResponse;
import ie.ucd.pel.ronin.communication.socket.RoninServer;
import ie.ucd.pel.ronin.model.Edge;
import javafx.scene.paint.Color;

/**
 *
 * @author Come CACHARD
 *
 * Class describing a Client query asking to return the color of an edge for the
 * current time step of a Ronin Simulation hosted by a Ronin server.
 */
public class GetEdgeColorQuery extends Query {

    /**
     * The id of the edge concerned by this query.
     */
    private final String edgeId;

    /**
     * Creates and returns a Query asking the color of an Edge corresponding to
     * the state of its trafic for the current time step of the simulation.
     *
     * @param edgeId the id of the edge we want the color
     */
    public GetEdgeColorQuery(String edgeId) {
        super();
        this.edgeId = edgeId;
    }

    /**
     * Executes the query of the client by returning the color of an edge for
     * the current time step of a Ronin Simulation hosted by a Ronin server. It
     * returns a EdgeColorServerResponse to send to the Client, that contains
     * the color of an edge for the current time step of the simulation. If the
     * Edge is not found, the request fails.
     *
     * @param roninServer the Ronin server that hosts the simulation and
     * receives this query.
     * @return a EdgeColorServerResponse to send to the Client.
     */
    @Override
    public EdgeColorServerResponse execute(RoninServer roninServer) {
        Edge e = roninServer.getSimulation().getNetwork().getEdge(edgeId);
        if (e == null) {
            return new EdgeColorServerResponse(ServerResponse.StatusResponse.STATUS_FAILED,
                    "The edge was not found for the requested id " + edgeId + ".", -1, -1, -1, -1);
        }
        int currentload = roninServer.getSimulation().getCurrentStepFinalLoadForEdge(edgeId).size();
        Color color = e.getColor(currentload);
        return new EdgeColorServerResponse(ServerResponse.StatusResponse.STATUS_SUCCESS,
                "The request has been processed successfully",
                color.getBlue(), color.getGreen(), color.getRed(), color.getOpacity());
    }

    @Override
    public boolean checkQueryParameters() {
        if (edgeId == null || edgeId.isEmpty()) {
            this.setResponse(new EdgeColorServerResponse(ServerResponse.StatusResponse.STATUS_FAILED,
                    "The id of the edge is not valid.", -1, -1, -1, -1));
            return false;
        }
        return true;
    }

    /**
     * Returns a EdgeColorServerResponse that contains the color of a specific
     * edge for the current time step of the simulation.
     *
     * @return a EdgeColorServerResponse that contains the color of a specific
     * edge for the current time step of the simulation.
     */
    @Override
    public EdgeColorServerResponse getResponse() {
        if (response == null) {
            return new EdgeColorServerResponse(ServerResponse.StatusResponse.STATUS_NO_RESPONSE,
                    "There is no response associated to this Query.", -1, -1, -1, -1);
        }
        return (EdgeColorServerResponse) response;
    }

}
