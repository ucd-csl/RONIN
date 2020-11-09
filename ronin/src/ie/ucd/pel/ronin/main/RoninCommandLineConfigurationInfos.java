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
package ie.ucd.pel.ronin.main;

import ie.ucd.pel.ronin.commandsparser.CommandLine;
import ie.ucd.pel.ronin.commandsparser.Option;
import ie.ucd.pel.ronin.commandsparser.OptionsList;
import java.util.Map;

/**
 *
 * @author Come CACHARD
 *
 * Class that contains the configuration of the running of the simulation set by
 * the user with command line.
 */
public class RoninCommandLineConfigurationInfos {

    /**
     * The list of options considered by this application.
     */
    private static OptionsList options;

    /**
     * The name of the simulation for outputs naming.
     */
    private String simulationName;

    /**
     * The path to the sumocfg file that contains sumo configuration
     * informations.
     */
    private String sumocfgFilePath;

    /**
     * If true, we generate the light loads matrix; otherwise it is not
     * generated.
     */
    private boolean generateLightLM;

    /**
     * If true, we generate the loads matrix; otherwise it is not generated. By
     * default, it is not generated.
     */
    private boolean generateLM;

    /**
     * If true, we generate the edge data output; otherwise it is not generated.
     * By default, it is not generated.
     */
    private boolean generateEdgeData;

    /**
     * If true, we generate the trip infos output; otherwise it is not
     * generated. By default, it is not generated.
     */
    private boolean generateTripInfos;

    /**
     * If true, we overwrite the outputs at each run; otherwise we write new
     * outputs. By default, we do not overwrite, we write new outputs.
     */
    private boolean overwriteOutputs;

    /**
     * If true, we print information about the profiling time of the running of
     * the simulation.
     */
    private boolean printProfilingTime;

    /**
     * If positive, we execute a given number of simulations (specified by the
     * user in command line) in order to get an average profiling time for the
     * simulation.
     */
    private int numberOfSimulationsForAverageProfilingTime;

    /**
     * If true, the simulation is monitored by a Ronin server and cannot do an
     * action on its own, it must wait for a query of the Client; otherwise, if
     * false, the simulation works on its own.
     */
    private boolean simulationMonitoredByServer;

    /**
     * The port where is listening the ronin server.
     */
    private int port;

    /**
     * The path to the vehiclesTypesFilePath from where to read the vehicles
     * types. If it is not specified, we use the sumorou file described in the
     * sumocfg file.
     */
    private String vehiclesTypesFilePath;

    /**
     * Equivalent of the command of the option CFG that is used in d-sumo.
     */
    private static final String OPTION_DSUMO_CFG_CMD = "filepath";

    /**
     * Command of the option CFG.
     */
    private static final String OPTION_CFG_CMD = "sumocfg";

    /**
     * Command of the option name.
     */
    private static final String OPTION_NAME_CMD = "name";

    /**
     * Command of the option light loads matrix.
     */
    private static final String OPTION_LIGTH_LOADS_MATRIX_CMD = "lightLoadsMatrix";

    /**
     * Command of the option loads matrix.
     */
    private static final String OPTION_LOADS_MATRIX_CMD = "loadsMatrix";

    /**
     * Command of the option edge data.
     */
    private static final String OPTION_EDGE_DATA_CMD = "edgeData";

    /**
     * Command of the option trip infos.
     */
    private static final String OPTION_TRIP_INFOS_CMD = "tripInfos";

    /**
     * Command of the option overwrite.
     */
    private static final String OPTION_OVERWRITE_OUTPUTS_CMD = "overwrite";

    /**
     * Command of the option profiling time.
     */
    private static final String OPTION_PRINT_PROFILING_TIME_CMD = "profilingTime";

    /**
     * Command of the option average profiling time.
     */
    private static final String OPTION_DO_AVERAGE_PROFILING_TIME_CMD = "avgProfilingTime";

    /**
     * Command of the option monitored by server.
     */
    private static final String OPTION_SIMULATION_MONITORED_BY_SERVER_CMD = "monitoredByServer";

    /**
     * Command of the option port.
     */
    private static final String OPTION_RONIN_PORT_CMD = "roninPort";

    /**
     * Command of the vehicles types filepath.
     */
    private static final String OPTION_SUMOROU_CMD = "vehiclesTypesFile";

    /**
     * Constructs and initializes the default Ronin configuration with the
     * values by default.
     */
    public RoninCommandLineConfigurationInfos() {
        this.simulationName = "simulation";
        this.sumocfgFilePath = "";
        this.generateLightLM = false;
        this.generateLM = false;
        this.generateEdgeData = false;
        this.generateTripInfos = false;
        this.overwriteOutputs = false;
        this.printProfilingTime = false;
        this.numberOfSimulationsForAverageProfilingTime = -1;
        this.simulationMonitoredByServer = false;
        this.port = -1;
        this.vehiclesTypesFilePath = "";
    }

    /**
     * Constructs and initializes the Ronin configuration with the values given
     * in command line by the user.
     *
     * @param cmdLine the command line that contains the configuration infos for
     * the simulation. If null, we use the default configuration.
     */
    public RoninCommandLineConfigurationInfos(CommandLine cmdLine) {
        this.simulationName = "simulation";
        this.sumocfgFilePath = "";
        this.generateLightLM = false;
        this.generateLM = false;
        this.generateEdgeData = false;
        this.generateTripInfos = false;
        this.overwriteOutputs = false;
        this.printProfilingTime = false;
        this.numberOfSimulationsForAverageProfilingTime = -1;
        this.simulationMonitoredByServer = false;
        this.port = -1;
        this.vehiclesTypesFilePath = "";

        if (cmdLine != null) {

            if (cmdLine.isOptionUsed(OPTION_CFG_CMD)) {
                this.sumocfgFilePath = cmdLine.getOptionArgumentValues(OPTION_CFG_CMD).get(0);
            }
            if (cmdLine.isOptionUsed(OPTION_NAME_CMD)) {
                this.simulationName = cmdLine.getOptionArgumentValues(OPTION_NAME_CMD).get(0);
            }
            if (cmdLine.isOptionUsed(OPTION_LIGTH_LOADS_MATRIX_CMD)) {
                this.generateLightLM = true;
            }
            if (cmdLine.isOptionUsed(OPTION_LOADS_MATRIX_CMD)) {
                this.generateLM = true;
            }
            if (cmdLine.isOptionUsed(OPTION_EDGE_DATA_CMD)) {
                this.generateEdgeData = true;
            }
            if (cmdLine.isOptionUsed(OPTION_TRIP_INFOS_CMD)) {
                this.generateTripInfos = true;
            }
            if (cmdLine.isOptionUsed(OPTION_OVERWRITE_OUTPUTS_CMD)) {
                this.overwriteOutputs = true;
            }
            if (cmdLine.isOptionUsed(OPTION_PRINT_PROFILING_TIME_CMD)) {
                this.printProfilingTime = true;
            }
            if (cmdLine.isOptionUsed(OPTION_DO_AVERAGE_PROFILING_TIME_CMD)) {
                try {
                    this.numberOfSimulationsForAverageProfilingTime = Integer.parseInt(cmdLine.getOptionArgumentValues(OPTION_DO_AVERAGE_PROFILING_TIME_CMD).get(0));
                } catch (Exception e) {
                    throw new IllegalArgumentException("Error : the number of simulations to do the average profiling time must be a number.");
                }
            }
            if (cmdLine.isOptionUsed(OPTION_SIMULATION_MONITORED_BY_SERVER_CMD)) {
                this.simulationMonitoredByServer = true;
            }
            if (cmdLine.isOptionUsed(OPTION_RONIN_PORT_CMD)) {
                try {
                    this.port = Integer.parseInt(cmdLine.getOptionArgumentValues(OPTION_RONIN_PORT_CMD).get(0));
                } catch (Exception e) {
                    throw new IllegalArgumentException("Error : the number of simulations to do the average profiling time must be a number.");
                }
            }
            if (cmdLine.isOptionUsed(OPTION_SUMOROU_CMD)) {
                this.vehiclesTypesFilePath = cmdLine.getOptionArgumentValues(OPTION_SUMOROU_CMD).get(0);
            }
        }

    }

    public RoninCommandLineConfigurationInfos(Map<String, Object> parameters) {
        this.simulationName = "simulation";
        this.sumocfgFilePath = "";
        this.generateLightLM = false;
        this.generateLM = false;
        this.generateEdgeData = false;
        this.generateTripInfos = false;
        this.overwriteOutputs = false;
        this.printProfilingTime = false;
        this.numberOfSimulationsForAverageProfilingTime = -1;
        this.simulationMonitoredByServer = false;
        this.port = -1;
        this.vehiclesTypesFilePath = "";

        if (parameters != null) {

            if (parameters.containsKey(OPTION_DSUMO_CFG_CMD)) {
                try {
                    this.sumocfgFilePath = (String) parameters.get(OPTION_DSUMO_CFG_CMD);
                } catch (Exception e) {
                }
            }
            if (parameters.containsKey(OPTION_CFG_CMD)) {
                try {
                    this.sumocfgFilePath = (String) parameters.get(OPTION_CFG_CMD);
                } catch (Exception e) {
                }
            }
            if (parameters.containsKey(OPTION_NAME_CMD)) {
                try {
                    this.simulationName = (String) parameters.get(OPTION_NAME_CMD);
                } catch (Exception e) {
                }
            }
            if (parameters.containsKey(OPTION_LIGTH_LOADS_MATRIX_CMD)) {
                try {
                    this.generateLightLM = (boolean) parameters.get(OPTION_LIGTH_LOADS_MATRIX_CMD);
                } catch (Exception e) {
                }
            }
            if (parameters.containsKey(OPTION_LOADS_MATRIX_CMD)) {
                try {
                    this.generateLM = (boolean) parameters.get(OPTION_LOADS_MATRIX_CMD);
                } catch (Exception e) {
                }
            }
            if (parameters.containsKey(OPTION_EDGE_DATA_CMD)) {
                try {
                    this.generateEdgeData = (boolean) parameters.get(OPTION_EDGE_DATA_CMD);
                } catch (Exception e) {
                }
            }
            if (parameters.containsKey(OPTION_TRIP_INFOS_CMD)) {
                try {
                    this.generateTripInfos = (boolean) parameters.get(OPTION_TRIP_INFOS_CMD);
                } catch (Exception e) {
                }
            }
            if (parameters.containsKey(OPTION_OVERWRITE_OUTPUTS_CMD)) {
                try {
                    this.overwriteOutputs = (boolean) parameters.get(OPTION_OVERWRITE_OUTPUTS_CMD);
                } catch (Exception e) {
                }
            }
            if (parameters.containsKey(OPTION_PRINT_PROFILING_TIME_CMD)) {
                try {
                    this.printProfilingTime = (boolean) parameters.get(OPTION_PRINT_PROFILING_TIME_CMD);
                } catch (Exception e) {
                }
            }
            if (parameters.containsKey(OPTION_DO_AVERAGE_PROFILING_TIME_CMD)) {
                try {
                    this.numberOfSimulationsForAverageProfilingTime = (Integer) parameters.get((OPTION_DO_AVERAGE_PROFILING_TIME_CMD));
                } catch (Exception e) {
                }
            }
            if (parameters.containsKey(OPTION_SIMULATION_MONITORED_BY_SERVER_CMD)) {
                try {
                    this.simulationMonitoredByServer = (boolean) parameters.get(OPTION_SIMULATION_MONITORED_BY_SERVER_CMD);
                } catch (Exception e) {
                }
            }
            if (parameters.containsKey(OPTION_RONIN_PORT_CMD)) {
                try {
                    this.port = (int) parameters.get(OPTION_RONIN_PORT_CMD);
                } catch (Exception e) {
                    try {
                        this.port = Integer.parseInt((String) parameters.get(OPTION_RONIN_PORT_CMD));
                    } catch (Exception es) {
                    }
                }
            }
            if (parameters.containsKey(OPTION_SUMOROU_CMD)) {
                try {
                    this.vehiclesTypesFilePath = (String) parameters.get(OPTION_SUMOROU_CMD);
                } catch (Exception e) {
                }
            }
        }

    }

    public String getCmdConfigLine() {
        StringBuilder sb = new StringBuilder();

        sb.append(createCmdLineForOption(OPTION_NAME_CMD, simulationName));
        sb.append(createCmdLineForOption(OPTION_CFG_CMD, sumocfgFilePath));
        if (generateLightLM) {
            sb.append(createCmdLineForOption(OPTION_LIGTH_LOADS_MATRIX_CMD, null));
        }
        if (generateLM) {
            sb.append(createCmdLineForOption(OPTION_LOADS_MATRIX_CMD, null));
        }
        if (generateEdgeData) {
            sb.append(createCmdLineForOption(OPTION_EDGE_DATA_CMD, null));
        }
        if (generateTripInfos) {
            sb.append(createCmdLineForOption(OPTION_TRIP_INFOS_CMD, null));
        }
        if (overwriteOutputs) {
            sb.append(createCmdLineForOption(OPTION_OVERWRITE_OUTPUTS_CMD, null));
        }
        if (printProfilingTime) {
            sb.append(createCmdLineForOption(OPTION_PRINT_PROFILING_TIME_CMD, null));
        }
        if (numberOfSimulationsForAverageProfilingTime > 0) {
            sb.append(createCmdLineForOption(OPTION_DO_AVERAGE_PROFILING_TIME_CMD, numberOfSimulationsForAverageProfilingTime));
        }
        if (simulationMonitoredByServer) {
            sb.append(createCmdLineForOption(OPTION_SIMULATION_MONITORED_BY_SERVER_CMD, null));
        }
        if (port >= 0) {
            sb.append(createCmdLineForOption(OPTION_RONIN_PORT_CMD, port));
        }
        if (!vehiclesTypesFilePath.isEmpty()) {
            sb.append(createCmdLineForOption(OPTION_SUMOROU_CMD, vehiclesTypesFilePath));
        }
        return sb.toString();
    }

    /**
     * Returns a String containing an argument for command line for an option.
     *
     * @param optionName the long name of the option
     * @param value the value of the option, if null we do not put any value
     * @return a String containing an argument for command line for an option
     */
    private String createCmdLineForOption(String optionName, Object value) {
        String optionPrefix = "--";
        StringBuilder sb = new StringBuilder(optionPrefix);
        sb.append(optionName);
        if (value != null) {
            sb.append(" ").append(value);
        }
        sb.append(" ");
        return sb.toString();
    }

    /**
     * Returns the list of options that are considered for the parser.
     *
     * @return the list of options that are considered for the parser.
     */
    public static OptionsList getParserOptionsList() {

        if (options != null) {
            return options;
        }

        options = new OptionsList();

        try {
            options.addOption(new Option(OPTION_CFG_CMD, "cfg", true, 1, "the path to the SUMO .sumocfg file."));
            options.addOption(new Option(OPTION_NAME_CMD, "n", false, 1, "the name of the simulation for outputs naming."));
            options.addOption(new Option(OPTION_LIGTH_LOADS_MATRIX_CMD, "llm", false, false, "if used, "
                    + "the light loads matrix will be generated. "
                    + "By default, if no output has been chosen, "
                    + "we generate the ligth loads matrix."));
            options.addOption(new Option(OPTION_LOADS_MATRIX_CMD, "lm", false, false, "if used, we generate the complete Loads Matix."));
            options.addOption(new Option(OPTION_EDGE_DATA_CMD, "ed", false, false, "if used, we generate the output containing the edges data."));
            options.addOption(new Option(OPTION_TRIP_INFOS_CMD, "ti", false, false, "if used, we generate the output containing the trip informations."));
            options.addOption(new Option(OPTION_OVERWRITE_OUTPUTS_CMD, "ow", false, false, "if used, we overwrite the outputs if name is already taken."));
            options.addOption(new Option(OPTION_PRINT_PROFILING_TIME_CMD, "pt", false, false, "if used, we print information about the profiling time of the simulation."));
            options.addOption(new Option(OPTION_DO_AVERAGE_PROFILING_TIME_CMD, "avgpt", false, 1, "if used, we execute the specified number of simulations to give an average of the profiling time of the simulation."));
            options.addOption(new Option(OPTION_SIMULATION_MONITORED_BY_SERVER_CMD, "m", false, false, "if used, the simulation is monitored by a Ronin server configured according to sumocfg file. The simulation will then be managed by queries of a Ronin Client."));
            options.addOption(new Option(OPTION_RONIN_PORT_CMD, "p", false, 1, "if the simulation is monitored by a ronin server, this option describes the port to use. Else the port will be read from the sumocfg file."));
            options.addOption(new Option(OPTION_SUMOROU_CMD, "vtf", false, 1, "if specified, we read the different types of vehicles from this file instead of the sumorou file described in the sumocfg file."));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        return options;
    }

    /**
     * Returns the name of the simulation.
     *
     * @return the name of the simulation.
     */
    public String getSimulationName() {
        return simulationName;
    }

    /**
     * Returns the path to the sumocfg file used for this simulation.
     *
     * @return the path to the sumocfg file used for this simulation.
     */
    public String getSumocfgFilePath() {
        return sumocfgFilePath;
    }

    /**
     * Returns the path to the sumorou file where to read the types of vehicles
     * used in this simulation. If empty, we should use the file described in
     * sumocfg file.
     *
     * @return the path to the sumorou file where to read the types of vehicles
     * used in this simulation. If empty, we should use the file described in
     * sumocfg file
     */
    public String getVehiclesTypesFilePath() {
        return vehiclesTypesFilePath;
    }

    /**
     * Returns the number of simulations to do to get the average profiling
     * time. If it is negative, we do not do an average profiling time.
     *
     * @return the number of simulations to do to get the average profiling
     * time. If it is negative, we do not do an average profiling time.
     */
    public int getNumberOfSimulationsForAverageProfilingTime() {
        return numberOfSimulationsForAverageProfilingTime;
    }

    /**
     * Returns the port of the ronin server.
     *
     * @return the port of the ronin server
     */
    public int getPort() {
        return port;
    }

    /**
     * Sets the name of the simulation.
     *
     * @param simulationName the name of the simulation for outputs naming.
     */
    public void setSimulationName(String simulationName) {
        this.simulationName = simulationName;
    }

    /**
     * Sets the path to the sumocfg file used by this simulation.
     *
     * @param sumocfgFilePath the path to the sumonet file used by this
     * simulation
     */
    public void setSumocfgFilePath(String sumocfgFilePath) {
        this.sumocfgFilePath = sumocfgFilePath;
    }

    /**
     * Sets the new value of the sumorou file used for reading the types of
     * vehicles used in this simulation.
     *
     * @param vehiclesTypesFilePath the path to the sumorou file where to read
     * the types of vehicles used in this simulation
     */
    public void setVehiclesTypesFilePath(String vehiclesTypesFilePath) {
        this.vehiclesTypesFilePath = vehiclesTypesFilePath;
    }

    /**
     * If we set this option to true, we generate a light loads matrix at the
     * end of the simulation.
     *
     * @param generateLightLM if we set this option to true, we generate a light
     * loads matrix at the end of the simulation
     */
    public void setGenerateLightLM(boolean generateLightLM) {
        this.generateLightLM = generateLightLM;
    }

    /**
     * If we set this option to true, we generate a loads matrix at the end of
     * the simulation.
     *
     * @param generateLM if we set this option to true, we generate a loads
     * matrix at the end of the simulation
     */
    public void setGenerateLM(boolean generateLM) {
        this.generateLM = generateLM;
    }

    /**
     * If we set this option to true, we generate edge data output at the end of
     * the simulation.
     *
     * @param generateEdgeData if we set this option to true, we generate edge
     * data output at the end of the simulation
     */
    public void setGenerateEdgeData(boolean generateEdgeData) {
        this.generateEdgeData = generateEdgeData;
    }

    /**
     * If we set this option to true, we generate trip infos output at the end
     * of the simulation.
     *
     * @param generateTripInfos if we set this option to true, we generate trip
     * infos output at the end of the simulation
     */
    public void setGenerateTripInfos(boolean generateTripInfos) {
        this.generateTripInfos = generateTripInfos;
    }

    /**
     * If we set to true, we overwrite the outputs of the simulation if the name
     * of the outputs are already taken; otherwise we write new outputs.
     *
     * @param overwriteOutputs if true, we overwrite the outputs of the
     * simulation if the name of the outputs are already taken; otherwise we
     * write new outputs
     */
    public void setOverwriteOutputs(boolean overwriteOutputs) {
        this.overwriteOutputs = overwriteOutputs;
    }

    /**
     * If sets to true, we print the profiling time of the simulation.
     *
     * @param printProfilingTime if true, we print the profiling time of the
     * simulation
     */
    public void setPrintProfilingTime(boolean printProfilingTime) {
        this.printProfilingTime = printProfilingTime;
    }

    /**
     * Sets the new value of the number of simulations to do for getting the
     * average profiling time. If the new value is negative, we do not do an
     * average profiling time; otherwise we execute the given number of
     * simulations.
     *
     * @param numberOfSimulationsForAverageProfilingTime the new value of the
     * number of simulations to do for getting the average profiling time. If
     * the new value is negative, we do not do an average profiling time;
     * otherwise we execute the given number of simulations.
     */
    public void setNumberOfSimulationsForAverageProfilingTime(int numberOfSimulationsForAverageProfilingTime) {
        this.numberOfSimulationsForAverageProfilingTime = numberOfSimulationsForAverageProfilingTime;
    }

    /**
     * If sets to true, the simulation will be monitored by a server.
     *
     * @param simulationMonitoredByServer if true, the simulation will be
     * monitored by a server
     */
    public void setSimulationMonitoredByServer(boolean simulationMonitoredByServer) {
        this.simulationMonitoredByServer = simulationMonitoredByServer;
    }

    /**
     * Sets the new value of the port of the ronin server.
     *
     * @param port the new value of the port of the ronin server.
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Returns true if we must generate a light loads matrix at the end of the
     * simulation; false otherwise.
     *
     * @return true if we must generate a light loads matrix at the end of the
     * simulation; false otherwise.
     */
    public boolean isGenerateLightLM() {
        return generateLightLM;
    }

    /**
     * Returns true if we must generate a loads matrix at the end of the
     * simulation; false otherwise.
     *
     * @return true if we must generate a loads matrix at the end of the
     * simulation; false otherwise.
     */
    public boolean isGenerateLM() {
        return generateLM;
    }

    /**
     * Returns true if we must generate edge data output at the end of the
     * simulation; false otherwise.
     *
     * @return true if we must generate edge data at the end of the simulation;
     * false otherwise.
     */
    public boolean isGenerateEdgeData() {
        return generateEdgeData;
    }

    /**
     * Returns true if we must generate trip infos output at the end of the
     * simulation; false otherwise.
     *
     * @return true if we must generate trip infos output at the end of the
     * simulation; false otherwise.
     */
    public boolean isGenerateTripInfos() {
        return generateTripInfos;
    }

    /**
     * Returns true if we overwrite outputs if their name is already taken;
     * false otherwise and in that case we write new outputs.
     *
     * @return true if we overwrite outputs if their name is already taken;
     * false otherwise and in that case we write new outputs.
     */
    public boolean isOverwriteOutputs() {
        return overwriteOutputs;
    }

    /**
     * Returns true if we print information about the profiling time of the
     * simulation; false otherwise.
     *
     * @return true if we print information about the profiling time of the
     * simulation; false otherwise
     */
    public boolean isPrintProfilingTime() {
        return printProfilingTime;
    }

    /**
     * Returns true if the simulation is monitored by a Ronin server; false
     * otherwise.
     *
     * @return true if the simulation is monitored by a Ronin server; false
     * otherwise.
     */
    public boolean isSimulationMonitoredByServer() {
        return simulationMonitoredByServer;
    }

    /**
     * Debug method used to print the values of the configuration of Ronin.
     */
    public void printRoninConfiguration() {
        System.out.println("Ronin configuration : ");
        System.out.println("\tsimulation name : " + simulationName);
        System.out.println("\tsumocfg file : " + sumocfgFilePath);
        System.out.println("\tgenerate light loads matrix : " + generateLightLM);
        System.out.println("\tgenerate loads matrix : " + generateLM);
        System.out.println("\tgenerate edge data output : " + generateEdgeData);
        System.out.println("\tgenerate trip infos output : " + generateTripInfos);
        System.out.println("\toverwrite outputs : " + overwriteOutputs);
        System.out.println("\tprint profiling time : " + printProfilingTime);
        System.out.println("\tdo average profiling time : " + numberOfSimulationsForAverageProfilingTime);
        System.out.println("\tsimulation monitored by server : " + simulationMonitoredByServer);
        System.out.println("\tport of ronin server : " + port);
        System.out.println("\tvehicles types file : " + vehiclesTypesFilePath);
    }

}
