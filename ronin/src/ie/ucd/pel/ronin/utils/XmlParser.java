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
package ie.ucd.pel.ronin.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author Come CACHARD
 *
 * Class used to give methods about parsing xml files.
 */
public class XmlParser {

    /**
     * The accepted file extensions without the dot for the xml parser.
     */
    private static final String[] ACCEPTED_XML_EXTENSIONS = {"xml", "sumocfg"};

    /**
     * Returns the normalized DOM Document Node of a given xml file
     *
     * @param xmlFilePath the path of the xml file we want to extract the
     * Document Node
     * @return the normalized DOM Document Node of the given xml file
     */
    public static Document getDocDomElement(String xmlFilePath) {
        try {
            File xmlFile = new File(xmlFilePath);

            //if the extension is not valid, we do nothing
            if (FileUtils.checkExtensionForFile(xmlFile, ACCEPTED_XML_EXTENSIONS) == false) {
                throw new RuntimeException("The extension of the given file " + xmlFilePath + " is not supported.");
            }

            if (!xmlFile.exists()) {
                throw new RuntimeException("The given file " + xmlFilePath + " is not found.");
            }

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbf.newDocumentBuilder();

            if (xmlFile.length() != 0) {
                Document doc = dBuilder.parse(xmlFile);
                doc.getDocumentElement().normalize();
                return doc;
            } else {
                throw new RuntimeException("The file " + xmlFilePath + " is empty.");
            }

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(XmlParser.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * Write a DOM document element into a file.
     *
     * @param doc the dom doc element we want to write in a file
     * @param outputFile the file where we want to write our output
     */
    public static void writeDomDocIntoFile(Document doc, File outputFile) {
        try {

            Source source = new DOMSource(doc);

            Result resultat = new StreamResult(new FileWriter(outputFile));

            TransformerFactory fabrique = TransformerFactory.newInstance();
            Transformer transformer = fabrique.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");

            transformer.transform(source, resultat);
        } catch (IllegalArgumentException | TransformerException e) {
            System.err.println("Error : cannot write dom into file " + outputFile.getPath() + ".");
        } catch (IOException ex) {
            Logger.getLogger(XmlParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
