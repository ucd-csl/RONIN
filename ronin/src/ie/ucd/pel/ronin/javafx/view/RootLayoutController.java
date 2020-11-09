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
package ie.ucd.pel.ronin.javafx.view;

import ie.ucd.pel.ronin.javafx.MainFx;
import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author Come CACHARD
 *
 * The controller that manages the menu bar of the javafx application.
 */
public class RootLayoutController {

    /**
     * Reference to the main application.
     */
    private MainFx mainFx;

    /**
     * Menu item from 'file' menu that allows us to open a loads matrix folder.
     */
    @FXML
    private MenuItem openLoadsMatrixMenuItem;

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainFx the reference to the main application
     */
    public void setMainFx(MainFx mainFx) {
        this.mainFx = mainFx;
    }

    /**
     * Disable the possibility of opening a matrix of loads. We should be
     * allowed to open a matrix of loads only if a network file is already read.
     *
     * @param value if true, we disable the menu item that can open a matrix of
     * loads; otherwise we do not disable the menu item
     */
    public void disableOpenLoadsMatrixMenuItem(boolean value) {
        openLoadsMatrixMenuItem.setDisable(value);
    }

    /**
     * Opens a FileChooser to let the user select a network file to read.
     */
    @FXML
    private void handleOpenNetwork() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "SUMO net files (*.net.xml)", ".net.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showOpenDialog(mainFx.getPrimaryStage());

        if (file != null) {
            mainFx.readGraphNetworkFromFile(file);
        }
    }

    /**
     * Opens a FileChooser to let the user select a matrix of loads'folder to
     * read.
     */
    @FXML
    private void handleOpenLoadsMatrix() {
        DirectoryChooser directoryChooser = new DirectoryChooser();

        // Show save file dialog
        File file = directoryChooser.showDialog(mainFx.getPrimaryStage());

        if (file != null) {
            mainFx.readLoadsMatrixFromFolder(file);
        }
    }

    /**
     * Opens an about dialog.
     */
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Ronin");
        alert.setHeaderText("About");
        alert.setContentText("Authors:\n\t\tCome Cachard\n\tAnthony Ventresque\nWebsite: https://github.com/aventresque/ronin");

        alert.showAndWait();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }

}
