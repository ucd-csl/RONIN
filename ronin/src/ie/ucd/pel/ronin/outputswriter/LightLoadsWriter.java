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
import java.util.Map;
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
 * Class used to write the number of vehicles per edge of one step of the
 * simulation in an xml file. The output folder contains a file for each step of
 * the simulation, and each step file contains for every Edge : - its id - the
 * number of Vehicles that were on the Edge at the considered step, i.e the load
 * of the Edge
 */
public class LightLoadsWriter extends AbstractOutputWriter {

    /**
     * If true, we do not write into the output file the edges that have a load
     * of 0.
     */
    private final boolean OPTIMIZE_WRITTING = true;

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
     * The loads per edge for one step of the simulation to write into a file.
     */
    private final Map< String, List<Vehicle>> loadsForOneStep;

    /**
     * The time slot corresponding to the loads in seconds.
     */
    private final double timeSlot;

    /**
     * The number of the step of the loads we want to write into a file.
     */
    private final int stepNumber;

    /**
     * Constructs and initializes a LoadsWriter with the loads of one step of
     * the simulation to write into a file and informations about the
     * simulation.
     *
     * @param globalOutputsDirectoryPath the path to the output directory of
     * this simulation.
     * @param outputBodyFileName the body name of the output file
     * @param loadsForOneStep the loads per edge for one step of the simulation
     * to write into a file
     * @param timeSlot the time slot corresponding to the loads in seconds
     * @param stepNumber the number of the step of the loads we want to write
     * into a file
     */
    public LightLoadsWriter(String globalOutputsDirectoryPath, String outputBodyFileName, Map<String, List<Vehicle>> loadsForOneStep, double timeSlot, int stepNumber) {
        super(globalOutputsDirectoryPath, "lightLoadsMatrix", "step_" + Integer.toString(stepNumber), outputBodyFileName, "llm.xml");
        this.loadsForOneStep = loadsForOneStep;
        this.timeSlot = timeSlot;
        this.stepNumber = stepNumber;
    }

    /**
     * Builds and returns the DOM node that represents a step for the
     * simulation.
     *
     * @return the DOM node that represents a step for the simulation.
     */
    @Override
    protected Document getDocElement() {
        DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder icBuilder;
        Document doc = null;

        try {
            icBuilder = icFactory.newDocumentBuilder();
            doc = icBuilder.newDocument();
            Element mainRootElement = doc.createElement(ELEMENT_TIME_STEP);
            mainRootElement.setAttribute(ATTRIBUTE_ID, Integer.toString(stepNumber));
            mainRootElement.setAttribute(ATTRIBUTE_TIME_SLOT, Double.toString(timeSlot));
            doc.appendChild(mainRootElement);
            appendChildEdgesNodesToStepElement(doc, mainRootElement);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(LightLoadsWriter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return doc;
    }

    /**
     * Creates and appends the child edges nodes to the step node.
     *
     * @param doc the DOM doc element that contains our xml content
     * @param stepElement the DOM node that represent a step
     */
    private void appendChildEdgesNodesToStepElement(Document doc, Element stepElement) {
        Element edgeElement;
        List<Vehicle> load;

        for (Map.Entry<String, List<Vehicle>> anItem : loadsForOneStep.entrySet()) {
            load = anItem.getValue();

            if (!load.isEmpty() || OPTIMIZE_WRITTING == false) {
                edgeElement = doc.createElement(ELEMENT_EDGE);
                edgeElement.setAttribute(ATTRIBUTE_ID, anItem.getKey());
                edgeElement.setAttribute(ATTRIBUTE_LOAD, Integer.toString(load.size()));

                stepElement.appendChild(edgeElement);
            }

        }

    }

}
