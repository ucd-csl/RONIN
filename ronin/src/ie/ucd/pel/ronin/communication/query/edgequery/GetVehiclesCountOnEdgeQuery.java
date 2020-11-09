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
import ie.ucd.pel.ronin.communication.serverresponse.vehicleresponse.CountVehiclesServerResponse;
import ie.ucd.pel.ronin.communication.socket.RoninServer;
import ie.ucd.pel.ronin.model.Vehicle;
import java.util.List;

/**
 *
 * @author Come CACHARD
 *
 * Class describing a Client query asking to return the number of vehicles that
 * are on a specific Edge at the current time step of a Ronin Simulation hosted
 * by a Ronin server.
 */
public class GetVehiclesCountOnEdgeQuery extends Query {

    /**
     * The id of the edge we want the number of vehicles.
     */
    private final String edgeId;

    /**
     * Constructs and initializes a query asking to return the number of
     * vehicles that are on a specific Edge at the current time step of a Ronin
     * Simulation hosted by a Ronin server.
     *
     * @param edgeId the id of the edge we want the number of vehicles at the
     * current time step
     */
    public GetVehiclesCountOnEdgeQuery(String edgeId) {
        super();
        this.edgeId = edgeId;
    }

    /**
     * Executes the query of the client by returning the number of vehicles on
     * an edge of a Ronin Simulation hosted by a Ronin server. It returns a
     * CountVehiclesServerResponse to send to the Client, that contains the
     * count of vehicles. If the Edge is not found, we do nothing and it is not
     * counted as error.
     *
     * @param roninServer the Ronin server that hosts the simulation and
     * receives this query.
     * @return a CountVehiclesServerResponse to send to the Client.
     */
    @Override
    public CountVehiclesServerResponse execute(RoninServer roninServer) {
        List<Vehicle> vehList = roninServer.getSimulation().getCurrentStepFinalLoadForEdge(edgeId);
        return new CountVehiclesServerResponse(ServerResponse.StatusResponse.STATUS_SUCCESS,
                "The query has been processed successfully.", vehList.size());
    }

    @Override
    public boolean checkQueryParameters() {
        if (edgeId == null || edgeId.isEmpty()) {
            this.setResponse(new CountVehiclesServerResponse(ServerResponse.StatusResponse.STATUS_FAILED,
                    "The id of the Edge is not valid.", -1));
            return false;
        }
        return true;
    }

    /**
     * Returns a CountVehiclesServerResponse that contains the number of the
     * vehicles on a specific edge.
     *
     * @return a CountVehiclesServerResponse that contains the number of the
     * vehicles on a specific edge
     */
    @Override
    public CountVehiclesServerResponse getResponse() {
        if (response == null) {
            return new CountVehiclesServerResponse(ServerResponse.StatusResponse.STATUS_NO_RESPONSE,
                    "There is no response associated to this Query.", -1);
        }
        return (CountVehiclesServerResponse) response;
    }

}
