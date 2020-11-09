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
package ie.ucd.pel.ronin.filesreader;

import ie.ucd.pel.ronin.filesreader.sumofilesreader.SumoFilesReader;
import ie.ucd.pel.ronin.filesreader.sumofilesreader.SumoConfigInformation;
import ie.ucd.pel.ronin.model.Edge;
import ie.ucd.pel.ronin.model.Network;
import ie.ucd.pel.ronin.model.Node;
import ie.ucd.pel.ronin.model.TimeConfiguration;
import ie.ucd.pel.ronin.model.Vehicle;
import ie.ucd.pel.ronin.model.VehicleType;
import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Come CACHARD
 */
public class SumoFilesReaderTest {

    public SumoFilesReaderTest() {
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
     * Test of readNetwork method, of class SumoFilesReader.
     */
    @Test
    public void testReadNetwork() {
        boolean isTestOk = true;
        StringBuilder sbNet = new StringBuilder();
        sbNet.append("test");
        sbNet.append(File.separator);
        sbNet.append("ie");
        sbNet.append(File.separator);
        sbNet.append("ucd");
        sbNet.append(File.separator);
        sbNet.append("pel");
        sbNet.append(File.separator);
        sbNet.append("ronin");
        sbNet.append(File.separator);
        sbNet.append("resources");
        sbNet.append(File.separator);
        sbNet.append("test1.net.xml");

        StringBuilder sbRou = new StringBuilder();
        sbRou.append("test");
        sbRou.append(File.separator);
        sbRou.append("ie");
        sbRou.append(File.separator);
        sbRou.append("ucd");
        sbRou.append(File.separator);
        sbRou.append("pel");
        sbRou.append(File.separator);
        sbRou.append("ronin");
        sbRou.append(File.separator);
        sbRou.append("resources");
        sbRou.append(File.separator);
        sbRou.append("test1.rou.xml");

        String netpath = sbNet.toString();
        String roupath = sbRou.toString();
        SumoConfigInformation sumoInfos = new SumoConfigInformation(roupath, netpath, "", "");
        SumoFilesReader instance = new SumoFilesReader(sumoInfos);
        Network n = instance.readNetwork();
        System.out.println("network edges : " + n.getEdges().size());

        if (n.getEdges().size() != 26) {
            isTestOk = false;
        }
        if (n.getNodes().size() != 19) {
            isTestOk = false;
        }
        if (n.getVehicleTypes().size() != 2) {
            isTestOk = false;
        }

        VehicleType vType = n.getVehicleType("CarA");
        if (vType == null) {
            isTestOk = false;
        } else {
            if (vType.getLength() != 5.0) {
                isTestOk = false;
            }
            if (vType.getMaxSpeed() != 50.00) {
                isTestOk = false;
            }
        }

        Vehicle v = n.getVehicleFromLoadedVehicles("veh1");
        if (v == null) {
            isTestOk = false;
        } else {
            if (v.getLength() != 5.0) {
                isTestOk = false;
            }
            if (v.getMaxSpeed() != 50.00) {
                isTestOk = false;
            }
            if (v.getRoute().size() != 6) {
                isTestOk = false;
            }
            Edge er = v.getRoute().get(3);
            if (er == null) {
                isTestOk = false;
            } else {
                if (!er.getId().equalsIgnoreCase("L10")) {
                    isTestOk = false;
                }
                if (er.getCapacity() != 738) {
                    isTestOk = false;
                }
            }
        }

        Edge e = n.getEdge("L17");
        if (e == null) {
            isTestOk = false;
        } else {
            if (e.getCapacity() != 738) {
                isTestOk = false;
            }
            if (e.getLength() != 987.15) {
                isTestOk = false;
            }
            if (e.getSpeedLimit() != 13.89) {
                isTestOk = false;
            }
            Node endNode = e.getEndNode();
            Node startNode = e.getStartNode();
            if (endNode == null) {
                isTestOk = false;
            } else if (!"3".equals(endNode.getId())) {
                isTestOk = false;
            }
            if (startNode == null) {
                isTestOk = false;
            } else {
                if (!"4".equals(startNode.getId())) {
                    isTestOk = false;
                }
                if (startNode.getX() != 3000.00) {
                    isTestOk = false;
                }
                if (startNode.getY() != 0.0) {
                    isTestOk = false;
                }
                if (startNode.getIngoingEdges().size() != 2) {
                    isTestOk = false;
                }
                if (startNode.getOutgoingEdges().size() != 2) {
                    isTestOk = false;
                }
            }

        }

        assertEquals(isTestOk, true);
    }

    /**
     * Test of readSimulationTimeConfiguration method, of class SumoFilesReader.
     */
    @Test
    public void testReadSimulationTimeConfiguration() {
        StringBuilder sbNet = new StringBuilder();
        sbNet.append("test");
        sbNet.append(File.separator);
        sbNet.append("ie");
        sbNet.append(File.separator);
        sbNet.append("ucd");
        sbNet.append(File.separator);
        sbNet.append("pel");
        sbNet.append(File.separator);
        sbNet.append("ronin");
        sbNet.append(File.separator);
        sbNet.append("resources");
        sbNet.append(File.separator);
        sbNet.append("test1.net.xml");

        StringBuilder sbRou = new StringBuilder();
        sbRou.append("test");
        sbRou.append(File.separator);
        sbRou.append("ie");
        sbRou.append(File.separator);
        sbRou.append("ucd");
        sbRou.append(File.separator);
        sbRou.append("pel");
        sbRou.append(File.separator);
        sbRou.append("ronin");
        sbRou.append(File.separator);
        sbRou.append("resources");
        sbRou.append(File.separator);
        sbRou.append("test1.rou.xml");

        StringBuilder sbCfg = new StringBuilder();
        sbCfg.append("test");
        sbCfg.append(File.separator);
        sbCfg.append("ie");
        sbCfg.append(File.separator);
        sbCfg.append("ucd");
        sbCfg.append(File.separator);
        sbCfg.append("pel");
        sbCfg.append(File.separator);
        sbCfg.append("ronin");
        sbCfg.append(File.separator);
        sbCfg.append("resources");
        sbCfg.append(File.separator);
        sbCfg.append("test1.sumocfg");

        String netpath = sbNet.toString();
        String roupath = sbRou.toString();
        String cfgpath = sbCfg.toString();
        SumoConfigInformation sumoInfos = new SumoConfigInformation(roupath, netpath, cfgpath, "");
        SumoFilesReader instance = new SumoFilesReader(sumoInfos);

        double expResult = 21600.00;
        TimeConfiguration result = instance.readSimulationTimeConfiguration();
        assertEquals(expResult, result.getBeginTime(), 0.1);
    }

}
