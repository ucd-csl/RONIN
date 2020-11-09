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
package ie.ucd.pel.ronin.filesreader.roninfilesreader;

import ie.ucd.pel.ronin.javafx.model.StepLoads;
import ie.ucd.pel.ronin.filesreader.AbstractFileReader;
import ie.ucd.pel.ronin.utils.XmlParser;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Come CACHARD
 *
 * Class used to read a step file of a light loads matrix produced by Ronin.
 */
public class LightLoadsMatrixStepReader extends AbstractFileReader {

    /**
     * The xml tag name for the element that will contain the loads for every
     * edge for one step.
     */
    private final String ELEMENT_TIME_STEP = "step";

    /**
     * The xml tag name for the attribute of a step that indicate the timeslot
     * of the step.
     */
    private final String ATTRIBUTE_TIME_SLOT = "timeSlot";

    /**
     * The xml tag name for the element that will contain the informations about
     * an edge.
     */
    private final String ELEMENT_EDGE = "edge";

    /**
     * The xml tag name for the attribute that contains the id of elements.
     */
    private final String ATTRIBUTE_ID = "id";

    /**
     * The xml tag name for the element that describes the load in an edge.
     */
    private final String ATTRIBUTE_LOAD = "load";

    /**
     * Constructs and initializes a LightLoadsMatrixStepReader that can read a
     * file step of a matrix of loads produced by Ronin.
     *
     * @param filePath the path to a file step of a matrix of loads produced by
     * Ronin
     */
    public LightLoadsMatrixStepReader(String filePath) {
        super(filePath, "llm.xml", "lm.xml");
    }

    /**
     * Reads the graph network from SUMO File, constructing and initializing the
     * Nodes and the Edges of the Graph.
     *
     * @return a StepLoads object that describes the map of loads per Edge for a
     * specific step
     */
    public StepLoads readStepLoads() {
        Document doc = XmlParser.getDocDomElement(filePath);

        if (doc == null) {
            throw new RuntimeException("The DOM Document element of the file at location " + filePath + " is null.");
        }

        NodeList edgesXmlNodeList = doc.getElementsByTagName(ELEMENT_EDGE);
        NodeList stepNodeList = doc.getElementsByTagName(ELEMENT_TIME_STEP);

        return new StepLoads(readTimeSlot(stepNodeList), readLoadPerEdge(edgesXmlNodeList));
    }

    /**
     * Reads and returns a map of load per Edge.
     *
     * @param edgesXmlNodeList the nodelist of xml edges nodes
     * @return a map of load per Edge
     */
    private Map<String, Double> readLoadPerEdge(NodeList edgesXmlNodeList) {
        Map<String, Double> loadPerEdge = new HashMap<>();

        final int listLength = edgesXmlNodeList.getLength();
        for (int i = 0; i < listLength; i++) {
            org.w3c.dom.Node edgeXmlNode = edgesXmlNodeList.item(i);
            Element edgeXmlElement = (Element) edgeXmlNode;

            String idEdge;
            double load;

            idEdge = edgeXmlElement.getAttribute(ATTRIBUTE_ID);
            if (idEdge.isEmpty()) {
                throw new RuntimeException("The id of an Edge object is empty in file " + filePath + " .");
            }

            //if the id of the Edge begins with ":", it means that it is an 
            //internal edge that won't be considered by our parsing.
            if (idEdge.startsWith(":")) {
                continue;
            }

            try {
                load = Double.parseDouble(edgeXmlElement.getAttribute(ATTRIBUTE_LOAD));
            } catch (NumberFormatException e) {
                throw new RuntimeException("The load of an Edge object whose id is \"" + idEdge + "\" is not valid in file " + filePath + " .");
            }

            loadPerEdge.put(idEdge, load);
        }
        return loadPerEdge;
    }

    /**
     * Reads and returns the timeslot of the read step file in seconds.
     *
     * @param stepXmlNodeList the nodelist of xml step node
     * @return the timeslot of the read step file in seconds.
     */
    private double readTimeSlot(NodeList stepXmlNodeList) {

        org.w3c.dom.Node stepXmlNode = stepXmlNodeList.item(0);
        Element stepXmlElement = (Element) stepXmlNode;

        double timeslot;

        try {
            timeslot = Double.parseDouble(stepXmlElement.getAttribute(ATTRIBUTE_TIME_SLOT));
        } catch (NumberFormatException e) {
            throw new RuntimeException("The timeslot in file " + filePath + " is invalid.");
        }

        return timeslot;
    }

}
