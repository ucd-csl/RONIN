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
import ie.ucd.pel.ronin.communication.serverresponse.vehicleresponse.VehicleServerResponse;
import ie.ucd.pel.ronin.communication.socket.RoninServer;
import ie.ucd.pel.ronin.model.Vehicle;

/**
 *
 * @author Come CACHARD
 *
 * Class describing a Client query asking to return a specific vehicle of a
 * Ronin Simulation hosted by a Ronin server. The properties of the vehicle such
 * as position and speed are for the current time step of the simulation.
 */
public class GetVehicleQuery extends Query {

    /**
     * The id of the vehicle to get.
     */
    private final String idVehicle;

    /**
     * Constructs and initializes a Query that search a Vehicle.
     *
     * @param idVehicle the id of the vehicle to return
     */
    public GetVehicleQuery(String idVehicle) {
        super();
        this.idVehicle = idVehicle;
    }

    /**
     * Executes the query of the client by returning the vehicle of a Ronin
     * Simulation hosted by a Ronin server. It returns a VehicleServerResponse
     * to send to the Client, that contains the vehicle. The properties of the
     * vehicle such as position and speed are for the current time step of the
     * simulation.
     *
     * @param roninServer the Ronin server that hosts the simulation and
     * receives this query.
     * @return a VehicleServerResponse to send to the Client.
     */
    @Override
    public VehicleServerResponse execute(RoninServer roninServer) {
        Vehicle v = roninServer.getSimulation().getNetwork().getVehicle(idVehicle);
        if (v == null) {
            return new VehicleServerResponse(ServerResponse.StatusResponse.STATUS_FAILED,
                    "The vehicle is not found for the requested id " + idVehicle + ".", v);
        }
        return new VehicleServerResponse(ServerResponse.StatusResponse.STATUS_SUCCESS,
                "The query has been processed successfully.", v);
    }

    @Override
    public boolean checkQueryParameters() {
        if (idVehicle == null || idVehicle.isEmpty()) {
            this.setResponse(new VehicleServerResponse(ServerResponse.StatusResponse.STATUS_FAILED,
                    "Warning : the id of the vehicle to look for is not valid.", null));
            return false;
        }
        return true;
    }

    @Override
    public VehicleServerResponse getResponse() {
        if (response == null) {
            return new VehicleServerResponse(ServerResponse.StatusResponse.STATUS_NO_RESPONSE,
                    "There is no response associated to this Query.", null);
        }
        return (VehicleServerResponse) response;
    }

}
