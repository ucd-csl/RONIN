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
package ie.ucd.pel.ronin.filesreader;

import ie.ucd.pel.ronin.utils.FileUtils;
import java.util.Arrays;

/**
 *
 * @author Come CACHARD
 *
 * Abstract class of a reader of file.
 */
public abstract class AbstractFileReader {

    /**
     * The path to the file to read.
     */
    protected final String filePath;

    /**
     * The file extensions accepted by the reader.
     */
    protected final String[] acceptedFileExtensions;

    /**
     * Constructs and initializes an AbstractFileReader.
     *
     * @param filePath the path to the file to read
     * @param acceptedFileExtensions the accepted file extensions by this
     * reader.
     */
    public AbstractFileReader(String filePath, String... acceptedFileExtensions) {
        if (!FileUtils.checkExtensionForFile(filePath, acceptedFileExtensions)) {
            throw new IllegalArgumentException("Error : the given file " + filePath
                    + " has not an accepted extension by " + this.getClass().getSimpleName()
                    + ".\nThe accepted extensions are "
                    + Arrays.toString(acceptedFileExtensions) + ".");
        }
        this.filePath = filePath;
        this.acceptedFileExtensions = acceptedFileExtensions;
    }

    /**
     * Returns the path to the file to read.
     *
     * @return the path to the file to read.
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Returns the file extensions accepted by the reader.
     *
     * @return the file extensions accepted by the reader
     */
    public String[] getAcceptedFileExtensions() {
        return acceptedFileExtensions;
    }

}
