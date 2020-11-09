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

import ie.ucd.pel.ronin.communication.query.ProcessNextStepQuery;
import ie.ucd.pel.ronin.communication.query.Query;
import ie.ucd.pel.ronin.communication.query.QueryLauncher;
import ie.ucd.pel.ronin.communication.query.StopServerQuery;
import ie.ucd.pel.ronin.communication.query.vehiclequery.AddVehicleQuery;
import ie.ucd.pel.ronin.communication.query.vehiclequery.GetAllVehiclesCountQuery;
import ie.ucd.pel.ronin.communication.query.vehiclequery.GetAllVehiclesQuery;
import ie.ucd.pel.ronin.communication.query.vehiclequery.RemoveVehicleQuery;
import ie.ucd.pel.ronin.communication.serverresponse.ServerResponse;
import ie.ucd.pel.ronin.main.RoninCommandLineConfigurationInfos;
import ie.ucd.pel.ronin.model.Vehicle;
import ie.ucd.pel.ronin.utils.MainUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Come CACHARD
 *
 * Class used for testing the connection between a Ronin Client and a Ronin
 * server.
 */
public class TCPClient {

    public static void main(String[] args) throws InterruptedException {
        RoninCommandLineConfigurationInfos roninConf = MainUtils.getRoninCmdConfiguration(args);
        String localhost = "127.0.0.1";
        int port = 12345;
        String pathToRoninJar = "dist" + File.separator + "ronin.jar";
        RoninClient client = new RoninClient(localhost, port, roninConf, pathToRoninJar);
        client.startServer();
        client.startConnection();
        
        example2(client);
        client.endCall();

    }
    
    private static void example(RoninClient client){
        List<String> route = new LinkedList<>();
        route.add("-100243994");
        
        System.out.println(client.sendProcessNextStepQuery().getDescription());
        System.out.println(client.sendProcessNextStepQuery().getDescription());
        System.out.println(client.sendProcessNextStepQuery().getDescription());
        System.out.println(client.sendProcessNextStepQuery().getDescription());
        System.out.println(client.sendProcessNextStepQuery().getDescription());
        System.out.println(client.sendProcessNextStepQuery().getDescription());

        System.out.println("is simulation finished : " + client.sendProcessNextStepQuery().isSimulationFinished());
        //System.out.println(client.sendAddVehicleQuery("veh12", "passenger_P_14_1", 78.0).getDescription());
        //System.out.println(client.sendProcessNextStepQuery().getDescription());
        //System.out.println(client.sendProcessNextStepQuery().getDescription());
        System.out.println(client.sendProcessNextStepQuery().getDescription());
        System.out.println(client.sendProcessNextStepQuery().getDescription());
        System.out.println(client.sendProcessNextStepQuery().getDescription());
        System.out.println(client.sendProcessNextStepQuery().getDescription());
        System.out.println(client.sendProcessNextStepQuery().getDescription());
        System.out.println(client.sendAddVehicleQuery("veh12", "passenger_P_14_1", 78.0,route).getDescription());
        System.out.println(client.sendAddVehicleQuery("veh12", "passenger_P_14_1", 78.0,route).getDescription());
        System.out.println(client.sendAddVehicleQuery("veh12", "passenger_P_14_1", 78.0,route).getDescription());
        System.out.println(client.sendAddVehicleQuery("veh12", "passenger_P_14_1", 78.0,route).getDescription());
        System.out.println(client.sendAddVehicleQuery("veh12", "passenger_P_14_1", 78.0,route).getDescription());
        System.out.println(client.sendAddVehicleQuery("veh12", "passenger_P_14_1", 78.0,route).getDescription());
        System.out.println(client.sendAddVehicleQuery("veh12", "passenger_P_14_1", 78.0,route).getDescription());
        System.out.println(client.sendAddVehicleQuery("veh12", "passenger_P_14_1", 78.0,route).getDescription());
        System.out.println(client.sendRemoveVehicleQuery("veh12").getDescription());
        System.out.println(client.sendRemoveVehicleQuery("veh12").getDescription());
        System.out.println(client.sendRemoveVehicleQuery("veh12").getDescription());
        System.out.println(client.sendRemoveVehicleQuery("veh12").getDescription());
        System.out.println(client.sendRemoveVehicleQuery("veh12").getDescription());
        System.out.println(client.sendRemoveVehicleQuery("veh12").getDescription());
        System.out.println(client.sendRemoveVehicleQuery("veh12").getDescription());
        System.out.println(client.sendRemoveVehicleQuery("veh12").getDescription());
        System.out.println(client.sendAddVehicleQuery("veh12", "passenger_P_14_1", 78.0,route).getDescription());
         

        
        System.out.println(client.sendAddVehicleQuery("veh12", "passenger_P_14_1", 78.0, route).getDescription());
        System.out.println(client.sendAddVehicleQuery("veh12", "passenger_P_14_1", 78.0, route).getDescription());
        System.out.println(client.sendAddVehicleQuery("veh12", "passenger_P_14_1", 78.0, route).getDescription());
        System.out.println(client.sendAddVehicleQuery("veh12", "passenger_P_14_1", 78.0, route).getDescription());
        System.out.println(client.sendAddVehicleQuery("veh12", "passenger_P_14_1", 78.0, route).getDescription());
        System.out.println(client.sendAddVehicleQuery("veh12", "passenger_P_14_1", 78.0, route).getDescription());
        System.out.println(client.sendAddVehicleQuery("veh12", "passenger_P_14_1", 78.0, route).getDescription());
        System.out.println(client.sendAddVehicleQuery("veh12", "passenger_P_14_1", 78.0, route).getDescription());

        //System.out.println(client.sendRemoveVehicleQuery("veh12").getDescription());
        System.out.println(client.sendProcessNextStepQuery().getDescription());
        System.out.println(client.sendStopServerQuery().getDescription());

        /*ProcessNextStepQuery q0 = new ProcessNextStepQuery();
        ProcessNextStepQuery q1 = new ProcessNextStepQuery();
        AddVehicleQuery q2 = new AddVehicleQuery("veh12", "passenger_P_14_1", 78.0);
        //RemoveVehicleQuery q3 = new RemoveVehicleQuery("veh12");
        ProcessNextStepQuery q3 = new ProcessNextStepQuery();
        ProcessNextStepQuery q4 = new ProcessNextStepQuery();
        Query q5 = new StopServerQuery();
        List<Query> l = new ArrayList<>();
        l.add(q0);
        l.add(q1);
        l.add(q2);
        //l.add(q3);
        //l.add(q4);
        System.out.println("\n\n We verify that we do not have responses yet for queries :\n");
        for (Query qo : l) {
            System.out.println(qo.getResponse().getClass());
            System.out.println(qo.getResponse().getDescription());
        }
        System.out.println("\n\n We send the queries to the server :\n");

        QueryLauncher ql = client.createNewQueryLauncher();
        ql.addQueries(l);
        ql.run();

        QueryLauncher ql2 = client.createNewQueryLauncher();
        ql2.addQuery(q3);
        ql2.run();

        ql.addQuery(q4);
        ql.addQuery(q5);
        ql.run();
        l.stream().forEach((q) -> {
            System.out.println(q.getResponse().getDescription());
        });
        System.out.println("is simulation finished : " + q1.getResponse().isSimulationFinished());
        System.out.println("q3 desc : "+q3.getResponse().getDescription());
        System.out.println("q4 desc : "+q4.getResponse().getDescription());
        System.out.println("q5 desc : "+q5.getResponse().getDescription());

         */
    }

    
    private static void example2(RoninClient client){
        List<String> route = new LinkedList<>();
        String routeEdges = "-151503319#4 -151503319#3 -151503319#2 -151503319#1 -151503319#0 -202527577#0 -202527576#0 4229593#0 154520784 4229595 4229596#0 204177812#0 4400142#0 4400142#0-AddedOffRampEdge 4400137#0 4400137#0-AddedOffRampEdge 25875643 20661575 4400132 128417958#1 47335015 145128808#1-AddedOnRampEdge 145128808#1 133859192-AddedOnRampEdge 133859192 133859192-AddedOffRampEdge 125777198#1 125777198#2-AddedOnRampEdge 125777198#2 125777198#2-AddedOffRampEdge 38326263#1 38326263#1-AddedOffRampEdge 38326263#3 133860033-AddedOnRampEdge 133860033 205968483#0 132614489 32013796 132624375-AddedOnRampEdge 132624375 132624375-AddedOffRampEdge 132624385 132624388 193706950 208913376 132624378 206716755 206716758 146373054#0 146373054#0-AddedOffRampEdge 208913386#1 209682818#0 209682801 26803141#0 209682810 209682805 209682816#1 26657764 26657763 209682819 183453995 209682813 183453994#0 183453992 209682823#1 209985249 40849433 209985251#0 209985251#1 209985250#0 150466972#0 -147061808#3 25152423#0 25152423#1 -25152423#1";
        for(String edge:routeEdges.split(" ")){
            route.add(edge);
        }

        ProcessNextStepQuery q0 = new ProcessNextStepQuery();
        ProcessNextStepQuery q1 = new ProcessNextStepQuery();
        AddVehicleQuery q21 = new AddVehicleQuery("veh10", "passenger_P_14_1", 78.0,route);
        AddVehicleQuery q22 = new AddVehicleQuery("veh11", "passenger_P_14_1", 78.0,route);
        AddVehicleQuery q23 = new AddVehicleQuery("veh12", "passenger_P_14_1", 78.0,route);
        RemoveVehicleQuery q31 = new RemoveVehicleQuery("veh10");
        RemoveVehicleQuery q32 = new RemoveVehicleQuery("veh11");
        RemoveVehicleQuery q33 = new RemoveVehicleQuery("veh12");
        ProcessNextStepQuery q4 = new ProcessNextStepQuery();
        Query q5 = new StopServerQuery();
        List<Query> l = new ArrayList<>();
        l.add(q0);
        l.add(q1);
        l.add(q21);
        l.add(q22);
        l.add(q23);

        QueryLauncher ql = client.createNewQueryLauncher();
        ql.addQueries(l);
        ql.run();
        testCountVehicles(ql, 3);
        

        QueryLauncher ql2 = client.createNewQueryLauncher();
        ql2.addQuery(q31);
        ql2.run();
        //client.sendProcessNextStepQuery();
        testCountVehicles(ql, 2);
        
        ql2.addQuery(q32);
        ql2.addQuery(q33);
        ql2.run();
        checkQuery(q32);
        checkQuery(q33);
        //client.sendProcessNextStepQuery();
        testGetAllVehicles(ql2);
        testCountVehicles(ql, 0);
        
        ql.addQuery(q4);
        ql.addQuery(q5);
        ql.run();


    }

    private static void testCountVehicles(QueryLauncher ql,int expectedValue){
        GetAllVehiclesCountQuery q = new GetAllVehiclesCountQuery();
        ql.addQuery(q);
        ql.run();
        if(q.getResponse().getCount()!=expectedValue){
            System.err.println("ERROR : there should be " +expectedValue+" vehicles but found "+q.getResponse().getCount());
        }else{
            System.out.println("OK");
        }
    }
    
    private static List<Vehicle> testGetAllVehicles(QueryLauncher ql){
        GetAllVehiclesQuery q = new GetAllVehiclesQuery();
        ql.addQuery(q);
        ql.run();
        System.out.println("\nList vehicles:");
        for (Vehicle v: q.getResponse().getVehiclesList()){
            System.out.println(v.getId());
        }
        System.out.println(" ");
        return q.getResponse().getVehiclesList();
    }
    
    private static boolean checkQuery(Query q){
        if(q.getResponse().getStatus()!=ServerResponse.StatusResponse.STATUS_SUCCESS){
            System.err.println(q.getResponse().getDescription());
            return false;
        }else{
            System.out.println(q.getResponse().getDescription());
            return true;
        }
    }
    
}
