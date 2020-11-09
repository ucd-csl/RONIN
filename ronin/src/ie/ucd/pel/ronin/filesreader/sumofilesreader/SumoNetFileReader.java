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
package ie.ucd.pel.ronin.filesreader.sumofilesreader;

import ie.ucd.pel.ronin.filesreader.AbstractFileReader;
import ie.ucd.pel.ronin.model.Edge;
import ie.ucd.pel.ronin.model.Graph;
import ie.ucd.pel.ronin.model.Node;
import ie.ucd.pel.ronin.utils.XmlParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Come CACHARD
 *
 * Class used to read information about the network configuration, i.e. the
 * junctions and the edges, from sumo file .net.xml.
 */
public class SumoNetFileReader extends AbstractFileReader {

    /**
     * Attribute used for giving output to the user during the execution of the
     * program. But if it is set to true, the performances will decrease.
     */
    private final boolean PRINT_ESTIMATION_TIME = false;

    /**
     * SUMO tag name for Edge element.
     */
    public final String ELEMENT_EDGE = "edge";

    /**
     * SUMO tag name for Junction element.
     */
    public final String ELEMENT_JUNCTION = "junction";

    /**
     * SUMO tag name for Lane element.
     */
    public final String ELEMENT_LANE = "lane";

    /**
     * SUMO tag name for id of SUMO objects.
     */
    public final String ATTRIBUTE_ID = "id";

    /**
     * SUMO tag name for attribute type for SUMO objects like Junction, Vehicle
     * and Edge.
     */
    public final String ATTRIBUTE_TYPE = "type";

    /**
     * SUMO tag name for Junction's attribute x.
     */
    public final String ATTRIBUTE_X = "x";

    /**
     * SUMO tag name for Junction's attribute y.
     */
    public final String ATTRIBUTE_Y = "y";

    /**
     * SUMO tag name for Edge's attribute from.
     */
    public final String ATTRIBUTE_FROM = "from";

    /**
     * SUMO tag name for Edge's attribute to.
     */
    public final String ATTRIBUTE_TO = "to";

    /**
     * SUMO tag name for Edge's attribute priority.
     */
    public final String ATTRIBUTE_PRIORITY = "priority";

    /**
     * SUMO tag name for Lane's attribute speed.
     */
    public final String ATTRIBUTE_SPEED = "speed";

    /**
     * SUMO tag name for Lane and vType's attribute length.
     */
    public final String ATTRIBUTE_LENGTH = "length";

    /**
     * SUMO tag name for Lane's attribute shape.
     */
    public final String ATTRIBUTE_SHAPE = "shape";

    /**
     * SUMO tag name for Route's attribute edges.
     */
    public final String ATTRIBUTE_EDGES = "edges";

    /**
     * Constructs and initializes a SumoNetFileReader, able to read sumo files
     * .net.xml.
     *
     * @param filePath the path to the file to read
     */
    public SumoNetFileReader(String filePath) {
        super(filePath, "net.xml");
    }

    /**
     * Reads the graph network from SUMO File, constructing and initializing the
     * Nodes and the Edges of the Graph.
     *
     * @param graph the network graph to fill
     */
    public void readGraph(Graph graph) {
        Document doc = XmlParser.getDocDomElement(filePath);

        if (doc == null) {
            throw new RuntimeException("The DOM Document element of the file at location " + filePath + " is null.");
        }

        NodeList edgesXmlNodeList = doc.getElementsByTagName(ELEMENT_EDGE);
        NodeList junctionsXmlNodeList = doc.getElementsByTagName(ELEMENT_JUNCTION);

        readNodes(junctionsXmlNodeList, graph);
        readEdges(edgesXmlNodeList, graph);

    }

    /**
     * Constructs and initializes the Nodes in the Graph of the Network
     *
     * @param junctionsXmlNodeList a list of the xml elements corresponding to
     * the Nodes to construct
     * @param graph the Network Graph where we want to constructs the Nodes
     */
    private void readNodes(NodeList junctionsXmlNodeList, Graph graph) {
        long tStart = System.currentTimeMillis();
        final int listLength = junctionsXmlNodeList.getLength();
        for (int i = 0; i < listLength; i++) {
            org.w3c.dom.Node junctionXmlNode = junctionsXmlNodeList.item(i);
            Element junctionXmlElement = (Element) junctionXmlNode;
            String id;
            Double x, y;

            id = junctionXmlElement.getAttribute(ATTRIBUTE_ID);
            if (id.isEmpty()) {
                throw new RuntimeException("The id of a junction object is empty in file " + filePath + " .");
            }

            try {
                x = Double.parseDouble(junctionXmlElement.getAttribute(ATTRIBUTE_X));
                y = Double.parseDouble(junctionXmlElement.getAttribute(ATTRIBUTE_Y));
            } catch (NumberFormatException e) {
                throw new RuntimeException("Coordinates of a junction object whose id is \"" + id + "\" are not valid in file " + filePath + " .");
            }

            graph.addNode(new Node(id, x, y));

            if (PRINT_ESTIMATION_TIME) {
                long tEnd = System.currentTimeMillis();
                System.out.print("Remaining time for Nodes : " + (int) (((tEnd - tStart) * listLength / (double) i - (tEnd - tStart))) / 1000 + "s ("
                        + (i * 10000 / listLength) / 100 + "%)           \r");
            }

        }
        long tEnd = System.currentTimeMillis();
        System.out.println("Nodes loaded in " + (tEnd - tStart) / 1000 + " seconds.");
    }

    /**
     * Constructs and initializes the Edges in the Network's Graph
     *
     * @param edgesXmlNodeList a list of the xml elements corresponding to the
     * Edges to construct
     * @param graph the Network graph where we want to constructs the Edges
     */
    private void readEdges(NodeList edgesXmlNodeList, Graph graph) {
        long tStart = System.currentTimeMillis();
        final int listLength = edgesXmlNodeList.getLength();
        for (int i = 0; i < listLength; i++) {
            org.w3c.dom.Node edgeXmlNode = edgesXmlNodeList.item(i);
            Element edgeXmlElement = (Element) edgeXmlNode;

            String idEdge, idNodeFrom, idNodeTo;
            Double length, speedLimit;
            int priority, capacity;
            Node startNode, endNode;

            idEdge = edgeXmlElement.getAttribute(ATTRIBUTE_ID);
            if (idEdge.isEmpty()) {
                throw new RuntimeException("The id of an Edge object is empty in file " + filePath + " .");
            }

            //if the id of the Edge begins with ":", it means that it is an 
            //internal edge that won't be considered by our parsing.
            if (idEdge.startsWith(":")) {
                continue;
            }

            idNodeFrom = edgeXmlElement.getAttribute(ATTRIBUTE_FROM);
            if (idNodeFrom.isEmpty()) {
                throw new RuntimeException("The id of the starting node of an Edge object whose id is \"" + idEdge + "\" is empty in file " + filePath + " .");
            }

            idNodeTo = edgeXmlElement.getAttribute(ATTRIBUTE_TO);
            if (idNodeTo.isEmpty()) {
                throw new RuntimeException("The id of the ending node of an Edge object whose id is \"" + idEdge + "\" is empty in file " + filePath + " .");
            }

            try {
                priority = Integer.parseInt(edgeXmlElement.getAttribute(ATTRIBUTE_PRIORITY));
            } catch (NumberFormatException e) {
                throw new RuntimeException("The priority of an Edge object whose id is \"" + idEdge + "\" is not valid in file " + filePath + " .");
            }

            //An Edge is composed of lanes in SUMO from which we can get the speed and length of the Edge
            NodeList lanesXmlNodeList = edgeXmlElement.getElementsByTagName(ELEMENT_LANE);
            if (lanesXmlNodeList.item(0).getNodeType() != org.w3c.dom.Node.ELEMENT_NODE) {
                throw new RuntimeException("Error importation while getting lane information for an Edge object  whose id is \"" + idEdge + "\" in file " + filePath + " .");
            }

            Element firstLaneXmlElement = (Element) lanesXmlNodeList.item(0);

            try {
                length = Double.parseDouble(firstLaneXmlElement.getAttribute(ATTRIBUTE_LENGTH));
            } catch (NumberFormatException e) {
                throw new RuntimeException("The length of a lane of an Edge object whose id is \"" + idEdge + "\" is not valid in file " + filePath + " .");
            }

            try {
                speedLimit = Double.parseDouble(firstLaneXmlElement.getAttribute(ATTRIBUTE_SPEED));
            } catch (NumberFormatException e) {
                throw new RuntimeException("The speed limit of a lane of an Edge object whose id is \"" + idEdge + "\" is not valid in file " + filePath + " .");
            }

            capacity = calculCarCapacity(lanesXmlNodeList.getLength(), length);

            //the nodes are supposed to be created before the edges.
            startNode = graph.getNode(idNodeFrom);
            if (startNode == null) {
                throw new RuntimeException("The starting Node with idNodeFrom \"" + idNodeFrom + "\" of an Edge whose id is \"" + idEdge + "\" is null in file " + filePath + " .");
            }

            endNode = graph.getNode(idNodeTo);
            if (endNode == null) {
                throw new RuntimeException("The ending Node with idNodeTo \"" + idNodeTo + "\" of an Edge whose id is \"" + idEdge + "\" is null in file " + filePath + " .");
            }

            //When we create the new Edge, we have to make the bound with the nodes.
            Edge edge = new Edge(idEdge, capacity, length, speedLimit, priority, startNode, endNode);
            endNode.addIngoingEdge(edge);
            startNode.addOutgoingEdge(edge);

            graph.addEdge(edge);

            if (PRINT_ESTIMATION_TIME) {
                long tEnd = System.currentTimeMillis();
                System.out.print("Remaining time for Edges : " + (int) (((tEnd - tStart) * listLength / (double) i - (tEnd - tStart))) / 1000 + "s ("
                        + (i * 10000 / listLength) / 100 + "%)           \r");
            }
        }

        long tEnd = System.currentTimeMillis();
        System.out.println("Edges loaded in " + (tEnd - tStart) / 1000 + " seconds.");
    }

    /**
     * Returns the car capacity of an Edge based on the number of its Lanes and
     * its size.
     *
     * @param nbLanes the number of lanes of the Edge we want to know the car
     * capacity
     * @param length the length of the Edge we want to know the car capacity
     * @return the car capacity of an Edge
     */
    private int calculCarCapacity(int nbLanes, Double length) {
        //we made the simplification that the average size of a car is 4 meters.
        int carLength = 4;
        Double capacityPerLane = length / carLength;
        int capacity = capacityPerLane.intValue() * nbLanes;
        return capacity;
    }

}
