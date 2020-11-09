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
public class VehicleTest {

    public VehicleTest() {
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
     * Test of addEdgeToRoute method, of class Vehicle.
     */
    @Test
    public void testAddEdgeToRoute() {
        Node start = new Node("8888", 45.05, 94.0);
        Node end = new Node("102", 55.01, 97.11);
        Edge e = new Edge("198182234#4", 35, 29.04, 90.00, 3, start, end);
        VehicleType vType = new VehicleType("vT1", 4.00, 90.0);
        Vehicle instance = new Vehicle("instance", 95.2, vType);

        boolean expResult = true;
        boolean result = instance.addEdgeToRoute(e);
        assertEquals(expResult, result);

        int length = instance.getRoute().size();
        Edge er = instance.getRoute().get(length - 1);
        assertEquals(e, er);
    }

    /**
     * Test of increaseTravelTime method, of class Vehicle.
     */
    @Test
    public void testIncreaseTravelTime() {
        VehicleType vType = new VehicleType("vT1", 4.00, 90.0);
        Vehicle instance = new Vehicle("instance", 95.2, vType);
        double timeToAddInSeconds = 9.0;
        instance.increaseTravelTime(timeToAddInSeconds);
        double expResult = 9.0;
        double result = instance.getTravelTime(1);
        assertEquals(expResult, result, 0.1);
    }

    /**
     * Test of increaseNbSlotsInSamePosition method, of class Vehicle.
     */
    @Test
    public void testIncreaseNbSlotsInSamePosition() {
        VehicleType vType = new VehicleType("vT1", 4.00, 90.0);
        Vehicle instance = new Vehicle("instance", 95.2, vType);
        int number = 10;
        instance.increaseNbSlotsInSamePosition(number);
        double expResult = 10;
        assertEquals(expResult, instance.getNbSlotsInSamePosition(), 0.1);
    }

    /**
     * Test of decreaseNbSlotsInSamePosition method, of class Vehicle.
     */
    @Test
    public void testDecreaseNbSlotsInSamePosition1() {
        VehicleType vType = new VehicleType("vT1", 4.00, 90.0);
        Vehicle instance = new Vehicle("instance", 95.2, vType);
        int number = 10;
        instance.decreaseNbSlotsInSamePosition(number);
        double expResult = 0;
        assertEquals(expResult, instance.getNbSlotsInSamePosition(), 0.1);
    }

    /**
     * Test of decreaseNbSlotsInSamePosition method, of class Vehicle.
     */
    @Test
    public void testDecreaseNbSlotsInSamePosition2() {
        VehicleType vType = new VehicleType("vT1", 4.00, 90.0);
        Vehicle instance = new Vehicle("instance", 95.2, vType);
        instance.increaseNbSlotsInSamePosition(12);
        int number = 10;
        instance.decreaseNbSlotsInSamePosition(number);
        double expResult = 2;
        assertEquals(expResult, instance.getNbSlotsInSamePosition(), 0.1);
    }

    /**
     * Test of isArrived method, of class Vehicle.
     */
    @Test
    public void testIsArrived1() {
        VehicleType vType = new VehicleType("vT1", 4.00, 90.0);
        Vehicle instance = new Vehicle("instance", 95.2, vType);
        boolean expResult = true;
        boolean result = instance.isArrived();
        assertEquals(expResult, result);
    }

    /**
     * Test of isArrived method, of class Vehicle.
     */
    @Test
    public void testIsArrived2() {
        VehicleType vType = new VehicleType("vT1", 4.00, 90.0);
        Vehicle instance = new Vehicle("instance", 95.2, vType);
        instance.addEdgeToRoute(new Edge("11", 0, 90.0, 30.0, 0, null, null));
        boolean expResult = true;
        boolean result = instance.isArrived();
        assertEquals(expResult, result);
    }

    /**
     * Test of isArrived method, of class Vehicle.
     */
    @Test
    public void testIsArrived3() {
        VehicleType vType = new VehicleType("vT1", 4.00, 90.0);
        Vehicle instance = new Vehicle("instance", 95.2, vType);
        instance.addEdgeToRoute(new Edge("11", 0, 90.0, 30.0, 0, null, null));
        instance.setPosition(1);
        boolean expResult = true;
        boolean result = instance.isArrived();
        assertEquals(expResult, result);
    }

    /**
     * Test of isArrived method, of class Vehicle.
     */
    @Test
    public void testIsArrived4() {
        VehicleType vType = new VehicleType("vT1", 4.00, 90.0);
        Vehicle instance = new Vehicle("instance", 95.2, vType);
        instance.addEdgeToRoute(new Edge("11", 0, 90.0, 30.0, 0, null, null));
        instance.addEdgeToRoute(new Edge("12", 0, 90.0, 30.0, 0, null, null));
        boolean expResult = false;
        boolean result = instance.isArrived();
        assertEquals(expResult, result);
    }

    /**
     * Test of setPosition method, of class Vehicle.
     */
    @Test
    public void testSetPosition1() {
        VehicleType vType = new VehicleType("vT1", 4.00, 90.0);
        Vehicle instance = new Vehicle("instance", 95.2, vType);
        int number = 10;
        instance.setPosition(number);
        double expResult = 0;
        assertEquals(expResult, instance.getPosition(), 0.1);
    }

    /**
     * Test of setPosition method, of class Vehicle.
     */
    @Test
    public void testSetPosition2() {
        VehicleType vType = new VehicleType("vT1", 4.00, 90.0);
        Vehicle instance = new Vehicle("instance", 95.2, vType);
        instance.addEdgeToRoute(new Edge("51", 0, 30.0, 30.0, 0, null, null));
        instance.addEdgeToRoute(new Edge("52", 0, 30.0, 30.0, 0, null, null));
        instance.addEdgeToRoute(new Edge("53", 0, 30.0, 30.0, 0, null, null));
        instance.addEdgeToRoute(new Edge("54", 0, 30.0, 30.0, 0, null, null));
        int number = 2;
        instance.setPosition(number);
        double expResult = 2;
        assertEquals(expResult, instance.getPosition(), 0.1);
    }

    /**
     * Test of increasePosition method, of class Vehicle.
     */
    @Test
    public void testIncreasePosition1() {
        VehicleType vType = new VehicleType("vT1", 4.00, 90.0);
        Vehicle instance = new Vehicle("instance", 95.2, vType);
        int number = 10;
        instance.increasePosition(number);
        double expResult = 0;
        assertEquals(expResult, instance.getPosition(), 0.1);
    }

    /**
     * Test of increasePosition method, of class Vehicle.
     */
    @Test
    public void testIncreasePosition2() {
        VehicleType vType = new VehicleType("vT1", 4.00, 90.0);
        Vehicle instance = new Vehicle("instance", 95.2, vType);
        instance.addEdgeToRoute(new Edge("51", 0, 30.0, 30.0, 0, null, null));
        instance.addEdgeToRoute(new Edge("52", 0, 30.0, 30.0, 0, null, null));
        instance.addEdgeToRoute(new Edge("53", 0, 30.0, 30.0, 0, null, null));
        instance.addEdgeToRoute(new Edge("54", 0, 30.0, 30.0, 0, null, null));
        int number = 2;
        instance.increasePosition(number);
        double expResult = 2;
        assertEquals(expResult, instance.getPosition(), 0.1);
    }

}
