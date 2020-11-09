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

import ie.ucd.pel.ronin.utils.XmlParser;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.w3c.dom.Document;

/**
 *
 * @author Come CACHARD
 *
 * Abstract class for writing outputs in xml files.
 */
public abstract class AbstractOutputWriter {

    /**
     * The path to the directory of the output of this Writer.
     */
    private final String outputDirectoryPath;

    /**
     * The base name of our output file.
     */
    private final String outputBaseFileName;

    /**
     * The body name of our output file.
     */
    private final String outputBodyFileName;

    /**
     * The extension of the output file without the first dot.
     */
    private final String outputExtension;

    /**
     * Constructs and initializes an AbstractOutputWriter. If the output
     * directory of this writer does not exist, we create it.
     *
     * @param globalOutputsDirectoryPath the path to the global outputs
     * directory of this simulation.
     * @param outputDirectoryName the name of the output directory of this
     * writer. If there is no name provided, the output will be written in the
     * global outputs directory.
     * @param outputBaseFileName the base name of our output file.
     * @param outputBodyFileName the body name of our output file.
     * @param outputExtension the extension of the output file without the first
     * dot.
     */
    public AbstractOutputWriter(String globalOutputsDirectoryPath, String outputDirectoryName, String outputBaseFileName, String outputBodyFileName, String outputExtension) {
        this.outputBaseFileName = outputBaseFileName;
        this.outputBodyFileName = outputBodyFileName;
        this.outputExtension = outputExtension;

        Path resultDirectoryPath = Paths.get(globalOutputsDirectoryPath, outputDirectoryName);
        File resultDirectory = new File(resultDirectoryPath.toString());
        if (!resultDirectory.exists()) {
            resultDirectory.mkdirs();
        }
        this.outputDirectoryPath = resultDirectoryPath.toString();
    }

    /**
     * Returns the output file path for the file where we will write our output.
     *
     * @return the output file path for the file where we will write our output.
     */
    private String getOutputFilePath() {
        StringBuilder fileNameSb = new StringBuilder();

        String extension;
        if (outputExtension.startsWith(".")) {
            extension = outputExtension;
        } else {
            extension = "." + outputExtension;
        }

        String bodyFileName = "";
        if (!outputBodyFileName.isEmpty() && outputBodyFileName != null) {
            bodyFileName = "_" + outputBodyFileName;
        }

        fileNameSb.append(outputBaseFileName).append(bodyFileName).append(extension);

        Path path = Paths.get(outputDirectoryPath, fileNameSb.toString());
        return path.toString();
    }

    /**
     * Returns the path to the directory of the output of this Writer.
     *
     * @return the path to the directory of the output of this Writer.
     */
    public String getOutputDirectoryPath() {
        return outputDirectoryPath;
    }

    /**
     * Returns the base name of our output file.
     *
     * @return the base name of our output file
     */
    public String getOutputBaseFileName() {
        return outputBaseFileName;
    }

    /**
     * Returns the body name of our output file.
     *
     * @return the body name of our output file
     */
    public String getOutputBodyFileName() {
        return outputBodyFileName;
    }

    /**
     * Returns the extension of the output file of this writer without the first
     * dot.
     *
     * @return the extension of the output file of this writer without the first
     * dot
     */
    public String getOutputExtension() {
        return outputExtension;
    }

    /**
     * Writes the output in an xml file in the output directory of the
     * application.
     */
    public void writeOutputFile() {

        Document doc = getDocElement();
        File outputFile = new File(getOutputFilePath());
        XmlParser.writeDomDocIntoFile(doc, outputFile);

    }

    /**
     * Creates and returns the DOM document Element of our xml output file.
     *
     * @return the DOM document Element of our xml output file
     */
    protected abstract Document getDocElement();

}
