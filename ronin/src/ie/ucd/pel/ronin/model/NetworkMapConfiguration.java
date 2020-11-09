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
package ie.ucd.pel.ronin.model;

/**
 *
 * @author Come CACHARD
 *
 * Class used to store information about the map of the network for the GUI.
 */
public class NetworkMapConfiguration {

    /**
     * The value of the minimal X_coordinate.
     */
    private double min_X;

    /**
     * The value of the maximum X_coordinate.
     */
    private double max_X;

    /**
     * The value of the minimal Y_coordinate.
     */
    private double min_Y;

    /**
     * The value of the maximum Y_coordinate.
     */
    private double max_Y;

    /**
     * Constructs and initializes a network map configuration for the GUI.
     *
     * @param min_X the minimal X_coordinate
     * @param max_X the maximum X_coordinate
     * @param min_Y the minimal Y_coordinate
     * @param max_Y the maximum Y_coordinate
     */
    public NetworkMapConfiguration(double min_X, double max_X, double min_Y, double max_Y) {
        this.min_X = min_X;
        this.max_X = max_X;
        this.min_Y = min_Y;
        this.max_Y = max_Y;
    }

    /**
     * Returns the minimal X_coordinate.
     *
     * @return the minimal X_coordinate
     */
    public double getMin_X() {
        return min_X;
    }

    /**
     * Returns the maximum X_coordinate.
     *
     * @return the maximum X_coordinate
     */
    public double getMax_X() {
        return max_X;
    }

    /**
     * Returns the minimal Y_coordinate.
     *
     * @return the minimal Y_coordinate
     */
    public double getMin_Y() {
        return min_Y;
    }

    /**
     * Returns the maximum Y_coordinate.
     *
     * @return the maximum Y_coordinate
     */
    public double getMax_Y() {
        return max_Y;
    }

    /**
     * Return the width of the map of the network in meter.
     *
     * @return the width of the map of the network in meter
     */
    public double getMapWidth() {
        return max_X - min_X;
    }

    /**
     * Return the height of the map of the network in meter.
     *
     * @return the height of the map of the network in meter
     */
    public double getMapHeight() {
        return max_Y - min_Y;
    }

    /**
     * Sets the new value of the minimal X_coordinate.
     *
     * @param min_X the new value of the minimal X_coordinate
     */
    public void setMin_X(double min_X) {
        this.min_X = min_X;
    }

    /**
     * Sets the new value of the maximum X_coordinate.
     *
     * @param max_X the new value of the maximum X_coordinate
     */
    public void setMax_X(double max_X) {
        this.max_X = max_X;
    }

    /**
     * Sets the new value of the minimal Y_coordinate.
     *
     * @param min_Y the new value of the minimal Y_coordinate
     */
    public void setMin_Y(double min_Y) {
        this.min_Y = min_Y;
    }

    /**
     * Sets the new value of the maximum Y_coordinate.
     *
     * @param max_Y the new value of the maximum Y_coordinate
     */
    public void setMax_Y(double max_Y) {
        this.max_Y = max_Y;
    }

    /**
     * Checks that the given coordinates fit into the borders drawn by min_X,
     * max_X,min_Y,max_Y. If not, we update the values of the borders.
     *
     * @param X the X_coordinate to check
     * @param Y the Y_coordinate to check
     */
    public void checkAndSetBordersWithCoordinates(double X, double Y) {
        checkAndSetXBorders(X);
        checkAndSetYBorders(Y);
    }

    /**
     * Checks that given X is between max_X and min_X. Else we update min_X or
     * max_X.
     *
     * @param X the coordinate X to check.
     */
    public void checkAndSetXBorders(double X) {
        if (X < min_X) {
            min_X = X;
        }
        if (X > max_X) {
            max_X = X;
        }
    }

    /**
     * Checks that given Y is between max_Y and min_Y. Else we update min_Y or
     * max_Y.
     *
     * @param Y the coordinate Y to check.
     */
    public void checkAndSetYBorders(double Y) {
        if (Y < min_Y) {
            min_Y = Y;
        }
        if (Y > max_Y) {
            max_Y = Y;
        }
    }

}
