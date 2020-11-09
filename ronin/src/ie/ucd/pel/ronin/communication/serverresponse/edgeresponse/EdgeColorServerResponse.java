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
 * asking to return the color of an edge (green is few trafic and red is jam)
 * for the current time step of the simulation.
 */
public class EdgeColorServerResponse extends ServerResponse {

    /**
     * The blue component of the color of the Edge.
     */
    private final double blue;

    /**
     * The green component of the color of the Edge.
     */
    private final double green;

    /**
     * The red component of the color of the Edge.
     */
    private final double red;

    /**
     * The opacity of the color of the Edge.
     */
    private final double opacity;

    /**
     * Creates a Server response to a query asking to return the color of an
     * edge corresponding to its trafic for the current time step of the
     * simulation.
     *
     * @param status the status of the execution of the query : if it has
     * succeeded or failed, or not implemented feature
     * @param description the description associated to the status of the
     * response
     * @param blue the blue component of the color of the Edge
     * @param green the green component of the color of the Edge
     * @param red the red component of the color of the Edge
     * @param opacity the opacity of the color of the Edge
     */
    public EdgeColorServerResponse(StatusResponse status, String description,
            double blue, double green, double red, double opacity) {
        super(status, description);
        this.blue = blue;
        this.green = green;
        this.red = red;
        this.opacity = opacity;
    }

    /**
     * Returns the blue component of the color of the Edge.
     *
     * @return the blue component of the color of the Edge
     */
    public double getBlue() {
        return blue;
    }

    /**
     * Returns the green component of the color of the Edge.
     *
     * @return the green component of the color of the Edge
     */
    public double getGreen() {
        return green;
    }

    /**
     * Returns the red component of the color of the Edge.
     *
     * @return the red component of the color of the Edge
     */
    public double getRed() {
        return red;
    }

    /**
     * Returns the opacity of the color of the Edge.
     *
     * @return the opacity of the color of the Edge
     */
    public double getOpacity() {
        return opacity;
    }

}
