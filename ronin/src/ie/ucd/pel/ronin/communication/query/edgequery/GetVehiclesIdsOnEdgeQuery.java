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
import ie.ucd.pel.ronin.communication.serverresponse.vehicleresponse.ListVehiclesIdsServerResponse;
import ie.ucd.pel.ronin.communication.socket.RoninServer;
import ie.ucd.pel.ronin.model.Vehicle;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Come CACHARD
 *
 * Class describing a Client query asking to return the list of ids of vehicles
 * that are on a specific Edge at the current time step of the simulation.
 */
public class GetVehiclesIdsOnEdgeQuery extends Query {

    /**
     * The id of the edge we want the vehicles ids.
     */
    private final String edgeId;

    /**
     * Constructs and initializes a Query that asks the list of ids of vehicles
     * that are on a specific Edge at the current time step of the simulation.
     *
     * @param edgeId the id of the Edge we want the list of vehicles ids
     */
    public GetVehiclesIdsOnEdgeQuery(String edgeId) {
        super();
        this.edgeId = edgeId;
    }

    /**
     * Executes the query of the client by returning the list of ids of vehicles
     * that are on a specific Edge at the current time step of the simulation.
     * It returns a ListVehiclesIdsServerResponse to send to the Client, that
     * contains the list of ids of the vehicles on a specific edge. If the Edge
     * is not found, we do nothing and it is not counted as error.
     *
     * @param roninServer the Ronin server that hosts the simulation and
     * receives this query.
     * @return a ListVehiclesIdsServerResponse to send to the Client.
     */
    @Override
    public ListVehiclesIdsServerResponse execute(RoninServer roninServer) {
        List<Vehicle> vehList = roninServer.getSimulation().getCurrentStepFinalLoadForEdge(edgeId);
        List<String> idsList = new LinkedList<>();
        vehList.stream().forEach((v) -> {
            idsList.add(v.getId());
        });
        return new ListVehiclesIdsServerResponse(ServerResponse.StatusResponse.STATUS_SUCCESS,
                "The query has been processed successfully.", idsList);
    }

    @Override
    public boolean checkQueryParameters() {
        if (edgeId == null || edgeId.isEmpty()) {
            this.setResponse(new ListVehiclesIdsServerResponse(ServerResponse.StatusResponse.STATUS_FAILED,
                    "The id of the Edge is not valid.", null));
            return false;
        }
        return true;
    }

    /**
     * Returns a ListVehiclesIdsServerResponse that contains the list of ids of
     * the vehicles on a specific edge.
     *
     * @return a ListVehiclesIdsServerResponse that contains the list of ids of
     * the vehicles on a specific edge
     */
    @Override
    public ListVehiclesIdsServerResponse getResponse() {
        if (response == null) {
            return new ListVehiclesIdsServerResponse(ServerResponse.StatusResponse.STATUS_NO_RESPONSE,
                    "There is no response associated to this Query.", null);
        }
        return (ListVehiclesIdsServerResponse) response;
    }

}
