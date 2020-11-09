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
package ie.ucd.pel.ronin.javafx;

import ie.ucd.pel.ronin.javafx.model.EdgeLoad;
import ie.ucd.pel.ronin.javafx.view.EdgeOverviewController;
import ie.ucd.pel.ronin.javafx.view.RootLayoutController;
import ie.ucd.pel.ronin.model.Graph;
import ie.ucd.pel.ronin.filesreader.sumofilesreader.SumoNetFileReader;
import ie.ucd.pel.ronin.utils.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.prefs.Preferences;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *
 * @author Come CACHARD
 *
 * Main class that launches the GUI. The GUI does not launch the simulation, it
 * reads a graph network (so a sumo .net file), and then it can load different
 * simulation about this network by reading the corresponding loads matrix
 * folder outputs.
 */
public class MainFx extends Application {

    /**
     * If this variable is set to true, we read the last opened files at the
     * lauching of the GUI; otherwise the GUI is launched with no files.
     */
    private final boolean readLastOpenedFiles = true;

    /**
     * Constant describing the preference of the filepath to the loads matrix
     * folder
     */
    private final static String PREF_FILE_LM = "LM_FILE_PATH";

    /**
     * Constant describing the preference of the filepath to the network file.
     */
    private final static String PREF_FILE_NETWORK = "NETWORK_FILE_PATH";

    /**
     * Main container of java FX application.
     */
    private Stage primaryStage;

    /**
     * Java fx node for main scene.
     */
    private BorderPane rootLayout;

    /**
     * The data as an observable list of edges.
     */
    private final ObservableList<EdgeLoad> edgeLoadData;

    /**
     * The observable list of step files of the loads matrix.
     */
    private final ObservableList<File> loadsMatrixFiles;

    /**
     * The graph of the network to consider.
     */
    private final Graph networkGraph;

    /**
     * The controller of the menu bar.
     */
    private RootLayoutController rootLayoutController;

    /**
     * The controller of the EdgeOverview.
     */
    private EdgeOverviewController edgeOverviewController;

    /**
     * Constructs and initializes the main of the javaFX application.
     */
    public MainFx() {
        networkGraph = new Graph();
        this.edgeLoadData = FXCollections.observableArrayList();
        this.loadsMatrixFiles = FXCollections.observableArrayList();
    }

    /**
     * Launches the java fx application.
     *
     * @param args arguments from command line
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Returns the data as an observable list of Edges.
     *
     * @return the data as an observable list of Edges
     */
    public ObservableList<EdgeLoad> getEdgeLoadData() {
        return edgeLoadData;
    }

    /**
     * Returns the loads matrix file preference, i.e. the file that was last
     * opened. The preference is read from the OS specific registry. If no such
     * preference can be found, null is returned.
     *
     * @return the loads matrix file preference, i.e. the file that was last
     * opened
     */
    public File getLoadsMatrixFolderPath() {
        Preferences prefs = Preferences.userNodeForPackage(MainFx.class);
        String filePath = prefs.get(PREF_FILE_LM, null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    /**
     * Returns the network file preference, i.e. the file that was last opened.
     * The preference is read from the OS specific registry. If no such
     * preference can be found, null is returned.
     *
     * @return the network file preference
     */
    public File getNetworkFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(MainFx.class);
        String filePath = prefs.get(PREF_FILE_NETWORK, null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    /**
     * Returns the main stage.
     *
     * @return the main stage.
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Returns the observable list of step files of a matrix of loads.
     *
     * @return the observable list of step files of a matrix of loads
     */
    public ObservableList<File> getLoadsMatrixFiles() {
        return loadsMatrixFiles;
    }

    /**
     * Returns the graph of the network to consider.
     *
     * @return the graph of the network to consider
     */
    public Graph getNetworkGraph() {
        return networkGraph;
    }

    /**
     * Returns an array of files describing the step files of a matrix of loads,
     * sorted "numerically" by names.
     *
     * @param loadsMatrixPath the path to the folder of a matrix of loads
     * @return an array of files describing the step files of a matrix of loads,
     * sorted "numerically" by names
     */
    private File[] getStepFilesOfLoadsMatrix(String loadsMatrixPath) {
        File[] filesOfMatrix = FileUtils.getFilesFromFolderPathWithExtension(loadsMatrixPath, "llm.xml", "lm.xml");
        Arrays.sort(filesOfMatrix, (File f1, File f2) -> {
            try {
                String name1 = FileUtils.getFileNameWithoutExtension(f1);
                String[] name1Split = name1.split("_");
                String name2 = FileUtils.getFileNameWithoutExtension(f2);
                String[] name2Split = name2.split("_");
                int i1 = Integer.parseInt(name1Split[name1Split.length - 1]);
                int i2 = Integer.parseInt(name2Split[name2Split.length - 1]);
                return i1 - i2;
            } catch (NumberFormatException e) {
                throw new AssertionError(e);
            }
        });
        return filesOfMatrix;
    }

    /**
     * Sets the file path of the currently loaded file. The path is persisted in
     * the OS specific registry.
     *
     * @param file the file or null to remove the path
     */
    public void setLoadsMatrixFolderPath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(MainFx.class);
        if (file != null) {
            prefs.put(PREF_FILE_LM, file.getPath());
        } else {
            prefs.remove(PREF_FILE_LM);
        }
    }

    /**
     * Sets the file path of the currently loaded file. The path is persisted in
     * the OS specific registry.
     *
     * @param file the file or null to remove the path
     */
    public void setNetworkFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(MainFx.class);
        if (file != null) {
            prefs.put(PREF_FILE_NETWORK, file.getPath());

            // Update the stage title.
            primaryStage.setTitle("Ronin - " + file.getName());
        } else {
            prefs.remove(PREF_FILE_NETWORK);

            // Update the stage title.
            primaryStage.setTitle("Ronin");
        }
    }

    /**
     * Reads the matrix of loads from its folder.
     *
     * @param file the folder containing all the step files of the matrix of
     * loads to read
     */
    public void readLoadsMatrixFromFolder(File file) {
        if (!file.isDirectory()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText(file.getPath() + "is not a Loads Matrix directory.");
            alert.showAndWait();
        } else if (loadsMatrixFiles != null && edgeOverviewController != null) {
            try {
                // Reading XML from the file and unmarshalling.
                loadsMatrixFiles.clear();
                loadsMatrixFiles.addAll(getStepFilesOfLoadsMatrix(file.getAbsolutePath()));
                if (!loadsMatrixFiles.isEmpty()) {
                    edgeOverviewController.setCurrentFile(loadsMatrixFiles.get(0));
                }
                setLoadsMatrixFolderPath(file);

            } catch (Exception e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Could not load data");
                alert.setContentText("Could not load loads matrix from folder:\n" + file.getPath());
                alert.showAndWait();
            }
        }

    }

    /**
     * Reads the network graph from a sumonet file
     *
     * @param file the sumonet file to construct the network graph from
     */
    public void readGraphNetworkFromFile(File file) {
        if (networkGraph != null && edgeLoadData != null && loadsMatrixFiles != null && edgeOverviewController != null) {
            try {
                // Reading XML from the file and unmarshalling.
                SumoNetFileReader networkReader = new SumoNetFileReader(file.getPath());
                networkGraph.clear();
                edgeLoadData.clear();
                loadsMatrixFiles.clear();
                networkReader.readGraph(networkGraph);

                edgeLoadData.addAll(EdgeLoad.getDefaultEdgeLoadsList(networkGraph.getEdges().values()));

                edgeOverviewController.drawNetworkGraph();

                // Save the file path to the registry.
                setNetworkFilePath(file);

            } catch (Exception e) {

                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Could not load data");
                alert.setContentText("Could not load graph from file:\n" + file.getPath());

                alert.showAndWait();

            }

            rootLayoutController.disableOpenLoadsMatrixMenuItem(false);
        }
    }

    /**
     * Starts the javafx application.
     *
     * @param primaryStage the primary stage of the application.
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Ronin");

        initRootLayout();

        showEdgeOverview();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainFx.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Give the controller access to the main app.
            rootLayoutController = loader.getController();
            rootLayoutController.setMainFx(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Shows the edge overview inside the root layout.
     */
    public void showEdgeOverview() {
        try {
            // Load edge overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainFx.class.getResource("view/EdgeOverview.fxml"));
            AnchorPane edgeOverview = (AnchorPane) loader.load();

            // Set edge overview into the center of root layout.
            rootLayout.setCenter(edgeOverview);

            // Give the controller access to the main app.
            edgeOverviewController = loader.getController();
            edgeOverviewController.setMainApp(this);

            // Try to load last opened graph.
            if (readLastOpenedFiles) {
                File networkGraphFile = getNetworkFilePath();
                if (networkGraphFile != null) {
                    readGraphNetworkFromFile(networkGraphFile);
                }

                File loadsMatrixFolder = getLoadsMatrixFolderPath();
                if (loadsMatrixFolder != null) {
                    readLoadsMatrixFromFolder(loadsMatrixFolder);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
