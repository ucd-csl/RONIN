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
public class EdgeTest {

    public EdgeTest() {
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
     * Test of isOverloaded method, of class Edge.
     */
    @Test
    public void testIsOverloaded1() {
        double load = 36.;
        Edge instance = new Edge("198182234#4", 35, 29.04, 90.00, 3, null, null);
        boolean expResult = true;
        boolean result = instance.isOverloaded((int) load);
        assertEquals(expResult, result);
    }

    /**
     * Test of isOverloaded method, of class Edge.
     */
    @Test
    public void testIsOverloaded2() {
        double load = 34.;
        Edge instance = new Edge("198182234#4", 35, 29.04, 90.00, 3, null, null);
        boolean expResult = false;
        boolean result = instance.isOverloaded((int) load);
        assertEquals(expResult, result);
    }

    /**
     * Test of getTravelTime method, of class Edge.
     */
    @Test
    public void testGetTravelTime1() {
        int load = 10;
        Edge instance = new Edge("198182234#4", 35, 90.00, 30.0, 3, null, null);
        double expResult = 3.00299875052;
        double result = instance.getTravelTime(load);
        assertEquals(expResult, result, 0.1);
    }

    /**
     * Test of getTravelTime method, of class Edge.
     */
    @Test
    public void testGetTravelTime2() {
        int load = 36;
        Edge instance = new Edge("198182234#4", 35, 90.00, 30.0, 3, null, null);
        double expResult = 3.45;
        double result = instance.getTravelTime(load);
        assertEquals(expResult, result, 0.1);
    }

    /**
     * Test of getMaxTravelTime method, of class Edge.
     */
    @Test
    public void testGetMaxTravelTime() {
        Edge instance = new Edge("198182234#4", 35, 90.00, 30.0, 3, null, null);
        System.out.println("min travel time : " + instance.getMinTravelTime());
        double expResult = 3.45;
        double result = instance.getMaxTravelTime();
        assertEquals(expResult, result, 0.1);
    }

    /**
     * Test of equals method, of class Edge.
     */
    @Test
    public void testEquals1() {
        Object obj = new Edge("198182234#4", 35, 29.04, 90.00, 3, null, null);
        Edge instance = new Edge("198182234#4", 35, 29.04, 90.00, 3, null, null);
        boolean expResult = true;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class Edge.
     */
    @Test
    public void testEquals2() {
        Object obj = new Edge("198182234#7", 35, 29.04, 90.00, 3, null, null);
        Edge instance = new Edge("198182234#4", 35, 29.04, 90.00, 3, null, null);
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class Edge.
     */
    @Test
    public void testEquals3() {
        Object obj = new Node("198182234#4", 45.05, 94.0);
        Edge instance = new Edge("198182234#4", 35, 29.04, 90.00, 3, null, null);
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }

    /**
     * Test of getMeanDensity method, of class Edge.
     */
    @Test
    public void testGetMeanDensity() {
        double nbSteps = 10;
        Edge instance = new Edge("198182234#4", 35, 2000., 90.00, 3, null, null);
        instance.increaseNbTotVehicles(400);

        double expResult = 20.0;
        double result = instance.getMeanDensity(nbSteps);
        assertEquals(expResult, result, 0.0);

    }

    /**
     * Test of getMeanTravelTime method, of class Edge.
     */
    @Test
    public void testGetMeanTravelTime() {
        double nbSteps = 5;
        Edge instance = new Edge("198182234#4", 35, 2000., 90.00, 3, null, null);
        instance.increaseTravelTimeTotal(40);
        instance.increaseTravelTimeTotal(40);
        instance.increaseTravelTimeTotal(40);
        instance.increaseTravelTimeTotal(40);
        instance.increaseTravelTimeTotal(40);

        double expResult = 40.0;
        double result = instance.getMeanTravelTime(nbSteps);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getMeanTravelTime method, of class Edge.
     */
    @Test
    public void testGetMeanTravelTime2() {
        double nbSteps = 10;
        Edge instance = new Edge("198182234#4", 35, 2000., 100.00, 3, null, null);
        instance.increaseTravelTimeTotal(40);
        instance.increaseTravelTimeTotal(40);
        instance.increaseTravelTimeTotal(40);
        instance.increaseTravelTimeTotal(40);
        instance.increaseTravelTimeTotal(40);

        double expResult = (200 + 5 * 20) / nbSteps;
        double result = instance.getMeanTravelTime(nbSteps);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getMeanSpeed method, of class Edge.
     */
    @Test
    public void testGetMeanSpeed() {
        double nbSteps = 10;
        Edge instance = new Edge("198182234#4", 35, 2000., 90.00, 3, null, null);
        for (int i = 0; i < nbSteps; i++) {
            instance.increaseTravelTimeTotal(40);
        }

        double expResult = 50.;
        double result = instance.getMeanSpeed(nbSteps);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getAverageTrafficVolume method, of class Edge.
     */
    @Test
    public void testGetAverageTrafficVolume() {
        double nbSteps = 10;
        Edge instance = new Edge("198182234#4", 35, 2000., 90.00, 3, null, null);
        instance.increaseNbTotVehicles(400);
        for (int i = 0; i < nbSteps; i++) {
            instance.increaseTravelTimeTotal(40);
        }

        double expResult = 3.6 * 20 * 50;
        double result = instance.getAverageTrafficVolume(nbSteps);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of increaseNbTotVehicles method, of class Edge.
     */
    @Test
    public void testIncreaseNbTotVehicles() {
        double number = 5;
        Edge instance = new Edge("198182234#4", 35, 29.04, 90.00, 3, null, null);
        instance.increaseNbTotVehicles(number);

        assertEquals(number, instance.getNbTotVehicles(), 0.1);
    }

    /**
     * Test of increaseTravelTimeTotal method, of class Edge.
     */
    @Test
    public void testIncreaseTravelTimeTotal() {
        double travelTime = 45;
        double nbSteps = 1;
        Edge instance = new Edge("198182234#4", 35, 29.04, 90.00, 3, null, null);
        instance.increaseTravelTimeTotal(travelTime);

        assertEquals(travelTime, instance.getTravelTimeTotal(nbSteps), 0.);
    }

    /**
     * Test of increaseArrivedVehicles method, of class Edge.
     */
    @Test
    public void testIncreaseArrivedVehicles() {
        double number = 5;
        Edge instance = new Edge("198182234#4", 35, 29.04, 90.00, 3, null, null);
        instance.increaseArrivedVehicles(number);

        assertEquals(number, instance.getArrivedVehicles(), 0.);
    }

}
