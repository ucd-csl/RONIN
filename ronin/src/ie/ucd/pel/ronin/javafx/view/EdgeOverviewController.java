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
import ie.ucd.pel.ronin.javafx.model.EdgeLoad;
import ie.ucd.pel.ronin.javafx.model.StepLoads;
import ie.ucd.pel.ronin.model.Edge;
import ie.ucd.pel.ronin.filesreader.roninfilesreader.LightLoadsMatrixStepReader;
import java.io.File;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Come CACHARD
 *
 * Controller of the view of the Edges display and the map.
 */
public class EdgeOverviewController {

    /**
     * Main pane of this controller.
     */
    @FXML
    private AnchorPane anchorPane;

    /**
     * A table view that displays the loads per Edge.
     */
    @FXML
    private TableView<EdgeLoad> edgeTable;

    /**
     * Column that contains the id of the Edges.
     */
    @FXML
    private TableColumn<EdgeLoad, String> idColumn;

    /**
     * Column that contains the loads of the Edges.
     */
    @FXML
    private TableColumn<EdgeLoad, Integer> loadsColumn;

    /**
     * Canvas where we draw the graph of the network.
     */
    @FXML
    private Canvas networkCanvas;

    /**
     * ListView that contains the list of step files of the read matrix of
     * loads.
     */
    @FXML
    private ListView listStepFilesView;

    /**
     * Scroll pane that contains the canvas where we draw our network.
     */
    @FXML
    private ScrollPane scrollPane;

    /**
     * Group that contains the elements of the scrollpane.
     */
    @FXML
    private Group scrollGroup;

    /**
     * Attributes used for the translation of the canvas.
     */
    double orgSceneX, orgSceneY;

    /**
     * Attributes used for the translation of the canvas.
     */
    double orgTranslateX, orgTranslateY;

    /**
     * The origin coordinates of the network canvas for drawing the edges of the
     * graph.
     */
    private final IntegerProperty originX, originY;

    /**
     * The scale of the network canvas.
     */
    private final DoubleProperty scale;

    /**
     * The id of the edge to show on the canvas.
     */
    private final StringProperty idEdgeToShow;

    /**
     * Timeline used for smooth animation.
     */
    private final Timeline timeline;

    /**
     * Current step file of matrix of loads that is displayed.
     */
    private File currentFile;

    /**
     * The loads per Edge for the current file.
     */
    private StepLoads stepLoads;

    /**
     * Reference to the main application.
     */
    private MainFx mainFx;

    /**
     * The constructor. The constructor is called before the initialize()
     * method.
     */
    public EdgeOverviewController() {
        this.timeline = new Timeline(60);
        originX = new SimpleIntegerProperty(0);
        originY = new SimpleIntegerProperty(0);
        scale = new SimpleDoubleProperty(1);
        idEdgeToShow = new SimpleStringProperty("");
        currentFile = null;
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the edge table with the two columns.
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idEdgeProperty());
        loadsColumn.setCellValueFactory(cellData -> cellData.getValue().loadProperty().asObject());
        edgeTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> idEdgeToShow.set(newValue.getIdEdge()));
        configViewElements();
        drawNetworkGraph();
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp a reference to the main application
     */
    public void setMainApp(MainFx mainApp) {
        this.mainFx = mainApp;

        // Add observable list data to the table
        edgeTable.setItems(mainFx.getEdgeLoadData());
        if (!mainFx.getLoadsMatrixFiles().isEmpty()) {
            currentFile = mainFx.getLoadsMatrixFiles().get(0);
        }
        configListView();
        updateLoads(currentFile);

        drawNetworkGraph();
    }

    /**
     * Sets the new current step file of loads matrix to read and update the
     * loads of the UI.
     *
     * @param currentFile the new current step file of loads matrix to read
     */
    public void setCurrentFile(File currentFile) {
        this.currentFile = currentFile;
        updateLoads(currentFile);
    }

    /**
     * Configures the view Elements of this controller.
     */
    private void configViewElements() {
        configScrollPane();
        configCanvas();

        idEdgeToShow.addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue o, Object oldVal,
                    Object newVal) {
                drawNetworkGraph();
            }
        });

        scale.addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue o, Object oldVal,
                    Object newVal) {
                if (scale.get() < 1) {
                    scale.set(1);
                }
                double maxZoom = mainFx.getNetworkGraph().getMapConfig().getMapHeight() / networkCanvas.getHeight();
                if (maxZoom > 1) {
                    if (scale.get() > maxZoom) {
                        scale.set(maxZoom);
                    }
                }
                drawNetworkGraph();
            }
        });

    }

    /**
     * Configures the list view of step files.
     */
    private void configListView() {
        listStepFilesView.setTooltip(new Tooltip("The different steps of the simulation."));
        listStepFilesView.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    public void changed(ObservableValue ov, Number value, Number new_value) {
                        if (mainFx.getLoadsMatrixFiles() != null && !mainFx.getLoadsMatrixFiles().isEmpty()) {
                            currentFile = mainFx.getLoadsMatrixFiles().get(new_value.intValue());
                        } else {
                            currentFile = null;
                        }

                        updateLoads(currentFile);
                    }
                });
        if (mainFx != null) {
            listStepFilesView.setItems(mainFx.getLoadsMatrixFiles());
        }
    }

    /**
     * Configures the scroll pane.
     */
    private void configScrollPane() {
        scrollPane.setStyle("-fx-background:white;");
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        networkCanvas.widthProperty().bind(scrollPane.widthProperty());
        networkCanvas.heightProperty().bind(scrollPane.heightProperty());
    }

    /**
     * Configures the canvas.
     */
    private void configCanvas() {
        networkCanvas.widthProperty().addListener(observable -> drawNetworkGraph());
        networkCanvas.heightProperty().addListener(observable -> drawNetworkGraph());

        networkCanvas.setOnMousePressed((MouseEvent event) -> {
            orgSceneX = event.getSceneX();
            orgSceneY = event.getSceneY();
            orgTranslateX = ((Canvas) (event.getSource())).getTranslateX();
            orgTranslateY = ((Canvas) (event.getSource())).getTranslateY();
        });

        networkCanvas.setOnMouseDragged((MouseEvent event) -> {
            double reductorTranslate = 0.125;
            double offsetX = event.getSceneX() - orgSceneX;
            double offsetY = event.getSceneY() - orgSceneY;
            double newTranslateX = (orgTranslateX + offsetX) * reductorTranslate;
            double newTranslateY = (orgTranslateY + offsetY) * reductorTranslate;
            moveCanvas((int) newTranslateX, (int) newTranslateY);
        });

        // Listen to scroll events (similarly you could listen to a button click, slider, ...)
        networkCanvas.setOnScroll((ScrollEvent event) -> {
            double zoomFactor = 1.125;
            if (event.getDeltaY() <= 0) {
                // zoom out
                zoomFactor = 1 / zoomFactor;
            }
            zoom(zoomFactor, event.getSceneX(), event.getSceneY());
        });
    }

    /**
     * Updates the loads of the GUI with a step file of matrix of loads.
     *
     * @param f the step file of matrix of loads from which we will read the new
     * loads
     */
    private void updateLoads(File f) {
        if (f != null) {
            LightLoadsMatrixStepReader llmReader = new LightLoadsMatrixStepReader(f.getAbsolutePath());//"./output/simulation/lightLoadsMatrix/step_6436.llm.xml"
            stepLoads = llmReader.readStepLoads();
            edgeTable.getItems().stream().forEach((e) -> {
                double load = (stepLoads.getLoadsPerEdge().getOrDefault(e.getIdEdge(), 0.));
                e.setLoad((int) load);
            });
        }

        drawNetworkGraph();
    }

    /**
     * Clears the canvas.
     */
    private void clearNetworkCanvas() {
        GraphicsContext gc = networkCanvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, networkCanvas.getWidth(), networkCanvas.getHeight());
    }

    /**
     * Clears the canvas and draws the graph on the canvas.
     */
    public void drawNetworkGraph() {
        clearNetworkCanvas();
        if (mainFx != null && mainFx.getNetworkGraph() != null && !mainFx.getNetworkGraph().isEmpty()) {
            GraphicsContext gc = networkCanvas.getGraphicsContext2D();
            double factorX = networkCanvas.getWidth() / mainFx.getNetworkGraph().getMapConfig().getMapWidth();
            double factorY = networkCanvas.getHeight() / mainFx.getNetworkGraph().getMapConfig().getMapHeight();

            double factorMapSize = Math.min(factorX, factorY) * this.scale.doubleValue();
            for (EdgeLoad e : edgeTable.getItems()) {
                Edge edge = mainFx.getNetworkGraph().getEdge(e.getIdEdge());
                if (e.getIdEdge().equals(idEdgeToShow.get())) {
                    gc.setFill(Color.LIME);
                    gc.setStroke(Color.LIME);
                    gc.setLineWidth(5);
                } else {

                    gc.setFill(edge.getColor(e.getLoad()));
                    gc.setStroke(edge.getColor(e.getLoad()));
                    gc.setLineWidth(1);
                }
                gc.strokeLine(originX.doubleValue() + edge.getStartNode().getX() * factorMapSize, originY.doubleValue() + edge.getStartNode().getY() * factorMapSize, originX.doubleValue() + edge.getEndNode().getX() * factorMapSize, originY.doubleValue() + edge.getEndNode().getY() * factorMapSize);
            }
        }
    }

    /**
     * Recenters the drawing of the graph on the canvas and reset scale to 1.
     */
    @FXML
    private void recenterNetworkMap() {
        originX.set(0);
        originY.set(0);
        networkCanvas.setScaleX(1);
        networkCanvas.setScaleY(1);
        scale.set(1);
        drawNetworkGraph();
    }

    /**
     * Moves the origin of the canvas with a dx and a dy.
     *
     * @param dx the dx to move the canvas
     * @param dy the dy to move the canvas
     */
    private void moveCanvas(int dx, int dy) {
        timeline.getKeyFrames().clear();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(200), new KeyValue(originX, originX.intValue() + dx)),
                new KeyFrame(Duration.millis(200), new KeyValue(originY, originY.intValue() + dy))
        );
        timeline.play();
        drawNetworkGraph();
    }

    /**
     * Make a zoom on the elements drawn on the canvas.
     *
     * @param factor the factor scale to do for the zoom
     * @param x the x origin of the zoom event
     * @param y the y origin of the zoom event
     */
    private void zoom(double factor, double x, double y) {
        // determine scale
        double oldScale = scale.doubleValue();
        scale.set(oldScale * factor);
        double f = scale.get() - oldScale;//(scale.doubleValue() / oldScale) - 1;

        // determine offset that we will have to move the node
        Bounds bounds = networkCanvas.localToScene(networkCanvas.getBoundsInLocal());
        double dx = (x - (bounds.getWidth() / 2 + bounds.getMinX()));
        double dy = (y - (bounds.getHeight() / 2 + bounds.getMinY()));

        // timeline that scales and moves the node
        timeline.getKeyFrames().clear();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(200), new KeyValue(originX, originX.intValue() - f * dx)),
                new KeyFrame(Duration.millis(200), new KeyValue(originY, originY.intValue() - f * dy))
        );
        timeline.play();
        drawNetworkGraph();
    }

}
