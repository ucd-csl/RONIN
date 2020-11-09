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
import ie.ucd.pel.ronin.communication.socket.RoninServer;

/**
 *
 * @author Come CACHARD
 *
 * Class describing a Client query asking to remove a vehicle from a Ronin
 * Simulation hosted by a Ronin server.
 */
public class RemoveVehicleQuery extends Query {

    /**
     * The id of the vehicle to remove.
     */
    private final String idVehicle;

    /**
     * Constructs and initializes a query that asks to remove a vehicle from a
     * Ronin simulation
     *
     * @param idVehicle the id of the vehicle to remove
     */
    public RemoveVehicleQuery(String idVehicle) {
        super();
        this.idVehicle = idVehicle;
    }

    /**
     * Executes the query of the client by removing a vehicle from the
     * simulation hosted by the Ronin server. If the vehicle does not exist, we
     * do nothing and the execution successed. It returns a ServerResponse to
     * send to the Client.
     *
     * @param roninServer the Ronin server that hosts the simulation and
     * receives this query.
     * @return a ServerResponse to send to the Client.
     */
    @Override
    public ServerResponse execute(RoninServer roninServer) {
        roninServer.getSimulation().getNetwork().removeVehicle(idVehicle);
        //roninServer.getSimulation().getNetwork().addVehicleToRemoveFromSimulationList(idVehicle);
        return new ServerResponse(ServerResponse.StatusResponse.STATUS_SUCCESS, "The execution of removing vehicle " + idVehicle + " has been processed successfully.");
    }

    @Override
    public boolean checkQueryParameters() {
        if (idVehicle == null || idVehicle.isEmpty()) {
            this.setResponse(new ServerResponse(ServerResponse.StatusResponse.STATUS_FAILED, "Warning : the id of the vehicle to remove is invalid. So the vehicle has not been removed."));
            return false;
        }
        return true;
    }

}
