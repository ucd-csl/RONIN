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
package ie.ucd.pel.ronin.communication.serverresponse.vehicleresponse;

import ie.ucd.pel.ronin.communication.serverresponse.ServerResponse;
import ie.ucd.pel.ronin.model.Vehicle;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Come CACHARD
 *
 * Class describing the response of a Ronin server to the query of a client
 * asking to return a list of Vehicles of a Ronin simulation.
 */
public class ListVehiclesServerResponse extends ServerResponse {

    /**
     * The list of vehicles asked by the Query.
     */
    private final List<Vehicle> vehiclesList;

    /**
     * Creates a Server response to a query asking to return a list of vehicles
     * of a Ronin Simulation.
     *
     * @param status the status of the execution of the query : if it has
     * succeeded or failed, or not implemented feature
     * @param description the description associated to the status of the
     * response
     * @param vehiclesList the list of vehicles asked by the Query
     */
    public ListVehiclesServerResponse(StatusResponse status, String description, List<Vehicle> vehiclesList) {
        super(status, description);
        this.vehiclesList = vehiclesList;
    }

    /**
     * Returns a non modifiable view of the list of vehicles asked by the Query
     *
     * @return a non modifiable view of the list of vehicles asked by the Query
     */
    public List<Vehicle> getVehiclesList() {
        return Collections.unmodifiableList(vehiclesList);
    }

}
