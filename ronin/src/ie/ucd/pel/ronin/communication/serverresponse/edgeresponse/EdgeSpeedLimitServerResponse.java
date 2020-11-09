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
package ie.ucd.pel.ronin.communication.serverresponse.edgeresponse;

import ie.ucd.pel.ronin.communication.serverresponse.ServerResponse;

/**
 *
 * @author Come CACHARD
 *
 * Class describing the response of a Ronin server to the query of a client
 * asking to return the speedLimit of an edge in m/s.
 */
public class EdgeSpeedLimitServerResponse extends ServerResponse {

    /**
     * The speedLimit of an edge in m/s.
     */
    private final double speedLimit;

    /**
     * Creates a Server response to a query asking to return the speedLimit of
     * an edge in m/s.
     *
     * @param status the status of the execution of the query : if it has
     * succeeded or failed, or not implemented feature
     * @param description the description associated to the status of the
     * response
     * @param speedLimit the speedLimit of an edge in m/s
     */
    public EdgeSpeedLimitServerResponse(StatusResponse status, String description, double speedLimit) {
        super(status, description);
        this.speedLimit = speedLimit;
    }

    /**
     * Returns the speedLimit of an edge in m/s.
     *
     * @return the speedLimit of an edge in m/s
     */
    public double getSpeedLimit() {
        return speedLimit;
    }

}
