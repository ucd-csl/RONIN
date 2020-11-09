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
package ie.ucd.pel.ronin.javafx.model;

import ie.ucd.pel.ronin.model.Edge;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Come CACHARD
 *
 * Model class used for java fx application. An Edge load object represents the
 * load of an edge at a specific timestep of a simulation. This object is only
 * used for the javafx GUI of Ronin.
 */
public class EdgeLoad {

    /**
     * The idEdge of the Edge.
     */
    private final StringProperty idEdge;

    /**
     * The number of Vehicles that can be on this Edge at the same time.
     */
    private final IntegerProperty load;

    /**
     * Constructs and initializes a default javaFx Edgeload.
     */
    public EdgeLoad() {
        this.idEdge = new SimpleStringProperty("");
        this.load = new SimpleIntegerProperty(0);
    }

    /**
     * Constructs and initializes a javaFx Edge load.
     *
     * @param idEdge the idEdge of the edge
     * @param currentLoads the load in number of vehicles of the edge
     */
    public EdgeLoad(String idEdge, int currentLoads) {
        this.idEdge = new SimpleStringProperty(idEdge);
        this.load = new SimpleIntegerProperty(currentLoads);
    }

    /**
     * Returns a list of EdgeLoad whose load value is set to 0 with a list of
     * Edges to consider.
     *
     * @param edgesList a list of Edges to consider for the GUI
     * @return a list of EdgeLoad whose load value is set to 0 with a list of
     * Edges to consider
     */
    public static List<EdgeLoad> getDefaultEdgeLoadsList(Collection<Edge> edgesList) {
        List<EdgeLoad> edgeLoadsList = new LinkedList<>();
        edgesList.stream().forEach((Edge e) -> {
            edgeLoadsList.add(new EdgeLoad(e.getId(), 0));
        });
        return edgeLoadsList;
    }

    /**
     * Returns a String containing the id of the Edge that is concerned by this
     * EdgeLoad.
     *
     * @return a String containing the id of the Edge that is concerned by this
     * EdgeLoad.
     */
    public String getIdEdge() {
        return idEdge.get();
    }

    /**
     * Returns the StringProperty describing the id of the Edge concerned by
     * this EdgeLoad.
     *
     * @return the StringProperty describing the id of the Edge concerned by
     * this EdgeLoad
     */
    public StringProperty idEdgeProperty() {
        return idEdge;
    }

    /**
     * Retuns the load, i.e. the number of vehicles, on the Edge concerned by
     * this EdgeLoad.
     *
     * @return the load, i.e. the number of vehicles, on the Edge concerned by
     * this EdgeLoad
     */
    public int getLoad() {
        return load.get();
    }

    /**
     * Returns the IntegerProperty describing the load of the Edge concerned by
     * this EdgeLoad.
     *
     * @return the IntegerProperty describing the load of the Edge concerned by
     * this EdgeLoad
     */
    public IntegerProperty loadProperty() {
        return load;
    }

    /**
     * Sets a new value for the load of the Edge concerned by this EdgeLoad.
     *
     * @param load the new value for the load of the Edge concerned by this
     * EdgeLoad
     */
    public void setLoad(int load) {
        this.load.set(load);
    }

}
