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

/**
 *
 * @author Come CACHARD
 *
 * Class describing the response of a Ronin server to the query of a client
 * asking to return a count of Vehicles of a Ronin simulation.
 */
public class CountVehiclesServerResponse extends ServerResponse {

    /**
     * The number of vehicles.
     */
    private final int count;

    /**
     * Creates a Server response to a query asking to return a count of vehicles
     * of a Ronin Simulation.
     *
     * @param status the status of the execution of the query : if it has
     * succeeded or failed, or not implemented feature
     * @param description the description associated to the status of the
     * response
     * @param count the number of vehicles asked by the query
     */
    public CountVehiclesServerResponse(StatusResponse status, String description, int count) {
        super(status, description);
        this.count = count;
    }

    /**
     * Returns the number of Vehicles asked by the Query.
     *
     * @return the number of Vehicles asked by the Query
     */
    public final int getCount() {
        return count;
    }

}
