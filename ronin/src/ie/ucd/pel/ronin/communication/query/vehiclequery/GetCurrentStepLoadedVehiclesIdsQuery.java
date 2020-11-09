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
package ie.ucd.pel.ronin.communication.query.vehiclequery;

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
 * that are loaded at the current timestep of a Ronin Simulation hosted by a
 * Ronin server.
 */
public class GetCurrentStepLoadedVehiclesIdsQuery extends Query {

    /**
     * Executes the query of the client by returning the list of of ids of
     * vehicles that are loaded at the current timestep of a Ronin Simulation
     * hosted by a Ronin server. It returns a ListVehiclesIdsServerResponse to
     * send to the Client, that contains the list of ids of vehicles.
     *
     * @param roninServer the Ronin server that hosts the simulation and
     * receives this query.
     * @return a ListVehiclesIdsServerResponse to send to the Client.
     */
    @Override
    public ListVehiclesIdsServerResponse execute(RoninServer roninServer) {
        List<Vehicle> vehList = roninServer.getSimulation().getNetwork().getCurrentStepLoadedVehicles();
        List<String> idsList = new LinkedList<>();
        vehList.stream().forEach((v) -> {
            idsList.add(v.getId());
        });
        return new ListVehiclesIdsServerResponse(ServerResponse.StatusResponse.STATUS_SUCCESS,
                "The query has been processed successfully.", idsList);
    }

    @Override
    public ListVehiclesIdsServerResponse getResponse() {
        if (response == null) {
            return new ListVehiclesIdsServerResponse(ServerResponse.StatusResponse.STATUS_NO_RESPONSE,
                    "There is no response associated to this Query.", null);
        }
        return (ListVehiclesIdsServerResponse) response;
    }

}
