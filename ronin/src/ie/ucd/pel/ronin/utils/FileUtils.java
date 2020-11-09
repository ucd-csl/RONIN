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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 *
 * @author Come CACHARD
 *
 * Class used to provide methods to process files.
 */
public class FileUtils {

    /**
     * Checks if the given extension to check is an accepted extension or not.
     * Returns true if it is accepted; false otherwise.
     *
     * @param extensionToCheck the file extension to check
     * @param acceptedExtensions the accepted file extensions
     * @return true if extensionToCheck is accepted; false otherwise
     */
    public static boolean checkExtension(String extensionToCheck, String... acceptedExtensions) {
        return Stream.of(acceptedExtensions).anyMatch(x -> x.equals(extensionToCheck));
    }

    /**
     * Checks if the given file has an accepted extension or not. Returns true
     * if it is accepted; false otherwise.
     *
     * @param f the file to check the extension
     * @param acceptedExtensions the accepted file extensions
     * @return true if the given file has an accepted extension; false otherwise
     */
    public static boolean checkExtensionForFile(File f, String... acceptedExtensions) {
        String filePath = f.getName();
        return checkExtensionForFile(filePath, acceptedExtensions);
    }

    /**
     * Checks if the given file has an accepted extension or not. Returns true
     * if it is accepted; false otherwise.
     *
     * @param filePath the file path to the file to check the extension
     * @param acceptedExtensions the accepted file extensions
     * @return true if the given file has an accepted extension; false otherwise
     */
    public static boolean checkExtensionForFile(String filePath, String... acceptedExtensions) {
        for (String acceptedExtension : acceptedExtensions) {
            if (filePath.endsWith("." + acceptedExtension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the extension of a file without the dot.
     *
     * @param f the file we want to get the extension
     * @param extensionLength the number of parts that composed the extension to
     * return.
     * @return a String that contains the extension of the given file without
     * the dot
     */
    public static String getExtension(int extensionLength, File f) {
        String filePath = f.getName();

        return getExtension(extensionLength, filePath);
    }

    /**
     * Returns the extension of a file without the dot.
     *
     * @param filePath the file path of the file we want to get the extension
     * @param extensionLength the number of parts that composed the extension to
     * return.
     * @return a String that contains the extension of the given file without
     * the dot
     */
    public static String getExtension(int extensionLength, String filePath) {

        //parse the file on the dot characters.
        String[] filename = filePath.split("\\.");

        int positionBegin = filename.length - extensionLength;

        if (positionBegin > 0) {
            StringBuilder sb = new StringBuilder();
            boolean begin = true;
            for (int i = positionBegin; i < filename.length; i++) {
                if (begin == false) {
                    sb.append(".");
                }
                sb.append(filename[i]);
                begin = false;
            }
            return sb.toString();
        } else {
            return filePath;
        }
    }

    /**
     * Returns the path to the parent folder of a file from its filepath.
     *
     * @param filePath the path to the file we want to get the path to the
     * parent folder
     * @return the path to the parent folder of a file
     */
    public static String getFileParentFolderPath(String filePath) {
        if (filePath.lastIndexOf(File.separator) == -1) {
            return "." + File.separator;
        }
        return filePath.substring(0, filePath.lastIndexOf(File.separator));
    }

    /**
     * Returns the name of a file without extension.
     *
     * @param filepath the path to the file we want the name
     * @return the name of a file without extension
     */
    public static String getFileNameWithoutExtension(String filepath) {
        String[] parsedFilepath = filepath.split(File.separator);
        return filepath.substring(0, parsedFilepath[parsedFilepath.length - 1].indexOf("."));
    }

    /**
     * Returns the name of a file without extension.
     *
     * @param file the file we want the name
     * @return the name of a file without extension
     */
    public static String getFileNameWithoutExtension(File file) {
        return getFileNameWithoutExtension(file.getName());
    }

    /**
     * Delete a folder and all its content recursively
     *
     * @param folder the folder to delete
     */
    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) { //some JVMs return null for empty dirs
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

    public static File[] getFilesFromFolderPathWithExtension(File folder, String... extensions) {
        if (!folder.isDirectory()) {
            return new File[0];
        }

        List<File> tempResult = new ArrayList<>();
        File[] files = folder.listFiles();
        if (files != null) { //some JVMs return null for empty dirs
            for (File f : files) {
                if (!f.isDirectory() && checkExtensionForFile(f, extensions)) {
                    tempResult.add(f);
                }
            }
        }

        File[] result = tempResult.toArray(new File[tempResult.size()]);

        return result;
    }

    public static File[] getFilesFromFolderPathWithExtension(String folderPath, String... extensions) {
        File folder = new File(folderPath);
        return getFilesFromFolderPathWithExtension(folder, extensions);
    }

}
