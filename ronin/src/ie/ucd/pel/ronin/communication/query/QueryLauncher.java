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
package ie.ucd.pel.ronin.communication.query;

import ie.ucd.pel.ronin.communication.serverresponse.ServerResponse;
import ie.ucd.pel.ronin.communication.socket.RoninClient;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Come CACHARD
 *
 * Class used to send queries to the Ronin server, and received the responses
 * that are then stored client.getInputStream() the queries by this Object.
 */
public class QueryLauncher {

    /**
     * The queries to send to the Ronin Server.
     */
    private final List<Query> queries;

    /**
     * The client of the Ronin server that sends queries.
     */
    private final RoninClient client;

    /**
     * Constructs and initializes a Query Launcher with a Ronin Client.
     *
     * @param client the Ronin Client that wants to interact with a Ronin Server
     */
    public QueryLauncher(RoninClient client) {
        this.queries = new LinkedList<>();
        this.client = client;
    }

    /**
     * Adds a query to the list of queries to send to the Ronin server. If the
     * query is not valid, we do not add it and we set its response to Failed
     * status.
     *
     * @param q the query to add
     * @return true if the query was added; false otherwise
     */
    public boolean addQuery(Query q) {
        if (q == null || q.checkQueryParameters() == false) {
            return false;
        }
        queries.add(q);
        return true;
    }

    /**
     * Adds a collection of queries to the list of queries to send to the Ronin
     * Server. If a query is not valid, we do not add it and we set its response
     * to Failed status.
     *
     * @param queriesCollection the collection of queries to add
     */
    public void addQueries(Collection<Query> queriesCollection) {
        Iterator<Query> iter = queriesCollection.iterator();
        while (iter.hasNext()) {
            Query q = iter.next();
            addQuery(q);
        }
    }

    /**
     * Removes a query to the list of queries to send to the Ronin server.
     *
     * @param q the query to remove
     */
    public void removeQuery(Query q) {
        queries.remove(q);
    }

    /**
     * Clears the list of queries of this QueryLauncher.
     */
    public void clearQueries() {
        queries.clear();
    }

    /**
     * Writes a list of queries to the outpustream for the Ronin server.
     *
     * @param queries the list of queries to send to the server
     */
    private void writeQueriesListToOutputStream(List<Query> queries) {
        try {
            client.getOutputStream().writeUnshared(queries);
            client.getOutputStream().flush();
        } catch (IOException ex) {
            System.err.println("Error write queries: " + ex);
            System.err.println(Arrays.toString(ex.getStackTrace()));
            clearQueries();
            client.endCall();
        }
    }

    /**
     * Sends the list of queries of this QueryLauncher to the RoninServer, gets
     * the server responses and put them to the associated queries. The list of
     * queries of this QueryLauncher is then cleared.
     */
    public void run() {
        if (!queries.isEmpty()) {
            writeQueriesListToOutputStream(queries);
            getServerResponses();
            clearQueries();
        }
    }

    /**
     * Gets the server responses for the queries of this QueryLauncher and put
     * them to the associated queries.
     */
    private void getServerResponses() {
        try {
            List<ServerResponse> responses = (List<ServerResponse>) client.getInputStream().readUnshared();
            if (responses.size() != queries.size()) {
                System.err.println("responses size : " + responses.size());
                System.err.println("queries size : " + queries.size());
                System.err.println("Error : we didn't receive the same number of responses than the number of queries.");
                return;
            }
            Iterator<Query> queryIter = queries.iterator();
            Iterator<ServerResponse> responseIter = responses.iterator();
            while (queryIter.hasNext()) {
                ServerResponse response = responseIter.next();
                Query query = queryIter.next();
                query.setResponse(response);
            }
        } catch (ClassNotFoundException ex) {
            System.err.println("\nInvalid object received");
            clearQueries();
        } catch (java.io.EOFException ex) {
            System.err.println(ex);
            System.err.println(Arrays.toString(ex.getStackTrace()));

        } catch (IOException ex) {
            System.err.println("\nConnection with Ronin Server broken.");
            System.err.println(ex);
            System.err.println(Arrays.toString(ex.getStackTrace()));
            clearQueries();
            client.endCall();
        }
    }

}
