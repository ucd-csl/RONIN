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
import ie.ucd.pel.ronin.communication.serverresponse.vehicleresponse.CountVehiclesServerResponse;
import ie.ucd.pel.ronin.communication.serverresponse.ServerResponse;
import ie.ucd.pel.ronin.communication.socket.RoninServer;

/**
 *
 * @author Come CACHARD
 *
 * Class describing a Client query asking to return the number of vehicles of a
 * Ronin Simulation hosted by a Ronin server.
 */
public class GetAllVehiclesCountQuery extends Query {

    /**
     * Executes the query of the client by returning the number of vehicles that
     * are in a Ronin Simulation hosted by a Ronin server. It returns a
     * CountVehiclesServerResponse to send to the Client, that contains the
     * count of vehicles.
     * We do not take in account the arrived vehicles.
     *
     * @param roninServer the Ronin server that hosts the simulation and
     * receives this query.
     * @return a CountVehiclesServerResponse to send to the Client.
     */
    @Override
    public CountVehiclesServerResponse execute(RoninServer roninServer) {
        return new CountVehiclesServerResponse(ServerResponse.StatusResponse.STATUS_SUCCESS,
                "The query has been processed successfully.",
                roninServer.getSimulation().getNetwork().getAllVehiclesOfSimulation().size()-roninServer.getSimulation().getNetwork().getArrivedVehicles().size());
    }

    @Override
    public CountVehiclesServerResponse getResponse() {
        if (response == null) {
            return new CountVehiclesServerResponse(ServerResponse.StatusResponse.STATUS_NO_RESPONSE,
                    "There is no response associated to this Query.", -1);
        }
        return (CountVehiclesServerResponse) response;
    }

}
