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
import java.util.Map;

/**
 *
 * @author Come CACHARD
 *
 * Class describing the response of a Ronin server to the query of a client
 * asking to return a map of Vehicles of a Ronin simulation mapped on a key
 * String that is the vehicle's id.
 */
public class MapVehiclesByIdServerResponse extends ServerResponse {

    /**
     * The map of Vehicles of a Ronin simulation mapped on a key String that is
     * the vehicle's id.
     */
    private final Map<String, Vehicle> map;

    /**
     * Creates a Server response to a query asking to return a map of vehicles
     * of a Ronin Simulation mapped on their ids.
     *
     * @param status the status of the execution of the query : if it has
     * succeeded or failed, or not implemented feature
     * @param description the description associated to the status of the
     * response
     * @param map the he map of Vehicles of a Ronin simulation mapped on a key
     * String that is the vehicle's id
     */
    public MapVehiclesByIdServerResponse(StatusResponse status, String description, Map<String, Vehicle> map) {
        super(status, description);
        this.map = map;
    }

    /**
     * Returns a non modifiable view of the map of Vehicles of a Ronin
     * simulation mapped on a key String that is the vehicle's id.
     *
     * @return a non modifiable view of the map of Vehicles of a Ronin
     * simulation mapped on a key String that is the vehicle's id
     */
    public Map<String, Vehicle> getMap() {
        return Collections.unmodifiableMap(map);
    }

}
