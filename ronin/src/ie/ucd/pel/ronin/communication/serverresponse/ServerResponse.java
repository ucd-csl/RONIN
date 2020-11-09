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
package ie.ucd.pel.ronin.communication.serverresponse;

import java.io.Serializable;

/**
 *
 * @author Come CACHARD
 *
 * ServerResponse sent by the Ronin server to a query of its Client. If we want
 * to give special parameters in the response, we must create a class that
 * inherits of this Class.
 */
public class ServerResponse implements Serializable {

    /**
     * Enum describing the different status of responses.
     */
    public enum StatusResponse {
        /**
         * Query executed.
         */
        STATUS_SUCCESS,
        /**
         * Query failed.
         */
        STATUS_FAILED,
        /**
         * Query has not a response yet.
         */
        STATUS_NO_RESPONSE,
        /**
         * Query functionality not implemented.
         */
        STATUS_NOT_IMPLEMENTED;
    }

    /**
     * The status of this response.
     */
    private final StatusResponse status;

    /**
     * The description of the response.
     */
    private final String description;

    /**
     * Constructs and initializes a response from server.
     *
     * @param status the status of the execution of the query of the user.
     * @param description the description associated to the response.
     */
    public ServerResponse(StatusResponse status, String description) {
        this.status = status;
        this.description = description;
    }

    /**
     * Returns the status of this Response.
     *
     * @return the status of this Response
     */
    public final StatusResponse getStatus() {
        return status;
    }

    /**
     * Return the description associated to this Response.
     *
     * @return the description associated to this Response
     */
    public final String getDescription() {
        return description;
    }

}
