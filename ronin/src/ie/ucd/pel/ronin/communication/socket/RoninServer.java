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

import ie.ucd.pel.ronin.communication.query.Query;
import ie.ucd.pel.ronin.communication.serverresponse.ServerResponse;
import ie.ucd.pel.ronin.main.RoninCommandLineConfigurationInfos;
import ie.ucd.pel.ronin.simulation.Simulation;
import ie.ucd.pel.ronin.filesreader.sumofilesreader.SumoConfigFileReader;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Come CACHARD
 *
 * Server that runs a simulation and listen a client to do actions on this
 * simulation. Only one client at a time can communicate with this server.
 */
public class RoninServer {

    /**
     * The port where is listening the server.
     */
    private final int port;

    /**
     * The number of concurrent clients.
     */
    private static final int BACKLOG = 1;

    /**
     * The outpustream of this server.
     */
    private ObjectOutputStream out;

    /**
     * The inputstream of this server.
     */
    private ObjectInputStream in;

    /**
     * the serverSocket where this server accepts new conections of clients.
     */
    private ServerSocket serverSocket;

    /**
     * The socket used by this Server to communicate with its Client.
     */
    private Socket socket;

    /**
     * The simulation hosted by this server.
     */
    private final Simulation simulation;

    /**
     * if true, we have to stop this RoninServer.
     */
    private boolean stopServer;

    /**
     * Constructs and initializes a Ronin server by initializing its simulation.
     * The Simulation is initialized by reading the sumo files, it will then
     * wait for query from user to process the steps, add new vehicles, remove
     * vehicles.
     *
     * @param roninConf the configuration of the simulation set by command line
     */
    public RoninServer(RoninCommandLineConfigurationInfos roninConf) {
        this.simulation = new Simulation(roninConf);
        SumoConfigFileReader sumoReader = new SumoConfigFileReader(roninConf.getSumocfgFilePath());
        if (roninConf.getPort() < 0) {
            this.port = sumoReader.readRoninServerPort();
        } else {
            this.port = roninConf.getPort();
        }
        stopServer = false;
    }

    /**
     * Returns the simulation hosted by this RoninServer.
     *
     * @return the simulation hosted by this RoninServer
     */
    public Simulation getSimulation() {
        return simulation;
    }

    /**
     * Sets the new value of stopServer. If true, we stop the server after
     * executing all the queries in the queries list of the stop server query.
     *
     * @param stopServer the new value of stopServer : if true, we stop the
     * server after executing all the queries in the queries list of the stop
     * server query.
     */
    public void setStopServer(boolean stopServer) {
        this.stopServer = stopServer;
    }

    /**
     * Starts this Ronin server by launching the simulation and makes it
     * listening to new Clients.
     */
    public void start() {
        try {
            serverSocket = new ServerSocket(port, BACKLOG);
        } catch (IOException ex) {
            System.err.println("Error starting server.");
            System.err.println(ex);
            return;
        }

        work();
    }

    /**
     * The main loop of this server : it accepts a Client, then listens to it
     * until the Client leaves or that the server is asked to stop. If the
     * client leaves without asking the server to stop, the server waits for
     * another Client.
     */
    private void work() {
        while (!serverSocket.isClosed()) {
            try {
                connectToClient();
                setIOStreams();
                processClientQueries();
            } catch (IOException ex) {
                System.err.println("Error working server.");
                System.err.println(ex);
            } finally {
                stopServer();
            }
        }
    }

    /**
     * Connects this Server to a new Client.
     *
     * @throws IOException
     */
    private void connectToClient() throws IOException {
        System.out.println("\nWaiting for client...");
        socket = serverSocket.accept();
        socket.setTcpNoDelay(true);
        System.out.println("Connection accepted from "
                + socket.getInetAddress().getHostName());
    }

    /**
     * Configures the IO streams of the socket of this server for communicating
     * with its client.
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
     * Listens to the queries of the Client and executes them.
     */
    private void processClientQueries() {
        while (!socket.isClosed()) {
            try {
                List<Query> queries = (List<Query>) in.readObject();
                List<ServerResponse> responses = new LinkedList<>();
                Iterator<Query> iter = queries.iterator();
                while (iter.hasNext()) {
                    Query q = iter.next();
                    try {
                        responses.add(q.execute(this));
                    } catch (Exception e) {
                        System.err.println(e);
                        System.err.println(Arrays.toString(e.getStackTrace()));
                    }

                }
                sendResponses(responses);
                if (stopServer) {
                    stopServer();
                }
            } catch (ClassNotFoundException e) {
                System.err.println("\nInvalid object received");
            } catch (IOException e) {
                System.err.println("Error server process queries : " + e);
                endClientCall();
            }
        }
    }

    /**
     * Sends a list of responses to a list of queries to the Client.
     *
     * @param responses the list of responses to send to the Client
     */
    private void sendResponses(List<ServerResponse> responses) {
        try {
            out.writeUnshared(responses);
            out.flush();
        } catch (IOException ex) {
            System.err.println("Error: " + ex);
            endClientCall();
        }
    }

    /**
     * Closes the connection with the Client.
     */
    public void endClientCall() {
        System.out.println("\nConnection closed");
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ex) {
            System.err.println("Error: " + ex);
        }
    }

    /**
     * Stops this server by closing the socket listening to new Clients and by
     * closing the connection with current client.
     */
    public void stopServer() {
        endClientCall();
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException ex) {
                System.err.println("Error : " + ex);
                return;
            }
        }
        System.exit(0);
    }

}
