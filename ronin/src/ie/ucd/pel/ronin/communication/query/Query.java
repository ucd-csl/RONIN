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
package ie.ucd.pel.ronin.communication.query;

import ie.ucd.pel.ronin.communication.serverresponse.ServerResponse;
import ie.ucd.pel.ronin.communication.socket.RoninServer;
import java.io.Serializable;

/**
 *
 * @author Come CACHARD
 *
 * Class used to describe queries sent from a Client to interact with a Ronin
 * simulation hosted by a Ronin server during runtime.
 */
public abstract class Query implements Serializable {

    /**
     * The ServerResponse to this Query.
     */
    protected ServerResponse response;

    /**
     * Creates and initializes a query.
     */
    public Query() {
        this.response = null;
    }

    /**
     * Returns the ServerResponse associated to this Query.
     *
     * @return the ServerResponse associated to this Query
     */
    public ServerResponse getResponse() {
        if (response == null) {
            return new ServerResponse(ServerResponse.StatusResponse.STATUS_NO_RESPONSE,
                    "There is no response associated to this Query.");
        }
        return response;
    }

    /**
     * Sets the ServerResponse associated to this Query. This method should be
     * used only by the QueryLauncher.
     *
     * @param response the response received from the Ronin server to this Query
     */
    public void setResponse(ServerResponse response) {
        this.response = response;
    }

    /**
     * Executes the query of the Client and returns a response to send to the
     * Client. The type of the return of this method is the same that the method
     * checkQueryParameters().
     *
     * @param roninServer the Ronin server that communicate with the Client and
     * that hosts the simulation
     * @return a response to send to the Client
     */
    public abstract ServerResponse execute(RoninServer roninServer);

    /**
     * Checks that the attributes of the query given as parameters are valid.
     * Returns true if the parameters of the query are valid or if the query
     * doesnot need any parameters; otherwise we return false and set the
     * ServerResponse of this Query with a server response that contains the
     * cause of the non validity and in that case, the query won't be executed.
     *
     * @return true if the parameters of the query are valid; otherwise we
     * return false and set the ServerResponse of this Query with a server
     * response that contains the cause of the non validity.
     */
    public boolean checkQueryParameters() {
        return true;
    }

}
