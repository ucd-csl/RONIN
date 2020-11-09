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
import ie.ucd.pel.ronin.model.TimeConfiguration;
import ie.ucd.pel.ronin.utils.XmlParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Come CACHARD
 *
 * Class used to read configuration information about the simulation in sumo
 * file .sumocfg.
 */
public class SumoConfigFileReader extends AbstractFileReader {

    /**
     * SUMO tag name for element input in sumocfg file.
     */
    private final String ELEMENT_INPUT = "input";

    /**
     * SUMO tag name for element net file that contains the path to the sumonet
     * file in sumocfg file.
     */
    private final String ELEMENT_NET_FILE = "net-file";

    /**
     * SUMO tag name for element net file that contains the path to the sumorou
     * file in sumocfg file.
     */
    private final String ELEMENT_ROU_FILE = "route-files";

    /**
     * SUMO tag name for element time in sumocfg file.
     */
    private final String ELEMENT_TIME = "time";

    /**
     * SUMO tag name for element begin for time in sumocfg file.
     */
    private final String ELEMENT_BEGIN = "begin";

    /**
     * SUMO tag name for element end for time in sumocfg file.
     */
    private final String ELEMENT_END = "end";

    /**
     * SUMO tag name for element step-length for time in sumocfg file.
     */
    private final String ELEMENT_STEP_LENGTH = "step-length";

    /**
     * SUMO tag name for element traci_serverType that describes the
     * configuration of remote server.
     */
    private final String ELEMENT_TRACI_SERVER_TYPE = "traci_server";

    /**
     * SUMO tag name for element remote-port that contains the listening port of
     * the server.
     */
    private final String ELEMENT_REMOTE_PORT = "remote-port";

    /**
     * SUMO tag name for the attribute value.
     */
    private final String ATTRIBUTE_VALUE = "value";

    /**
     * Constructs and initializes a SumoConfigFileReader with the path to the
     * file to read.
     *
     * @param filePath the path to the sumo config file .sumocfg
     */
    public SumoConfigFileReader(String filePath) {
        super(filePath, "sumocfg");
    }

    /**
     * Returns the time configuration of the simulation.
     *
     * @return the time configuration of the simulation
     */
    public TimeConfiguration readSimulationTimeConfiguration() {
        double beginTime = 0.0;
        double endTime = -1.0;
        double stepLength = 1.0;

        Document doc = XmlParser.getDocDomElement(filePath);

        if (doc == null) {
            throw new RuntimeException("The DOM Document element of the file at location " + filePath + " is null.");
        }

        NodeList timeNodeList = doc.getElementsByTagName(ELEMENT_TIME);
        Element timeElement = (Element) timeNodeList.item(0);

        NodeList beginNodeList = timeElement.getElementsByTagName(ELEMENT_BEGIN);
        Element beginElement = (Element) beginNodeList.item(0);
        if (beginElement != null && !beginElement.getAttribute(ATTRIBUTE_VALUE).isEmpty()) {
            try {
                beginTime = Double.parseDouble(beginElement.getAttribute(ATTRIBUTE_VALUE));
            } catch (NumberFormatException e) {
                throw new RuntimeException("The beginning time of the simulation defined in file " + filePath + " is not valid.");
            }
        }

        NodeList endNodeList = timeElement.getElementsByTagName(ELEMENT_END);
        Element endElement = (Element) endNodeList.item(0);
        if (endElement != null && !endElement.getAttribute(ATTRIBUTE_VALUE).isEmpty()) {
            try {
                endTime = Double.parseDouble(endElement.getAttribute(ATTRIBUTE_VALUE));
            } catch (NumberFormatException e) {
                throw new RuntimeException("The end time of the simulation defined in file " + filePath + " is not valid.");
            }
        }

        NodeList stepLengthNodeList = timeElement.getElementsByTagName(ELEMENT_STEP_LENGTH);
        Element stepLengthElement = (Element) stepLengthNodeList.item(0);
        if (stepLengthElement != null && !stepLengthElement.getAttribute(ATTRIBUTE_VALUE).isEmpty()) {
            try {
                stepLength = Double.parseDouble(stepLengthElement.getAttribute(ATTRIBUTE_VALUE));
            } catch (NumberFormatException e) {
                throw new RuntimeException("The step length of the simulation defined in file " + filePath + " is not valid.");
            }
        }

        return new TimeConfiguration(beginTime, endTime, stepLength);
    }

    /**
     * Reads and returns the sumonet and sumorou file paths.
     *
     * @return an array containing in first position the sumonet file path and
     * in second position the sumorou file path
     */
    public String[] readSumoFilesInputsConfiguration() {
        String sumonetPath = "";
        String sumorouPath = "";

        Document doc = XmlParser.getDocDomElement(filePath);

        if (doc == null) {
            throw new RuntimeException("The DOM Document element of the file at location " + filePath + " is null.");
        }

        NodeList inputNodeList = doc.getElementsByTagName(ELEMENT_INPUT);
        Element inputElement = (Element) inputNodeList.item(0);

        NodeList netFileNodeList = inputElement.getElementsByTagName(ELEMENT_NET_FILE);
        Element netFileElement = (Element) netFileNodeList.item(0);
        if (netFileElement != null && !netFileElement.getAttribute(ATTRIBUTE_VALUE).isEmpty()) {
            sumonetPath = netFileElement.getAttribute(ATTRIBUTE_VALUE);
        }

        NodeList rouFileNodeList = inputElement.getElementsByTagName(ELEMENT_ROU_FILE);
        Element rouFileElement = (Element) rouFileNodeList.item(0);
        if (rouFileElement != null && !rouFileElement.getAttribute(ATTRIBUTE_VALUE).isEmpty()) {
            sumorouPath = rouFileElement.getAttribute(ATTRIBUTE_VALUE);
        }

        String[] result = {sumonetPath, sumorouPath};
        return result;
    }

    /**
     * Reads and returns the listening port of our Ronin Server.
     *
     * @return the listening port of our Ronin Server
     */
    public int readRoninServerPort() {
        int port = 0;

        Document doc = XmlParser.getDocDomElement(filePath);

        if (doc == null) {
            throw new RuntimeException("The DOM Document element of the file at location " + filePath + " is null.");
        }

        NodeList traciNodeList = doc.getElementsByTagName(ELEMENT_TRACI_SERVER_TYPE);
        Element traciElement = (Element) traciNodeList.item(0);

        NodeList portNodeList = traciElement.getElementsByTagName(ELEMENT_REMOTE_PORT);
        Element portFileElement = (Element) portNodeList.item(0);
        if (portFileElement != null && !portFileElement.getAttribute(ATTRIBUTE_VALUE).isEmpty()) {
            try {
                port = Integer.parseInt(portFileElement.getAttribute(ATTRIBUTE_VALUE));
            } catch (NumberFormatException e) {
                throw new RuntimeException("The listening port of Ronin server defined in " + ELEMENT_TRACI_SERVER_TYPE + " in file " + filePath + " is not valid.");
            }
        }

        return port;
    }

}
