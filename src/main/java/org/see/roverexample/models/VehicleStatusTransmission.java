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

    public VehicleStatusTransmission() {}

    public VehicleStatusTransmission(String vehicleName, Vector3D position) {
        this.vehicleName = vehicleName;
        this.position = position;
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
}
