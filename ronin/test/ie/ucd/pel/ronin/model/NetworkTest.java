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

import java.util.LinkedList;
import java.util.List;
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
public class NetworkTest {

    public NetworkTest() {
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
     * Test of addVehicleType method, of class Network.
     */
    @Test
    public void testAddVehicleType() {
        VehicleType vType = new VehicleType("vT1", 4.00, 90.0);
        Network instance = new Network();
        boolean expResult = true;
        boolean result = instance.addVehicleType(vType);
        assertEquals(expResult, result);

        VehicleType vTypen = instance.getVehicleType("vT1");
        assertEquals(vType, vTypen);
    }

    /**
     * Test of addNode method, of class Network.
     */
    @Test
    public void testAddNode() {
        Node n = new Node("8888", 45.05, 94.0);
        Network instance = new Network();
        boolean expResult = true;
        boolean result = instance.addNode(n);
        assertEquals(expResult, result);

        Node nn = instance.getNode("8888");
        assertEquals(n, nn);
    }

    /**
     * Test of addEdge method, of class Network.
     */
    @Test
    public void testAddEdge() {
        Node start = new Node("8888", 45.05, 94.0);
        Node end = new Node("102", 55.01, 97.11);
        Edge e = new Edge("198182234#4", 35, 29.04, 90.00, 3, start, end);
        Network instance = new Network();
        boolean expResult = true;
        boolean result = instance.addEdge(e);
        assertEquals(expResult, result);

        Edge en = instance.getEdge("198182234#4");
        assertEquals(e, en);
    }

    /**
     * Test of addVehicleToLoadedVehicles method, of class Network.
     */
    @Test
    public void testAddVehicleToAddToSimulationList1() {
        VehicleType vType = new VehicleType("vT1", 4.00, 90.0);
        Edge e = new Edge("e", 0, 10.0, 10.0, 0, null, null);
        Vehicle v = new Vehicle("v1", 760.2, vType);
        v.addEdgeToRoute(e);
        Network instance = new Network();
        boolean expResult = true;
        boolean result = instance.addVehicleToLoadedVehicles(v);
        assertEquals(expResult, result);
        instance.flushLoadedVehicles();
        Vehicle vn = instance.getVehicleFromNotDepartedVehicles("v1");
        assertEquals(v, vn);
    }

    /**
     * Test of addVehicleToLoadedVehicles method, of class Network.
     */
    @Test
    public void testAddVehicleToAddToSimulationList2() {
        Edge e = new Edge("e", 0, 10.0, 10.0, 0, null, null);
        VehicleType vType = new VehicleType("vT1", 4.00, 90.0);
        Vehicle v = new Vehicle("v1", 760.2, vType);
        v.addEdgeToRoute(e);
        Network instance = new Network();
        List<Vehicle> expResult = new LinkedList<>();
        expResult.add(v);
        instance.addVehicleToLoadedVehicles(v);
        assertEquals(expResult, instance.getLoadedVehicles());
    }

    /**
     * Test of addVehicleToArrivedVehicles method, of class Network.
     */
    @Test
    public void testAddVehicleToArrivedVehicles() {
        VehicleType vType = new VehicleType("vT1", 4.00, 90.0);
        Edge e = new Edge("e", 0, 10.0, 10.0, 0, null, null);
        Vehicle v = new Vehicle("v1", 760.2, vType);
        v.addEdgeToRoute(e);
        //Edge e = new Edge("edge", 0, 30., 30., 0, null, null);
        //v.addEdgeToRoute(e);
        Network instance = new Network();
        boolean expResult = true;
        boolean result = instance.addVehicleToArrivedVehicles(v);
        assertEquals(expResult, result);

        Vehicle vn = instance.getVehicleFromArrivedVehicles("v1");
        assertEquals(v, vn);
    }

    /**
     * Test of flushLoadedVehicles method, of class Network.
     */
    @Test
    public void testFlushLoadedVehicles() {
        Edge e = new Edge("e", 0, 10.0, 10.0, 0, null, null);
        VehicleType vType = new VehicleType("vT1", 4.00, 90.0);
        Vehicle v3 = new Vehicle("v1", 761., vType);
        Vehicle v2 = new Vehicle("v2", 762., vType);
        Vehicle v1 = new Vehicle("v3", 763., vType);
        v1.addEdgeToRoute(e);
        v2.addEdgeToRoute(e);
        v3.addEdgeToRoute(e);
        Network instance = new Network();
        instance.addVehicleToLoadedVehicles(v1);
        instance.addVehicleToLoadedVehicles(v2);
        instance.addVehicleToLoadedVehicles(v3);
        instance.flushLoadedVehicles();

        List<Vehicle> expResult = new LinkedList<>();
        expResult.add(v3);
        expResult.add(v2);
        expResult.add(v1);
        assertEquals(expResult, instance.getNotDepartedVehicles());

        List<Vehicle> expResult2 = new LinkedList<>();
        expResult2.add(v1);
        expResult2.add(v2);
        expResult2.add(v3);
        assertEquals(expResult2, instance.getCurrentStepLoadedVehicles());

        assertTrue(instance.getLoadedVehicles().isEmpty());
    }

    /**
     * Test of addVehicleToRemoveFromSimulationList method, of class Network.
     */
    @Test
    public void testAddVehicleToRemoveFromSimulationList() {
        Network instance = new Network();
        instance.addVehicleToRemoveFromSimulationList("v2");
        instance.addVehicleToRemoveFromSimulationList("v3");

        List<String> expResult1 = new LinkedList<>();
        expResult1.add("v2");
        expResult1.add("v3");
        assertEquals(expResult1, instance.getVehiclesToRemoveFromSimulation());

    }

    /**
     * Test of flushVehiclesToRemoveFromSimulationList method, of class Network.
     */
    @Test
    public void testFlushVehiclesToRemoveFromSimulationList() {
        VehicleType vType = new VehicleType("vT1", 4.00, 90.0);
        Edge e = new Edge("e", 0, 10.0, 10.0, 0, null, null);
        Vehicle v1 = new Vehicle("v1", 761., vType);
        Vehicle v2 = new Vehicle("v2", 762., vType);
        Vehicle v3 = new Vehicle("v3", 763., vType);
        Vehicle v4 = new Vehicle("v4", 764., vType);
        v1.addEdgeToRoute(e);
        v2.addEdgeToRoute(e);
        v3.addEdgeToRoute(e);
        v4.addEdgeToRoute(e);
        Network instance = new Network();
        instance.addVehicleToArrivedVehicles(v2);
        instance.addVehicleToLoadedVehicles(v1);
        instance.addVehicleToLoadedVehicles(v3);
        instance.addVehicleToLoadedVehicles(v4);
        instance.flushLoadedVehicles();

        instance.addVehicleToRemoveFromSimulationList("v2");
        instance.addVehicleToRemoveFromSimulationList("v3");
        instance.flushVehiclesToRemoveFromSimulationList();

        List<Vehicle> expResult1 = new LinkedList<>();
        expResult1.add(v1);
        expResult1.add(v4);
        assertEquals(expResult1, instance.getNotDepartedVehicles());

        List<Vehicle> expResult2 = new LinkedList<>();
        assertEquals(expResult2, instance.getArrivedVehicles());

        assertTrue(instance.getVehiclesToRemoveFromSimulation().isEmpty());
    }

    /**
     * Test of removeVehicle method, of class Network.
     */
    @Test
    public void testRemoveVehicle1() {
        VehicleType vType = new VehicleType("vT1", 4.00, 90.0);
        Edge e = new Edge("e", 0, 10.0, 10.0, 0, null, null);
        Vehicle v1 = new Vehicle("v1", 761., vType);
        Vehicle v2 = new Vehicle("v2", 762., vType);
        Vehicle v3 = new Vehicle("v3", 763., vType);
        Vehicle v4 = new Vehicle("v4", 764., vType);
        v1.addEdgeToRoute(e);
        v2.addEdgeToRoute(e);
        v3.addEdgeToRoute(e);
        v4.addEdgeToRoute(e);
        Network instance = new Network();
        instance.addVehicleToLoadedVehicles(v1);
        instance.addVehicleToLoadedVehicles(v2);
        instance.addVehicleToLoadedVehicles(v3);
        instance.addVehicleToLoadedVehicles(v4);
        instance.flushLoadedVehicles();

        assertEquals(4, instance.getAllVehiclesOfSimulation().size());

        instance.removeVehicle("v2");

        List<Vehicle> expResult = new LinkedList<>();
        expResult.add(v1);
        expResult.add(v3);
        expResult.add(v4);
        assertEquals(3, instance.getAllVehiclesOfSimulation().size());
        assertEquals(expResult, instance.getNotDepartedVehicles());
        assertFalse(instance.getAllVehiclesOfSimulation().containsKey("v2"));
    }

    /**
     * Test of removeVehicle method, of class Network.
     */
    @Test
    public void testRemoveVehicle2() {
        VehicleType vType = new VehicleType("vT1", 4.00, 90.0);
        Edge e = new Edge("e", 0, 10.0, 10.0, 0, null, null);
        Vehicle v1 = new Vehicle("v1", 761., vType);
        Vehicle v2 = new Vehicle("v2", 762., vType);
        Vehicle v3 = new Vehicle("v3", 763., vType);
        Vehicle v4 = new Vehicle("v4", 764., vType);
        v1.addEdgeToRoute(e);
        v2.addEdgeToRoute(e);
        v3.addEdgeToRoute(e);
        v4.addEdgeToRoute(e);
        Network instance = new Network();
        instance.addVehicleToArrivedVehicles(v2);
        instance.addVehicleToLoadedVehicles(v1);
        instance.addVehicleToLoadedVehicles(v3);
        instance.addVehicleToLoadedVehicles(v4);
        instance.flushLoadedVehicles();

        instance.removeVehicle("v2");

        List<Vehicle> expResult1 = new LinkedList<>();
        expResult1.add(v1);
        expResult1.add(v3);
        expResult1.add(v4);
        assertEquals(expResult1, instance.getNotDepartedVehicles());

        List<Vehicle> expResult2 = new LinkedList<>();
        assertEquals(expResult2, instance.getArrivedVehicles());
    }

    /**
     * Test of removeVehicle method, of class Network.
     */
    @Test
    public void testRemoveVehicle3() {
        VehicleType vType = new VehicleType("vT1", 4.00, 90.0);
        Edge e = new Edge("e", 0, 10.0, 10.0, 0, null, null);
        Vehicle v1 = new Vehicle("v1", 761., vType);
        Vehicle v2 = new Vehicle("v2", 762., vType);
        Vehicle v3 = new Vehicle("v3", 763., vType);
        Vehicle v4 = new Vehicle("v4", 764., vType);
        v1.addEdgeToRoute(e);
        v2.addEdgeToRoute(e);
        v3.addEdgeToRoute(e);
        v4.addEdgeToRoute(e);
        Network instance = new Network();
        instance.addVehicleToArrivedVehicles(v1);
        instance.addVehicleToRunningVehicles(v2);
        instance.addVehicleToNotDepartedVehicles(v3);
        instance.addVehicleToLoadedVehicles(v4);

        instance.removeVehicle("v2");

        List<Vehicle> expResult1 = new LinkedList<>();
        expResult1.add(v1);
        assertEquals(expResult1, instance.getArrivedVehicles());

        assertTrue(instance.getRunningVehicles().isEmpty());

        List<Vehicle> expResult3 = new LinkedList<>();
        expResult3.add(v3);
        assertEquals(expResult3, instance.getNotDepartedVehicles());

        List<Vehicle> expResult4 = new LinkedList<>();
        expResult4.add(v4);
        assertEquals(expResult4, instance.getLoadedVehicles());
    }

    /**
     * Test of removeVehicle method, of class Network.
     */
    @Test
    public void testRemoveVehicle4() {
        VehicleType vType = new VehicleType("vT1", 4.00, 90.0);
        Vehicle v1 = new Vehicle("v1", 761., vType);
        Vehicle v2 = new Vehicle("v2", 762., vType);
        Vehicle v3 = new Vehicle("v3", 763., vType);
        Vehicle v4 = new Vehicle("v4", 764., vType);
        Edge e = new Edge("e", 0, 10.0, 10.0, 0, null, null);
        v1.addEdgeToRoute(e);
        v2.addEdgeToRoute(e);
        v3.addEdgeToRoute(e);
        v4.addEdgeToRoute(e);
        Network instance = new Network();
        instance.addVehicleToArrivedVehicles(v1);
        instance.addVehicleToLoadedVehicles(v2);
        instance.addVehicleToNotDepartedVehicles(v3);
        instance.addVehicleToLoadedVehicles(v4);

        instance.removeVehicle("v2");

        List<Vehicle> expResult1 = new LinkedList<>();
        expResult1.add(v1);
        assertEquals(expResult1, instance.getArrivedVehicles());

        List<Vehicle> expResult3 = new LinkedList<>();
        expResult3.add(v3);
        assertEquals(expResult3, instance.getNotDepartedVehicles());

        List<Vehicle> expResult4 = new LinkedList<>();
        expResult4.add(v4);
        assertEquals(expResult4, instance.getLoadedVehicles());
    }

    /**
     * Test of removeVehicle method, of class Network.
     */
    @Test
    public void testRemoveVehicle5() {
        VehicleType vType = new VehicleType("vT1", 4.00, 90.0);
        Vehicle v1 = new Vehicle("v1", 761., vType);
        Vehicle v2 = new Vehicle("v2", 762., vType);
        Vehicle v3 = new Vehicle("v3", 763., vType);
        Vehicle v4 = new Vehicle("v4", 764., vType);
        Edge e = new Edge("e", 0, 10.0, 10.0, 0, null, null);
        v1.addEdgeToRoute(e);
        v2.addEdgeToRoute(e);
        v3.addEdgeToRoute(e);
        v4.addEdgeToRoute(e);
        Network instance = new Network();
        instance.addVehicleToArrivedVehicles(v1);
        instance.addVehicleToArrivedVehicles(v2);
        instance.addVehicleToNotDepartedVehicles(v3);
        instance.addVehicleToLoadedVehicles(v4);

        instance.removeVehicle("v2");

        List<Vehicle> expResult1 = new LinkedList<>();
        expResult1.add(v1);
        assertEquals(expResult1, instance.getArrivedVehicles());

        List<Vehicle> expResult3 = new LinkedList<>();
        expResult3.add(v3);
        assertEquals(expResult3, instance.getNotDepartedVehicles());

        List<Vehicle> expResult4 = new LinkedList<>();
        expResult4.add(v4);
        assertEquals(expResult4, instance.getLoadedVehicles());
    }

    /**
     * Test of removeVehicle method, of class Network.
     */
    @Test
    public void testRemoveVehicle6() {
        VehicleType vType = new VehicleType("vT1", 4.00, 90.0);
        Vehicle v1 = new Vehicle("v1", 761., vType);
        Vehicle v2 = new Vehicle("v2", 762., vType);
        Vehicle v3 = new Vehicle("v3", 763., vType);
        Vehicle v4 = new Vehicle("v4", 764., vType);
        Edge e = new Edge("e", 0, 10.0, 10.0, 0, null, null);
        v1.addEdgeToRoute(e);
        v2.addEdgeToRoute(e);
        v3.addEdgeToRoute(e);
        v4.addEdgeToRoute(e);
        Network instance = new Network();
        instance.addVehicleToArrivedVehicles(v1);
        instance.addVehicleToNotDepartedVehicles(v2);
        instance.addVehicleToNotDepartedVehicles(v3);
        instance.addVehicleToLoadedVehicles(v4);

        instance.removeVehicle("v2");

        List<Vehicle> expResult1 = new LinkedList<>();
        expResult1.add(v1);
        assertEquals(expResult1, instance.getArrivedVehicles());

        List<Vehicle> expResult3 = new LinkedList<>();
        expResult3.add(v3);
        assertEquals(expResult3, instance.getNotDepartedVehicles());

        List<Vehicle> expResult4 = new LinkedList<>();
        expResult4.add(v4);
        assertEquals(expResult4, instance.getLoadedVehicles());
    }

    /**
     * Test of updateDepartedVehiclesForCurrentTimeStep method, of class
     * Network.
     */
    @Test
    public void testUpdateDepartedVehiclesForCurrentTimeStep() {
        VehicleType vType = new VehicleType("vT1", 4.00, 90.0);
        Vehicle v1 = new Vehicle("v1", 761.0, vType);
        Vehicle v2 = new Vehicle("v2", 762.0, vType);
        Vehicle v3 = new Vehicle("v3", 763.0, vType);
        Vehicle v4 = new Vehicle("v4", 764.0, vType);
        Edge e = new Edge("e", 0, 10.0, 10.0, 0, null, null);
        v1.addEdgeToRoute(e);
        v2.addEdgeToRoute(e);
        v3.addEdgeToRoute(e);
        v4.addEdgeToRoute(e);
        Network instance = new Network();
        instance.addVehicleToArrivedVehicles(v2);
        instance.addVehicleToLoadedVehicles(v1);
        instance.addVehicleToLoadedVehicles(v3);
        instance.addVehicleToLoadedVehicles(v4);
        instance.flushLoadedVehicles();

        instance.updateDepartedVehiclesForCurrentTimeStep(761.);

        List<Vehicle> expResult1 = new LinkedList<>();
        expResult1.add(v3);
        expResult1.add(v4);
        assertEquals(expResult1, instance.getNotDepartedVehicles());

        List<Vehicle> expResult2 = new LinkedList<>();
        expResult2.add(v1);
        assertEquals(expResult2, instance.getRunningVehicles());
        assertEquals(expResult2, instance.getCurrentStepDepartedVehicles());

        List<Vehicle> expResult3 = new LinkedList<>();
        expResult3.add(v2);
        assertEquals(expResult3, instance.getArrivedVehicles());

    }

}
