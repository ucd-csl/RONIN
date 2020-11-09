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
package ie.ucd.pel.ronin.utils;

import ie.ucd.pel.ronin.model.Vehicle;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Come CACHARD
 *
 * Provides some utils methods for processing maps.
 */
public class MapUtils {

    /**
     * Inits a given map of double mapped to String with 0 for the given keys.
     *
     * @param map the map to init
     * @param keys the keys to add to the map
     */
    public static void initMap(Map<String, Double> map, Set<String> keys) {
        keys.stream().forEach((key) -> {
            map.put(key, 0.0);
        });
    }

    /**
     * Inits a given map of lists of elements mapped to String with empty
     * linkedList for the given keys.
     *
     * @param map the map to init
     * @param keys the keys to add to the map
     */
    public static void initVehiclesListMap(Map<String, List<Vehicle>> map, Set<String> keys) {
        keys.stream().forEach((key) -> {
            map.put(key, new LinkedList<>());
        });
    }

    /**
     * Adds a vehicle to a list of vehicles mapped on a given key.
     *
     * @param loads the map of lists of vehicles
     * @param v the vehicle to add
     * @param key the key to which is mapped the list of vehicles where to add
     * the vehicle
     */
    public static void addVehicleToVehiclesListMap(Map<String, List<Vehicle>> loads, Vehicle v, String key) {
        if (!loads.containsKey(key)) {
            loads.put(key, new LinkedList<>());
        }

        loads.get(key).add(v);
    }

    /**
     * Checks that a map of parameters contains all the given keys. Returns true
     * if the map contains all the keys; false otherwise.
     *
     * @param parameters the map we want to check the keys
     * @param keys the keys we want to find in the map
     * @return true if the map contains all the keys; false otherwise
     */
    public static boolean checkKeysPresentInMap(Map<String, String> parameters, String... keys) {
        for (String key : keys) {
            if (!parameters.containsKey(key)) {
                return false;
            }
        }
        return true;
    }

}
