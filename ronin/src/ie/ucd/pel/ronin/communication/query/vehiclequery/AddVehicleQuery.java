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
import ie.ucd.pel.ronin.model.Edge;
import ie.ucd.pel.ronin.model.Vehicle;
import ie.ucd.pel.ronin.model.VehicleType;
import java.util.Collection;

/**
 *
 * @author Come CACHARD
 *
 * Describes a Client's query that add a vehicle to the simulation hosted by a
 * Ronin server.
 */
public class AddVehicleQuery extends Query {

    /**
     * The id of the Vehicle to add.
     */
    private final String idVehicle;

    /**
     * The id of the type of the Vehicle to add.
     */
    private final String idVType;

    /**
     * The timestamp in seconds when the Vehicle to add starts its trip. If
     * equals -1, the vehicle starts at the step it was added.
     */
    private final Double departureTime;

    /**
     * The route of the Vehicle that is the list of ids of Edges where the
     * Vehicle will go through.
     */
    private final Collection<String> routeEdgesIds;

    /**
     * Constructs and initializes a Query for adding a Vehicle to a simulation.
     *
     * @param idVehicle the id of the vehicle to add
     * @param idVType the id of the type of the Vehicle to add
     * @param departureTime the timestamp in seconds when the Vehicle to add
     * starts its trip
     * @param routeEdgesIds the list of ids of the edges of the route of the
     * vehicle to add
     */
    public AddVehicleQuery(String idVehicle, String idVType, Double departureTime, Collection<String> routeEdgesIds) {
        super();
        this.idVehicle = idVehicle;
        this.idVType = idVType;
        this.departureTime = departureTime;
        this.routeEdgesIds = routeEdgesIds;
    }

    /**
     * Executes the query of the client by adding a vehicle to the simulation
     * hosted by the Ronin server. If the given attributes for the vehicle to
     * add are not correct, we do not add the vehicle. It returns a
     * ServerResponse to send to the Client.
     *
     * @param roninServer the Ronin server that hosts the simulation and
     * receives this query.
     * @return a ServerResponse to send to the Client.
     */
    @Override
    public ServerResponse execute(RoninServer roninServer) {
        VehicleType vType = roninServer.getSimulation().getNetwork().getVehicleType(idVType);
        if (vType == null) {
            return new ServerResponse(ServerResponse.StatusResponse.STATUS_FAILED, "Warning : the vehicle type " + idVType + " of the vehicle to add whose id is " + idVehicle + " is not found. So the vehicle has not been added.");
        }
        double vDepart = (departureTime < 0) ? roninServer.getSimulation().getCurrentTimeSlot() : departureTime;
        Vehicle v = new Vehicle(idVehicle, vDepart, vType);
        for (String idEdge : routeEdgesIds) {
            Edge e = roninServer.getSimulation().getNetwork().getEdge(idEdge);
            if (e == null) {
                return new ServerResponse(ServerResponse.StatusResponse.STATUS_FAILED, "Warning : the edge " + idEdge + " of the route of the vehicle to add whose id is " + idVehicle + " is not found. So the vehicle has not been added.");
            }
            v.addEdgeToRoute(e);
        }
        roninServer.getSimulation().getNetwork().addVehicleToLoadedVehicles(v);
        return new ServerResponse(ServerResponse.StatusResponse.STATUS_SUCCESS, "The adding of the vehicle " + idVehicle + " has been processed successfully.");
    }

    @Override
    public boolean checkQueryParameters() {
        if (idVehicle == null || idVehicle.isEmpty()) {
            this.setResponse(new ServerResponse(ServerResponse.StatusResponse.STATUS_FAILED, "Warning : the id of the vehicle to add is invalid. So the vehicle has not been added."));
            return false;
        }
        if (idVType == null || idVType.isEmpty()) {
            this.setResponse(new ServerResponse(ServerResponse.StatusResponse.STATUS_FAILED, "Warning : the id of the vehicle type of the vehicle to add whose id is " + idVehicle + "is invalid. So the vehicle has not been added."));
            return false;
        }
        if (this.departureTime == null) {
            this.setResponse(new ServerResponse(ServerResponse.StatusResponse.STATUS_FAILED, "Warning : the departure time of the vehicle to add whose id is " + idVehicle + " is null. So the vehicle has not been added."));
            return false;
        }
        if (this.routeEdgesIds == null || this.routeEdgesIds.isEmpty()) {
            this.setResponse(new ServerResponse(ServerResponse.StatusResponse.STATUS_FAILED, "Warning : the vehicle to add whose id is " + idVehicle + " has no route. So the vehicle has not been added."));
            return false;
        }
        return true;
    }

}
