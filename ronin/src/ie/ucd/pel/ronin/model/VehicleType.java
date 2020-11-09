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

import java.util.Objects;

/**
 *
 * @author Come CACHARD
 *
 * Class used in order to instantiate Vehicle from SUMO files, using the SUMO
 * object vType.
 */
public class VehicleType {

    /**
     * The id of this VehicleType.
     */
    private final String id;

    /**
     * The length in meter of the Vehicle belonging to this VehicleType.
     */
    private final double length;

    /**
     * The maximum speed in m/s a Vehicle belonging to this VehicleType can
     * drive.
     */
    private final double maxSpeed;

    /**
     * Constructs and initializes a VehicleType with the specified properties.
     *
     * @param id the id of the newly constructed VehicleType
     * @param length the length in meter of the newly constructed VehicleType
     * @param maxSpeed the maximum speed in m/s of the newly constructed
     * VehicleType
     */
    public VehicleType(String id, double length, double maxSpeed) {
        this.id = id;
        this.length = length;
        this.maxSpeed = maxSpeed;
    }

    /**
     * Returns the id of this VehicleType.
     *
     * @return the id of this VehicleType
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the length in meter of this VehicleType.
     *
     * @return the length in meter of this VehicleType
     */
    public double getLength() {
        return length;
    }

    /**
     * Returns the maximum speed in m/s of this VehicleType.
     *
     * @return the maximum speed in m/s of this VehicleType
     */
    public double getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * Returns the hashcode for this instance of VehicleType.
     *
     * @return the hascode for this VehicleType.
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.id);
        return hash;
    }

    /**
     * Determines wether or not two vehicle types are equal. Two instances of
     * VehicleType are equal if they have the same id.
     *
     * @param obj an object to be compared with this VehicleType.
     * @return true if the object to be compared is an instance of VehicleType
     * and has the same id; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VehicleType other = (VehicleType) obj;
        return Objects.equals(this.id, other.id);
    }

}
