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
package ie.ucd.pel.ronin.communication.socket;

import ie.ucd.pel.ronin.communication.query.vehiclequery.AddVehicleQuery;
import ie.ucd.pel.ronin.communication.query.ProcessNextStepQuery;
import ie.ucd.pel.ronin.communication.query.Query;
import ie.ucd.pel.ronin.communication.query.QueryLauncher;
import ie.ucd.pel.ronin.communication.query.vehiclequery.RemoveVehicleQuery;
import ie.ucd.pel.ronin.communication.query.StopServerQuery;
import ie.ucd.pel.ronin.communication.serverresponse.ProcessNextStepServerResponse;
import ie.ucd.pel.ronin.communication.serverresponse.ServerResponse;
import ie.ucd.pel.ronin.main.RoninCommandLineConfigurationInfos;
import ie.ucd.pel.ronin.utils.MainUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Arrays;
import java.util.Collection;
import static java.lang.Thread.sleep;

/**
 *
 * @author Come CACHARD
 *
 * Client that can interact with a Ronin server in order to act on its
 * simulation, by adding or removing a vehicle, and to ask to process the next
 * step, or to stop everything, or other queries. The use can choose to create a
 * new QueryLauncher and add the queries he wants in order to execute them in
 * one time.
 */
public class RoninClient {

    /**
     * Debug constant used to print the output of the Ronin server when
     * launching it.
     */
    private static final boolean PRINT_OUTPUT_SERVER = false;

    /**
     * The arguments given by the user in command line.
     */
    private final String argsCmdLine;

    /**
     * The adress of the host i.e. the server name or its IP adress to
     * communicate with.
     */
    private final String serverLocation;

    /**
     * The port where is listening the server.
     */
    private final int port;

    /**
     * The output stream of this Client.
     */
    private ObjectOutputStream out;

    /**
     * The input stream of this Client.
     */
    private ObjectInputStream in;

    /**
     * The socket used by this Client to communicate with the Server.
     */
    private Socket socket;

    /**
     * If true, the client is started and can communicate with Client Server;
     * false otherwise.
     */
    private boolean isConnected;

    /**
     * If true, the server is started.
     */
    private boolean isServerLaunched;

    /**
     * The query launcher that sends the queries to the server and receives the
     * responses.
     */
    private final QueryLauncher queryLauncher;

    /**
     * The path to the Ronin jar for launching the Ronin server.
     */
    private final String pathToRoninJar;

    /**
     * Constructs and initializes a Ronin Client with the address of the server
     * to communicate with and the listening port of the server.
     *
     * @param serverLocation the address of the server to communicate with
     * @param port the listening port of the server.
     * @param args the arguments given by the user in command line
     * @param pathToRoninJar the path to the Ronin jar for launching the Ronin
     * server
     */
    public RoninClient(String serverLocation, int port, String[] args, String pathToRoninJar) {
        this.serverLocation = serverLocation;
        this.port = port;
        RoninCommandLineConfigurationInfos roninConfig = MainUtils.getRoninCmdConfiguration(args);
        roninConfig.setSimulationMonitoredByServer(true);
        roninConfig.setPort(port);
        this.argsCmdLine = roninConfig.getCmdConfigLine();
        this.isConnected = false;
        this.isServerLaunched = false;
        this.pathToRoninJar = pathToRoninJar;
        this.queryLauncher = new QueryLauncher(this);
    }

    /**
     * Constructs and initializes a Ronin Client with the address of the server
     * to communicate with and the listening port of the server.
     *
     * @param serverLocation the address of the server to communicate with
     * @param port the listening port of the Ronin server
     * @param roninConfig the Ronin configuration parameters from command line
     * @param pathToRoninJar the path to the Ronin jar for launching the Ronin
     * server
     */
    public RoninClient(String serverLocation, int port, RoninCommandLineConfigurationInfos roninConfig, String pathToRoninJar) {
        this.serverLocation = serverLocation;
        this.port = port;
        roninConfig.setSimulationMonitoredByServer(true);
        roninConfig.setPort(port);
        this.argsCmdLine = roninConfig.getCmdConfigLine();
        this.isConnected = false;
        this.isServerLaunched = false;
        this.pathToRoninJar = pathToRoninJar;
        this.queryLauncher = new QueryLauncher(this);
    }

    /**
     * Returns the input stream of this Client.
     *
     * @return the input stream of this Client
     */
    public ObjectInputStream getInputStream() {
        return in;
    }

    /**
     * Returns the output stream of this Client.
     *
     * @return the output stream of this Client
     */
    public ObjectOutputStream getOutputStream() {
        return out;
    }

    /**
     * Connects this Client to a Ronin server and sets its streams to
     * communicate with it.
     *
     * @return true if we manage to connect to the server;false otherwise
     */
    public boolean startConnection() {
        try {
            if (isServerLaunched == false) {
                System.out.println("The server is not launched yet.");
                return false;
            }
            connectToServer();
            setIOStreams();
            return true;
        } catch (IOException e) {
            System.err.println("Error starting Client.");
            System.err.println(e);
            System.err.println(e.getCause());
            System.err.println(Arrays.toString(e.getStackTrace()));
        } catch (InterruptedException ex) {
            Logger.getLogger(RoninClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Launches a Ronin server.
     *
     * @return true if we manage to launch the server; false otherwise.
     */
    public boolean startServer() {
        try {
            String c = "java -cp " + pathToRoninJar + " ie.ucd.pel.ronin.main.Main " + argsCmdLine;
            Process p = Runtime.getRuntime().exec(c);
            if (PRINT_OUTPUT_SERVER) {
                printOuputFromServerProcess(p);
            }
            isServerLaunched = p.isAlive();
            return p.isAlive();
        } catch (IOException ex) {
            System.err.println("Error launching server on remote computer.");
            System.err.println(ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * Method used to print the output of the process of the Ronin server.
     *
     * @param p the process of the Ronin server
     * @throws IOException
     */
    private void printOuputFromServerProcess(Process p) throws IOException {
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

        // read the output from the command
        System.out.println("Here is the standard output of the command:\n");
        String s;
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
        }
    }

    /**
     * Sets the input/output streams of this Client to communicate with a Ronin
     * server.
     *
     * @throws IOException
     */
    private void setIOStreams() throws IOException {
        out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        out.flush();
        in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        System.out.println("\nI/O Stream is ready.");
    }

    /**
     * Connects this Client to a Ronin Server.
     *
     * @throws IOException
     */
    private void connectToServer() throws InterruptedException {
        System.out.println("\nConnecting...");
        int waiting = 0;
        String waitingMessage = "\rWaiting a server ";
        String pointWaitingMessage = "...";
        while (isConnected == false) {
            try {
                socket = new Socket(InetAddress.getByName(serverLocation), port);
                socket.setTcpNoDelay(true);
                isConnected = true;
            } catch (IOException ex) {
                System.out.print(waitingMessage + pointWaitingMessage.substring(0, waiting + 1));
                waiting++;
                waiting %= 3;
                sleep(1000);
            }
        }

        System.out.println("\nConnected to "
                + socket.getInetAddress().getHostName());
    }

    /**
     * Ends the call between this Client and the server by closing the streams
     * and by closing the socket.
     */
    public void endCall() {
        try {
            out.close();
            in.close();
            socket.close();
            isConnected = false;
            System.out.println("\nConnection closed");
        } catch (IOException ex) {
            System.err.println(ex.getCause());
        }
    }

    /**
     * Returns a new Query Launcher. We can add queries to it and the query
     * launcher can send all its queries in one time to the server. It will then
     * put the responses into the associated queries. If the communication with
     * the server is not done, we return null.
     *
     * @return a Query Launcher if the communication with the server is done;
     * null otherwise
     */
    public QueryLauncher createNewQueryLauncher() {
        if (!isConnected) {
            return null;
        }
        return new QueryLauncher(this);
    }

    /**
     * Sends a valid query to the server and returns the response of the server.
     * If the query is not valid, we do not send the Query to the Server and
     * return a ServerResponse with status failed.
     *
     * @param q the query to send to the server
     * @return the response of the server to the query; null if we didn't
     * receive a valid response or were not able to read the response.
     */
    private ServerResponse sendQuery(Query q) {
        queryLauncher.addQuery(q);
        queryLauncher.run();
        return q.getResponse();
    }

    /**
     * Sends a query to Ronin server to add a vehicle to its simulation.
     *
     * @param idVehicle the id of the vehicle to add
     * @param idVType the id the type of the vehicle to add
     * @param departureTime the departure time in seconds of the vehicle to add
     * @param routeEdgesIds the list of ids of the edges of the route of the
     * vehicle to add
     * @return the response of the server to the query; null if we didn't
     * receive a valid response or were not able to read the response.
     */
    public ServerResponse sendAddVehicleQuery(String idVehicle, String idVType, Double departureTime, Collection<String> routeEdgesIds) {
        Query q = new AddVehicleQuery(idVehicle, idVType, departureTime, routeEdgesIds);
        return sendQuery(q);
    }

    /**
     * Sends a query to Ronin server to remove a vehicle from its simulation.
     *
     * @param idVehicle the id of the vehicle to remove
     * @return the response of the server to the query; null if we didn't
     * receive a valid response or were not able to read the response.
     */
    public ServerResponse sendRemoveVehicleQuery(String idVehicle) {
        Query q = new RemoveVehicleQuery(idVehicle);
        return sendQuery(q);
    }

    /**
     * Sends a query to Ronin Server to process the next step of its simulation.
     *
     * @return the response of the server to the query; null if we didn't
     * receive a valid response or were not able to read the response.
     */
    public ProcessNextStepServerResponse sendProcessNextStepQuery() {
        Query q = new ProcessNextStepQuery();
        return (ProcessNextStepServerResponse) sendQuery(q);
    }

    /**
     * Sends a query to Ronin Server to stop and close the connection.
     *
     * @return the response of the server to the query; null if we didn't
     * receive a valid response or were not able to read the response.
     */
    public ServerResponse sendStopServerQuery() {
        Query q = new StopServerQuery();
        return sendQuery(q);
    }

}
