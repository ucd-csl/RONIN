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
 * asking to return the length in m of an edge.
 */
public class EdgeLengthServerResponse extends ServerResponse {

    /**
     * The length in m of an edge.
     */
    private final double length;

    /**
     * Creates a Server response to a query asking to return the length in m of
     * an edge.
     *
     * @param status the status of the execution of the query : if it has
     * succeeded or failed, or not implemented feature
     * @param description the description associated to the status of the
     * response
     * @param length the length in m of an edge
     */
    public EdgeLengthServerResponse(StatusResponse status, String description, double length) {
        super(status, description);
        this.length = length;
    }

    /**
     * Returns the length in m of an edge.
     *
     * @return the length in m of an edge
     */
    public double getLength() {
        return length;
    }

}
