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

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Come CACHARD
 *
 * Model class that describes a traffic road intersection.
 */
public class Node implements Serializable {

    /**
     * The id of this Node.
     */
    private final String id;

    /**
     * The X coordinate of this Node.
     */
    private final Double x;

    /**
     * The Y coordinate of this Node.
     */
    private final Double y;

    /**
     * The map of Edges that start from this Node. The key is the Edge's id.
     */
    private final Map<String, Edge> outgoingEdges;

    /**
     * The map of Edges that end to this Node. The key is the Node's id.
     */
    private final Map<String, Edge> ingoingEdges;

    /**
     * Constructs and initializes a Node with the specified properties.
     *
     * @param id the id of the newly constructed Node
     * @param x the X-coordinate of the newly constructed Node
     * @param y the Y-coordinate of the newly constructed Node
     */
    public Node(String id, Double x, Double y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.outgoingEdges = new HashMap<>();
        this.ingoingEdges = new HashMap<>();
    }

    /**
     * Returns the id of this Node.
     *
     * @return the id of this Node
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the X coordinate of this Node.
     *
     * @return the X coordinate of this Node
     */
    public Double getX() {
        return x;
    }

    /**
     * Returns the Y coordinate of this Node.
     *
     * @return the Y coordinate of this Node
     */
    public Double getY() {
        return y;
    }

    /**
     * Returns a non modifiable map of Edges that start at this Node.
     *
     * @return a non modifiable map of Edges that start at this Node
     */
    public Map<String, Edge> getOutgoingEdges() {
        return Collections.unmodifiableMap(outgoingEdges);
    }

    /**
     * Returns a non modifiable map of Edges that end at this Node.
     *
     * @return a non modifiable map of Edges that end at this Node.
     */
    public Map<String, Edge> getIngoingEdges() {
        return Collections.unmodifiableMap(ingoingEdges);
    }

    /**
     * Adds an Edge to the map of ingoing Edges of this Graph. If the Edge is
     * already in the map, we do nothing.
     *
     * @param e the Edge we want to add
     * @return false if the edge was null and not added to the Edges map, true
     * else
     */
    public boolean addIngoingEdge(Edge e) {
        if (e == null) {
            return false;
        }
        ingoingEdges.putIfAbsent(e.getId(), e);
        return true;
    }

    /**
     * Adds an Edge to the map of outgoing Edges of this Graph. If the Edge is
     * already in the map, we do nothing.
     *
     * @param e the Edge we want to add
     * @return false if the edge was null and not added to the Edges map, true
     * else
     */
    public boolean addOutgoingEdge(Edge e) {
        if (e == null) {
            return false;
        }
        outgoingEdges.putIfAbsent(e.getId(), e);
        return true;
    }

    /**
     * Returns the hashcode for this instance of Node.
     *
     * @return the hascode for this Node.
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.id);
        return hash;
    }

    /**
     * Determines wether or not two Nodes are equal. Two instances of Node are
     * equal if they have the same id.
     *
     * @param obj an object to be compared with this Node.
     * @return true if the object to be compared is an instance of Node and has
     * the same id; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Node other = (Node) obj;
        return Objects.equals(this.id, other.id);
    }

}
