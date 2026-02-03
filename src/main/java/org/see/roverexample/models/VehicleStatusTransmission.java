/*****************************************************************
 SEE Tutorial - Tutorial projects demonstrating how to use the
 SEE HLA Starter Kit Framework.
 Copyright (c) 2026, Hridyanshu Aatreya - Modelling & Simulation
 Group (MSG) at Brunel University of London. All rights reserved.

 GNU Lesser General Public License (GNU LGPL).

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 3.0 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library.
 If not, see http://http://www.gnu.org/licenses/
 *****************************************************************/

package org.see.roverexample.models;

import org.apache.commons.geometry.euclidean.threed.Vector3D;
import org.see.roverexample.encoding.Vector3DCoder;
import org.see.skf.annotations.InteractionClass;
import org.see.skf.annotations.Parameter;
import org.see.skf.util.encoding.HLAunicodeStringCoder;

@InteractionClass(name = "HLAinteractionRoot.VehicleStatusTransmission")
public class VehicleStatusTransmission {
    @Parameter(name = "VehicleName", coder = HLAunicodeStringCoder.class)
    private String vehicleName;

    @Parameter(name = "Position", coder = Vector3DCoder.class)
    private Vector3D position;

    @Parameter(name = "Message", coder = HLAunicodeStringCoder.class)
    private String message;

    public VehicleStatusTransmission() {}

    public VehicleStatusTransmission(String vehicleName, Vector3D position, String message) {
        this.vehicleName = vehicleName;
        this.position = position;
        this.message = message;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public Vector3D getPosition() {
        return position;
    }

    public void setPosition(Vector3D position) {
        this.position = position;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
