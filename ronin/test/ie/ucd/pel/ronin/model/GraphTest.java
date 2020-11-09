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
package ie.ucd.pel.ronin.model;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Come CACHARD
 */
public class GraphTest {

    public GraphTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of addEdge method, of class Graph.
     */
    @Test
    public void testAddEdge() {
        Node start = new Node("8888", 45.05, 94.0);
        Node end = new Node("102", 55.01, 97.11);
        Edge e = new Edge("198182234#4", 35, 29.04, 90.00, 3, start, end);
        Graph instance = new Graph();
        boolean expResult = true;
        boolean result = instance.addEdge(e);
        assertEquals(expResult, result);

        Edge eg = instance.getEdge("198182234#4");
        assertEquals(e, eg);
    }

    /**
     * Test of addNode method, of class Graph.
     */
    @Test
    public void testAddNode() {
        Node n = new Node("8888", 45.05, 94.0);
        Graph instance = new Graph();
        boolean expResult = true;
        boolean result = instance.addNode(n);
        assertEquals(expResult, result);

        Node ng = instance.getNode("8888");
        assertEquals(n, ng);
    }

}
