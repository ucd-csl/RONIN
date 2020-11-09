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
package ie.ucd.pel.ronin.outputswriter;

import ie.ucd.pel.ronin.model.Edge;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Come CACHARD
 *
 * Class used to write global information about each edge in an xml file such as
 * the average density, the average traffic volume. It is a light version of
 * EdgeData output of SUMO. The file contains for every Edge : - its id - the
 * number of arrived vehicles on this Edge - its mean density in nbVehicles /
 * kilometer - its mean traffic volume in nbVehicles / hour - its mean travel
 * time in seconds - the mean speed of vehicles on this Edge in m/s
 */
public class EdgesWriter extends AbstractOutputWriter {

    /**
     * The xml tag name for the element that will contain the data.
     */
    private final String ELEMENT_MEAN_DATA = "meandata";

    /**
     * The xml tag name for the element that describes the interval of time of
     * the whole simulation.
     */
    private final String ELEMENT_INTERVAL = "interval";

    /**
     * The xml tag name for the attribute that contains the beginning time slot
     * of the simulation in seconds.
     */
    private final String ATTRIBUTE_BEGIN = "begin";

    /**
     * The xml tag name for the attribute that contains the end time slot of the
     * simulation in seconds.
     */
    private final String ATTRIBUTE_END = "end";

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
     * The xml tag name for the attribute that contains the average density in
     * nbVehicles/km of an edge.
     */
    private final String ATTRIBUTE_DENSITY = "density";

    /**
     * The xml tag name for the attribute that contains the average traffic
     * volume in nbVehicles/hour.
     */
    private final String ATTRIBUTE_TRAFFIC_VOLUME = "trafficVolume";

    /**
     * The xml tag name for the attribute that contains the average speed in
     * m/s.
     */
    private final String ATTRIBUTE_SPEED = "speed";

    /**
     * The xml tag name for the attribute that contains the average travel time
     * in seconds.
     */
    private final String ATTRIBUTE_TRAVEL_TIME = "traveltime";

    /**
     * The xml tag name for the attribute that contains the number of vehicles
     * that finished their trip on this Edge.
     */
    private final String ATTRIBUTE_ARRIVED = "arrived";

    /**
     * The map of the edges of the network.
     */
    private final Collection<Edge> edges;

    /**
     * The begin time slot of the simulation in seconds.
     */
    private final double beginTime;

    /**
     * The end time slot of the simulation in seconds.
     */
    private final double endTime;

    /**
     * The number of steps of the simulation.
     */
    private final double nbSteps;

    /**
     * Constructs and initializes a EdgesWriter with the loads of one step of
     * the simulation to write into a file and informations about the
     * simulation.
     *
     * @param globalOutputsDirectoryPath the path to the output directory of
     * this simulation.
     * @param outputName the body name of the output file
     * @param edges the collection of the edges of the network
     * @param beginTime the begin time slot in seconds of the simulation.
     * @param endTime the end time slot in seconds of the simulation
     * @param nbSteps the number of steps of the simulation
     */
    public EdgesWriter(final String globalOutputsDirectoryPath, final String outputName, final Collection<Edge> edges, final double beginTime, final double endTime, final double nbSteps) {
        super(globalOutputsDirectoryPath, "", "edgeData", outputName, "edd.xml");

        this.edges = edges;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.nbSteps = nbSteps;
    }

    /**
     * Builds and returns the DOM node that contains the edges data for the
     * simulation.
     *
     * @return the DOM node that contains the edges data for the simulation.
     */
    @Override
    protected Document getDocElement() {
        DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder icBuilder;
        Document doc = null;

        try {
            icBuilder = icFactory.newDocumentBuilder();
            doc = icBuilder.newDocument();
            Element mainRootElement = doc.createElement(ELEMENT_MEAN_DATA);
            doc.appendChild(mainRootElement);

            Element intervalElement = doc.createElement(ELEMENT_INTERVAL);
            intervalElement.setAttribute(ATTRIBUTE_BEGIN, Double.toString(beginTime));
            intervalElement.setAttribute(ATTRIBUTE_END, Double.toString(endTime));
            mainRootElement.appendChild(intervalElement);

            appendChildEdgesNodesToIntervalElement(doc, intervalElement);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(EdgesWriter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return doc;
    }

    /**
     * Creates and appends the child edges nodes to the interval node.
     *
     * @param doc the DOM doc element that contains our xml content
     * @param stepElement the DOM node that represent the interval of the whole
     * simulation
     */
    private void appendChildEdgesNodesToIntervalElement(Document doc, Element intervalElement) {
        Element edgeElement;

        for (Edge e : edges) {
            edgeElement = doc.createElement(ELEMENT_EDGE);
            edgeElement.setAttribute(ATTRIBUTE_ID, e.getId());
            edgeElement.setAttribute(ATTRIBUTE_ARRIVED, Double.toString(e.getArrivedVehicles()));
            edgeElement.setAttribute(ATTRIBUTE_DENSITY, Double.toString(e.getMeanDensity(nbSteps)));
            edgeElement.setAttribute(ATTRIBUTE_SPEED, Double.toString(e.getMeanSpeed(nbSteps)));
            edgeElement.setAttribute(ATTRIBUTE_TRAVEL_TIME, Double.toString(e.getMeanTravelTime(nbSteps)));
            edgeElement.setAttribute(ATTRIBUTE_TRAFFIC_VOLUME, Double.toString(e.getAverageTrafficVolume(nbSteps)));

            intervalElement.appendChild(edgeElement);
        }

    }

}
