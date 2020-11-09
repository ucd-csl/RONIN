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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Come CACHARD
 *
 * Model class that describe a traffic road graph, i.e. edges (the roads) and
 * nodes (the road intersections)
 */
public class Graph {

    /**
     * The map of nodes that will represent the intersections of our network.
     * The key is the id of the Node.
     */
    private final Map<String, Node> nodes;

    /**
     * The map of edges that will represent the roads of our network. The key is
     * the id of the Edge.
     */
    private final Map<String, Edge> edges;

    /**
     * The configuration of the map of the network for GUI.
     */
    private NetworkMapConfiguration mapConfig;

    /**
     * Constructs and initializes a Graph with empty HashMaps for nodes and
     * edges.
     */
    public Graph() {
        nodes = new HashMap<>();
        edges = new HashMap<>();
        mapConfig = new NetworkMapConfiguration(0, 0, 0, 0);
    }

    /**
     * Returns a non modifiable map of Nodes that composed this Graph.
     *
     * @return a non modifiable map of Nodes that composed this Graph
     */
    public Map<String, Node> getNodes() {
        return Collections.unmodifiableMap(nodes);
    }

    /**
     * Returns a non modifiable map of Edges that composed this Graph.
     *
     * @return a non modifiable map of Edges that composed this Graph
     */
    public Map<String, Edge> getEdges() {
        return Collections.unmodifiableMap(edges);
    }

    /**
     * Returns the Node to which the specified key is mapped, or null if this
     * Graph contains no mapping for the key.
     *
     * @param idNode the id whose associated Node is to be returned
     * @return
     */
    public Node getNode(String idNode) {
        return nodes.get(idNode);
    }

    /**
     * Returns the Edge to which the specified key is mapped, or null if this
     * Graph contains no mapping for the key.
     *
     * @param idEdge the id whose associated Edge is to be returned
     * @return
     */
    public Edge getEdge(String idEdge) {
        return edges.get(idEdge);
    }

    /**
     * Returns the map configuration of the network.
     *
     * @return the map configuration of the network
     */
    public NetworkMapConfiguration getMapConfig() {
        return mapConfig;
    }

    /**
     * Adds an Edge to the map of Edges of this Graph. If the Edge is already in
     * the map, we do nothing.
     *
     * @param e the Edge we want to add.
     * @return false if the edge was null and not added to the Edges map, true
     * else
     */
    public boolean addEdge(Edge e) {
        if (e == null) {
            return false;
        }
        edges.putIfAbsent(e.getId(), e);

        return true;
    }

    /**
     * Adds a Node to the map of Nodes of this Graph. If the Node is already in
     * the map, we do nothing
     *
     * @param n the Node we want to add
     * @return false if the node was null and not added to the Nodes map, true
     * else
     */
    public boolean addNode(Node n) {
        if (n == null) {
            return false;
        }
        nodes.putIfAbsent(n.getId(), n);

        mapConfig.checkAndSetBordersWithCoordinates(n.getX(), n.getY());

        return true;
    }

    /**
     * Returns true if this graph is empty, so it contains no nodes and no
     * edges; false otherwise.
     *
     * @return true if this graph is empty, so it contains no nodes and no
     * edges; false otherwise
     */
    public boolean isEmpty() {
        return edges.isEmpty() && nodes.isEmpty();
    }

    /**
     * Clears the graph by removing all its edges and nodes.
     */
    public void clear() {
        nodes.clear();
        edges.clear();
        mapConfig = new NetworkMapConfiguration(0, 0, 0, 0);
    }

}
