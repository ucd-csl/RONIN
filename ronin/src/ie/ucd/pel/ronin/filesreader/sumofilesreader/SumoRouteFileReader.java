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
import ie.ucd.pel.ronin.model.Network;
import ie.ucd.pel.ronin.model.Vehicle;
import ie.ucd.pel.ronin.model.VehicleType;
import ie.ucd.pel.ronin.utils.XmlParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Come CACHARD
 *
 * Class used to read the vehicles, the vehicle types and their routes from sumo
 * file .rou.xml.
 */
public class SumoRouteFileReader extends AbstractFileReader {

    /**
     * Attribute used for giving output to the user during the execution of the
     * program. But if it is set to true, the performances will decrease.
     */
    private final boolean PRINT_ESTIMATION_TIME = false;

    /**
     * SUMO tag name for Vehicle element.
     */
    public final String ELEMENT_VEHICLE = "vehicle";

    /**
     * SUMO tag name for Route element.
     */
    public final String ELEMENT_ROUTE = "route";

    /**
     * SUMO tag name for vType element.
     */
    public final String ELEMENT_VEHICLE_TYPE = "vType";

    /**
     * SUMO tag name for Lane and vType's attribute length.
     */
    public final String ATTRIBUTE_LENGTH = "length";

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
     * SUMO tag name for vType's attribute maxSpeed.
     */
    public final String ATTRIBUTE_MAX_SPEED = "maxSpeed";

    /**
     * SUMO tag name for Vehicle's attribute depart.
     */
    public final String ATTRIBUTE_DEPART = "depart";

    /**
     * SUMO tag name for Route's attribute edges.
     */
    public final String ATTRIBUTE_EDGES = "edges";

    /**
     * Constructs and initializes a SumoRouteFileReader, able to read the
     * vehicles from a sumo file .rou.xml
     *
     * @param filePath the path to the file to read. It must be a .rou.xml file.
     */
    public SumoRouteFileReader(String filePath) {
        super(filePath, "rou.xml");
    }

    /**
     * Reads the Vehicles from SUMO .rou File, constructing and initializing the
     * Vehicles of the Network and their routes . WARNING : the Graph of the
     * Network must be constructed before the Vehicles. WARNING : the vehicles
     * types must be read before reading the vehicles.
     *
     * @param network the network which will contain the Vehicles
     */
    public void readVehicles(Network network) {

        Document doc = XmlParser.getDocDomElement(filePath);

        if (doc == null) {
            throw new RuntimeException("The DOM Document element of the file at location " + filePath + " is null.");
        }

        NodeList vehiclesXmlNodeList = doc.getElementsByTagName(ELEMENT_VEHICLE);

        long tStart = System.currentTimeMillis();
        final int listLength = vehiclesXmlNodeList.getLength();
        for (int i = 0; i < listLength; i++) {
            org.w3c.dom.Node vehicleXmlNode = vehiclesXmlNodeList.item(i);
            Element vehicleXmlElement = (Element) vehicleXmlNode;
            String idVehicle, idVType;
            Double departureTime;

            idVehicle = vehicleXmlElement.getAttribute(ATTRIBUTE_ID);
            if (idVehicle.isEmpty()) {
                throw new RuntimeException("The id of a Vehicle object is empty in file at location " + filePath + ".");
            }

            idVType = vehicleXmlElement.getAttribute(ATTRIBUTE_TYPE);
            if (idVType.isEmpty()) {
                throw new RuntimeException("The id of the Vehicle Type of a Vehicle object whose id is \"" + idVehicle + "\" is empty.");
            }

            try {
                departureTime = Double.parseDouble(vehicleXmlElement.getAttribute(ATTRIBUTE_DEPART));
            } catch (NumberFormatException e) {
                throw new RuntimeException("The departure time of a Vehicle object whose id is \"" + idVehicle + "\" is not valid.");
            }

            NodeList routeXmlNodeList = vehicleXmlElement.getElementsByTagName(ELEMENT_ROUTE);
            if (routeXmlNodeList.item(0).getNodeType() != org.w3c.dom.Node.ELEMENT_NODE) {
                throw new RuntimeException("Error importation while getting route information for a Vehicle object whose id is \"" + idVehicle + "\" .");
            }

            //Constructs the Vehicle with the vehicleType got from the Network
            VehicleType vType = network.getVehicleType(idVType);
            if (vType == null) {
                throw new RuntimeException("The VehicleType with idVehicleType \"" + idVType + "\" of a Vehicle  whose idVehicle is \"" + idVehicle + "\" is null.");
            }
            Vehicle v = new Vehicle(idVehicle, departureTime, vType);

            //Load the route for the vehicle
            Element routeXmlElement = (Element) routeXmlNodeList.item(0);
            String[] idsEdges = routeXmlElement.getAttribute(ATTRIBUTE_EDGES).split(" ");

            for (String idEdge : idsEdges) {
                if (idEdge.isEmpty() == false) {
                    v.addEdgeToRoute(network.getEdge(idEdge));
                }
            }

            network.addVehicleToLoadedVehicles(v);

            if (PRINT_ESTIMATION_TIME) {
                long tEnd = System.currentTimeMillis();
                System.out.print("Remaining time for Nodes : " + (int) (((tEnd - tStart) * listLength / (double) i - (tEnd - tStart))) / 1000 + "s ("
                        + (i * 10000 / listLength) / 100 + "%)           \r");
            }

        }

        long tEnd = System.currentTimeMillis();
        System.out.println("Vehicles loaded in " + (tEnd - tStart) / 1000 + " seconds.");

    }

    /**
     * Reads the VehicleTypes that are considered for our simulation,
     * constructing and initializing the VehicleTypes of the Network. The
     * VehicleTypes can be written in the sumo rou file or in the sumo net file.
     * Normally, the vTypes should be in the sumo rou file
     *
     * @param network the Network which will contain the Vehicles
     */
    public void readVehicleTypes(Network network) {
        //In some custom files configuration, the vehicleType can be described in other files than sumo file .rou.xml
        String[] consideredFilesPaths = {filePath};

        long tStartReading = System.currentTimeMillis();

        for (String file : consideredFilesPaths) {
            Document doc = XmlParser.getDocDomElement(file);

            if (doc == null) {
                throw new RuntimeException("The DOM Document element of a file at location \"" + file + "\" is null.");
            }

            NodeList vehicleTypesXmlNodeList = doc.getElementsByTagName(ELEMENT_VEHICLE_TYPE);

            long tStart = System.currentTimeMillis();
            final int listLength = vehicleTypesXmlNodeList.getLength();
            for (int i = 0; i < listLength; i++) {
                org.w3c.dom.Node vehicleXmlNode = vehicleTypesXmlNodeList.item(i);
                Element vehicleXmlElement = (Element) vehicleXmlNode;
                String idVType;
                Double maxSpeed, length;

                idVType = vehicleXmlElement.getAttribute(ATTRIBUTE_ID);
                if (idVType.isEmpty()) {
                    throw new RuntimeException("The id of a VehicleType object in file at location \"" + file + "\" is empty.");
                }

                try {
                    maxSpeed = Double.parseDouble(vehicleXmlElement.getAttribute(ATTRIBUTE_MAX_SPEED));
                } catch (NumberFormatException e) {
                    throw new RuntimeException("The maximum speed of a vehicleType object whose id is \"" + idVType + "\" is not valid.");
                }

                try {
                    length = Double.parseDouble(vehicleXmlElement.getAttribute(ATTRIBUTE_LENGTH));
                } catch (NumberFormatException e) {
                    throw new RuntimeException("The length of a vehicleType object whose id is \"" + idVType + "\" is not valid.");
                }

                VehicleType vType = new VehicleType(idVType, length, maxSpeed);
                network.addVehicleType(vType);

                if (PRINT_ESTIMATION_TIME) {
                    long tEnd = System.currentTimeMillis();
                    System.out.print("Remaining time for Nodes : " + (int) (((tEnd - tStart) * listLength / (double) i - (tEnd - tStart))) / 1000 + "s ("
                            + (i * 10000 / listLength) / 100 + "%)           \r");
                }

            }

        }

        long tEndReading = System.currentTimeMillis();
        System.out.println("VehicleTypes loaded in " + (tEndReading - tStartReading) / 1000 + " seconds.");
    }

}
