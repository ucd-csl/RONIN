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

import ie.ucd.pel.ronin.model.Vehicle;
import java.util.List;
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
 * Class used to write global informations about the trips of vehicles. It is a
 * light version of tripinfos output of SUMO. The output file contains for each
 * vehicle : - its id - its departure time in seconds - the id of its departure
 * Edge - its arrival time in seconds - the id of its arrival Edge - the length
 * of its total route in meter - the duration in seconds of its trip - the
 * number of steps this Vehicle had to wait - the id of the vehicle type of this
 * Vehicle
 */
public class TripInfosWriter extends AbstractOutputWriter {

    /**
     * The xml tag name for the element that contains all the trip infos for the
     * whole simulation.
     */
    private final String ELEMENT_TRIP_INFOS = "tripinfos";

    /**
     * The xml tag name for the element that contains the information of a trip.
     */
    private final String ELEMENT_TRIP_INFO = "tripinfo";

    /**
     * The xml tag name for the attribute that contains the id of the vehicle of
     * the trip.
     */
    private final String ATTRIBUTE_ID = "id";

    /**
     * The xml tag name for the attribute that contains the departure time in
     * seconds of the trip.
     */
    private final String ATTRIBUTE_DEPART = "depart";

    /**
     * The xml tag name for the attribute that contains the id of the departure
     * edge of the trip.
     */
    private final String ATTRIBUTE_DEPART_EDGE = "departEdge";

    /**
     * The xml tag name for the attribute that contains the arrival time in
     * seconds of the trip.
     */
    private final String ATTRIBUTE_ARRIVAL = "arrival";

    /**
     * The xml tag name for the attribute that contains the id of the arrival
     * edge of the trip.
     */
    private final String ATTRIBUTE_ARRIVAL_EDGE = "arrivalEdge";

    /**
     * The xml tag name for the attribute that contains the duration of the trip
     * in seconds.
     */
    private final String ATTRIBUTE_DURATION = "duration";

    /**
     * The xml tag name for the attribute that contains the number of steps
     * where the vehicle cannot move.
     */
    private final String ATTRIBUTE_WAIT_STEPS = "waitSteps";

    /**
     * The xml tag name for the attribute that contains the length of the route
     * of the trip.
     */
    private final String ATTRIBUTE_ROUTE_LENGTH = "routeLength";

    /**
     * The xml tag name for the attribute that contains the type of the vehicle
     * of the trip.
     */
    private final String ATTRIBUTE_VEHICLE_TYPE = "vType";

    /**
     * The list of all the vehicles at the end of the simulation.
     */
    private final List<Vehicle> arrivedVehicles;

    /**
     * The duration in seconds of a timeslot.
     */
    private final double timeUnit;

    /**
     * Constructs and initializes a tripinfosWriter.
     *
     * @param globalOutputsDirectoryPath the path to the output directory of
     * this simulation
     * @param outputName the body name of the output file
     * @param timeUnit the duration in seconds of a timeslot
     * @param arrivedVehicles the list of all the vehicles at the end of the
     * simulation
     */
    public TripInfosWriter(String globalOutputsDirectoryPath, String outputName, Double timeUnit, List<Vehicle> arrivedVehicles) {
        super(globalOutputsDirectoryPath, "", "tripinfos", outputName, "ti.xml");

        this.arrivedVehicles = arrivedVehicles;
        this.timeUnit = timeUnit;

    }

    /**
     * Builds and returns the DOM node that contains the tripinfos for the
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
            Element mainRootElement = doc.createElement(ELEMENT_TRIP_INFOS);
            doc.appendChild(mainRootElement);

            appendChildTripinfoNodesToTripInfosRootElement(doc, mainRootElement);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(TripInfosWriter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return doc;
    }

    /**
     * Creates and appends the child tripinfo nodes to the tripinfos node.
     *
     * @param doc the DOM doc element that contains our xml content
     * @param stepElement the DOM node that represent the interval of the whole
     * simulation
     */
    private void appendChildTripinfoNodesToTripInfosRootElement(Document doc, Element tripInfosElement) {

        arrivedVehicles.stream().forEach(v -> {
            Element tripinfoElement = doc.createElement(ELEMENT_TRIP_INFO);
            tripinfoElement.setAttribute(ATTRIBUTE_ID, v.getId());
            tripinfoElement.setAttribute(ATTRIBUTE_DEPART, Double.toString(v.getDepartureTime()));
            tripinfoElement.setAttribute(ATTRIBUTE_DEPART_EDGE, v.getEdgeOfRouteAtPosition(0).getId());
            tripinfoElement.setAttribute(ATTRIBUTE_ARRIVAL, Double.toString(v.getArrivalTime(timeUnit)));
            tripinfoElement.setAttribute(ATTRIBUTE_ARRIVAL_EDGE, v.getEdgeOfRouteAtPosition(v.getRoute().size() - 1).getId());
            tripinfoElement.setAttribute(ATTRIBUTE_ROUTE_LENGTH, Double.toString(v.getRouteLength()));
            tripinfoElement.setAttribute(ATTRIBUTE_DURATION, Double.toString(v.getTravelTime(timeUnit)));
            tripinfoElement.setAttribute(ATTRIBUTE_WAIT_STEPS, Double.toString(v.getNbTotSlotsInSamePosition()));
            tripinfoElement.setAttribute(ATTRIBUTE_VEHICLE_TYPE, v.getvType());

            tripInfosElement.appendChild(tripinfoElement);

        });
    }

}
